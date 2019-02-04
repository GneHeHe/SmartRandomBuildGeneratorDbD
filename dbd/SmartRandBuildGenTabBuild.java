package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.InputStream;
import java.util.TreeSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * SmartRandBuildGenTabBuild
 *
 * @author GneHeHe (2018)
 *
 */
public class SmartRandBuildGenTabBuild extends JPanel {

    // Swing Components
    private JPanel pan_config, pan_table;
    private JScrollPane scrollPane;
    private JButton b_run;
    private JComboBox cb_nbperks, cb_nbbuilds;
    private JLabel lab_nbperks, lab_nbbuilds;
    private JCheckBox check_care, check_sprint;
    private JTextArea text;
    private JTable table;
    // SmartRandBuildGen Object
    private SmartRandBuildGen srbg;
    // Number of Random Builds
    private int nbbuilds;
    // Size of Pictures    
    private int size;

    /**
     * Default Constructor
     *
     * @param myBuilder
     */
    public SmartRandBuildGenTabBuild(SmartRandBuildGen myBuilder) {

        // Set SmartRandBuildGen Object
        this.srbg = myBuilder;

        // Define Size of Pictures
        this.size = 220;

        // Add Swing Components
        addComponents();

        // Create Subpanels
        pan_config = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_config.add(lab_nbperks);
        pan_config.add(cb_nbperks);
        pan_config.add(lab_nbbuilds);
        pan_config.add(cb_nbbuilds);
        pan_config.add(check_care);
        pan_config.add(check_sprint);
        pan_config.add(b_run);

        scrollPane = new JScrollPane(text);

        pan_table = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_table.add(table);

        // Set the Layout and add Subpanels
        setLayout(new BorderLayout());
        add(pan_config, BorderLayout.NORTH);
        add(pan_table, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Define JButton Objects
        b_run = new JButton("Get Random Builds");

        // Define JLabel Objects
        lab_nbperks = new JLabel("Number of Perks");
        lab_nbbuilds = new JLabel("Number of Builds");

        // Define JTextArea Objects
        text = new JTextArea(30, 20);
        text.setEditable(false);
        text.setFont(new Font("Helvetica", Font.PLAIN, 16));

        // Define JComboBox Objects for Perks
        cb_nbperks = new JComboBox(new Integer[]{1, 2, 3, 4});
        cb_nbperks.setPreferredSize(new Dimension(50, 20));
        ((JLabel) cb_nbperks.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        cb_nbperks.setSelectedItem(srbg.getNbPerk());

        // Define JComboBox Objects for Number of Builds
        cb_nbbuilds = new JComboBox(new Integer[]{1, 3, 5, 10});
        cb_nbbuilds.setPreferredSize(new Dimension(50, 20));
        ((JLabel) cb_nbbuilds.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        cb_nbbuilds.setSelectedIndex(2);
        nbbuilds = Integer.parseInt(cb_nbbuilds.getSelectedItem().toString());

        // Define JCheckBox Objects
        check_care = new JCheckBox("Care Perk Needed");
        if (srbg.getNeedCare()) {
            check_care.setSelected(true);
        } else {
            check_care.setSelected(false);
        }
        check_care.setToolTipText(srbg.getCareAsString());

        check_sprint = new JCheckBox("Sprint Perk Needed");
        if (srbg.getNeedSprint()) {
            check_sprint.setSelected(true);
        } else {
            check_sprint.setSelected(false);
        }
        check_sprint.setToolTipText(srbg.getSprintAsString());

        // Define JTable Objects
        table = new JTable(1, 4);
        table.setRowHeight(this.size);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setCellRenderer(new IconTableCellRenderer());
            column.setPreferredWidth(this.size);
        }
        table.setAutoscrolls(false);

        // Define ActionListener
        b_run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update Var according to Side
                if (srbg.getSide().equals("Killer")) {
                    // Special Case => disabled JCheckBox and Care/Perk Status
                    check_care.setSelected(false);
                    check_care.setEnabled(false);
                    srbg.setNeedCare(false);
                    check_sprint.setSelected(false);
                    check_sprint.setEnabled(false);
                    srbg.setNeedSprint(false);
                } else {
                    check_care.setEnabled(true);
                    check_care.setSelected(srbg.getNeedCare());
                    check_sprint.setEnabled(true);
                    check_sprint.setSelected(srbg.getNeedSprint());
                }
                // Reset TextArea
                text.setText("");
                // Reset Table => Remove Data in TableModel (RowCount = 0)
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
                // Add a new Line for the 1st Build
                model.setRowCount(1);
                // Define the Text to be Displayed
                String fulltext = "\n ";
                for (int i = 1; i <= nbbuilds; i++) {
                    TreeSet<String> l = srbg.genRandomBuild();
                    fulltext = fulltext + srbg.showBuild(l, i + "", "       ");
                    if (i < nbbuilds) {
                        fulltext = fulltext + "\n\n ";
                    }
                    // 1st Build => Display It in the Table
                    if (i == 1) {
                        int nb = 0;
                        String path = "";
                        String icon = null;
                        JLabel tmp = null;
                        InputStream is = null;
                        for (String s : l) {
                            icon = srbg.getPerk(s).getIconString();
                            path = "icons/" + icon + ".png";
                            is = this.getClass().getResourceAsStream(path);
                            if (is != null) {
                                tmp = new JLabel(new ImageIcon(Tools.resizePicture(is, size, size)));
                            }
                            table.getModel().setValueAt(tmp, 0, nb);
                            nb++;
                        }
                    }
                }
                // Display Text
                text.setText(fulltext);
            }
        });

        // Define ActionListener
        cb_nbperks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve and Define the Number of Perks
                srbg.setNbPerk(Integer.parseInt(cb_nbperks.getSelectedItem().toString()));
                if (srbg.getNbPerk() < 2) {
                    // Special Case => disabled JCheckBox and Care/Sprint Status
                    check_care.setSelected(false);
                    check_care.setEnabled(false);
                    srbg.setNeedCare(false);
                    check_sprint.setSelected(false);
                    check_sprint.setEnabled(false);
                    srbg.setNeedSprint(false);
                } else {
                    // Common Case => enabled JCheckBox
                    check_care.setEnabled(true);
                    check_sprint.setEnabled(true);
                    // Update JCheckBox according to SRBG Object
                    if (srbg.getNeedCare()) {
                        check_care.setSelected(true);
                    } else {
                        check_care.setSelected(false);
                    }
                    // Update JCheckBox according to SRBG Object
                    if (srbg.getNeedSprint()) {
                        check_sprint.setSelected(true);
                    } else {
                        check_sprint.setSelected(false);
                    }
                }
            }
        });

        // Define ActionListener
        cb_nbbuilds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve and Define the Number of Builds
                nbbuilds = Integer.parseInt(cb_nbbuilds.getSelectedItem().toString());
                System.out.println("# Number of wanted Builds = " + nbbuilds);
            }
        });

        // Define ItemListener
        check_care.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Retrieve and Define the Needed Care Status
                if (check_care.isSelected()) {
                    srbg.setNeedCare(true);
                } else {
                    srbg.setNeedCare(false);
                }
            }
        });

        // Define ItemListener
        check_sprint.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Retrieve and Define the Needed Sprint Status
                if (check_sprint.isSelected()) {
                    srbg.setNeedSprint(true);
                } else {
                    srbg.setNeedSprint(false);
                }
            }
        });

    }

}
