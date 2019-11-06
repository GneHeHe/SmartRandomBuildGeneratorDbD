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
        addComponents();

        // Create Subpanels
        pan_config = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_config.add(lab_nbperks);
        pan_config.add(cb_nbperks);
        pan_config.add(lab_nbbuilds);
        pan_config.add(cb_nbbuilds);
        pan_config.add(cb_side);
        pan_config.add(cb_char);
        pan_config.add(check_cons1);
        pan_config.add(check_cons2);
        pan_config.add(check_cons3);
        pan_config.add(check_cons4);
        pan_config.add(check_syn);
        pan_config.add(b_build);

        pan_table = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_table.add(table);

        // Create JScrollPane (hide bars)
        scrollPane = new JScrollPane(text);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Set Layout & add Subpanels
        setLayout(new BorderLayout());
        add(pan_config, BorderLayout.NORTH);
        add(pan_table, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Define JTable Objects
        int size = 220;
        table = new JTable(1, 5);
        table.setRowHeight(size);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setCellRenderer(new IconTableCellRenderer());
            column.setPreferredWidth(size);
        }

        // Define JButton Objects
        b_build = new JButton("Generate Builds");
        b_build.setToolTipText("Generate random builds that match parameters");

        // Define JLabel Objects
        lab_nbperks = new JLabel("  Nb Perks ");
        lab_nbbuilds = new JLabel("  Nb Builds ");

        // Define JTextArea Objects
        text = new JTextArea(30, 20);
        text.setEditable(false);
        text.setFont(new Font("Helvetica", Font.PLAIN, 16));

        // Define JComboBox Object for Perks
        cb_nbperks = new JComboBox(new Integer[]{1, 2, 3, 4});
        cb_nbperks.setPreferredSize(new Dimension(50, 20));
        cb_nbperks.setToolTipText("Define the number of perks in a build");
        ((JLabel) cb_nbperks.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        cb_nbperks.setSelectedItem(srbg.getNbPerksBuild());

        // Define JComboBox Object for Nb of Builds
        cb_nbbuilds = new JComboBox(new Integer[]{1, 3, 5, 7, 9});
        cb_nbbuilds.setPreferredSize(new Dimension(50, 20));
        cb_nbbuilds.setToolTipText("Define the number of builds to generate");
        ((JLabel) cb_nbbuilds.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        cb_nbbuilds.setSelectedIndex(1);
        nbbuilds = Integer.parseInt(cb_nbbuilds.getSelectedItem().toString());

        // Define JComboBox Object for Side
        cb_side = new JComboBox(new String[]{"Random", "Survivor", "Killer"});
        cb_side.setSelectedIndex(0);
        cb_side.setPreferredSize(new Dimension(100, 25));
        cb_side.setToolTipText("Define the active side");
        ((JLabel) cb_side.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Define JComboBox Object for Character
        cb_char = new JComboBox();
        cb_char.setPreferredSize(new Dimension(130, 25));
        cb_char.setToolTipText("Define the character (generic or specific character)");
        ((JLabel) cb_char.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        updateComboBoxes();

        // Define JCheckBox Objects
        check_cons1 = new JCheckBox("Cons1");
        check_cons2 = new JCheckBox("Cons2");
        check_cons3 = new JCheckBox("Cons3");
        check_cons4 = new JCheckBox("Cons4");
        check_syn = new JCheckBox("Synergy");
        check_syn.setToolTipText("Define the synergy rules status");
        updateCheckBoxes();

        // Define ActionListener
        cb_nbperks.addActionListener(new ActionListener() {
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
        cb_nbbuilds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define Nb of Builds
                nbbuilds = Integer.parseInt(cb_nbbuilds.getSelectedItem().toString());
                System.out.println("# Number of wanted Builds = " + nbbuilds);
            }
        });

        // Define ActionListener
        cb_side.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Update JComboBox for Side & related Characters
                updateComboBoxes();
                updateCheckBoxes();
            }
        });

        // Define ActionListener
        cb_char.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define the Character
                String s = cb_char.getSelectedItem().toString();
                srbg.setCharacter(s);
            }
        });

        // Define ActionListener
        b_build.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                srbg.verbose = false;
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
                srbg.showParams(false);
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
        check_syn.addItemListener(new ItemListener() {
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
        check_cons1.addItemListener(new ItemListener() {
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
        check_cons2.addItemListener(new ItemListener() {
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
        check_cons3.addItemListener(new ItemListener() {
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
        check_cons4.addItemListener(new ItemListener() {
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
        if (cb_side.getSelectedItem().equals("Survivor")) {
            // All Survivors
            cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList("Survivor", false).toArray()));
        } else if (cb_side.getSelectedItem().equals("Killer")) {
            // All Killers
            cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList("Killer", false).toArray()));
        } else if (cb_side.getSelectedItem().equals("Random")) {
            if (srbg.getSide().equals("Survivor")) {
                // Generic Survivor
                cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList("Survivor", true).toArray()));
            } else {
                // Generic Killer
                cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList("Killer", true).toArray()));
            }
        }
        // Select 1st Item
        cb_char.setSelectedIndex(0);
        // Update Character
        srbg.setCharacter(cb_char.getSelectedItem().toString());
    }

}
