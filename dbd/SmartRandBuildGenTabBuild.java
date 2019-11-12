package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
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
    private DefaultComboBoxModel cbm_default;
    private DefaultTableModel model_default;
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
        b_build.setToolTipText("Generate random builds that match parameters and constraints");

        // Define JLabel Objects
        lab_nbperks = new JLabel("  Nb Perks ");
        lab_nbbuilds = new JLabel("  Nb Builds ");

        // Define JTextArea Objects
        text = new JTextArea(30, 20);
        text.setEditable(false);
        text.setFont(new Font("Helvetica", Font.PLAIN, 16));

        // Default ComboBox Model
        cbm_default = new DefaultComboBoxModel();

        // Define JComboBox Object for Perks
        cb_nbperks = new JComboBox(new Integer[]{1, 2, 3, 4});
        cb_nbperks.setPreferredSize(new Dimension(50, 20));
        cb_nbperks.setToolTipText("Define the number of desired perks in a build");
        ((JLabel) cb_nbperks.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        cb_nbperks.setSelectedItem(srbg.getNbPerksBuild());

        // Define JComboBox Object for Nb of Builds
        cb_nbbuilds = new JComboBox(new Integer[]{1, 2, 3, 4, 5, 7, 9});
        cb_nbbuilds.setPreferredSize(new Dimension(50, 20));
        cb_nbbuilds.setToolTipText("<html>Define the number of builds to generate<br>The build with the highest score is graphically displayed<br>Meta builds or builds with combos perks (favorable synergy) are associated with high scores<br>They tend to be returned when the number of desired builds is larger than 1!</html>");

        ((JLabel) cb_nbbuilds.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        cb_nbbuilds.setSelectedIndex(1);
        nbbuilds = Integer.parseInt(cb_nbbuilds.getSelectedItem().toString());

        // Define JComboBox Object for Side
        cb_side = new JComboBox(new String[]{srbg.s_side_rand, srbg.s_side_surv, srbg.s_side_killer});
        cb_side.setSelectedIndex(0);
        cb_side.setPreferredSize(new Dimension(100, 25));
        cb_side.setToolTipText("Define the active side (random or specific side)");
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
        check_cons1.setToolTipText("<html>Required perk from specific subset in generated builds:<br><ul><li>" + srbg.getConstraints(1, srbg.s_side_surv) + "</li><br><li>" + srbg.getConstraints(1, srbg.s_side_killer) + "</li></ul></html>");
        check_cons2.setToolTipText("<html>Required perk from specific subset in generated builds:<br><ul><li>" + srbg.getConstraints(2, srbg.s_side_surv) + "</li><br><li>" + srbg.getConstraints(2, srbg.s_side_killer) + "</li></ul></html>");
        check_cons3.setToolTipText("<html>Required perk from specific subset in generated builds:<br><ul><li>" + srbg.getConstraints(3, srbg.s_side_surv) + "</li><br><li>" + srbg.getConstraints(3, srbg.s_side_killer) + "</li></ul></html>");
        check_cons4.setToolTipText("<html>Required perk from specific subset in generated builds:<br><ul><li>" + srbg.getConstraints(4, srbg.s_side_surv) + "</li><br><li>" + srbg.getConstraints(4, srbg.s_side_killer) + "</li></ul></html>");

        // Define ActionListener
        cb_nbperks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                // Retrieve & Define Nb of Builds
                nbbuilds = Integer.parseInt(cb_nbbuilds.getSelectedItem().toString());
                System.out.println("# Number of wanted Builds = " + nbbuilds);
            }
        });

        // Define ActionListener
        cb_side.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update JComboBox for Side & related Characters
                updateComboBoxes();
                updateCheckBoxes();
            }
        });

        // Define ActionListener
        cb_char.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve & Define the Character
                String s = cb_char.getSelectedItem().toString();
                srbg.setCharacter(s);
            }
        });

        // Define ActionListener
        b_build.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update JCheckBoxes
                updateCheckBoxes();
                // Monitor Random Feature
                boolean force_random = false;
                if (cb_side.getSelectedItem().toString().equals(srbg.s_side_rand)) {
                    force_random = true;
                    updateComboBoxes();
                }
                // Display loaded Parameters
                srbg.showParams(false);
                // Reset Table => Remove Data in TableModel (RowCount = 0)
                model_default = (DefaultTableModel) table.getModel();
                model_default.setRowCount(0);
                // Add a new Line for 1st Build
                model_default.setRowCount(1);
                // Generate Random Builds
                Build b = new Build();
                List<Build> l = new ArrayList();
                for (int i = 1; i <= nbbuilds; i++) {
                    b = srbg.genRandomBuild("Build " + i);
                    l.add(b);
                }
                // Sort Builds
                Collections.sort(l);
                // Generate Text
                String fulltext = "";
                for (int i = 0; i < nbbuilds; i++) {
                    fulltext = fulltext + "\n " + l.get(i).show(false, "     ") + "\n";
                }
                // Display Builds in GUI
                text.setText(fulltext);
                // Display Builds in Shell
                System.out.println("#" + fulltext.replaceAll("\n\n ", "\n# ").replaceFirst("\n", ""));
                // Get & Display Best Build
                b = l.get(0);
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
                // Back to Random Side if desired
                if (force_random) {
                    cb_side.setSelectedItem(srbg.s_side_rand);
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
        if (cb_side.getSelectedItem().equals(srbg.s_side_surv)) {
            srbg.setSide(srbg.s_side_surv);
            // All Survivors
            cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList(srbg.s_side_surv, false).toArray()));
        } else if (cb_side.getSelectedItem().equals(srbg.s_side_killer)) {
            srbg.setSide(srbg.s_side_killer);
            // All Killers
            cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList(srbg.s_side_killer, false).toArray()));
        } else if (cb_side.getSelectedItem().equals(srbg.s_side_rand)) {
            srbg.setSide(srbg.s_side_rand);
            cb_char.setModel(cbm_default);
        }
    }

}
