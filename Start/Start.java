package Start;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import setBackgroud.*;
import Menu.*;

public class Start extends JFrame {
    public Start() {
        setTitle("CHESS GAME");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //สร้าง icon เเละแสดงผล
        ImageIcon icon = new ImageIcon("Image/icon.png");
        setIconImage(icon.getImage());

        // สร้าง BackgroundPanel และกำหนดรูปภาพพื้นหลัง
        // ตรวจสอบให้แน่ใจว่าไฟล์ chess.png อยู่ในตำแหน่งที่ถูกต้อง
        BackgroundPanel backgroundPanel = new BackgroundPanel("Image/chess.png");
        setContentPane(backgroundPanel);

        // สร้าง JPanel สำหรับปุ่มทั้งหมด
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // ทำให้ JPanel นี้โปร่งใส
        buttonPanel.setLayout(null);

        // ปุ่ม Play
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 30)); // ฟอนต์ที่ใหญ่ขึ้นและตัวหนา
        playButton.setBackground(new Color(60, 179, 113)); // สีเขียวมรกต
        playButton.setForeground(Color.WHITE); // สีตัวอักษรสีขาว
        playButton.setBorder(BorderFactory.createRaisedBevelBorder()); // ขอบนูน
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new GameWindow(false); // เริ่มเกมใหม่เสมอ
            }
        });

        // ปุ่ม Continue
        JButton ContinueButton = new JButton("Continue");
        ContinueButton.setFont(new Font("Arial", Font.BOLD, 30));
        ContinueButton.setBackground(new Color(30, 144, 255)); // สีน้ำเงิน
        ContinueButton.setForeground(Color.WHITE);
        ContinueButton.setBorder(BorderFactory.createRaisedBevelBorder());
        ContinueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.io.File save = new java.io.File("Save/board.csv");
                if (!save.exists()) {
                    JOptionPane.showMessageDialog(null, "No saved game found!");
                    new GameWindow(false);
                } else {
                    new GameWindow(true);
                 }
                dispose();
            }
        });

        // ปุ่ม Exit
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 30));
        exitButton.setBackground(new Color(255, 69, 0)); // สีส้ม
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // วางตำแหน่งปุ่มแบบพิกัด (x, y, width, height)
        playButton.setBounds(520, 795, 250, 70);
        ContinueButton.setBounds(830, 795, 250, 70);
        exitButton.setBounds(1135, 795, 250, 70);

        // เพิ่มปุ่มทั้งหมดลงใน JPanel สำหรับปุ่ม
        buttonPanel.add(playButton);
        buttonPanel.add(ContinueButton);
        buttonPanel.add(exitButton);

        // เพิ่ม buttonPanel ลงใน backgroundPanel
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
