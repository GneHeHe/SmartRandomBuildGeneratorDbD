package dbd;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * BuildTable
 *
 * @author GneHeHe (2018)
 *
 */
public class BuildTable extends JTable {

    // Row Sorter
    private TableRowSorter<BuildTableModel> sorter;

    /**
     * Default Constructor
     *
     */
    public BuildTable() {
        // Default Dimension of Header
        this.getTableHeader().setPreferredSize(new Dimension(30, 30));
        // Set Reordering/Resizing Status
        this.getTableHeader().setReorderingAllowed(false);
        this.getTableHeader().setResizingAllowed(false);
        // Set Fonts
        this.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 16));
        this.setFont(new Font("Helvetica", Font.BOLD, 14));
        // No Manual Sorting
        this.getTableHeader().setEnabled(false);
    }

    /**
     * Center Text in all Columns
     *
     */
    public void centerText() {
        for (int i = 0; i < getColumnCount(); i++) {
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Center Text in a Column
     *
     * @param column
     */
    public void centerText(int column) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
    }

    /**
     * Set JLabel Rendering in a Column
     *
     * @param i
     */
    public void setIconColumn(int i) {
        TableColumn column = getColumnModel().getColumn(i);
        column.setCellRenderer(new IconTableCellRenderer());
    }

    /**
     * Remove Selected Rows from Table
     *
     * @return 
     */
    public boolean removeSelectedRows() {
        BuildTableModel model = (BuildTableModel) this.getModel();
        int[] rows = getSelectedRows();
        if (rows.length == 0) {
            return false;
        }
        for (int i = 0; i < rows.length; i++) {
            model.removeBuild(rows[i] - i);
        }
        return true;
    }

    /**
     * Get a Sorter
     *
     * @return
     */
    public TableRowSorter getSorter() {
        return this.sorter;
    }

    /**
     * Set a Sorter
     *
     */
    public void setSorter() {
        sorter = new TableRowSorter(getModel());
        setRowSorter(sorter);
    }

}
