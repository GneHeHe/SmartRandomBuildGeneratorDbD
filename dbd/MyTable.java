package dbd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * MyTable
 *
 * @author GneHeHe (2018)
 *
 */
public class MyTable extends JTable {

    private Vector<Integer> v_weights;
    private int weight_max;

    /**
     * Default Constructor
     */
    public MyTable() {
        // Default Dimension of Header
        getTableHeader().setPreferredSize(new Dimension(30, 30));
        // Set Reordering Status
        getTableHeader().setReorderingAllowed(false);
        // Set Resizing Status
        getTableHeader().setResizingAllowed(false);
        // Set Font of Header
        getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 16));
        // Set Font of Table
        setFont(new Font("Helvetica", Font.BOLD, 14));
        // Set Available Weights
        this.v_weights = new Vector<>();
        this.v_weights.add(0);
        this.v_weights.add(1);
        this.v_weights.add(5);
        this.v_weights.add(10);
        this.v_weights.add(25);
        this.v_weights.add(50);
        this.v_weights.add(75);
        this.v_weights.add(100);
        this.v_weights.add(125);
        this.v_weights.add(150);
        this.v_weights.add(175);
        this.v_weights.add(200);
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
        //Set up the editor for the sport cells.
        JComboBox comboBox = new JComboBox(v_weights);
        TableColumn column = getColumnModel().getColumn(i);
        column.setCellEditor(new DefaultCellEditor(comboBox));
    }

    /**
     * Define Content of a Column as a JLabel
     *
     * @param i
     */
    public void setColumnPerk(int i) {
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
                // Define Background according to third Hue with half Saturation and default Brightness
                comp.setBackground(Color.getHSBColor(h3, 0.5f, 1));
            }
        }
        return comp;
    }

    /**
     * Get the largest Weight
     *
     * @return
     */
    private int getWeightMax() {
        int max = 0;
        for (Integer v : v_weights) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

}
