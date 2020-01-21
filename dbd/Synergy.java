package dbd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * Synergy
 *
 * @author GneHeHe (2019)
 *
 */
public class Synergy {

    // Synergy Maps
    private TreeMap<String, ArrayList> m_synergy_chars;
    private TreeMap<String, ArrayList> m_synergy_perks;
    // List of all Perks
    private ArrayList<String> l_perks;
    // List of all Characters
    private ArrayList<String> l_chars;
    // Synergy Filenames
    public final String s_perks = "syn_perks.txt";
    public final String s_perks_custom = "syn_perks_custom.txt";
    public final String s_chars = "syn_chars.txt";
    public final String s_chars_custom = "syn_chars_custom.txt";

    /**
     * Constructor
     *
     * @param l_perks
     * @param l_chars
     * @param verbose
     */
    public Synergy(ArrayList<String> l_perks, ArrayList<String> l_chars, boolean verbose) {
        m_synergy_chars = new TreeMap<>();
        m_synergy_perks = new TreeMap<>();
        this.l_perks = l_perks;
        this.l_chars = l_chars;
        // Read Synergy Rules
        readSynergyRulesChars(verbose);
        if (verbose) {
            System.out.println("");
        }
        readSynergyRulesPerks(verbose);
    }

    /**
     * Read Synergy Rules Again
     *
     * @param verbose
     */
    public void readRulesAgain(boolean verbose) {
        // Clear Rules
        m_synergy_chars.clear();
        m_synergy_perks.clear();
        // Read again Synergy Rules
        readSynergyRulesChars(verbose);
        readSynergyRulesPerks(verbose);
        System.out.println("");
    }

