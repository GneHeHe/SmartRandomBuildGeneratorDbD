package dbd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * BuildTableModel
 *
 * @author GneHeHe (2019)
 *
 */
public class BuildTableModel extends AbstractTableModel {

    // Define Columns
    private final String[] columns = {"ID", "Name", "Side", "Character", "Perk1", "Perk2", "Perk3", "Perk4"};
    // Define Content of Table
    private ArrayList<Build> l_builds;
    // SmartRandBuildGen Object
    private SmartRandBuildGen srbg;

    /**
     * Constructor
     *
     * @param srbg
     */
    public BuildTableModel(SmartRandBuildGen srbg) {
        // Define List
        this.l_builds = new ArrayList<>();
        // Set SmartRandBuildGen Object
        this.srbg = srbg;
        // Read default Build Database
        this.initDatabase();
    }

    /**
     * Get Nb of Rows
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return this.l_builds.size();
    }

    /**
     * Get Nb of Columns
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return this.columns.length;
    }

    /**
     * Get Name of a given Column
     *
     * @param columnIndex
     * @return
     */
    @Override
    public String getColumnName(int columnIndex) {
        return this.columns[columnIndex];
    }

    /**
     * Get Class of a given Column
     *
     * @param columnIndex
     * @return
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return JLabel.class;
            case 4:
                return JLabel.class;
            case 5:
                return JLabel.class;
            case 6:
                return JLabel.class;
            case 7:
                return JLabel.class;
            default:
                return Object.class;
        }
    }

    /**
     * Get Value from a given Cell
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                //System.out.println("#" + (rowIndex+1));
                return "#" + (rowIndex + 1);
            case 1:
                //System.out.println(l_builds.get(rowIndex).getName());
                return this.l_builds.get(rowIndex).getName();
            case 2:
                //System.out.println(l_builds.get(rowIndex).getSide());
                return this.l_builds.get(rowIndex).getSide();
            case 3:
                //System.out.println(l_builds.get(rowIndex).getCharacter().getIconImage(1).getName());
                return this.l_builds.get(rowIndex).getCharacter().getIconImage(1);
            case 4:
                //System.out.println(l_builds.get(rowIndex).getPerk(1).getIconImage(2).getName());
                return this.l_builds.get(rowIndex).getPerk(1).getIconImage(2);
            case 5:
                //System.out.println(l_builds.get(rowIndex).getPerk(2).getIconImage(2).getName());
                return this.l_builds.get(rowIndex).getPerk(2).getIconImage(2);
            case 6:
                //System.out.println(l_builds.get(rowIndex).getPerk(3).getIconImage(2).getName());
                return this.l_builds.get(rowIndex).getPerk(3).getIconImage(2);
            case 7:
                //System.out.println(l_builds.get(rowIndex).getPerk(4).getIconImage(2).getName());
                return this.l_builds.get(rowIndex).getPerk(4).getIconImage(2);
            default:
                return null;
        }
    }

    /**
     * Is the Cell Editable ?
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 1:
                return true;
            default:
                return false;
        }
    }

    /**
     * Set Value in a given Cell
     *
     * @param aValue
     * @param rowIndex
     * @param columnIndex
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue != null) {
            // Get current Object
            Build b = this.l_builds.get(rowIndex);
            if (columnIndex == 1) {
                b.setName((String) aValue);
                System.out.println("# Updated Row " + (rowIndex + 1) + " | " + b.show(false, "   "));
            }
        }
    }

    /**
     * Add new Build in Table (Update List & Throw Event)
     *
     * @param newbuild
     * @param verbose
     * @return
     */
    public boolean addBuild(Build newbuild, boolean verbose) {
        // Check if Build is already known
        for (Build b : this.l_builds) {
            if (b.isDuplicate(newbuild)) {
                System.out.println("# Skipped Build | " + newbuild.show(false, "   "));
                return false;
            }
        }
        // Add Build
        this.l_builds.add(newbuild);
        if (verbose) {
            System.out.println("# Added Build | " + newbuild.show(false, "   "));
        }
        // Update JTable using an Event
        this.fireTableDataChanged();
        return true;
    }

    /**
     * Remove selected Builds (Update List & Throw Event)
     *
     * @param row
     */
    public void removeBuild(int row) {
        // Remove a Build
        Build b = this.l_builds.remove(row);
        System.out.println("# Removed Build | " + b.show(false, "   "));
        // Update JTable using an Event
        this.fireTableRowsDeleted(row, row);
    }

