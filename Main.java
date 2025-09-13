import javax.swing.*;
import UI.*;

public class Main {
    public static void main(String[] args) {
            JFrame frame = new JFrame("CHESS GAME");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ChessBoard());
            frame.setSize(640, 640);
            frame.setLocationRelativeTo(null); // แสดงตรงกลางหน้าจอ
            frame.setResizable(false);
            frame.setVisible(true); 
    }
}