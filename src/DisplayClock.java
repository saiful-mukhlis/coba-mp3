import java.awt.*;
import java.util.*; 
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class DisplayClock extends JFrame {
     DisplayClock() {
        final JLabel timeField = new JLabel();
        timeField.setFont(new Font("sansserif", Font.PLAIN, 20));

        Container content = this.getContentPane();
        content.setLayout(new FlowLayout());
        content.add(timeField); 

        setTitle("Clock");
        setSize(100,70);

        javax.swing.Timer t = new javax.swing.Timer(1000,new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Calendar cal = new GregorianCalendar();
            String hour = String.valueOf(cal.get(Calendar.HOUR));
            String minute = String.valueOf(cal.get(Calendar.MINUTE));
            String second = String.valueOf(cal.get(Calendar.SECOND));
            timeField.setText("" + hour + ":" + minute + ":" + second);
            }
        });
        t.start(); 
    }
    public static void main(String[] args) {
        JFrame clock = new DisplayClock();
        clock.setVisible(true);
    }
}