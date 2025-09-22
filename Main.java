import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Menu.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        // ensure Swing components are created on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }

    public Main() { 
        setTitle("CHESS GAME");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ใช้ BorderLayout
        setLayout(new BorderLayout());

        // Label หัวเรื่อง
        JLabel title = new JLabel("CHESS GAME DEMO", SwingConstants.CENTER);
        title.setFont(new Font("",Font.PLAIN, 28));
        add(title, BorderLayout.CENTER);

        // ปุ่ม Play
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("",Font.PLAIN, 24));
        playButton.addActionListener(new ActionListener() {
            @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);  // ปิดหน้าปัจจุบัน
            new GameWindow(); // เปิดเกมใหม่
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(playButton);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    
}
