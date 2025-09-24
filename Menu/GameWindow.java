package Menu;
import javax.swing.*;
import java.awt.*;
import UI.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("CHESS GAME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080); 
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- Panel ข้างๆ ---
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(300, 1080));
        sidePanel.setLayout(null); // ใช้ absolute layout
        sidePanel.setBackground(new Color(230, 230, 230));

        // --- Player Labels ---
        JLabel player1Name = new JLabel("Player 1 (White)", SwingConstants.CENTER);
        JLabel player1Active = new JLabel("Active", SwingConstants.CENTER);
        JLabel timer1Label = new JLabel("10:00", SwingConstants.CENTER);

        JLabel player2Name = new JLabel("Player 2 (Black)", SwingConstants.CENTER);
        JLabel player2Active = new JLabel("Active", SwingConstants.CENTER);
        JLabel timer2Label = new JLabel("10:00", SwingConstants.CENTER);

        // ฟอนต์
        player1Name.setFont(new Font("", Font.BOLD, 28));
        player2Name.setFont(new Font("", Font.BOLD, 28));
        player1Active.setFont(new Font("", Font.BOLD, 22));
        player2Active.setFont(new Font("", Font.BOLD, 22));
        player1Active.setForeground(Color.GREEN);
        player2Active.setForeground(Color.GREEN);
        player1Active.setVisible(false);
        player2Active.setVisible(false);

        timer1Label.setFont(new Font("", Font.PLAIN, 24));
        timer2Label.setFont(new Font("", Font.PLAIN, 24));

        // Game Log
        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font("", Font.PLAIN, 20));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("GAME LOG"));

        // --- ChessBoard ตรงกลาง ---
        ChessBoard chessBoard = new ChessBoard(
            player1Name, player1Active, timer1Label,
            player2Name, player2Active, timer2Label
        );
        add(chessBoard, BorderLayout.CENTER);

        // Fix ตำแหน่ง (x, y, width, height)
        player2Name.setBounds(30, 20, 240, 50);
        player2Active.setBounds(30, 70, 240, 30);
        timer2Label.setBounds(30, 110, 240, 40);

        scrollPane.setBounds(10, 300, 280, 400);

        player1Name.setBounds(30, 800, 240, 50);
        player1Active.setBounds(30, 850, 240, 30);
        timer1Label.setBounds(30, 890, 240, 40);

        // --- ใส่ component ลง sidePanel ---
        sidePanel.add(player1Name);
        sidePanel.add(player1Active);
        sidePanel.add(timer1Label);
        sidePanel.add(player2Name);
        sidePanel.add(player2Active);
        sidePanel.add(timer2Label);
        sidePanel.add(scrollPane);

        add(sidePanel, BorderLayout.WEST);
        setVisible(true);

        // sync สถานะตอนเริ่มเกม
        chessBoard.updatePlayerLabelStyles();
    }
}
