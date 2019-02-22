package dbd;

import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * Character
 *
 * @author GneHeHe (2018)
 *
 */
public final class Character implements Comparable<Character> {

    // Name of Character
    private String name;
    // Side of Character
    private String side;
    // Icon of Character (as String)
    private String icon_string;
    // Icon of Character (as JLabel)
    private JLabel lab_img_small;
    private JLabel lab_img_large;
    // Size of Icon Pictures
    private final int size_small = 55;
    private final int size_large = 175;

    /**
     * Constructor
     *
     * @param name
     * @param side
     */
    public Character(String name, String side) {
        this.setName(name);
        this.setSide(side);
        this.lab_img_small = new JLabel("", SwingConstants.CENTER);
        this.lab_img_large = new JLabel("", SwingConstants.CENTER);
        // Set Default Icon
        String icon = "";
        if (this.side.equals("Survivor")) {
            icon = "iconHelpLoading_survivor";
        } else if (this.side.equals("Killer")) {
            icon = "iconHelpLoading_killer";
        }
        try {
            this.setIconPicture(icon);
        } catch (IOException ex) {
            System.err.println("ERROR while creating Character " + name);
            System.err.println(ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Constructor
     *
     * @param name
     * @param side
     * @param icon
     */
    public Character(String name, String side, String icon) {
        this.setName(name);
        this.setSide(side);
        this.lab_img_small = new JLabel("", SwingConstants.CENTER);
        this.lab_img_large = new JLabel("", SwingConstants.CENTER);
        try {
            this.setIconPicture(icon);
        } catch (IOException ex) {
            System.err.println("ERROR while creating Character " + name);
            System.err.println(ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Get Character Name
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set Character Name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Character Side
     *
     * @return
     */
    public String getSide() {
        return this.side;
    }

    /**
     * Set Character Side
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
     * Get Character Icon as JLabel
     *
     * @param size
     * @return
     */
    public JLabel getIconImage(int size) {
        switch (size) {
            case 1:
                return lab_img_small;
            case 2:
                return lab_img_large;
            default:
                return new JLabel(this.name, SwingConstants.CENTER);
        }
    }

    /**
     * Get Character Icon as String
     *
     * @return
     */
    public String getIconString() {
        return this.icon_string;
    }

    /**
     * Set Character Icon
     *
     * @param s_icon
     * @throws java.io.IOException
     */
    private void setIconPicture(String s_icon) throws IOException {
        // Try to set given Icon Picture
        this.icon_string = s_icon;
        String path = "icons_char/" + s_icon + ".png";
        if (this.getClass().getResourceAsStream(path) != null) {
            // Set Icons in JLabel
            this.lab_img_small.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_small, this.size_small)));
            this.lab_img_large.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_large, this.size_large)));
            // Set Name for Tooltip
            this.lab_img_small.setName(this.name);
            this.lab_img_large.setName(this.name);
        } else {
            // Try to use default Icon Picture
            if (side.equals("Survivor")) {
                s_icon = "iconHelpLoading_survivor";
            } else if (side.equals("Killer")) {
                s_icon = "iconHelpLoading_killer";
            }
            //System.err.println("# WARNING: Icon File '" + path + "' was not found for Character '" + this.name + "' => using default Icon File ('" + s_icon + "') from data directory");
            path = "icons_char/" + s_icon + ".png";
            if (this.getClass().getResourceAsStream(path) != null) {
                // Set Default Icon in JLabel
                this.lab_img_small.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_small, this.size_small)));
                this.lab_img_large.setIcon(new ImageIcon(Tools.resizePicture(path, this.size_large, this.size_large)));
                // Set Name for Tooltip
                this.lab_img_small.setName(this.name);
                this.lab_img_large.setName(this.name);
            } else {
                System.err.println("# WARNING: Both expected and default Icon Files were not found for Character '" + this.name + "' => Exit");
                System.exit(0);
            }
        }
    }

    /**
     * Display Character as String
     *
     * @param detail
     * @return
     */
    public String show(boolean detail) {
        if (detail) {
            return "# Character = '" + this.name + "' | Side = " + this.side + " | Icon = " + this.icon_string;
        } else {
            return "# Character = '" + this.name + "' | Side = " + this.side;
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
     * Compare two Characters
     *
     * @param p
     * @return
     */
    @Override
    public int compareTo(Character p) {
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
