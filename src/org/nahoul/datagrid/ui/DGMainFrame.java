/*

Copyright (c) 2013 by Sleiman Rabah

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

*/

package org.nahoul.datagrid.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JMenu;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DGMainFrame extends JFrame {

    private static final Logger log = LoggerFactory.getLogger(DGMainFrame.class.getName());
    //-- The panel that holds all the components.
    private JPanel jpContainerPanel;
    private JLabel lblStatusLabel;
    private JToolBar jtlMainToolBar;
    //-- Holds the JTree.
    private JPanel jpSelectionPanel;
    private JPanel jplMainRightPanel;
    private JPanel jpCenterContainer;
    private JPanel jpViewsPanel;
    private JSplitPane splitViewsLog;
    private JSplitPane spFrameSplitter;
    private JPanel mjpMainPanel;
    private JTabbedPane mGUITabs;
    private JTree mMainTree;
    private DGItemsPanel mRequestViewPanel;

    public DGMainFrame() {
        init();
    }

    private void init() {

        ImageIcon image = GuiHelpers.getImage(AppConstants.APP_ICON);
        setIconImage(image.getImage());
        setTitle("Nahoul Data Grid");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 635, 510);
        //--
        makeMenuBar();
        //--        
        makePanels();
        //--
        makeGuiViews();
        //-- Register the main frame in the golbal instance.
        GuiPackage.getInstance().setMainFrame(this);
        //-- Add the status label here.
        makeStatusBar();
        //--
        makeToolBar();
        this.pack();
    }

    public void showConsoleWindow() {
    }

    public void closeApp() {
        if (GuiHelpers.confirmQ("Are you sure you want to exit?", this.getTitle())) {
            log.info("Closing " + this.getTitle() + "...");
            System.exit(0);
        }
    }

    private void makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        mnFile.setMnemonic(KeyEvent.VK_F);
        menuBar.add(mnFile);

        JMenuItem mntmOpen = new JMenuItem("Open...");
        mntmOpen.setMnemonic(KeyEvent.VK_O);
        mntmOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                log.info("Open menu clicked...");
            }
        });
        mnFile.add(mntmOpen);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.setMnemonic(KeyEvent.VK_X);
        mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeApp();
            }
        });
        mnFile.add(mntmExit);
        JMenuItem mntmConsole = new JMenuItem("Console");
        mntmConsole.setMnemonic(KeyEvent.VK_C);
        mntmConsole.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showConsoleWindow();
            }
        });
        JMenu mnHelp = new JMenu("Help");
        mnHelp.setMnemonic(KeyEvent.VK_H);
        menuBar.add(mnHelp);
        JMenuItem mntmAbout = new JMenuItem("About");
        mntmAbout.setMnemonic(KeyEvent.VK_A);
        mnHelp.add(mntmAbout);
    }

    private void makeToolBar() {
        jtlMainToolBar = mRequestViewPanel.getToolBar();
        jtlMainToolBar.setFloatable(false);
        jtlMainToolBar.setRollover(true);
        mjpMainPanel.add(jtlMainToolBar, BorderLayout.NORTH);
    }

    private void setMyToolBar(JToolBar toolBar) {
        this.remove(jtlMainToolBar);
        jtlMainToolBar = toolBar;
        mjpMainPanel.add(jtlMainToolBar, BorderLayout.NORTH);
        this.repaint();
    }

    private void makePanels() {
        mjpMainPanel = new JPanel();
        mjpMainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mjpMainPanel.setLayout(new BorderLayout(0, 0));
        setContentPane(mjpMainPanel);

        jpContainerPanel = new JPanel();
        mjpMainPanel.add(jpContainerPanel, BorderLayout.CENTER);
        jpContainerPanel.setLayout(new BoxLayout(jpContainerPanel, BoxLayout.X_AXIS));

        spFrameSplitter = new JSplitPane();
        // splitPane.setOneTouchExpandable(true);
        spFrameSplitter.setDividerLocation(150);
        spFrameSplitter.setAlignmentX(Component.RIGHT_ALIGNMENT);
        jpContainerPanel.add(spFrameSplitter);

        jpSelectionPanel = new JPanel();
        jpSelectionPanel.setSize(250, 300);
        spFrameSplitter.setLeftComponent(jpSelectionPanel);
        // Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(150, 50);
        jpSelectionPanel.setMinimumSize(minimumSize);
        jpSelectionPanel.setLayout(new BorderLayout(0, 0));

        jplMainRightPanel = new JPanel();
        jplMainRightPanel.setSize(150, 300);
        spFrameSplitter.setRightComponent(jplMainRightPanel);
        jplMainRightPanel.setLayout(new BorderLayout(150, 150));

        jpCenterContainer = new JPanel();
        jplMainRightPanel.add(jpCenterContainer, BorderLayout.CENTER);
        jpCenterContainer.setLayout(new BorderLayout(0, 0));
    }

    private void makeGuiViews() {
        mRequestViewPanel = new DGItemsPanel();
        this.add(mRequestViewPanel, BorderLayout.CENTER);
    }

    private void makeStatusBar() {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
        statusPanel.setLayout(new BorderLayout());
        lblStatusLabel = new JLabel("Ready...");
        lblStatusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(lblStatusLabel);
        mjpMainPanel.add(statusPanel, BorderLayout.SOUTH);
    }

    public void updateStatusBar(String text) {
        this.lblStatusLabel.setText(text);
    }

}
