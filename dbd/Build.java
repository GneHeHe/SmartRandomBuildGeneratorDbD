package dbd;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Build
 *
 * @author GneHeHe (2019)
 *
 */
public class Build {

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

    /**
     * Default Constructor
     *
     */
    public Build() {
        this.name = "";
        this.side = "";
        this.character = null;
        this.l_perks = new ArrayList<>();
        this.score = 0;
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
        this.character = c;
        this.l_perks = l;
        this.score = 0;
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
        this.character = c;
        this.l_perks = l;
        this.score = score;
    }

    /**
     * Get Build Name
     *
     * @return
     */
    public String getName() {
        return this.name;
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
        return this.side;
    }

    /**
     * Set Side
     *
     * @param side
     */
    public void setSide(String side) {
        // Only 2 Sides are Available
        if (side.equals("Killer") || side.equals("Survivor")) {
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
        return this.character;
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
        return this.score;
    }

    /**
     * Set Score
     *
     * @param s
     */
    public void setScore(int s) {
        this.score = s;
    }

    /**
     * Add Perk
     *
     * @param p
     */
    public void addPerk(Perk p) {
        this.l_perks.add(p);
    }

    /**
     * Get Perks
     *
     * @return
     */
    public ArrayList<Perk> getPerks() {
        return this.l_perks;
    }

    /**
     * Get i-th Perk
     *
     * @param i
     * @return
     */
    public Perk getPerk(int i) {
        int nb = 1;
        for (Perk p : this.l_perks) {
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
        return this.l_perks.size();
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
            s = "Name = '" + this.name + "'" + spacer + "| Score = " + this.score + " | Side = '" + this.side + "'" + spacer + "| Character = '" + this.character + "'" + spacer;
        } else {
            s = this.name + spacer + this.score + spacer + this.side + spacer + this.character + spacer;
        }
        int nb = 1;
        for (Perk p : this.l_perks) {
            if (showDetails) {
                s = s + "| Perk" + nb + " = '" + p.getName() + "'";
            } else {
                s = s + p.getName();
            }
            if (nb < this.getNbPerks()) {
                s = s + spacer;
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
        return this.name;
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
        l1.add(this.getSide());
        l1.add(this.getCharacter().getName());
        l1.add(this.getPerk(1).getName());
        l1.add(this.getPerk(2).getName());
        l1.add(this.getPerk(3).getName());
        l1.add(this.getPerk(4).getName());
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
     * Get the Build with Highest Score
     *
     * @param l_builds
     * @return
     */
    public static Build getBestBuild(List<Build> l_builds) {
        // Generic Build (Score=0)
        Build b = new Build();
        // Find Best Build
        for (Build current : l_builds) {
            if (current.getScore() > b.getScore()) {
                b = current;
            }
        }
        return b;
    }

}
