import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.*;
import java.net.*;

public class ContohJMF extends JFrame {
	static Player myPlayer = null;

	public ContohJMF() {
		super("Contoh JMF");
		play();
		Component control = myPlayer.getControlPanelComponent();
		this.getContentPane().add(control, BorderLayout.CENTER);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				stop();
				System.exit(0);
			}
		});
		this.pack();
		this.setSize(new Dimension(400, 150));
		this.setVisible(true);
	}

	public static void main(String[] args) {
		ContohJMF helloJMF = new ContohJMF();
		helloJMF.play();
	}

	void play() {
try {
URL url = new URL("file",null,"l:\\musik\\mmmmm\\03. KOKORONO TOMO - MAYUMI ITSUWA.mp3");
myPlayer = Manager.createRealizedPlayer(url);
}
catch (Exception e) {
System.out.println("Unable to create the audioPlayer :" + e);
}
}

	void stop() {
		myPlayer.stop();
		myPlayer.close();
	}
}