package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * SRBG_TabPerk
 *
 * @author GneHeHe (2019)
 *
 */
public class SRBG_TabPerk extends JPanel {

    // Swing Components
    private JPanel pan_side, pan_button;
    private JScrollPane scrollPane;
    private JButton b_load, b_save, b_default, b_same, b_remote_syn;
    private JLabel lab_side;
    private JComboBox cb_side;
    private JFileChooser fileChooser;
    public TablePerk table;
    // SRBG Object 
    private SRBG srbg;
    // Synergy Filenames
    private final String s_syn_chars = "/master/dbd/data/syn_chars.txt";
    private final String s_syn_perks = "/master/dbd/data/syn_perks.txt";

    /**
     * Default Constructor
     *
     * @param srbg
     */
    public SRBG_TabPerk(SRBG srbg) {

        // Set SRBG Object
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
        b_same = new JButton("Generate Pure Random Builds");
        b_default = new JButton("Reload Original Weights");
        b_remote_syn = new JButton("Update Synergy Rules");

        // Add Tooltips
        b_load.setToolTipText("Load a custom weight distribution for each perk");
        b_save.setToolTipText("Save the current weight distribution from the table in an output file");
        b_same.setToolTipText("Disable all features & constraints, same weight for each perk: pure random builds will be generated");
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
        table = new TablePerk(srbg.weight_perk_max);
        table.setModel(new TableModelPerk(srbg));
        table.setRowHeight(58);
        table.centerText();
        table.setIconColumn(1);
        table.setIconColumn(2);
        table.setColumnWeight(3);
        // Update Table
        ((TableModelPerk) table.getModel()).updateTable(cb_side.getSelectedItem().toString());

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                if (column != 1) {
                    sortKeys.clear();
                    sortKeys.add(new RowSorter.SortKey(column, SortOrder.ASCENDING));
                    sorter.setSortKeys(sortKeys);
                    sorter.sort();
                    //table.repaint();
                }
            }
        });

        // Define ActionListener
        b_load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = chooseFile(true);
                if (f != null) {
                    if (new File(f).exists()) {
                        try {
                            System.out.println("# Loading custom weight distribution from " + f);
                            // Load a custom Weight File
                            srbg.readWeights(f);
                            // Update Model & Table
                            ((TableModelPerk) table.getModel()).updateTable(cb_side.getSelectedItem().toString());
                        } catch (Exception ex) {
                            Tools.getAlert("ERROR: Issues were encountered while processing the weight file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
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
                ((TableModelPerk) table.getModel()).updateTable(cb_side.getSelectedItem().toString());
            }
        });

        // Define ActionListener
        b_default.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("\n# All perks have loaded weights from start");
                // Load default Weight File
                srbg.readWeightsDefault();
                // Update Model & Table
                ((TableModelPerk) table.getModel()).updateTable(cb_side.getSelectedItem().toString());
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
                        Tools.getAlert("ERROR: Issues were encountered while saving the weight file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
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
                    URL website = new URL(srbg.GIT_URL_RAW + s_syn_chars);
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
                    website = new URL(srbg.GIT_URL_RAW + s_syn_perks);
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
                ((TableModelPerk) table.getModel()).updateTable(value);
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
