/*
 * AlarmAudioPlayer.java
 *
 * Created on 19 February 2008, 17:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.tams.timer;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *  The AlarmAudioPlayer class sets up and processes the sound file...
 *  In other words in creates the AudioInputStream object. The method playSound
 *  sets up and plays the audio file. The method closeSound stops the audio file 
 *  playing as required.
 *
 *  Thanks to R.J.Lorimer and his tutorial on JavaLobby on playing mp3 files in
 *  helping the developer understand the processes required. 
 *
 *  http://www.javalobby.org/java/forums/t18465.html
 *
 *  As is the code will play WAV files. Follow the instructions from the above 
 *  URL/tutorial to enable the application to play mp3 files. You may also be
 *  able to follow the tutorial better to understand what is going on rather 
 *  than the following comments.
 *
 *  @author Tam
 */
public class AlarmAudioPlayer {
    // we need a good size buffer for the
    private static final int BUFFER_SIZE = 4096;
    // the sound file to be played
    private File soundFile = null;
    // input stream for audio files
    private AudioInputStream in = null;
    //data line for buffering audio
    private SourceDataLine line = null;
    // are we debugging
    private final static boolean DEBUGGING = true;
    
    /**
     * Constructor.
     * @param The sound file.
     * Checks to see if sound file present - yes then set up AudioInputStream.
     */
    public AlarmAudioPlayer(File file) {
        // do we have a sound file - assign it
	if(file != null) 
            this.soundFile = file;
	
        // only progress when sound file present
        if(soundFile != null){
            try {
                // get audio input stream for audio file
                in = AudioSystem.getAudioInputStream(soundFile);
            } catch (UnsupportedAudioFileException e) {
                /**
                 * provided to debug if problems arise, set DEBUGGING to false
                 */
                if(DEBUGGING)e.printStackTrace();
                // sound file is null so other methods have no effect
                else soundFile = null; 
            } catch (IOException e) {
                if(DEBUGGING)e.printStackTrace();
                else soundFile = null;
            }
        }
    } // END constructor
	
    /**
     * method sets up and processes the audio file to users audio out
     */
    public void playSound(){
        // no sound file do nothing
        if(soundFile == null) return;
        
        // store format of sound file
        AudioFormat inFormat = in.getFormat();
        // create new format given that the format may be some other encoding like mp3
	AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					inFormat.getSampleRate(), 16, inFormat.getChannels(),
					inFormat.getChannels() * 2, inFormat.getSampleRate(),
					false);	
        // create the new audio stream from the decoded format created
        AudioInputStream decodedInput = AudioSystem.getAudioInputStream(decodedFormat, in);
        // get connection to users sudio out
	DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
	try {
            // get the SourceDataLine for the audio file to start and stop
            line = (SourceDataLine) AudioSystem.getLine(info);
            // open the data line
            line.open(decodedFormat);
	} catch (LineUnavailableException e) {
            // if we have problems debug 
            if(DEBUGGING)e.printStackTrace();
            //close the sound file to terminate
            closeSound();
	}catch (Exception e){
            if(DEBUGGING)e.printStackTrace();
            closeSound();
	}
	
        // start the audio file playing
	line.start();
	
        // number of bytes read -initial value
	int nBytesRead = 0;
        // add buffer big enough to hold audio data
	byte[]	soundData = new byte[BUFFER_SIZE];
        // process the audio file
	while (nBytesRead != -1)
	{
            try {
                // get number of bytes in buffer
                nBytesRead = decodedInput.read(soundData, 0, soundData.length);
            }
            catch (IOException e){
		if(DEBUGGING)e.printStackTrace();
                closeSound();
            }
            // so long as not end of audio file - write buffer to output audio
            if (nBytesRead >= 0){
		line.write(soundData, 0, nBytesRead);
            }
	}
        // flush buffer
        line.drain();
        // clean up
        stopSound();
    } // end playSound
    
    /**
     * stops the audio file process.
     */
    private void stopSound(){
        line.stop();
        line.close();
    }
    
    /**
     * method to prematurely stop the audio file playing
     * call to this method ensures audio quits for this instance
     */
    public void closeSound(){
	if(soundFile == null) return;
        //flushing buffer doesnot stop audio just clean up
	stopSound();
    }

    /**
     * static method to allow objects to validate the audio file format
     * will play on the host system - also signafies whether the required jars
     * have been added to the classpath.
     */
    protected static boolean checkSoundFileFormat(File file) {
        boolean fileOK = true;
        
        try {
            AudioInputStream temp = AudioSystem.getAudioInputStream(file);
            temp.close();
            temp = null;
        } catch (UnsupportedAudioFileException ex) {
            fileOK = false;
        } catch (IOException ex) {
            fileOK = false;
        }
        return fileOK;
    }
}
