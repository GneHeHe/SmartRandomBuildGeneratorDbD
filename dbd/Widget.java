package dbd;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

/**
 *
 * Widget
 *
 * @author GneHeHe (2019)
 *
 */
public class Widget extends JFrame {

    // Swing Components
    private JScrollPane scrollPane;
    private JTable table_mini;
    // Frame Location
    private Point point;
    private double x_decal = 0.17;
    private double y_decal = 0.17;
    // Table
    private int table_size = 160;
    private int table_height = 135;

    /**
     * Default Constructor
     *
     */
    public Widget() {

        // Frame without Decoration
        setUndecorated(true);

        // Set Background Color
        //getRootPane().setBackground(new Color(0, 0, 0, 0));
        getRootPane().setBackground(Color.BLACK);

        // Add Swing Components
        addComponents();

        // Create JScrollPane
        scrollPane = new JScrollPane(table_mini);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.getViewport().setOpaque(false);

        // Add JScrollPane
        setContentPane(scrollPane);

        // Set Frame
        setSize(new Dimension(5 * table_size, table_height));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        requestFocus();
        //pack();
        setVisible(true);

    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Point to Deal with Frame Location
        point = new Point();

        // Translation of Frame
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (x_decal * dimension.getWidth());
        int y = (int) (y_decal * dimension.getHeight());
        setLocation(x, y);

        // Define non-editable JTable
        table_mini = new JTable(1, 5) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };

        // Define JTable Object
        table_mini.setTableHeader(null);
        table_mini.setRowHeight(table_height);
        table_mini.setShowHorizontalLines(false);
        table_mini.setShowVerticalLines(false);
        table_mini.setOpaque(false);
        for (int i = 0; i < table_mini.getColumnCount(); i++) {
            TableColumn column = table_mini.getColumnModel().getColumn(i);
            column.setCellRenderer(new IconTableCellRenderer());
            column.setPreferredWidth(table_size);
        }

        // Define MouseListener
        table_mini.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    // Close Frame after Right Mouse Button has been Pressed
                    dispose();
                }
            }
        });

        // Define MouseMotionListener
        table_mini.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
            }
        });

        // Define KeyListener
        table_mini.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                // Close Frame after Escape Key has been Pressed
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });

    }

    /**
     * Get Table
     *
     * @return
     */
    public JTable getTable() {
        return table_mini;
    }

}
