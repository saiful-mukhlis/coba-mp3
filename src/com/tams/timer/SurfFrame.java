/*
 * SurfFrame.java
 *
 * Created on March 18, 2008, 11:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.tams.timer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author tam
 */
public class SurfFrame extends JFrame implements ActionListener {
    /** 
     * constant strings for alarm set or not - 
     * used in message label on bottom alarm panel
     */
    private final static String SET_ALARM = "Alarm Set\n";
    private final static String NOT_SET = "Alarm Not Set\n";
    /**
     * swing components...
     * a panel for content pane, 3 others for top, middle and bottom panels
     */
    private JPanel contentPane = null;
    private JPanel upperPanel = null;
    private JPanel middlePanel = null;
    private JPanel bottomPanel = null;
    /**
     * top panel consists of label and text area for current time
     */
    private JLabel currentTimeLabel = null;
    private JTextField currentText = null;
    /**
     * middle panel consists of spinner for selecting number of minutes
     * and a button for starting or stopping the alarm
     **/
    private JSpinner spinner = null;
    private JButton button = null;
    /**
     * bottom panel consists of text area for the time alarm is set to
     * and two labels, one for message of alarm set or not,
     * and a label for the sound file currently selected to play
     */
    private JTextField alarmText = null;
    private JLabel messageLabel = null;
    private JLabel soundLabel = null;
    /**
     * the windows menu components, File > Open > Save > Exit
     * and a Help > About
     */
    private JMenuBar menuBar = null;
    private JMenu fileMenu = null;
    private JMenuItem openMenuItem = null;
    private JMenuItem saveMenuItem = null;
    private JMenuItem exitMenuItem = null;
    private JMenu helpMenu = null;
    private JMenuItem aboutMenuItem = null;
    
    /**
     * Other class variables.
     * Class CurrentMonitor variable - thread for current time updates.
     *
     * File object - holds details of the current sound file.
     *
     * Class AlarmMonitor - thread for checking current time against alarm time
     * and firing off the sound file.
     */
    private CurrentMonitor currentMonitor = null;
    private File soundFile = null;
    private AlarmMonitor alarmMonitor = null;
    /**
     * flag for change of sound file - used in prompt for option to save file
     **/
    private boolean changeFile = false;
    
