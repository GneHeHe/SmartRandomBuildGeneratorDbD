package dbd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * SmartRandBuildGen
 *
 * @author GneHeHe (2018)
 *
 */
public class SmartRandBuildGen {

    // List of all Perks
    private ArrayList<Perk> l_perks_all;
    private ArrayList<String> l_perks_all_string;
    private ArrayList<String> l_perks_pool;
    // List of Survivor Perks
    private ArrayList<String> l_perks_survivor;
    // List of Killer Perks
    private ArrayList<String> l_perks_killer;
    // List of Survivor Characters
    private ArrayList<Character> l_char_survivor;
    // List of Killer Characters
    private ArrayList<Character> l_char_killer;
    // Saved Build
    private Build build_last;
    // Active Side
    private String side;
    // List of Care Perks
    private List<String> l_care;
    // List of Sprint Perks
    private List<String> l_sprint;
    // Boolean Care Perk Needed
    private boolean care_needed;
    // Boolean Sprint Perk Needed
    private boolean sprint_needed;
    // Nb of Perks in Build
    private int nb_perks_build;
    // Nb of loaded Perks
    private int nb_perks_all;
    // Nb of active Perks
    private int nb_perks_side;
    // Random Character Status
    private boolean bool_rand_character;
    // Update Pool of Perks
    private boolean update_perks_pool;
    // Path of Configuration File
    private String config;
    // Verbose Level
    private boolean verbose;
    private final String MYSPACER = "##########";
    // Version & Title of Tool
    public final static double VERSION = 1.4;
    public final static String TITLE = "Smart Random Build Generator for Dead by Daylight " + VERSION;
    // GitHub User/Repos
    public final static String GIT_USER = "GneHeHe";
    public final static String GIT_REPO = "SmartRandomBuildGeneratorDbD";
    public final static String GIT_DB_REMOTE = "https://raw.githubusercontent.com/GneHeHe/SmartRandomBuildGeneratorDbD/master/dbd/data/build_db.txt";

    /**
     * Default Constructor
     *
     */
    public SmartRandBuildGen() {

        System.out.println("\n" + MYSPACER + " " + TITLE + " " + MYSPACER + "\n");

        // Set Lists of Perks
        this.l_perks_all = new ArrayList<>();
        this.l_perks_all_string = new ArrayList<>();
        this.l_perks_pool = new ArrayList<>();
        this.l_perks_survivor = new ArrayList<>();
        this.l_perks_killer = new ArrayList<>();
        this.l_char_survivor = new ArrayList<>();
        this.l_char_killer = new ArrayList<>();

        // Define both default Side & Nb of Perks
        this.side = "";
        this.setSide("Random");
        this.setNbPerksBuild(4);

        // Read default Weight Distribution File
        this.initConfigFile();
        this.initCharacters();

        // Init Care/Sprint Perk Lists
        this.l_care = initListFromFile("data/perk_care.txt");
        this.l_sprint = initListFromFile("data/perk_sprint.txt");

        // Set Care/Sprint Status
        this.setNeedCare(false);
        this.setNeedSprint(false);

        // Set Character Status
        this.setRandomCharacterStatus(false);

        // Define last saved Build
        this.build_last = null;

        // Set Verbose Mode
        this.verbose = false;

    }

    /**
     * Set same Weight for all Perks
     *
     * @param value
     */
    public void setSameWeight(int value) {
        System.out.println("# All perks now have the same weight = " + value + "\n");
        for (Perk p : this.l_perks_all) {
            p.setWeight(value);
        }
        // Update Pool of Perks
        this.setUpdatePerkPool(true);
        // Display Perks
        showPerks(false);
    }

    /**
     * Get Perk List
     *
     * @return
     */
    public List getPerks() {
        return this.l_perks_all;
    }

    /**
     * Get Perk List
     *
     * @param side
     * @return
     */
    public List getPerks(String side) {
        if (side.equals("Survivor")) {
            return this.l_perks_survivor;
        } else if (side.equals("Killer")) {
            return this.l_perks_killer;
        } else {
            return null;
        }
    }

