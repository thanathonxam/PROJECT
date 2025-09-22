package UI;

public class ChessPiece {
    public enum Color { WHITE, BLACK }
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
            case KING:   return color == Color.BLACK ? "\u2654" : "\u265A";
            case QUEEN:  return color == Color.BLACK ? "\u2655" : "\u265B";
            case ROOK:   return color == Color.BLACK ? "\u2656" : "\u265C";
            case BISHOP: return color == Color.BLACK ? "\u2657" : "\u265D";
            case KNIGHT: return color == Color.BLACK ? "\u2658" : "\u265E";
            case PAWN:   return color == Color.BLACK ? "\u2659" : "\u265F";
        }
        return "";
    }
}