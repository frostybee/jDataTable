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
package org.nahoul.datagrid;

import org.nahoul.datagrid.ui.DGMainFrame;
import org.nahoul.datagrid.ui.GuiHelpers;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDataGrid {

    private static final Logger log = LoggerFactory.getLogger(JDataGrid.class.getName());

    private static void startGUI() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to load Look and Feel....");
            System.out.println(ex.getMessage());
        }

        try {

            DGMainFrame mainFrame = new DGMainFrame();
            mainFrame.setVisible(true);
            mainFrame.validate();
            //-- Center the main frame.
            GuiHelpers.centerComponentInWindow(mainFrame, 70);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator jc = new JoranConfigurator();
            jc.setContext(context);
            context.reset(); // override default configuration
            // inject the name of the current application as "application-name"
            // property of the LoggerContext
            context.putProperty("application-name", "Data Grid");
            jc.doConfigure("logback.xml");
            log.info("Starting GUI...");
            startGUI();
        } catch (Exception e) {
            System.out
                    .print("A problem has been occured when running the application.");
            System.out.print("System Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
