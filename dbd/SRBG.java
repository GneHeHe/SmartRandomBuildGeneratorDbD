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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * SRBG
 *
 * @author GneHeHe (2019)
 *
 */
public class SRBG {

    // List of all Perks
    private ArrayList<Perk> l_perks_all;
    private ArrayList<String> l_perks_all_string;
    private ArrayList<String> l_perks_pool;
    private ArrayList<String> l_perks_pool_default;
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
    // Random-based Characters (Objects)
    private Map<String, Integer> m_char_random_killer;
    private Map<String, Integer> m_char_random_survivor;
    private ArrayList<String> l_char_random;
    // Random-based Characters (Constants)
    private int char_val_orig = 20;
    private int char_val_dec = 18;
    // Synergy Object
    private Synergy synergy;
    // Nb Builds to Generate
    private int nb_builds;
    // Nb of Best Builds to Display
    private int nb_best_builds;
    // Saved Build
    private Build best_build;
    // Active Side
    private String side = "";
    // Different Sides
    public final String s_side_surv = "Survivor";
    public final String s_side_killer = "Killer";
    public final String s_side_rand = "Random";
    // Active Character
    private Character character;
    // Prefix for Constraints
    private final String s_cons1_surv = "SURVIV_1";
    private final String s_cons2_surv = "SURVIV_2";
    private final String s_cons3_surv = "SURVIV_3";
    private final String s_cons4_surv = "SURVIV_4";
    private final String s_cons1_killer = "KILLER_1";
    private final String s_cons2_killer = "KILLER_2";
    private final String s_cons3_killer = "KILLER_3";
    private final String s_cons4_killer = "KILLER_4";
    private String s_cons1_surv_txt;
    private String s_cons2_surv_txt;
    private String s_cons3_surv_txt;
    private String s_cons4_surv_txt;
    private String s_cons1_killer_txt;
    private String s_cons2_killer_txt;
    private String s_cons3_killer_txt;
    private String s_cons4_killer_txt;
    // Bonus Weight for Constrained Perks (high value may be misleading)
    public final int weight_perk_bonus = 150;
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
    // Thresholds for Random Nb of Perks
    private double rand_nb_perk1 = 0.03; // 0.03 ; 0.25
    private double rand_nb_perk2 = 0.10; // 0.10 ; 0.50
    private double rand_nb_perk3 = 0.25; // 0.25 ; 0.75
    // Nb of loaded Perks
    private int nb_perks_all;
    // Nb of active Perks
    private int nb_perks_side;
    // Enable Synergy Rules
    private boolean b_synergy;
    // Update Pool of Perks
    private boolean b_perkpool_changed;
    // Path of Weight File
    private String weight_file;
    // Full Random Mode
    public boolean b_random;
    // Verbose Level
    public boolean b_verbose;
    // Default Nb of Perks in Builds
    public final int nb_perks_ref = 4;
    // Generic Perk
    public final Perk perk_generic = new Perk();
    // Min Weight for Perk (normal)
    public final int weight_perk_min = 0;
    // Max Weight for Perk (normal and after synergy)
    public final int weight_perk_max = 500;
    // Min Weight for Perk after Synergy (Reevaluation Mode / Critical Value)
    public final int syn_min_weight = 130;
    // Score Penalty for Build with Lack of Synergy (Reevaluation Mode)
    public final int syn_penalty = -1000;
    // Max Nb of Loops
    private final int maxloop = 3000;
    // Character File
    private final String s_char = "data/characters.txt";
    // Character Status File
    private final String s_char_disabled = "disabled_chars.txt";
    // Perk DB Files
    private final String s_perk = "data/perk_db.txt";
    public final String s_perk_custom = "perk_db_custom.txt";
    // Constraint Files
    private final String s_cons = "data/perk_cons.txt";
    private final String s_cons_custom = "perk_cons_custom.txt";
    // Version
    public final double VERSION = 3.0;
    // Title
    public final String TITLE = "Smart Random Build Generator for Dead by Daylight ( SRBG " + VERSION + " )";
    // GitHub Data
    public final String GIT_USER = "GneHeHe";
    public final String GIT_REPO = "SmartRandomBuildGeneratorDbD";
    public final String GIT_URL = "https://github.com/" + GIT_USER + "/" + GIT_REPO;
    public final String GIT_URL_RAW = "https://raw.githubusercontent.com/" + GIT_USER + "/" + GIT_REPO;
    public final String GIT_URL_API = "https://api.github.com/repos/" + GIT_USER + "/" + GIT_REPO + "/releases";
    // Author Information
    public final String GUIDE = "https://steamcommunity.com/sharedfiles/filedetails/?id=1641511649";
    public final String EMAIL = "gnehehe70@gmail.com";
    public final String PAYPAL = "https://www.paypal.me/gnehehe";
    // String Spacer
    private final String MYSPACER = "##########";

