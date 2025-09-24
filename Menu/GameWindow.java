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

        // --- ChessBoard ตรงกลาง ---
        ChessBoard chessBoard = new ChessBoard();
        add(chessBoard, BorderLayout.CENTER);

        // --- Panel ข้างๆ ---
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(160, 640));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        JLabel player1 = new JLabel("Player 1");
        JLabel timer1 = new JLabel("Time: 10:00");
        JLabel player2 = new JLabel("Player 2");
        JLabel timer2 = new JLabel("Time: 10:00");

        player1.setAlignmentX(Component.CENTER_ALIGNMENT);
        timer1.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2.setAlignmentX(Component.CENTER_ALIGNMENT);
        timer2.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(player1);
        sidePanel.add(timer1);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(player2);
        sidePanel.add(timer2);
        sidePanel.add(Box.createVerticalStrut(20));

        add(sidePanel, BorderLayout.WEST);
        setVisible(true);
    }
}
