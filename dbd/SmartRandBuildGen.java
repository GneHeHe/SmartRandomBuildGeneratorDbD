package dbd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * SmartRandBuildGen
 *
 * @author GneHeHe (2018)
 */
public class SmartRandBuildGen {

    // List of all Perks (Perk Objects)
    private ArrayList<Perk> l_perks;
    // List of all Perks (String Objects)
    private ArrayList<String> l_perks_string;
    // Current Side (Survivor / Killer)
    private String side_current;
    // List for Care Perks
    private TreeSet<String> set_care;
    // List for Sprint Perks
    private TreeSet<String> set_sprint;
    // Boolean Care Perk Needed
    private boolean care_needed;
    // Boolean Sprint Perk Needed
    private boolean sprint_needed;
    // Number of Perks in the Build
    private int nb_perks;
    // Title of Tool
    private String title;
    // Path to current Configuration File
    private String config;
    // Path to default Configuration File
    private String config_def;
    // Verbose Level
    private boolean verbose;
    public String spacer;

    /**
     * Default Constructor
     *
     */
    public SmartRandBuildGen() {

        // Set the Lists of Perks
        this.l_perks = new ArrayList<>();
        this.l_perks_string = new ArrayList<>();

        // Define the default Side
        this.side_current = "Survivor";

        // Define the default Number of Perks
        this.nb_perks = 4;

        // Read the default Perk Distribution File
        initConfigFile();

        // Init Care/Sprint Perk Lists
        this.set_care = initTreeSet("data/perks_care.txt");
        this.set_sprint = initTreeSet("data/perks_sprint.txt");

        // Set the Care/Sprint Status
        this.care_needed = false;
        this.sprint_needed = false;

        // Set Title of Tool
        setTitle();

        this.verbose = false;

        this.spacer = "####################################";

    }

    /**
     * Set the same Weight for all Perks
     *
     * @param value
     */
    public void setSameWeight(int value) {
        System.out.println("# All perks now have the same weight = " + value);
        for (Perk p : this.l_perks) {
            p.setWeight(value);
        }
    }

    /**
     * Get the Perk List
     *
     * @return
     */
    public List getPerks() {
        return this.l_perks;
    }

