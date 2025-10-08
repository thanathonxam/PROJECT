package Menu;
import java.awt.event.*;
import javax.swing.*;
import UI.*;

public class GameClock {
    private int timeWhite;   // เวลาของผู้เล่นขาว (วินาที)
    private int timeBlack;   // เวลาของผู้เล่นดำ (วินาที)
    private JLabel whiteLabel;
    private JLabel blackLabel;
    private Timer clockTimer;
    private ChessPiece.Color currentTurn;

    public GameClock(int startSeconds, JLabel whiteLabel, JLabel blackLabel) {
        this.timeWhite = startSeconds;
        this.timeBlack = startSeconds;
        this.whiteLabel = whiteLabel;
        this.blackLabel = blackLabel;
        this.currentTurn = ChessPiece.Color.BLACK; // เริ่มให้ขาวเดินก่อน
    }

    // เปลี่ยนฝั่งที่จะนับเวลา
    public void switchTurn(ChessPiece.Color turn) {
        this.currentTurn = turn;
    }
    // เริ่มจับเวลา
    public void startClock() {
        if (clockTimer != null) clockTimer.stop(); //หยุด timer เก่าถ้ามีอยู่แล้ว เพื่อป้องกันการมีหลายตัวนับซ้อนกัน

    clockTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentTurn == ChessPiece.Color.WHITE) {
                timeWhite--;
                whiteLabel.setText(formatTime(timeWhite));
                if (timeWhite <= 0) {
                    JOptionPane.showMessageDialog(null, "White time out! Black wins!");
                    System.exit(0);
                }
            } else {
                timeBlack--;
                blackLabel.setText(formatTime(timeBlack));
                if (timeBlack <= 0) {
                    JOptionPane.showMessageDialog(null, "Black time out! White wins!");
                    System.exit(0);
                }
            }
        }
    });
        clockTimer.start();
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
