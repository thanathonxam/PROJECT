package UI;
import java.awt.*;
import javax.swing.*;

public class ChessBoard extends JPanel {
    private final JButton[][] squares = new JButton[8][8];

    private final Color light = Color.PINK;
    private final Color dark = Color.GRAY;

    public ChessBoard() {
        setLayout(new GridLayout(8, 8));
        Font pieceFont = new Font("chess", Font.PLAIN, 50);

        // สร้างปุ่ม 8x8 (สลับสี)
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                JButton bt = new JButton();
                bt.setFont(pieceFont);
                bt.setFocusPainted(false);
                bt.setOpaque(true);
                bt.setBorderPainted(false);
                // สลับสีช่อง
                bt.setBackground(((r + c) % 2 == 0) ? light : dark);

                squares[r][c] = bt;
                add(bt);
            }
        }
    }


}