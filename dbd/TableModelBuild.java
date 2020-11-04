package dbd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * TableModelBuild
 *
 * @author GneHeHe (2019)
 *
 */
public class TableModelBuild extends AbstractTableModel {

    // Define Columns
    private final String[] columns = {"Build ID", "Build Name", "Side", "Character", "Perk1", "Perk2", "Perk3", "Perk4"};
    // Define Content of Table
    private ArrayList<Build> l_builds;
    private HashMap<String, Integer> m_builds;
    // SRBG Object
    private SRBG srbg;
    // Database Filenames
    private final String s_build = "data/build_db.txt";
    private final String s_build_custom = "build_db_custom.txt";

    /**
     * Constructor
     *
     * @param srbg
     */
    public TableModelBuild(SRBG srbg) {
        // Define List
        l_builds = new ArrayList<>();
        // Define Map
        m_builds = new HashMap<>();
        // Set SRBG Object
        this.srbg = srbg;
        // Read default Build Database
        initDatabase();
    }

    /**
     * Get Nb of Rows
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return l_builds.size();
    }

    /**
     * Get Nb of Columns
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * Get Name of a given Column
     *
     * @param columnIndex
     * @return
     */
    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
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
                return l_builds.get(rowIndex).getName();
            case 2:
                //System.out.println(l_builds.get(rowIndex).getSide());
                return l_builds.get(rowIndex).getSide();
            case 3:
                //System.out.println(l_builds.get(rowIndex).getCharacter().getIconImage(1).getName());
                return l_builds.get(rowIndex).getCharacter().getIconImage(1);
            case 4:
                //System.out.println(l_builds.get(rowIndex).getPerk(1).getIconImage(2).getName());
                return l_builds.get(rowIndex).getPerk(1).getIconImage(2);
            case 5:
                //System.out.println(l_builds.get(rowIndex).getPerk(2).getIconImage(2).getName());
                return l_builds.get(rowIndex).getPerk(2).getIconImage(2);
            case 6:
                //System.out.println(l_builds.get(rowIndex).getPerk(3).getIconImage(2).getName());
                return l_builds.get(rowIndex).getPerk(3).getIconImage(2);
            case 7:
                //System.out.println(l_builds.get(rowIndex).getPerk(4).getIconImage(2).getName());
                return l_builds.get(rowIndex).getPerk(4).getIconImage(2);
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
            Build b = l_builds.get(rowIndex);
            if (columnIndex == 1) {
                b.setName((String) aValue);
                System.out.println("# Updated Row " + (rowIndex + 1) + " | " + b.show(false, "   "));
            }
        }
    }

    /**
     * Add new Build in Table (Update List and Throw Event)
     *
     * @param newbuild
     * @param verbose
     * @return
     */
    public boolean addBuild(Build newbuild, boolean verbose) {
        /*
        // Check if Build is already known
        for (Build b : l_builds) {
            if (b.isDuplicate(newbuild, true)) {
                System.out.println("# Skipped Build | " + newbuild.show(false, "   "));
                return false;
            }
        }
        // Add Build
        l_builds.add(newbuild);
        if (verbose) {
            System.out.println("# Added Build | " + newbuild.show(false, "   "));
        }
        // Update JTable using an Event
        fireTableDataChanged();
        return true;
         */
        String tmp = newbuild.show_raw();
        // Check if Build is already known
        Integer exist = m_builds.get(tmp);
        if (exist == null) {
            // Add Build in List
            l_builds.add(newbuild);
            // Add Build (String as Key) in Map
            m_builds.put(tmp, 1);
            if (verbose) {
                System.out.println("# Added Build | " + newbuild.show(false, "   "));
            }
            // Update JTable using an Event
            fireTableDataChanged();
            return true;
        } else {
            System.out.println("# Skipped Build | " + newbuild.show(false, "   "));
            return false;
        }
    }

    /**
     * Remove selected Builds (Update List and Throw Event)
     *
     * @param row
     */
    public void removeBuild(int row) {
        // Remove a Build
        Build b = l_builds.remove(row);
        System.out.println("# Removed Build | " + b.show(false, "   "));
        // Update JTable using an Event
        fireTableRowsDeleted(row, row);
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
            for (Build p : l_builds) {
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
        String f = System.getProperty("user.dir") + File.separator + s_build_custom;
        if (!new File(f).exists()) {
            // Or use default build Database
            f = s_build;
        }
        // Read build database
        readDatabase(f, true);
    }

    /**
     * Load Build Database
     *
     * @param input
     * @param reset
     */
    public void readDatabase(String input, boolean reset) {
        String spacer = "\t";
        // Reset Model if needed
        if (reset) {
            l_builds.clear();
            m_builds.clear();
        }
        String line = "";
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
                        Character mychar = srbg.retrieveCharacter(mychar_s);
                        Perk p1 = srbg.getPerk(tab[3]);
                        Perk p2 = srbg.getPerk(tab[4]);
                        Perk p3 = srbg.getPerk(tab[5]);
                        Perk p4 = srbg.getPerk(tab[6]);
                        if (!((myside.equals(srbg.s_side_surv)) || (myside.equals(srbg.s_side_killer)))) {
                            // Check if Side is Ok
                            System.err.println("# ERROR: wrong side ('" + myside + "') => skipped line " + nb_lines + " : >" + line + "< from input file");
                        } else if (mychar == null) {
                            // Check if Character is OK
                            System.err.println("# ERROR: unknown character ('" + mychar_s + "') => skipped line " + nb_lines + " : >" + line + "< from input file");
                        } else if (!mychar.getSide().equals(myside)) {
                            // Check if Character is OK wrt Side
                            System.err.println("# ERROR: mismatch between character ('" + mychar_s + "') and side ('" + myside + "') => skipped line " + nb_lines + " : >" + line + "< from input file");
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
                            // Sort Perks
                            Collections.sort(l);
                            if (!Tools.hasDuplicateElements(l, Perk.GENERIC)) {
                                // Everything is OK => Create Build Object
                                Build b = new Build();
                                b.setName(myname);
                                b.setSide(myside);
                                b.setCharacter(mychar);
                                b.addPerk(srbg.getPerk(l.get(0)));
                                b.addPerk(srbg.getPerk(l.get(1)));
                                b.addPerk(srbg.getPerk(l.get(2)));
                                b.addPerk(srbg.getPerk(l.get(3)));
                                // Add Build to Model
                                if (addBuild(b, false)) {
                                    nb_builds++;
                                }
                            } else {
                                System.err.println("# ERROR: duplicate perks in build ('" + p1.getName() + "'/'" + p2.getName() + "'/'" + p3.getName() + "'/'" + p4.getName() + "') => skipped line " + nb_lines + " : >" + line + "< from input file");
                            }
                        }
                    } else {
                        System.err.println("# ERROR: corrupted build database => skipped line " + nb_lines + " : >" + line + "< from input file");
                    }
                }
                nb_lines++;
                line = br.readLine();
                // Display
                if (((nb_builds % 1000) == 0) && (nb_builds > 0)) {
                    System.out.println("# " + nb_builds + " Builds were loaded");
                }
            }
            System.out.println("# " + nb_builds + " Builds were loaded");
            br.close();
        } catch (Exception ex) {
            System.err.println("\n# ERROR: Issues with the Build Database");
            System.err.println("Last processed line from input file: >" + line + "<");
            System.err.println(ex.getMessage());
            System.exit(0);
        }
        // Update JTable using an Event
        fireTableDataChanged();
    }

    /**
     * Get Build from a given Row
     *
     * @param row
     * @return
     */
    public Build getBuildFromRow(int row) {
        return l_builds.get(row);
    }

    /**
     * Rescore loaded Builds using Synergy-based Rules
     *
     */
    public void rescoreDatabase() {
        // Loop over loaded Builds
        for (int i = 0; i < getRowCount(); i++) {
            Build b = getBuildFromRow(i);
            // Original Score
            int val = 0;
            for (Perk p : b.getPerks()) {
                val = val + p.getWeight();
            }
            // Display Reevaluated Score and Build
            System.out.println(b.rescoreBuild(srbg) + " | " + val + " | " + b.getName() + "\t" + b.getSide() + "\t" + b.getCharacter() + "\t" + b.getPerk(1) + "\t" + b.getPerk(2) + "\t" + b.getPerk(3) + "\t" + b.getPerk(4));
            if (srbg.b_verbose) {
                System.out.println("");
            }
        }
    }

}
