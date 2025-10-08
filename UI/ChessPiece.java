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
    if (color == Color.WHITE) {
        switch (type) {
            case KING:   return "\u265A"; 
            case QUEEN:  return "\u265B"; 
            case ROOK:   return "\u265C"; 
            case BISHOP: return "\u265D"; 
            case KNIGHT: return "\u265E"; 
            case PAWN:   return "\u265F"; 
        }
    } else { // BLACK
        switch (type) {
            case KING:   return "\u265A"; 
            case QUEEN:  return "\u265B"; 
            case ROOK:   return "\u265C"; 
            case BISHOP: return "\u265D"; 
            case KNIGHT: return "\u265E"; 
            case PAWN:   return "\u265F"; 
        }
    }
    return "";
}
}
