import javax.swing.*;
import java.awt.*;
import Menu.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        new Main();
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
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.CENTER);

        // ปุ่ม Play
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.PLAIN, 24));
        playButton.addActionListener(e -> { 
            dispose();
            // เปิดเกมจริง
            new GameWindow();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(playButton);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    
}
