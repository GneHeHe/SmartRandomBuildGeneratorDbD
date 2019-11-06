package dbd;

import java.awt.*;
import javax.swing.*;
import javax.swing.UIManager.*;

/**
 *
 * SmartRandBuildGenGUI
 *
 * @author GneHeHe (2019)
 *
 */
public class SmartRandBuildGenGUI extends JFrame {

    // Main Panel
    private JTabbedPane tabbedPane;
    // Tabs
    private SmartRandBuildGenTabPerks pan_perks;
    private SmartRandBuildGenTabBuild pan_builds;
    private SmartRandBuildGenTabInfo pan_info;
    private SmartRandBuildGenTabData pan_data;

    /**
     * Constructor
     *
     * @param myperks
     * @param mybuilds
     * @param myinfo
     * @param mydata
     */
    public SmartRandBuildGenGUI(SmartRandBuildGenTabPerks myperks, SmartRandBuildGenTabBuild mybuilds, SmartRandBuildGenTabInfo myinfo, SmartRandBuildGenTabData mydata) {

        // Set LookAndFeel
        setlookandfeel();

        // Init Tabs
        pan_perks = myperks;
        pan_builds = mybuilds;
        pan_info = myinfo;
        pan_data = mydata;

        // Main Panel
        tabbedPane = new JTabbedPane();

        // Add Tabs to Main Panel
        tabbedPane.addTab("Configure Perks", pan_perks);
        tabbedPane.addTab("Generate Random Builds", pan_builds);
        tabbedPane.addTab("Database of Builds", pan_data);
        tabbedPane.addTab("Contact & Help", pan_info);

        // Add Tooltips on Tabs
        tabbedPane.setToolTipTextAt(0, "Define the weights for all perks");
        tabbedPane.setToolTipTextAt(1, "Generate random builds (Main tab of SRBG)");
        tabbedPane.setToolTipTextAt(2, "Save favorite builds in database");
        tabbedPane.setToolTipTextAt(3, "Contact author & read help");

        // Set favorite Tab
        tabbedPane.setSelectedIndex(1);

        // Add Main Panel to Frame
        getContentPane().add(tabbedPane);

        // Set Frame
        setTitle(SmartRandBuildGen.TITLE);
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
        SmartRandBuildGen mysrbg = new SmartRandBuildGen(false);
        mysrbg.setConstraintsPerks(1, true);
        mysrbg.setConstraintsPerks(2, true);
        mysrbg.setConstraintsPerks(3, true);
        System.out.println("");

        // Define Tabs
        SmartRandBuildGenTabPerks myPerks = new SmartRandBuildGenTabPerks(mysrbg);
        SmartRandBuildGenTabBuild myBuilds = new SmartRandBuildGenTabBuild(mysrbg);
        SmartRandBuildGenTabInfo myInfo = new SmartRandBuildGenTabInfo(mysrbg);
        SmartRandBuildGenTabData myData = new SmartRandBuildGenTabData(mysrbg);

        // Check Update
        if (mysrbg.checkUpdate()) {
            Tools.getAlert("An update is available from GitHub repository.\n\nClickable link is available from \"Contact & Help\" tab.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }

        // Launch Frame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SmartRandBuildGenGUI frame = new SmartRandBuildGenGUI(myPerks, myBuilds, myInfo, myData);
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
            System.err.println("# ERROR: LookAndFeel can't be loaded\n" + ex.getMessage());
            System.exit(0);
        }
    }

}
