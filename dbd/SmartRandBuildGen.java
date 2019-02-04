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
    // List for Exhaustion Perks
    private TreeSet<String> set_exhaust;
    // Boolean Care Perk Needed
    private boolean care_needed;
    // Boolean Exhaustion Perk Needed
    private boolean exhaust_needed;
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

        // Init Care/Exhaustion Perk Lists
        this.set_care = initTreeSet("data/perks_care.txt");
        this.set_exhaust = initTreeSet("data/perks_exhaust.txt");

        // Set the Care/Exhaustion Status
        this.care_needed = true;
        this.exhaust_needed = true;

        // Set Title of Tool
        setTitle();

        this.verbose = false;

    }

    /**
     * Set the same Weight for all Perks
     *
     * @param value
     */
    public void setSameWeight(int value) {
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
            setNeedExhaust(false);
            System.out.println("# WARNING: Booleans (NeedCare and NeedExhaust) are set to False because the number of desired Perks is lower than 2");
        }
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
     * Get the current Side
     *
     * @return
     */
    public String getSide() {
        return this.side_current;
    }

    /**
     * Set the current Side
     *
     * @param s
     */
    public void setSide(String s) {
        this.side_current = s;
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
     * Set the current Care Perks Status (with Contraints regarding the current
     * Number of Perks)
     *
     * @param b
     */
    public void setNeedCare(boolean b) {
        if (((getNbPerk() == 1) && (!getNeedExhaust())) || (getNbPerk() >= 2)) {
            this.care_needed = b;
        } else {
            this.care_needed = false;
            System.out.println("# WARNING: NeedCare Value set to " + this.care_needed);
        }
    }

    /**
     * Get the current Exhaustion Perks Status
     *
     * @return
     */
    public boolean getNeedExhaust() {
        return this.exhaust_needed;
    }

    /**
     * Set the current Exhaustion Perks Status (with Contraints regarding the
     * current Number of Perks)
     *
     * @param b
     */
    public void setNeedExhaust(boolean b) {
        if (((getNbPerk() == 1) && (!getNeedCare())) || (getNbPerk() >= 2)) {
            this.exhaust_needed = b;
        } else {
            this.exhaust_needed = false;
            System.out.println("# WARNING: NeedExhaust Value set to " + this.exhaust_needed);
        }
    }

    /**
     * Get the Exhaustion Set
     *
     * @return
     */
    public TreeSet getExhaustSet() {
        return this.set_exhaust;
    }

    /**
     * Get the Exhaustion Set as String
     *
     * @return
     */
    public String getExhaustString() {
        String s = "";
        for (String e : set_exhaust) {
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
    public String getCareString() {
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
        for (Perk p : this.l_perks) {
            System.out.println(p.show());
        }
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
     * Generate single Random Build
     *
     * @return
     */
    public TreeSet<String> genRandomBuild() {
        if (verbose) {
            System.out.println("# Need Care Perk = " + getNeedCare() + " | Need Exhaustion Perk = " + getNeedExhaust());
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
        if ((!this.care_needed) && (!this.exhaust_needed)) {
            loop = false;
        } else {
            loop = true;
        }
        while ((loop) || (nbloop == 1)) {
            if (verbose) {
                System.out.print("# Loop " + nbloop + " => ");
            }
            boolean exhaus_found = false;
            boolean care_found = false;
            l_ok.clear();
            while (l_ok.size() < getNbPerk()) {
                int rand = (int) (l.size() * Math.random());
                String perk = l.get(rand);
                if (!l_ok.contains(perk)) {
                    if ((exhaus_found && (this.set_exhaust.contains(perk))) || (care_found && (this.set_care.contains(perk)))) {
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
                // Exhaustion Perk Found => Update Boolean
                if (this.set_exhaust.contains(perk)) {
                    exhaus_found = true;
                }
                // Care Perk Found => Update Boolean
                if (this.set_care.contains(perk)) {
                    care_found = true;
                }
            }
            // Check Criteria => Stop or Continue the Loop
            boolean care_check = (!this.care_needed) || (this.care_needed && care_found);
            boolean exhaust_check = (!this.exhaust_needed) || (this.exhaust_needed && exhaus_found);
            if (care_check && exhaust_check) {
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
        } catch (IOException ex) {
            System.out.println("# ERROR: Issues with the input File " + f);
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        return tree;
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
        if (args.length != 2) {
            System.out.println("\n# " + srbg.getTitle() + "\n#");
            System.out.println("# Usage:");
            System.out.println("# - Arg1 = Number of Random Builds");
            System.out.println("# - Arg2 = Number of Perks for each Build\n");
            System.exit(0);
        }

        // Define default Number of Random Builds to Generate
        int nbbuilds = 10;
        // Try updating this Value
        try {
            nbbuilds = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("\n# ERROR: Wrong Argument => Default Value will be used (Nb Builds = " + nbbuilds + ")\n");
        }

        // Define default Number of Perks in each Build
        int nbperk = 4;
        // Try updating this Value
        try {
            nbperk = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("\n# ERROR: Wrong Argument => Default Value will be used (Nb Perks = " + nbperk + ")\n");
        }
        srbg.setNbPerk(nbperk);

        // Display loaded Data
        String spacer = "####################################";
        System.out.println("\n" + spacer + " Loaded Perk List with their Attributes " + spacer + "\n");
        srbg.showPerks();

        // Generate several Random Builds using various Care/Exhaustion Criteria Combinations
        Boolean[] tab = {true, false};
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab.length; j++) {
                srbg.setNeedCare(tab[i]);
                srbg.setNeedExhaust(tab[j]);
                System.out.println("\n" + spacer + " Need Care Perk = " + srbg.getNeedCare() + " | Need Exhaustion Perk = " + srbg.getNeedExhaust() + " " + spacer);
                for (int k = 1; k <= nbbuilds; k++) {
                    System.out.println("");
                    TreeSet<String> l = srbg.genRandomBuild();
                    System.out.println("# " + srbg.showBuild(l, k + "", "\t"));
                }
            }
        }
        System.out.println("");
    }

}
