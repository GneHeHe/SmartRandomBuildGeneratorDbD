package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
    private JButton b_load, b_save, b_default, b_same;
    private JLabel lab_side;
    private JComboBox cb_side;
    private JFileChooser fileChooser;
    private PerkTable table;
    // SmartRandBuildGen Object 
    private SmartRandBuildGen srbg;

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

        // Add Tooltips
        b_load.setToolTipText("Load a custom weight distribution for each perk");
        b_save.setToolTipText("Save the current weight distribution from the table in an output file");
        b_same.setToolTipText("Pure random builds will be generated (same weight for each perk, no perk constraint and no synergy rules)");
        b_default.setToolTipText("Reset each weight to its original value");
        
        // Define JFileChooser Objects
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "ini");
        fileChooser.setFileFilter(filter);

        // Define JComboBox Objects for Side
        cb_side = new JComboBox(new String[]{"Survivor", "Killer"});
        cb_side.setPreferredSize(new Dimension(125, 25));
        ((JLabel) cb_side.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        if (srbg.getSide().equals("Survivor")) {
            cb_side.setSelectedIndex(0);
        } else {
            cb_side.setSelectedIndex(1);
        }

        // Create Table
        table = new PerkTable();
        table.setModel(new PerkTableModel(srbg));
        table.setRowHeight(58);
        table.centerText();
        table.setIconColumn(1);
        table.setColumnWeight(2);
        // Update Table
        ((PerkTableModel) table.getModel()).updateTable(cb_side.getSelectedItem().toString());

        // Check
        if (table.getWeightMax() >= srbg.weight_perk_max) {
            Tools.getAlert("ERROR: Synergy Feature is impossible because the highest tolerated Weight for Perks is too small ( " + srbg.weight_perk_max + " vs " + table.getWeightMax() + " ) => Exit", "Warning", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

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
                srbg.setSynergy(false);
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
