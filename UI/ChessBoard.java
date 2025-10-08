package UI;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.List;
import Logic.*;
import javax.swing.*;
import javax.swing.text.*;
import End.*;
import Menu.*;
import java.io.*;

public class ChessBoard extends JPanel{
    private final JButton[][] squares = new JButton[8][8];
    private final ChessPiece[][] board = new ChessPiece[8][8];
    private Point selected = null;
    private boolean gameOver = false;

    // ใช้ GameClock แทนตัวแปรเวลาเดิม
    private GameClock gameClock;

    // ดูว่าเป็นเทิร์นของใคร
    private ChessPiece.Color currentTurn = ChessPiece.Color.WHITE;

    private static final Color BOARD_DARK  = new Color(36, 40, 48);  //#242830
    private static final Color BOARD_LIGHT = new Color(66, 74, 86);  //#424A56
    private static final Color ACCENT      = new Color(0, 189, 166); // teal
    private static final Color ACCENT_SOFT = new Color(0, 189, 166, 60); // teal โปร่ง
    

    // Player UI
    private JLabel player1Active;
    private JLabel player2Active;
    private JTextPane logArea;

      public ChessBoard(JLabel player1Active, JLabel timer1Label,JLabel player2Active, JLabel timer2Label,JTextPane logArea) {

        this.player1Active = player1Active;
        this.player2Active = player2Active;
        this.logArea = logArea;
       
        // สร้าง GameClock (600 วินาที = 10 นาที)
        this.gameClock = new GameClock(600, timer1Label, timer2Label);
        this.gameClock.startClock();
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
                bt.setBackground(((r + c) % 2 == 0) ? BOARD_LIGHT : BOARD_DARK);
                bt.setBorderPainted(true);
                bt.setBorder(BorderFactory.createMatteBorder(1,1,1,1,new Color(0,0,0,50)));
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
        List<Point> legalMoves = (selected != null) ? Move.getLegalMoves(board, selected.x, selected.y) : null;
        Point inCheck = Move.getCheckSquare(board, currentTurn);
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                JButton btn = squares[r][c];
                ChessPiece piece = board[r][c];
                // if this square is a legal move destination and empty, show a dot
                boolean isLegal = (legalMoves != null && legalMoves.contains(new Point(r, c)));
                if (isLegal && piece == null) {
                    btn.setText("•");
                    btn.setForeground(Color.YELLOW);
                } else {
                    if (piece == null) {
                        btn.setText("");
                    } else {
                        btn.setText(piece.getUnicode());
                    }
                }

                // พื้นหลังแล้ว ถ้าเป็นช่องที่เลือกไว้ให้ใช้สีไฮไลท์
                Color bg;
                if ((r + c) % 2 == 0) {
                    bg = BOARD_LIGHT;
                } else {
                    bg = BOARD_DARK;
                }
                if (selected != null && selected.x == r && selected.y == c) {
                    bg = ACCENT;
                } else if (isLegal && piece != null) {
                    // legal capture target
                    bg = ACCENT_SOFT;
                }
                if (inCheck != null && r == inCheck.x && c == inCheck.y) {
                    bg = new Color(200, 0, 0); // ไฮไลท์แดงเมื่อราชาถูกรุก
                }

                btn.setBackground(bg);
                // don't override a legal-move dot's color
                if (!(isLegal && piece == null)) {
                    btn.setForeground(
                        piece != null && piece.getColor() == ChessPiece.Color.WHITE
                            ? Color.WHITE
                            : Color.BLACK
                    );
                }
            }
        }
        gameClock.switchTurn(currentTurn); //  แจ้งให้ clock รู้ว่าฝั่งไหนต้องเดิน

    }
    
    private static String toSquare(int r, int c) {
    // r: 0..7 (บน->ล่าง = 8..1), c: 0..7 (ซ้าย->ขวา = a..h)
    char file = (char) ('a' + c);
    int rank = 8 - r;
    return "" + file + rank;
}

    private static String pieceGlyph(ChessPiece p) {
    if (p == null) return "?";
    switch (p.getType()) {
        case KING:   return "\u265A"; // ♚
        case QUEEN:  return "\u265B"; // ♛
        case ROOK:   return "\u265C"; // ♜
        case BISHOP: return "\u265D"; // ♝
        case KNIGHT: return "\u265E"; // ♞
        case PAWN:   return "\u265F"; // ♟
        default:     return "";
    }
}
    private static void appendStyled(JTextPane pane, String text, Color color, boolean bold) {
            StyledDocument doc = pane.getStyledDocument();
            SimpleAttributeSet attr = new SimpleAttributeSet();
        if (color != null) StyleConstants.setForeground(attr, color);
            StyleConstants.setBold(attr, bold);
        try {
            doc.insertString(doc.getLength(), text, attr);
        } catch (BadLocationException ignored) {}
        pane.setCaretPosition(doc.getLength());
    }

    private void logMove(ChessPiece mover, int rFrom, int cFrom, int rTo, int cTo, boolean isCapture) {
        if (logArea == null || mover == null) return;
            String glyph = pieceGlyph(mover);
            Color base = logArea.getForeground();
        if (!glyph.isEmpty()) {
            Color pieceColor = (mover.getColor() == ChessPiece.Color.WHITE) ? Color.WHITE : Color.BLACK;
            appendStyled(logArea, glyph + " ", pieceColor, true);
        }
        String mid = isCapture ? "x" : "→";  // ถ้ากิน = x, ถ้าไม่กิน = →
        appendStyled(logArea, toSquare(rFrom, cFrom) +  mid  + toSquare(rTo, cTo) + "\n", base, false);
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

    private void enableBoard() {
    for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
            if (squares[r][c] != null) squares[r][c].setEnabled(true);
        }
    }
}