    /**
     * Read Synergy Rules for Characters
     *
     */
    private void readSynergyRulesChars(boolean verbose) {
        String spacer = "\t";
        int nb = 0;
        try {
            // Define the Reader
            BufferedReader br = null;
            // Try to detect a custom Character Synergy File in the current Directory
            String f = System.getProperty("user.dir") + File.separator + s_chars_custom;
            if (new File(f).exists()) {
                System.out.print("# Loading custom Character Synergy Rules from " + f + ": ");
                br = new BufferedReader(new FileReader(new File(f).getAbsolutePath()));
            } else {
                InputStream is = getClass().getResourceAsStream("data/" + s_chars);
                System.out.print("# Loading default Character Synergy Rules from data/" + s_chars + ": ");
                br = new BufferedReader(new InputStreamReader(is));
            }
            // Loop over the Reader
            ArrayList l_current;
            ArrayList l_val;
            String line = "";
            line = br.readLine();
            while (line != null) {
                if ((!line.startsWith("#")) && (line.length() > 0)) {
                    // Split Line according to Spacer
                    String tab[] = line.split(spacer);
                    if (tab.length == 3) {
                        // Get Data (3 Fields are expected)
                        // Get Character
                        String chars = tab[0];
                        if (!l_chars.contains(chars)) {
                            System.err.println("\n# ERROR: Corrupted Character Synergy File => Character " + chars + " does not exist !\n");
                            System.exit(0);
                        }
                        // Get Perk
                        String synperk = tab[1];
                        if (!l_perks.contains(synperk)) {
                            System.err.println("\n# ERROR: Corrupted Character Synergy File => Perk " + synperk + " does not exist !\n");
                            System.exit(0);
                        }
                        // Get Synergy Weight
                        Integer synweight = Integer.parseInt(tab[2]);
                        // Add Synergy Rules for the Character (Update or Create new Entry)                        
                        l_val = new ArrayList();
                        l_val.add(synperk);
                        l_val.add(synweight);
                        if (m_synergy_chars.containsKey(chars)) {
                            l_current = m_synergy_chars.get(chars);
                            l_current.add(l_val);
                        } else {
                            l_current = new ArrayList();
                            l_current.add(l_val);
                            m_synergy_chars.put(chars, l_current);
                        }
                        nb++;
                    } else {
                        System.err.println("\n# ERROR: Corrupted Character Synergy File => Exit [ wrong line : >" + line + "< from input file ]\n");
                        System.exit(0);
                    }
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            System.err.println("\n# ERROR: Issues with Character Synergy File => Exit");
            System.err.println(ex.getMessage());
            System.exit(0);
        }
        System.out.println(nb + " Synergy Rules were loaded over " + m_synergy_chars.size() + " Characters");
        if (verbose) {
            // Display Synergy Map
            showSynergy(m_synergy_chars, "Character");
        }
    }

    /**
     * Read Synergy Rules for Perks
     *
     */
    private void readSynergyRulesPerks(boolean verbose) {
        String spacer = "\t";
        int nb = 0;
        try {
            // Define the Reader
            BufferedReader br = null;
            // Try to detect a custom Perk Synergy File in the current Directory
            String f = System.getProperty("user.dir") + File.separator + s_perks_custom;
            if (new File(f).exists()) {
                System.out.print("# Loading custom Perk Synergy Rules from " + f + ": ");
                br = new BufferedReader(new FileReader(new File(f).getAbsolutePath()));
            } else {
                InputStream is = getClass().getResourceAsStream("data/" + s_perks);
                System.out.print("# Loading default Perk Synergy Rules from data/" + s_perks + ": ");
                br = new BufferedReader(new InputStreamReader(is));
            }
            // Loop over the Reader
            ArrayList l_current;
            ArrayList l_val;
            String line = "";
            line = br.readLine();
            while (line != null) {
                if ((!line.startsWith("#")) && (line.length() > 0)) {
                    // Split Line according to Spacer
                    String tab[] = line.split(spacer);
                    if (tab.length == 3) {
                        // Get Data (3 Fields are expected)
                        // Get 1st Perk
                        String synperk1 = tab[0];
                        if (!l_perks.contains(synperk1)) {
                            System.err.println("\n# ERROR: Corrupted Perk Synergy File => Perk " + synperk1 + " does not exist !\n");
                            System.exit(0);
                        }
                        // Get 2nd Perk
                        String synperk2 = tab[1];
                        if (!l_perks.contains(synperk2)) {
                            System.err.println("\n# ERROR: Corrupted Perk Synergy File => Perk " + synperk2 + " does not exist !\n");
                            System.exit(0);
                        }
                        // Get Synergy Weight
                        Integer synweight = Integer.parseInt(tab[2]);
                        // Add Synergy Rules for Perk1->Perk2 (Update or Create new Entry)                        
                        l_val = new ArrayList();
                        l_val.add(synperk2);
                        l_val.add(synweight);
                        if (m_synergy_perks.containsKey(synperk1)) {
                            l_current = m_synergy_perks.get(synperk1);
                            l_current.add(l_val);
                        } else {
                            l_current = new ArrayList();
                            l_current.add(l_val);
                            m_synergy_perks.put(synperk1, l_current);
                        }
                        // Add Synergy Rules for Perk2->Perk1 (Update or Create new Entry)
                        l_val = new ArrayList();
                        l_val.add(synperk1);
                        l_val.add(synweight);
                        if (m_synergy_perks.containsKey(synperk2)) {
                            l_current = m_synergy_perks.get(synperk2);
                            l_current.add(l_val);
                        } else {
                            l_current = new ArrayList();
                            l_current.add(l_val);
                            m_synergy_perks.put(synperk2, l_current);
                        }
                        nb = nb + 2;
                    } else {
                        System.err.println("\n# ERROR: Corrupted Perk Synergy File => Exit [ wrong line : >" + line + "< from input file ]\n");
                        System.exit(0);
                    }
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            System.err.println("\n# ERROR: Issues with Perk Synergy File => Exit");
            System.err.println(ex.getMessage());
            System.exit(0);
        }
        System.out.println((nb / 2) + " Synergy Rules were loaded over " + m_synergy_perks.size() + " Perks");
        if (verbose) {
            // Display Synergy Map
            showSynergy(m_synergy_perks, "Perk");
        }
    }

    /**
     * Display loaded Synergy Rules
     *
     */
    private void showSynergy(TreeMap<String, ArrayList> syn, String type) {
        for (Map.Entry<String, ArrayList> entry : syn.entrySet()) {
            String key = entry.getKey();
            ArrayList<ArrayList> value = entry.getValue();
            for (ArrayList perk_weight : value) {
                System.out.println("# - Synergy Rule between " + type + " '" + key + "' and Perk '" + perk_weight.get(0) + "' with Weight = " + perk_weight.get(1));
            }
        }
    }

    /**
     * Update Perk Weights according to Synergy Rules
     *
     * @param refchar
     * @param refperk
     * @param srbg
     * @return
     */
    public boolean update_weights(String refchar, String refperk, SRBG srbg) {
        // Monitor Modifications
        boolean update = false;
        // Update Perk Weights according to Characters Synergy Rules
        if (refchar != null) {
            if (m_synergy_chars.containsKey(refchar)) {
                ArrayList<ArrayList> value = m_synergy_chars.get(refchar);
                // Loop over Rules for this Character
                for (ArrayList perk_weight : value) {
                    String perk = perk_weight.get(0).toString();
                    int weight = Integer.parseInt(perk_weight.get(1).toString());
                    // Get concerned Perk
                    Perk p = srbg.getPerk(perk);
                    if (p != null) {
                        // Update Weight (max value is strict, min value can be negative here)
                        int weight_orig = p.getWeight();
                        int weight_tmp = p.getWeight() + weight;
                        /*
                         if (weight_tmp < srbg.weight_perk_min) {
                         p.setWeight(Math.max(srbg.weight_perk_min, weight_tmp), false);
                         } else*/
                        if (weight_tmp > srbg.weight_perk_max) {
                            p.setWeight(Math.min(srbg.weight_perk_max, weight_tmp), false);
                        } else {
                            p.setWeight(weight_tmp, false);
                        }
                        if (srbg.b_verbose) {
                            System.out.print("\n# Synergy with Character '" + refchar + "' => Updated Weight for Perk '" + p.getName() + "': from " + weight_orig + " to " + p.getWeight());
                        }
                        // Modifications were encountered
                        update = true;
                    } else {
                        System.err.println("\n# ERROR: Wrong Perk Name '" + perk + "' => Exit\n");
                        System.exit(0);
                    }
                }
            }
        }
        // Update Perk Weights according to Perk Synergy Rules
        if (refperk != null) {
            if (m_synergy_perks.containsKey(refperk)) {
                ArrayList<ArrayList> value = m_synergy_perks.get(refperk);
                // Loop over Rules for this Perk
                for (ArrayList perk_weight : value) {
                    String perk = perk_weight.get(0).toString();
                    int weight = Integer.parseInt(perk_weight.get(1).toString());
                    // Get concerned Perk
                    Perk p = srbg.getPerk(perk);
                    if (p != null) {
                        // Update Weight (max value is strict, min value can be negative here)
                        int weight_orig = p.getWeight();
                        int weight_tmp = p.getWeight() + weight;
                        /*
                         if (weight_tmp < srbg.weight_perk_min) {
                         p.setWeight(Math.max(srbg.weight_perk_min, weight_tmp), false);
                         } else*/
                        if (weight_tmp > srbg.weight_perk_max) {
                            p.setWeight(Math.min(srbg.weight_perk_max, weight_tmp), false);
                        } else {
                            p.setWeight(weight_tmp, false);
                        }
                        if (srbg.b_verbose) {
                            System.out.print("\n# Synergy with Perk '" + refperk + "' => Updated Weight for Perk '" + p.getName() + "': from " + weight_orig + " to " + p.getWeight());
                        }
                        // Modifications were encountered
                        update = true;
                    } else {
                        System.err.println("\n# ERROR: Wrong Perk Name '" + perk + "' => Exit\n");
                        System.exit(0);
                    }
                }
            }
        }
        if (update && srbg.b_verbose) {
            System.out.println("");
        }
        return update;
    }

}
