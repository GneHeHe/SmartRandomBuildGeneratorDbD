package dbd;

import java.awt.*;
import javax.swing.*;
import javax.swing.UIManager.*;

/**
 *
 * SmartRandBuildGenGUI
 *
 * @author GneHeHe (2018)
 *
 */
public class SmartRandBuildGenGUI extends JFrame {

    // Tabs
    private SmartRandBuildGenTabPerks pan_perks;
    private SmartRandBuildGenTabBuild pan_builds;
    private SmartRandBuildGenTabInfo pan_info;

    /**
     * Constructor
     *
     * @param myperks
     * @param mybuilds
     * @param myinfo
     */
    public SmartRandBuildGenGUI(SmartRandBuildGenTabPerks myperks, SmartRandBuildGenTabBuild mybuilds, SmartRandBuildGenTabInfo myinfo) {

        // Set LookAndFeel
        setlookandfeel();

        // Init the Tabs
        this.pan_perks = myperks;
        this.pan_builds = mybuilds;
        this.pan_info = myinfo;

        // TabPanel Component
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Panels as Tabs
        tabbedPane.addTab("Configure Perks", this.pan_perks);
        tabbedPane.addTab("Generate Random Builds", this.pan_builds);
        tabbedPane.addTab("Contact & Help", this.pan_info);

        // Add Tooltip text on Tabs
        tabbedPane.setToolTipTextAt(0, "Set Side and the Weight for each Perk");

        // Set the favorite Tab
        tabbedPane.setSelectedIndex(0);

        // Add tabs to current Frame
        getContentPane().add(tabbedPane);

        // Set the Frame
        setTitle(myperks.srbg.getTitle());
        setSize(new Dimension(900, 760));
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

        // Build SmartRandBuildGen Object
        SmartRandBuildGen srbg = new SmartRandBuildGen();

        // Define Tabs
        final SmartRandBuildGenTabPerks myPerks = new SmartRandBuildGenTabPerks(srbg);
        final SmartRandBuildGenTabBuild myBuilds = new SmartRandBuildGenTabBuild(srbg);
        final SmartRandBuildGenTabInfo myInfo = new SmartRandBuildGenTabInfo(srbg);

        
        if (srbg.checkUpdate()) {
            getAlert("An update is available from GitHub repository.\n\nClickable link is available from \"Contact & Help\" tab.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Launch Frame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SmartRandBuildGenGUI frame = new SmartRandBuildGenGUI(myPerks, myBuilds, myInfo);
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

        // Apply the predefined LookAndFeel
        try {
            UIManager.setLookAndFeel(laf);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("# ERROR: LookAndFeel can't be loaded\n" + ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Display Message in a Window
     *
     * @param msg the string to display
     * @param title the title of the window
     * @param type the type of alert (error, information ...)
     */
    private static void getAlert(String msg, String title, int type) {
        JOptionPane.showMessageDialog(null, msg, title, type);
    }

}
