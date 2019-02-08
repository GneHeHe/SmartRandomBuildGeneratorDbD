package dbd;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.*;

/**
 *
 * SmartRandBuildGenTabInfo
 *
 * @author GneHeHe (2018)
 *
 */
public class SmartRandBuildGenTabInfo extends JPanel {

    // Swing Components
    private JPanel pan_header, pan_author;
    private JScrollPane scrollPane;
    private JEditorPane pan_tuto;
    private JLabel lab_author, lab_email, lab_git;
    private ImageIcon pict;
    // Strings
    private String s_profile, s_git, s_email;
    // SmartRandBuildGen Object
    private SmartRandBuildGen srbg;

    /**
     * Constructor
     *
     * @param myBuilder
     */
    public SmartRandBuildGenTabInfo(SmartRandBuildGen myBuilder) {

        // Set SmartRandBuildGen Object
        this.srbg = myBuilder;

        // Define Strings
        this.s_profile = "http://steamcommunity.com/id/trna";
        this.s_email = "gnehehe70@gmail.com";
        this.s_git = "https://github.com/" + srbg.git_user + "/" + srbg.git_repo + "/releases";

        // Create JEditorPane for HTML Tutorial
        this.pan_tuto = new JEditorPane();

        // Add Swing Components
        addComponents();

        this.pan_author = new JPanel(new FlowLayout());
        this.pan_author.setLayout(new BoxLayout(this.pan_author, BoxLayout.X_AXIS));
        this.pan_author.add(this.lab_author);
        this.pan_author.add(this.lab_email);
        this.pan_author.add(this.lab_git);

        // Create Subpanels
        this.pan_header = new JPanel();
        this.pan_header.setLayout(new BorderLayout());
        this.pan_header.add(new JLabel(this.pict), BorderLayout.CENTER);
        this.pan_header.add(this.pan_author, BorderLayout.SOUTH);

        // Add tutorial to Panel
        this.scrollPane = new JScrollPane(this.pan_tuto);

        // Set the Layout and add Subpanels
        this.setLayout(new BorderLayout());
        this.add(this.pan_header, BorderLayout.NORTH);
        this.add(this.scrollPane, BorderLayout.CENTER);

    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Define ImageIcon Picture
        String logo = "data/logo.png";
        InputStream is = getClass().getResourceAsStream(logo);
        if (is != null) {
            this.pict = new ImageIcon(Tools.resizePicture(is, 45));
        } else {
            System.err.println("# WARNING: " + logo + " not found !");
        }

        // Define JEditorPane
        this.pan_tuto.setEditable(false);
        URL url_tuto = getClass().getResource("data/tuto.html");
        if (url_tuto != null) {
            try {
                this.pan_tuto.setPage(url_tuto);
            } catch (IOException e) {
                getAlert("ERROR: The tutorial file was not found !", "Warning", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } else {
            getAlert("ERROR: The tutorial file was not found !", "Warning", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Define JLabel Name
        this.lab_author = new JLabel("<html><u>Author</u></html>", SwingConstants.CENTER);
        this.lab_author.setFont(new Font("Helvetica", Font.BOLD, 18));
        this.lab_author.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.lab_author.setToolTipText("Go to GneHeHe's STEAM Profile");

        // Define JLabel Email
        this.lab_email = new JLabel("<html><u>Contact</u></html>", SwingConstants.CENTER);
        this.lab_email.setFont(new Font("Helvetica", Font.BOLD, 18));
        this.lab_email.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.lab_email.setToolTipText("Send an email to GneHeHe");

        // Define JLabel Git
        this.lab_git = new JLabel("<html><u>GitHub</u></html>", SwingConstants.CENTER);
        this.lab_git.setFont(new Font("Helvetica", Font.BOLD, 18));
        this.lab_git.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.lab_git.setToolTipText("Go to SRBG GitHub Repository");

        // Define MouseListener
        this.lab_author.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open default Browser and Go to STEAM Profile
                    Desktop.getDesktop().browse(new URI(s_profile));
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("WARNING_STEAM_PROFILE");
                    System.err.println(ex.getMessage());
                }
            }
        });

        // Define MouseListener
        this.lab_email.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open default Mail App
                    URI mailto = new URI("mailto:" + s_email + "?subject=SRBG");
                    Desktop.getDesktop().mail(mailto);
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("WARNING_EMAIL");
                    System.err.println(ex.getMessage());
                }
            }
        });

        // Define MouseListener
        this.lab_git.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open default Browser and Go to STEAM Profile
                    Desktop.getDesktop().browse(new URI(s_git));
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("WARNING_GIT");
                    System.err.println(ex.getMessage());
                }
            }
        });

    }

    /**
     * Display Message in a Window
     *
     * @param msg the string to display
     * @param title the title of the window
     * @param type the type of alert
     */
    private void getAlert(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

}