    /**
     * Get a Perk Object given its Name
     *
     * @param name
     * @return
     */
    public Perk getPerk(String name) {
        for (Perk p : this.l_perks) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Get the Number of Perks in a Build
     *
     * @return
     */
    public int getNbPerk() {
        return this.nb_perks;
    }

    /**
     * Set the Number of Perks in a Build
     *
     * @param n
     */
    public void setNbPerk(int n) {
        this.nb_perks = n;
        // Adjust Booleans if Value is lower than 2
        if (this.nb_perks < 2) {
            setNeedCare(false);
            setNeedSprint(false);
            System.out.println("# WARNING: Booleans (NeedCare and NeedSprint) are set to False because the number of desired Perks is lower than 2");
        }
        System.out.println("# Number of wanted Perks = " + this.nb_perks);
    }

    /**
     * Get the current Configuration File
     *
     * @return
     */
    public String getConfig() {
        return this.config;
    }

    /**
     * Set the current Configuration File
     *
     * @param s
     */
    public void setConfig(String s) {
        this.config = s;
        System.out.println("# Current configuration file =  " + this.config);
    }

    /**
     * Get the default Configuration File
     *
     * @return
     */
    public String getConfigDef() {
        return this.config_def;
    }

    /**
     * Set the current Side
     *
     * @param s
     */
    public void setSide(String s) {
        if ((s.equals("Survivor")) || (s.equals("Killer"))) {
            this.side_current = s;
        } else {
            System.out.println("# ERROR: The side must be either 'Survivor' or 'Killer'");
            System.exit(0);
        }
        System.out.println("# Selected Side = " + this.side_current);
    }

    /**
     * Get the current Side
     *
     * @return
     */
    public String getSide() {
        return this.side_current;
    }

    /**
     * Set the current Care Perks Status (with Contraints regarding the current
     * Number of Perks)
     *
     * @param b
     */
    public void setNeedCare(boolean b) {
        if ((this.side_current.equals("Survivor")) && (((getNbPerk() == 1) && (!getNeedSprint())) || (getNbPerk() >= 2))) {
            this.care_needed = b;
            System.out.println("# Care Mode = " + this.care_needed);
        } else {
            this.care_needed = false;
            System.out.println("# Care Mode = " + this.care_needed + " (WARNING: value can't be updated)");
        }
    }

    /**
     * Get the current Care Perks Status
     *
     * @return
     */
    public boolean getNeedCare() {
        return this.care_needed;
    }

    /**
     * Set the current Sprint Perks Status (with Contraints regarding the
     * current Number of Perks)
     *
     * @param b
     */
    public void setNeedSprint(boolean b) {
        if ((this.side_current.equals("Survivor")) && (((getNbPerk() == 1) && (!getNeedCare())) || (getNbPerk() >= 2))) {
            this.sprint_needed = b;
            System.out.println("# Sprint Mode = " + this.sprint_needed);
        } else {
            this.sprint_needed = false;
            System.out.println("# Sprint Mode = " + this.sprint_needed + " (WARNING: value can't be updated)");
        }
    }

    /**
     * Get the current Sprint Perks Status
     *
     * @return
     */
    public boolean getNeedSprint() {
        return this.sprint_needed;
    }

    /**
     * Get the Sprint Set
     *
     * @return
     */
    public TreeSet getSprintSet() {
        return this.set_sprint;
    }

    /**
     * Get the Sprint Set as String
     *
     * @return
     */
    public String getSprintAsString() {
        String s = "";
        for (String e : set_sprint) {
            s = s + e + ", ";
        }
        return s.trim().substring(0, s.length() - 2);
    }

    /**
     * Get the Care Set
     *
     * @return
     */
    public TreeSet getCareSet() {
        return this.set_care;
    }

    /**
     * Get the Care Set as String
     *
     * @return
     */
    public String getCareAsString() {
        String s = "";
        for (String e : set_care) {
            s = s + e + ", ";
        }
        return s.trim().substring(0, s.length() - 2);
    }

    /**
     * Display all Perks and their Attributes
     *
     */
    public void showPerks() {
        System.out.println("\n" + spacer + " Loaded Perks with their Attributes " + spacer + "\n");
        for (Perk p : this.l_perks) {
            System.out.println(p.show());
        }
    }

    /**
     * Display all Parameters
     *
     */
    public void showParams() {
        System.out.println("\n" + spacer + " Input Parameters " + spacer + "\n");
        System.out.println("# Side = " + this.side_current);
        System.out.println("# Number of Perks per Build = " + this.nb_perks);
        System.out.println("# Care Needed = " + this.care_needed);
        System.out.println("# Sprint Needed = " + this.sprint_needed);
        System.out.println("# Number of Loaded Perks = " + this.l_perks_string.size());
    }

    /**
     * Get a Build as String
     *
     * @param tree
     * @param name
     * @param spacer
     * @return
     */
    public String showBuild(TreeSet<String> tree, String name, String spacer) {
        int nb = 1;
        String build = "Random Build " + name + " :" + spacer;
        for (String s : tree) {
            if (nb == getNbPerk()) {
                build = build + s;
            } else {
                build = build + s + spacer;
            }
            nb++;
        }
        return build;
    }

    /**
     * Set Title of Tool
     *
     */
    private void setTitle() {

        // Object to format Date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        // Get Modification Date of current Class
        long date = Calendar.getInstance().getTimeInMillis();
        try {
            date = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).lastModified();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        // Set Title of Tool
        this.title = "Smart Random Build Generator for Dead by Daylight (version 1.0, last build " + sdf.format(date) + ")";
    }

    /**
     * Get Title of Tool
     *
     * @return
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Set Verbose Mode
     *
     * @param b
     */
    public void setVerbose(boolean b) {
        this.verbose = b;
        System.out.println("# Verbose Mode = " + this.verbose);
    }

    /**
     * Get Verbose Mode
     *
     * @return
     */
    public boolean getVerbose() {
        return this.verbose;
    }

    /**
     * Generate single Random Build
     *
     * @return
     */
    public TreeSet<String> genRandomBuild() {
        if (verbose) {
            System.out.println("# Need Care Perk = " + getNeedCare() + " | Need Sprint Perk = " + getNeedSprint());
        }
        List<String> l = new ArrayList();
        TreeSet<String> l_ok = new TreeSet();
        int nbperks = 0;
        // Get weighted List of Perks from the Side of interest
        for (Perk perk : this.l_perks) {
            if (perk.getSide().equals(this.side_current)) {
                int value = perk.getWeight();
                for (int i = 0; i < value; i++) {
                    l.add(perk.getName());
                    // Count the Number of unique Perks
                    if (i == 0) {
                        nbperks++;
                    }
                }
            }
        }
        // Check if the Pool of Perks if sufficiently big enough, same for the Number of unique Perks
        if ((l.size() < this.nb_perks) || (nbperks < this.nb_perks)) {
            System.out.println("# Pool of available Perks is too small => Check the Weights of Perks and/or the Number of Perks in each Build");
            return l_ok;
        }
        // Several Loops may be required if "Needed Perks" Booleans are not set to "False"
        int nbloop = 1;
        boolean loop;
        if ((!this.care_needed) && (!this.sprint_needed)) {
            loop = false;
        } else {
            loop = true;
        }
        while ((loop) || (nbloop == 1)) {
            if (verbose) {
                System.out.print("# Loop " + nbloop + " => ");
            }
            boolean sprint_found = false;
            boolean care_found = false;
            l_ok.clear();
            while (l_ok.size() < getNbPerk()) {
                int rand = (int) (l.size() * Math.random());
                String perk = l.get(rand);
                if (!l_ok.contains(perk)) {
                    if ((sprint_found && (this.set_sprint.contains(perk))) || (care_found && (this.set_care.contains(perk)))) {
                        if (verbose) {
                            System.out.print("... skipped " + perk + " ... ");
                        }
                    } else {
                        // New Perk satisfying all Criteria => added to the Build
                        l_ok.add(perk);
                        if (verbose) {
                            System.out.print(perk + " | ");
                        }
                    }

                }
                // Sprint Perk Found => Update Boolean
                if (this.set_sprint.contains(perk)) {
                    sprint_found = true;
                }
                // Care Perk Found => Update Boolean
                if (this.set_care.contains(perk)) {
                    care_found = true;
                }
            }
            // Check Criteria => Stop or Continue the Loop
            boolean care_check = (!this.care_needed) || (this.care_needed && care_found);
            boolean sprint_check = (!this.sprint_needed) || (this.sprint_needed && sprint_found);
            if (care_check && sprint_check) {
                loop = false;
            }
            nbloop++;
            if (verbose) {
                System.out.println("");
            }
        }
        // Return Random Build
        return l_ok;
    }

    /**
     * Set Weights for the Perks using Configuration File
     *
     */
    private void initConfigFile() {
        // Try to detect a custom Configuration File in the current Directory
        String f = System.getProperty("user.dir") + File.separator + "perks_db_custom.txt";
        if (new File(f).exists()) {
            this.config = new File(f).getAbsolutePath();
            System.out.println("# Loading custom perk distribution from " + this.config);
            readConfigFile(f, "\t");
        } else {
            // Or use the default Configuration File
            this.config = "data/perks_db.txt";
            System.out.println("# Loading default perk distribution from " + this.config);
            InputStream is = getClass().getResourceAsStream(this.config);
            if (is != null) {
                readConfigFile(is, "\t");
            } else {
                System.out.println("\n# ERROR: The configuration file \"" + this.config + "\" was not found !\n");
                System.exit(0);
            }
        }
        // Set the default Configuration File (may be the same as current one)
        this.config_def = "data/perks_db.txt";
    }

    /**
     * Set Weights for the Perks using Configuration File
     *
     * @param input
     * @param spacer
     */
    public void readConfigFile(Object input, String spacer) {
        this.l_perks.clear();
        this.l_perks_string.clear();
        try {
            // Define the Reader
            BufferedReader br = null;
            if (new File(input.toString()).exists()) {
                String f = input.toString();
                br = new BufferedReader(new FileReader(new File(f)));
            } else {
                InputStream is = (InputStream) input;
                br = new BufferedReader(new InputStreamReader(is));
            }
            // Loop over the Reader
            String line = "";
            line = br.readLine();
            while (line != null) {
                // Split Line according to Spacer
                String tab[] = line.split(spacer);
                if (tab.length == 4) {
                    // Get Data (4 Fields are expected)
                    String name = tab[0];
                    String side = tab[1];
                    String icon = tab[2];
                    int weight = Integer.parseInt(tab[3]);
                    // Check Weight Value
                    if (weight < 0) {
                        System.out.println("# ERROR: Weights must be larger than 0 (Error with this Line : >" + line + "<");
                        System.exit(0);
                    }
                    // Create Perk Object
                    Perk p = new Perk(name, weight, side, icon);
                    // Add Perk to the Pool
                    this.l_perks.add(p);
                    // Add Perk Name to known Perk List
                    this.l_perks_string.add(name);
                } else {
                    System.out.println("# ERROR: Corrupted Configuration File (Error with this Line : >" + line + "< (defined spacer='" + spacer + "')");
                    System.exit(0);
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException | NumberFormatException ex) {
            System.out.println("# ERROR: Issues with the Configuration File");
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Write the current Perk Distribution in a Configuration File
     *
     * @param filename
     * @param spacer
     */
    public void writeConfigFile(String filename, String spacer) {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File(filename)));
            for (Perk p : this.l_perks) {
                bw.write(p.getName() + spacer + p.getSide() + spacer + p.getIconString() + spacer + p.getWeight() + "\n");
            }
            System.out.println("# Saving current perk distribution in " + filename);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            System.out.println("# ERROR: Issues while saving the current Perk Distribution");
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Init a TreeSet from a Configuration File
     *
     */
    private TreeSet<String> initTreeSet(String f) {
        TreeSet<String> tree = new TreeSet<>();
        try {
            InputStream is = getClass().getResourceAsStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            while (line != null) {
                if (l_perks_string.contains(line)) {
                    tree.add(line);
                } else {
                    System.out.println("# ERROR: Perk " + line + " does not exist !");
                    System.exit(0);
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            System.out.println("# ERROR: Issues with the input File " + f);
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        return tree;
    }

    /**
     * Display Help and Quit
     *
     */
    public void displayHelp() {
        System.out.println("\n# " + getTitle() + "\n#");
        System.out.println("# Options:");
        System.out.println("#  -conf : custom configuration file for the perk distribution");
        System.out.println("#  -side : set the side ('Survivor' or 'Killer')");
        System.out.println("#  -perk : set the number of perks in each build");
        System.out.println("#  -build : set the number of builds to generate");
        System.out.println("#  -care : enable constraints for care-related perks");
        System.out.println("#  -sprint : enable constraints for sprint-related perks");
        System.out.println("#  -v : enable verbose mode");
        System.out.println("#  -h : print this help and quit\n");
        System.exit(0);
    }

    /**
     * Main Method for Command-line Use
     *
     * @param args
     */
    public static void main(String args[]) {

        // Build SmartRandBuildGen Object
        SmartRandBuildGen srbg = new SmartRandBuildGen();

        // Check Args
        if (args.length == 0) {
            srbg.displayHelp();
        }

        // Define default Number of Random Builds to Generate
        int nbbuilds = 10;

        // Process User-defined Arguments
        String val = "";
        int valn = 0;
        int argn = args.length;
        for (int i = 0; i < argn; i++) {
            if (args[i].equals("-conf")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        val = args[i + 1];
                        srbg.readConfigFile(val, "\t");
                        System.out.println("# Updating perk distribution from this configuration file " + val);
                    } else {
                        System.out.println("\n# FATAL: The '-conf' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.out.println("\n# FATAL: The '-conf' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-side")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        val = args[i + 1];
                        srbg.setSide(val);
                    } else {
                        System.out.println("\n# FATAL: The '-side' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.out.println("\n# FATAL: The '-side' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-care")) {
                srbg.setNeedCare(true);
            } else if (args[i].equals("-sprint")) {
                srbg.setNeedSprint(true);
            } else if (args[i].equals("-perk")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        try {
                            valn = Integer.parseInt(args[i + 1]);
                            srbg.setNbPerk(valn);
                        } catch (Exception ex) {
                            System.out.println("\n# FATAL: The '-perk' option requires an integer value\n");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("\n# FATAL: The '-perk' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.out.println("\n# FATAL: The '-perk' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-build")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        try {
                            valn = Integer.parseInt(args[i + 1]);
                            nbbuilds = valn;
                        } catch (Exception ex) {
                            System.out.println("\n# FATAL: The '-build' option requires an integer value\n");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("\n# FATAL: The '-build' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.out.println("\n# FATAL: The '-build' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-v")) {
                srbg.setVerbose(true);
            } else if (args[i].equals("-h")) {
                srbg.displayHelp();
            } else if (args[i].startsWith("-")) {
                System.out.println("\n# FATAL: Wrong option '" + args[i] + "'\n");
                System.exit(0);
            }
        }

        // Display loaded Data & Perks
        srbg.showParams();
        srbg.showPerks();

        // Generate several Random Builds using various Care/Sprint Criteria Combinations
        System.out.println("\n" + srbg.spacer + " " + nbbuilds + " Random Builds " + srbg.spacer + "\n");
        for (int k = 1; k <= nbbuilds; k++) {
            if (srbg.getVerbose()) {
                System.out.println("");
            }
            TreeSet<String> l = srbg.genRandomBuild();
            System.out.println("# " + srbg.showBuild(l, k + "", "\t"));
        }
        System.out.println("");

    }

}