    /**
     * Get a Perk Object given its Name
     *
     * @param name
     * @return
     */
    public Perk getPerk(String name) {
        for (Perk p : this.l_perks_all) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Get a Character Object given its Name
     *
     * @param name
     * @param side
     * @return
     */
    public Character getCharacter(String name, String side) {
        if (side.equals("Survivor")) {
            for (Character c : this.l_char_survivor) {
                if (c.getName().equals(name)) {
                    return c;
                }
            }
        } else if (side.equals("Killer")) {
            for (Character c : this.l_char_killer) {
                if (c.getName().equals(name)) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Get a Character Object given its Name (Generic Character will not be
     * returned)
     *
     * @return
     */
    public Character getRandomCharacter() {
        Character c = null;
        int rand;
        if (this.side.equals("Survivor")) {
            rand = Math.max(1, (int) (this.l_char_survivor.size() * Math.random()));
            c = this.l_char_survivor.get(rand);
        } else if (this.side.equals("Killer")) {
            rand = Math.max(1, (int) (this.l_char_killer.size() * Math.random()));
            c = l_char_killer.get(rand);
        }
        return c;
    }

    /**
     * Get Nb of Perks in Build
     *
     * @return
     */
    public int getNbPerksBuild() {
        return this.nb_perks_build;
    }

    /**
     * Set Nb of Perks in Build
     *
     * @param n
     */
    public final void setNbPerksBuild(int n) {
        this.nb_perks_build = n;
        // Adjust Booleans if Value is lower than 2
        if (this.nb_perks_build < 2) {
            setNeedCare(false);
            setNeedSprint(false);
            System.out.println("# WARNING: Both Need-Care/Need-Sprint booleans are set to False because number of Perks is too low");
        }
        System.out.println("# Nb of Perks per Build = " + this.nb_perks_build + "\n");
    }

    /**
     * Get Nb of Loaded Perks
     *
     * @return
     */
    public int getNbPerksAll() {
        return this.nb_perks_all;
    }

    /**
     * Get Nb of Perks from Active Side
     *
     * @return
     */
    public int getNbPerksSide() {
        return this.nb_perks_side;
    }

    /**
     * Get Random Character Status
     *
     * @return
     */
    public boolean getRandomCharacterStatus() {
        return this.bool_rand_character;
    }

    /**
     * Set Random Character Status
     *
     * @param b
     */
    public final void setRandomCharacterStatus(boolean b) {
        this.bool_rand_character = b;
        System.out.println("# Selecting Character Randomly = " + this.bool_rand_character + "\n");
    }

    /**
     * Get current Configuration File
     *
     * @return
     */
    public String getConfigFile() {
        return this.config;
    }

    /**
     * Set current Configuration File
     *
     * @param s
     */
    public void setConfigFile(String s) {
        this.config = s;
        System.out.println("# Current configuration file = " + this.config + "\n");
    }

    /**
     * Get Saved Build
     *
     * @return
     */
    public Build getBuildLast() {
        return this.build_last;
    }

    /**
     * Set Saved Build
     *
     * @param b
     */
    public void setBuildLast(Build b) {
        this.build_last = b;
    }

    /**
     * Set active Side
     *
     * @param s
     */
    public final void setSide(String s) {
        // Random Selection Case
        if (s.equals("Random")) {
            s = selectRandomSide();
        }
        // Update Nb Perks on Active Side
        if (s.equals("Survivor")) {
            this.nb_perks_side = l_perks_survivor.size();
        } else if (s.equals("Killer")) {
            this.nb_perks_side = l_perks_killer.size();
        } else {
            System.err.println("\n# ERROR: The side must be either 'Survivor' OR 'Killer' OR 'Random'\n");
            System.exit(0);
        }
        // Copy current Side & Update Active Side
        String side_old = this.side;
        this.side = s;
        System.out.println("# Active Side = " + this.side + "\n");
        // Update Pool of Perks if needed (no update and different side)        
        if ((!this.getUpdatePerkPool()) && (!this.side.equals(side_old))) {
            this.setUpdatePerkPool(true);
        }
    }

    /**
     * Get active Side
     *
     * @return
     */
    public String getSide() {
        return this.side;
    }

    /**
     * Get Character List
     *
     * @param side
     * @return
     */
    public ArrayList getCharacters(String side) {
        if (side.equals("Survivor")) {
            return this.l_char_survivor;
        } else if (side.equals("Killer")) {
            return this.l_char_killer;
        } else {
            return null;
        }
    }

    /**
     * Set Care Perks Status
     *
     * @param b
     */
    public final void setNeedCare(boolean b) {
        if ((this.side.equals("Survivor")) && (((getNbPerksBuild() == 1) && (!getNeedSprint())) || (getNbPerksBuild() >= 2))) {
            this.care_needed = b;
            System.out.println("# Care Perk Needed = " + this.care_needed + "\n");
        } else {
            this.care_needed = false;
            System.out.println("# Care Perk Needed = " + this.care_needed + " (WARNING: value was not updated)\n");
        }
    }

    /**
     * Get Care Perks Status
     *
     * @return
     */
    public boolean getNeedCare() {
        return this.care_needed;
    }

    /**
     * Set Sprint Perks Status
     *
     * @param b
     */
    public final void setNeedSprint(boolean b) {
        if ((this.side.equals("Survivor")) && (((getNbPerksBuild() == 1) && (!getNeedCare())) || (getNbPerksBuild() >= 2))) {
            this.sprint_needed = b;
            System.out.println("# Sprint Perk Needed = " + this.sprint_needed + "\n");
        } else {
            this.sprint_needed = false;
            System.out.println("# Sprint Perk Needed = " + this.sprint_needed + " (WARNING: value was not updated)\n");
        }
    }

    /**
     * Get Sprint Perks Status
     *
     * @return
     */
    public boolean getNeedSprint() {
        return this.sprint_needed;
    }

    /**
     * Get Sprint List
     *
     * @return
     */
    public List<String> getSprintList() {
        return this.l_sprint;
    }

    /**
     * Get Sprint List as String
     *
     * @return
     */
    public String getSprintListAsString() {
        String s = "";
        for (String e : l_sprint) {
            s = s + e + ", ";
        }
        return s.trim().substring(0, s.length() - 2);
    }

    /**
     * Get Care List
     *
     * @return
     */
    public List<String> getCareList() {
        return this.l_care;
    }

    /**
     * Get Care List as String
     *
     * @return
     */
    public String getCareListAsString() {
        String s = "";
        for (String e : l_care) {
            s = s + e + ", ";
        }
        return s.trim().substring(0, s.length() - 2);
    }

    /**
     * Display all Perks & Features
     *
     * @param detail
     */
    public void showPerks(boolean detail) {
        System.out.println("\n# All Loaded Perks (" + this.nb_perks_all + " Perks)\n");
        for (Perk p : this.l_perks_all) {
            System.out.println(p.show(detail));
        }
        System.out.println("");
    }

    /**
     * Display active Perks & Features
     *
     * @param detail
     */
    public void showPerksSide(boolean detail) {
        System.out.println("\n# Active Perks from '" + this.side + "' Side (" + this.nb_perks_side + " Perks)\n");
        for (Perk p : this.l_perks_all) {
            if (p.getSide().equals(this.side)) {
                System.out.println(p.show(detail));
            }
        }
        System.out.println("");
    }

    /**
     * Display all Parameters
     *
     */
    public void showParams() {
        System.out.println(MYSPACER + " Input Parameters " + MYSPACER + "\n");
        System.out.println("# Nb of Loaded Perks = " + this.nb_perks_all);
        System.out.println("# Active Side = " + this.side);
        System.out.println("# Randomly Select Character = " + this.bool_rand_character);
        System.out.println("# Nb of Perks on Active Side = " + this.nb_perks_side);
        System.out.println("# Nb of Perks per Build = " + this.nb_perks_build);
        System.out.println("# Care Perk Needed = " + this.care_needed);
        System.out.println("# Sprint Perk Needed = " + this.sprint_needed);
        System.out.println("# Verbose Mode = " + this.verbose);
    }

    /**
     * Set Verbose Mode
     *
     * @param b
     */
    private void setVerbose(boolean b) {
        this.verbose = b;
        System.out.println("# Verbose Mode = " + this.verbose + "\n");
    }

    /**
     * Get Status of Pool of Perks
     *
     * @return
     */
    public boolean getUpdatePerkPool() {
        return this.update_perks_pool;
    }

    /**
     * Set Status of Pool of Perks
     *
     * @param b
     */
    public void setUpdatePerkPool(boolean b) {
        this.update_perks_pool = b;
        //System.out.println("# Update Pool of Perks = " + this.update_perks_pool + "\n");
    }

    /**
     * Generate single Random Build
     *
     * @param buildname
     * @return
     */
    public Build genRandomBuild(String buildname) {
        if (verbose) {
            System.out.println("\n# Need Care Perk = " + getNeedCare() + " | Need Sprint Perk = " + getNeedSprint());
        }
        List<String> l_ok = new ArrayList<>();
        // Update Pool of Perks if needed
        if (this.getUpdatePerkPool()) {
            // Reset Pool of Perks 
            this.l_perks_pool.clear();
            // Rebuild Pool of Perks 
            for (Perk perk : this.l_perks_all) {
                if (perk.getSide().equals(this.side)) {
                    int value = perk.getWeight();
                    for (int i = 0; i < value; i++) {
                        this.l_perks_pool.add(perk.getName());
                    }
                }
            }
            this.setUpdatePerkPool(false);
        }
        // Check if Pool of Perks is big enough
        if (this.l_perks_pool.size() < this.nb_perks_build) {
            System.out.println("# Pool of available Perks is too small => Check the Weights of Perks");
            return null;
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
            while (l_ok.size() < getNbPerksBuild()) {
                int rand = (int) (l_perks_pool.size() * Math.random());
                String perk = l_perks_pool.get(rand);
                if (!l_ok.contains(perk)) {
                    if ((sprint_found && (this.l_sprint.contains(perk))) || (care_found && (this.l_care.contains(perk)))) {
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
                if (this.l_sprint.contains(perk)) {
                    sprint_found = true;
                }
                // Care Perk Found => Update Boolean
                if (this.l_care.contains(perk)) {
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
        Collections.sort(l_ok);
        // Return Random Build
        Build b = new Build();
        b.setName(buildname);
        b.setSide(this.side);
        for (String s : l_ok) {
            Perk p = getPerk(s);
            b.addPerk(p);
        }
        return b;
    }

    /**
     * Set Weights from Configuration File (at start)
     *
     */
    public final void initConfigFile() {
        // Try to detect a custom Configuration File in the current Directory
        String f = System.getProperty("user.dir") + File.separator + "perk_db_custom.txt";
        if (new File(f).exists()) {
            this.config = new File(f).getAbsolutePath();
        } else {
            // Or use the default Configuration File
            this.config = "data/perk_db.txt";
        }
        // Read the Configuration File
        readConfigFile(this.config);
    }

    /**
     * Set Weights from Configuration File
     *
     * @param input
     */
    public void readConfigFile(String input) {
        String spacer = "\t";
        this.l_perks_all.clear();
        this.l_perks_all_string.clear();
        this.l_perks_survivor.clear();
        this.l_perks_killer.clear();
        // Add generic Perk
        Perk p = new Perk();
        this.l_perks_all.add(p);
        this.l_perks_all_string.add(p.getName());
        this.l_perks_survivor.add(p.getName());
        this.l_perks_killer.add(p.getName());

        try {
            // Define the Reader
            BufferedReader br = null;
            if (new File(input).exists()) {
                System.out.println("# Loading custom Weight Distribution from " + input);
                br = new BufferedReader(new FileReader(new File(input)));
            } else {
                InputStream is = getClass().getResourceAsStream(input);
                System.out.println("# Loading default Weight Distribution from " + input);
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
                    String myname = tab[0];
                    String myside = tab[1];
                    if (!((myside.equals("Survivor")) || (myside.equals("Killer")))) {
                        System.err.println("\n# ERROR: wrong side ('" + myside + "') => Exit [ wrong line : >" + line + "< from input file ]\n");
                        System.exit(0);
                    }
                    String myicon = tab[2];
                    int myweight = Integer.parseInt(tab[3]);
                    // Check Weight Value
                    if (myweight < 0) {
                        System.err.println("\n# ERROR: wrong weight ('" + myweight + "') => Exit [ wrong line : >" + line + "< from input file ]\n");
                        System.exit(0);
                    }
                    // Create Perk Object
                    p = new Perk(myname, myweight, myside, myicon);
                    // Add Perk to the List
                    this.l_perks_all.add(p);
                    // Add Perk Name to Perk List
                    this.l_perks_all_string.add(myname);
                    if (myside.equals("Survivor")) {
                        this.l_perks_survivor.add(myname);
                    } else {
                        this.l_perks_killer.add(myname);
                    }
                } else {
                    System.err.println("\n# ERROR: corrupted configuration file => Exit [ wrong line : >" + line + "< from input file ]\n");
                    System.exit(0);
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            System.err.println("\n# ERROR: issues with configuration file => Exit");
            System.err.println(ex.getMessage());
            System.exit(0);
        }
        // Set Nb of Perks
        this.nb_perks_all = this.l_perks_all.size();
        // Update Nb of Active Perks
        if (this.side.equals("Survivor")) {
            this.nb_perks_side = l_perks_survivor.size();
        } else if (this.side.equals("Killer")) {
            this.nb_perks_side = l_perks_killer.size();
        }
        // Display Perks
        showPerks(false);
        // Update Pool of Perks
        this.setUpdatePerkPool(true);
    }

    /**
     * Init Characters
     *
     */
    private void initCharacters() {
        String spacer = "\t";
        this.l_char_survivor.clear();
        this.l_char_killer.clear();
        // Add generic Characters
        Character c = new Character("Survivor");
        this.l_char_survivor.add(c);
        c = new Character("Killer");
        this.l_char_killer.add(c);
        try {
            // Define the Reader
            BufferedReader br = null;
            String input = "data/characters.txt";
            InputStream is = getClass().getResourceAsStream(input);
            System.out.println("# Loading Characters from " + input + "\n");
            br = new BufferedReader(new InputStreamReader(is));
            // Loop over the Reader
            String line = "";
            line = br.readLine();
            while (line != null) {
                // Split Line according to Spacer
                String tab[] = line.split(spacer);
                if (tab.length == 3) {
                    // Get Data (4 Fields are expected)
                    String myname = tab[0];
                    String myside = tab[1];
                    if (!((myside.equals("Survivor")) || (myside.equals("Killer")))) {
                        System.err.println("\n# ERROR: wrong side ('" + myside + "') => Exit [ wrong line : >" + line + "< from input file ]\n");
                        System.exit(0);
                    }
                    String myicon = tab[2];
                    // Create Character Object
                    c = new Character(myname, myside, myicon);
                    // Add Character to related List
                    if (myside.equals("Survivor")) {
                        this.l_char_survivor.add(c);
                    } else {
                        this.l_char_killer.add(c);
                    }
                } else {
                    System.err.println("\n# ERROR: corrupted character file => Exit [ wrong line : >" + line + "< from input file ]\n");
                    System.exit(0);
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            System.err.println("\n# ERROR: issues with character file => Exit");
            System.err.println(ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Save Weight Distribution
     *
     * @param filename
     */
    public void saveConfigFile(String filename) {
        String spacer = "\t";
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File(filename)));
            for (Perk p : this.l_perks_all) {
                bw.write(p.getName() + spacer + p.getSide() + spacer + p.getIconString() + spacer + p.getWeight() + "\n");
            }
            System.out.println("# Saving current weight distribution in " + filename);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            System.err.println("\n# ERROR: Issues while saving the current weight distribution");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Init a List from a Configuration File
     *
     */
    private List<String> initListFromFile(String f) {
        List<String> list = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            while (line != null) {
                if (l_perks_all_string.contains(line)) {
                    list.add(line);
                } else {
                    System.err.println("\n# ERROR: Perk " + line + " does not exist !\n");
                    System.exit(0);
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            System.err.println("\n# ERROR: Issues with the input File " + f);
            System.err.println(ex.getMessage());
            System.exit(0);
        }
        Collections.sort(list);
        return list;
    }

    /**
     * Display Help & Quit
     *
     */
    public void displayHelp() {
        System.out.println(MYSPACER + " Available Options in Smart Random Build Generator " + MYSPACER + "\n");
        System.out.println("#  -conf : load custom weight distribution file (all perks)");
        System.out.println("#  -side : set active side ('Survivor' OR 'Killer' OR 'Random')");
        System.out.println("#  -perk : set number of perks per build");
        System.out.println("#  -build : set number of builds to generate");
        System.out.println("#  -char : select randomly the character (disabled by default)");
        System.out.println("#  -care : enable constraints for care-related perks");
        System.out.println("#  -sprint : enable constraints for sprint-related perks");
        System.out.println("#  -v : enable verbose mode (disabled by default)");
        System.out.println("#  -h : print this help and quit\n");
        System.exit(0);
    }

    /**
     * Randomly Select Active Side
     *
     * @return
     */
    public String selectRandomSide() {
        System.out.println("# Randomly Selecting Side\n");
        // Get random Number
        double p = Math.random();
        // Add Bias toward the other Side
        double offset = 0.20;
        if (this.side.equals("Survivor")) {
            p = p - offset;
        } else if (this.side.equals("Killer")) {
            p = p + offset;
        }
        // Select Side according to Random Value
        if (p > 0.5) {
            return "Survivor";
        } else {
            return "Killer";
        }
    }

    /**
     * Check Update from GitHub
     *
     * @return
     */
    public boolean checkUpdate() {
        System.out.print("# Checking Update from remote GitHub Repository\n# ");
        boolean update_new = false;
        double gitversion = Tools.getLastVersionGitHub(SmartRandBuildGen.GIT_USER, SmartRandBuildGen.GIT_REPO);
        if (gitversion > SmartRandBuildGen.VERSION) {
            update_new = true;
            System.out.println("# Remote Version = " + gitversion + "\n# Local Version = " + SmartRandBuildGen.VERSION + "\n# An Update is available from https://github.com/" + SmartRandBuildGen.GIT_USER + "/" + SmartRandBuildGen.GIT_REPO + "/releases\n");
        } else if (gitversion > 0) {
            System.out.println("# You already have the last Version (" + SmartRandBuildGen.VERSION + ")\n");
        }
        return update_new;
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

        // Check Update
        srbg.checkUpdate();

        // Define default Nb of Random Builds
        int nbbuilds = 10;

        // Process User-defined Arguments
        System.out.println(srbg.MYSPACER + " Parsing Arguments from User " + srbg.MYSPACER + "\n");
        String val = "";
        boolean valb = true;
        int valn = 0;
        int argn = args.length;
        for (int i = 0; i < argn; i++) {
            if (args[i].equals("-conf")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        val = args[i + 1];
                        srbg.readConfigFile(val);
                    } else {
                        System.err.println("\n# ERROR: The '-conf' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.err.println("\n# ERROR: The '-conf' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-side")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        val = args[i + 1];
                        srbg.setSide(val);
                    } else {
                        System.err.println("\n# ERROR: The '-side' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.err.println("\n# ERROR: The '-side' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-char")) {
                srbg.setRandomCharacterStatus(true);
            } else if (args[i].equals("-care")) {
                srbg.setNeedCare(true);
            } else if (args[i].equals("-sprint")) {
                srbg.setNeedSprint(true);
            } else if (args[i].equals("-perk")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        try {
                            valn = Integer.parseInt(args[i + 1]);
                            srbg.setNbPerksBuild(valn);
                        } catch (Exception ex) {
                            System.err.println("\n# ERROR: The '-perk' option requires an integer value\n");
                            System.exit(0);
                        }
                    } else {
                        System.err.println("\n# ERROR: The '-perk' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.err.println("\n# ERROR: The '-perk' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-build")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        try {
                            valn = Integer.parseInt(args[i + 1]);
                            nbbuilds = valn;
                            System.out.println("# Number of desired Builds = " + nbbuilds + "\n");
                        } catch (Exception ex) {
                            System.err.println("\n# ERROR: The '-build' option requires an integer value\n");
                            System.exit(0);
                        }
                    } else {
                        System.err.println("\n# ERROR: The '-build' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.err.println("\n# ERROR: The '-build' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-v")) {
                srbg.setVerbose(true);
            } else if (args[i].equals("-h")) {
                srbg.displayHelp();
            } else if (args[i].startsWith("-")) {
                System.err.println("\n# ERROR: Wrong option '" + args[i] + "'\n");
                System.exit(0);
            }
        }

        // Display loaded Parameters
        srbg.showParams();

        // Get Character if desired
        if (srbg.getRandomCharacterStatus()) {
            System.out.println("\n" + srbg.MYSPACER + " Randomly Selected Character " + srbg.MYSPACER + "\n");
            System.out.println("# Selected Character = " + srbg.getRandomCharacter().getName());
        }

        // Generate Random Builds
        System.out.println("\n" + srbg.MYSPACER + " " + nbbuilds + " Random Builds with " + srbg.getNbPerksBuild() + " Perks per Build " + srbg.MYSPACER + "\n");
        Build b = null;
        for (int k = 1; k <= nbbuilds; k++) {
            b = srbg.genRandomBuild("Random Build " + k);
            System.out.println("# " + b.show(false, "\t"));
        }
        System.out.println("");

    }

}
