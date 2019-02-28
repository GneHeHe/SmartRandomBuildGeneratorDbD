package dbd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
 * @author GneHeHe (2018)
 *
 */
public class SmartRandBuildGenTabData extends JPanel {

    // Swing Components
    private JPanel pan_all, pan_build, pan_perks, pan_button;
    private JScrollPane scrollPane;
    private JButton b_load, b_save, b_add, b_add_last, b_remove, b_rand, b_reload;
    private JLabel lab_filter, lab_side, lab_char, lab_perk1, lab_perk2, lab_perk3, lab_perk4;
    private JComboBox cb_side, cb_char, cb_perk1, cb_perk2, cb_perk3, cb_perk4;
    private JFileChooser fileChooser;
    private JTextField tf_name, tf_expr, tf_build;
    private BuildTable table;
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
        this.addComponents();

        // Create Subpanels
        this.pan_all = new JPanel();
        this.pan_all.setLayout(new BoxLayout(this.pan_all, BoxLayout.Y_AXIS));

        this.pan_button = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.pan_button.add(this.tf_build);
        this.pan_button.add(this.lab_filter);
        this.pan_button.add(this.tf_expr);
        this.pan_button.add(this.b_rand);
        this.pan_button.add(this.b_reload);
        this.pan_button.add(this.b_load);
        this.pan_button.add(this.b_save);

        this.pan_build = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.pan_build.add(this.tf_name);
        this.pan_build.add(this.lab_side);
        this.pan_build.add(this.cb_side);
        this.pan_build.add(this.lab_char);
        this.pan_build.add(this.cb_char);
        this.pan_build.add(this.b_add_last);
        this.pan_build.add(this.b_add);
        this.pan_build.add(this.b_remove);

        this.pan_perks = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.pan_perks.add(this.lab_perk1);
        this.pan_perks.add(this.cb_perk1);
        this.pan_perks.add(this.lab_perk2);
        this.pan_perks.add(this.cb_perk2);
        this.pan_perks.add(this.lab_perk3);
        this.pan_perks.add(this.cb_perk3);
        this.pan_perks.add(this.lab_perk4);
        this.pan_perks.add(this.cb_perk4);

        this.pan_all.add(this.pan_build);
        this.pan_all.add(this.pan_perks);

        // Add Table to Panel
        this.scrollPane = new JScrollPane(this.table);

        // Set Layout & add Subpanels
        this.setLayout(new BorderLayout());
        this.add(this.pan_button, BorderLayout.NORTH);
        this.add(this.pan_all, BorderLayout.SOUTH);
        this.add(this.scrollPane, BorderLayout.CENTER);

    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Create Table & Model
        this.table = new BuildTable();
        this.table.setModel(new BuildTableModel(this.srbg));
        this.table.setSorter();
        this.table.setRowHeight(69);
        this.table.centerText();
        this.table.setIconColumn(3);
        this.table.setIconColumn(4);
        this.table.setIconColumn(5);
        this.table.setIconColumn(6);
        this.table.setIconColumn(7);
        this.table.setShowVerticalLines(false);

        // Define JLabel Objects
        this.lab_filter = new JLabel(" Filter Rows ");
        this.lab_side = new JLabel(" Side ");
        this.lab_char = new JLabel(" Character ");
        this.lab_perk1 = new JLabel(" Perk 1 ");
        this.lab_perk2 = new JLabel("  Perk 2 ");
        this.lab_perk3 = new JLabel("  Perk 3 ");
        this.lab_perk4 = new JLabel("  Perk 4 ");

        // Define JTextField Objects
        this.tf_name = new JTextField(10);
        this.tf_name.setText("MyBuild");
        this.tf_name.setHorizontalAlignment(JTextField.CENTER);
        this.tf_name.setEditable(true);
        this.tf_name.setToolTipText("Define Name of Build");
        this.tf_expr = new JTextField(10);
        this.tf_expr.setText("");
        this.tf_expr.setHorizontalAlignment(JTextField.CENTER);
        this.tf_expr.setEditable(true);
        this.tf_expr.setToolTipText("Case Insensitive Search (Regular Expressions can be used)");
        this.tf_build = new JTextField(12);
        this.tf_build.setText(table.getRowCount() + " loaded Builds");
        this.tf_build.setHorizontalAlignment(JTextField.CENTER);
        this.tf_build.setEditable(false);

