/*
 * ConfigClass.java
 *
 * Created on 19 February 2008, 22:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.tams.timer;

import java.io.*;

/**
 *  Config class
 *
 *  Configures the sound file...
 *      1. When first run - displays message on bottom Label of Surf Alarm
 *      2. Stores last file run when program last closed
 *      3. Sound file path and name written to configuration file
 *      4. Reads file path and name from configuration file
 *      5. Displays last sound file on bottom label of Surf Alarm
 *      6. Sound File available for Surf Alarm
 *
 * @author Tam
 */
public class ConfigClass {
    // the directory and file - holds the sound file name and path
    final static String directory = "conf/sound.cfg";
    private File file = null;       // for the configuration file
    
    /** 
     * Creates a new instance of ConfigClass 
     * no params - sets File instance variable
     */
    public ConfigClass() {
        file = new File(directory);
     }  /** end constructor */
    
    /**
     *  returns true if configuration file exists
     */
    public boolean fileExists(){
        return file.exists();
    }   /** end method fileExists */
    
    /**
     *  Reads the sound file path and name from configuration file
     *  Returns the File object 
     *  or null if file not found or problems reading file 
     **/
    public File getFile() {
        // check file exists
        if(fileExists()){
            // open streams
            BufferedReader bf;
            try {
                bf = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
                return null;
            }
            
            // path holds String read from file
            String path;
            try {
                path = bf.readLine();
                /**
                 * config file, on first run contains only word NONE.
                 **/
                if(path.equalsIgnoreCase("NONE"))
                    // returning null - handled in calling method
                    return null;
                /*
                 *  configuration file contains message at top 
                 *  denoted by the asterix character at the start
                 */
                while(path.contains("*")) 
                    path = bf.readLine();
                
                // close streams
                bf.close();
            } catch (IOException ex) {
                return null;
            }
            
            /**
             *   last line read is sound file path and name
             *   make sure we have a initialised string
             **/
            if(path != null)
                return new File(path);
        }
        return null;
    } 
    /** end method getFile */
    
    /**
     *  Writes current sound file from Surf Timer 
     *  to configuration file - self explanatory
     *  returns true on success, false otherwise
     */
    public boolean setFile(File filePath){
        // we got a initialised file?
        if(filePath == null)return false;
        // write it name and path to configuration file
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            PrintWriter out = new PrintWriter(bw);
            out.println("***************************************************************");
            out.println("*** Configuration file... holds path and file of sound file ***");
            out.println("***************************************************************");
            out.println(filePath.getAbsolutePath());
            out.close();
            bw.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}