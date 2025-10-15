package End;
import javax.swing.*;
import Start.Start;
import java.awt.*;
import setBackgroud.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        setUndecorated(true);
        
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
            public void actionPerformed(ActionEvent evt) {
                Window w = SwingUtilities.getWindowAncestor(backButton);
                    if (w != null) w.dispose();
                        new Start();; // กลับหน้า Start
                    }
                });

        // 2. ปุ่ม Exit (ออกจากโปรแกรม)
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 30));
        exitButton.setBackground(new Color(255, 69, 0)); 
        exitButton.setForeground(Color.WHITE);
        exitButton.setPreferredSize(new Dimension(250, 60)); // กำหนดขนาดปุ่ม
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(exitButton);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        deleteSaveIfExists(); // ลบไฟล์เซฟถ้ามี
        setVisible(true);
    }

    private void deleteSaveIfExists() {
    try {
        Path savePath = Paths.get("Save", "board.csv");
        if (Files.exists(savePath)) {
            Files.delete(savePath);               // ลบไฟล์เซฟ
        }
        // (ออปชัน) ถ้าอยากลบโฟลเดอร์เมื่อว่างจริง ๆ:
        Path saveDir = Paths.get("Save");
        try {
            Files.delete(saveDir);               // จะลบได้ก็ต่อเมื่อว่าง
        } catch (IOException ignore) {
            // โฟลเดอร์ไม่ว่าง/ลบไม่ได้ก็ไม่เป็นไร
        }
    } catch (IOException e) {
        // ไม่ต้องเด้ง error ให้ผู้ใช้ แค่ log ไว้พอ
        System.err.println("Delete save failed: " + e.getMessage());
    }
}
}



