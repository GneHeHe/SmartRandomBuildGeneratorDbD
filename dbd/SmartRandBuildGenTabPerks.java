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
 * @author GneHeHe (2018)
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
    private JTextField tf_perks;
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
        this.addComponents();

        // Create Subpanels
        this.pan_side = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.pan_side.add(this.lab_side);
        this.pan_side.add(this.cb_side);
        this.pan_side.add(this.tf_perks);

        this.pan_button = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.pan_button.add(this.b_default);
        this.pan_button.add(this.b_same);
        this.pan_button.add(this.b_load);
        this.pan_button.add(this.b_save);

        // Add Table to Panel
        this.scrollPane = new JScrollPane(this.table);

        // Set Layout & add Subpanels
        this.setLayout(new BorderLayout());
        this.add(this.pan_side, BorderLayout.NORTH);
        this.add(this.pan_button, BorderLayout.SOUTH);
        this.add(this.scrollPane, BorderLayout.CENTER);

    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Create Table
        this.table = new PerkTable();
        this.table.setModel(new PerkTableModel(this.srbg));
        this.table.setRowHeight(45);
        this.table.centerText();
        this.table.setIconColumn(1);
        this.table.setColumnWeight(2);
        this.table.setShowVerticalLines(false);
        // Update Table
        ((PerkTableModel) this.table.getModel()).updateTable();

        // Define JLabel Objects
        this.lab_side = new JLabel("Set active Side");

        // Define JTextField Objects
        this.tf_perks = new JTextField(24);
        this.tf_perks.setHorizontalAlignment(JTextField.CENTER);
        this.tf_perks.setEditable(false);
        this.updateText();

        // Define JButton Objects
        this.b_load = new JButton("Open custom Perk Distribution");
        this.b_save = new JButton("Save current Perk Distribution");
        this.b_same = new JButton("Set identical Weights");
        this.b_default = new JButton("Reload original Weights");

        // Define JFileChooser Objects
        this.fileChooser = new JFileChooser();
        this.fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "ini");
        this.fileChooser.setFileFilter(filter);

        // Define JComboBox Objects for Side
        this.cb_side = new JComboBox(new String[]{"Survivor", "Killer"});
        this.cb_side.setPreferredSize(new Dimension(125, 25));
        ((JLabel) this.cb_side.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        if (this.srbg.getSide().equals("Survivor")) {
            this.cb_side.setSelectedIndex(0);
        } else {
            this.cb_side.setSelectedIndex(1);
        }

        // Define ActionListener
        this.b_load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = chooseFile(true);
                if (f != null) {
                    if (new File(f).exists()) {
                        try {
                            System.out.println("# Loading custom weight distribution from " + f);
                            // Load a custom Configuration File
                            srbg.readConfigFile(f);
                            // Update Fields
                            updateText();
                            // Update Model & Table
                            ((PerkTableModel) table.getModel()).updateTable();
                        } catch (Exception ex) {
                            Tools.getAlert("ERROR: Issues were encountered while processing the configuration file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Define ActionListener
        this.b_same.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set same Weight for Perks
                srbg.setSameWeight(1);
                // Update Model & Table
                ((PerkTableModel) table.getModel()).updateTable();
            }
        });

        // Define ActionListener
        this.b_default.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("# All perks have loaded weights from start");
                // Load default Configuration File
                srbg.initConfigFile();
                // Update Fields
                updateText();
                // Update Model & Table
                ((PerkTableModel) table.getModel()).updateTable();
            }
        });

        // Define ActionListener
        this.b_save.addActionListener(new ActionListener() {
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
        this.cb_side.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define Side
                String value = combo.getSelectedItem().toString();
                srbg.setSide(value);
                // Update Field
                updateText();
                if (value.equals("Killer")) {
                    // Update Values
                    srbg.setNeedCare(false);
                    srbg.setNeedSprint(false);
                }
                // Update Table => Only Display Perks from active Side
                ((PerkTableModel) table.getModel()).updateTable();
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
        this.fileChooser.setCurrentDirectory(new File(dir));
        // Set Default Output Filename
        String file = dir + File.separator + "perk_db_custom.txt";
        this.fileChooser.setSelectedFile(new File(file));
        int status;
        if (b) {
            // Open File Dialog
            status = this.fileChooser.showOpenDialog(this);
        } else {
            // Saving File Dialog
            status = this.fileChooser.showSaveDialog(this);
        }
        if (status == JFileChooser.APPROVE_OPTION) {
            return this.fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * Update Text Field
     *
     */
    private void updateText() {
        tf_perks.setText(srbg.getNbPerksAll() + " loaded Perks (" + srbg.getNbPerksSide() + " active Perks)");
    }

}
