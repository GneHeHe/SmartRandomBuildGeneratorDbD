package dbd;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
     * Save Swing Component as Picture
     *
     * @param myComponent
     * @param filename
     * @param format
     */
    public static void saveComponentAsImage(Component myComponent, String filename, String format) {
        // Get Dimensions
        Dimension size = myComponent.getSize();
        // Test Dimensions
        if ((size.width > 0) && (size.height > 0)) {
            // Define BufferedImage
            BufferedImage picture = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
            // Paint Component in BufferedImage
            myComponent.paint(picture.getGraphics());
            try {
                // Write BufferedImage as Picture
                ImageIO.write(picture, format, new File(filename));
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            System.err.println("\n# ERROR: Picture has either 0-height or 0-width");
        }
    }

    /**
     * Save 2 Swing Components in a single Picture
     *
     * @param myComponent1
     * @param myComponent2
     * @param filename
     * @param format
     */
    public static void saveComponentAsImage(Component myComponent1, Component myComponent2, String filename, String format) {
        // Get Dimensions
        Dimension size1 = myComponent1.getSize();
        Dimension size2 = myComponent2.getSize();
        // Test Dimensions
        if ((size1.width > 0) && (size2.width > 0) && (size1.height > 0) && (size2.height > 0)) {
            // Define BufferedImages
            BufferedImage pict1 = new BufferedImage(size1.width, size1.height, BufferedImage.TYPE_INT_ARGB);
            BufferedImage pict2 = new BufferedImage(size2.width, size2.height, BufferedImage.TYPE_INT_ARGB);
            BufferedImage pict_all = new BufferedImage(Math.max(size1.width, size2.width), (size1.height + size2.height), BufferedImage.TYPE_INT_ARGB);
            // Paint Components in BufferedImages
            myComponent1.paint(pict1.getGraphics());
            myComponent2.paint(pict2.getGraphics());
            // Merge both BufferedImages
            Graphics g = pict_all.getGraphics();
            g.drawImage(pict1, 0, 0, null);
            g.drawImage(pict2, 0, size1.height, null);
            try {
                // Write combined BufferedImage as Picture
                ImageIO.write(pict_all, format, new File(filename));
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            System.err.println("\n# ERROR: Picture has either 0-height or 0-width");
        }
    }

    /**
     * Get remote Data as JSON Element
     *
     * @param s_url
     * @return
     */
    public static JsonElement getJSONdata(String s_url) {
        JsonElement jsdata = null;
        InputStream is = null;
        try {
            // Create URL
            URL url = new URL(s_url);
            // Get remote Data as InputStream
            is = url.openStream();
            // Retrieve JSON Data
            jsdata = new JsonParser().parse(new InputStreamReader(is));
            // Close InputStream
            is.close();
        } catch (Exception ex) {
            System.err.println("\n# ERROR while checking Update");
            System.err.println(ex.getMessage());
        }
        return jsdata;
    }

    /**
     * Extract Data from JSON Element
     *
     * @param s_url
     * @param field
     * @return
     */
    public static String extractJSONdata(String s_url, String field) {
        // Get JSON Data
        JsonElement jsdata = getJSONdata(s_url);
        if (jsdata != null) {
            // JSON Data as Array
            JsonArray arr = jsdata.getAsJsonArray();
            // Loop over JSON Data            
            for (int i = 0; i < arr.size(); i++) {
                // Get current JSON Object
                JsonObject obj = arr.get(i).getAsJsonObject();
                if (obj.has(field)) {
                    // Extract 1st encountered Data using Field name 
                    return obj.get(field).getAsString();
                }
            }
        }
        return null;
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
     * Any Duplicate Elements?
     *
     * @param l
     * @param skip
     * @return
     */
    public static boolean hasDuplicateElements(List<String> l, String skip) {
        // Sets only contain non-duplicate elements
        Set single = new HashSet();
        for (String s : l) {
            // Check skipped Line
            if (!s.equals(skip)) {
                if (!single.add(s)) {
                    // Set.add() returns false if duplicate element
                    return true;
                }
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

    /**
     * Play Sound
     *
     * @param path
     */
    public static void playSound(String path) {
        try {
            // Create URI
            URI uri = Tools.class.getResource(path).toURI();
            // Create Media
            Media media = new Media(uri.toString());
            // Create Media Player
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            // Set Volume Gain (Low Value)
            mediaPlayer.setVolume(0.03);
            // Play Sound
            mediaPlayer.play();
        } catch (Exception ex) {
            System.err.println("\n# ERROR while loading Sound File from " + path);
            System.err.println(ex.getMessage());
        }
    }

}
