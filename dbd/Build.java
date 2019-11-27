package dbd;

import java.util.ArrayList;

/**
 *
 * Build
 *
 * @author GneHeHe (2019)
 *
 */
public class Build implements Comparable<Build> {

    // Name
    private String name;
    // Side
    private String side;
    // Character
    private Character character;
    // List of Perks
    private ArrayList<Perk> l_perks;
    // Score
    private int score;
    // Strings
    public final static String SURVIVOR = "Survivor";
    public final static String KILLER = "Killer";

    /**
     * Default Constructor
     *
     */
    public Build() {
        name = "";
        side = "";
        character = null;
        l_perks = new ArrayList<>();
        score = 0;
    }

    /**
     * Constructor
     *
     * @param name
     * @param side
     * @param c
     * @param l
     */
    public Build(String name, String side, Character c, ArrayList<Perk> l) {
        this.name = name;
        this.side = side;
        character = c;
        l_perks = l;
        score = 0;
    }

    /**
     * Constructor
     *
     * @param name
     * @param side
     * @param c
     * @param l
     * @param score
     */
    public Build(String name, String side, Character c, ArrayList<Perk> l, int score) {
        this.name = name;
        this.side = side;
        character = c;
        l_perks = l;
        this.score = score;
    }

    /**
     * Get Build Name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set Build Name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Side
     *
     * @return
     */
    public String getSide() {
        return side;
    }

    /**
     * Set Side
     *
     * @param side
     */
    public void setSide(String side) {
        // Only 2 Sides are Available
        if (side.equals(KILLER) || side.equals(SURVIVOR)) {
            this.side = side;
        } else {
            System.err.println("# ERROR: This side " + side + " is wrong (expected value = 'Killer' or 'Survivor'");
            System.exit(0);
        }
    }

    /**
     * Get Character
     *
     * @return
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Set Character
     *
     * @param character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Get Score
     *
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * Set Score
     *
     * @param s
     */
    public void setScore(int s) {
        score = s;
    }

    /**
     * Add Perk
     *
     * @param p
     */
    public void addPerk(Perk p) {
        l_perks.add(p);
    }

    /**
     * Get Perks
     *
     * @return
     */
    public ArrayList<Perk> getPerks() {
        return l_perks;
    }

    /**
     * Get i-th Perk
     *
     * @param i
     * @return
     */
    public Perk getPerk(int i) {
        int nb = 1;
        for (Perk p : l_perks) {
            if (nb == i) {
                return p;
            }
            nb++;
        }
        return null;
    }

    /**
     * Get Nb of Perks
     *
     * @return
     */
    public int getNbPerks() {
        return l_perks.size();
    }

    /**
     * Display Build as String
     *
     * @param showDetails
     * @param spacer
     * @return
     */
    public String show(boolean showDetails, String spacer) {
        String s = "";
        if (showDetails) {
            s = "Name = '" + name + "'" + spacer + "| Score = " + score + " | Side = '" + side + "'" + spacer + "| Character = '" + character + "'" + spacer;
        } else {
            s = name + spacer + score + spacer + side + spacer + character + spacer;
        }
        int nb = 1;
        for (Perk p : l_perks) {
            if (showDetails) {
                s = s + "| Perk" + nb + " = '" + p.getName() + "'";
            } else {
                s = s + p.getName();
            }
            if (nb < getNbPerks()) {
                s = s + spacer;
            }
            nb++;
        }
        return s;
    }

    /**
     * Display Build as String
     *
     * @return
     */
    public String show_other() {
        String s = score + " | " + name + "\t" + side + "\t" + character + "\t";
        int nb = 1;
        for (Perk p : l_perks) {
            s = s + p.getName();
            if (nb < getNbPerks()) {
                s = s + "\t";
            }
            nb++;
        }
        return s;
    }

    /**
     * toString() Method
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Check if two Builds are Identical
     *
     * @param b
     * @return
     */
    public boolean isDuplicate(Build b) {
        // Current Build as List
        ArrayList<String> l1 = new ArrayList<>();
        l1.add(getSide());
        l1.add(getCharacter().getName());
        l1.add(getPerk(1).getName());
        l1.add(getPerk(2).getName());
        l1.add(getPerk(3).getName());
        l1.add(getPerk(4).getName());
        // Argument Build as List
        ArrayList<String> l2 = new ArrayList<>();
        l2.add(b.getSide());
        l2.add(b.getCharacter().getName());
        l2.add(b.getPerk(1).getName());
        l2.add(b.getPerk(2).getName());
        l2.add(b.getPerk(3).getName());
        l2.add(b.getPerk(4).getName());
        // Are they Identical?
        return Tools.isDuplicate(l1, l2, true);
    }

    /**
     * Compare two Builds
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Build o) {
        if (score > o.score) {
            return -1;
        } else if (score < o.score) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Rescore Build using Synergy-based Rules
     *
     * @param srbg
     * @return
     */
    public int rescoreBuild(SmartRandBuildGen srbg) {
        // Get Synergy Object
        Synergy syn = srbg.getSynergy();
        // Reevaluated Score
        int score_syn = 0;
        // Restore reference Weights
        srbg.setWeightRef();
        if (srbg.b_verbose) {
            System.out.println("# Rescoring Build '" + name + "'\n");
            // Display original Perk Weights
            for (Perk p : getPerks()) {
                System.out.println(p.show(false));
            }
        }
        // Synergy Rules with current Character
        syn.update_weights(getCharacter().getName(), null, srbg);
        // Synergy Rules with all Perks
        for (Perk p : getPerks()) {
            syn.update_weights(null, p.getName(), srbg);
        }
        if (srbg.b_verbose) {
            System.out.println("");
        }
        // Display updated Perk Weights
        for (Perk p : getPerks()) {
            if (srbg.b_verbose) {
                System.out.println(p.show(false));
            }
            score_syn = score_syn + p.getWeight();
        }
        if (srbg.b_verbose) {
            System.out.println("");
        }
        // Restore reference Weights
        srbg.setWeightRef();
        // Return reevaluated Score
        return score_syn;
    }

}
