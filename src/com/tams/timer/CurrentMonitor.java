package com.tams.timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;

/**
 * Class which monitors the current time and updates the real time clock
 * on the user interface. The class extends the java.lang.Thread class, 
 * so that it can monitor and update the current time.
 *
 * @author Tam
 */
public class CurrentMonitor extends Thread {
    private JTextField field;   // the text field to update in the user interface
    private final static boolean DEBUG = false;
    /** 
     * Creates a new instance of CurrentMonitor
     * parameter is the text field from the user interface.
     */
    public CurrentMonitor(JTextField tfield) {
        this.field = tfield;
        updateTime();
    } // END constructor

    /**
     * Method updates the current time for display in the user interface text 
     * field utilising java.util.Date and the java.util.SimpleDateFormat
     */
    private void updateTime() {
        Date now = new Date();
        SimpleDateFormat form = new SimpleDateFormat("kk:mm:ss");
        field.setText(form.format(now));
    } // END updateTime
    
    /**
     * The overriden method run is used to constantly update the current time
     * while an instance of CurrentMonitor is active.
     */
    public void run(){
        // run continuously
        while(true){
            try {
                Thread.sleep(500);
                updateTime();
            } catch (InterruptedException ex) {
                if(DEBUG) ex.printStackTrace();
                else field.setText("ERROR");
            }  
        }
    } // END run
} // END CurrentMonitor class.
