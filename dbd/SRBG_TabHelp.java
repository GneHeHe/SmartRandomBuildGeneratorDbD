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
    private JLabel lab_author, lab_email, lab_git;
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
        pan_author.add(lab_author);
        pan_author.add(lab_email);
        pan_author.add(lab_git);

        pan_header = new JPanel();
        pan_header.setLayout(new BorderLayout());
        pan_header.add(new JLabel(pict), BorderLayout.CENTER);
        pan_header.add(pan_author, BorderLayout.SOUTH);

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
            System.err.println("# WARNING: " + s_logo + " not found !");
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

        // Define JLabel Name
        lab_author = new JLabel("<html><u>Author</u></html>", SwingConstants.CENTER);
        lab_author.setFont(new Font("Helvetica", Font.BOLD, 18));
        lab_author.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lab_author.setToolTipText("Go to GneHeHe's STEAM profile");

        // Define JLabel Email
        lab_email = new JLabel("<html><u>Contact</u></html>", SwingConstants.CENTER);
        lab_email.setFont(new Font("Helvetica", Font.BOLD, 18));
        lab_email.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lab_email.setToolTipText("Send an email to GneHeHe");

        // Define JLabel Git
        lab_git = new JLabel("<html><u>SRBG on GitHub</u></html>", SwingConstants.CENTER);
        lab_git.setFont(new Font("Helvetica", Font.BOLD, 18));
        lab_git.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lab_git.setToolTipText("Go to GitHub repository to download the last version of SRBG ('SmartRandBuildGen.jar' file)");

        // Define MouseListener
        lab_author.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open default Browser & Go to STEAM Profile
                    Desktop.getDesktop().browse(new URI(srbg.STEAM));
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("WARNING_STEAM_PROFILE");
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
                    System.err.println("WARNING_EMAIL");
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
                    Desktop.getDesktop().browse(new URI(srbg.GIT_URL_RELEASE));
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("WARNING_GIT");
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
                        System.err.println("WARNING_TUTORIAL");
                        System.err.println(ex.getMessage());
                    }
                }
            }
        });

    }

}
