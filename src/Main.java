import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
 
public class Main extends JFrame {
 
    private final JLabel l1;
    private final JLabel l2;
    private final JButton b1;
    private final JButton b2;
    private final JButton b3;
    private final JPanel p1;
    private final JPanel p2;
    private final JPanel p3;
    private final JFileChooser fc;
    private String alamat;
    private File file;
    private MediaLocator ml;
    private Player player;
 
    public Main() {
        super("My MP3 Player");
        l1 = new JLabel("Pilih file");
        l2 = new JLabel();
        b1 = new JButton("Open");
        b2 = new JButton("Play");
        b3 = new JButton("Pause");
        b3.setEnabled(false);
        p1 = new JPanel(new FlowLayout());
        p1.add(l1);
        p1.add(b1);
        p2 = new JPanel(new BorderLayout());
        p2.setBorder(new TitledBorder("File Path"));
        p2.add(l2);
        p3 = new JPanel(new GridLayout(1, 2));
        p3.add(b2);
        p3.add(b3);
        setLayout(new BorderLayout());
        add(p1, BorderLayout.NORTH);
        add(p2, BorderLayout.CENTER);
        add(p3, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        setDefaultCloseOperation(3);
        setSize(500, 150);
        setLocationRelativeTo(null);
        fc = new JFileChooser();
        b1.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                fc.setCurrentDirectory(new File(""));
                int i = fc.showOpenDialog(null);
                if (i == JFileChooser.APPROVE_OPTION) {
                    try {
                        alamat = fc.getSelectedFile().getPath();
                        l2.setText(alamat);
                        System.out.println(alamat);
                        file = new File(alamat);
                        ml = new MediaLocator(file.toURL());
                        player = Manager.createPlayer(ml);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "File format tidak didukung", "Error", JOptionPane.ERROR_MESSAGE);
                        l2.setText("");
                    }
                }
            }
        });
 
        b2.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                try {
                    player.start();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                b3.setEnabled(true);
                b2.setEnabled(false);
            }
        });
 
        b3.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                player.close();
                try {
                    player = Manager.createPlayer(ml);
                } catch (Exception ex) {
                }
                b2.setEnabled(true);
                b3.setEnabled(false);
            }
        });
    }
 
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
        new Main();
    }
}