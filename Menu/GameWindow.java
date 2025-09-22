package Menu;
import javax.swing.*;
import java.awt.*;
import UI.*; 

public class GameWindow extends JFrame {
    private int timer1 = 600; 
    private int timer2 = 600; 
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

        // create player labels first so we can pass them to the board for dynamic updates
        JLabel player1 = new JLabel("Player 1 (White)");
        JLabel timer1Label = new JLabel("Time : "+ timer1/60 + ":" + String.format("%02d", timer1%60));
        JLabel player2 = new JLabel("Player 2 (Black)");
        JLabel timer2Label = new JLabel("Time : " + timer2/60 + ":" + String.format("%02d", timer2%60));

        player1.setAlignmentX(Component.CENTER_ALIGNMENT);
        timer1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2.setAlignmentX(Component.CENTER_ALIGNMENT);
        timer2Label.setAlignmentX(Component.CENTER_ALIGNMENT);


        // --- ChessBoard ตรงกลาง ---
        ChessBoard chessBoard = new ChessBoard( player1, player2);
        add(chessBoard, BorderLayout.CENTER);

    sidePanel.add(Box.createVerticalStrut(20));
    sidePanel.add(player1);
    sidePanel.add(timer1Label);
    sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(player2);
        sidePanel.add(timer2Label);
        sidePanel.add(Box.createVerticalStrut(20));

        add(sidePanel, BorderLayout.WEST);
        setVisible(true);
        // debug: print label instance ids so we can compare with ChessBoard
        System.out.println("[GameWindow] player1 id=" + System.identityHashCode(player1) + " text='" + player1.getText() + "'");
        System.out.println("[GameWindow] player2 id=" + System.identityHashCode(player2) + " text='" + player2.getText() + "'");
        // ensure ChessBoard has synced the styles (might be needed after visible)
        chessBoard.updatePlayerLabelStyles();
    }
}
