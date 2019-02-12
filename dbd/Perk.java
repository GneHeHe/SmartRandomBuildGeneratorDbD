package dbd;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;

/**
 *
 * @author GneHeHe (2018)
 */
public final class Perk {

    // Name of the Perk
    private String name;
    // Weight of the Perk
    private int weight;
    // Side the Perk
    private String side;
    // Icon of the Perk (as String)
    private String icon_string;
    // Icon of the Perk (as ImageIcon)
    private ImageIcon icon_img_small;
    private ImageIcon icon_img_large;
    // Sizes of ImageIcon
    private final int size_small = 60;
    private final int size_large = 220;

    /**
     * Default Constructor
     *
     */
    public Perk() {
        this.name = "";
        this.weight = 0;
        this.side = "";
        this.icon_string = "";
        this.icon_img_small = null;
        this.icon_img_large = null;
    }

    /**
     * Constructor
     *
     * @param name
     * @param weight
     * @param side
     * @param icon
     */
    public Perk(String name, int weight, String side, String icon) {
        setName(name);
        setSide(side);
        setWeight(weight);
        try {
            setIcon(icon);
        } catch (IOException ex) {
            System.err.println("ERROR while creating Perk " + name);
            System.err.println(ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Get Perk Name
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set Perk Name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Perk Side
     *
     * @return
     */
    public String getSide() {
        return this.side;
    }

    /**
     * Set Perk Side
     *
     * @param side
     */
    public void setSide(String side) {
        // Only 2 Sides are Available
        if (side.equals("Killer") || side.equals("Survivor")) {
            this.side = side;
        } else {
            System.err.println("# ERROR: This side " + side + " is wrong (expected value = 'Killer' or 'Survivor'");
            System.exit(0);
        }
    }

    /**
     * Get Perk Icon as ImageIcon
     *
     * @param getlarge
     * @return
     */
    public ImageIcon getIconImage(boolean getlarge) {
        if (getlarge) {
            return this.icon_img_large;
        } else {
            return this.icon_img_small;
        }
    }

    /**
     * Get Perk Icon as String
     *
     * @return
     */
    public String getIconString() {
        return this.icon_string;
    }

    /**
     * Set Perk Icon
     *
     * @param s_icon
     * @throws java.io.IOException
     */
    public void setIcon(String s_icon) throws IOException {
        this.icon_string = s_icon;
        this.icon_img_small = null;
        this.icon_img_large = null;
        String path = "icons/" + s_icon + ".png";
        InputStream is1 = this.getClass().getResourceAsStream(path);
        // Any way to reset the InputStream instead of reading it twice in different Objects (functions mark(), reset() don't work) ?
        InputStream is2 = this.getClass().getResourceAsStream(path);
        if (is1 != null) {
            this.icon_img_small = new ImageIcon(Tools.resizePicture(is1, this.size_small, this.size_small));
            this.icon_img_large = new ImageIcon(Tools.resizePicture(is2, this.size_large, this.size_large));
        } else {
            System.err.println("# WARNING: Icon File '" + path + "' was not found for Perk '" + this.name + "' => using default Icon File ('iconPerks_default.png') from data directory");
            path = "data/iconPerks_default.png";
            is1 = this.getClass().getResourceAsStream(path);
            is2 = this.getClass().getResourceAsStream(path);
            if (is1 != null) {
                this.icon_img_small = new ImageIcon(Tools.resizePicture(is1, this.size_small, this.size_small));
                this.icon_img_large = new ImageIcon(Tools.resizePicture(is2, this.size_large, this.size_large));
            } else {
                System.err.println("# WARNING: Both expected and default Icon Files were not found for Perk '" + this.name + "' => exit");
                System.exit(0);
            }
        }
    }

    /**
     * Get Perk Weight
     *
     * @return
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Set Perk Weight
     *
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Display Perk as String
     *
     * @param detail
     * @return
     */
    public String show(boolean detail) {
        if (detail) {
            return "# Perk = '" + this.name + "' | Side = " + this.side + " | Icon = " + this.icon_string + " | Weight = " + this.getWeight();
        } else {
            return "# Perk = '" + this.name + "' | Side = " + this.side + " | Weight = " + this.getWeight();
        }
    }

}