    /**
     * Default Constructor
     *
     * @param v
     */
    public SRBG(boolean v) {

        System.out.println("\n" + MYSPACER + " " + TITLE + " " + MYSPACER);

        // Define Verbose Mode (ON at start)
        b_verbose = true;

        // Define Reference Lists
        l_perks_all = new ArrayList<>();
        l_perks_all_string = new ArrayList<>();
        l_perks_pool = new ArrayList<>();
        l_perks_pool_default = new ArrayList<>();
        l_perks_survivor = new ArrayList<>();
        l_perks_killer = new ArrayList<>();
        l_char_survivor = new ArrayList<>();
        l_char_survivor_generic = new ArrayList<>();
        l_char_killer = new ArrayList<>();
        l_char_killer_generic = new ArrayList<>();
        l_char_all_string = new ArrayList<>();
        l_char_random = new ArrayList<>();

        // Map for Random-based Characters
        m_char_random_killer = new HashMap<>();
        m_char_random_survivor = new HashMap<>();

        // Random Mode OFF
        b_random = false;

        // Define Nb of Perks in a Build
        nb_perks_build = 4;

        // Define Nb Builds
        nb_builds = 5;

        // Define Nb of Best Builds to Display
        nb_best_builds = 3;

        // Init Characters
        initCharacters();

        // Init Weights
        readWeightsDefault();

        // Init Synergy
        b_synergy = true;
        synergy = new Synergy(l_perks_all_string, l_char_all_string, b_verbose);

        // Set Constraints
        b_cons_warn = false;
        setConstraintsPerks(1, false);
        setConstraintsPerks(2, false);
        setConstraintsPerks(3, false);
        setConstraintsPerks(4, false);

        // Init Constraints for Perks
        l_cons1 = new ArrayList<>();
        l_cons2 = new ArrayList<>();
        l_cons3 = new ArrayList<>();
        l_cons4 = new ArrayList<>();
        l_cons1_surv = new ArrayList<>();
        l_cons2_surv = new ArrayList<>();
        l_cons3_surv = new ArrayList<>();
        l_cons4_surv = new ArrayList<>();
        l_cons1_killer = new ArrayList<>();
        l_cons2_killer = new ArrayList<>();
        l_cons3_killer = new ArrayList<>();
        l_cons4_killer = new ArrayList<>();
        initPerkConstraints();

        // Define side
        setSide(s_side_rand);

        // Update Pool of Perks
        updatePerkPool(false);

        // Define best generated Build
        best_build = null;

        // Define Verbose Mode
        b_verbose = v;

    }

    /**
     * Set same Weight for all Perks
     *
     * @param value
     */
    public void setSameWeight(int value) {
        System.out.println("# All perks now have the same weight = " + value + "\n");
        for (Perk p : l_perks_all) {
            p.setWeight(value, true);
        }
        // Update Pool of Perks
        setPerkPoolChanged(true);
        if (b_verbose) {
            // Display Perks
            showPerks(false);
        }
        // Random Mode ON
        b_random = true;
    }

    /**
     * Set reference Weight for all Perks
     *
     */
    public void setWeightRef() {
        for (Perk p : l_perks_all) {
            p.setWeight(p.getWeightRef(), false);
        }
        // Update Pool of Perks
        setPerkPoolChanged(true);
    }

    /**
     * Get Perk List
     *
     * @return
     */
    public List<Perk> getPerks() {
        return l_perks_all;
    }

    /**
     * Get Random Perk
     *
     * @return
     */
    public Perk getPerkRandom() {
        List<String> l = getPerks(side);
        int i = Math.max(1, ((int) (Math.random() * l.size())));
        return getPerk(l.get(i));
    }

