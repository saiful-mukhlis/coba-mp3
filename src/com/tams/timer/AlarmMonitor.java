/*
 * AlarmMonitor.java
 *
 * Created on 19 February 2008, 16:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.tams.timer;

import java.awt.Color;
import java.io.File;
import java.util.Calendar;
import javax.swing.JTextField;

/**
 * The AlarmMonitor class, which extends java.lang.Thread, function is to
 * compare the time the alarm is set to against the current time. When both 
 * are equal the class initiates the sound to fire the alarm sound.
 *
 * @author Tam
 */
public class AlarmMonitor extends Thread {
    
    private JTextField alarmTime = null;                //copy of the alarm text field
    /**
     *  Target variables store the hour, minute and second the alarm will fire
     **/
    private int targetMinute, targetHour, targetSecond; 
    /**
     * Two boolean variables which control the threaded component;
     * stopping controls the loop in the overridden run method
     * isRunnable controls access to the method checkForTriggerAlarm, not 
     * required once alarm has sounded or has been cancelled
     */
    private boolean stopping = false;
    private boolean isRunnable = true;
    /**
     * Two variables used to trigger the alarm.
     * The AlarmAudioPlayer object plays the File object, a music file.
     **/
    private AlarmAudioPlayer player = null;
    private File music = null;
    
    /** 
     * Creates a new instance of AlarmMonitor 
     * @params - JTextField displays the time alarm will fire
     *           int value of the minutes - value of JSpinner object
     *           File is the music file which will play when alarm is fired
     *           
     */
    public AlarmMonitor(JTextField alarm, int minute, File musicFile){
        // instantiate the text field with the JTextField parameter
	alarmTime = alarm;  
        // instantiate the target time - hour, minute and second
	targetHour = targetMinute = targetSecond = 0;
        // method sets and displays in the text field the target time
	getTargetTime(minute);
        // instantiate the music file
	music = musicFile;
    } // END constructor
    
    /**
     *  Sets values for target hour, minute and second. 
     *  Displays formatted time in text field
     *  parameter is the int value for minutes required.
     */
    private void getTargetTime(int minute) {
        // Calendar object for current time
        Calendar now = Calendar.getInstance();
        // set target time from Calendar objects
        targetSecond = now.get(Calendar.SECOND);
        // add minute - value of JSpinner object
	targetMinute = now.get(Calendar.MINUTE) + minute;
	targetHour = now.get(Calendar.HOUR_OF_DAY);
        
        /**
         *  Where current time plus minute value from JSpinner is greater than
         *  60 minutes, we need to ensure we get the correct value for the hour
         *  and minutes to display in the text field and testing for alarm to 
         *  trigger
         */
	if(targetMinute > 59){
		targetMinute -= 60;
		targetHour++;
		if(targetHour > 23)
			targetHour = 0;
	}
        setAlarmTimeText();
    } // end getTargetTime

    private void setAlarmTimeText() {
        /** 
         * Display text - account for showing two numerical values when hour, 
         * minute or second are less than ten 
         */
        String text = "" + (targetHour > 9? targetHour:"0" + targetHour) +
        	":" + (targetMinute > 9? targetMinute:"0" + targetMinute) +
                ":" + (targetSecond > 9? targetSecond:"0" + targetSecond);
        // show formatted text in text field
        alarmTime.setText(text);
    }
    
    /**
     *  Method periodically, by useage in a threaded environment, checks the
     *  current time against the alarm time, when both are same or current 
     *  time is larger then the alarm is triggered. Also changes the text field 
     *  background to yellow when one minute left and red when alarm is 
     *  triggered.
     */
    private void checkForTriggerAlarm() {
        // get current time and extract minutes and seconds
	Calendar now = Calendar.getInstance();
	int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        /**
         * Where current minute is equal to target minute and current second 
         * is equal to target second fire alarm by instantiating the 
         * AlarmAudioPlayer object and playing the sound file parameter.
         * Text field background set to red and boolean isRunnable set to false.
         */
	if(minute == targetMinute){
            if(second >= targetSecond){
                isRunnable = false;
                alarmTime.setBackground(Color.red);
                player = new AlarmAudioPlayer(music);
                player.playSound();
            }
	}else 
            /**
             *  Change text field background to yellow when minute till alarm
             */
            if(minute + 1 == targetMinute)
		alarmTime.setBackground(Color.yellow);
    }   // END checkForTriggerAlarm
    
    /**
     *  Overridden method run from Thread.
     *  Continues to check the alarm time with current time by calling method 
     *  checkForTriggerAlarm up until alarm sounds
     */
    public void run(){
        while(!stopping){
            try {
		Thread.sleep(1000);
		if(isRunnable) checkForTriggerAlarm();
            } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
            }
	}
        // reset text field to initial value and change background to green
        alarmTime.setText("00:00:00");
        alarmTime.setBackground(Color.GREEN);
        // reset stopping semaphore
	stopping = false;
    } // END run.
    
    /**
     *  Method stops alarm sound file playing and sets stopping control 
     *  variable to stop the loop
     */
    public void terminate() {
        if(player != null){
            player.closeSound();
        }
	stopping = true;
    } // END terminate.
}// END AlarmMonitor class
