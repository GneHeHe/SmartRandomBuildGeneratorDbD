package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * SmartRandBuildGenTabData
 *
 * @author GneHeHe (2019)
 *
 */
public class SmartRandBuildGenTabData extends JPanel {

    // Swing Components
    private JPanel pan_all, pan_build, pan_perks, pan_button;
    private JScrollPane scrollPane;
    private JButton b_load, b_save, b_add, b_add_last, b_remove, b_rand, b_reload, b_reload_remote;
    private JLabel lab_filter, lab_side, lab_char, lab_perk1, lab_perk2, lab_perk3, lab_perk4;
    private JComboBox cb_side, cb_char, cb_perk1, cb_perk2, cb_perk3, cb_perk4;
    private JFileChooser fileChooser;
    private JTextField tf_name, tf_expr, tf_build;
    private BuildTable table;
    private final String s_build_remote = "build_db_remote.txt";
    private final String s_build_custom = "build_db_custom.txt";
    // SmartRandBuildGen Object 
    private SmartRandBuildGen srbg;

    /**
     * Default Constructor
     *
     * @param srbg
     */
    public SmartRandBuildGenTabData(SmartRandBuildGen srbg) {

        // Set SmartRandBuildGen Object
        this.srbg = srbg;

        // Add Swing Components
        addComponents();

        // Create Subpanels
        pan_all = new JPanel();
        pan_all.setLayout(new BoxLayout(pan_all, BoxLayout.Y_AXIS));

        pan_button = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_button.add(tf_build);
        pan_button.add(lab_filter);
        pan_button.add(tf_expr);
        pan_button.add(b_rand);
        pan_button.add(b_reload);
        pan_button.add(b_reload_remote);
        pan_button.add(b_load);
        pan_button.add(b_save);

        pan_build = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_build.add(tf_name);
        pan_build.add(lab_side);
        pan_build.add(cb_side);
        pan_build.add(lab_char);
        pan_build.add(cb_char);
        pan_build.add(b_add_last);
        pan_build.add(b_add);
        pan_build.add(b_remove);

        pan_perks = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan_perks.add(lab_perk1);
        pan_perks.add(cb_perk1);
        pan_perks.add(lab_perk2);
        pan_perks.add(cb_perk2);
        pan_perks.add(lab_perk3);
        pan_perks.add(cb_perk3);
        pan_perks.add(lab_perk4);
        pan_perks.add(cb_perk4);

        pan_all.add(pan_build);
        pan_all.add(pan_perks);

        // Add Table to Panel
        scrollPane = new JScrollPane(table);

        // Set Layout & add Subpanels
        setLayout(new BorderLayout());
        add(pan_button, BorderLayout.NORTH);
        add(pan_all, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Create Table & Model
        table = new BuildTable();
        table.setModel(new BuildTableModel(srbg));
        table.setSorter();
        table.setRowHeight(69);
        table.centerText();
        table.setIconColumn(3);
        table.setIconColumn(4);
        table.setIconColumn(5);
        table.setIconColumn(6);
        table.setIconColumn(7);
        table.setShowVerticalLines(false);

        // Define JLabel Objects
        lab_filter = new JLabel(" Filter Rows ");
        lab_side = new JLabel(" Side ");
        lab_char = new JLabel(" Character ");
        lab_perk1 = new JLabel(" Perk 1 ");
        lab_perk2 = new JLabel("  Perk 2 ");
        lab_perk3 = new JLabel("  Perk 3 ");
        lab_perk4 = new JLabel("  Perk 4 ");

        // Define JTextField Objects
        tf_name = new JTextField(12);
        tf_name.setText("MyBuild");
        tf_name.setHorizontalAlignment(JTextField.CENTER);
        tf_name.setEditable(true);
        tf_name.setToolTipText("Define name of build");
        tf_expr = new JTextField(8);
        tf_expr.setText("");
        tf_expr.setHorizontalAlignment(JTextField.CENTER);
        tf_expr.setEditable(true);
        tf_expr.setToolTipText("Case insensitive search (regular expressions can be used)");
        tf_build = new JTextField(8);
        tf_build.setText(table.getRowCount() + " Builds");
        tf_build.setHorizontalAlignment(JTextField.CENTER);
        tf_build.setEditable(false);

        // Define JButton Objects
        b_rand = new JButton("Random Select");
        b_reload = new JButton("Reload DB");
        b_reload_remote = new JButton("Update DB (GitHub)");
        b_load = new JButton("Open custom DB");
        b_save = new JButton("Save current DB");
        b_add = new JButton("Add current Build");
        b_add_last = new JButton("Add random Build");
        b_remove = new JButton("Delete Builds");

        // Set Tooltips for Buttons
        b_rand.setToolTipText("Randomly select one build from database");
        b_reload.setToolTipText("Reload default build database");
        b_reload_remote.setToolTipText("Merge current build database with remote version from GitHub");
        b_load.setToolTipText("Open custom build database");
        b_save.setToolTipText("Save current build database");
        b_add.setToolTipText("Add current build in database");
        b_add_last.setToolTipText("Add generated build from other tab in database");
        b_remove.setToolTipText("Delete selected builds from database");

        // Define Colors for Buttons
        b_add_last.setBackground(Color.BLUE);
        b_add_last.setOpaque(true);
        b_add_last.setForeground(Color.WHITE);
        b_add.setBackground(Color.WHITE);
        b_add.setOpaque(true);
        b_add.setForeground(Color.BLACK);
        b_remove.setBackground(Color.RED);
        b_remove.setOpaque(true);
        b_remove.setForeground(Color.WHITE);

        // Define JFileChooser Objects
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "ini");
        fileChooser.setFileFilter(filter);

        // Define JComboBox Objects for Character & Perks
        cb_char = new JComboBox();
        cb_perk1 = new JComboBox();
        cb_perk2 = new JComboBox();
        cb_perk3 = new JComboBox();
        cb_perk4 = new JComboBox();
        cb_char.setPreferredSize(new Dimension(150, 25));
        cb_perk1.setPreferredSize(new Dimension(190, 25));
        cb_perk2.setPreferredSize(new Dimension(190, 25));
        cb_perk3.setPreferredSize(new Dimension(190, 25));
        cb_perk4.setPreferredSize(new Dimension(190, 25));
        ((JLabel) cb_char.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel) cb_perk1.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel) cb_perk2.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel) cb_perk3.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel) cb_perk4.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Define JComboBox Objects for Side
        cb_side = new JComboBox(new String[]{"Survivor", "Killer"});
        cb_side.setSelectedIndex(0);
        cb_side.setPreferredSize(new Dimension(130, 25));
        ((JLabel) cb_side.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        if (cb_side.getSelectedItem().toString().equals("Survivor")) {
            cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList("Survivor",false).toArray()));
            cb_perk1.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
            cb_perk2.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
            cb_perk3.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
            cb_perk4.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
        } else {
            cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList("Killer",false).toArray()));
            cb_perk1.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
            cb_perk2.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
            cb_perk3.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
            cb_perk4.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
        }

        // Define ActionListener
        b_reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset Filter
                tf_expr.setText("");
                // Reload default build database
                ((BuildTableModel) table.getModel()).initDatabase();
            }
        });

        // Define ActionListener
        b_reload_remote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset Filter
                tf_expr.setText("");
                // Download remote build database from GitHub (last version)
                try {
                    String output = s_build_remote;
                    URL website = new URL(SmartRandBuildGen.GIT_DB_REMOTE);
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(output);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    if (new File(output).exists()) {
                        // Load remote build database from GitHub
                        ((BuildTableModel) table.getModel()).readData(output, false);
                    } else {
                        Tools.getAlert("ERROR: Issues were encountered while downloading the last build database from GitHub", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    Tools.getAlert("ERROR: Issues were encountered while downloading the last build database from GitHub", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Define ActionListener
        b_load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = chooseFile(true);
                if (f != null) {
                    // Reset Filter
                    tf_expr.setText("");
                    if (new File(f).exists()) {
                        try {
                            // Load custom build database
                            ((BuildTableModel) table.getModel()).readData(f,true);
                        } catch (Exception ex) {
                            Tools.getAlert("ERROR: Issues were encountered while processing the build database file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Define ActionListener
        b_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = chooseFile(false);
                if (f != null) {
                    // Reset Filter
                    tf_expr.setText("");
                    try {
                        // Write build database in Output File
                        ((BuildTableModel) table.getModel()).saveDatabase(f);
                    } catch (Exception ex) {
                        Tools.getAlert("ERROR: Issues were encountered while saving the build database file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Define ActionListener
        b_rand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tf_expr.setText("");
                int rand = -1;
                // Get Selected Rows
                int[] rows = table.getSelectedRows();
                if (rows.length > 1) {
                    // Get Random Build from Selected Rows
                    rand = rows[(int) (rows.length * Math.random())] + 1;
                } else {
                    // Get Random Build from Database
                    rand = (int) (table.getRowCount() * Math.random()) + 1;
                }
                // Display It using Row Filter
                tf_expr.setText("^#" + rand + "$");
            }
        });

        // Define ActionListener
        cb_side.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define active Side
                String value = combo.getSelectedItem().toString();
                if (value.equals("Survivor")) {
                    cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList("Survivor",false).toArray()));
                    cb_perk1.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
                    cb_perk2.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
                    cb_perk3.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
                    cb_perk4.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
                } else if (value.equals("Killer")) {
                    cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacterList("Killer",false).toArray()));
                    cb_perk1.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
                    cb_perk2.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
                    cb_perk3.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
                    cb_perk4.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
                }
            }
        });

        // Define ActionListener
        b_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Define Build Object
                Build b = new Build();
                b.setName(tf_name.getText());
                b.setSide(cb_side.getSelectedItem().toString());
                b.setCharacter(srbg.retrieveCharacter(cb_char.getSelectedItem().toString()));
                ArrayList<String> l = new ArrayList<>();
                l.add(cb_perk1.getSelectedItem().toString());
                l.add(cb_perk2.getSelectedItem().toString());
                l.add(cb_perk3.getSelectedItem().toString());
                l.add(cb_perk4.getSelectedItem().toString());
                // Check Build
                if ((!Tools.hasDuplicateElements(l)) || (l.contains(Perk.GENERIC))) {
                    b.addPerk(srbg.getPerk(cb_perk1.getSelectedItem().toString()));
                    b.addPerk(srbg.getPerk(cb_perk2.getSelectedItem().toString()));
                    b.addPerk(srbg.getPerk(cb_perk3.getSelectedItem().toString()));
                    b.addPerk(srbg.getPerk(cb_perk4.getSelectedItem().toString()));
                    // Add Build
                    boolean added = ((BuildTableModel) table.getModel()).addBuild(b,true);
                    if (!added) {
                        Tools.getAlert("ERROR: This Build is already present in Database", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Tools.getAlert("ERROR: Duplicate Perks in your Build", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Define ActionListener
        b_add_last.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve last saved Build
                Build b = srbg.getBestBuild();
                if (b != null) {
                    // Generic Character if Survivor
                    if (b.getSide().equals("Survivor")) {
                        b.setCharacter(new Character(b.getSide()));
                    }
                    // Fill Build with generic Perks if needed
                    while (b.getPerks().size() < 4) {
                        Perk tmp = new Perk();
                        b.addPerk(tmp);
                    }
                    // Update GUI
                    updateGUI(b);
                    // Add Build
                    boolean added = ((BuildTableModel) table.getModel()).addBuild(b,true);
                    if (!added) {
                        Tools.getAlert("ERROR: This Build is already present in Database", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Tools.getAlert("ERROR: No Build was previously generated on the other Tab", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Define ActionListener
        b_remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Filter Mode must be disabled
                if (tf_expr.getText().equals("")) {
                    // Remove Builds
                    if (!table.removeSelectedRows()) {
                        Tools.getAlert("ERROR: No build was selected", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Tools.getAlert("ERROR: Builds can't be deleted while row filtering mode is used", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Define DocumentListener
        tf_expr.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                filterRows();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                filterRows();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterRows();
            }
        });

        // Define ModelListener
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                String s = table.getRowCount() + " Builds";
                // Update Field
                System.out.println("# " + s);
                tf_build.setText(s);
            }
        });

        // Define MouseListener
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Get Row & Column
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    // Get Selected Build from Database
                    Build b = ((BuildTableModel) table.getModel()).getBuildFromRow(row);
                    // Update GUI
                    updateGUI(b);
                }
            }
        });

    }

    /**
     * Update GUI with Selected Build
     *
     */
    private void updateGUI(Build b) {
        // Retrieve Name
        tf_name.setText(b.getName());
        // Retrieve Side
        cb_side.setSelectedItem(b.getSide());
        // Retrieve Character
        cb_char.setSelectedItem(b.getCharacter());
        // Retrieve Perks
        cb_perk1.setSelectedItem(b.getPerk(1).getName());
        cb_perk2.setSelectedItem(b.getPerk(2).getName());
        cb_perk3.setSelectedItem(b.getPerk(3).getName());
        cb_perk4.setSelectedItem(b.getPerk(4).getName());
    }

    /**
     * Filter Rows based on Regular Expression
     *
     */
    private void filterRows() {
        RowFilter rf = null;
        try {
            // Case insensitive Search
            rf = RowFilter.regexFilter("(?i)" + tf_expr.getText());
        } catch (PatternSyntaxException e) {
            return;
        }
        table.getSorter().setRowFilter(rf);
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
        String file = dir + File.separator + s_build_custom;
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
