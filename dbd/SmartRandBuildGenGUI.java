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
        this.pan_perks = myperks;
        this.pan_builds = mybuilds;
        this.pan_info = myinfo;
        this.pan_data = mydata;

        // Main Panel
        this.tabbedPane = new JTabbedPane();

        // Add Tabs to Main Panel
        this.tabbedPane.addTab("Configure Perks", this.pan_perks);
        this.tabbedPane.addTab("Generate Random Builds", this.pan_builds);
        this.tabbedPane.addTab("Database of Builds", this.pan_data);
        this.tabbedPane.addTab("Contact & Help", this.pan_info);

        // Add Tooltips on Tabs
        this.tabbedPane.setToolTipTextAt(0, "Define the weights for all perks");
        this.tabbedPane.setToolTipTextAt(1, "Generate random builds (Main tab of SRBG)");
        this.tabbedPane.setToolTipTextAt(2, "Save favorite builds in database");
        this.tabbedPane.setToolTipTextAt(3, "Contact author & read help");

        // Set favorite Tab
        this.tabbedPane.setSelectedIndex(1);

        // Add Main Panel to Frame
        this.getContentPane().add(this.tabbedPane);

        // Set Frame
        this.setTitle(SmartRandBuildGen.TITLE);
        this.setSize(new Dimension(1100, 750));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

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
