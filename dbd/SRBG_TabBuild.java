package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * SRBG_TabBuild
 *
 * @author GneHeHe (2019)
 *
 */
public class SRBG_TabBuild extends JPanel {

    // Swing Components
    private JPanel pan_config, pan_table;
    private JScrollPane scrollPane;
    private JButton b_build, b_widget;
    private JComboBox cb_nbperks, cb_nbbuilds, cb_side, cb_char;
    private JLabel lab_nbperks, lab_nbbuilds;
    private JCheckBox check_cons1, check_cons2, check_cons3, check_cons4, check_syn;
    private DefaultComboBoxModel cbm_default;
    private JTextArea text;
    private JTable table_perks;
    // SRBG Object
    private SRBG srbg;
    // Additional Modes
    private String s_rand = "Rand";
    private String s_rand_surv = s_rand + "_Surv";
    private String s_rand_killer = s_rand + "_Killer";
    // Animation
    private SwingWorker<Void, Void> worker, worker_widget;
    private final Perk perk_gen = new Perk();
    private int anim_delay_trans = 500;
    private int anim_delay_perk = 300;
    private int anim_delay_char = 400;
    private int anim_loop_perk = 7;
    private int anim_loop_char = 6;
    public boolean b_ready;
    private final String s_sound = "data/sound.mp3";