        // Define JButton Objects
        this.b_rand = new JButton("Random Select");
        this.b_reload = new JButton("Reload Database");
        this.b_load = new JButton("Open Database");
        this.b_save = new JButton("Save Database");
        this.b_add = new JButton("Add current Build");
        this.b_add_last = new JButton("Add random Build");
        this.b_remove = new JButton("Delete Builds");

        // Set Tooltips for Buttons
        this.b_rand.setToolTipText("Randomly select one build from database");
        this.b_reload.setToolTipText("Reload default build database");
        this.b_load.setToolTipText("Open custom build database");
        this.b_save.setToolTipText("Save current build database");
        this.b_add.setToolTipText("Add current build in database");
        this.b_add_last.setToolTipText("Add generated build from other tab in database");
        this.b_remove.setToolTipText("Delete selected builds from database");

        // Define Colors for Buttons
        this.b_add_last.setBackground(Color.BLUE);
        this.b_add_last.setOpaque(true);
        this.b_add_last.setForeground(Color.WHITE);
        this.b_add.setBackground(Color.WHITE);
        this.b_add.setOpaque(true);
        this.b_add.setForeground(Color.BLACK);
        this.b_remove.setBackground(Color.RED);
        this.b_remove.setOpaque(true);
        this.b_remove.setForeground(Color.WHITE);

