package End;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import Start.Start;
import setBackgroud.*;

public class DrawGameWindow extends JFrame {
    /**
     * คอนสตรักเตอร์สำหรับสร้างหน้าต่าง End Game
     */
    public DrawGameWindow() {
        setTitle("Draw");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //สร้าง icon เเละแสดงผล
        ImageIcon icon = new ImageIcon("Image/icon.png");
        setIconImage(icon.getImage());
        
        BackgroundPanel backgroundPanel = new BackgroundPanel("Image/End.png");
        setContentPane(backgroundPanel);

        // --- ส่วนหัว (ข้อความ END GAME) ---
        JLabel titleLabel = new JLabel("Draw Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60)); // ฟอนต์ขนาดใหญ่
        titleLabel.setForeground(new Color(192, 57, 43)); // สีแดงเข้ม (Pomegranate)
        backgroundPanel.add(titleLabel, BorderLayout.CENTER);

        // --- ส่วนปุ่ม (Panel สำหรับใส่ปุ่ม) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // ทำให้ JPanel นี้โปร่งใส
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // 1. ปุ่ม Back (กลับไปเล่นใหม่/เมนู)
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 30)); // ฟอนต์ที่ใหญ่ขึ้นและตัวหนา
        backButton.setBackground(new Color(46, 204, 113)); // สีเขียวสดใส (Emerald)
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(250, 60)); // กำหนดขนาดปุ่ม
        backButton.setBorder(BorderFactory.createRaisedBevelBorder()); // ขอบนูน
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileToDelete = new File("Savefile/filegame.dat");
                if (fileToDelete.exists()) {
                    fileToDelete.delete(); // ลบไฟล์ถ้ามีอยู่
                }
                setVisible(false);
                new Start();
            }
        });

        // 2. ปุ่ม Exit (ออกจากโปรแกรม)
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 30));
        exitButton.setBackground(new Color(255, 69, 0)); 
        exitButton.setForeground(Color.WHITE);
        exitButton.setPreferredSize(new Dimension(250, 60)); // กำหนดขนาดปุ่ม
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(backButton);
        buttonPanel.add(exitButton);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}

