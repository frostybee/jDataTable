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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.nahoul.datagrid.ui.table.DGDataTable;

public class DGItemsPanel extends JPanel {

    private JToolBar tbResourceToolBar;
    private JPanel jpViewTitle;
    private JPanel mainPanel;
    private DGDataTable mItemsTable;
    private JButton btnClearItems;

    public DGItemsPanel() {
        super();
        init();
        this.revalidate();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.add(makeTitlePanel(), BorderLayout.NORTH);
        this.add(makeCenterPanel(), BorderLayout.CENTER);
        makeToolBar();
    }

    private Component makeTitlePanel() {
        jpViewTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font font = new Font(Font.SANS_SERIF, 3, 12);
        JLabel tableLabel = new JLabel("Received Resource Requests");
        tableLabel.setFont(font);
        jpViewTitle.add(tableLabel);
        return jpViewTitle;
    }

    private void makeToolBar() {
        tbResourceToolBar = new JToolBar();
        tbResourceToolBar.setFloatable(false);
        tbResourceToolBar.setRollover(true);

//        JButton btnClearAll = new JButton("");
//        btnClearAll.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
//        btnClearAll.setToolTipText("Clear");
//        btnClearAll
//                .setIcon(new ImageIcon(AppConstants.BTN_CLEAR));
//        tbResourceToolBar.add(btnClearAll);
        //--         
        //-- Start service
        btnClearItems = new JButton("Clear Items");
        btnClearItems.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mItemsTable.clearData();
            }
        });
        btnClearItems.setToolTipText("Clear items...");
        btnClearItems
                .setIcon(GuiHelpers.getImage(AppConstants.BTN_CLEAR));
        tbResourceToolBar.add(btnClearItems);
    }

    public JToolBar getToolBar() {
        return tbResourceToolBar;
    }

    private Component makeCenterPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        //--
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        //--
        //--
        mItemsTable = new DGDataTable();
        //-- Table header
        // tablePanel.add(makeTopPanel(), BorderLayout.NORTH);
        tablePanel.add(mItemsTable.getTablePanel(), BorderLayout.CENTER);
        //jpanel.add(tablePanel, BorderLayout.CENTER);
        //--
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        return mainPanel;
    }
}
