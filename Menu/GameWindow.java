package Menu;
import javax.swing.*;
import java.awt.*;
import UI.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("CHESS GAME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 640); 
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- Panel ข้างๆ ---
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(160, 640));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        // --- Player Labels ---
        JLabel player1 = new JLabel();
        JLabel timer1Label = new JLabel("10:00");
        JLabel player2 = new JLabel();
        JLabel timer2Label = new JLabel("10:00");

        
        // ใช้เมธอดใหม่ในการสร้าง Panel สำหรับผู้เล่น
        JPanel player1Panel = createPlayerPanel("Player 1", "White", player1, timer1Label);
        JPanel player2Panel = createPlayerPanel("Player 2", "Black", player2, timer2Label);

    
        // --- ChessBoard ตรงกลาง ---
        ChessBoard chessBoard = new ChessBoard(player1, player2, timer1Label, timer2Label);
        add(chessBoard, BorderLayout.CENTER);

        // --- จัดวางใน sidePanel ---
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(player1Panel);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(player2Panel);
        sidePanel.add(Box.createVerticalStrut(20));

        add(sidePanel, BorderLayout.WEST);
        setVisible(true);

        // debug: print label instance ids
        System.out.println("[GameWindow] player1 id=" + System.identityHashCode(player1) + " text='" + player1.getText() + "'");
        System.out.println("[GameWindow] player2 id=" + System.identityHashCode(player2) + " text='" + player2.getText() + "'");

        // sync สถานะตอนเริ่มเกม
        chessBoard.updatePlayerLabelStyles();
    }

    // เมธอดใหม่: สำหรับสร้าง panel ของผู้เล่น (เพิ่มมาใหม่ทั้งหมด)
    private JPanel createPlayerPanel(String name, String color, JLabel nameLabel, JLabel timerLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);

        // ใช้ HTML เพื่อขึ้นบรรทัดใหม่
        nameLabel.setText("<html><div style='text-align: center;'>" + name + "<br>(" + color + ")</div></html>");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(timerLabel);
        return panel;
    }
}
