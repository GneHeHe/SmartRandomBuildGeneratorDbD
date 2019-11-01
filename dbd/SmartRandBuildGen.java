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
 * @author GneHeHe (2019)
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
    private ArrayList<Character> l_char_survivor_generic;
    // List of Killer Characters
    private ArrayList<Character> l_char_killer;
    private ArrayList<Character> l_char_killer_generic;
    // List of All Characters
    private ArrayList<String> l_char_all_string;
    // Synergy Object
    private Synergy synergy;
    // Saved Build
    private Build best_build;
    // Active Side
    private String side;
    // Active Character
    private Character character;
    // Prefix for Constraints
    private final String s_cons1_surv = "SURV_1_CARE";
    private final String s_cons2_surv = "SURV_2_SURVIVAL";
    private final String s_cons3_surv = "SURV_3_CHASE";
    private final String s_cons4_surv = "SURV_4_DETECT";
    private final String s_cons1_killer = "KILLER_1_SLOWDOWN";
    private final String s_cons2_killer = "KILLER_2_CHASE";
    private final String s_cons3_killer = "KILLER_3_DETECT";
    private final String s_cons4_killer = "KILLER_4_ENDGAME";
    // List of Constraints for both Sides
    private List<String> l_cons1;
    private List<String> l_cons2;
    private List<String> l_cons3;
    private List<String> l_cons4;
    private List<String> l_cons1_surv;
    private List<String> l_cons1_killer;
    private List<String> l_cons2_surv;
    private List<String> l_cons2_killer;
    private List<String> l_cons3_surv;
    private List<String> l_cons3_killer;
    private List<String> l_cons4_surv;
    private List<String> l_cons4_killer;
    // Boolean Constraints for Perks
    private boolean b_cons_warn;
    private boolean b_cons1_perks;
    private boolean b_cons2_perks;
    private boolean b_cons3_perks;
    private boolean b_cons4_perks;
    // Nb of Perks in Build
    private int nb_perks_build;
    // Nb of loaded Perks
    private int nb_perks_all;
    // Nb of active Perks
    private int nb_perks_side;
    // Random Character Status
    private boolean b_character_random;
    // Enable Synergy Rules
    private boolean b_synergy;
    // Update Pool of Perks
    private boolean b_update_pool_perks;

    // Path of Configuration File
    private String config;
    // Verbose Level
    public boolean verbose;
    // Min Weight for Perk after Synergy
    public final int weight_perk_min = 0;
    // Max Weight for Perk after Synergy
    public final int weight_perk_max = 500;
    // Max Nb of Loops
    private final int maxloop = 5000;
    // Default Constraint  File
    private final String s_cons = "data/perk_cons.txt";
    // String Spacer
    private final String MYSPACER = "##########";
    // Version & Title of Tool
    public final static double VERSION = 1.8;
    public final static String TITLE = "Smart Random Build Generator for Dead by Daylight ( SRBG " + VERSION + " )";
    // GitHub User/Repos
    public final static String GIT_USER = "GneHeHe";
    public final static String GIT_REPO = "SmartRandomBuildGeneratorDbD";
    public final static String GIT_DB_REMOTE = "https://raw.githubusercontent.com/GneHeHe/SmartRandomBuildGeneratorDbD/master/dbd/data/build_db.txt";

    /**
     * Default Constructor
     *
     * @param verbose
     */
    public SmartRandBuildGen(boolean verbose) {

        System.out.println("\n" + MYSPACER + " " + TITLE + " " + MYSPACER + "\n");

        // Set Verbose Mode
        this.verbose = verbose;

        // Set Reference Lists
        this.l_perks_all = new ArrayList<>();
        this.l_perks_all_string = new ArrayList<>();
        this.l_perks_pool = new ArrayList<>();
        this.l_perks_survivor = new ArrayList<>();
        this.l_perks_killer = new ArrayList<>();
        this.l_char_survivor = new ArrayList<>();
        this.l_char_survivor_generic = new ArrayList<>();
        this.l_char_killer = new ArrayList<>();
        this.l_char_killer_generic = new ArrayList<>();
        this.l_char_all_string = new ArrayList<>();

        // Define both default Side & Nb of Perks
        this.side = "";
        this.setSide("Random");
        this.setNbPerksBuild(4);

        // Read default Weight Distribution File
        this.initConfigFile();
        this.initCharacters();

        // Init Synergy
        this.b_synergy = true;
        this.synergy = new Synergy(l_perks_all_string, l_char_all_string, this.verbose);

        // Set Constraints
        this.b_cons_warn = false;
        this.setConstraintsPerks(1, false);
        this.setConstraintsPerks(2, false);
        this.setConstraintsPerks(3, false);
        this.setConstraintsPerks(4, false);

        // Init Constraints for Perks
        this.l_cons1 = new ArrayList<>();
        this.l_cons2 = new ArrayList<>();
        this.l_cons3 = new ArrayList<>();
        this.l_cons4 = new ArrayList<>();
        this.l_cons1_surv = new ArrayList<>();
        this.l_cons2_surv = new ArrayList<>();
        this.l_cons3_surv = new ArrayList<>();
        this.l_cons4_surv = new ArrayList<>();
        this.l_cons1_killer = new ArrayList<>();
        this.l_cons2_killer = new ArrayList<>();
        this.l_cons3_killer = new ArrayList<>();
        this.l_cons4_killer = new ArrayList<>();
        this.initPerkConstraints();

        // Set Character Status
        this.b_character_random = false;
        if (this.side.equals("Killer")) {
            this.character = l_char_killer.get(0);
        } else {
            this.character = l_char_survivor.get(0);
        }

        // Update Pool of Perks
        this.setUpdatePerkPool(true);
        // Update Pool of Perks if needed
        this.updatePerkPool(false);

        // Define best generated Build
        this.best_build = null;

    }

    /**
     * Set same Weight for all Perks
     *
     * @param value
     */
    public void setSameWeight(int value) {
        System.out.println("# All perks now have the same weight = " + value + "\n");
        for (Perk p : this.l_perks_all) {
            p.setWeight(value, true);
        }
        // Update Pool of Perks
        this.setUpdatePerkPool(true);
        // Display Perks
        showPerks(false);
    }

    /**
     * Set reference Weight for all Perks
     *
     */
    public void setWeightRef() {
        for (Perk p : this.l_perks_all) {
            p.setWeight(p.getWeightRef(), false);
        }
        // Update Pool of Perks
        this.setUpdatePerkPool(true);
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
        switch (side) {
            case "Survivor":
                return this.l_perks_survivor;
            case "Killer":
                return this.l_perks_killer;
            default:
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
     * @return
     */
    public Character retrieveCharacter(String name) {
        for (Character c : this.l_char_survivor) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        for (Character c : this.l_char_killer) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Set active Character
     *
     * @param name
     */
    public void setCharacter(String name) {
        // Retrieve Character
        this.character = retrieveCharacter(name);
        if (this.character == null) {
            System.err.println("\n# ERROR: Wrong Character Name '" + name + "' with current Side '" + this.side + "' \n");
            System.exit(0);
        }
        // Update Side
        setSide(this.character.getSide());
        // Update Character Status
        setCharacterRandomStatus(false);

        System.out.println("# Defined Character = " + this.character.getName() + " | Side = " + this.side);
    }

    /**
     * Set active Character
     *
     * @param character
     */
    public void setCharacter(Character character) {
        // Set Character
        this.character = character;
        if (!this.character.getSide().equals(this.side)) {
            System.err.println("\n# ERROR: Wrong Character Name '" + character.getName() + "' with current Side '" + this.side + "' \n");
            System.exit(0);
        }
        // Update Character Status
        setCharacterRandomStatus(false);
        System.out.println("# Defined Character = " + this.character.getName() + " | Side = " + this.side);
    }

    /**
     * Get active Character
     *
     * @return
     */
    public Character getCharacter() {
        return this.character;
    }

    /**
     * Get a random Character
     *
     * @return
     */
    public final Character getCharacterRandom() {
        // Get Generic Character
        Character c = getCharacterList(this.side, true).get(0);
        if (this.b_character_random) {
            // Get Random non-generic Character
            int rand;
            if (this.side.equals("Survivor")) {
                rand = Math.max(1, (int) (this.l_char_survivor.size() * Math.random()));
                c = this.l_char_survivor.get(rand);
            } else if (this.side.equals("Killer")) {
                rand = Math.max(1, (int) (this.l_char_killer.size() * Math.random()));
                c = l_char_killer.get(rand);
            }
        }
        System.out.println("# Random Character = " + c.getName() + " | Side = " + this.side + "\n");
        return c;
    }

    /**
     * Get Character List
     *
     * @param side
     * @param generic
     * @return
     */
    public ArrayList<Character> getCharacterList(String side, boolean generic) {
        if (side.equals("Survivor")) {
            if (generic) {
                return this.l_char_survivor_generic;
            } else {
                return this.l_char_survivor;
            }
        } else if (side.equals("Killer")) {
            if (generic) {
                return this.l_char_killer_generic;
            } else {
                return this.l_char_killer;
            }
        } else {
            return null;
        }
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
    public boolean getCharacterRandomStatus() {
        return this.b_character_random;
    }

    /**
     * Set Random Character Status
     *
     * @param b
     */
    public final void setCharacterRandomStatus(boolean b) {
        this.b_character_random = b;
        if (this.verbose) {
            System.out.println("# Random Character Selection = " + this.b_character_random + "\n");
        }
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
     * Get Best Build
     *
     * @return
     */
    public Build getBestBuild() {
        return this.best_build;
    }

    /**
     * Set Best Build
     *
     * @param b
     */
    public void setBestBuild(Build b) {
        this.best_build = b;
    }

    /**
     * Set active Side
     *
     * @param s
     */
    public final void setSide(String s) {
        // Random Selection Case
        if (s.equals("Random")) {
            s = choseSideRandom();
        }
        // Update Nb Perks on Active Side
        if (s.equals("Survivor")) {
            this.nb_perks_side = l_perks_survivor.size();
            this.l_cons1 = this.l_cons1_surv;
            this.l_cons2 = this.l_cons2_surv;
            this.l_cons3 = this.l_cons3_surv;
            this.l_cons4 = this.l_cons4_surv;
        } else if (s.equals("Killer")) {
            this.nb_perks_side = l_perks_killer.size();
            this.l_cons1 = this.l_cons1_killer;
            this.l_cons2 = this.l_cons2_killer;
            this.l_cons3 = this.l_cons3_killer;
            this.l_cons4 = this.l_cons4_killer;
        } else {
            System.err.println("\n# ERROR: The side must be either 'Survivor' OR 'Killer' OR 'Random'\n");
            System.exit(0);
        }
        // Copy current Side & Update Active Side
        String side_old = this.side;
        this.side = s;
        if (this.verbose) {
            System.out.println("# Active Side = " + this.side + "\n");
        }
        // Update Pool of Perks if needed
        if (!this.side.equals(side_old)) {
            this.setUpdatePerkPool(true);
        } else {
            this.setUpdatePerkPool(false);
        }
        this.updatePerkPool(true);
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
     * Set Contraints on selected Perks
     *
     * @param n
     * @param b
     */
    public final void setConstraintsPerks(int n, boolean b) {
        int cons = 0;
        if (this.b_cons1_perks || ((n == 1) && b)) {
            cons++;
        }
        if (this.b_cons2_perks || ((n == 2) && b)) {
            cons++;
        }
        if (this.b_cons3_perks || ((n == 3) && b)) {
            cons++;
        }
        if (this.b_cons4_perks || ((n == 4) && b)) {
            cons++;
        }
        // Disable Constraints in this Case
        if (cons > this.nb_perks_build) {
            System.out.println("# WARNING: Not enough Perks in desired Build to activate so many Constraints => all Constraints are Reseted");
            this.b_cons_warn = true;
            this.b_cons1_perks = false;
            this.b_cons2_perks = false;
            this.b_cons3_perks = false;
            this.b_cons4_perks = false;
            //System.out.println("# Constraints on Set of Perks 1 = " + this.b_cons1_perks);
            //System.out.println("# Constraints on Set of Perks 2 = " + this.b_cons2_perks);
            //System.out.println("# Constraints on Set of Perks 3 = " + this.b_cons3_perks);
            //System.out.println("# Constraints on Set of Perks 4 = " + this.b_cons4_perks);
        } else {
            this.b_cons_warn = false;
        }
        if (!this.b_cons_warn) {
            switch (n) {
                case 1:
                    this.b_cons1_perks = b;
                    System.out.println("# Constraints on Set of Perks 1 = " + this.b_cons1_perks);
                    break;
                case 2:
                    this.b_cons2_perks = b;
                    System.out.println("# Constraints on Set of Perks 2 = " + this.b_cons2_perks);
                    break;
                case 3:
                    this.b_cons3_perks = b;
                    System.out.println("# Constraints on Set of Perks 3 = " + this.b_cons3_perks);
                    break;
                case 4:
                    this.b_cons4_perks = b;
                    System.out.println("# Constraints on Set of Perks 4 = " + this.b_cons4_perks);
                    break;
                default:
                    System.err.println("\n# Wrong Constraint Class => Exit");
                    System.exit(0);
            }
        }
    }

    /**
     * Get Contraints Status on Perks Classes
     *
     * @param n
     * @return
     */
    public boolean getConstraintsPerks(int n) {
        switch (n) {
            case 1:
                return this.b_cons1_perks;
            case 2:
                return this.b_cons2_perks;
            case 3:
                return this.b_cons3_perks;
            case 4:
                return this.b_cons4_perks;
            default:
                System.err.println("\n# Generic ERROR => Exit");
                System.exit(0);
        }
        return false;
    }

    /**
     * Get concerned Perks as String for a given Constraint Class
     *
     * @param n
     * @return
     */
    public String getConstraints(int n) {
        String s = "";
        List<String> l = null;
        switch (n) {
            case 1:
                l = this.l_cons1;
                break;
            case 2:
                l = this.l_cons2;
                break;
            case 3:
                l = this.l_cons3;
                break;
            case 4:
                l = this.l_cons4;
                break;
            default:
                System.err.println("\n# Generic ERROR => Exit");
                System.exit(0);
        }
        if (!l.isEmpty()) {
            for (String e : l) {
                s = s + e + ", ";
            }
            s = s.trim().substring(0, s.length() - 2);
        }
        return s;
    }

    /**
     * Get concerned Perks as String for a given Constraint Class
     *
     * @param n
     * @param side
     * @return
     */
    public String getConstraints(int n, String side) {
        if ((!side.equals("Survivor")) && (!side.equals("Killer"))) {
            System.err.println("\n# ERROR: Wrong side\n");
            System.exit(0);
        }
        String s = "";
        List<String> l = null;
        switch (n) {
            case 1:
                switch (side) {
                    case "Survivor":
                        l = this.l_cons1_surv;
                        s = this.s_cons1_surv;
                        break;
                    case "Killer":
                        l = this.l_cons1_killer;
                        s = this.s_cons1_killer;
                        break;
                }
                break;
            case 2:
                switch (side) {
                    case "Survivor":
                        l = this.l_cons2_surv;
                        s = this.s_cons2_surv;
                        break;
                    case "Killer":
                        l = this.l_cons2_killer;
                        s = this.s_cons2_killer;
                        break;
                }
                break;
            case 3:
                switch (side) {
                    case "Survivor":
                        l = this.l_cons3_surv;
                        s = this.s_cons3_surv;
                        break;
                    case "Killer":
                        l = this.l_cons3_killer;
                        s = this.s_cons3_killer;
                        break;
                }
                break;
            case 4:
                switch (side) {
                    case "Survivor":
                        l = this.l_cons4_surv;
                        s = this.s_cons4_surv;
                        break;
                    case "Killer":
                        l = this.l_cons4_killer;
                        s = this.s_cons4_killer;
                        break;
                }
                break;
            default:
                System.err.println("\n# Generic ERROR (n=" + n + ", side=" + side + ") => Exit");
                System.exit(0);
        }
        s = s + ": ";
        if (!l.isEmpty()) {
            for (String e : l) {
                s = s + e + ", ";
            }
            s = s.trim().substring(0, s.length() - 2);
        }
        return s;
    }

    /**
     * Display all Perks & Features
     *
     * @param detail
     */
    public void showPerks(boolean detail) {
        System.out.println("# All Loaded Perks (" + this.nb_perks_all + " Perks)\n");
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
     * Set Synergy Mode
     *
     * @param b
     */
    public void setSynergy(boolean b) {
        this.b_synergy = b;
        System.out.println("\n# Synergy Mode = " + this.b_synergy + "\n");
    }

    /**
     * Get Synergy Mode
     *
     * @return
     */
    public boolean getSynergy() {
        return this.b_synergy;
    }

    /**
     * Get Status of Pool of Perks
     *
     * @return
     */
    public boolean getUpdatePerkPool() {
        return this.b_update_pool_perks;
    }

    /**
     * Set Status of Pool of Perks
     *
     * @param b
     */
    public final void setUpdatePerkPool(boolean b) {
        this.b_update_pool_perks = b;
        //System.out.println("# Update Pool of Perks = " + this.b_update_pool_perks + "\n");
    }

    /**
     * Update the Pool of Perks (with/without reset)
     *
     * @param reset
     */
    public final void updatePerkPool(boolean reset) {
        if (reset) {
            // Restore reference Weights
            setWeightRef();
        }
        if (getUpdatePerkPool()) {
            //System.out.print("# Orig Pool Size = " + this.l_perks_pool.size());
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
            //System.out.println(" | New Pool Size = " + this.l_perks_pool.size());
            setUpdatePerkPool(false);
        }
    }

    /**
     * Generate single Random Build
     *
     * @param buildname
     * @return
     */
    public Build genRandomBuild(String buildname) {
        // Define Random Build
        Build b = new Build();
        b.setName(buildname);
        b.setSide(this.side);
        // Get Random Character if desired
        if (getCharacterRandomStatus()) {
            b.setCharacter(this.getCharacterRandom());
        } else {
            b.setCharacter(this.character);
        }
        // Define List of current Perk
        List<String> l_perk_ok = new ArrayList<>();
        // Several Loops may be required if Constraints are enabled
        int nbloop = 1;
        while (true) {
            // Loop until either valid Build was generated or max loops reached
            if (this.verbose) {
                System.out.print("# Loop " + nbloop);
            }
            // Restore reference Weights
            setWeightRef();
            // Apply Synergy Rules with current Character
            if (this.b_synergy) {
                this.synergy.update_weights(b.getCharacter().getName(), null, this);
            }
            // Update Pool of Perks
            updatePerkPool(false);
            // Reset List of selected Perks
            l_perk_ok.clear();
            while (l_perk_ok.size() < getNbPerksBuild()) {
                // Loop until desired number of perks was reached
                int rand = (int) (l_perks_pool.size() * Math.random());
                // Get a random Perk from Pool
                String random_perk = l_perks_pool.get(rand);
                if (!l_perk_ok.contains(random_perk)) {
                    // New Perk found => added to the Build
                    l_perk_ok.add(random_perk);
                    if (this.verbose) {
                        System.out.print(" | " + random_perk);
                    }
                    // Apply Synergy Rules with current Perk & Update Pool of Perks if needed
                    if (this.b_synergy && (l_perk_ok.size() < getNbPerksBuild())) {
                        if (this.synergy.update_weights(null, random_perk, this)) {
                            setUpdatePerkPool(true);
                            updatePerkPool(false);
                        }
                    }
                }
            }
            // Define Constraint Booleans
            boolean b_cons1_found = false;
            boolean b_cons2_found = false;
            boolean b_cons3_found = false;
            boolean b_cons4_found = false;
            for (String p : l_perk_ok) {
                if (this.l_cons1.contains(p)) {
                    // Perk from Set1 Found => Update Boolean
                    b_cons1_found = true;
                    break;
                }
            }
            for (String p : l_perk_ok) {
                if (this.l_cons2.contains(p)) {
                    // Perk from Set1 Found => Update Boolean
                    b_cons2_found = true;
                    break;
                }
            }
            for (String p : l_perk_ok) {
                if (this.l_cons3.contains(p)) {
                    // Perk from Set1 Found => Update Boolean
                    b_cons3_found = true;
                    break;
                }
            }
            for (String p : l_perk_ok) {
                if (this.l_cons4.contains(p)) {
                    // Perk from Set1 Found => Update Boolean
                    b_cons4_found = true;
                    break;
                }
            }
            //System.out.println("\n# BOOLEANS: " + b_cons1_found + " " + b_cons2_found + " " + b_cons3_found + " " + b_cons4_found);
            // Check all Criteria to validate current Build
            boolean b_cons1_check = (!this.b_cons1_perks) || (this.b_cons1_perks && b_cons1_found);
            boolean b_cons2_check = (!this.b_cons2_perks) || (this.b_cons2_perks && b_cons2_found);
            boolean b_cons3_check = (!this.b_cons3_perks) || (this.b_cons3_perks && b_cons3_found);
            boolean b_cons4_check = (!this.b_cons4_perks) || (this.b_cons4_perks && b_cons4_found);
            if (b_cons1_check && b_cons2_check && b_cons3_check && b_cons4_check) {
                // All Criteria are Ok
                break;
            }
            nbloop++;
            if (nbloop >= this.maxloop) {
                // Max Loop Reached
                System.out.println("\n# WARNING: Max loop (" + this.maxloop + ") reached !\n");
                return b;
            }
            if (this.verbose) {
                System.out.println("");
            }
        }
        // All each validated Perk to the Build and Compute Score (Sum of Weights)
        int score = 0;
        Collections.sort(l_perk_ok);
        for (String s : l_perk_ok) {
            Perk p = getPerk(s);
            b.addPerk(p);
            score = score + p.getWeight();
        }
        b.setScore(score);
        // Return Build
        return b;
    }

    /**
     * Set Weights from Configuration File
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
                System.out.println("# Loading custom Weight Distribution from " + input + "\n");
                br = new BufferedReader(new FileReader(new File(input)));
            } else {
                InputStream is = getClass().getResourceAsStream(input);
                System.out.println("# Loading default Weight Distribution from " + input + "\n");
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
                    if (myweight > weight_perk_max) {
                        myweight = weight_perk_max;
                    } else if (myweight < weight_perk_min) {
                        myweight = weight_perk_min;
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
        // Sort Lists
        Collections.sort(this.l_perks_all);
        Collections.sort(this.l_perks_all_string);
        Collections.sort(this.l_perks_survivor);
        Collections.sort(this.l_perks_killer);
        // Set Nb of Perks
        this.nb_perks_all = this.l_perks_all.size();
        // Update Nb of Active Perks
        if (this.side.equals("Survivor")) {
            this.nb_perks_side = l_perks_survivor.size();
        } else if (this.side.equals("Killer")) {
            this.nb_perks_side = l_perks_killer.size();
        }
        // Display Perks        
        if (this.verbose) {
            showPerks(false);
        }
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
        this.l_char_survivor_generic.add(c);
        c = new Character("Killer");
        this.l_char_killer.add(c);
        this.l_char_killer_generic.add(c);
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
                    this.l_char_all_string.add(myname);
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
     * Init Perk Constraints
     *
     */
    private void initPerkConstraints() {
        String line = null;
        String perk = "";
        try {
            // Define the Reader
            BufferedReader br = null;
            // Try to detect a custom Perk Synergy File in the current Directory
            String f = System.getProperty("user.dir") + File.separator + "perk_constraints_custom.txt";
            if (new File(f).exists()) {
                System.out.println("\n# Loading custom Perk Constraints from " + f);
                br = new BufferedReader(new FileReader(new File(f).getAbsolutePath()));
            } else {
                System.out.println("\n# Loading default Perk Constraints from " + this.s_cons);
                InputStream is = getClass().getResourceAsStream(this.s_cons);
                br = new BufferedReader(new InputStreamReader(is));
            }
            line = br.readLine();
            while (line != null) {
                if (line.split("\t").length == 2) {
                    perk = line.split("\t")[1];
                    // Check Perk
                    if (l_perks_all_string.contains(perk)) {
                        // Add Perk to reference List
                        if (line.startsWith(this.s_cons1_surv)) {
                            l_cons1_surv.add(perk);
                        } else if (line.startsWith(this.s_cons2_surv)) {
                            l_cons2_surv.add(perk);
                        } else if (line.startsWith(this.s_cons3_surv)) {
                            l_cons3_surv.add(perk);
                        } else if (line.startsWith(this.s_cons4_surv)) {
                            l_cons4_surv.add(perk);
                        } else if (line.startsWith(this.s_cons1_killer)) {
                            l_cons1_killer.add(perk);
                        } else if (line.startsWith(this.s_cons2_killer)) {
                            l_cons2_killer.add(perk);
                        } else if (line.startsWith(this.s_cons3_killer)) {
                            l_cons3_killer.add(perk);
                        } else if (line.startsWith(this.s_cons4_killer)) {
                            l_cons4_killer.add(perk);
                        } else {
                            System.err.println("\n# ERROR: Issues with the constraints File on Line >" + line + "<\n");
                            System.exit(0);
                        }
                    } else {
                        System.err.println("\n# ERROR: Perk '" + perk + "' does not exist !\n");
                        System.exit(0);
                    }
                } else {
                    System.err.println("\n# ERROR: Issues with the constraints File on Line >" + line + "<\n");
                    System.exit(0);
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            System.err.println("\n# ERROR: Issues with the constraints File on Line >" + line + "<\n");
            System.exit(0);
        }
        // Sort Lists
        Collections.sort(l_cons1_surv);
        Collections.sort(l_cons2_surv);
        Collections.sort(l_cons3_surv);
        Collections.sort(l_cons4_surv);
        Collections.sort(l_cons1_killer);
        Collections.sort(l_cons2_killer);
        Collections.sort(l_cons3_killer);
        Collections.sort(l_cons4_killer);
        // Display Perk Constraints
        showPerkConstraints();
    }

    /**
     * Init Perk Constraints
     *
     */
    private void showPerkConstraints() {
        System.out.println("\n# Perk Constraints on Survivor Side");
        System.out.println("# Set of Perks 1 = " + getConstraints(1, "Survivor"));
        System.out.println("# Set of Perks 2 = " + getConstraints(2, "Survivor"));
        System.out.println("# Set of Perks 3 = " + getConstraints(3, "Survivor"));
        System.out.println("# Set of Perks 4 = " + getConstraints(4, "Survivor") + "\n");
        System.out.println("# Perk Constraints on Killer Side");
        System.out.println("# Set of Perks 1 = " + getConstraints(1, "Killer"));
        System.out.println("# Set of Perks 2 = " + getConstraints(2, "Killer"));
        System.out.println("# Set of Perks 3 = " + getConstraints(3, "Killer"));
        System.out.println("# Set of Perks 4 = " + getConstraints(4, "Killer") + "\n");
    }

    /**
     * Display Parameters
     *
     */
    public void showParams() {
        System.out.println("\n" + MYSPACER + " Input Parameters " + MYSPACER + "\n");
        System.out.println("# Nb of Loaded Perks = " + this.nb_perks_all);
        System.out.println("# Active Side = " + this.side);
        System.out.println("# Nb of Perks on Active Side = " + this.nb_perks_side);
        System.out.println("# Active Character = " + this.character.getName());
        System.out.println("# Random Character Selection = " + this.b_character_random);
        System.out.println("# Nb of Perks per Build = " + this.nb_perks_build);
        System.out.println("# Perk from Set 1 is Required = " + this.b_cons1_perks);
        //System.out.println("# Perk from Set 1 = " + getConstraints(1, this.side));
        System.out.println("# Perk from Set 2 is Required = " + this.b_cons2_perks);
        //System.out.println("# Perk from Set 2 = " + getConstraints(2, this.side));
        System.out.println("# Perk from Set 3 is Required = " + this.b_cons3_perks);
        //System.out.println("# Perk from Set 3 = " + getConstraints(3, this.side));
        System.out.println("# Perk from Set 4 is Required = " + this.b_cons4_perks);
        //System.out.println("# Perk from Set 4 = " + getConstraints(4, this.side));
        System.out.println("# Synergy Mode = " + this.b_synergy);
        System.out.println("# Verbose Mode = " + this.verbose);
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
        System.out.println("#  -char : define the desired character");
        System.out.println("#  -cons1 : enable constraints for 1st set of perks");
        System.out.println("#  -cons2 : enable constraints for 2nd set of perks");
        System.out.println("#  -cons3 : enable constraints for 3rd set of perks");
        System.out.println("#  -cons4 : enable constraints for 4th set of perks");
        System.out.println("#  -nosyn : disable synergy rules");
        System.out.println("#  -h : print this help and quit\n");
        showPerkConstraints();
        System.exit(0);
    }

    /**
     * Random Selection of Active Side
     *
     * @return
     */
    public String choseSideRandom() {
        System.out.println("# Random Side Selection\n");
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
        SmartRandBuildGen srbg = new SmartRandBuildGen(false);

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
            } else if (args[i].equals("-nosyn")) {
                srbg.setSynergy(false);
            } else if (args[i].equals("-char")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        val = args[i + 1];
                        srbg.setCharacter(val);
                        srbg.setCharacterRandomStatus(false);
                    } else {
                        System.err.println("\n# ERROR: The '-char' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.err.println("\n# ERROR: The '-char' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-cons1")) {
                srbg.setConstraintsPerks(1, true);
            } else if (args[i].equals("-cons2")) {
                srbg.setConstraintsPerks(2, true);
            } else if (args[i].equals("-cons3")) {
                srbg.setConstraintsPerks(3, true);
            } else if (args[i].equals("-cons4")) {
                srbg.setConstraintsPerks(4, true);
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
                srbg.verbose = true;
            } else if (args[i].equals("-h")) {
                srbg.displayHelp();
            } else if (args[i].startsWith("-")) {
                System.err.println("\n# ERROR: Wrong option '" + args[i] + "'\n");
                System.exit(0);
            }
        }

        // Display loaded Parameters
        srbg.showParams();

        // Generate Random Builds
        List l = new ArrayList();
        System.out.println("\n" + srbg.MYSPACER + " " + nbbuilds + " Random Builds with " + srbg.getNbPerksBuild() + " Perks per Build " + srbg.MYSPACER + "\n");
        Build b = null;
        for (int k = 1; k <= nbbuilds; k++) {
            b = srbg.genRandomBuild("Random Build " + k);
            l.add(b);
            System.out.println("\n# " + b.show(true, " ") + "\n");
        }

        // Display Best Build
        srbg.setBestBuild(Build.getBestBuild(l));
        System.out.println("# Best Build over Generated Builds\n" + srbg.getBestBuild().show(true, " ") + "\n");

    }

}
