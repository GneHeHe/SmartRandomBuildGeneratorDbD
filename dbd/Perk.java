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
    // Side of Perk
    private String side;
    // Icon of Perk (as String)
    private String icon_string;
    // Icon of Perk (as JLabel)
    private JLabel lab_img_small;
    private JLabel lab_img_medium;
    private JLabel lab_img_large;
    private JLabel lab_img_widget;
    // Size of Icon Pictures
    private final static int SIZE_SMALL = 50;
    private final static int SIZE_MEDIUM = 60;
    private final static int SIZE_LARGE = 160;
    private final static int SIZE_WIDGET = 110;
    // Generic Perk
    public final static String SURVIVOR = "Survivor";
    public final static String KILLER = "Killer";
    public final static String GENERIC = "Undefined";

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
        setWeight(weight, true);
        lab_img_small = new JLabel("", SwingConstants.CENTER);
        lab_img_medium = new JLabel("", SwingConstants.CENTER);
        lab_img_large = new JLabel("", SwingConstants.CENTER);
        lab_img_widget = new JLabel("", SwingConstants.CENTER);
        try {
            setIconPicture(icon);
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
        setName(Perk.GENERIC);
        side = Perk.GENERIC;
        setWeight(0, true);
        // Set Default Icon
        String icon = "iconPerks_default";
        lab_img_small = new JLabel("", SwingConstants.CENTER);
        lab_img_medium = new JLabel("", SwingConstants.CENTER);
        lab_img_large = new JLabel("", SwingConstants.CENTER);
        lab_img_widget = new JLabel("", SwingConstants.CENTER);
        try {
            setIconPicture(icon);
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
     * Get Perk Icon as String
     *
     * @return
     */
    public String getIconString() {
        return icon_string;
    }

    /**
     * Set Perk Icon
     *
     * @param s_icon
     * @throws java.io.IOException
     */
    private void setIconPicture(String s_icon) throws IOException {
        // Try to set given Icon Picture
        icon_string = s_icon;
        String path = "icons_perks/" + s_icon + ".png";
        if (getClass().getResourceAsStream(path) != null) {
            // Set Icons in JLabel
            lab_img_small.setIcon(new ImageIcon(Tools.resizePicture(path, Perk.SIZE_SMALL, Perk.SIZE_SMALL)));
            lab_img_medium.setIcon(new ImageIcon(Tools.resizePicture(path, Perk.SIZE_MEDIUM, Perk.SIZE_MEDIUM)));
            lab_img_large.setIcon(new ImageIcon(Tools.resizePicture(path, Perk.SIZE_LARGE, Perk.SIZE_LARGE)));
            lab_img_widget.setIcon(new ImageIcon(Tools.resizePicture(path, Perk.SIZE_WIDGET, Perk.SIZE_WIDGET)));
            // Set Name for Tooltip
            lab_img_small.setName(name);
            lab_img_medium.setName(name);
            lab_img_large.setName(name);
            lab_img_widget.setName(name);
        } else {
            // Try to use default Icon Picture
            s_icon = "iconPerks_default";
            path = "icons_perks/" + s_icon + ".png";
            if (getClass().getResourceAsStream(path) != null) {
                // Set Default Icon in JLabel
                lab_img_small.setIcon(new ImageIcon(Tools.resizePicture(path, Perk.SIZE_SMALL, Perk.SIZE_SMALL)));
                lab_img_medium.setIcon(new ImageIcon(Tools.resizePicture(path, Perk.SIZE_MEDIUM, Perk.SIZE_MEDIUM)));
                lab_img_large.setIcon(new ImageIcon(Tools.resizePicture(path, Perk.SIZE_LARGE, Perk.SIZE_LARGE)));
                lab_img_widget.setIcon(new ImageIcon(Tools.resizePicture(path, Perk.SIZE_WIDGET, Perk.SIZE_WIDGET)));
                // Set Name for Tooltip
                lab_img_small.setName(name);
                lab_img_medium.setName(name);
                lab_img_large.setName(name);
                lab_img_widget.setName(name);
            } else {
                System.err.println("\n# ERROR: Both expected and default Icon Files were not found for Perk '" + name + "' => Exit\n");
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
     * Set Perk Weight
     *
     * @param weight
     * @param init
     */
    public final void setWeight(int weight, boolean init) {
        this.weight = weight;
        if (init) {
            weight_ref = weight;
        }
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
        if (side.equals(Perk.GENERIC)) {
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
