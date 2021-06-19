package dbd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * TablePerk
 *
 * @author GneHeHe (2019)
 *
 */
public class TablePerk extends JTable {

    // Define available Weights
    private ArrayList<Integer> l_weights;
    // Define max Weight
    private int weight_max;

    /**
     * Default Constructor
     *
     * @param max
     */
    public TablePerk(int max) {
        // Check Value
        if (max <= 0) {
            System.err.println("\n# ERROR: Wrong maximum weight value '" + max + "\n");
            System.exit(0);
        }
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
        // Only select 1 Line
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Set Available Weights
        l_weights = new ArrayList<>();
        l_weights.add(0);
        l_weights.add(1);
        l_weights.add(5);
        l_weights.add(10);
        l_weights.add(25);
        l_weights.add(50);
        l_weights.add(100);
        l_weights.add(150);
        l_weights.add(200);
        l_weights.add(250);
        l_weights.add(300);
        Collections.sort(l_weights);
        int maxlocal = l_weights.get(l_weights.size() - 1);
        // Check Value
        if (maxlocal >= max) {
            System.err.println("\n# ERROR: Maximum weight value '" + max + "' is expected to be larger than '" + maxlocal + "\n");
            System.exit(0);
        }
        weight_max = max;
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
     * Define Content of a Column as a JComboBox
     *
     * @param i
     */
    public void setColumnWeight(int i) {
        JComboBox comboBox = new JComboBox(l_weights.toArray());
        TableColumn column = getColumnModel().getColumn(i);
        column.setCellEditor(new DefaultCellEditor(comboBox));
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
     * Define Special Renderer for Cells
     *
     * @param renderer
     * @param row
     * @param col
     * @return
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
        int modelRow = convertRowIndexToModel(row);
        Component comp = super.prepareRenderer(renderer, row, col);
        // Update Background Color according to Value (Only Process Third Column)
        if (col == 3) {
            // Get Value (required double/float)
            float value = Float.parseFloat(getModel().getValueAt(modelRow, col).toString());
            if (value <= 0) {
                // No Weight => Red Background
                comp.setBackground(Color.RED);
            } else {
                // Use of HSB Model | Set first Hue to Orange Color
                float h1 = 30.0f / 360;
                // Use of HSB Model | Set second Hue to Dark Cyan Color
                float h2 = 210.0f / 360;
                // Use of HSB Model | Set third Hue to Intermediate Color
                float h3 = h1 + (value / weight_max) * (h2 - h1);
                // Define Background according to third Hue with half Saturation & default Brightness
                comp.setBackground(Color.getHSBColor(h3, 0.5f, 1));
            }
        }
        return comp;
    }

}
