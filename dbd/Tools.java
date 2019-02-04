package dbd;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
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
            System.out.println(ex.getMessage());
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
            System.out.println(ex.getMessage());
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
            System.out.println(ex.getMessage());
        }
        return dimg;
    }

}
