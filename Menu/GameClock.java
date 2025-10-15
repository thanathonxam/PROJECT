package Menu;
import java.awt.Window;
import java.awt.event.*;
import javax.swing.*;
import UI.ChessPiece;
import End.*;

public class GameClock {
    private int timeWhite;   // เวลาของผู้เล่นขาว (วินาที)
    private int timeBlack;   // เวลาของผู้เล่นดำ (วินาที)
    private JLabel whiteLabel;
    private JLabel blackLabel;
    private Timer clockTimer;
    private boolean gameEnded = false;
    private ChessPiece.Color currentTurn;

    public ChessPiece.Color getCurrentTurn() { return currentTurn; }
    public void setCurrentTurn(ChessPiece.Color turn) { this.currentTurn = turn; }

    public GameClock(int startSeconds, JLabel whiteLabel, JLabel blackLabel) {
        this.timeWhite = startSeconds;
        this.timeBlack = startSeconds;
        this.whiteLabel = whiteLabel;
        this.blackLabel = blackLabel;
        this.currentTurn = ChessPiece.Color.WHITE; // เริ่มให้ขาวเดินก่อน
    }

    // เปลี่ยนฝั่งที่จะนับเวลา
    public void switchTurn(ChessPiece.Color turn) {
        this.currentTurn = turn;
    }
    // เริ่มจับเวลา
    public void startClock() {
    if (clockTimer != null) clockTimer.stop();

    clockTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameEnded) return; // กันยิงซ้ำ

            if (currentTurn == ChessPiece.Color.WHITE) {
                timeWhite--;
                whiteLabel.setText(formatTime(timeWhite));
                if (timeWhite <= 0) {
                    timeoutToEnd(ChessPiece.Color.BLACK, "Time out");
                }
            } else {
                timeBlack--;
                blackLabel.setText(formatTime(timeBlack));
                if (timeBlack <= 0) {
                    timeoutToEnd(ChessPiece.Color.WHITE, "Time out");
                }
            }
        }
    });
        clockTimer.start();
    }

    // เปิด EndGameWindow แล้วขึ้นผู้ชนะตามสี (ภาษาอังกฤษ)
    private void timeoutToEnd(ChessPiece.Color winner, String reason) {
        gameEnded = true;
        if (clockTimer != null) clockTimer.stop();

        // ปิดหน้าต่างเกม (หา window จาก label ไหนก็ได้ที่อยู่ใน GameWindow)
        Window w = SwingUtilities.getWindowAncestor(
            (currentTurn == ChessPiece.Color.WHITE) ? whiteLabel : blackLabel
        );
        if (w != null) w.dispose();

        // เปิด EndGameWindow พร้อมข้อความผู้ชนะ
        EndGameWindow eg = new EndGameWindow(winner, reason);
        eg.setVisible(true);
    }

    // แปลงวินาทีเป็น MM:SS
    private String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
    public void stopClock() {
    if (clockTimer != null) {
        clockTimer.stop();
        }
    }

    public void resetClock(int startSeconds) {
    this.timeWhite = startSeconds;
    this.timeBlack = startSeconds;
    this.currentTurn = ChessPiece.Color.WHITE; // เริ่มที่ขาว (ปรับได้)
    if (whiteLabel != null) whiteLabel.setText(formatTime(timeWhite));
    if (blackLabel != null) blackLabel.setText(formatTime(timeBlack));
    }

    public void setTimes(int[] times) {
        timeWhite = times[0];
        timeBlack = times[1];
        if (whiteLabel != null) whiteLabel.setText(formatTime(timeWhite));
        if (blackLabel != null) blackLabel.setText(formatTime(timeBlack));
    }

    public int[] getTimes() {
    return new int[]{ timeWhite, timeBlack };
    }

    public void pause() {
        clockTimer.stop();
    }
    
    public void resume() {
        clockTimer.start();
    }
}
