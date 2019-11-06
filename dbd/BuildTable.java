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
 * @author GneHeHe (2019)
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
        getTableHeader().setPreferredSize(new Dimension(30, 30));
        // Set Reordering/Resizing Status
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);
        // Set Fonts
        getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 16));
        setFont(new Font("Helvetica", Font.BOLD, 14));
        // No Manual Sorting
        getTableHeader().setEnabled(false);
    }

    /**
     * Center Text in a given Column
     *
     * @param column
     */
    public void centerText(int column) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
    }

    /**
     * Center Text in all Columns
     *
     */
    public void centerText() {
        for (int i = 0; i < getColumnCount(); i++) {
            centerText(i);
        }
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
        BuildTableModel model = (BuildTableModel) getModel();
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
        return sorter;
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