    /**
     * Get Perk List
     *
     * @param side
     * @return
     */
    public List<String> getPerks(String side) {
        switch (side) {
            case s_side_surv:
                return l_perks_survivor;
            case s_side_killer:
                return l_perks_killer;
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
        for (Perk p : l_perks_all) {
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
        for (Character c : l_char_survivor) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        for (Character c : l_char_killer) {
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
        Character c = retrieveCharacter(name);
        if (c == null) {
            System.err.println("\n# ERROR: Unknown Character Name '" + name + "\n");
            System.exit(0);
        }
        // Check if active Side is Ok
        if (!side.equals(c.getSide())) {
            setSide(c.getSide());
        }
        character = c;
        System.out.println("# Defined Character = " + character.getName() + " | Side = " + side);
    }

    /**
     * Get active Character
     *
     * @return
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Get a random Character
     *
     * @return
     */
    /*
    public final Character getCharacterRandom() {
        Character c = null;
        // Get Random non-generic Character
        int rand;
        if (side.equals(s_side_surv)) {
            rand = Math.max(1, (int) (l_char_survivor.size() * Math.random()));
            c = l_char_survivor.get(rand);
        } else if (side.equals(s_side_killer)) {
            rand = Math.max(1, (int) (l_char_killer.size() * Math.random()));
            c = l_char_killer.get(rand);
        }
        System.out.println("# Random Character = " + c.getName() + " | Side = " + side);
        return c;
    }*/
    /**
     * Get a random Character (Updated)
     *
     * @return
     */
    public final Character getCharacterRandom() {
        // Random non-generic Character
        Character c = null;
        // Get Iterator
        Iterator iterator = null;
        switch (side) {
            case s_side_surv:
                iterator = m_char_random_survivor.entrySet().iterator();
                break;
            case s_side_killer:
                iterator = m_char_random_killer.entrySet().iterator();
                break;
            default:
                System.err.println("\n# ERROR: The side must be either 'Survivor' OR 'Killer' OR 'Random'\n");
                System.exit(0);
        }
        // Reset Character List
        l_char_random.clear();
        // Build New Character List
        String char_tmp = "";
        int weight_tmp = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            char_tmp = (String) entry.getKey();
            if (retrieveCharacter(char_tmp).getStatus()) {
                weight_tmp = (int) entry.getValue();
                for (int i = 0; i < weight_tmp; i++) {
                    l_char_random.add(char_tmp);
                }
            }
        }
        // Get Random Character
        int rand = 0;
        if (l_char_random.size() > 1) {
            rand = (int) (l_char_random.size() * Math.random());
        }
        char_tmp = l_char_random.get(rand);
        c = retrieveCharacter(char_tmp);
        System.out.println("# Random Character = " + c.getName() + " | Side = " + side);
        return c;
    }

    /**
     * Update Map Weight associated to a given Character
     *
     * @param name
     */
    protected void updateCharacterMapWeights(String name) {
        int weight;
        // Update Weight associated to a given Character from active Side
        switch (side) {
            case s_side_surv:
                weight = m_char_random_survivor.get(name);
                //System.out.print("\n# " + name + " " + weight);
                m_char_random_survivor.put(name, Math.max(1, weight - char_val_dec));
                //System.out.println(" => " + m_char_random_survivor.get(name) + " | " + l_char_random.size());
                break;
            case s_side_killer:
                weight = m_char_random_killer.get(name);
                //System.out.print("\n# " + name + " " + weight);
                m_char_random_killer.put(name, Math.max(1, weight - char_val_dec));
                //System.out.println(" => " + m_char_random_killer.get(name) + " | " + l_char_random.size());
                break;
            default:
                System.err.println("\n# ERROR: The side must be either 'Survivor' OR 'Killer' OR 'Random'\n");
                System.exit(0);
        }
    }

    /**
     * Get a generic Character
     *
     * @return
     */
    public final Character getCharacterGeneric() {
        Character c = null;
        if (side.equals(s_side_surv)) {
            c = l_char_survivor_generic.get(0);
        } else if (side.equals(s_side_killer)) {
            c = l_char_killer_generic.get(0);
        }
        System.out.println("# Generic Character = " + c.getName() + " | Side = " + side);
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
        switch (side) {
            case s_side_surv:
                if (generic) {
                    return l_char_survivor_generic;
                } else {
                    return l_char_survivor;
                }
            case s_side_killer:
                if (generic) {
                    return l_char_killer_generic;
                } else {
                    return l_char_killer;
                }
            default:
                return null;
        }
    }

    /**
     * Get Nb of Perks in Build
     *
     * @return
     */
    public int getNbPerksBuild() {
        return nb_perks_build;
    }

    /**
     * Set Nb of Perks in Build
     *
     * @param n
     */
    public final void setNbPerksBuild(int n) {
        if ((n >= 0) && (n <= 4)) {
            if (n == 0) {
                System.out.println("# Random Number of Perks");
                double rand = Math.random();
                if (rand <= rand_nb_perk1) {
                    n = 1;
                } else if (rand <= rand_nb_perk2) {
                    n = 2;
                } else if (rand <= rand_nb_perk3) {
                    n = 3;
                } else {
                    n = 4;
                }
            }
            nb_perks_build = n;
            System.out.println("\n# Nb of Perks in a Build = " + nb_perks_build + "\n");
        } else {
            System.out.println("# ERROR: Wrong desired Nb of Perks (" + n + ") in a Build");
            System.exit(0);
        }
    }

    /**
     * Get Nb of desired Builds
     *
     * @return
     */
    public int getNbBuilds() {
        return nb_builds;
    }

    /**
     * Set Nb of desired Builds
     *
     * @param n
     */
    public void setNbBuilds(int n) {
        nb_builds = n;
        System.out.println("\n# Nb of desired Builds = " + nb_builds + "\n");
    }

    /**
     * Get Nb of Best Builds
     *
     * @return
     */
    public int getNbBestBuilds() {
        return nb_best_builds;
    }

    /**
     * Set Nb of Best Builds
     *
     * @param n
     */
    public final void setNbBestBuilds(int n) {
        nb_best_builds = n;
        System.out.println("\n# Nb of Best Builds to Display = " + nb_best_builds + "\n");
    }

    /**
     * Get Nb of Loaded Perks
     *
     * @return
     */
    public int getNbPerksAll() {
        return nb_perks_all;
    }

    /**
     * Get Nb of Perks from Active Side
     *
     * @return
     */
    public int getNbPerksSide() {
        return nb_perks_side;
    }

    /**
     * Get current Weight File
     *
     * @return
     */
    public String getWeightFile() {
        return weight_file;
    }

    /**
     * Set current Weight File
     *
     * @param s
     */
    public void setWeightFile(String s) {
        weight_file = s;
        System.out.println("# Current weight file = " + weight_file + "\n");
    }

    /**
     * Get Best Build
     *
     * @return
     */
    public Build getBestBuild() {
        return best_build;
    }

    /**
     * Set Best Build
     *
     * @param b
     */
    public void setBestBuild(Build b) {
        best_build = b;
    }

    /**
     * Set active Side
     *
     * @param s
     */
    public final void setSide(String s) {
        // Copy current Side & Update Active Side
        String side_old = this.side;
        side = s;
        // Generic Character Mode ON
        boolean char_generic = true;
        // Random Selection Case
        if (s.equals(s_side_rand)) {
            side = choseSideRandom();
            // Get Random Character
            character = getCharacterRandom();
            // Generic Character Mode Off
            char_generic = false;
        }
        // Update Nb Perks on Active Side
        switch (side) {
            case s_side_surv:
                nb_perks_side = l_perks_survivor.size();
                l_cons1 = l_cons1_surv;
                l_cons2 = l_cons2_surv;
                l_cons3 = l_cons3_surv;
                l_cons4 = l_cons4_surv;
                break;
            case s_side_killer:
                nb_perks_side = l_perks_killer.size();
                l_cons1 = l_cons1_killer;
                l_cons2 = l_cons2_killer;
                l_cons3 = l_cons3_killer;
                l_cons4 = l_cons4_killer;
                break;
            default:
                System.err.println("\n# ERROR: The side must be either 'Survivor' OR 'Killer' OR 'Random'\n");
                System.exit(0);
        }
        // Get Generic Character if Desired
        if (char_generic) {
            character = getCharacterGeneric();
        }
        // Update Pool of Perks if needed
        if (!side.equals(side_old)) {
            setPerkPoolChanged(true);
        } else {
            setPerkPoolChanged(false);
        }
    }

    /**
     * Get active Side
     *
     * @return
     */
    public String getSide() {
        return side;
    }

    /**
     * Set Contraints on selected Perks
     *
     * @param n
     * @param b
     */
    public final void setConstraintsPerks(int n, boolean b) {
        int cons = 0;
        if (b_cons1_perks || ((n == 1) && b)) {
            cons++;
        }
        if (b_cons2_perks || ((n == 2) && b)) {
            cons++;
        }
        if (b_cons3_perks || ((n == 3) && b)) {
            cons++;
        }
        if (b_cons4_perks || ((n == 4) && b)) {
            cons++;
        }
        // Disable Constraints in this Case
        if (cons > nb_perks_build) {
            System.out.println("\n# WARNING: Not enough Perks in desired Build to activate so many Constraints => all Constraints are Reseted");
            b_cons_warn = true;
            b_cons1_perks = false;
            b_cons2_perks = false;
            b_cons3_perks = false;
            b_cons4_perks = false;
        } else {
            b_cons_warn = false;
        }
        if (!b_cons_warn) {
            switch (n) {
                case 1:
                    b_cons1_perks = b;
                    System.out.println("\n# Constraints on Set of Perks 1 = " + b_cons1_perks);
                    break;
                case 2:
                    b_cons2_perks = b;
                    System.out.println("\n# Constraints on Set of Perks 2 = " + b_cons2_perks);
                    break;
                case 3:
                    b_cons3_perks = b;
                    System.out.println("\n# Constraints on Set of Perks 3 = " + b_cons3_perks);
                    break;
                case 4:
                    b_cons4_perks = b;
                    System.out.println("\n# Constraints on Set of Perks 4 = " + b_cons4_perks);
                    break;
                default:
                    System.err.println("\n# ERROR: Wrong Constraint Class => Exit\n");
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
                return b_cons1_perks;
            case 2:
                return b_cons2_perks;
            case 3:
                return b_cons3_perks;
            case 4:
                return b_cons4_perks;
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
                l = l_cons1;
                break;
            case 2:
                l = l_cons2;
                break;
            case 3:
                l = l_cons3;
                break;
            case 4:
                l = l_cons4;
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
        if ((!side.equals(s_side_surv)) && (!side.equals(s_side_killer))) {
            System.err.println("\n# ERROR: Wrong side\n");
            System.exit(0);
        }
        String s = "";
        List<String> l = null;
        switch (n) {
            case 1:
                switch (side) {
                    case s_side_surv:
                        l = l_cons1_surv;
                        s = s_cons1_surv_txt + ", " + s_cons1_surv;
                        break;
                    case s_side_killer:
                        l = l_cons1_killer;
                        s = s_cons1_killer_txt + ", " + s_cons1_killer;
                        break;
                }
                break;
            case 2:
                switch (side) {
                    case s_side_surv:
                        l = l_cons2_surv;
                        s = s_cons2_surv_txt + ", " + s_cons2_surv;
                        break;
                    case s_side_killer:
                        l = l_cons2_killer;
                        s = s_cons2_killer_txt + ", " + s_cons2_killer;
                        break;
                }
                break;
            case 3:
                switch (side) {
                    case s_side_surv:
                        l = l_cons3_surv;
                        s = s_cons3_surv_txt + ", " + s_cons3_surv;
                        break;
                    case s_side_killer:
                        l = l_cons3_killer;
                        s = s_cons3_killer_txt + ", " + s_cons3_killer;
                        break;
                }
                break;
            case 4:
                switch (side) {
                    case s_side_surv:
                        l = l_cons4_surv;
                        s = s_cons4_surv_txt + ", " + s_cons4_surv;
                        break;
                    case s_side_killer:
                        l = l_cons4_killer;
                        s = s_cons4_killer_txt + ", " + s_cons4_killer;
                        break;
                }
                break;
            default:
                System.err.println("\n# Generic ERROR (n=" + n + ", side=" + side + ") => Exit");
                System.exit(0);
        }
        s = " [ " + s + " ] : ";
        if (!l.isEmpty()) {
            for (String e : l) {
                s = s + e + ", ";
            }
            s = s.trim();
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * Display all Perks and Features
     *
     * @param detail
     */
    public void showPerks(boolean detail) {
        System.out.println("# Table of Perks ( Total = " + nb_perks_all + " )");
        for (Perk p : l_perks_all) {
            System.out.println(p.show(detail));
        }
        System.out.println("");
    }

    /**
     * Set Synergy Mode
     *
     * @param b
     */
    public void setSynergyStatus(boolean b) {
        b_synergy = b;
        System.out.println("\n# Synergy Mode = " + b_synergy + "\n");
    }

    /**
     * Get Synergy Mode
     *
     * @return
     */
    public boolean getSynergyStatus() {
        return b_synergy;
    }

    /**
     * Get Synergy
     *
     * @return
     */
    public Synergy getSynergy() {
        return synergy;
    }

    /**
     * Get Status of Pool of Perks
     *
     * @return
     */
    public boolean getPerkPoolChanged() {
        return b_perkpool_changed;
    }

    /**
     * Set Status of Pool of Perks
     *
     * @param b
     */
    public final void setPerkPoolChanged(boolean b) {
        b_perkpool_changed = b;
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
        // Reset Pool of Perks 
        l_perks_pool.clear();
        l_perks_pool_default.clear();
        // Rebuild Pool of Perks 
        for (Perk perk : l_perks_all) {
            if (perk.getSide().equals(side)) {
                int value = perk.getWeight();
                // Weight Bonus for Constrained Perks
                // Just for the Pool Stage (not the Real Perk Weight)
                if (b_cons1_perks) {
                    if (l_cons1.contains(perk.getName())) {
                        value = value + weight_perk_bonus;
                    }
                }
                if (b_cons2_perks) {
                    if (l_cons2.contains(perk.getName())) {
                        value = value + weight_perk_bonus;
                    }
                }
                if (b_cons3_perks) {
                    if (l_cons3.contains(perk.getName())) {
                        value = value + weight_perk_bonus;
                    }
                }
                if (b_cons4_perks) {
                    if (l_cons4.contains(perk.getName())) {
                        value = value + weight_perk_bonus;
                    }
                }
                // Update Pool of Perk
                for (int i = 0; i < value; i++) {
                    l_perks_pool.add(perk.getName());
                }
                // Update default Pool of Perk
                l_perks_pool_default.add(perk.getName());
            }
        }
        setPerkPoolChanged(false);
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
        // Define Side
        b.setSide(side);
        // Define Character
        b.setCharacter(character);
        // Define List of current Perk
        List<String> l_perk_ok = new ArrayList<>();
        // Several Loops may be required if Constraints are enabled
        int nbloop = 1;
        while (true) {
            // Loop until either valid Build was generated or max loops reached
            if (b_verbose) {
                System.out.print("# Loop " + nbloop);
            }
            // Restore reference Weights
            setWeightRef();
            // Apply Synergy Rules with current Character
            if (b_synergy) {
                synergy.update_weights(b.getCharacter().getName(), null, this);
            }
            // Update Pool of Perks Anyway
            updatePerkPool(false);
            // Reset List of selected Perks
            l_perk_ok.clear();
            while (l_perk_ok.size() < getNbPerksBuild()) {
                // Loop until desired number of perks was reached
                // Get one random Perk from Pool
                String random_perk = "";
                if (l_perk_ok.isEmpty()) {
                    // Weight-unbiased Selection for the first looted Perk
                    int rand = (int) (l_perks_pool_default.size() * Math.random());
                    random_perk = l_perks_pool_default.get(rand);
                } else {
                    // Weight-biased Selection for other looted Perks
                    int rand = (int) (l_perks_pool.size() * Math.random());
                    random_perk = l_perks_pool.get(rand);
                }
                if (!l_perk_ok.contains(random_perk)) {
                    // New Perk found => added to the Build
                    l_perk_ok.add(random_perk);
                    if (b_verbose) {
                        System.out.print(" | " + random_perk);
                    }
                    // Apply Synergy Rules with current Perk & Update Pool of Perks if needed
                    if (b_synergy) {
                        if (synergy.update_weights(null, random_perk, this)) {
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
                if (l_cons1.contains(p)) {
                    // Perk from Set1 Found => Update Boolean
                    b_cons1_found = true;
                    break;
                }
            }
            for (String p : l_perk_ok) {
                if (l_cons2.contains(p)) {
                    // Perk from Set1 Found => Update Boolean
                    b_cons2_found = true;
                    break;
                }
            }
            for (String p : l_perk_ok) {
                if (l_cons3.contains(p)) {
                    // Perk from Set1 Found => Update Boolean
                    b_cons3_found = true;
                    break;
                }
            }
            for (String p : l_perk_ok) {
                if (l_cons4.contains(p)) {
                    // Perk from Set1 Found => Update Boolean
                    b_cons4_found = true;
                    break;
                }
            }
            // Check all Criteria to validate current Build
            boolean b_cons1_check = (!b_cons1_perks) || (b_cons1_perks && b_cons1_found);
            boolean b_cons2_check = (!b_cons2_perks) || (b_cons2_perks && b_cons2_found);
            boolean b_cons3_check = (!b_cons3_perks) || (b_cons3_perks && b_cons3_found);
            boolean b_cons4_check = (!b_cons4_perks) || (b_cons4_perks && b_cons4_found);
            if (b_cons1_check && b_cons2_check && b_cons3_check && b_cons4_check) {
                // All Criteria are Ok
                break;
            }
            nbloop++;
            if (nbloop >= maxloop) {
                // Max Loop Reached
                System.out.println("\n# WARNING: Max loop (" + maxloop + ") reached !\n");
                return b;
            }
            if (b_verbose) {
                System.out.println("");
            }
        }
        // All each validated Perk to the Build and Compute Score (Sum of Weights)
        int score = 0;
        int nb = 0;
        Collections.sort(l_perk_ok);
        for (String s : l_perk_ok) {
            nb++;
            Perk p = getPerk(s);
            if (b_verbose) {
                if (nb == 1) {
                    System.out.println("");
                }
                System.out.println(p.show(false));
            }
            b.addPerk(p);
            score = score + p.getWeight();
        }
        b.setScore(score);
        // Fill Build with generic Perks if needed
        while (b.getNbPerks() < nb_perks_ref) {
            b.addPerk(perk_generic);
        }
        // Return Build
        return b;
    }

    /**
     * Generate Random Builds
     *
     * @param prefix
     * @param b_commandline
     * @return
     */
    public ArrayList<Build> genRandomBuilds(String prefix, boolean b_commandline) {
        ArrayList<Build> l = new ArrayList<>();
        Build b = null;
        // Generate the Nb of desired Builds
        for (int i = 1; i <= getNbBuilds(); i++) {
            b = genRandomBuild(prefix + " " + i);
            l.add(b);
            if (b_verbose && b_commandline) {
                System.out.println("");
            }
            if (b_commandline) {
                System.out.println("# " + b.show(false, " | "));
            }
            if ((i < getNbBuilds()) && b_verbose && b_commandline) {
                System.out.println("");
            }
        }
        // Sort Builds
        Collections.sort(l);
        // Set Best Build
        setBestBuild(l.get(0));
        return l;
    }

    /**
     * Display Best Builds
     *
     * @param l
     * @param b_sort
     */
    public void displayBestBuilds(ArrayList<Build> l, boolean b_sort) {
        // TreeSet: No Duplicate Elements are allowed
        TreeSet<String> tree = new TreeSet<>();
        Build b = null;
        if (b_sort) {
            // Sort Build List if desired
            Collections.sort(l);
        }
        // Update Value
        if (l.size() < nb_best_builds) {
            nb_best_builds = l.size();
        }
        int i = 0;
        int nb = 0;
        // Display unique Best Builds
        while (nb < nb_best_builds) {
            b = l.get(i);
            b.setName("Build");
            String s = b.show_other();
            if (tree.add(s)) {
                // Display Unique Build
                nb++;
                System.out.println(s);
            }
            i++;
        }
        tree.clear();
    }

    /**
     * Set Weights from default Weight File
     *
     */
    public final void readWeightsDefault() {
        // Try to detect a custom Weight File in the current Directory
        String f = System.getProperty("user.dir") + File.separator + s_perk_custom;
        if (new File(f).exists()) {
            weight_file = new File(f).getAbsolutePath();
        } else {
            // Or use the default Weight File
            weight_file = s_perk;
        }
        // Read File
        readWeights(weight_file);
    }

    /**
     * Set Weights from Weight File
     *
     * @param input
     */
    public void readWeights(String input) {
        String spacer = "\t";
        l_perks_all.clear();
        l_perks_all_string.clear();
        l_perks_survivor.clear();
        l_perks_killer.clear();
        // Add generic Perk
        Perk p = new Perk();
        l_perks_all.add(p);
        l_perks_all_string.add(p.getName());
        l_perks_survivor.add(p.getName());
        l_perks_killer.add(p.getName());
        try {
            // Define the Reader
            BufferedReader br = null;
            if (new File(input).exists()) {
                System.out.println("\n# Loading custom Weights from " + input + "\n");
                br = new BufferedReader(new FileReader(new File(input)));
            } else {
                InputStream is = getClass().getResourceAsStream(input);
                System.out.println("\n# Loading default Weights from " + input + "\n");
                br = new BufferedReader(new InputStreamReader(is));
            }
            // Loop over the Reader
            String line = "";
            line = br.readLine();
            while (line != null) {
                if ((!line.startsWith("#")) && (line.length() > 0)) {
                    // Split Line according to Spacer
                    String tab[] = line.split(spacer);
                    if (tab.length == 5) {
                        // Get Data (5 Fields are required)
                        String myname = tab[0];
                        String myside = tab[1];
                        if (!((myside.equals(s_side_surv)) || (myside.equals(s_side_killer)))) {
                            System.err.println("\n# ERROR: wrong side ('" + myside + "') => Exit [ wrong line : >" + line + "< from input file ]\n");
                            System.exit(0);
                        }
                        String myicon = tab[2];
                        int myweight = Integer.parseInt(tab[3]);
                        String myparent = tab[4];
                        // Check Weight Value
                        if (myweight > weight_perk_max) {
                            myweight = weight_perk_max;
                        } else if (myweight < weight_perk_min) {
                            myweight = weight_perk_min;
                        }
                        // Check Parent
                        if (!l_char_all_string.contains(myparent)) {
                            System.err.println("\n# ERROR: wrong parent ('" + myparent + "') for perk '" + myname + "' => Exit [ wrong line : >" + line + "< from input file ]\n");
                            System.exit(0);
                        }
                        // Create Perk Object
                        p = new Perk(myname, myweight, myside, myicon, myparent, retrieveCharacter(myparent).getIconString());
                        // Add Perk to the List
                        l_perks_all.add(p);
                        // Add Perk Name to Perk List
                        l_perks_all_string.add(myname);
                        if (myside.equals(s_side_surv)) {
                            l_perks_survivor.add(myname);
                        } else {
                            l_perks_killer.add(myname);
                        }
                    } else {
                        System.err.println("\n# ERROR: corrupted weight file => Exit [ wrong line : >" + line + "< from input file ]\n");
                        System.exit(0);
                    }
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            System.err.println("\n# ERROR: issues with weight file => Exit");
            System.err.println(ex.getMessage());
            System.exit(0);
        }
        // Sort Lists
        Collections.sort(l_perks_all);
        Collections.sort(l_perks_all_string);
        //Collections.sort(l_perks_survivor);
        //Collections.sort(l_perks_killer);
        // Set Nb of Perks
        nb_perks_all = l_perks_all.size();
        // Update Nb of Active Perks
        if (side.equals(s_side_surv)) {
            nb_perks_side = l_perks_survivor.size();
        } else if (side.equals(s_side_killer)) {
            nb_perks_side = l_perks_killer.size();
        }
        // Display Perks
        if (b_verbose) {
            showPerks(false);
        }
        // Random Mode OFF
        b_random = false;
    }

    /**
     * Init Characters
     *
     */
    private void initCharacters() {
        String spacer = "\t";
        l_char_survivor.clear();
        l_char_killer.clear();
        // Add generic Characters
        Character c = new Character(s_side_surv);
        l_char_survivor.add(c);
        l_char_survivor_generic.add(c);
        l_char_all_string.add(c.getName());
        c = new Character(s_side_killer);
        l_char_killer.add(c);
        l_char_killer_generic.add(c);
        l_char_all_string.add(c.getName());
        // Try to detect Character Status File
        BufferedReader br = null;
        List<String> l_char_disabled = new ArrayList();
        String f = System.getProperty("user.dir") + File.separator + s_char_disabled;
        if (new File(f).exists()) {
            System.out.println("\n# Character Status File Detected");
            try {
                br = new BufferedReader(new FileReader(new File(f).getAbsolutePath()));
                String line = br.readLine();
                while (line != null) {
                    l_char_disabled.add(line);
                    line = br.readLine();
                }
                br.close();
            } catch (Exception ex) {
                System.err.println("\n# ERROR: issues with character status file => Exit");
                System.exit(0);
            }
        }
        // Load Characters
        try {
            String input = s_char;
            InputStream is = getClass().getResourceAsStream(input);
            System.out.println("\n# Loading Characters from " + input);
            br = new BufferedReader(new InputStreamReader(is));
            // Loop over the Reader
            String line = "";
            line = br.readLine();
            while (line != null) {
                // Split Line according to Spacer
                String tab[] = line.split(spacer);
                if (tab.length == 3) {
                    // Get Data (3 Fields are expected)
                    String myname = tab[0];
                    String myside = tab[1];
                    if (!((myside.equals(s_side_surv)) || (myside.equals(s_side_killer)))) {
                        System.err.println("\n# ERROR: wrong side ('" + myside + "') => Exit [ wrong line : >" + line + "< from input file ]\n");
                        System.exit(0);
                    }
                    String myicon = tab[2];
                    // Create Character Object
                    c = new Character(myname, myside, myicon);
                    // Add Character to related List
                    if (myside.equals(s_side_surv)) {
                        l_char_survivor.add(c);
                        // Add Character to Map
                        m_char_random_survivor.put(myname, char_val_orig);
                    } else {
                        l_char_killer.add(c);
                        // Add Character to Map
                        m_char_random_killer.put(myname, char_val_orig);
                    }
                    l_char_all_string.add(myname);
                    // Check if Character must be disabled
                    if (l_char_disabled.indexOf(myname) >= 0) {
                        c.setStatus(false);
                    }
                } else {
                    System.err.println("\n# ERROR: corrupted character file => Exit [ wrong line : >" + line + "< from input file ]\n");
                    System.exit(0);
                }
                line = br.readLine();
            }
            br.close();
            // Display Characters
            int i = 1;
            System.out.print("# All Characters: ");
            for (String s : l_char_all_string) {
                System.out.print(s + ", ");
                if (i % 10 == 0) {
                    System.out.print("\n# ");
                }
                i++;
            }
            System.out.println();
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
            for (Perk p : l_perks_all) {
                if (!p.getName().equals(Perk.GENERIC)) {
                    bw.write(p.getName() + spacer + p.getSide() + spacer + p.getIconString() + spacer + p.getWeight() + spacer + p.getParent() + "\n");
                }
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
        String type = "";
        try {
            // Define the Reader
            BufferedReader br = null;
            // Try to detect a custom Perk Synergy File in the current Directory
            String f = System.getProperty("user.dir") + File.separator + s_cons_custom;
            if (new File(f).exists()) {
                System.out.println("\n# Loading custom Perk Constraints from " + f);
                br = new BufferedReader(new FileReader(new File(f).getAbsolutePath()));
            } else {
                System.out.println("\n# Loading default Perk Constraints from " + s_cons);
                InputStream is = getClass().getResourceAsStream(s_cons);
                br = new BufferedReader(new InputStreamReader(is));
            }
            line = br.readLine();
            while (line != null) {
                if (line.split("\t").length == 3) {
                    type = line.split("\t")[1];
                    perk = line.split("\t")[2];
                    // Check Perk
                    if (l_perks_all_string.contains(perk)) {
                        // Add Perk to reference List
                        if (line.startsWith(s_cons1_surv)) {
                            l_cons1_surv.add(perk);
                            s_cons1_surv_txt = type;
                        } else if (line.startsWith(s_cons2_surv)) {
                            l_cons2_surv.add(perk);
                            s_cons2_surv_txt = type;
                        } else if (line.startsWith(s_cons3_surv)) {
                            l_cons3_surv.add(perk);
                            s_cons3_surv_txt = type;
                        } else if (line.startsWith(s_cons4_surv)) {
                            l_cons4_surv.add(perk);
                            s_cons4_surv_txt = type;
                        } else if (line.startsWith(s_cons1_killer)) {
                            l_cons1_killer.add(perk);
                            s_cons1_killer_txt = type;
                        } else if (line.startsWith(s_cons2_killer)) {
                            l_cons2_killer.add(perk);
                            s_cons2_killer_txt = type;
                        } else if (line.startsWith(s_cons3_killer)) {
                            l_cons3_killer.add(perk);
                            s_cons3_killer_txt = type;
                        } else if (line.startsWith(s_cons4_killer)) {
                            l_cons4_killer.add(perk);
                            s_cons4_killer_txt = type;
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
        System.out.println("\n# Perk Constraints on Survivor Side:");
        System.out.println("# - Set of Perks 1 = " + getConstraints(1, s_side_surv));
        System.out.println("# - Set of Perks 2 = " + getConstraints(2, s_side_surv));
        System.out.println("# - Set of Perks 3 = " + getConstraints(3, s_side_surv));
        System.out.println("# - Set of Perks 4 = " + getConstraints(4, s_side_surv) + "\n");
        System.out.println("# Perk Constraints on Killer Side:");
        System.out.println("# - Set of Perks 1 = " + getConstraints(1, s_side_killer));
        System.out.println("# - Set of Perks 2 = " + getConstraints(2, s_side_killer));
        System.out.println("# - Set of Perks 3 = " + getConstraints(3, s_side_killer));
        System.out.println("# - Set of Perks 4 = " + getConstraints(4, s_side_killer) + "\n");
    }

    /**
     * Display Parameters
     *
     * @param detail
     */
    public final void showParams(boolean detail) {
        System.out.println("\n" + MYSPACER + " Input Parameters " + MYSPACER + "\n");
        if (detail) {
            System.out.println("# Min/Max Weight Values = " + weight_perk_min + "/" + weight_perk_max);
            System.out.println("# Total Perks = " + nb_perks_all + " ( Active Perks = " + nb_perks_side + " )");
        }
        System.out.println("# Active Side = " + side);
        System.out.println("# Active Character = " + character.getName());
        System.out.println("# Nb of desired Builds = " + nb_builds);
        System.out.println("# Nb of Perks in a Build = " + nb_perks_build);
        System.out.println("# Perk from Set 1 is Required = " + b_cons1_perks);
        System.out.println("# Perk from Set 2 is Required = " + b_cons2_perks);
        System.out.println("# Perk from Set 3 is Required = " + b_cons3_perks);
        System.out.println("# Perk from Set 4 is Required = " + b_cons4_perks);
        System.out.println("# Synergy Mode = " + b_synergy);
        if (detail) {
            System.out.println("# Verbose Mode = " + b_verbose);
        }
        System.out.println("");
    }

    /**
     * Display Help and Quit
     *
     */
    public void displayHelp() {
        System.out.println("\n" + MYSPACER + " Available Options in Smart Random Build Generator " + VERSION + " " + MYSPACER + "\n");
        System.out.println("# -side : define the active side 'Survivor' / 'Killer' / 'Random' (before defining character)");
        System.out.println("# -char : define the desired character (after defining side)");
        System.out.println("# -perk : define the number of perks in a build");
        System.out.println("# -cons1 : enable constraints for set of perks 1 (at least one perk from set is required in the build)");
        System.out.println("# -cons2 : enable constraints for set of perks 2 (at least one perk from set is required in the build)");
        System.out.println("# -cons3 : enable constraints for set of perks 3 (at least one perk from set is required in the build)");
        System.out.println("# -cons4 : enable constraints for set of perks 4 (at least one perk from set is required in the build)");
        System.out.println("# -build : define the number of desired builds");
        System.out.println("# -best : define the top build list to display");
        System.out.println("# -nosyn : disable synergy rules");
        System.out.println("# -conf : load custom weight distribution file (predefined weight for each perk)");
        System.out.println("# -eval : reevaluate builds from reference database using synergy-based rules and quit (exclusive process)");
        System.out.println("# -v : enable verbose mode");
        System.out.println("# -h : print this help and quit\n");
        System.exit(0);
    }

    /**
     * Random Selection of Active Side
     *
     * @return
     */
    public String choseSideRandom() {
        System.out.print("# Random Side Selection");
        // Get random Number
        double p = Math.random();
        // Add Bias toward the other Side
        double offset = 0.20;
        if (side.equals(s_side_surv)) {
            p = p - offset;
        } else if (side.equals(s_side_killer)) {
            p = p + offset;
        }
        // Select Side according to Random Value
        String randside = "";
        if (p > 0.5) {
            randside = s_side_surv;
        } else {
            randside = s_side_killer;
        }
        System.out.println(" = " + randside + "\n");
        return randside;
    }

    /**
     * Check for Update from GitHub
     *
     * @return
     */
    public boolean checkUpdate() {
        System.out.print("\n# Checking Update from remote GitHub Repository\n");
        boolean update_new = false;
        // Dummy Version
        double gitversion = -1;
        // Retrieve last Version from SRBG
        String val = Tools.extractJSONdata(GIT_URL_API, "tag_name");
        if (val == null) {
            System.err.println("\n# ERROR while checking Update\n");
            return update_new;
        } else {
            try {
                // Convert Value
                gitversion = Double.parseDouble(val);
            } catch (NumberFormatException ex) {
                System.err.println("\n# ERROR while checking Update\n");
                return update_new;
            }
        }
        // New Version Found?
        if (gitversion > VERSION) {
            update_new = true;
            System.out.println("# Remote Version = " + gitversion + " | Local Version = " + VERSION + "\n# An Update is available from " + GIT_URL);
            System.out.println("# New Features from SRBG " + gitversion + ":\n" + Tools.extractJSONdata(GIT_URL_API, "body").replaceAll("\n-", "\n# -").replaceAll("^-", "# -") + "\n");
        } else if (gitversion > 0) {
            System.out.println("# Current Version (" + VERSION + ") is already the last One\n");
        }
        return update_new;
    }

    /**
     * Main Method for Command-line Use
     *
     * @param args
     */
    public static void main(String args[]) {

        // Build SRBG Object
        SRBG srbg = new SRBG(false);

        // Check Args
        if (args.length == 0) {
            srbg.displayHelp();
        }

        // Check Update
        srbg.checkUpdate();

        // Reevaluation Mode
        boolean b_reeval = false;

        // Process User-defined Arguments
        System.out.println(srbg.MYSPACER + " Parsing Arguments from User " + srbg.MYSPACER + "\n");
        String val = "";
        int valn = 0;
        int argn = args.length;
        for (int i = 0; i < argn; i++) {
            if (args[i].equals("-conf")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        val = args[i + 1];
                        srbg.readWeights(val);
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
                srbg.setSynergyStatus(false);
            } else if (args[i].equals("-char")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        val = args[i + 1];
                        srbg.setCharacter(val);
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
                            srbg.setNbBuilds(Integer.parseInt(args[i + 1]));
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
            } else if (args[i].equals("-best")) {
                if ((i + 1) < argn) {
                    if (!args[i + 1].startsWith("-")) {
                        try {
                            srbg.setNbBestBuilds(Integer.parseInt(args[i + 1]));
                        } catch (Exception ex) {
                            System.err.println("\n# ERROR: The '-best' option requires an integer value\n");
                            System.exit(0);
                        }
                    } else {
                        System.err.println("\n# ERROR: The '-best' option requires an argument\n");
                        System.exit(0);
                    }
                } else {
                    System.err.println("\n# ERROR: The '-best' option requires an argument\n");
                    System.exit(0);
                }
            } else if (args[i].equals("-v")) {
                srbg.b_verbose = true;
            } else if (args[i].equals("-eval")) {
                b_reeval = true;
                System.out.println("\n# Reevaluation of Builds = " + b_reeval + " (exclusive process)");
            } else if (args[i].equals("-h")) {
                srbg.displayHelp();
            } else if (args[i].startsWith("-")) {
                System.err.println("\n# ERROR: Wrong option '" + args[i] + "'\n");
                System.exit(0);
            }
        }

        // Exclusive Build Evaluation
        if (b_reeval) {
            System.out.println("\n" + srbg.MYSPACER + " Reevaluation of Builds from Database using Synergy-based Rules " + srbg.MYSPACER + "\n");
            System.out.println("# Minimum Weight allowed after Synergy = " + srbg.syn_min_weight);
            System.out.println("# Score Penalty for identified Builds = " + srbg.syn_penalty);
            // Force Synergy Mode
            srbg.b_synergy = true;
            // Load Builds from Reference Database
            TableModelBuild btm = new TableModelBuild(srbg);
            System.out.println("");
            // Rescore Builds from DB, then Exit
            btm.rescoreDatabase();
            System.exit(0);
        }

        // Display loaded Parameters
        srbg.showParams(true);

        // Generate Random Builds
        System.out.println(srbg.MYSPACER + " Generating " + srbg.getNbBuilds() + " random Builds with " + srbg.getNbPerksBuild() + " Perks in each Build " + srbg.MYSPACER + "\n");
        ArrayList<Build> l = srbg.genRandomBuilds("Build", true);

        // Display Top unique Builds
        System.out.println("\n" + srbg.MYSPACER + " Top " + srbg.getNbBestBuilds() + " unique Builds over " + l.size() + " Generated Builds " + srbg.MYSPACER + "\n");
        srbg.displayBestBuilds(l, false);

        // Clean
        l.clear();
        System.out.println("");

    }

}
