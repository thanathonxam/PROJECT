package UI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessBoard extends JPanel {
    private final JButton[][] squares = new JButton[8][8];
    private final ChessPiece[][] board = new ChessPiece[8][8];
    private Point selected = null;

    private final Color light = new Color(181, 136, 99);
    private final Color dark = new Color(240, 217, 181);

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
                final int rr = r, cc = c;
                // สลับสีช่อง
                bt.setBackground(((r + c) % 2 == 0) ? light : dark);
                bt.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onSquareClicked(rr, cc);
                            }
                    });
                squares[r][c] = bt;
                add(bt);
            }
        }
        initStartingPosition();
        refreshBoard();
    }

    private void initStartingPosition() {
        // black pieces
        board[0][0] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.ROOK);
        board[0][1] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.KNIGHT);
        board[0][2] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.BISHOP);
        board[0][3] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.QUEEN);
        board[0][4] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.KING);
        board[0][5] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.BISHOP);
        board[0][6] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.KNIGHT);
        board[0][7] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.ROOK);
        for (int c = 0; c < 8; c++) 
            {board[1][c] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.PAWN);}

        // white pieces
        for (int c = 0; c < 8; c++) 
            {board[6][c] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.PAWN);}
        board[7][0] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.ROOK);
        board[7][1] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.KNIGHT);
        board[7][2] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.BISHOP);
        board[7][3] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.QUEEN);
        board[7][4] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.KING);
        board[7][5] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.BISHOP);
        board[7][6] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.KNIGHT);
        board[7][7] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.ROOK);
    }

    private void refreshBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                JButton btn = squares[r][c];
                ChessPiece piece = board[r][c];
                btn.setText(piece == null ? "" : piece.getUnicode());
                btn.setBackground(((r + c) % 2 == 0) ? light : dark);
                btn.setForeground(piece != null && piece.getColor() == ChessPiece.Color.WHITE ? Color.BLACK : Color.WHITE);
                btn.setBorder(null);
            }
        }
    }

    private void onSquareClicked(int r, int c) {
    // ถ้ายังไม่ได้เลือกหมาก
        if (selected == null && board[r][c] != null) {
            selected = new Point(r, c);
        }
    // ถ้าคลิกซ้ำที่เดิม → ยกเลิก
        else if (selected != null && selected.x == r && selected.y == c) {
            selected = null;
        }
    // ถ้าเลือกหมากแล้ว และกดช่องอื่น → ย้าย
        else if (selected != null) {
            board[r][c] = board[selected.x][selected.y];
            board[selected.x][selected.y] = null;
            selected = null;
        }
    refreshBoard(); // อัพเดท UI ทุกครั้ง
    }
}