    /**
     * Default Constructor
     *
     * @param srbg
     */
    public SRBG_TabBuild(SRBG srbg) {

        // Set SRBG Object
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
        pan_config.add(b_widget);

        pan_table = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_table.add(table_perks);

        // Create JScrollPane (hide bars)
        scrollPane = new JScrollPane(text);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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

        // SRBG Status
        b_ready = true;

        // Define JTable Object
        int size = 220;
        table_perks = new JTable(1, 5);
        table_perks.setRowHeight(size);
        table_perks.setShowHorizontalLines(false);
        table_perks.setShowVerticalLines(false);
        for (int i = 0; i < table_perks.getColumnCount(); i++) {
            TableColumn column = table_perks.getColumnModel().getColumn(i);
            column.setCellRenderer(new IconTableCellRenderer());
            column.setPreferredWidth(size);
        }

        // Define JButton Objects
        b_build = new JButton("Run");
        b_build.setToolTipText("<html>Generate random builds that match considered parameters & constraints<br><br>Click on generated build to export it as png picture (saved in working directory)</html>");
        b_widget = new JButton("Widget");
        b_widget.setToolTipText("<html>Open widget window for more convenient use of SRBG within Game<br><br>Click on Widget, then:<br><ul><li>Press SPACE or ENTER to generate random builds</li><br><li>Press ESCAPE to close the widget</li></ul></html>");

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
        cb_nbbuilds = new JComboBox(new Integer[]{1, 3, 5, 15, 30});
        cb_nbbuilds.setPreferredSize(new Dimension(50, 20));
        cb_nbbuilds.setToolTipText("<html>Define the number of builds to generate<br><br>The build with the highest score is graphically displayed<br><br>Meta builds or builds with 'combos perks' (favorable synergy) are associated with high scores<br><br>They tend to be returned when the number of desired builds is larger than 1!</html>");
        ((JLabel) cb_nbbuilds.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        cb_nbbuilds.setSelectedIndex(0);
        // Force 1 desired build in GUI
        srbg.setNbBuilds(Integer.parseInt(cb_nbbuilds.getSelectedItem().toString()));

        // Define JComboBox Object for Side
        cb_side = new JComboBox(new String[]{srbg.s_side_rand, s_rand_surv, s_rand_killer, srbg.s_side_surv, srbg.s_side_killer});
        cb_side.setSelectedIndex(0);
        cb_side.setPreferredSize(new Dimension(130, 25));
        cb_side.setToolTipText("<html>Define the side for the character:<ul><li>" + srbg.s_side_rand + ": random side & random character</li><br><li>" + s_rand_surv + ": survivor side & random character</li><br><li>" + s_rand_killer + ": killer side & random character</li><br><li>" + srbg.s_side_surv + ": survivor side & ability to chose character</li><br><li>" + srbg.s_side_killer + ": killer side & ability to chose character</li></ul></html>");
        ((JLabel) cb_side.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Define JComboBox Object for Character
        cb_char = new JComboBox();
        cb_char.setPreferredSize(new Dimension(130, 25));
        cb_char.setToolTipText("<html>Define the character:<br><ul><li>" + (srbg.getCharacterList(srbg.s_side_surv, false).size() - 1) + " survivors + generic survivor</li><br><li>" + (srbg.getCharacterList(srbg.s_side_killer, false).size() - 1) + " killers + generic killer</li></ul>Only available with non-random based selection of character</html>");
        ((JLabel) cb_char.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        updateComboBoxes();

        // Define JCheckBox Objects
        check_cons1 = new JCheckBox("Cons1");
        check_cons2 = new JCheckBox("Cons2");
        check_cons3 = new JCheckBox("Cons3");
        check_cons4 = new JCheckBox("Cons4");
        check_syn = new JCheckBox("Synergy");
        check_syn.setToolTipText("<html>Enable or disable the synergy rules<br><br>Perk weights are dynamically updated with respect to selected character & previous looted perks<br><br>The goal of 'perk constraints' & 'synergy rules' is to generate even better random builds</html>");
        updateCheckBoxes();
        check_cons1.setToolTipText("<html>Required perk from this specific subset in generated builds:<br><ul><li>" + srbg.getConstraints(1, srbg.s_side_surv) + "</li><br><li>" + srbg.getConstraints(1, srbg.s_side_killer) + "</li></ul></html>");
        check_cons2.setToolTipText("<html>Required perk from this specific subset in generated builds:<br><ul><li>" + srbg.getConstraints(2, srbg.s_side_surv) + "</li><br><li>" + srbg.getConstraints(2, srbg.s_side_killer) + "</li></ul></html>");
        check_cons3.setToolTipText("<html>Required perk from this specific subset in generated builds:<br><ul><li>" + srbg.getConstraints(3, srbg.s_side_surv) + "</li><br><li>" + srbg.getConstraints(3, srbg.s_side_killer) + "</li></ul></html>");
        check_cons4.setToolTipText("<html>Required perk from this specific subset in generated builds:<br><ul><li>" + srbg.getConstraints(4, srbg.s_side_surv) + "</li><br><li>" + srbg.getConstraints(4, srbg.s_side_killer) + "</li></ul></html>");

        // Init Animation 
        displayInit(table_perks, 3, 2);

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
                srbg.setNbBuilds(Integer.parseInt(cb_nbbuilds.getSelectedItem().toString()));
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
                // Generate Build
                genBuild(table_perks, 3, 2);
            }
        });

        // Define ActionListener
        b_widget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable Button
                b_widget.setEnabled(false);
                // Load Widget
                loadWidget();
            }
        });

        // Define ItemListener
        check_syn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Update Synergy Status
                if (check_syn.isSelected()) {
                    srbg.setSynergyStatus(true);
                    if (srbg.b_random) {
                        // Reload default Weights for logical Purpose
                        // Back to Random Mode OFF
                        srbg.readWeightsDefault();
                    }
                } else {
                    srbg.setSynergyStatus(false);
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

        // Define MouseListener
        table_perks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Define Output Filename
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String output = "build_random_" + sdf.format(System.currentTimeMillis()) + ".png";
                // Export Random Build as Picture
                Tools.saveComponentAsImage(table_perks, output, "PNG");
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
        if (srbg.getSynergyStatus()) {
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
        } else if (cb_side.getSelectedItem().equals(s_rand_surv)) {
            srbg.setSide(srbg.s_side_surv);
            cb_char.setModel(cbm_default);
            srbg.setCharacter(srbg.getCharacterRandom().getName());
        } else if (cb_side.getSelectedItem().equals(srbg.s_side_killer)) {
            srbg.setSide(srbg.s_side_killer);
            // All Killers
            cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList(srbg.s_side_killer, false).toArray()));
        } else if (cb_side.getSelectedItem().equals(s_rand_killer)) {
            srbg.setSide(srbg.s_side_killer);
            cb_char.setModel(cbm_default);
            srbg.setCharacter(srbg.getCharacterRandom().getName());
        } else if (cb_side.getSelectedItem().equals(srbg.s_side_rand)) {
            srbg.setSide(srbg.s_side_rand);
            cb_char.setModel(cbm_default);
        }
    }

    /**
     * Generate Builds (Main Method)
     *
     * @param table
     * @param size_perk
     * @param size_char
     */
    private void genBuild(JTable table, int size_perk, int size_char) {
        // Update JCheckBoxes
        updateCheckBoxes();
        // Monitor Random Feature
        boolean force_random = false;
        String mode = "";
        // Test with 'Rand' substring is important to catch 3 random modes
        if (cb_side.getSelectedItem().toString().startsWith(s_rand)) {
            force_random = true;
            mode = cb_side.getSelectedItem().toString();
            updateComboBoxes();
        }
        // Reset Table
        resetTable(table);
        // Define SwingWorker
        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // Disable Button
                b_build.setEnabled(false);
                // Reset Text
                String fulltext = "";
                text.setText(fulltext);
                // Display loaded Parameters
                srbg.showParams(false);
                // Generate Random Builds
                List<Build> l = srbg.genRandomBuilds("Build", false);
                // Generate Text
                for (int i = 0; i < srbg.getNbBuilds(); i++) {
                    fulltext = fulltext + "\n " + l.get(i).show(false, "     ") + "\n";
                }
                // Reset List
                l.clear();
                try {
                    // Display both Perk Animations & Build                    
                    displayBuild(table, size_perk, size_char);
                } catch (Exception ex) {
                    System.err.println("\n# ERROR: issues during perk animation => Exit");
                    System.err.println(ex.getMessage());
                    System.exit(0);
                }
                // Display Builds as Text in GUI
                text.setText(fulltext);
                // Display Builds as Text in Shell
                System.out.println("\n#" + fulltext.replaceAll("\n\n ", "\n# ").replaceFirst("\n", ""));
                // Enable Button
                b_build.setEnabled(true);
                // Update boolean
                b_ready = true;
                // End SwingWorker (doInBackground method)
                return null;
            }
        };
        // Execute SwingWorker
        worker.execute();
        // Back to Random Side if desired
        if (force_random) {
            cb_side.setSelectedItem(mode);
        }
    }

    /**
     * Display both Animation & Build
     *
     * @param table
     * @param size_perk
     * @param size_char
     * @throws java.lang.Exception
     */
    private void displayBuild(JTable table, int size_perk, int size_char) throws Exception {
        // Animation if Random Mode
        if (cb_side.getSelectedItem().toString().startsWith(s_rand)) {
            for (int i = 0; i < srbg.nb_perks_ref; i++) {
                table.getModel().setValueAt(srbg.perk_generic.getIconImage(size_perk), 0, i);
            }
            for (int i = 0; i < anim_loop_char; i++) {
                table.getModel().setValueAt(srbg.getCharacterRandom().getIconImage(size_char), 0, 4);
                Thread.sleep(anim_delay_char);
            }
            table.getModel().setValueAt(srbg.getCharacterGeneric().getIconImage(size_char), 0, 4);
            Thread.sleep(anim_delay_trans);
        }
        // Display Character
        table.getModel().setValueAt(srbg.getBestBuild().getCharacter().getIconImage(size_char), 0, 4);
        // Always display animation for Perks
        for (int i = 0; i < anim_loop_perk; i++) {
            for (int j = 0; j < srbg.getNbPerksBuild(); j++) {
                table.getModel().setValueAt(srbg.getPerkRandom().getIconImage(size_perk), 0, j);
            }
            Thread.sleep(anim_delay_perk);
        }
        // Play Sound
        Tools.playSound(s_sound);
        // Display Generic Perks
        for (int j = 0; j < srbg.getNbPerksBuild(); j++) {
            table.getModel().setValueAt(perk_gen.getIconImage(size_perk), 0, j);
        }
        Thread.sleep(anim_delay_trans);
        // Display Best Build in Table
        int nb = 0;
        for (Perk p : srbg.getBestBuild().getPerks()) {
            table.getModel().setValueAt(p.getIconImage(size_perk), 0, nb);
            nb++;
        }
    }

    /**
     * Init Animation
     *
     * @param table
     * @param size_perk
     * @param size_char
     * @throws java.lang.Exception
     */
    private void displayInit(JTable table, int size_perk, int size_char) {
        // Display Generic Perks
        for (int i = 0; i < srbg.nb_perks_ref; i++) {
            table.getModel().setValueAt(srbg.perk_generic.getIconImage(size_perk), 0, i);
        }
        // Display Generic Character
        table.getModel().setValueAt(srbg.getCharacterGeneric().getIconImage(size_char), 0, 4);
    }

    /**
     * Reset Build Table
     *
     * @param table
     */
    public void resetTable(JTable table) {
        // Reset Table => Remove Data in TableModel (RowCount = 0)
        DefaultTableModel model_default = (DefaultTableModel) table.getModel();
        model_default.setRowCount(0);
        // Add new Line
        model_default.setRowCount(1);
        // Update JTable using an Event
        model_default.fireTableDataChanged();
    }

    /**
     * Load Widget
     *
     */
    public void loadWidget() {
        // Define SwingWorker
        worker_widget = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {

                // Define Widget
                Widget widget = new Widget();

                // Init Animation 
                displayInit(widget.getTable(), 4, 3);

                // Define KeyListener
                widget.getTable().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int keyCode = e.getKeyCode();
                        // Generate Build after either ENTER or SPACE Keys have been Pressed
                        if (b_ready && ((keyCode == KeyEvent.VK_ENTER) || (keyCode == KeyEvent.VK_SPACE))) {
                            // SRBG Status
                            b_ready = false;
                            // Reset Table
                            resetTable(table_perks);
                            // Generate Build
                            genBuild(widget.getTable(), 4, 3);
                        }
                    }
                });

                // Define WindowListener
                widget.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        // Enable Button
                        b_widget.setEnabled(true);
                    }
                });

                // End SwingWorker (doInBackground method)
                return null;
            }
        };
        // Execute SwingWorker
        worker_widget.execute();

    }

}