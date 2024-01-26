package com.croyd.functional_chess;

import com.croyd.functional_chess.TailCalls.TailCall;

public class Board {

	public final Stack<String> PIECE_POSITIONS;
	public final String[][] BOARD_LAYOUT;

	public Board(String[][] boardLayout, Stack<String> piecePositions) {
		this.BOARD_LAYOUT = boardLayout;
		this.PIECE_POSITIONS = piecePositions;
	}

	public Board movePiece(final String from, final String to) {
		return new Board(this.BOARD_LAYOUT, searchPieces(this.PIECE_POSITIONS, from, to, new Stack<String>()).invoke());
	}

	private TailCall<Stack<String>> searchPieces(final Stack<String> pieces, final String from, final String to,
			final Stack<String> updatedPieces) {
		if (pieces.isEmpty())
			return TailCalls.done(updatedPieces);

		if (pieces.first().get().endsWith(from)) {
			final String pieceToUpdate = pieces.first().get();
			return TailCalls.call(this.searchPieces(pieces.remove(), from, to, new Stack<String>()
					.add(pieceToUpdate.substring(0, pieceToUpdate.length() - from.length()) + to).add(updatedPieces)));
		}

		return TailCalls.call(this.searchPieces(pieces.remove(), from, to,
				new Stack<String>().add(pieces.first().get()).add(updatedPieces)));
	}

	public String toString() {
		return this.stringifyBoard(this.PIECE_POSITIONS, 0, 0, "").invoke();
	}

	private TailCall<String> stringifyBoard(final Stack<String> pieces, final int x_coord, final int y_coord,
			final String result) {
		if (y_coord == 8) {
			return TailCalls.done(result);
		}

		if (!pieces.isEmpty() && pieces.first().get().endsWith(String.valueOf(x_coord) + String.valueOf(y_coord))) {
			return TailCalls.call(this.stringifyBoard(pieces.remove(), x_coord + 1 == 8 ? 0 : x_coord + 1,
					x_coord + 1 == 8 ? y_coord + 1 : y_coord,
					result + "| " + pieces.first().get().substring(0, pieces.first().get().length() - 2) + " "
							+ (x_coord + 1 == 8 ? "|" + (y_coord + 1 == 8 ? "" : "\n") : "")));
		}

		return TailCalls.call(this.stringifyBoard(pieces, x_coord + 1 == 8 ? 0 : x_coord + 1,
				x_coord + 1 == 8 ? y_coord + 1 : y_coord,
				result + "|    " + (x_coord + 1 == 8 ? "|" + (y_coord + 1 == 8 ? "" : "\n") : "")));
	}
}