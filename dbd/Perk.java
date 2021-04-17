package dbd;

import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * Perk
 *
 * @author GneHeHe (2019)
 *
 */
public class Perk implements Comparable<Perk> {

    // Name of Perk
    private String name;
    // Weight of Perk
    private int weight;
    private int weight_ref;
    private int weight_orig;
    // Side of Perk
    private String side;
    // Parent of Perk
    private String parent;
    private String parent_string;
    // Icon of Perk (as String)
    private String icon_string;
    // Icon of Perk (as JLabel)
    private JLabel lab_img_small;
    private JLabel lab_img_medium;
    private JLabel lab_img_large;
    private JLabel lab_img_widget;
    // Icon of Parent (as JLabel)
    private JLabel lab_parent_small;
    // Size of Icon Pictures
    private final static int SIZE_SMALL = 50;
    private final static int SIZE_MEDIUM = 60;
    private final static int SIZE_LARGE = 160;
    private final static int SIZE_WIDGET = 110;
    private final static int SIZE_CHAR = 35;
    private final static double SIZE_CHAR_SCALING = 1.3;
    // Generic Perk
    public final static String SURVIVOR = "Survivor";
    public final static String KILLER = "Killer";
    public final static String GENERIC = "Undefined";
    public final static String GENERIC_ICON = "iconPerks_default.png";

