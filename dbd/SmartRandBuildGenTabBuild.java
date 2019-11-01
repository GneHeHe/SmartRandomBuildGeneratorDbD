package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * SmartRandBuildGenTabBuild
 *
 * @author GneHeHe (2019)
 *
 */
public class SmartRandBuildGenTabBuild extends JPanel {

    // Swing Components
    private JPanel pan_config, pan_table;
    private JScrollPane scrollPane;
    private JButton b_build;
    private JComboBox cb_nbperks, cb_nbbuilds, cb_side, cb_char;
    private JLabel lab_nbperks, lab_nbbuilds;
    private JCheckBox check_cons1, check_cons2, check_cons3, check_cons4, check_syn;
    private JTextArea text;
    private JTable table;
    // SmartRandBuildGen Object
    private SmartRandBuildGen srbg;
    // Nb of Random Builds
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
        this.pan_config.add(this.cb_side);
        this.pan_config.add(this.cb_char);
        this.pan_config.add(this.check_cons1);
        this.pan_config.add(this.check_cons2);
        this.pan_config.add(this.check_cons3);
        this.pan_config.add(this.check_cons4);
        this.pan_config.add(this.check_syn);
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
        int size = 220;
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
        this.b_build = new JButton("Generate Builds");
        this.b_build.setToolTipText("Generate random builds that match parameters");

        // Define JLabel Objects
        this.lab_nbperks = new JLabel("  Nb Perks ");
        this.lab_nbbuilds = new JLabel("  Nb Builds ");

        // Define JTextArea Objects
        this.text = new JTextArea(30, 20);
        this.text.setEditable(false);
        this.text.setFont(new Font("Helvetica", Font.PLAIN, 16));

