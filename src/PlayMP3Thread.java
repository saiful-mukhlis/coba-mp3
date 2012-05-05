import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;

public class PlayMP3Thread extends Thread {

 private URL url;

 public PlayMP3Thread(File mp3) {
  try {
   this.url = mp3.toURI().toURL();
  } catch (MalformedURLException e) {
   e.printStackTrace();
  }
 }

 public void run() {
  try {
   MediaLocator ml = new MediaLocator(url);
   final Player player = Manager.createPlayer(ml);
   player.addControllerListener(new ControllerListener() {
    public void controllerUpdate(ControllerEvent event) {
     if (event instanceof EndOfMediaEvent) {
      player.stop();
      player.close();
     }
    }
   });
   player.realize();
   player.start();
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

}