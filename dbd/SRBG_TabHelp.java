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
 * SRBG_TabHelp
 *
 * @author GneHeHe (2019)
 *
 */
public class SRBG_TabHelp extends JPanel {

    // Swing Components
    private JPanel pan_header, pan_author;
    private JScrollPane scrollPane;
    private JEditorPane editor;
    private JLabel lab_git, lab_guide, lab_email, lab_donate;
    private ImageIcon pict;
    // SRBG Object 
    private SRBG srbg;

    /**
     * Constructor
     *
     * @param srbg
     */
    public SRBG_TabHelp(SRBG srbg) {

        // Set SRBG Object
        this.srbg = srbg;

        // Create JEditorPane for HTML Tutorial
        editor = new JEditorPane();

        // Add Swing Components
        addComponents();

        // Create Subpanels
        pan_author = new JPanel(new FlowLayout());
        pan_author.setLayout(new BoxLayout(pan_author, BoxLayout.X_AXIS));
        pan_author.add(lab_git);
        pan_author.add(lab_guide);
        pan_author.add(lab_email);
        pan_author.add(lab_donate);

        pan_header = new JPanel();
        pan_header.setLayout(new BorderLayout());
        pan_header.add(new JLabel(pict), BorderLayout.CENTER);
        pan_header.add(pan_author, BorderLayout.SOUTH);

        // Define Background
        editor.setBackground(pan_header.getBackground());

        // Add Tutorial to Panel
        scrollPane = new JScrollPane(editor);

        // Set Layout & add Subpanels
        setLayout(new BorderLayout());
        add(pan_header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * Add Swing Components
     */
    private void addComponents() {

        // Define ImageIcon Picture
        String s_logo = "data/logo.png";
        if (getClass().getResourceAsStream(s_logo) != null) {
            pict = new ImageIcon(Tools.resizePicture(s_logo, 45));
        } else {
            System.err.println("\n# ERROR: " + s_logo + " not found !\n");
        }

        // Define JEditorPane
        editor.setEditable(false);
        editor.setContentType("text/html");
        URL url_tuto = getClass().getResource("data/tuto.html");
        if (url_tuto != null) {
            try {
                editor.setPage(url_tuto);
            } catch (IOException e) {
                Tools.getAlert("ERROR: The tutorial file was not found !", "Warning", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } else {
            Tools.getAlert("ERROR: The tutorial file was not found !", "Warning", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Define JLabel GitHub Project
        lab_git = new JLabel("<html><u>GitHub Project</u></html>", SwingConstants.CENTER);
        lab_git.setFont(new Font("Helvetica", Font.BOLD, 18));
        lab_git.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lab_git.setToolTipText("Go to GitHub Project Homepage");

        // Define JLabel STEAM Guide
        lab_guide = new JLabel("<html><u>STEAM Guide</u></html>", SwingConstants.CENTER);
        lab_guide.setFont(new Font("Helvetica", Font.BOLD, 18));
        lab_guide.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lab_guide.setToolTipText("<html>Go to STEAM Guide (Steamcommunity.com)<br><br>If you appreciated SRBG, please:<br><ul><li>Upvote this Guide with a 'thumbs up'</li><br><li>Bookmark this Guide to your Favorites</li><br><li>Share it on Social Networks and with your Friends</li></ul>This will help the SRBG Guide to be more visible among the thousands of DbD Guides!</html>");

        // Define JLabel Email
        lab_email = new JLabel("<html><u>Contact</u></html>", SwingConstants.CENTER);
        lab_email.setFont(new Font("Helvetica", Font.BOLD, 18));
        lab_email.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lab_email.setToolTipText("<html>Send an Email to " + srbg.GIT_USER + " to:<br><ul><li>Give Feedback</li><br><li>Report any Bug</li><br><li>Suggest potential new Features</li></ul></html>");

        // Define JLabel Donate
        lab_donate = new JLabel("<html><u>Donate</u></html>", SwingConstants.CENTER);
        lab_donate.setFont(new Font("Helvetica", Font.BOLD, 18));
        lab_donate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lab_donate.setToolTipText("<html>Donation using Paypal<br><br>Feel free to help me out if you enjoyed SRBG, TIA :)</html>");

        // Define MouseListener
        lab_guide.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open default Browser & Go to SRBG STEAM Guide
                    Desktop.getDesktop().browse(new URI(srbg.GUIDE));
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("\n# ERROR: WARNING_GUIDE");
                    System.err.println(ex.getMessage());
                }
            }
        });

        // Define MouseListener
        lab_email.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open default Mail App
                    URI mailto = new URI("mailto:" + srbg.EMAIL + "?subject=SRBG");
                    Desktop.getDesktop().mail(mailto);
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("\n# ERROR: WARNING_EMAIL");
                    System.err.println(ex.getMessage());
                }
            }
        });

        // Define MouseListener
        lab_git.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open default Browser & Go to GitHub
                    Desktop.getDesktop().browse(new URI(srbg.GIT_URL));
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("\n# ERROR: WARNING_GIT");
                    System.err.println(ex.getMessage());
                }
            }
        });

        // Define MouseListener
        lab_donate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open default Browser & Go to GitHub
                    Desktop.getDesktop().browse(new URI(srbg.PAYPAL));
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("\n# ERROR: WARNING_PAYPAL");
                    System.err.println(ex.getMessage());
                }
            }
        });

        // Define HyperlinkListener
        editor.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                    try {
                        // Open default Browser & Go to STEAM Profile
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException ex) {
                        System.err.println("\n# ERROR: WARNING_TUTORIAL");
                        System.err.println(ex.getMessage());
                    }
                }
            }
        });

    }

}
