package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

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
    private JButton b_load, b_save, b_default, b_same, b_rand;
    private JLabel lab_side;
    private JComboBox cb_side;
    private JFileChooser fileChooser;
    // Table Objects
    private MyTable table;
    private MyTableModel mymodel;
    // SmartRandBuildGen Object 
    public SmartRandBuildGen srbg;

    /**
     * Default Constructor
     *
     * @param myBuilder
     */
    public SmartRandBuildGenTabPerks(SmartRandBuildGen myBuilder) {

        // Set SmartRandBuildGen Object
        this.srbg = myBuilder;

        // Add Swing Components
        addComponents();

        // Create Subpanels
        pan_side = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_side.add(lab_side);
        pan_side.add(cb_side);
        pan_side.add(b_rand);

        pan_button = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_button.add(b_default);
        pan_button.add(b_same);
        pan_button.add(b_load);
        pan_button.add(b_save);

        // Add the table to Panel
        scrollPane = new JScrollPane(table);

        // Set the Layout and add Subpanels
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
        lab_side = new JLabel("Select the Side");

        // Define JButton Objects
        b_rand = new JButton("Automatically Select Side");
        b_load = new JButton("Open Custom Perk Distribution");
        b_save = new JButton("Save Current Perk Distribution");
        b_same = new JButton("Set Identical Weights");
        b_default = new JButton("Set Default Weights");

        // Define JFileChooser Objects
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Create Table & Model
        mymodel = new MyTableModel(srbg);
        mymodel.updateTable();
        table = new MyTable();
        table.setModel(mymodel);
        table.setRowHeight(45);
        table.centerText();
        table.setColumnPerk(1);
        table.setColumnWeight(2);

        // Define JComboBox Objects for Side
        cb_side = new JComboBox(new String[]{"Survivor", "Killer"});
        cb_side.setPreferredSize(new Dimension(125, 30));
        ((JLabel) cb_side.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        if (srbg.getSide().equals("Survivor")) {
            cb_side.setSelectedIndex(0);
        } else {
            cb_side.setSelectedIndex(1);
        }

        // Define ActionListener
        b_rand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double p = Math.random();
                // Slight Bias toward other Side
                double offset = 0.2;
                if (srbg.getSide().equals("Survivor")) {
                    p = p - offset;
                } else {
                    p = p + offset;
                }
                // Select Side according to Random Value
                if (p > 0.5) {
                    cb_side.setSelectedIndex(0);
                } else {
                    cb_side.setSelectedIndex(1);
                }
                getAlert("You will play on the " + srbg.getSide() + " side for your next round !", "Message from The Entity", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Define ActionListener
        b_load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = chooseFile(true);
                if (new File(f).exists()) {
                    try {
                        // Load a custom Configuration File
                        srbg.readConfigFile(f, "\t");
                        // Update Model & Table
                        mymodel.updateTable();
                    } catch (Exception ex) {
                        getAlert("ERROR: Issues were encountered during process of the configuration file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Define ActionListener
        b_same.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the same Weight for each Perk
                srbg.setSameWeight(1);
                // Update Model & Table
                mymodel.updateTable();
            }
        });

        // Define ActionListener
        b_default.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputStream is = srbg.getClass().getResourceAsStream(srbg.getConfigDef());
                if (is != null) {
                    // Load default Configuration File
                    srbg.readConfigFile(is, "\t");
                    // Update Model & Table
                    mymodel.updateTable();
                } else {
                    getAlert("ERROR: Issues were encountered during process of default configuration file " + srbg.getConfigDef(), "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Define ActionListener
        b_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Define an Output Configuration File
                String f = chooseFile(false);
                try {
                    // Write the Perk Distribution in the Output File
                    srbg.writeConfigFile(f, "\t");
                } catch (Exception ex) {
                    getAlert("ERROR: Issues were encountered during saving of configuration file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Define ActionListener
        cb_side.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve and Define the Side
                String value = combo.getSelectedItem().toString();
                if (value.equals("Survivor")) {
                    srbg.setSide(value);
                } else if (value.equals("Killer")) {
                    srbg.setSide(value);
                    // Update Values
                    srbg.setNeedCare(false);
                    srbg.setNeedExhaust(false);
                } else {
                    getAlert("ERROR: Wrong Side", "Warning", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("# Selected Side = " + srbg.getSide());
                // Update Table => Only Display Perks from the current Side                
                mymodel.updateTable();
            }
        });

    }

    /**
     * Display a message in a windows
     *
     * @param msg the string to display
     * @param title the title of the window
     * @param type the type of alert (error, information ...)
     */
    private void getAlert(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    /**
     * Return the path of the selected file
     */
    private String chooseFile(boolean b) {
        // Set Default Directory
        String dir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(dir));
        // Set Default Output Filename
        String file = dir + File.separator + "perks_db_custom.txt";
        fileChooser.setSelectedFile(new File(file));
        if (b) {
            // Open File Dialog
            fileChooser.showOpenDialog(this);
        } else {
            // Saving File Dialog
            fileChooser.showSaveDialog(this);
        }
        return fileChooser.getSelectedFile().getAbsolutePath();
    }

}
