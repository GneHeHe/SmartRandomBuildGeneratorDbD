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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        // Cast
        JLabel c = (JLabel) value;

        // Add Tooltip
        if (c != null) {
            c.setToolTipText(c.getName());
        }

        // Component rendered as JLabel Object (Icon)
        return c;
    }

}
