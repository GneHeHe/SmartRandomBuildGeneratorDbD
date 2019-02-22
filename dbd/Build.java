package dbd;

import java.util.ArrayList;

/**
 *
 * Build
 *
 * @author GneHeHe (2018)
 * 
 */
public final class Build {

    // Name of Build
    private String name;
    // Side of Build
    private String side;
    // Character of Build
    private Character character;
    // List of Perks
    private ArrayList<Perk> l_perks;

    /**
     * Default Constructor
     *
     */
    public Build() {
        this.name = "";
        this.side = "";
        this.character = null;
        this.l_perks = new ArrayList<>();
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
     * @param name
     */
    public void setCharacter(Character name) {
        this.character = name;
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
     * Get Number of Perks
     *
     * @return
     */
    public int getNbPerks() {
        return this.l_perks.size();
    }

    /**
     * Display Build as String
     *
     * @param showDetail
     * @param spacer
     * @return
     */
    public String show(boolean showDetail, String spacer) {
        String s = "";
        if (showDetail) {
            s = "Name = '" + this.name + "'" + spacer + "| Side = '" + this.side + "'" + spacer + "| Character = '" + this.character + "'" + spacer;
        } else {
            s = this.name + spacer;
        }
        int nb = 1;
        for (Perk p : this.l_perks) {
            if (showDetail) {
                s = s + "| Perk " + nb + " = '" + p.getName() + "'";
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
        ArrayList<String> l1 = new ArrayList<>();
        l1.add(this.getCharacter().getName());
        l1.add(this.getPerk(1).getName());
        l1.add(this.getPerk(2).getName());
        l1.add(this.getPerk(3).getName());
        l1.add(this.getPerk(4).getName());
        ArrayList<String> l2 = new ArrayList<>();
        l2.add(b.getCharacter().getName());
        l2.add(b.getPerk(1).getName());
        l2.add(b.getPerk(2).getName());
        l2.add(b.getPerk(3).getName());
        l2.add(b.getPerk(4).getName());
        return Tools.isDuplicate(l1, l2, true);
    }

}