    /** 
     * Creates a new instance of SurfFrame 
     */
    public SurfFrame() {
        this.setSize(350, 350);
        this.setTitle("Surf Timer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // getSurfMenuBar - initializes the menu components
        this.setJMenuBar(getSurfMenuBar());
        // initContentPane initializes swing components on pane
        this.setContentPane(initContentPane());
        /**
         * listen for window closing - to save sound file if changed
         * and stop running threads
         */
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt){
                frameWindowClosing(evt);
            }
        });
        this.setLocationByPlatform(true);
        // start current time thread object
        this.currentMonitor = new CurrentMonitor(currentText);
        this.currentMonitor.start();
        // gets default sound file from last run - read from a config file
        verifySoundFile();
    } // END CONSTRUCTOR
    
    /**
     * main method shows window
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SurfFrame().setVisible(true);
            }
        });
    } // END MAIN

    /**
     * initalizes the main content pane and sets up the windows components
     */
    private Container initContentPane() {
        // background color of pane and components
        Color backColor = new Color(0, 153, 153);
        
        if(contentPane == null){
            // init the content pane
            contentPane = new JPanel();
            contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            contentPane.setBackground(backColor);
            contentPane.setLayout(new BorderLayout());
            
            // top panel components
            // simple label displaying text 
            currentTimeLabel = new JLabel("Current Time: ");
            currentTimeLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
            currentTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            // text field displays - current time - constatntly updated by thread
            currentText = new JTextField("00:00:00");
            currentText.setPreferredSize(new Dimension(100,55));
            currentText.setHorizontalAlignment(SwingConstants.CENTER);
            currentText.setBackground(backColor);
            currentText.setToolTipText("The Current Time");
            currentText.setFont(new Font("Tahoma", 1, 18));
            currentText.setEditable(false);
            
            // ADD components to upper panel
            upperPanel = new JPanel();
            upperPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
            upperPanel.setPreferredSize(new Dimension(340, 100));
            upperPanel.setBackground(backColor);
            upperPanel.add(currentTimeLabel);
            upperPanel.add(currentText);
            
            // middle panel components
            // spinner allows user to select 0-55 minutes for alarm 
            spinner = new JSpinner(new SpinnerNumberModel(0, 0, 55, 5));
            spinner.setPreferredSize(new Dimension(80, 50));
            spinner.setFont(new java.awt.Font("Tahoma", 1, 18));
            spinner.setToolTipText("Enter minutes from now for alarm, enter value or increment in steps of 5 minutes");
            // button for starting or stopping alarm
            button = new JButton("Start Alarm");
            button.addActionListener(this);
            
            // ADD components to middle panel
            middlePanel = new JPanel();
            middlePanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
            middlePanel.setPreferredSize(new Dimension(340, 100));
            middlePanel.setBackground(backColor);
            middlePanel.add(spinner);
            middlePanel.add(button);
            
            /**
             * Bottom Panel utilizes BoxLayout to line components in one column,
             * each in the centre of panel and text centred.
             *
             * Dimension object sets same size for component, max, minimum and
             * preferred sizes for orientation on window, initial values for 
             * labels.
             */
            Dimension size = new Dimension(300,30);
            // label for alarm set or not, initially not set
            messageLabel = new JLabel(NOT_SET);
            messageLabel.setMaximumSize(size);
            messageLabel.setPreferredSize(size);
            messageLabel.setMinimumSize(size);
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            messageLabel.setBackground(backColor);
            // label for sound file,
            soundLabel = new JLabel();
            soundLabel.setMaximumSize(size);
            soundLabel.setPreferredSize(size);
            soundLabel.setMinimumSize(size);
            soundLabel.setHorizontalAlignment(SwingConstants.CENTER);
            soundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            soundLabel.setBackground(backColor);
            // set dimension for text field
            size = new Dimension(120,55);
            // text field for alarm time, initally no time - not set
            alarmText = new JTextField("00:00:00");
            alarmText.setMaximumSize(size);
            alarmText.setPreferredSize(size);
            alarmText.setMinimumSize(size);
            alarmText.setHorizontalAlignment(SwingConstants.CENTER);
            alarmText.setAlignmentX(Component.CENTER_ALIGNMENT);
            alarmText.setBackground(Color.GREEN);
            alarmText.setFont(new Font("Tahoma", 1, 18));
            alarmText.setToolTipText("Time alarm is set to");
            alarmText.setEditable(false);
            
            // ADD components to bottom panel
            bottomPanel = new JPanel();
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
            bottomPanel.setPreferredSize(new Dimension(340, 120));
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
            bottomPanel.setBackground(backColor);
            bottomPanel.add(messageLabel);
            bottomPanel.add(alarmText);
            bottomPanel.add(soundLabel);
            
            /**
             * Finally add top, middle and bottom panels to content pane
             **/
            contentPane.add(upperPanel, BorderLayout.NORTH);
            contentPane.add(middlePanel, BorderLayout.CENTER);
            contentPane.add(bottomPanel, BorderLayout.SOUTH);
        }
        // return the content pane
        return contentPane;
    } // END initContentPane method.

    /**
     * Constructs the menu bar for window.
     * adds mnemonic characters and listeners where appropriate
     */
    private JMenuBar getSurfMenuBar() {
        if(menuBar == null){
            menuBar = new JMenuBar();
            
            // add the file menu items
            fileMenu = new JMenu("File");
            fileMenu.setMnemonic('F');
            openMenuItem = new JMenuItem("Open");
            openMenuItem.setMnemonic('O');
            openMenuItem.setIcon(new ImageIcon("images/Open24.gif"));
            openMenuItem.addActionListener(this);
            saveMenuItem = new JMenuItem("Save");
            saveMenuItem.setMnemonic('S');
            saveMenuItem.setIcon(new ImageIcon("images/Save24.gif"));
            saveMenuItem.addActionListener(this);
            exitMenuItem = new JMenuItem("Exit");
            exitMenuItem.setMnemonic('x');
            exitMenuItem.addActionListener(this);
            
            fileMenu.add(openMenuItem);
            fileMenu.add(saveMenuItem);
            fileMenu.addSeparator();
            fileMenu.add(exitMenuItem);
            
            helpMenu = new JMenu("Help");
            helpMenu.setMnemonic('H');
            aboutMenuItem = new JMenuItem("About");
            aboutMenuItem.setMnemonic('A');
            aboutMenuItem.addActionListener(this);
            
            helpMenu.add(aboutMenuItem);
            
            menuBar.add(fileMenu);
            menuBar.add(helpMenu);
        }
        return menuBar;
    } // END getSurfMenuBar method.

    /**
     * used initially to verify a configuration file is present
     * configuration file holds data of the last sound file saved.
     * On first run of program there will be no default sound file data.
     */
    private void verifySoundFile() {
        /**
         * Object deals with configuration file, holds location of file.
         */
        ConfigClass config = new ConfigClass();
        String temp;    // for text name of sound file or if first run 
        /**
         * where file exists get the sound file
         */ 
        if(config.fileExists()){
            soundFile = config.getFile();
            if(soundFile == null){
                // first run
                temp = "Error OR First Run - Choose a sound file";
            }
            else
                temp = "Sound File: " + soundFile.getName();
        }
        else{
            // something wrong
            temp = "Error OR First Run - Choose a sound file";
        }
        // update sound label accordingly
        soundLabel.setText(temp);
    } // END verifySoundFile.
    
    /**
     * Calls method saveAndClose() 
     **/
    public void frameWindowClosing(WindowEvent event){
        saveAndClose();
    } // END frameWindowClosing

    /**
     * Method performs appropriate function according to which component
     * fired the event.
     **/
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        
        if(obj == button){
            startButtonClicked();
        }
        else if(obj == openMenuItem){
            openMenuItemClicked();
        }
        else if(obj == saveMenuItem){
            saveMenuItemClicked();
        } 
        else if(obj == exitMenuItem){
            exitMenuItemClicked();
        }
        else if(obj == aboutMenuItem){
            aboutMenuItemClicked();
        }
    } // END actionPerformed

    /**
     * Start/Stop Alarm Button fired event.
     * Alarm can be stopped at anytime safely before and after alarm is sounded
     **/
    private void startButtonClicked() {
        // string for alarm set or noty
        String temp;
        // did we start alarm
        if(button.getText().equals("Start Alarm")){
            /**
             * AlarmMonitor object monitors alarm time against current time.
             * Parameters are the alarm text field object, value of JSpinner, sound file.
             * Alarm time set to current time + value of JSpinner object.
             * Thread used to check current time with time alarm set to.
             * When the times are equal sound file plays.
             **/
            alarmMonitor = new AlarmMonitor(alarmText, (Integer)spinner.getValue(), soundFile);
            alarmMonitor.start();
            button.setText("Stop Alarm");   // change button text to stop alarm.
            spinner.setEnabled(false);      // stops resetting minutes value
            temp = SET_ALARM;               // for message label
        }
        else {
            // terminate alarm stops thread
            alarmMonitor.terminate();
            button.setText("Start Alarm");  // change button text to start alarm
            // enable and zero the JSpinner object to select minutes
            spinner.setEnabled(true);
            spinner.setValue(0);
            // free resource for reuse if button selected again
            if(alarmMonitor != null)
                alarmMonitor = null;
            temp = NOT_SET; 
        }
        // update message label accordingly
        messageLabel.setText(temp);
    } // END startButtonClicked.

    /**
     * Method allows user to select new sound file
     **/
    private void openMenuItemClicked() {
        /**
         * File object set to current sound file - incase user presses cancel
         **/
        File tempFile = soundFile;
        // select sound file
        JFileChooser fileChooser = new JFileChooser();
	fileChooser.showOpenDialog(this);
	soundFile = fileChooser.getSelectedFile();
        // did user press cancel
        if(soundFile == null){
            // yes restore old sound file
            soundFile = tempFile;
        }
        // check audio format is valid for surf alarm and OS
        else if(AlarmAudioPlayer.checkSoundFileFormat(soundFile)){
            // new sound file - update boolean flag and sound label
            changeFile = true;
            soundLabel.setText("Sound File: " + soundFile.getName());
        }
        else {
            // show error message dialog
            invalidAudioFormat();
            // restore old sound file
            soundFile = tempFile;
        }
    } // END openMenuItemClicked

    /**
     * Method initialises message string and calls method to save sound file
     **/
    private void saveMenuItemClicked() {
        String msg = "Saved the current sound file?\n" + soundFile.getName();
        saveOption(msg);
    } // END saveMenuItemClicked.

    /**
     * Method calls method to save sound file (if changed) and stop running threads
     * then closes window. 
     **/
    private void exitMenuItemClicked() {
        saveAndClose();
        System.exit(0);
    }

    /**
     * Shows dialog box about surf alarm
     */
    private void aboutMenuItemClicked() {
        AboutDialog about = new AboutDialog(this);
        if(about != null){
            about = null;
        }
    }


    
    /**
     * Following methods deal with saving the sound file and closing the window.
     *
     * When save is chosen from the File menu, user is prompted to save the current
     * sound file on acknowledgement, sound file data is written to the configuration 
     * file.
     *
     * On exiting from the program, if sound file has changed since last saved, user
     * is prompted whether they want to save file or not. The program then terminates
     * any  running threads.
     *
     * The boolean flag changeFile is used to signify whether or not the file has 
     * changed since last save was commited, be it during this run or previous
     * run of the program.
     */
    
    /**
     * Prompt for save if sound file changed then terminate threads
     **/
    private void saveAndClose() {
        if(changeFile){
            String msg = "Sound File Has Changed\n SAVE this sound to run by Default\n";
            msg += soundFile.getName();
            saveOption(msg);
        }
        if(currentMonitor != null)
            currentMonitor = null;
        if(alarmMonitor != null)
            alarmMonitor = null;
    }   // END saveAndClose.

    /**
     * Prompt for saving the sound file
     */
    private void saveOption(String msg) {
        if(changeFile){
            String[] choices ={"Save", "Cancel"};
            int response = JOptionPane.showOptionDialog(this, 
                    msg, "Save Sound File", JOptionPane.YES_NO_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, null, choices, "Cancel");
            
            if(response == 0 )
                saveSound();
        }
    }   // END saveOption.

    /**
     * Save the sound file, written to configuration file utilising the
     * ConfigClass object.
     */
    private void saveSound() {
        ConfigClass config = new ConfigClass();
        if(changeFile){
            /**
             * ConfigClass method returns true if file written successfully to file.
             * Where old data not present method verifySoundFile will flag an error.
             */
            if(config.setFile(soundFile))
                changeFile = true;
            else
                verifySoundFile();
        }
    }   // END saveSound.

    /**
     * shows Error message when audio format is not valid for sound file
     */
    private void invalidAudioFormat() {
        // construct message showing extension
        String msg = "Audio Format ";
        String fileName = soundFile.getName();
        int index = fileName.indexOf('.');
        msg += fileName.substring(index + 1);
        msg += "\n not compatible in Surf Alarm";
        // show message pane
        JOptionPane.showMessageDialog(this, msg, "Audio Format Error", JOptionPane.OK_OPTION);
    }
}
