import java.applet.Applet;
import java.awt.Button;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
 
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.Player;
 
public class LoadSoundApplet extends Applet implements ActionListener, ControllerListener {
 
	Button play, stop;
	private Player player;
	private static final String PLAY = "PLAY";
	private static final String STOP = "STOP";
 
	public void init(){
		play = new Button();
		play.setLabel(PLAY);
		play.setActionCommand(PLAY);
		play.addActionListener(this);
		add(play);
 
		stop = new Button();
		stop.setLabel(STOP);
		stop.setActionCommand(STOP);
		stop.addActionListener(this);
		add(stop);
 
	}
 
	@Override
	public void actionPerformed(ActionEvent e) {
 
		if(e.getActionCommand().equals(PLAY)){
 
			try{
 
				player = Manager.createPlayer(new URL(getCodeBase(),"BillyJean.mp3"));
				player.addControllerListener(this);
 
				player.start();
 
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
 
		}else if(e.getActionCommand().equals(STOP)){
			player.stop();
		}else{
			player.stop();
		}
 
	}
 
	@Override
	public void controllerUpdate(ControllerEvent c) {
		// TODO Auto-generated method stub
		if(player == null)
			return;
 
	}
}
