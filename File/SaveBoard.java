package File;

import UI.ChessBoard;
import Menu.GameClock;
import UI.ChessPiece;

import java.io.*;
import java.nio.file.*;

public class SaveBoard {

    // --- Save Binary (Serializable) แบบ atomic write ---
    public static boolean save(ChessBoard board, GameClock clock, String filename) {
        Path temp = Paths.get(filename + ".tmp");
        Path target = Paths.get(filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(temp))) {
            oos.writeObject(board.getBoard());       // บอร์ด
            oos.writeObject(board.getCurrentTurn()); // เทิร์น
            oos.writeInt(clock.getTimeWhite());      // เวลาฝ่ายขาว
            oos.writeInt(clock.getTimeBlack());      // เวลาฝ่ายดำ
            oos.flush();
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Load Binary ---
    public static boolean load(ChessBoard board, GameClock clock, String filename) {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) return false;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            ChessPiece[][] b = (ChessPiece[][]) ois.readObject();
            ChessPiece.Color turn = (ChessPiece.Color) ois.readObject();
            int tWhite = ois.readInt();
            int tBlack = ois.readInt();

            board.setBoard(b);
            board.setCurrentTurn(turn);
            clock.resetClockWithTime(tWhite, tBlack, turn);
            board.refreshBoard();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
