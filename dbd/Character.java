package dbd;

import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * Character
 *
 * @author GneHeHe (2019)
 *
 */
public class Character implements Comparable<Character> {

    // Name of Character
    private String name;
    // Side of Character
    private String side;
    // Icon of Character (as String)
    private String icon_string;
    // Icon of Character (as JLabel)
    private JLabel lab_img_small;
    private JLabel lab_img_large;
    private JLabel lab_img_widget;
    // Size of Icon Pictures
    private final static int SIZE_SMALL = 55;
    private final static int SIZE_LARGE = 175;
    private final static int SIZE_WIDGET = 130;
    // Strings
    public final static String SURVIVOR = "Survivor";
    public final static String KILLER = "Killer";
    // Generic Characters
    public final static String GENERIC_SURVIVOR = "GenSurv";
    public final static String GENERIC_KILLER = "GenKiller";

    /**
     * Constructor
     *
     * @param name
     * @param side
     * @param icon
     */
    public Character(String name, String side, String icon) {
        setName(name);
        setSide(side);
        lab_img_small = new JLabel("", SwingConstants.CENTER);
        lab_img_large = new JLabel("", SwingConstants.CENTER);
        lab_img_widget = new JLabel("", SwingConstants.CENTER);
        try {
            setIconPicture(icon);
        } catch (IOException ex) {
            System.err.println("\n# ERROR while creating Character " + name +"\n");
            System.err.println(ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Constructor for Generic Character
     *
     * @param side
     */
    public Character(String side) {
        if (side.equals(SURVIVOR)) {
            setName(Character.GENERIC_SURVIVOR);
        } else if (side.equals(KILLER)) {
            setName(Character.GENERIC_KILLER);
        } else {
            System.err.println("\n# ERROR while creating generic Character\n");
            System.exit(0);
        }
        setSide(side);
        lab_img_small = new JLabel("", SwingConstants.CENTER);
        lab_img_large = new JLabel("", SwingConstants.CENTER);
        lab_img_widget = new JLabel("", SwingConstants.CENTER);
        // Set Default Icon
        String icon = "";
        if (side.equals(SURVIVOR)) {
            icon = "iconHelpLoading_survivor";
        } else if (side.equals(KILLER)) {
            icon = "iconHelpLoading_killer";
        }
        try {
            setIconPicture(icon);
        } catch (IOException ex) {
            System.err.println("\n# ERROR while creating generic Character\n");
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
        return name;
    }

    /**
     * Set Character Name
     *
     * @param name
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * Get Character Side
     *
     * @return
     */
    public String getSide() {
        return side;
    }

    /**
     * Set Character Side
     *
     * @param side
     */
    public final void setSide(String side) {
        // Only 2 Sides are Available
        if (side.equals(KILLER) || side.equals(SURVIVOR)) {
            this.side = side;
        } else {
            System.err.println("\n# ERROR: This side " + side + " is wrong (expected value = 'Killer' or 'Survivor'\n");
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
            case 3:
                return lab_img_widget;
            default:
                return new JLabel(name, SwingConstants.CENTER);
        }
    }

    /**
     * Get Character Icon as String
     *
     * @return
     */
    public String getIconString() {
        return icon_string;
    }

    /**
     * Set Character Icon
     *
     * @param s_icon
     * @throws java.io.IOException
     */
    private void setIconPicture(String s_icon) throws IOException {
        // Try to set given Icon Picture
        icon_string = s_icon;
        String path = "icons_char/" + s_icon + ".png";
        if (getClass().getResourceAsStream(path) != null) {
            // Set Icons in JLabel
            lab_img_small.setIcon(new ImageIcon(Tools.resizePicture(path, Character.SIZE_SMALL, Character.SIZE_SMALL)));
            lab_img_large.setIcon(new ImageIcon(Tools.resizePicture(path, Character.SIZE_LARGE, Character.SIZE_LARGE)));
            lab_img_widget.setIcon(new ImageIcon(Tools.resizePicture(path, Character.SIZE_WIDGET, Character.SIZE_WIDGET)));
            // Set Name for Tooltip
            lab_img_small.setName(name);
            lab_img_large.setName(name);
            lab_img_widget.setName(name);
        } else {
            // Try to use default Icon Picture
            if (side.equals(SURVIVOR)) {
                s_icon = "iconHelpLoading_survivor";
            } else if (side.equals(KILLER)) {
                s_icon = "iconHelpLoading_killer";
            }
            path = "icons_char/" + s_icon + ".png";
            if (getClass().getResourceAsStream(path) != null) {
                // Set Default Icon in JLabel
                lab_img_small.setIcon(new ImageIcon(Tools.resizePicture(path, Character.SIZE_SMALL, Character.SIZE_SMALL)));
                lab_img_large.setIcon(new ImageIcon(Tools.resizePicture(path, Character.SIZE_LARGE, Character.SIZE_LARGE)));
                lab_img_widget.setIcon(new ImageIcon(Tools.resizePicture(path, Character.SIZE_WIDGET, Character.SIZE_WIDGET)));
                // Set Name for Tooltip
                lab_img_small.setName(name);
                lab_img_large.setName(name);
                lab_img_widget.setName(name);
            } else {
                System.err.println("\n# ERROR: Both expected and default Icon Files were not found for Character '" + name + "' => Exit\n");
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
            return "# Character = '" + name + "' | Side = " + side + " | Icon = " + icon_string;
        } else {
            return "# Character = '" + name + "' | Side = " + side;
        }
    }

    /**
     * Is Side Correct ?
     *
     * @param side
     * @return
     */
    public boolean checkSide(String side) {
        return side.equals(side);
    }

    /**
     * Compare two Characters
     *
     * @param p
     * @return
     */
    @Override
    public int compareTo(Character p) {
        return name.compareTo(p.name);
    }

    /**
     * toString() Method
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }

}
