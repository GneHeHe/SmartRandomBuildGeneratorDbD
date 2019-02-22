package dbd;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * IconTableCellRenderer
 *
 * @author GneHeHe (2018)
 *
 */
public class IconTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        JLabel c = (JLabel) value;
        if (c != null) {
            // Add Tooltip
            c.setToolTipText(c.getName());
        }
        return c;
    }
}
