package dbd;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * PerkTableModel
 *
 * @author GneHeHe (2018)
 *
 */
public final class PerkTableModel extends AbstractTableModel {

    // Define Columns
    private final String[] columns = {"Perk", "Icon", "Weight"};
    // Define Content of Table
    private ArrayList<Perk> l_perks;
    // SmartRandBuildGen Object
    private SmartRandBuildGen srbg;

    /**
     * Constructor
     *
     * @param object
     */
    public PerkTableModel(SmartRandBuildGen object) {
        // Define List
        this.l_perks = new ArrayList();
        // Set SmartRandBuildGen Object
        this.srbg = object;
    }

    /**
     * Get Number of Rows
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return l_perks.size();
    }

    /**
     * Get Number of Columns
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * Get Name of a Column
     *
     * @param columnIndex
     * @return
     */
    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    /**
     * Get Class of a Column
     *
     * @param columnIndex
     * @return
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return JLabel.class;
            case 2:
                return Integer.class;
            default:
                return Object.class;
        }
    }

    /**
     * Get Value from a given Cell
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                //System.out.println(l_perks.get(rowIndex).getPerkName());
                return l_perks.get(rowIndex).getName();
            case 1:
                //System.out.println(l_perks.get(rowIndex).getPerkIcon());
                return l_perks.get(rowIndex).getIconImage(1);
            case 2:
                //System.out.println(l_perks.get(rowIndex).getPerkWeight());
                return l_perks.get(rowIndex).getWeight();
            default:
                return null;
        }
    }

    /**
     * Is this Cell Editable ?
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return true;
            default:
                return false;
        }
    }

    /**
     * Set Value in a given Cell
     *
     * @param aValue
     * @param rowIndex
     * @param columnIndex
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue != null) {
            Perk p = l_perks.get(rowIndex);
            if (columnIndex == 2) {
                // Update Weight for given Perk
                p.setWeight(Integer.parseInt(aValue.toString()));
                System.out.println("# Updated Perk '" + p.getName() + "' => New Value is '" + p.getWeight() + "'");
            }
            // Update SmartRandBuildGen Object
            this.updateBuilder();
        }
    }

    /**
     * Update Table according to SmartRandBuildGen Object
     *
     */
    public void updateTable() {
        // Reset Data
        l_perks.clear();
        // Loop over Perks from SmartRandBuildGen Object
        List<Perk> allperks = srbg.getPerks();
        for (Perk p : allperks) {
            if (p.getSide().equals(srbg.getSide())) {
                l_perks.add(p);
            }
        }
        // Update JTable using an Event
        this.fireTableDataChanged();
    }

    /**
     * Update SmartRandBuildGen Object according to current Values in Table
     *
     */
    public void updateBuilder() {
        // Loop over Data from Model
        for (int i = 0; i < l_perks.size(); i++) {
            Perk p_table = l_perks.get(i);
            // Retrieve Perk from SmartRandBuildGen Object
            Perk p_builder = srbg.getPerk(p_table.getName());
            // Update Value
            p_builder.setWeight(p_table.getWeight());
        }
    }

}
