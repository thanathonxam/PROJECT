package UI;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;
import Menu.*;

public class ChessBoard extends JPanel {
    private final JButton[][] squares = new JButton[8][8];
    private final ChessPiece[][] board = new ChessPiece[8][8];
    private Point selected = null;

    // ใช้ GameClock แทนตัวแปรเวลาเดิม
    private GameClock gameClock;

    // track whose turn it is; Player 1 will be WHITE
    private ChessPiece.Color currentTurn = ChessPiece.Color.WHITE;

    private final Color light = new Color(181, 136, 99);
    private final Color dark = new Color(240, 217, 181);
    private final Color select = new Color(250, 200, 0);

    private JLabel player1Label = null;
    private JLabel player2Label = null;

    // Player UI
    private JLabel player1Name, player1Active, timer1Label;
    private JLabel player2Name, player2Active, timer2Label;

      public ChessBoard(JLabel player1Name, JLabel player1Active, JLabel timer1Label,JLabel player2Name, JLabel player2Active, JLabel timer2Label) {
        this.player1Name = player1Name;
        this.player1Active = player1Active;
        this.timer1Label = timer1Label;

        this.player2Name = player2Name;
        this.player2Active = player2Active;
        this.timer2Label = timer2Label;

        // สร้าง GameClock (600 วินาที = 10 นาที)
        this.gameClock = new GameClock(600, timer1Label, timer2Label);
        this.gameClock.startClock();

        // initialize player label styles
        updatePlayerLabelStyles();

        setLayout(new GridLayout(8, 8));
        Font pieceFont = new Font("chess", Font.PLAIN, 100);

        // สร้างปุ่ม 8x8 (สลับสี)
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                JButton bt = new JButton();
                bt.setFont(pieceFont);
                bt.setFocusPainted(false);
                bt.setOpaque(true);
                bt.setBorderPainted(false);
                final int rr = r, cc = c;
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
    // Black pieces (ด้านบน)
    board[0][0] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.ROOK);
    board[0][1] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.KNIGHT);
    board[0][2] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.BISHOP);
    board[0][3] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.QUEEN);
    board[0][4] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.KING);
    board[0][5] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.BISHOP);
    board[0][6] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.KNIGHT);
    board[0][7] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.ROOK);
    for (int c = 0; c < 8; c++) {
        board[1][c] = new ChessPiece(ChessPiece.Color.BLACK, ChessPiece.Type.PAWN);
    }

    // White pieces (ด้านล่าง)
    for (int c = 0; c < 8; c++) {
        board[6][c] = new ChessPiece(ChessPiece.Color.WHITE, ChessPiece.Type.PAWN);
    }
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
                if (piece == null) {
                        btn.setText("");
                     } else {
                             btn.setText(piece.getUnicode());
                        }

                // พื้นหลังแล้ว ถ้าเป็นช่องที่เลือกไว้ให้ใช้สีไฮไลท์
                Color bg;
                if ((r + c) % 2 == 0) {
                    bg = light;
                    } else {
                         bg = dark;
                        }
                if (selected != null && selected.x == r && selected.y == c) {
                    bg = select;
                }
                btn.setBackground(bg);
                btn.setForeground(
                    piece != null && piece.getColor() == ChessPiece.Color.WHITE 
                        ? Color.WHITE   // White → สีขาว
                        : Color.BLACK   // Black → สีดำ
                    );
                // highlight all pieces belonging to the current player so it's obvious who can move
                if (piece != null && piece.getColor() == currentTurn) {
                    btn.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                } else {
                    btn.setBorder(null);
                }
            }
        }
        gameClock.switchTurn(currentTurn); //  แจ้งให้ clock รู้ว่าฝั่งไหนต้องเดิน

    }

    // อัปเดตสไตล์ของป้ายชื่อผู้เล่น
    public void updatePlayerLabelStyles() {
        if (currentTurn == ChessPiece.Color.WHITE) {
            player1Active.setVisible(true);
            player2Active.setVisible(false);
        } else {
            player1Active.setVisible(false);
            player2Active.setVisible(true);
        }
    }

    private void onSquareClicked(int r, int c) {
        // debug: print clicked square, piece there, and whose turn it is
        ChessPiece clicked = board[r][c];
        String pieceInfo;
        if (clicked == null) {
                pieceInfo = "none";
            } else {
                String type = Objects.toString(clicked.getType(), "unknown");
                String color = Objects.toString(clicked.getColor(), "unknown");
                pieceInfo = type + "/" + color;
            }
            System.out.println(String.format("Clicked (%d,%d) piece=%s currentTurn=%s", r, c, pieceInfo, currentTurn));
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
                // เปลี่ยนเทิร์นหลังย้ายหมากสำเร็จ
                if (currentTurn == ChessPiece.Color.WHITE) {
                        currentTurn = ChessPiece.Color.BLACK;
                    } else {
                         currentTurn = ChessPiece.Color.WHITE;
                                }
                updatePlayerLabelStyles();
            }
        }
    refreshBoard(); // อัพเดท UI ทุกครั้ง
    }
}