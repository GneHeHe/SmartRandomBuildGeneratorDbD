package dbd;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * Tools
 *
 * @author GneHeHe (2018)
 *
 */
public class Tools {

    /**
     * Return a Scaled Picture
     *
     * @param filename
     * @param width
     * @param height
     * @return
     */
    public static Image resizePicture(String filename, int width, int height) {
        Image dimg = null;
        try {
            BufferedImage img = ImageIO.read(new File(filename));
            dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (Exception ex) {
            System.err.println("\n# ERROR while resizing " + filename);
            System.err.println(ex.getMessage());
        }
        return dimg;
    }

    /**
     * Return a Scaled Picture
     *
     * @param is
     * @param width
     * @param height
     * @return
     */
    public static Image resizePicture(InputStream is, int width, int height) {
        Image dimg = null;
        try {
            BufferedImage img = ImageIO.read(is);
            dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (Exception ex) {
            System.err.println("\n# ERROR while resizing " + is.toString());
            System.err.println(ex.getMessage());
        }
        return dimg;
    }

    /**
     * Return a Scaled Picture
     *
     * @param is
     * @param percent
     * @return
     */
    public static Image resizePicture(InputStream is, int percent) {
        Image dimg = null;
        try {
            BufferedImage img = ImageIO.read(is);
            dimg = img.getScaledInstance((int) (img.getWidth() * percent / 100), (int) (img.getHeight() * percent / 100), Image.SCALE_SMOOTH);
        } catch (Exception ex) {
            System.err.println("\n# ERROR while resizing " + is.toString());
            System.err.println(ex.getMessage());
        }
        return dimg;
    }

    /**
     * Get Latest Version from Github Project
     *
     * @param user
     * @param proj
     * @return
     */
    public static double checkUpdate(String user, String proj) {
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
            System.err.println(ex.getMessage());
        }
        return version_git;
    }

}
