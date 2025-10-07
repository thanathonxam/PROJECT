package Menu;
import javax.swing.*;
import java.awt.*;
import UI.*;
import setBackgroud.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("CHESS GAME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        //สร้าง icon เเละแสดงผล
        ImageIcon icon = new ImageIcon("Image/icon.png");
        setIconImage(icon.getImage());

        // --- Panel ข้างๆ (พื้นหลังโทนเข้ม) ---
        JPanel sidePanel = new BackgroundPanel("Image/sidepanel.png");
        sidePanel.setOpaque(false);              // สำคัญ: ต้องโปร่งใส
        sidePanel.setPreferredSize(new Dimension(300, 1080));
        sidePanel.setLayout(null);                  // สำคัญ: ใช้ null layout ที่ panel นี้

        add(sidePanel, BorderLayout.WEST);

        // --- Player Labels ---
        JLabel player1Name   = new JLabel("PLAYER 1", SwingConstants.LEFT);
        JLabel player1Color  = new JLabel("(WHITE)",  SwingConstants.LEFT);
        JLabel player1Active = new JLabel("ACTIVE",   SwingConstants.LEFT);
        JLabel timer1Label   = new JLabel("10:00",    SwingConstants.LEFT);

        JLabel player2Name   = new JLabel("PLAYER 2", SwingConstants.LEFT);
        JLabel player2Color  = new JLabel("(BLACK)",  SwingConstants.LEFT);
        JLabel player2Active = new JLabel("ACTIVE",   SwingConstants.LEFT);
        JLabel timer2Label   = new JLabel("10:00",    SwingConstants.LEFT);

        // ฟอนต์/สีให้ตัดกับพื้นหลังเข้ม
        Color text = new Color(220,230,240);
        Color sub  = new Color(170,180,190);
        player1Name.setFont(new Font("", Font.BOLD, 28));
        player2Name.setFont(new Font("", Font.BOLD, 28));
        player1Color.setFont(new Font("", Font.PLAIN, 24));
        player2Color.setFont(new Font("", Font.PLAIN, 24));
        player1Active.setFont(new Font("", Font.BOLD, 22));
        player2Active.setFont(new Font("", Font.BOLD, 22));
        timer1Label.setFont(new Font("", Font.PLAIN, 24));
        timer2Label.setFont(new Font("", Font.PLAIN, 24));
        player1Name.setForeground(text);
        player2Name.setForeground(text);
        player1Color.setForeground(sub);
        player2Color.setForeground(sub);
        timer1Label.setForeground(text);
        timer2Label.setForeground(text);
        player1Active.setForeground(new Color(0, 220, 80)); // เขียว
        player2Active.setForeground(new Color(0, 220, 80));
        player1Active.setVisible(false);
        player2Active.setVisible(false);

        // --- Game Log ---
        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font("", Font.PLAIN, 20));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setForeground(text);
        messageArea.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createTitledBorder("GAME LOG"));

        // ====== พิกัดที่ตรงกับกรอบในภาพพื้นหลัง (300x1080) ======

        int x = 24;
        int w = 252;
        int topY = 24;
        int topH = 210;
        int gap = 10;
        int botH = 210;
        int botY = 1080 - 24 - botH;         // 846
        int logY = topY + topH + gap;        // 244
        int logH = botY - gap - logY;        // 592

        // ====== แผงย่อยโปร่งใส 3 ตัว ======
        JPanel topPanel = new JPanel(null);     // null เพื่อใช้ setBounds ของ label ภายใน
        topPanel.setOpaque(false);
        topPanel.setBounds(x, topY, w, topH);
        sidePanel.add(topPanel);

        JPanel logPanel = new JPanel(null);
        logPanel.setOpaque(false);
        logPanel.setBounds(x, logY, w, logH);
        sidePanel.add(logPanel);

        JPanel bottomPanel = new JPanel(null);
        bottomPanel.setOpaque(false);
        bottomPanel.setBounds(x, botY, w, botH);
        sidePanel.add(bottomPanel);

        // ====== ใส่คอมโพเนนต์ลงแต่ละกรอบให้ตรง ======
        // TOP: PLAYER 2 (กรอบบน)
        player2Name.setBounds(10,  8, w-20, 36);
        player2Color.setBounds(10, 44, w-20, 26);
        player2Active.setBounds(10, 74, w-20, 26);
        timer2Label.setBounds(10, 104, w-20, 30);
        topPanel.add(player2Name);
        topPanel.add(player2Color);
        topPanel.add(player2Active);
        topPanel.add(timer2Label);

        // LOG: กลาง
        scrollPane.setBounds(0, 0, w, logH);
        logPanel.add(scrollPane);

        // BOTTOM: PLAYER 1 (กรอบล่าง)
        player1Name.setBounds(10,  8,  w-20, 36);
        player1Color.setBounds(10, 44, w-20, 26);
        player1Active.setBounds(10, 74, w-20, 26);
        timer1Label.setBounds(10, 104, w-20, 30);
        bottomPanel.add(player1Name);
        bottomPanel.add(player1Color);
        bottomPanel.add(player1Active);
        bottomPanel.add(timer1Label);

        // --- กระดานหมากรุกตรงกลาง ---
        ChessBoard chessBoard = new ChessBoard(
                player1Active, timer1Label,
                player2Active, timer2Label
        );
        add(chessBoard, BorderLayout.CENTER);

        setVisible(true);
    }
}
