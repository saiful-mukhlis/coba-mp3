import java.io.File;

import javax.media.Manager;
import javax.media.Player;
import javax.media.bean.playerbean.MediaPlayer;
public class Tesmp3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			   Player myMp3File = Manager.createPlayer(new File("m:\\backup\\bell\\wav\\000 Sekolah buka.wav").toURI().toURL());
			   myMp3File.start();
			  } catch (Exception e) {
			   e.printStackTrace();
			  }


	}

}
