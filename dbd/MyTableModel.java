package dbd;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * MyTableModel
 *
 * @author GneHeHe (2018)
 *
 */
public final class MyTableModel extends AbstractTableModel {

    // Define the Columns
    private final String[] columns = {"Perk", "Icon", "Weight"};
    // Define Content of Table
    private final ArrayList<MyTableData> perks;
    // SmartRandBuildGen Object
    private SmartRandBuildGen builder;

    /**
     * Constructor
     *
     * @param builder
     */
    public MyTableModel(SmartRandBuildGen builder) {
        // Define List
        this.perks = new ArrayList();
        // Set the SmartRandBuildGen Object
        this.builder = builder;

    }

    /**
     * Get the Number of Rows
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return perks.size();
    }

    /**
     * Get the Number of Columns
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * Get the Name of a Column
     *
     * @param columnIndex
     * @return
     */
    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    /**
     * Get the Value from a given Cell
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                //System.out.println(perks.get(rowIndex).getPerkName());
                return perks.get(rowIndex).getPerkName();
            case 1:
                //System.out.println(perks.get(rowIndex).getPerkIcon());
                return perks.get(rowIndex).getPerkIcon();
            case 2:
                //System.out.println(perks.get(rowIndex).getPerkWeight());
                return perks.get(rowIndex).getPerkWeight();
            default:
                return null;
        }
    }

    /**
     * Is this Cell is Editable ?
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
     * Set the Value in a given Cell
     *
     * @param aValue
     * @param rowIndex
     * @param columnIndex
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue != null) {
            MyTableData data = perks.get(rowIndex);
            if (columnIndex == 2) {
                // Update Weight for given Perk
                data.setPerkWeight(Integer.parseInt(aValue.toString()));
                System.out.println("# Updated Perk '" + data.getPerkName() + "' => New Value is '" + data.getPerkWeight() + "'");
            }
            // Update SmartRandBuildGen Object
            updateBuilder();
        }
    }

    /**
     * Get the Class of a Column
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
     * Update the Table according to SmartRandBuildGen Object
     *
     */
    public void updateTable() {
        // Reset Dztz
        perks.clear();
        // Loop over Perks from SmartRandBuildGen Object
        List<Perk> allperks = builder.getPerks();
        for (Perk p : allperks) {
            if (p.getSide().equals(builder.getSide())) {
                // Retrieve Data for a single Line
                MyTableData data = new MyTableData(p);
                // Store Values in the Model
                perks.add(data);
            }
        }
        // Update JTable using an Event
        fireTableDataChanged();
    }

    /**
     * Update SmartRandBuildGen Object according to current Values in Table
     *
     */
    public void updateBuilder() {
        // Loop over Data from Model
        for (int i = 0; i < perks.size(); i++) {
            MyTableData data = perks.get(i);
            // Retrieve Perk from SmartRandBuildGen Object
            Perk p = builder.getPerk(data.getPerkName());
            // Update Value
            p.setWeight(data.getPerkWeight());
        }
    }

}
