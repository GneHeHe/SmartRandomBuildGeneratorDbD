package dbd;

import java.awt.*;
import javafx.embed.swing.JFXPanel;
import javax.swing.*;
import javax.swing.UIManager.*;

/**
 *
 * SRBG_GUI
 *
 * @author GneHeHe (2019)
 *
 */
public class SRBG_GUI extends JFrame {

    // Main Panel
    private JTabbedPane tabbedPane;
    // Tabs
    private SRBG_TabPerk pan_perk;
    private SRBG_TabBuild pan_build;
    private SRBG_TabHelp pan_help;
    private SRBG_TabDB pan_db;
    // Strings
    private String s_perk = "Configure Perks";
    private String s_build = "Generate Random Builds";
    private String s_help1 = "Contact & Help";
    private static String s_help2 = "Contact & Help";
    private String s_db = "Build Database";
    // Required to Play Sound
    private final JFXPanel fxPanel = new JFXPanel();

    /**
     * Constructor
     *
     * @param title
     * @param myperk
     * @param mybuild
     * @param myhelp
     * @param mydb
     */
    public SRBG_GUI(String title, SRBG_TabPerk myperk, SRBG_TabBuild mybuild, SRBG_TabHelp myhelp, SRBG_TabDB mydb) {

        // Set LookAndFeel
        setlookandfeel();

        // Init Tabs
        pan_perk = myperk;
        pan_build = mybuild;
        pan_help = myhelp;
        pan_db = mydb;

        // Main Panel
        tabbedPane = new JTabbedPane();

        // Add Tabs to Main Panel
        tabbedPane.addTab(s_perk, pan_perk);
        tabbedPane.addTab(s_build, pan_build);
        tabbedPane.addTab(s_db, pan_db);
        tabbedPane.addTab(s_help1, pan_help);

        // Add Tooltips on Tabs
        tabbedPane.setToolTipTextAt(0, "<html>Define the weight for each perk (optional tab)<br><br>Synergy rules may have impact on perk weights: only values associated to last generated build are displayed in the table<br><br>Perk weights are always reseted to their original values before generating any new build</html>");
        tabbedPane.setToolTipTextAt(1, "Generate random builds & load widget (main tab)");
        tabbedPane.setToolTipTextAt(2, "Browse build database & save your favorite builds");
        tabbedPane.setToolTipTextAt(3, "Tutorial & infos");

        // Set favorite Tab
        tabbedPane.setSelectedIndex(1);

        // Add Main Panel to Frame
        getContentPane().add(tabbedPane);

        // Increase Tooltip Time (ms)
        ToolTipManager.sharedInstance().setDismissDelay(15000);
        ToolTipManager.sharedInstance().registerComponent(tabbedPane);

        // Set Frame
        setTitle(title);
        setSize(new Dimension(1100, 750));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /**
     * Main Method
     *
     * @param args
     */
    public static void main(String[] args) {

        // Build Object (No Verbose Mode in GUI)
        SRBG mysrbg = new SRBG(false);
        mysrbg.setConstraintsPerks(1, true);
        mysrbg.setConstraintsPerks(3, true);

        // Define Tabs
        SRBG_TabPerk myPerks = new SRBG_TabPerk(mysrbg);
        SRBG_TabBuild myBuilds = new SRBG_TabBuild(mysrbg);
        SRBG_TabHelp myInfo = new SRBG_TabHelp(mysrbg);
        SRBG_TabDB myData = new SRBG_TabDB(mysrbg);

        // Check Update
        if (mysrbg.checkUpdate()) {
            String news = Tools.extractJSONdata(mysrbg.GIT_URL_API, "body");
            if (news == null) {
                news = "";
            }
            Tools.getAlert("An update is available from GitHub repository\n\nNew Features:\n" + news + "\n\nClickable 'GitHub' link is available from '" + SRBG_GUI.s_help2 + "' tab", "Information", JOptionPane.INFORMATION_MESSAGE);
        }

        // Launch Frame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SRBG_GUI frame = new SRBG_GUI(mysrbg.TITLE, myPerks, myBuilds, myInfo, myData);
            }
        }
        );
    }

    /**
     * Define best LookAndFeel
     *
     */
    private void setlookandfeel() {

        // LookAndFeel
        String laf = null;

        // Get OS name
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("mac")) {
            // Use System LookAndFeel for MacOS
            laf = UIManager.getSystemLookAndFeelClassName();
        } else {
            boolean found = false;
            // Loop over available LookAndFeels
            LookAndFeelInfo[] tab_laf = UIManager.getInstalledLookAndFeels();
            for (int i = 0; i < tab_laf.length; i++) {
                if (tab_laf[i].getName().matches("Nimbus")) {
                    // Try to use Nimbus LookAndFeel
                    found = true;
                    laf = tab_laf[i].getClassName();
                    break;
                }
            }
            if (!found) {
                // Use System LookAndFeel
                laf = UIManager.getSystemLookAndFeelClassName();
            }
        }

        // Apply predefined LookAndFeel
        try {
            UIManager.setLookAndFeel(laf);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("\n# ERROR: LookAndFeel can't be loaded\n" + ex.getMessage() + "\n");
            System.exit(0);
        }
    }

}
