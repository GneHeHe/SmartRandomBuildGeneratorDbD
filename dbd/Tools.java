package dbd;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * Tools (Generic Class with static Methods)
 *
 * @author GneHeHe (2018)
 *
 */
public class Tools {

    /**
     * Return Scaled Picture
     *
     * @param filename
     * @param width
     * @param height
     * @return
     */
    public static Image resizePicture(String filename, int width, int height) {
        Image dimg = null;
        BufferedImage img = null;
        try {
            // Read Picture from Filename or InputStream
            if (new File(filename).exists()) {
                img = ImageIO.read(new File(filename));
            } else {
                InputStream is = Tools.class.getResourceAsStream(filename);
                img = ImageIO.read(is);
            }
            // Get Scaled Picture
            dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (Exception ex) {
            System.err.println("\n# ERROR while resizing " + filename);
            System.err.println(ex.getMessage());
        }
        return dimg;
    }

    /**
     * Return Scaled Picture
     *
     * @param filename
     * @param percent
     * @return
     */
    public static Image resizePicture(String filename, int percent) {
        Image dimg = null;
        BufferedImage img = null;
        try {
            // Read Picture from Filename or InputStream
            if (new File(filename).exists()) {
                img = ImageIO.read(new File(filename));
            } else {
                InputStream is = Tools.class.getResourceAsStream(filename);
                img = ImageIO.read(is);
            }
            // Get Scaled Picture
            dimg = img.getScaledInstance((int) (img.getWidth() * percent / 100), (int) (img.getHeight() * percent / 100), Image.SCALE_SMOOTH);
        } catch (Exception ex) {
            System.err.println("\n# ERROR while resizing " + filename);
            System.err.println(ex.getMessage());
        }
        return dimg;
    }

    /**
     * Get Last Version from Github
     *
     * @param user
     * @param proj
     * @return
     */
    public static double getLastVersionGitHub(String user, String proj) {
        double version_git = -1;
        double val = -1;
        try {
            // Create URL to Project (JSON Data)
            URL url = new URL("https://api.github.com/repos/" + user + "/" + proj + "/releases");
            System.out.println(url.toString());
            // Get remote Data as InputStream
            InputStream is;
            is = url.openStream();
            // Retrieve JSON Data
            JsonElement jsdata = null;
            jsdata = new JsonParser().parse(new InputStreamReader(is));
            // Close InputStream
            is.close();
            // Loop over JSON Data
            if (jsdata != null) {
                JsonArray arr = jsdata.getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    // Get current JSON Object
                    JsonObject obj = arr.get(i).getAsJsonObject();
                    // Get Last Version of Tool on GitHub
                    val = Double.parseDouble(obj.get("tag_name").getAsString());
                    if (val > version_git) {
                        version_git = val;
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("\n# ERROR while checking Update");
            System.err.println(ex.getMessage() + "\n");
        }
        return version_git;
    }

    /**
     * Any Duplicate Elements?
     *
     * @param l
     * @return
     */
    public static boolean hasDuplicateElements(List l) {
        // Sets only contain non-duplicate elements
        Set single = new HashSet();
        for (Object s : l) {
            if (!single.add(s)) {
                // Set.add() returns false if duplicate element
                return true;
            }
        }
        // No duplicate at this Stage
        return false;
    }

    /**
     * Duplicate Lists ?
     *
     * @param l1
     * @param l2
     * @param sort
     * @return
     */
    public static boolean isDuplicate(ArrayList l1, ArrayList l2, boolean sort) {
        // Check if same Size
        if (l1.size() != l2.size()) {
            return false;
        }
        // Sort Lists if desired
        if (sort) {
            Collections.sort(l1);
            Collections.sort(l2);
        }
        // Check Elements (1-by-1)
        for (int i = 0; i < l1.size(); i++) {
            if (!l1.get(i).equals(l2.get(i))) {
                return false;
            }
        }
        // No difference at this Stage
        return true;
    }

    /**
     * Display Message in a Window
     *
     * @param msg string to display
     * @param title title of window
     * @param type type of alert
     */
    public static void getAlert(String msg, String title, int type) {
        JOptionPane.showMessageDialog(null, msg, title, type);
    }

}
