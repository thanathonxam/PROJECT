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

        // Label หัวเรื่อง
        JLabel title = new JLabel("CHESS GAME DEMO", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 100)); // เปลี่ยนฟอนต์ให้ดูดีขึ้น
        title.setForeground(Color.WHITE); 
        backgroundPanel.add(title, BorderLayout.NORTH);

        // สร้าง JPanel สำหรับปุ่มทั้งหมด
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // ทำให้ JPanel นี้โปร่งใส
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // จัดปุ่มให้อยู่กึ่งกลางและมีช่องว่าง

        // ปุ่ม Play
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 30)); // ฟอนต์ที่ใหญ่ขึ้นและตัวหนา
        playButton.setBackground(new Color(60, 179, 113)); // สีเขียวมรกต
        playButton.setForeground(Color.WHITE); // สีตัวอักษรสีขาว
        playButton.setPreferredSize(new Dimension(250, 60)); // กำหนดขนาดปุ่ม
        playButton.setBorder(BorderFactory.createRaisedBevelBorder()); // ขอบนูน
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new GameWindow();
            }
        });

        // ปุ่ม Continue
        JButton ContinueButton = new JButton("Continue");
        ContinueButton.setFont(new Font("Arial", Font.BOLD, 30));
        ContinueButton.setBackground(new Color(30, 144, 255)); // สีน้ำเงิน
        ContinueButton.setForeground(Color.WHITE);
        ContinueButton.setPreferredSize(new Dimension(250, 60));
        ContinueButton.setBorder(BorderFactory.createRaisedBevelBorder());
        ContinueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.io.File saveFile = new java.io.File("Savefile/filegame.dat");
                if (saveFile.exists()) {
                    setVisible(false);
                    GameWindow gw = new GameWindow();
                    if (gw instanceof GameWindow) {
                        gw.loadSavedGame(saveFile);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No saved game found!");
                }
            }
        });

        // ปุ่ม Exit
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 30));
        exitButton.setBackground(new Color(255, 69, 0)); // สีส้ม
        exitButton.setForeground(Color.WHITE);
        exitButton.setPreferredSize(new Dimension(250, 60));
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // เพิ่มปุ่มทั้งหมดลงใน JPanel สำหรับปุ่ม
        buttonPanel.add(playButton);
        buttonPanel.add(ContinueButton);
        buttonPanel.add(exitButton);

        // เพิ่ม buttonPanel ลงใน backgroundPanel
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
