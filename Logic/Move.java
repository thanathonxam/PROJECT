package Logic;
import UI.ChessPiece;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Move {
	// คืนรายการตำแหน่งที่เดินได้จริง (legal moves) ของตัวที่ระบุ (r,c) บนกระดาน board
	// - คืนลิสต์ว่างถ้า (r,c) ว่างหรืออยู่นอกกระดาน
	public static List<Point> getLegalMoves(ChessPiece[][] board, int r, int c) {
		List<Point> legal = new ArrayList<>();
		if (!inBounds(r, c)) return legal;
		ChessPiece p = board[r][c];
		if (p == null) return legal;

		// กรอง pseudo-legal moves ที่ไม่ทำให้ราชาถูกเช็ค
		List<Point> pseudo = getPseudoLegalMoves(board, r, c);
		for (Point dst : pseudo) {
			if (!isMoveLeavingKingInCheck(board, r, c, dst.x, dst.y)) {
				legal.add(dst);
			}
		}
		return legal;
	}

	// ตรวจสอบว่าพิกัด (r,c) อยู่ในกระดานหรือไม่
	private static boolean inBounds(int r, int c) {
		return r >= 0 && r < 8 && c >= 0 && c < 8;
	}

	// เพิ่มการเดินของเบี้ยลงในลิสต์ moves (ไม่ตรวจสอบว่าเดินแล้วราชาปลอดภัยหรือไม่)
	private static void addPawnMoves(ChessPiece[][] board, int r, int c, ChessPiece.Color color, List<Point> moves) {
		int dir;
		int startRow;
		switch (color) {
    		case WHITE:
        		dir = -1;
        		startRow = 6;
        		break;
    		case BLACK:
    			default:
                dir = 1;
                startRow = 1;
                break;
		}
		// เดินไปข้างหน้า 1 ช่อง (ถ้าว่าง)
		int nr = r + dir;
		if (inBounds(nr, c) && board[nr][c] == null) {
			moves.add(new Point(nr, c));
			// สองไปข้างหน้า (ถ้าอยู่แถวเริ่มต้น)
			int nr2 = r + 2*dir;
			if (r == startRow && inBounds(nr2, c) && board[nr2][c] == null) {
				moves.add(new Point(nr2, c));
			}
		}
		// กินเฉียง
		int[] dc = {-1, 1};
		for (int d : dc) {
			int cr = r + dir;
			int cc = c + d;
			if (inBounds(cr, cc) && board[cr][cc] != null && board[cr][cc].getColor() != color) {
				moves.add(new Point(cr, cc));
			}
		}
	}

	// คืนรายการตำแหน่งที่เดินได้แบบ pseudo-legal (ไม่สนใจว่าเดินแล้วราชาปลอดภัยหรือไม่)
	private static List<Point> getPseudoLegalMoves(ChessPiece[][] board, int r, int c) {
		List<Point> moves = new ArrayList<>();
		if (!inBounds(r, c)) return moves;
		ChessPiece p = board[r][c];
		if (p == null) return moves;
		ChessPiece.Type type = p.getType();
		ChessPiece.Color color = p.getColor();
		// เรียกฟังก์ชันย่อยตามชนิดตัวหมาก
		switch (type) {
			case PAWN:
				addPawnMoves(board, r, c, color, moves);
				break;
			case KNIGHT:
				addKnightMoves(board, r, c, color, moves);
				break;
			case BISHOP:
				addSlidingMoves(board, r, c, color, moves, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}});
				break;
			case ROOK:
				addSlidingMoves(board, r, c, color, moves, new int[][]{{1,0},{-1,0},{0,1},{0,-1}});
				break;
			case QUEEN:
				addSlidingMoves(board, r, c, color, moves, new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}});
				break;
			case KING:
				addKingMoves(board, r, c, color, moves);
				break;
		}
		return moves;
	}

	// ตรวจสอบว่าการเดินจาก (fr,fc) ไป (tr,tc) จะทำให้ราชาถูกเช็คหรือไม่
	private static boolean isMoveLeavingKingInCheck(ChessPiece[][] board, int fr, int fc, int tr, int tc) {
		ChessPiece mover = board[fr][fc];
		if (mover == null) return true; // 
		// ทำสำเนากระดานและลองเดิน
		ChessPiece[][] copy = copyBoard(board);
		copy[tr][tc] = copy[fr][fc];
		copy[fr][fc] = null;

		ChessPiece.Color moverColor = mover.getColor();
		Point kingPos;
		if (mover.getType() == ChessPiece.Type.KING) {
			// ถ้าเดินตัวราชา ให้ใช้ตำแหน่งใหม่
			kingPos = new Point(tr, tc);
		} else {
			kingPos = findKing(copy, moverColor);
			if (kingPos == null) {
				// ไม่มีราชา = ผิดปกติ ถือว่าเช็ค
				return true;
			}
		}

		// ตรวจสอบว่าราชาถูกคุกคามหรือไม่
		ChessPiece.Color opponent;
		if (moverColor == ChessPiece.Color.WHITE) {
    		opponent = ChessPiece.Color.BLACK;
		} else {
    		opponent = ChessPiece.Color.WHITE;
		}
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				ChessPiece p = copy[r][c];
				if (p == null || p.getColor() != opponent) continue;
				List<Point> attacks = getPseudoLegalMoves(copy, r, c);
				for (Point pt : attacks) {
					if (pt.x == kingPos.x && pt.y == kingPos.y) return true;
				}
			}
		}

		return false; // king is safe
	}

	// คืนตำแหน่งที่ราชาถูกเช็ค (ถ้าไม่ถูกเช็คคืน null)
	private static ChessPiece[][] copyBoard(ChessPiece[][] board) {
		ChessPiece[][] copy = new ChessPiece[8][8];
		for (int r = 0; r < 8; r++) {
			System.arraycopy(board[r], 0, copy[r], 0, 8);
		}
		return copy;
	}

	// หาและคืนตำแหน่งราชาของสีที่ระบุ (ถ้าไม่เจอคืน null)
	private static Point findKing(ChessPiece[][] board, ChessPiece.Color color) {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				ChessPiece p = board[r][c];
				if (p != null && p.getType() == ChessPiece.Type.KING && p.getColor() == color) {
					return new Point(r, c);
				}
			}
		}
		return null;
	}

	// ถ้าฝั่งที่ระบุ 'ถูก Rook/Check' ให้คืนตำแหน่งราชา; ถ้าไม่ถูก ให้คืน null
	public static Point getCheckSquare(ChessPiece[][] board, ChessPiece.Color color) {
    	if (!isInCheck(board, color)) return null;
    	return findKing(board, color);
	}

	// ตรวจสอบว่าฝั่งที่ระบุ 'ถูกเช็ค' หรือไม่
	public static boolean isInCheck(ChessPiece[][] board, ChessPiece.Color color) {
		Point kingPos = findKing(board, color);
		if (kingPos == null) return false; // no king found
		ChessPiece.Color opponent;
		if (color == ChessPiece.Color.WHITE) {
    		opponent = ChessPiece.Color.BLACK;
		} else {
    		opponent = ChessPiece.Color.WHITE;
		}
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				ChessPiece p = board[r][c];
				if (p == null || p.getColor() != opponent) continue;
				List<Point> attacks = getPseudoLegalMoves(board, r, c);
				for (Point pt : attacks) {
					if (pt.x == kingPos.x && pt.y == kingPos.y) return true;
				}
			}
		}
		return false;
	}

	// Checkmate: in check and no legal moves
	public static boolean isCheckmate(ChessPiece[][] board, ChessPiece.Color color) {
		if (!isInCheck(board, color)) return false;
		// ตรวจสอบว่าฝั่งที่ระบุ 'ถูกเช็ค' หรือไม่
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				ChessPiece p = board[r][c];
				if (p == null || p.getColor() != color) continue;
				List<Point> legal = getLegalMoves(board, r, c);
				if (!legal.isEmpty()) return false;
			}
		}
		return true;
	}

	// Stalemate: not in check and no legal moves
	public static boolean isStalemate(ChessPiece[][] board, ChessPiece.Color color) {
		if (isInCheck(board, color)) return false;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				ChessPiece p = board[r][c];
				if (p == null || p.getColor() != color) continue;
				List<Point> legal = getLegalMoves(board, r, c);
				if (!legal.isEmpty()) return false;
			}
		}
		return true;
	}

	// เพิ่มการเดินของอัศวินลงในลิสต์ moves (ไม่ตรวจสอบว่าเดินแล้วราชาปลอดภัยหรือไม่)
	private static void addKnightMoves(ChessPiece[][] board, int r, int c, ChessPiece.Color color, List<Point> moves) {
		int[][] deltas = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
		for (int[] d : deltas) {
			int nr = r + d[0], nc = c + d[1];
			if (!inBounds(nr, nc)) continue;
			if (board[nr][nc] == null || board[nr][nc].getColor() != color) moves.add(new Point(nr, nc));
		}
	}

	// เพิ่มการเดินของราชาลงในลิสต์ moves (ไม่ตรวจสอบว่าเดินแล้วราชาปลอดภัยหรือไม่)
	private static void addKingMoves(ChessPiece[][] board, int r, int c, ChessPiece.Color color, List<Point> moves) {
		for (int dr = -1; dr <= 1; dr++) {
			for (int dc = -1; dc <= 1; dc++) {
				if (dr == 0 && dc == 0) continue;
				int nr = r + dr, nc = c + dc;
				if (!inBounds(nr, nc)) continue;
				if (board[nr][nc] == null || board[nr][nc].getColor() != color) moves.add(new Point(nr, nc));
			}
		}
	}

	// เพิ่มการเดินแบบเลื่อนของบิชอป/รูค/ควีนลงในลิสต์ moves (ไม่ตรวจสอบว่าเดินแล้วราชาปลอดภัยหรือไม่)
	private static void addSlidingMoves(ChessPiece[][] board, int r, int c, ChessPiece.Color color, List<Point> moves, int[][] directions) {
		for (int[] dir : directions) {
			int dr = dir[0], dc = dir[1];
			int nr = r + dr, nc = c + dc;
			while (inBounds(nr, nc)) {
				if (board[nr][nc] == null) {
					moves.add(new Point(nr, nc));
				} else {
					if (board[nr][nc].getColor() != color) moves.add(new Point(nr, nc));
					break; // เจอหมากขวาง
				}
				nr += dr; nc += dc;
			}
		}
	}

	// ประเมินสถานะเกม (GameState) จากมุมมองของฝั่งที่จะเดิน (sideToMove)
	public enum GameState {
		NORMAL,      // เดินต่อได้ตามปกติ
		CHECK,       // ฝั่งที่จะเดินอยู่ในสถานะ "รุก"
		CHECKMATE,   // เช็คเมท (เกมจบ)
		STALEMATE    // เสมอแบบอับ (เกมจบ)
	}

	// ประเมินสถานะเกม (GameState) จากมุมมองของฝั่งที่จะเดิน (sideToMove)
	public static GameState evaluateState(ChessPiece[][] board, ChessPiece.Color sideToMove) {
		if (isCheckmate(board, sideToMove)) return GameState.CHECKMATE;
		if (isStalemate(board, sideToMove)) return GameState.STALEMATE;
		if (isInCheck(board, sideToMove))   return GameState.CHECK;
		return GameState.NORMAL;
	}
}
