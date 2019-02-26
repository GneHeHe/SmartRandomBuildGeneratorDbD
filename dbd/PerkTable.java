package dbd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
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
 * PerkTable
 *
 * @author GneHeHe (2018)
 *
 */
public class PerkTable extends JTable {

    // Define available Weights
    private ArrayList<Integer> l_weights;
    // Define max Weight
    private int weight_max;

    /**
     * Default Constructor
     */
    public PerkTable() {
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
        // Only select 1 Line
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Set Available Weights
        this.l_weights = new ArrayList<>();
        this.l_weights.add(0);
        this.l_weights.add(1);
        this.l_weights.add(5);
        this.l_weights.add(10);
        this.l_weights.add(25);
        this.l_weights.add(50);
        this.l_weights.add(75);
        this.l_weights.add(100);
        this.l_weights.add(125);
        this.l_weights.add(150);
        this.l_weights.add(175);
        this.l_weights.add(200);
        this.weight_max = getWeightMax();
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
        Component comp = super.prepareRenderer(renderer, row, col);
        // Update Background Color according to Value (Only Process Third Column)
        if (col == 2) {
            // Get Value (required double/float)
            float value = Float.parseFloat(getModel().getValueAt(row, col).toString());
            if (value == 0) {
                // No Weight => Red Background
                comp.setBackground(Color.RED);
            } else {
                // Use of HSB Model | Set first Hue to Orange Color
                //float h1 = Color.RGBtoHSB(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), null)[0];
                float h1 = 30.0f / 360;
                // Use of HSB Model | Set second Hue to Dark Cyan Color
                //float h2 = Color.RGBtoHSB(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), null)[0];
                float h2 = 210.0f / 360;
                // Use of HSB Model | Set third Hue to Intermediate Color
                float h3 = h1 + (value / this.weight_max) * (h2 - h1);
                // Define Background according to third Hue with half Saturation & default Brightness
                comp.setBackground(Color.getHSBColor(h3, 0.5f, 1));
            }
        }
        return comp;
    }

    /**
     * Get largest Weight
     *
     * @return
     */
    private int getWeightMax() {
        int max = 0;
        for (Integer v : l_weights) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

}
