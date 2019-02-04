package dbd;

import javax.swing.JLabel;

/**
 *
 * MyTableData
 *
 * @author GneHeHe (2018)
 *
 */
public class MyTableData {

    // Name of Perk
    private String data_name;
    // Weight of Perk
    private int data_weight;
    // Icon of Perk
    private JLabel data_icon;

    /**
     * Constructor
     *
     * @param p
     */
    public MyTableData(Perk p) {

        // Set the Name
        this.data_name = p.getName();
        // Set the Weight
        this.data_weight = p.getWeight();
        // Set the Icon
        this.data_icon = new JLabel(p.getIconImage());

    }

    /**
     * Get the Name of Perk
     *
     * @return
     */
    public String getPerkName() {
        return data_name;
    }

    /**
     * Set the Name of Perk
     *
     * @param perk
     */
    public void setPerkName(String perk) {
        this.data_name = perk;
    }

    /**
     * Get the Perk Icon
     *
     * @return
     */
    public JLabel getPerkIcon() {
        return data_icon;
    }

    /**
     * Set the Perk Icon
     *
     * @param icon
     */
    public void setPerkIcon(JLabel icon) {
        this.data_icon = icon;
    }

    /**
     * Get the Perk Weight
     *
     * @return
     */
    public int getPerkWeight() {
        return data_weight;
    }

    /**
     * Set the Perk Weight
     *
     * @param weight
     */
    public void setPerkWeight(int weight) {
        this.data_weight = weight;
    }

}
