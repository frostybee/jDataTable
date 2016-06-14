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

package org.nahoul.datagrid.ui.grid;

import org.nahoul.datagrid.core.Functor;
import org.nahoul.datagrid.core.PersonInfo;
import org.nahoul.datagrid.ui.DgGuiHelpers;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
public class DgDataTable implements ActionListener {

    private static final Logger log = LoggerFactory.getLogger(DgDataTable.class.getName());
    private JButton btnLoadItems;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnUp;
    private JButton btnDown;
    private JTable mjtItemTable;
    private JScrollPane mTableScrollPane;
    private JPanel mjpTablePanel;
    private JPopupMenu mnPopUpMenu;
    //--
    private DgObjectModel mTableModel;
    private Object mLock = new Object();
    //-- Image objects.
    private static final ImageIcon imageSuccess = DgGuiHelpers.getImage(
            "dialog-ok.png", "Processed");
    private static final ImageIcon imageFailure = DgGuiHelpers.getImage(
            "error.png", "Not Processed");
    //-- Column names.
    private static final String[] mTableColumns = {
        "ID",
        "First Name",
        "Last Name",
        "Email",
        "Accepted",
        "Processed"
    };
    // Column renderers
    private static final TableCellRenderer[] mCellRenderers
            = new TableCellRenderer[]{
                new DgCellLeftAlignRenderer(), // Label
                new DgCellLeftAlignRenderer(), // Description
                null, // Mean        
                null
            };

    public DgDataTable() {
        super();
        init();
    }

    private void init() {

        mTableModel = new DgObjectModel(mTableColumns,
                PersonInfo.class,
                new Functor[]{
                    new Functor("getPersonID"),
                    new Functor("getFirstName"),
                    new Functor("getLastName"),
                    new Functor("getEmail"),
                    new DgDataTable.SampleSuccessFunctor("isAccepted"),
                    new DgDataTable.SampleSuccessFunctor("isProcessed"),},
                new Functor[]{null, null, null, null},
                new Class[]{String.class,
                    String.class,
                    String.class,
                    String.class,
                    ImageIcon.class, ImageIcon.class});
        //--
        makeResourceJTable();
        //--
        loadData();
    }

    public void clearData() {
        if (DgGuiHelpers.confirmQuestion("Do you really want to remove all table items?", "Confirm Clear Items")) {
            synchronized (mLock) {
                log.info("Clearing items...");
                mTableModel.clearData();
            }
        }
    }

