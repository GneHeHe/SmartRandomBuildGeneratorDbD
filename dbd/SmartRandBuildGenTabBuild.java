package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    private JButton b_build;
    private JComboBox cb_nbperks, cb_nbbuilds;
    private JLabel lab_nbperks, lab_nbbuilds;
    private JCheckBox check_care, check_sprint, check_side, check_char;
    private JTextArea text;
    private JTable table;
    // SmartRandBuildGen Object
    private SmartRandBuildGen srbg;
    // Number of Random Builds
    private int nbbuilds;

    /**
     * Default Constructor
     *
     * @param srbg
     */
    public SmartRandBuildGenTabBuild(SmartRandBuildGen srbg) {

        // Set SmartRandBuildGen Object
        this.srbg = srbg;

        // Add Swing Components
        this.addComponents();

        // Create Subpanels
        this.pan_config = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.pan_config.add(this.lab_nbperks);
        this.pan_config.add(this.cb_nbperks);
        this.pan_config.add(this.lab_nbbuilds);
        this.pan_config.add(this.cb_nbbuilds);
        this.pan_config.add(this.check_side);
        this.pan_config.add(this.check_char);
        this.pan_config.add(this.check_care);
        this.pan_config.add(this.check_sprint);
        this.pan_config.add(this.b_build);

        this.pan_table = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.pan_table.add(this.table);

        // Create JScrollPane (hide bars)
        this.scrollPane = new JScrollPane(this.text);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Set Layout & add Subpanels
        this.setLayout(new BorderLayout());
        this.add(this.pan_config, BorderLayout.NORTH);
        this.add(this.pan_table, BorderLayout.SOUTH);
        this.add(this.scrollPane, BorderLayout.CENTER);
    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Define JTable Objects
        int size = 200;
        this.table = new JTable(1, 5);
        this.table.setRowHeight(size);
        this.table.setShowHorizontalLines(false);
        this.table.setShowVerticalLines(false);
        for (int i = 0; i < this.table.getColumnCount(); i++) {
            TableColumn column = this.table.getColumnModel().getColumn(i);
            column.setCellRenderer(new IconTableCellRenderer());
            column.setPreferredWidth(size);
        }

        // Define JButton Objects
        this.b_build = new JButton("Get Random Builds");

        // Define JLabel Objects
        this.lab_nbperks = new JLabel("  Nb of Perks ");
        this.lab_nbbuilds = new JLabel("  Nb of Builds ");

        // Define JTextArea Objects
        this.text = new JTextArea(30, 20);
        this.text.setEditable(false);
        this.text.setFont(new Font("Helvetica", Font.PLAIN, 16));

        // Define JComboBox Objects for Perks
        this.cb_nbperks = new JComboBox(new Integer[]{1, 2, 3, 4});
        this.cb_nbperks.setPreferredSize(new Dimension(50, 20));
        ((JLabel) this.cb_nbperks.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        this.cb_nbperks.setSelectedItem(this.srbg.getNbPerksBuild());

        // Define JComboBox Objects for Number of Builds
        this.cb_nbbuilds = new JComboBox(new Integer[]{1, 3, 5, 10});
        this.cb_nbbuilds.setPreferredSize(new Dimension(50, 20));
        ((JLabel) this.cb_nbbuilds.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        this.cb_nbbuilds.setSelectedIndex(2);
        this.nbbuilds = Integer.parseInt(this.cb_nbbuilds.getSelectedItem().toString());

        // Define JCheckBox Objects
        this.check_side = new JCheckBox("Random Side");
        this.check_side.setSelected(false);
        this.check_side.setToolTipText("Get Random Side when generating Build");

        this.check_char = new JCheckBox("Random Character");
        if (this.srbg.getRandomCharacterStatus()) {
            this.check_char.setSelected(true);
        } else {
            this.check_char.setSelected(false);
        }
        this.check_char.setToolTipText("Get Random Character when generating Build");

        this.check_care = new JCheckBox("Care Perk");
        if (this.srbg.getNeedCare()) {
            this.check_care.setSelected(true);
        } else {
            this.check_care.setSelected(false);
        }
        this.check_care.setToolTipText("One Care Perk is Required (" + this.srbg.getCareListAsString() + ")");

        this.check_sprint = new JCheckBox("Sprint Perk");
        if (this.srbg.getNeedSprint()) {
            this.check_sprint.setSelected(true);
        } else {
            this.check_sprint.setSelected(false);
        }
        this.check_sprint.setToolTipText("One Sprint Perk is Required (" + this.srbg.getSprintListAsString() + ")");

        // Define ActionListener
        this.b_build.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update Side if desired
                if (check_side.isSelected()) {
                    srbg.setSide("Random");
                }
                // Get Character
                Character c = new Character(srbg.getSide());
                if (srbg.getRandomCharacterStatus()) {
                    c = srbg.getRandomCharacter();
                }
                // Update Var according to Side
                if (srbg.getSide().equals("Killer")) {
                    // Special Case => disabled JCheckBox & Care/Perk Status
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
                // Display loaded Parameters
                srbg.showParams();
                // Reset TextArea
                text.setText("");
                // Reset Table => Remove Data in TableModel (RowCount = 0)
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
                // Add a new Line for 1st Build
                model.setRowCount(1);
                // Generate Random Builds
                String fulltext = "\n ";
                Build b = new Build();
                for (int i = 1; i <= nbbuilds; i++) {
                    b = srbg.genRandomBuild("Random Build " + i);
                    fulltext = fulltext + b.show(false, "      ");
                    if (i < nbbuilds) {
                        fulltext = fulltext + "\n\n ";
                    }
                    if (i == 1) {
                        // Save 1st Build
                        b.setName("LastBuild");
                        b.setCharacter(c);
                        srbg.setBuildLast(b);
                        // Display 1st Build in Table
                        int nb = 0;
                        for (Perk p : b.getPerks()) {
                            table.getModel().setValueAt(p.getIconImage(3), 0, nb);
                            nb++;
                        }
                        // Display Character in Table
                        table.getModel().setValueAt(c.getIconImage(2), 0, 4);
                    }

                }
                // Display Text
                text.setText(fulltext);
                // Reformat String for Shell Display
                fulltext = fulltext.replaceFirst("\n", "\n#");
                fulltext = fulltext.replaceAll("\n\n", "\n#");
                System.out.println(fulltext + "\n");

            }
        });

        // Define ActionListener
        this.cb_nbperks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define Nb of Perks
                srbg.setNbPerksBuild(Integer.parseInt(cb_nbperks.getSelectedItem().toString()));
                if (srbg.getNbPerksBuild() < 2) {
                    // Special Case => disabled JCheckBox & Care/Sprint Status
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
        this.cb_nbbuilds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define Nb of Builds
                nbbuilds = Integer.parseInt(cb_nbbuilds.getSelectedItem().toString());
                System.out.println("# Number of wanted Builds = " + nbbuilds);
            }
        });

        // Define ItemListener
        this.check_char.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Define Set Char Boolean
                if (check_char.isSelected()) {
                    srbg.setRandomCharacterStatus(true);
                } else {
                    srbg.setRandomCharacterStatus(false);
                }
            }
        });

        // Define ItemListener
        this.check_care.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Retrieve & Define Needed Care Status
                if (check_care.isSelected()) {
                    srbg.setNeedCare(true);
                } else {
                    srbg.setNeedCare(false);
                }
            }
        });

        // Define ItemListener
        this.check_sprint.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Retrieve & Define Needed Sprint Status
                if (check_sprint.isSelected()) {
                    srbg.setNeedSprint(true);
                } else {
                    srbg.setNeedSprint(false);
                }
            }
        });

    }

}
