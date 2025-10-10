package Menu;
import java.awt.Window;
import java.awt.event.*;
import javax.swing.*;
import Start.Start;
import UI.*;

public class GameClock {
    private int timeWhite;   // เวลาของผู้เล่นขาว (วินาที)
    private int timeBlack;   // เวลาของผู้เล่นดำ (วินาที)
    private JLabel whiteLabel;
    private JLabel blackLabel;
    private Timer clockTimer;
    private boolean gameEnded = false;
    private ChessPiece.Color currentTurn;

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
                    timeoutGoToStart("White time out! Black wins!");
                }
            } else {
                timeBlack--;
                blackLabel.setText(formatTime(timeBlack));
                if (timeBlack <= 0) {
                    timeoutGoToStart("Black time out! White wins!");
                }
            }
        }
    });
        clockTimer.start();
    }
    private void timeoutGoToStart(String msg) {
        gameEnded = true;           // กันไม่ให้ actionPerformed ทำงานต่อ
            if (clockTimer != null) {
                clockTimer.stop();
            }
    JOptionPane.showMessageDialog(null, msg);

    // ปิดหน้าต่างเกม (หา window จาก label ไหนก็ได้ที่อยู่ใน GameWindow)
    Window w = SwingUtilities.getWindowAncestor(
        (currentTurn == ChessPiece.Color.WHITE) ? whiteLabel : blackLabel
    );
    if (w != null) w.dispose();

    // เปิดหน้า Start บน EDT
        SwingUtilities.invokeLater(() -> {
        new Start().setVisible(true);   // ถ้าไม่มี Start.open() ใช้ new Start().setVisible(true);
    });
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

    public int[] getTimes() {
        return new int[] { timeWhite, timeBlack };
    }

    public void setTimes(int[] times) {
        timeWhite = times[0];
        timeBlack = times[1];
        if (whiteLabel != null) whiteLabel.setText(formatTime(timeWhite));
        if (blackLabel != null) blackLabel.setText(formatTime(timeBlack));
    }
    public void pause() {
        clockTimer.stop();
    }
    public void resume() {
        clockTimer.start();
    }
}
