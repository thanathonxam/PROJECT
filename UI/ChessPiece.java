package UI;

public class ChessPiece {
    public enum Color { BLACK, WHITE }
    public enum Type { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN }

    private final Color color;
    private final Type type;

    public ChessPiece(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() { return color; }
    public Type getType() { return type; }

    public String getUnicode() {
        switch (type) {
            case KING:   return color == Color.WHITE ? "\u265A" : "\u265A";
            case QUEEN:  return color == Color.WHITE ? "\u265B" : "\u265B";
            case ROOK:   return color == Color.WHITE ? "\u265C" : "\u265C";
            case BISHOP: return color == Color.WHITE ? "\u265D" : "\u265D";
            case KNIGHT: return color == Color.WHITE ? "\u265E" : "\u265E";
            case PAWN:   return color == Color.WHITE ? "\u265F" : "\u265F";
        }
        return "";
    }
}