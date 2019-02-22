package dbd;

import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * Perk
 *
 * @author GneHeHe (2018)
 *
 */
public final class Perk implements Comparable<Perk> {

    // Name of Perk
    private String name;
    // Weight of Perk
    private int weight;
    // Side of Perk
    private String side;
    // Icon of Perk (as String)
    private String icon_string;
    // Icon of Perk (as JLabel)
    private JLabel lab_img_small;
    private JLabel lab_img_medium;
    private JLabel lab_img_large;
    // Size of Icon Pictures
    private final int size_small = 55;
    private final int size_medium = 75;
    private final int size_large = 200;

    /**
     * Constructor
     *
     * @param name
     * @param weight
     * @param side
     * @param icon
     */
    public Perk(String name, int weight, String side, String icon) {
        this.setName(name);
        this.setSide(side);
        this.setWeight(weight);
        this.lab_img_small = new JLabel("", SwingConstants.CENTER);
        this.lab_img_medium = new JLabel("", SwingConstants.CENTER);
        this.lab_img_large = new JLabel("", SwingConstants.CENTER);
        try {
            this.setIconPicture(icon);
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
     * Get Perk Icon as JLabel
     *
     * @param size
     * @return
     */
    public JLabel getIconImage(int size) {
        switch (size) {
            case 1:
                return lab_img_small;
            case 2:
                return lab_img_medium;
            case 3:
                return lab_img_large;
            default:
                return new JLabel(this.name);
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
    private void setIconPicture(String s_icon) throws IOException {
        // Try to set given Icon Picture
        this.icon_string = s_icon;
        String path = "icons_perks/" + s_icon + ".png";
        if (this.getClass().getResourceAsStream(path) != null) {
            // Set Icons in JLabel
            this.lab_img_small.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_small, this.size_small)));
            this.lab_img_medium.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_medium, this.size_medium)));
            this.lab_img_large.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_large, this.size_large)));
            // Set Name for Tooltip
            this.lab_img_small.setName(this.name);
            this.lab_img_medium.setName(this.name);
            this.lab_img_large.setName(this.name);
        } else {
            // Try to use default Icon Picture
            s_icon = "iconPerks_default";
            System.err.println("# WARNING: Icon File '" + path + "' was not found for Perk '" + this.name + "' => using default Icon File ('" + s_icon + "') from data directory");
            path = "icons_perks/" + s_icon + ".png";
            if (this.getClass().getResourceAsStream(path) != null) {
                // Set Default Icon in JLabel
                this.lab_img_small.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_small, this.size_small)));
                this.lab_img_medium.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_medium, this.size_medium)));
                this.lab_img_large.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_large, this.size_large)));
                // Set Name for Tooltip
                this.lab_img_small.setName(this.name);
                this.lab_img_medium.setName(this.name);
                this.lab_img_large.setName(this.name);
            } else {
                System.err.println("# WARNING: Both expected and default Icon Files were not found for Perk '" + this.name + "' => Exit");
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

    /**
     * Is Side Correct ?
     *
     * @param side
     * @return
     */
    public boolean checkSide(String side) {
        return this.side.equals(side);
    }

    /**
     * Compare two Perks
     *
     * @param p
     * @return
     */
    @Override
    public int compareTo(Perk p) {
        return this.name.compareTo(p.name);
    }

    /**
     * toString() Method
     *
     * @return
     */
    @Override
    public String toString() {
        return this.name;
    }

}