/** เริ่มเกมใหม่ (เรียกจากปุ่มใน GameWindow) */
public void restartGame() {
    // reset state
    for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
            board[r][c] = null;
        }
    }
    selected = null;
    currentTurn = ChessPiece.Color.WHITE;
    gameOver = false;

    // ตั้งหมากใหม่ + เปิดกระดาน + อัปเดต UI
    initStartingPosition();
    enableBoard();
    updatePlayerLabelStyles();
    refreshBoard();

    // รีเซ็ตนาฬิกา
    if (gameClock != null) {
        gameClock.resetClock(600); // 10 นาทีใหม่ (ปรับได้)
        gameClock.startClock();
        gameClock.switchTurn(currentTurn);
        }
    }

    private void onSquareClicked(int r, int c) {
        // แสดงข้อมูลการคลิกว่าเป็นช่องไหน มีหมากอะไร และเทิร์นของใคร
        if (gameOver) return;
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
            // อนุญาตให้เลือกหมากได้เฉพาะของฝ่ายที่เป็นเทิร์นอยู่
            if (board[r][c].getColor() == currentTurn) {
                selected = new Point(r, c);
            }
        }
    // ถ้าคลิกซ้ำที่เดิม → ยกเลิก
        else if (selected != null && selected.x == r && selected.y == c) {
            selected = null;
        }
    // ถ้าเลือกหมากแล้ว และกดช่องอื่น → ย้าย (เฉพาะถ้าเป็นช่องที่ถูกไฮไลท์)
        else if (selected != null) {
            ChessPiece from = board[selected.x][selected.y];
            ChessPiece to = board[r][c];
            // ถ้าช่องปลายทางมีหมากของฝ่ายเดียวกัน -> เปลี่ยน selection ไปยังช่องนั้น (เลือกหมากใหม่)
            if (to != null && from != null && to.getColor() == from.getColor()) {
                selected = new Point(r, c);
            } else {
                // Only move if destination is a legal move for the selected piece
                List<Point> legalMoves = Move.getLegalMoves(board, selected.x, selected.y);
                boolean isLegal = false;
                for (Point p : legalMoves) {
                    if (p.x == r && p.y == c) { isLegal = true; break; }
                }
                if (isLegal) {
                    board[r][c] = from;
                    board[selected.x][selected.y] = null;
                    ChessPiece mover = from;
                    boolean isCapture = (to != null);
                    logMove(mover, selected.x, selected.y, r, c, isCapture);
                    selected = null;
                    // เปลี่ยนเทิร์นหลังย้ายหมากสำเร็จ
                    if (currentTurn == ChessPiece.Color.WHITE) {
                        currentTurn = ChessPiece.Color.BLACK;
                    } else {
                        currentTurn = ChessPiece.Color.WHITE;
                    }
                    updatePlayerLabelStyles();
                // ประเมินสถานะเกมจากมุมมองฝั่งที่จะเดินถัดไป
                } else {
                    // click on non-legal square -> ignore (keep selection)
                }
                Move.GameState state = Move.evaluateState(board, currentTurn);
                switch (state) {
			case CHECKMATE: {
                gameClock.stopClock(); // หยุดนาฬิกาเมื่อเกมจบ
                gameOver = true; // กันไม่ให้กดต่อ
                java.awt.Window win = SwingUtilities.getWindowAncestor(this);
                if (win != null) win.dispose(); // ปิดหน้าต่างเกมเดิม (GameWindow)
                new EndGameWindow();
				break;
			}
			case STALEMATE: {
                gameOver = true;
                java.awt.Window win = SwingUtilities.getWindowAncestor(this);
                if (win != null) win.dispose();
                gameClock.stopClock();
				new DrawGameWindow();
				break;
			}
			case CHECK: {
				// ราชาถูกเช็ค (ไฮไลท์ใน refreshBoard()
				break;
			}
			case NORMAL:
				// เดินต่อได้ตามปกติ
				break;
		}

            }
        }
    refreshBoard(); // อัพเดท UI ทุกครั้ง
    }

    public void saveGame(File file) {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
        out.writeObject(board); // board must be Serializable
        out.writeObject(gameClock.getTimes()); // Save both timers (implement getTimes() in GameClock)
        out.writeObject(currentTurn);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void loadGame(File file) {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
        ChessPiece[][] loadedBoard = (ChessPiece[][]) in.readObject();
        int[] times = (int[]) in.readObject();
        ChessPiece.Color loadedTurn = (ChessPiece.Color) in.readObject();
        System.arraycopy(loadedBoard, 0, board, 0, board.length);
        gameClock.setTimes(times); // implement setTimes(int[]) in GameClock
        currentTurn = loadedTurn;
        refreshBoard();
        updatePlayerLabelStyles();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}

// Enable or disable the board for moves
public void setBoardEnabled(boolean enabled) {
    for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
            squares[r][c].setEnabled(enabled);
        }
    }
}

// Pause the game clock
public void pauseClock() {
    if (gameClock != null) {
        gameClock.pause();
    }
}

// Resume the game clock
public void resumeClock() {
    if (gameClock != null) {
        gameClock.resume();
    }
}
}