        // Define JComboBox Object for Perks
        this.cb_nbperks = new JComboBox(new Integer[]{1, 2, 3, 4});
        this.cb_nbperks.setPreferredSize(new Dimension(50, 20));
        this.cb_nbperks.setToolTipText("Define the number of perks in a build");
        ((JLabel) this.cb_nbperks.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        this.cb_nbperks.setSelectedItem(this.srbg.getNbPerksBuild());

        // Define JComboBox Object for Nb of Builds
        this.cb_nbbuilds = new JComboBox(new Integer[]{1, 3, 5, 7, 9});
        this.cb_nbbuilds.setPreferredSize(new Dimension(50, 20));
        this.cb_nbbuilds.setToolTipText("Define the number of builds to generate");
        ((JLabel) this.cb_nbbuilds.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        this.cb_nbbuilds.setSelectedIndex(1);
        this.nbbuilds = Integer.parseInt(this.cb_nbbuilds.getSelectedItem().toString());

        // Define JComboBox Object for Side
        this.cb_side = new JComboBox(new String[]{"Random", "Survivor", "Killer"});
        this.cb_side.setSelectedIndex(0);
        this.cb_side.setPreferredSize(new Dimension(100, 25));
        this.cb_side.setToolTipText("Define the active side");
        ((JLabel) this.cb_side.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Define JComboBox Object for Character
        this.cb_char = new JComboBox();
        this.cb_char.setPreferredSize(new Dimension(130, 25));
        this.cb_char.setToolTipText("Define the character (generic or specific character)");
        ((JLabel) this.cb_char.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        updateComboBoxes();

        // Define JCheckBox Objects
        this.check_cons1 = new JCheckBox("Cons1");
        this.check_cons2 = new JCheckBox("Cons2");
        this.check_cons3 = new JCheckBox("Cons3");
        this.check_cons4 = new JCheckBox("Cons4");
        this.check_syn = new JCheckBox("Synergy");
        this.check_syn.setToolTipText("Define the synergy rules status");
        updateCheckBoxes();

        // Define ActionListener
        this.cb_nbperks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define Nb of Perks
                srbg.setNbPerksBuild(Integer.parseInt(cb_nbperks.getSelectedItem().toString()));
                if (srbg.getNbPerksBuild() < 4) {
                    // Disable Constraints
                    check_cons1.setSelected(false);
                    srbg.setConstraintsPerks(1, false);
                    check_cons2.setSelected(false);
                    srbg.setConstraintsPerks(2, false);
                    check_cons3.setSelected(false);
                    srbg.setConstraintsPerks(3, false);
                    check_cons4.setSelected(false);
                    srbg.setConstraintsPerks(4, false);
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

        // Define ActionListener
        this.cb_side.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Update JComboBox for Side & related Characters
                updateComboBoxes();
                updateCheckBoxes();
            }
        });

        // Define ActionListener
        this.cb_char.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define the Character
                String s = cb_char.getSelectedItem().toString();
                srbg.setCharacter(s);
            }
        });

        // Define ActionListener
        this.b_build.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update JCheckBoxes
                updateCheckBoxes();
                // Monitor Random Feature
                boolean force_random = false;
                if (cb_side.getSelectedItem().toString().equals("Random")) {
                    force_random = true;
                    // Define Random Side
                    srbg.setSide(srbg.choseSideRandom());
                    // Update JComboBox for Side & related Characters
                    updateComboBoxes();
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
                List l = new ArrayList();
                for (int i = 1; i <= nbbuilds; i++) {
                    b = srbg.genRandomBuild("Build " + i);
                    l.add(b);
                    fulltext = fulltext + b.show(false, "     ");
                    if (i < nbbuilds) {
                        fulltext = fulltext + "\n\n ";
                    }
                }
                // Retrieve & Set Best Build                
                b = Build.getBestBuild(l);
                b.setName("Best Build");
                srbg.setBestBuild(b);
                // Display Best Build in Table
                int nb = 0;
                for (Perk p : b.getPerks()) {
                    table.getModel().setValueAt(p.getIconImage(3), 0, nb);
                    nb++;
                }
                // Display Character in Table
                table.getModel().setValueAt(b.getCharacter().getIconImage(2), 0, 4);
                // Display Text
                text.setText(fulltext);
                // Reformat String for Shell Display
                fulltext = fulltext.replaceFirst("\n", "\n#").replaceAll("\n\n", "\n#");
                System.out.println(fulltext + "\n");
                // Set Random Side if Needed
                if (force_random) {
                    cb_side.setSelectedItem("Random");
                }
            }
        });

        // Define ItemListener
        this.check_syn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Update Synergy Status
                if (check_syn.isSelected()) {
                    srbg.setSynergy(true);
                } else {
                    srbg.setSynergy(false);
                }
            }
        });

        // Define ItemListener
        this.check_cons1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Update Constraint Status
                if (check_cons1.isSelected()) {
                    srbg.setConstraintsPerks(1, true);
                } else {
                    srbg.setConstraintsPerks(1, false);
                }
                // Update JCheckBoxes
                updateCheckBoxes();
            }
        });

        // Define ItemListener
        this.check_cons2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Update Constraint Status
                if (check_cons2.isSelected()) {
                    srbg.setConstraintsPerks(2, true);
                } else {
                    srbg.setConstraintsPerks(2, false);
                }
                // Update JCheckBoxes
                updateCheckBoxes();
            }
        });

        // Define ItemListener
        this.check_cons3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Update Constraint Status
                if (check_cons3.isSelected()) {
                    srbg.setConstraintsPerks(3, true);
                } else {
                    srbg.setConstraintsPerks(3, false);
                }
                // Update JCheckBoxes
                updateCheckBoxes();
            }
        });

        // Define ItemListener
        this.check_cons4.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Update Constraint Status
                if (check_cons4.isSelected()) {
                    srbg.setConstraintsPerks(4, true);
                } else {
                    srbg.setConstraintsPerks(4, false);
                }
                // Update JCheckBoxes
                updateCheckBoxes();
            }
        });

    }

    /**
     * Update JCheckBoxes to monitor Constraints
     *
     */
    public void updateCheckBoxes() {
        // Update Tooltips for Constraints
        check_cons1.setToolTipText("Required perk from subset " + srbg.getConstraints(1, srbg.getSide()));
        check_cons2.setToolTipText("Required perk from subset " + srbg.getConstraints(2, srbg.getSide()));
        check_cons3.setToolTipText("Required perk from subset " + srbg.getConstraints(3, srbg.getSide()));
        check_cons4.setToolTipText("Required perk from subset " + srbg.getConstraints(4, srbg.getSide()));
        if (srbg.getConstraintsPerks(1)) {
            check_cons1.setSelected(true);
        } else {
            check_cons1.setSelected(false);
        }
        if (srbg.getConstraintsPerks(2)) {
            check_cons2.setSelected(true);
        } else {
            check_cons2.setSelected(false);
        }
        if (srbg.getConstraintsPerks(3)) {
            check_cons3.setSelected(true);
        } else {
            check_cons3.setSelected(false);
        }
        if (srbg.getConstraintsPerks(4)) {
            check_cons4.setSelected(true);
        } else {
            check_cons4.setSelected(false);
        }
        if (srbg.getSynergy()) {
            check_syn.setSelected(true);
        } else {
            check_syn.setSelected(false);
        }
    }

    /**
     * Update JComboBoxes to monitor Side and related Characters
     *
     */
    public void updateComboBoxes() {
        if (this.cb_side.getSelectedItem().equals("Survivor")) {
            // All Survivors
            this.cb_char.setModel(new DefaultComboBoxModel(this.srbg.getCharacterList("Survivor", false).toArray()));
        } else if (this.cb_side.getSelectedItem().equals("Killer")) {
            // All Killers
            this.cb_char.setModel(new DefaultComboBoxModel(this.srbg.getCharacterList("Killer", false).toArray()));
        } else if (this.cb_side.getSelectedItem().equals("Random")) {
            if (this.srbg.getSide().equals("Survivor")) {
                // Generic Survivor
                this.cb_char.setModel(new DefaultComboBoxModel(this.srbg.getCharacterList("Survivor", true).toArray()));
            } else {
                // Generic Killer
                this.cb_char.setModel(new DefaultComboBoxModel(this.srbg.getCharacterList("Killer", true).toArray()));
            }
        }
        // Select 1st Item
        this.cb_char.setSelectedIndex(0);
        // Update Character
        this.srbg.setCharacter(this.cb_char.getSelectedItem().toString());
    }

}
