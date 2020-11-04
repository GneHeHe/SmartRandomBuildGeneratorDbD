package dbd;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * TableModelPerk
 *
 * @author GneHeHe (2019)
 *
 */
public class TableModelPerk extends AbstractTableModel {

    // Define Columns
    private final String[] columns = {"Perk Name", "Perk Icon", "Linked Character", "Perk Weight"};
    // Define Content of Table
    private ArrayList<Perk> l_perks;
    // SRBG Object
    private SRBG srbg;

    /**
     * Constructor
     *
     * @param srbg
     */
    public TableModelPerk(SRBG srbg) {
        // Define List
        l_perks = new ArrayList();
        // Set SRBG Object
        this.srbg = srbg;
    }

    /**
     * Get Nb of Rows
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return l_perks.size();
    }

    /**
     * Get Nb of Columns
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * Get Name of a given Column
     *
     * @param columnIndex
     * @return
     */
    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    /**
     * Get Class of a given Column
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
                return JLabel.class;
            case 3:
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
                return l_perks.get(rowIndex).getParentImage();
            case 3:
                //System.out.println(l_perks.get(rowIndex).getPerkWeight());
                return l_perks.get(rowIndex).getWeight();
            default:
                return null;
        }
    }

    /**
     * Is the Cell Editable ?
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
                return false;
            case 3:
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
            if (columnIndex == 3) {
                // Update Weight for given Perk
                p.setWeight(Integer.parseInt(aValue.toString()), true);
                System.out.println("# Updated Perk '" + p.getName() + "' => New Value is '" + p.getWeight() + "'");
            }
            // Update SRBG Object
            updateBuilder();
        }
    }

    /**
     * Update Table according to SRBG Object and Throw Event
     *
     * @param side
     */
    public void updateTable(String side) {
        // Reset Data
        l_perks.clear();
        // Loop over Perks from SRBG Object
        List<Perk> allperks = srbg.getPerks();
        for (Perk p : allperks) {
            if (p.getSide().equals(side)) {
                l_perks.add(p);
            }
        }
        // Update JTable using an Event
        fireTableDataChanged();
    }

    /**
     * Update SRBG Object according to current Values in Table
     *
     */
    public void updateBuilder() {
        // Loop over Data from Model
        for (int i = 0; i < l_perks.size(); i++) {
            Perk p_table = l_perks.get(i);
            // Retrieve Perk from SRBG Object
            Perk p_builder = srbg.getPerk(p_table.getName());
            // Update Value
            p_builder.setWeight(p_table.getWeight(), true);
        }
        // Update Pool of Perks
        srbg.setPerkPoolChanged(true);
    }

}