    /**
     * Save Build Database
     *
     * @param filename
     */
    public void saveDatabase(String filename) {
        String spacer = "\t";
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File(filename)));
            for (Build p : this.l_builds) {
                bw.write(p.getName() + spacer + p.getSide() + spacer + p.getCharacter() + spacer + p.getPerk(1).getName() + spacer + p.getPerk(2).getName() + spacer + p.getPerk(3).getName() + spacer + p.getPerk(4).getName() + "\n");
            }
            System.out.println("# Saving Build Database in " + filename);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            System.err.println("\n# ERROR: Issues while saving the Build Database");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Load Build Database at Start (default or custom)
     *
     */
    public final void initDatabase() {
        // Try to detect a custom build Database in current Directory
        String f = System.getProperty("user.dir") + File.separator + "build_db_custom.txt";
        if (!new File(f).exists()) {
            // Or use default build Database
            f = "data/build_db.txt";
        }
        // Read build database
        this.readData(f, true);
    }

    /**
     * Load Build Database
     *
     * @param input
     * @param reset
     */
    public void readData(String input, boolean reset) {
        String spacer = "\t";
        // Reset Model if needed
        if (reset) {
            this.l_builds.clear();
        }
        try {
            // Define Reader
            BufferedReader br = null;
            if (new File(input).exists()) {
                System.out.println("\n# Loading custom Build Database from " + input);
                br = new BufferedReader(new FileReader(new File(input)));
            } else {
                InputStream is = getClass().getResourceAsStream(input);
                System.out.println("\n# Loading default Build Database from " + input);
                br = new BufferedReader(new InputStreamReader(is));
            }
            // Loop over Reader
            String line = "";
            line = br.readLine();
            int nb_lines = 1;
            int nb_builds = 0;
            while (line != null) {
                // Split Line according to Spacer
                String tab[] = line.split(spacer);
                if (!line.startsWith("#")) {
                    if (tab.length == 7) {
                        // Get Data (7 Fields are expected)
                        String myname = tab[0];
                        String myside = tab[1];
                        String mychar_s = tab[2];
                        Character mychar = this.srbg.retrieveCharacter(mychar_s);
                        Perk p1 = this.srbg.getPerk(tab[3]);
                        Perk p2 = this.srbg.getPerk(tab[4]);
                        Perk p3 = this.srbg.getPerk(tab[5]);
                        Perk p4 = this.srbg.getPerk(tab[6]);
                        if (!((myside.equals("Survivor")) || (myside.equals("Killer")))) {
                            // Check if Side is Ok
                            System.err.println("# ERROR: wrong side ('" + myside + "') => skipped line " + nb_lines + " : >" + line + "< from input file");
                        } else if (!mychar.getSide().equals(myside)) {
                            // Check if Character is OK wrt Side
                            System.err.println("# ERROR: unknown character ('" + mychar_s + "') or mismatch with side ('" + myside + "') => skipped line " + nb_lines + " : >" + line + "< from input file");
                        } else if ((p1 == null) || (p2 == null) || (p3 == null) || (p4 == null)) {
                            // Check if Perks are OK
                            System.err.println("# ERROR: unknown perk in build => skipped line " + nb_lines + " : >" + line + "< from input file");
                        } else if (!((p1.checkSide(myside)) && (p2.checkSide(myside)) && (p3.checkSide(myside)) && (p4.checkSide(myside)))) {
                            // Check if Perks are OK wrt Side
                            System.err.println("# ERROR: mismatch between side ('" + myside + "') and perks ('" + p1.getName() + "'/'" + p2.getName() + "'/'" + p3.getName() + "'/'" + p4.getName() + "') => skipped line " + nb_lines + " : >" + line + "< from input file");
                        } else {
                            // Check duplicate Perks
                            ArrayList<String> l = new ArrayList<>();
                            l.add(p1.getName());
                            l.add(p2.getName());
                            l.add(p3.getName());
                            l.add(p4.getName());
                            if ((!Tools.hasDuplicateElements(l)) || (l.contains(Perk.GENERIC))) {
                                // Everything is OK => Create Build Object
                                Build b = new Build();
                                b.setName(myname);
                                b.setSide(myside);
                                b.setCharacter(mychar);
                                b.addPerk(p1);
                                b.addPerk(p2);
                                b.addPerk(p3);
                                b.addPerk(p4);
                                // Add Build to Model
                                this.addBuild(b, false);
                            } else {
                                System.err.println("# ERROR: duplicate perks in build ('" + p1.getName() + "'/'" + p2.getName() + "'/'" + p3.getName() + "'/'" + p4.getName() + "') => skipped line " + nb_lines + " : >" + line + "< from input file");
                            }
                        }
                    } else {
                        System.err.println("# ERROR: corrupted build database => skipped line " + nb_lines + " : >" + line + "< from input file");
                    }
                    nb_builds++;
                }
                nb_lines++;
                line = br.readLine();
                // Display
                if (((nb_builds % 1000) == 0) && (nb_builds > 0)) {
                    System.out.println("# " + nb_builds + " Builds were Loaded");
                }
            }
            System.out.println("# " + nb_builds + " Builds were Loaded");
            br.close();
            System.out.println("");
        } catch (Exception ex) {
            System.err.println("\n# ERROR: Issues with the Build Database");
            System.err.println(ex.getMessage());
            System.exit(0);
        }
        // Update JTable using an Event
        this.fireTableDataChanged();
    }

    /**
     * Get Build from a given Row
     *
     * @param row
     * @return
     */
    public Build getBuildFromRow(int row) {
        return this.l_builds.get(row);
    }

}
