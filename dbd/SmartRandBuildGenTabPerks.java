package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * SmartRandBuildGenTabPerks
 *
 * @author GneHeHe (2019)
 *
 */
public class SmartRandBuildGenTabPerks extends JPanel {

    // Swing Components
    private JPanel pan_side, pan_button;
    private JScrollPane scrollPane;
    private JButton b_load, b_save, b_default, b_same, b_remote_syn;
    private JLabel lab_side;
    private JComboBox cb_side;
    private JFileChooser fileChooser;
    private PerkTable table;
    // SmartRandBuildGen Object 
    private SmartRandBuildGen srbg;
    // Synergy Filenames
    private final String s_syn_chars = "/master/dbd/data/syn_chars.txt";
    private final String s_syn_perks = "/master/dbd/data/syn_perks.txt";

    /**
     * Default Constructor
     *
     * @param srbg
     */
    public SmartRandBuildGenTabPerks(SmartRandBuildGen srbg) {

        // Set SmartRandBuildGen Object
        this.srbg = srbg;

        // Add Swing Components
        addComponents();

        // Create Subpanels
        pan_side = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_side.add(lab_side);
        pan_side.add(cb_side);

        pan_button = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_button.add(b_default);
        pan_button.add(b_same);
        pan_button.add(b_load);
        pan_button.add(b_save);
        pan_button.add(b_remote_syn);

        // Add Table to Panel
        scrollPane = new JScrollPane(table);

        // Set Layout & add Subpanels
        setLayout(new BorderLayout());
        add(pan_side, BorderLayout.NORTH);
        add(pan_button, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Define JLabel Objects
        lab_side = new JLabel("Display Perks from this Side: ");

        // Define JButton Objects
        b_load = new JButton("Open custom Weight File");
        b_save = new JButton("Save current Weight Table");
        b_same = new JButton("Set identical Weights");
        b_default = new JButton("Reload original Weights");
        b_remote_syn = new JButton("Update Synergy Rules");

        // Add Tooltips
        b_load.setToolTipText("Load a custom weight distribution for each perk");
        b_save.setToolTipText("Save the current weight distribution from the table in an output file");
        b_same.setToolTipText("Pure random builds will be generated (same weight for each perk, no perk constraint and no synergy rules)");
        b_default.setToolTipText("Reset each weight to its original value");
        b_remote_syn.setToolTipText("<html>Download last version of synergy rules from GitHub, then reload them<br>Backup is performed if custom synergy files were already present</html>");

        // Define JFileChooser Objects
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "ini");
        fileChooser.setFileFilter(filter);

        // Define JComboBox Objects for Side
        cb_side = new JComboBox(new String[]{srbg.s_side_surv, srbg.s_side_killer});
        cb_side.setPreferredSize(new Dimension(125, 25));
        ((JLabel) cb_side.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        if (srbg.getSide().equals(srbg.s_side_surv)) {
            cb_side.setSelectedIndex(0);
        } else {
            cb_side.setSelectedIndex(1);
        }

        // Create Table
        table = new PerkTable(srbg.weight_perk_max);
        table.setModel(new PerkTableModel(srbg));
        table.setRowHeight(58);
        table.centerText();
        table.setIconColumn(1);
        table.setColumnWeight(2);
        // Update Table
        ((PerkTableModel) table.getModel()).updateTable(cb_side.getSelectedItem().toString());

        // Define ActionListener
        b_load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = chooseFile(true);
                if (f != null) {
                    if (new File(f).exists()) {
                        try {
                            System.out.println("# Loading custom weight distribution from " + f);
                            // Load a custom Configuration File
                            srbg.readConfigFile(f);
                            // Update Model & Table
                            ((PerkTableModel) table.getModel()).updateTable(cb_side.getSelectedItem().toString());
                        } catch (Exception ex) {
                            Tools.getAlert("ERROR: Issues were encountered while processing the configuration file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Define ActionListener
        b_same.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set same Weight for Perks
                srbg.setSameWeight(1);
                // Disable Synergy & all Constraints
                srbg.setSynergyStatus(false);
                srbg.setConstraintsPerks(1, false);
                srbg.setConstraintsPerks(2, false);
                srbg.setConstraintsPerks(3, false);
                srbg.setConstraintsPerks(4, false);
                // Update Model & Table
                ((PerkTableModel) table.getModel()).updateTable(cb_side.getSelectedItem().toString());
            }
        });

        // Define ActionListener
        b_default.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("# All perks have loaded weights from start");
                // Load default Configuration File
                srbg.initConfigFile();
                // Update Model & Table
                ((PerkTableModel) table.getModel()).updateTable(cb_side.getSelectedItem().toString());
            }
        });

        // Define ActionListener
        b_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = chooseFile(false);
                if (f != null) {
                    try {
                        // Write Perk Distribution in Output File
                        srbg.saveConfigFile(f);
                    } catch (Exception ex) {
                        Tools.getAlert("ERROR: Issues were encountered while saving the configuration file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Define ActionListener
        b_remote_syn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Download last synergy rule files from GitHub
                try {
                    // Character-based Synergy File
                    boolean ok1 = false;
                    String output = srbg.getSynergy().s_chars_custom;
                    // Backup previous custom Synergy File
                    if (new File(output).exists()) {
                        System.out.println("# Moving existing File '" + new File(output).toPath() + "' to '" + new File("bak_" + output).toPath() + "' target File");
                        Files.move(new File(output).toPath(), new File("bak_" + output).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    URL website = new URL(SmartRandBuildGen.GIT_URL_RAW + s_syn_chars);
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(output);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    if (new File(output).exists()) {
                        ok1 = true;
                    }
                    // Perk-based Synergy File
                    boolean ok2 = false;
                    output = srbg.getSynergy().s_perks_custom;
                    // Backup previous custom Synergy File
                    if (new File(output).exists()) {
                        System.out.println("# Moving existing File '" + new File(output).toPath() + "' to '" + new File("bak_" + output).toPath() + "' target File");
                        Files.move(new File(output).toPath(), new File("bak_" + output).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    website = new URL(SmartRandBuildGen.GIT_URL_RAW + s_syn_perks);
                    rbc = Channels.newChannel(website.openStream());
                    fos = new FileOutputStream(output);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    if (new File(output).exists()) {
                        ok2 = true;
                    }
                    // Check
                    if (ok1 && ok2) {
                        // Reload Synergy Rules
                        srbg.getSynergy().readRulesAgain(srbg.b_verbose);
                    } else {
                        Tools.getAlert("ERROR: Issues were encountered while downloading the last synergy rules GitHub", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    Tools.getAlert("ERROR: Issues were encountered while downloading the last synergy rules GitHub", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Define ActionListener
        cb_side.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define Side
                String value = combo.getSelectedItem().toString();
                //srbg.setSide(value);
                // Update Table => Only Display Perks from active Side
                ((PerkTableModel) table.getModel()).updateTable(value);
            }
        });

    }

    /**
     * Open Dialog Window & Get Path of selected File
     *
     */
    private String chooseFile(boolean b) {
        // Set Default Directory
        String dir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(dir));
        // Set Default Output Filename
        String file = dir + File.separator + srbg.s_perk_custom;
        fileChooser.setSelectedFile(new File(file));
        int status;
        if (b) {
            // Open File Dialog
            status = fileChooser.showOpenDialog(this);
        } else {
            // Saving File Dialog
            status = fileChooser.showSaveDialog(this);
        }
        if (status == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

}
