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
    private String s_icon;
    // Icon of the Perk (as ImageIcon)
    private ImageIcon img_icon;

    /**
     * Default Constructor
     *
     */
    public Perk() {
        this.name = "";
        this.weight = 0;
        this.side = "";
        this.s_icon = "";
        this.img_icon = null;
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
            System.out.println(ex.getMessage());
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
            System.out.println("# ERROR: This side " + side + " is wrong (expected value = 'Killer' or 'Survivor'");
            System.exit(0);
        }
    }

    /**
     * Get Perk Icon as ImageIcon
     *
     * @return
     */
    public ImageIcon getIconImage() {
        return this.img_icon;
    }

    /**
     * Get Perk Icon as String
     *
     * @return
     */
    public String getIconString() {
        return this.s_icon;
    }

    /**
     * Set Perk Icon
     *
     * @param s_icon
     * @throws java.io.IOException
     */
    public void setIcon(String s_icon) throws IOException {
        this.s_icon = s_icon;
        this.img_icon = null;
        String path = "icons/" + s_icon + ".png";
        InputStream is = this.getClass().getResourceAsStream(path);
        if (is != null) {
            this.img_icon = new ImageIcon(Tools.resizePicture(is, 60, 60));
        } else {
            System.out.println("# WARNING: " + path + " not found !");
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
     * @return
     */
    public String show() {
        return "# Name = " + this.name + " | Side = " + this.side + " | Icon = " + this.s_icon + " | Weight = " + this.getWeight();
    }

}
