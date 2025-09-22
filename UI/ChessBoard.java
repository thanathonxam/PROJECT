package UI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessBoard extends JPanel {
    private final JButton[][] squares = new JButton[8][8];
    private final ChessPiece[][] board = new ChessPiece[8][8];
    private Point selected = null;
    // track whose turn it is; Player 1 will be WHITE
    private ChessPiece.Color currentTurn = ChessPiece.Color.WHITE;

    private final Color light = new Color(181, 136, 99);
    private final Color dark = new Color(240, 217, 181);
    private final Color select = new Color(250, 200, 0);
    private JLabel turnLabel = null;
    private JLabel player1Label = null;
    private JLabel player2Label = null;

    public ChessBoard() { this(null, null); }

    public ChessBoard(JLabel player1Label, JLabel player2Label) {
        this.player1Label = player1Label;
        this.player2Label = player2Label;
        // initialize player label styles according to currentTurn
        updatePlayerLabelStyles();
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
                // พื้นหลังแล้ว ถ้าเป็นช่องที่เลือกไว้ให้ใช้สีไฮไลท์
                Color bg = ((r + c) % 2 == 0) ? light : dark;
                if (selected != null && selected.x == r && selected.y == c) {
                    bg = select;
                }
                btn.setBackground(bg);
                btn.setForeground(piece != null && piece.getColor() == ChessPiece.Color.WHITE ? Color.BLACK : Color.WHITE);
                // highlight all pieces belonging to the current player so it's obvious who can move
                if (piece != null && piece.getColor() == currentTurn) {
                    btn.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                } else {
                    btn.setBorder(null);
                }
            }
        }
    }

    // update UI styles for player labels so it's clear whose turn it is
    public void updatePlayerLabelStyles() {
        if (player1Label == null || player2Label == null) return;
        Font normal = player1Label.getFont().deriveFont(Font.PLAIN);
        Font bold = player1Label.getFont().deriveFont(Font.BOLD);
        if (currentTurn == ChessPiece.Color.BLACK) {
            player1Label.setFont(bold);
            player2Label.setFont(normal);
            player1Label.setText("Player 1 (White) - Active");
            player2Label.setText("Player 2 (Black)");
        } else {
            player1Label.setFont(normal);
            player2Label.setFont(bold);
            player1Label.setText("Player 1 (White)");
            player2Label.setText("Player 2 (Black) - Active");
        }
        
        // debug: print identity hashes so we can verify same instances
        System.out.println("[ChessBoard] updatePlayerLabelStyles: currentTurn=" + currentTurn);
        System.out.println("[ChessBoard] player1Label id=" + System.identityHashCode(player1Label) + " text='" + player1Label.getText() + "'");
        System.out.println("[ChessBoard] player2Label id=" + System.identityHashCode(player2Label) + " text='" + player2Label.getText() + "'");
        if (turnLabel != null) System.out.println("[ChessBoard] turnLabel id=" + System.identityHashCode(turnLabel) + " text='" + turnLabel.getText() + "'");
        // ensure UI repaint
        player1Label.revalidate(); player1Label.repaint();
        player2Label.revalidate(); player2Label.repaint();
        if (turnLabel != null) { turnLabel.revalidate(); turnLabel.repaint(); }
    }

    private void onSquareClicked(int r, int c) {
        // debug: print clicked square, piece there, and whose turn it is
        ChessPiece clicked = board[r][c];
        System.out.println("Clicked (" + r + "," + c + ") piece=" + (clicked==null?"null":clicked.getType()+"/"+clicked.getColor()) + " currentTurn=" + currentTurn);
    // ถ้ายังไม่ได้เลือกหมาก
        if (selected == null && board[r][c] != null) {
            // only allow selecting pieces of the side whose turn it is
            if (board[r][c].getColor() == currentTurn) {
                selected = new Point(r, c);
            }
        }
    // ถ้าคลิกซ้ำที่เดิม → ยกเลิก
        else if (selected != null && selected.x == r && selected.y == c) {
            selected = null;
        }
    // ถ้าเลือกหมากแล้ว และกดช่องอื่น → ย้าย
        else if (selected != null) {
            ChessPiece from = board[selected.x][selected.y];
            ChessPiece to = board[r][c];
            // ถ้าช่องปลายทางมีหมากของฝ่ายเดียวกัน -> เปลี่ยน selection ไปยังช่องนั้น (เลือกหมากใหม่)
            if (to != null && from != null && to.getColor() == from.getColor()) {
                selected = new Point(r, c);
            } else {
                board[r][c] = from;
                board[selected.x][selected.y] = null;
                selected = null;
                // toggle turn after a successful move
                currentTurn = (currentTurn == ChessPiece.Color.WHITE) ? ChessPiece.Color.BLACK : ChessPiece.Color.WHITE;
                
                updatePlayerLabelStyles();
            }
        }
    refreshBoard(); // อัพเดท UI ทุกครั้ง
    }
}

