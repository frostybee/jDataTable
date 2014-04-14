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

import java.awt.Component;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
public class GuiHelpers {

    private static final Logger log = LoggerFactory.getLogger(GuiHelpers.class.getName());

    public static void centerComponentInWindow(Component component, int percentOfScreen) {
        if (percentOfScreen < 0) {
            centerComponentInWindow(component, -percentOfScreen);
            return;
        }
        if (percentOfScreen > 100) {
            centerComponentInWindow(component, 100);
            return;
        }
        double percent = percentOfScreen / 100.d;
        Dimension dimension = component.getToolkit().getScreenSize();
        component.setSize((int) (dimension.getWidth() * percent), (int) (dimension.getHeight() * percent));
        centerComponentInWindow(component);
    }

    /**
     * Use this static method if you want to center a component in Window.
     *
     * @param component the component you want to center in window
     */
    public static void centerComponentInWindow(Component component) {
        Dimension dimension = component.getToolkit().getScreenSize();

        component.setLocation((int) ((dimension.getWidth() - component.getWidth()) / 2),
                (int) ((dimension.getHeight() - component.getHeight()) / 2));
        component.validate();
        component.repaint();
    }

    /**
     * Use this static method if you want to center a component over another
     * component.
     *
     * @param parent the component you want to use to place it on
     * @param toBeCentered the component you want to center
     */
    public static void centerComponentInComponent(Component parent, Component toBeCentered) {
        toBeCentered.setLocation(parent.getX() + (parent.getWidth() - toBeCentered.getWidth()) / 2, parent.getY()
                + (parent.getHeight() - toBeCentered.getHeight()) / 2);

        toBeCentered.validate();
        toBeCentered.repaint();
    }

    public static JScrollPane makeScrollPane(Component comp) {
        JScrollPane pane = new JScrollPane(comp,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setPreferredSize(pane.getMinimumSize());
        pane.setViewportBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        return pane;
    }

    public static void showError(String message, String title) {
        JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(),
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(String message, String title) {
        JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(),
                message,
                title,
                JOptionPane.WARNING_MESSAGE);
    }

    public static void showSuccess(String message, String title) {
        JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(),
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmQ(String msg, String title) {
        return (JOptionPane.showConfirmDialog(GuiPackage.getInstance().getMainFrame(), msg, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION);
    }

    private void setFirstTableColumnPreferredSize(JTable table) {
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setMaxWidth(300);
        column.setPreferredWidth(180);
    }

    public static final void runSafe(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException e) {
                ///TODO add logger in this class.
                //log.warn("Interrupted in thread "+Thread.currentThread().getName(), e);
            } catch (InvocationTargetException e) {
                throw new Error(e);
            }
        }
    }

    public static ImageIcon getImage(String name, String description) {
        ImageIcon icon = getImage(name);
        if (icon != null) {
            icon.setDescription(description);
        }
        return icon;
    }

    public static ImageIcon getImage(String name) {
        try {
            URL url = GuiHelpers.class.getClassLoader().getResource(
                    AppConstants.IMAGES_FOLDER+ name.trim());
            if (url != null) {
                return new ImageIcon(url); // $NON-NLS-1$
            } else {
                log.warn("no icon for " + name);
                return null;
            }
        } catch (NoClassDefFoundError e) {// Can be returned by headless hosts
            log.info("no icon for " + name + " " + e.getMessage());
            return null;
        } catch (InternalError e) {// Can be returned by headless hosts
            log.info("no icon for " + name + " " + e.getMessage());
            return null;
        }
    }
}
