/*
 * AboutDialog.java
 *
 * Created on 18 February 2008, 20:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.tams.timer;

import java.awt.*;          // for Dimension, Icon and Layouts
import java.awt.event.*;    // Event listener button
import java.io.*;           // read GPL license file
import javax.swing.*;       // swing components

/**
 *  AboutDialog class
 *  Shows About Dialog window for Surf Alarm.
 *  Displays the GPL license
 *  Modal window - other windows not active
 * @author Tam
 */
public class AboutDialog extends JDialog {
    // dialog window size and size of icon
    final static Dimension SIZE = new Dimension(500, 350);
    final static int ICON_SIZE = 80;
    final static String IMAGE_FILE = "images/alarm.gif";
    // tile plus copyright character \u00A9
    final static String TITLE_STRING = "\n\t\tSURF ALARM \n\t\t tamtam \u00A9 Feb 2007";
    // ok button closes dialog window
    private JButton okButton = null;
    // singleton for button listener
    private AboutDialog self = null;
    
    /** 
     *  Constructor
     *  parameter is parent component
     *  Constructs and shows the dialog window
     *  Creates a new instance of AboutDialog 
     */
    public AboutDialog(JFrame parent) {
        // second parameter modal set to true
        super(parent, "About - " + parent.getTitle(), true);
        setSize(SIZE);
        // assign its self
        self = this;    
        // set content pane
        JPanel content_pane = (JPanel) getContentPane();
        
        /**
         * prepare the top panel - icon first
         */
        ImageIcon icon = new ImageIcon(IMAGE_FILE);
        ImageIcon scaled_icon = new ImageIcon(icon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_DEFAULT));
        JLabel icon_label = new JLabel(scaled_icon);
        icon_label.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
        
        // text area for title - set color to rest of window
        JTextArea textTitle = new JTextArea();
        textTitle.setEditable(false);
        textTitle.setBackground(content_pane.getBackground());
        textTitle.setText(TITLE_STRING);
        
        // add top panel componenets to a top panel
        JPanel top_panel = new JPanel();
        top_panel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        top_panel.setLayout(new BorderLayout());
        top_panel.add(icon_label, BorderLayout.WEST);
        top_panel.add(textTitle);
        /** end top panel */
        
        /**
         *  Prepare the middle panel
         *  prepare text area and add GPL license
         *  message about GPL license in a label then
         *  place license in a scroll pane
         */
        JTextArea license = new JTextArea();
        // method to read GPL license file - parameter is the JTextArea
        readGPL(license);
        JPanel license_panel = new JPanel();
        license_panel.setLayout(new BorderLayout());
        license_panel.add(
                new JLabel("  You can read how to use Surf Timer"), BorderLayout.NORTH);
        license_panel.add(new JScrollPane(license), BorderLayout.CENTER);
        /** end middle panel */
        
        /** 
         *  bottom panel with ok button 
         *  and OK button component event listeners
         */
        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // use singleton for events
                self.setVisible(false);
                self.dispose();
            }
        });
        JPanel button_panel = new JPanel();
        button_panel.add(okButton);
        /** end bottom panel */
        
        /** add three panels, top, middle and button to content pane */
        content_pane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        content_pane.setLayout(new BorderLayout());
        content_pane.add(top_panel, BorderLayout.NORTH);
        content_pane.add(license_panel, BorderLayout.CENTER);
        content_pane.add(button_panel, BorderLayout.SOUTH);
        
        // about dialog on parent
        Rectangle frame_bounds = parent.getBounds();
        setLocation(frame_bounds.x + 
                (frame_bounds.width - SIZE.width) / 2, frame_bounds.y + (frame_bounds.height - SIZE.height) / 2);
        // show dialog
        setVisible(true);
    } 
    /** end constructor */
    
    /**
     *  Method reads the GPL file in conf folder.
     *  Parameter is the JTextArea to display the text. 
     */
    private void readGPL(JTextArea gpl) {
        String pad = "    ";    // padding from text area edge - each line read
        final String GPL_FILE = "conf/surf.txt";
        try {
            // read the file open stream
            BufferedReader read = new BufferedReader(new FileReader(GPL_FILE));
            // read first line
            String text = read.readLine();
            while(text != null){
                // pad out the text add new line
                gpl.append(pad);
                gpl.append(text);
                gpl.append("\n");
                // next line
                text = read.readLine();
            }
            // close steams
            read.close();
            // ensures first line in view in text area
            gpl.setCaretPosition(0);
        } catch (FileNotFoundException ex) {
            // pad out and display exception message in text area
            pad += pad;
            gpl.append(pad);
            gpl.append("File Not Found");
        } catch(IOException ie){
            // pad out and display exception message in text area
            pad += pad;
            gpl.append(pad);
            gpl.append("Problem reading file");
        } // end try - catch
    }   // end readGPL method
}
/**
 *  End class About Dialog
 */
