package dbd;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * SmartRandBuildGenTabInfo
 *
 * @author GneHeHe (2019)
 *
 */
public class SmartRandBuildGenTabInfo extends JPanel {

    // Swing Components
    private JPanel pan_header, pan_author;
    private JScrollPane scrollPane;
    private JEditorPane editor;
    private JLabel lab_author, lab_email, lab_git;
    private ImageIcon pict;
    private String url_git;
    private final String PROFILE = "http://steamcommunity.com/id/trna";
    private final String EMAIL = "gnehehe70@gmail.com";

    /**
     * Constructor
     *
     * @param srbg
     */
    public SmartRandBuildGenTabInfo(SmartRandBuildGen srbg) {

        // Define Git String
        this.url_git = "https://github.com/" + SmartRandBuildGen.GIT_USER + "/" + SmartRandBuildGen.GIT_REPO + "/releases";

        // Create JEditorPane for HTML Tutorial
        this.editor = new JEditorPane();

        // Add Swing Components
        this.addComponents();

        // Create Subpanels
        this.pan_author = new JPanel(new FlowLayout());
        this.pan_author.setLayout(new BoxLayout(this.pan_author, BoxLayout.X_AXIS));
        this.pan_author.add(this.lab_author);
        this.pan_author.add(this.lab_email);
        this.pan_author.add(this.lab_git);

        this.pan_header = new JPanel();
        this.pan_header.setLayout(new BorderLayout());
        this.pan_header.add(new JLabel(this.pict), BorderLayout.CENTER);
        this.pan_header.add(this.pan_author, BorderLayout.SOUTH);

        // Add Tutorial to Panel
        this.scrollPane = new JScrollPane(this.editor);

        // Set Layout & add Subpanels
        this.setLayout(new BorderLayout());
        this.add(this.pan_header, BorderLayout.NORTH);
        this.add(this.scrollPane, BorderLayout.CENTER);

    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Define ImageIcon Picture
        String s_logo = "data/logo.png";
        if (getClass().getResourceAsStream(s_logo) != null) {
            this.pict = new ImageIcon(Tools.resizePicture(s_logo, 45));
        } else {
            System.err.println("# WARNING: " + s_logo + " not found !");
        }

        // Define JEditorPane
        this.editor.setEditable(false);
        this.editor.setContentType("text/html");
        URL url_tuto = getClass().getResource("data/tuto.html");
        if (url_tuto != null) {
            try {
                this.editor.setPage(url_tuto);
            } catch (IOException e) {
                Tools.getAlert("ERROR: The tutorial file was not found !", "Warning", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } else {
            Tools.getAlert("ERROR: The tutorial file was not found !", "Warning", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Define JLabel Name
        this.lab_author = new JLabel("<html><u>Author</u></html>", SwingConstants.CENTER);
        this.lab_author.setFont(new Font("Helvetica", Font.BOLD, 18));
        this.lab_author.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.lab_author.setToolTipText("Go to GneHeHe's STEAM profile");

        // Define JLabel Email
        this.lab_email = new JLabel("<html><u>Contact</u></html>", SwingConstants.CENTER);
        this.lab_email.setFont(new Font("Helvetica", Font.BOLD, 18));
        this.lab_email.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.lab_email.setToolTipText("Send an email to GneHeHe");

        // Define JLabel Git
        this.lab_git = new JLabel("<html><u>GitHub</u></html>", SwingConstants.CENTER);
        this.lab_git.setFont(new Font("Helvetica", Font.BOLD, 18));
        this.lab_git.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.lab_git.setToolTipText("Go to GitHub repository to download the last version of SRBG ('SmartRandBuildGen.jar' file)");

        // Define MouseListener
        this.lab_author.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open default Browser & Go to STEAM Profile
                    Desktop.getDesktop().browse(new URI(PROFILE));
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
                    URI mailto = new URI("mailto:" + EMAIL + "?subject=SRBG");
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
                    // Open default Browser & Go to GitHub
                    Desktop.getDesktop().browse(new URI(url_git));
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("WARNING_GIT");
                    System.err.println(ex.getMessage());
                }
            }
        });

        // Define HyperlinkListener
        this.editor.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                    try {
                        // Open default Browser & Go to STEAM Profile
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException ex) {
                        System.err.println("WARNING_TUTORIAL");
                        System.err.println(ex.getMessage());
                    }
                }
            }
        });

    }

}