    /**
     * Constructor
     *
     * @param name
     * @param weight
     * @param side
     * @param icon
     * @param parent
     * @param icon_parent
     */
    public Perk(String name, int weight, String side, String icon, String parent, String icon_parent) {
        setName(name);
        setSide(side);
        setIconString(icon);
        setParent(parent, icon_parent);
        setWeight(weight, true);
        setWeightOrig(weight);
        try {
            setIconPicture();
            setParentPicture();
        } catch (IOException ex) {
            System.err.println("\n# ERROR while creating Perk " + name + "\n");
            System.err.println(ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Constructor for Generic Perk
     *
     */
    public Perk() {
        setName(GENERIC);
        side = GENERIC;
        setIconString(GENERIC_ICON);
        setParent(GENERIC, GENERIC_ICON);
        setWeight(0, true);
        setWeightOrig(0);
        try {
            setIconPicture();
            //setParentPicture();
        } catch (IOException ex) {
            System.err.println("\n# ERROR while creating generic Perk\n");
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
        return name;
    }

    /**
     * Set Perk Name
     *
     * @param name
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * Get Perk Side
     *
     * @return
     */
    public String getSide() {
        return side;
    }

    /**
     * Set Perk Side
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
     * Get Perk Icon as String
     *
     * @return
     */
    public String getIconString() {
        return icon_string;
    }

    /**
     * Set Perk Icon as String
     *
     * @param icon_string
     */
    public final void setIconString(String icon_string) {
        this.icon_string = icon_string;
    }

    /**
     * Get Parent
     *
     * @return
     */
    public String getParent() {
        return parent;
    }

    /**
     * Set Parent
     *
     * @param parent
     * @param parent_string
     */
    private void setParent(String parent, String parent_string) {
        this.parent = parent;
        this.parent_string = parent_string;
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
            case 4:
                return lab_img_widget;
            default:
                return new JLabel(name);
        }
    }

    /**
     * Get Parent Icon as JLabel
     *
     * @return
     */
    public JLabel getParentImage() {
        return lab_parent_small;
    }

    /**
     * Set Perk Icon
     *
     * @throws java.io.IOException
     */
    private void setIconPicture() throws IOException {
        // Define JLabels
        lab_img_small = new JLabel("", SwingConstants.CENTER);
        lab_img_medium = new JLabel("", SwingConstants.CENTER);
        lab_img_large = new JLabel("", SwingConstants.CENTER);
        lab_img_widget = new JLabel("", SwingConstants.CENTER);
        // Try using given Icon Picture
        String path = "icons_perks/" + icon_string;
        if (getClass().getResourceAsStream(path) == null) {
            // Try using default Icon Picture
            path = "icons_perks/" + GENERIC_ICON;
            System.err.println("\n# WARNING: Using default Icon Files for Perk '" + name + "'");
        }
        // Load Icon Picture
        if (getClass().getResourceAsStream(path) != null) {
            // Set Icons in JLabel
            lab_img_small.setIcon(new ImageIcon(Tools.resizePicture(path, SIZE_SMALL, SIZE_SMALL)));
            lab_img_medium.setIcon(new ImageIcon(Tools.resizePicture(path, SIZE_MEDIUM, SIZE_MEDIUM)));
            lab_img_large.setIcon(new ImageIcon(Tools.resizePicture(path, SIZE_LARGE, SIZE_LARGE)));
            lab_img_widget.setIcon(new ImageIcon(Tools.resizePicture(path, SIZE_WIDGET, SIZE_WIDGET)));
            // Set Name for Tooltip
            lab_img_small.setName(name);
            lab_img_medium.setName(name);
            lab_img_large.setName("<html>" + name + "<br><br>Linked Character: " + parent + "</html>");
            lab_img_widget.setName("<html>" + name + "<br><br>Linked Character: " + parent + "</html>");
        } else {
            System.err.println("\n# ERROR: Icon Files were not found for Perk '" + name + "' => Exit\n");
            System.exit(0);
        }
    }

    /**
     * Set Parent Picture
     *
     * @throws java.io.IOException
     */
    private void setParentPicture() throws IOException {
        // Define JLabel
        lab_parent_small = new JLabel("", SwingConstants.CENTER);
        // Try using given Icon Picture
        String path = "icons_char/" + parent_string;
        if (getClass().getResourceAsStream(path) == null) {
            // Try to use default Parent Picture
            if (side.equals(SURVIVOR)) {
                path = "icons_char/" + Character.GENERIC_SURVIVOR_ICON;
            } else if (side.equals(KILLER)) {
                path = "icons_char/" + Character.GENERIC_KILLER_ICON;
            }
            System.err.println("\n# WARNING: Using default Icon Files for Character '" + parent + "' for Perk '" + name + "'");
        }
        // Load Icon Picture
        if (getClass().getResourceAsStream(path) != null) {
            // Set Icons in JLabel
            lab_parent_small.setIcon(new ImageIcon(Tools.resizePicture(path, SIZE_CHAR, (int) (SIZE_CHAR * SIZE_CHAR_SCALING))));
            // Set Name for Tooltip
            lab_parent_small.setName(parent);
        } else {
            System.err.println("\n# ERROR: Icon Files for Character '" + parent + "' were not found for Perk '" + name + "' => Exit\n");
            System.exit(0);
        }
    }

    /**
     * Get Perk Weight
     *
     * @return
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Get Perk Weight (Reference)
     *
     * @return
     */
    public int getWeightRef() {
        return weight_ref;
    }

    /**
     * Get Perk Weight (Orig)
     *
     * @return
     */
    public int getWeightOrig() {
        return weight_orig;
    }

    /**
     * Set Perk Weight
     *
     * @param weight
     * @param init
     */
    public final void setWeight(int weight, boolean init) {
        this.weight = weight;
        if (init) {
            this.weight_ref = weight;
        }
    }

    /**
     * Set Perk Weight (Orig)
     *
     * @param weight
     */
    private void setWeightOrig(int weight) {
        weight_orig = weight;
    }

    /**
     * Display Perk as String
     *
     * @param detail
     * @return
     */
    public String show(boolean detail) {
        if (detail) {
            return "# - Perk '" + name + "' | " + side + " Side | Icon = " + icon_string + " | Weight = " + getWeight();
        } else {
            return "# - Perk '" + name + "' | " + side + " Side | Weight = " + getWeight();
        }
    }

    /**
     * Is Side Correct ?
     *
     * @param test_side
     * @return
     */
    public boolean checkSide(String test_side) {
        if (side.equals(GENERIC)) {
            return true;
        } else {
            return side.equals(test_side);
        }
    }

    /**
     * Compare two Perks
     *
     * @param p
     * @return
     */
    @Override
    public int compareTo(Perk p) {
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