    private void makeResourceJTable() {
        mjpTablePanel = new JPanel();
        mjpTablePanel.setLayout(new BorderLayout());
        Border margin = new EmptyBorder(10, 10, 5, 10);
        mjpTablePanel.setBorder(margin);
        //--
        mjtItemTable = new JTable(mTableModel);
        mjtItemTable.getTableHeader().setDefaultRenderer(new HeaderAsPropertyRenderer());
        mjtItemTable.setDefaultRenderer(String.class, new DgCellCenterAlignRenderer());
        mjtItemTable.setPreferredScrollableViewportSize(new Dimension(500, 440));
        mjtItemTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        mjtItemTable.getColumnModel().getColumn(1).setPreferredWidth(130);
        //--
        JPanel tablePanel = new JPanel(new BorderLayout());
        //--
        mTableScrollPane = DgGuiHelpers.makeScrollPane(tablePanel);
        mTableScrollPane.setRowHeaderView(tablePanel);
        mTableScrollPane.setCorner(
                JScrollPane.UPPER_LEFT_CORNER,
                mjtItemTable.getTableHeader());
        mTableScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        //...
        DgRendererUtils.applyRenderers(mjtItemTable, mCellRenderers);
        //...
        tablePanel.add(mjtItemTable.getTableHeader(), BorderLayout.PAGE_START);
        tablePanel.add(DgGuiHelpers.makeScrollPane(mjtItemTable), BorderLayout.CENTER);
        //--
        mjpTablePanel.add(tablePanel, BorderLayout.CENTER);
        mjpTablePanel.add(makeControlPanel(), BorderLayout.SOUTH);
        //--
        //-- Setup resource detail splitter.
        /*spResourceView = new JSplitPane();
         spResourceView.setDividerSize(10);
         spResourceView.setDividerLocation(920);
         spResourceView.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
         spResourceView.setOneTouchExpandable(true);
         spResourceView.setLeftComponent(jpTablePanel);
         spResourceView.setRightComponent(jpResourceDetail);*/
        //--
        mjtItemTable.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int row = mjtItemTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < mjtItemTable.getRowCount()) {
                    mjtItemTable.setRowSelectionInterval(row, row);
                } else {
                    mjtItemTable.clearSelection();
                }
                int rowindex = mjtItemTable.getSelectedRow();
                if (rowindex < 0) {
                    return;
                }
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    PersonInfo selectedRequest = (PersonInfo) mTableModel.getValueAt(rowindex);
                    JPopupMenu popup = createTablePopUp(selectedRequest);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private JPopupMenu createTablePopUp(PersonInfo selectedRequest) {
        mnPopUpMenu = new JPopupMenu();

        //--        
        JMenuItem mntEdit = new JMenuItem("Edit Item");
        mntEdit.setMnemonic(KeyEvent.VK_E);
        mntEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editItem();
            }
        });
        //--
        JMenuItem mntmDelete = new JMenuItem("Delete Item");
        mntmDelete.setMnemonic(KeyEvent.VK_D);
        mntmDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });
        //--         
        mnPopUpMenu.add(mntEdit);
        mnPopUpMenu.add(new JSeparator(0));
        mnPopUpMenu.add(mntmDelete);

        return mnPopUpMenu;
    }

    public Component getTablePanel() {
        return mjpTablePanel;
    }

    private Component makeControlPanel() {

        btnDelete = new JButton("Delete");
        btnDelete.setMnemonic(KeyEvent.VK_D);
        btnDelete.setToolTipText("Delete resource(s)");

        btnUp = new JButton("Up");
        btnUp.setMnemonic(KeyEvent.VK_U);
        btnUp.setToolTipText("Move up");
        //--
        btnDown = new JButton("Down");
        btnDown.setMnemonic(KeyEvent.VK_N);
        btnDown.setToolTipText("Move down");

        btnLoadItems = new JButton("Load Items");
        btnLoadItems.setMnemonic(KeyEvent.VK_L);
        btnLoadItems.setToolTipText("Load available items");
        //--
        btnEdit = new JButton("Edit");
        btnEdit.setMnemonic(KeyEvent.VK_Q);
        btnEdit.setToolTipText("Edit Request");
        //--
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        btnDelete.addActionListener(this);
        btnEdit.addActionListener(this);
        btnUp.addActionListener(this);
        btnDown.addActionListener(this);
        btnLoadItems.addActionListener(this);
        //--

        buttonPanel.add(btnLoadItems);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnUp);
        buttonPanel.add(btnDown);
        return buttonPanel;
    }

    public void changeSelectedRow() {
        int i = mjtItemTable.getSelectedRow();
        mTableModel.fireTableDataChanged();
        //-- If it is the first row, then move down.        
        if (i == 0) {
            moveDown();
            moveUp();
        } else {
            moveUp();
            moveDown();
        }
    }

    private void moveUp() {
        int[] rowsSelected = mjtItemTable.getSelectedRows();
        if (rowsSelected.length > 0 && rowsSelected[0] > 0) {
            mjtItemTable.clearSelection();
            int rowSelected = rowsSelected[0];
            mjtItemTable.addRowSelectionInterval(rowSelected - 1, rowSelected - 1);
        } else {
            //-- No row has been selected! Then select the last one.
            int rowCount = mTableModel.getRowCount();
            if (rowCount > 0) {
                mjtItemTable.clearSelection();
                mjtItemTable.addRowSelectionInterval(rowCount - 1, rowCount - 1);
            }
        }
    }

    private void moveDown() {
        int[] rowsSelected = mjtItemTable.getSelectedRows();
        if (rowsSelected.length > 0 && rowsSelected[rowsSelected.length - 1] < mjtItemTable.getRowCount() - 1) {        
            mjtItemTable.clearSelection();
            int rowSelected = rowsSelected[0];
            mjtItemTable.addRowSelectionInterval(rowSelected + 1, rowSelected + 1);

        } else {
            int rowCount = mTableModel.getRowCount();
            if (rowCount > 0) {
                mjtItemTable.clearSelection();
                mjtItemTable.addRowSelectionInterval(rowCount - 1, rowCount - 1);
            }
            /*//-- No row has been selected!
            if (mTableModel.getRowCount() == 0) {
                mjtItemTable.clearSelection();
                mjtItemTable.addRowSelectionInterval(0, 0);
            }*/
        }
    }

    private void deleteSelectedRow() {
        if (mjtItemTable.isEditing()) {
            TableCellEditor cellEditor = mjtItemTable.getCellEditor(mjtItemTable.getEditingRow(), mjtItemTable
                    .getEditingColumn());
            cellEditor.cancelCellEditing();
        }
        int rowSelected = mjtItemTable.getSelectedRow();
        if (rowSelected >= 0) {
            if (DgGuiHelpers.confirmQuestion("Are you sure you want to delete this item?", "Confirm Delete Item")) {
                //--
                //--
                if (mTableModel.getRowCount() == 0) {
                    btnDelete.setEnabled(false);
                } // Table still contains one or more rows, so highlight (select)
                // the appropriate one.
                else {
                    int rowToSelect = rowSelected;
                    if (rowSelected >= mTableModel.getRowCount()) {
                        rowToSelect = rowSelected - 1;
                    }
                    mTableModel.removeRow(rowSelected);
                    mTableModel.fireTableDataChanged();
                }
            }
        } else {
            DgGuiHelpers.showWarning("You must select a resource to delete!", "Delete Resource");

        }
    }

    public boolean isRowSelected() {
        return (mjtItemTable.getSelectedRows().length > 0);
    }

    private void loadData() {
        log.info("Clearing items...");
        mTableModel.clearData();
        //--
        log.info("Loading items...");
        PersonInfo newRow = new PersonInfo("1", "Joe", "Scotch", "joe@example.com", true, false);
        mTableModel.addRow(newRow);
        newRow = new PersonInfo("2", "John", "Smith", "smith@example.com", false, true);
        mTableModel.addRow(newRow);
        newRow = new PersonInfo("3", "John", "Smith", "smith@example.com", false, false);
        mTableModel.addRow(newRow);
        log.info("Items loaded...");
    }

    private void editItem() {
        if (isRowSelected()) {
            int rowIndex = mjtItemTable.getSelectedRow();
            PersonInfo item = (PersonInfo) mTableModel.getValueAt(rowIndex);
            log.info("Editting item: " + item.getFirstName());
        } else {
            DgGuiHelpers.showWarning("Please select a row to edit", "Edit Item");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDelete) {
            deleteSelectedRow();
        } else if (e.getSource() == btnUp) {
            moveUp();
        } else if (e.getSource() == btnDown) {
            moveDown();
        } else if (e.getSource() == btnEdit) {
            editItem();
        } else if (e.getSource() == btnLoadItems) {
            loadData();
        }
    }

    public static class SampleSuccessFunctor extends Functor {

        public SampleSuccessFunctor(String methodName) {
            super(methodName);
        }

        @Override
        public Object invoke(Object p_invokee) {
            Boolean success = (Boolean) super.invoke(p_invokee);

            if (success != null) {
                if (success.booleanValue()) {
                    return imageSuccess;
                } else {
                    return imageFailure;
                }
            } else {
                return null;
            }
        }
    }
}