        // Define JFileChooser Objects
        this.fileChooser = new JFileChooser();
        this.fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "ini");
        this.fileChooser.setFileFilter(filter);

        // Define JComboBox Objects for Character & Perks
        this.cb_char = new JComboBox();
        this.cb_perk1 = new JComboBox();
        this.cb_perk2 = new JComboBox();
        this.cb_perk3 = new JComboBox();
        this.cb_perk4 = new JComboBox();
        this.cb_char.setPreferredSize(new Dimension(130, 25));
        this.cb_perk1.setPreferredSize(new Dimension(180, 25));
        this.cb_perk2.setPreferredSize(new Dimension(180, 25));
        this.cb_perk3.setPreferredSize(new Dimension(180, 25));
        this.cb_perk4.setPreferredSize(new Dimension(180, 25));
        ((JLabel) this.cb_char.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel) this.cb_perk1.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel) this.cb_perk2.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel) this.cb_perk3.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel) this.cb_perk4.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Define JComboBox Objects for Side
        this.cb_side = new JComboBox(new String[]{"Survivor", "Killer"});
        this.cb_side.setSelectedIndex(0);
        this.cb_side.setPreferredSize(new Dimension(130, 25));
        ((JLabel) this.cb_side.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        if (this.cb_side.getSelectedItem().toString().equals("Survivor")) {
            this.cb_char.setModel(new DefaultComboBoxModel(this.srbg.getCharacters("Survivor").toArray()));
            this.cb_perk1.setModel(new DefaultComboBoxModel(this.srbg.getPerks("Survivor").toArray()));
            this.cb_perk2.setModel(new DefaultComboBoxModel(this.srbg.getPerks("Survivor").toArray()));
            this.cb_perk3.setModel(new DefaultComboBoxModel(this.srbg.getPerks("Survivor").toArray()));
            this.cb_perk4.setModel(new DefaultComboBoxModel(this.srbg.getPerks("Survivor").toArray()));
        } else {
            this.cb_char.setModel(new DefaultComboBoxModel(this.srbg.getCharacters("Killer").toArray()));
            this.cb_perk1.setModel(new DefaultComboBoxModel(this.srbg.getPerks("Killer").toArray()));
            this.cb_perk2.setModel(new DefaultComboBoxModel(this.srbg.getPerks("Killer").toArray()));
            this.cb_perk3.setModel(new DefaultComboBoxModel(this.srbg.getPerks("Killer").toArray()));
            this.cb_perk4.setModel(new DefaultComboBoxModel(this.srbg.getPerks("Killer").toArray()));
        }

        // Define ActionListener
        this.b_reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset Filter
                tf_expr.setText("");
                // Reload default build database
                ((BuildTableModel) table.getModel()).initDatabase();
            }
        });

        // Define ActionListener
        this.b_load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = chooseFile(true);
                if (f != null) {
                    // Reset Filter
                    tf_expr.setText("");
                    if (new File(f).exists()) {
                        try {
                            // Load custom build database
                            ((BuildTableModel) table.getModel()).readData(f);
                        } catch (Exception ex) {
                            Tools.getAlert("ERROR: Issues were encountered while processing the build database file " + f, "Warning", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Define ActionListener
        this.b_save.addActionListener(new ActionListener() {
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
        this.b_rand.addActionListener(new ActionListener() {
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
        this.cb_side.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox) e.getSource();
                // Retrieve & Define active Side
                String value = combo.getSelectedItem().toString();
                if (value.equals("Survivor")) {
                    cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacters("Survivor").toArray()));
                    cb_perk1.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
                    cb_perk2.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
                    cb_perk3.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
                    cb_perk4.setModel(new DefaultComboBoxModel(srbg.getPerks("Survivor").toArray()));
                } else if (value.equals("Killer")) {
                    cb_char.setModel(new DefaultComboBoxModel(srbg.getCharacters("Killer").toArray()));
                    cb_perk1.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
                    cb_perk2.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
                    cb_perk3.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
                    cb_perk4.setModel(new DefaultComboBoxModel(srbg.getPerks("Killer").toArray()));
                }
            }
        });

        // Define ActionListener
        this.b_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Define Build Object
                Build b = new Build();
                b.setName(tf_name.getText());
                b.setSide(cb_side.getSelectedItem().toString());
                b.setCharacter(srbg.getCharacter(cb_char.getSelectedItem().toString(), b.getSide()));
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
                    boolean added = ((BuildTableModel) table.getModel()).addBuild(b);
                    if (!added) {
                        Tools.getAlert("ERROR: This Build is already present in Database", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Tools.getAlert("ERROR: Duplicate Perks in your Build", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Define ActionListener
        this.b_add_last.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve last saved Build
                Build b = srbg.getBuildLast();
                if (b != null) {
                    // Generic Character if Survivor
                    if (b.getSide().equals("Survivor")) {
                        b.setCharacter(new Character(b.getSide()));
                    }
                    tf_name.setText(b.getName());
                    cb_side.setSelectedItem(b.getSide());
                    // Retrieve Character
                    cb_char.setSelectedItem(b.getCharacter());
                    // Fill Build with generic Perks if needed
                    while (b.getPerks().size() < 4) {
                        Perk tmp = new Perk();
                        b.addPerk(tmp);
                    }
                    // Retrieve Perks
                    cb_perk1.setSelectedItem(b.getPerk(1).getName());
                    cb_perk2.setSelectedItem(b.getPerk(2).getName());
                    cb_perk3.setSelectedItem(b.getPerk(3).getName());
                    cb_perk4.setSelectedItem(b.getPerk(4).getName());
                    // Add Build
                    boolean added = ((BuildTableModel) table.getModel()).addBuild(b);
                    if (!added) {
                        Tools.getAlert("ERROR: This Build is already present in Database", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Tools.getAlert("ERROR: No Build was previously generated on the other Tab", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Define ActionListener
        this.b_remove.addActionListener(new ActionListener() {
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
        this.tf_expr.getDocument().addDocumentListener(new DocumentListener() {
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
        this.table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                String s = table.getRowCount() + " loaded Builds";
                // Update Field
                System.out.println("# " + s);
                tf_build.setText(s);
            }
        });

    }

    /**
     * Filter Rows based on Regular Expression
     *
     */
    private void filterRows() {
        RowFilter rf = null;
        try {
            // Case insensitive Search
            rf = RowFilter.regexFilter("(?i)" + this.tf_expr.getText());
        } catch (PatternSyntaxException e) {
            return;
        }
        this.table.getSorter().setRowFilter(rf);
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
        String file = dir + File.separator + "build_db_custom.txt";
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

}
