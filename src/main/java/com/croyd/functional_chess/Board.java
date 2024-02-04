package com.croyd.functional_chess;

import com.croyd.functional_chess.TailCalls.TailCall;
import com.croyd.functional_chess.pieces.Coordinates;
import com.croyd.functional_chess.pieces.Piece;

public class Board {
	
	public final Stack<Piece> PIECES;
	public final String[][] BOARD_LAYOUT;

	public Board(String[][] boardLayout, Stack<Piece> piecePositions) {
		this.BOARD_LAYOUT = boardLayout;
		this.PIECES = piecePositions;
	}

	public Board movePiece(final Coordinates from, final Coordinates to) {
		return new Board(
			this.BOARD_LAYOUT,
			PIECES.replace(
				PIECES.filter(
						piece -> piece.COORDINATES,
						new Stack<Coordinates>().add(new Coordinates(from.X(), from.Y()))
					).first().get(),
				PIECES.filter(
						piece -> piece.COORDINATES,
						new Stack<Coordinates>().add(new Coordinates(from.X(), from.Y()))
					).first().get().move(to)
			)
		);
	}
	
	public boolean coordinatesInBounds(final Coordinates coordinates) {
		if (this.BOARD_LAYOUT.length <= coordinates.Y() || this.BOARD_LAYOUT[coordinates.Y()].length <= coordinates.X())
			return false;
		
		if (this.BOARD_LAYOUT[coordinates.X()][coordinates.Y()].isEmpty())
			return false;
		
		return true;
	}
	
	public Stack<Coordinates> getAttackedFields (final String defendingColor) {
		return fieldsUnderAttack(this.PIECES.filter(piece -> piece.COLOR, new Stack<String>().add(defendingColor), false), new Stack<Coordinates>()).invoke();
	}
	
	private TailCall<Stack<Coordinates>> fieldsUnderAttack(final Stack<Piece> pieces, final Stack<Coordinates> attackedFields) {
		if (pieces.isEmpty())
			return TailCalls.done(attackedFields);
		
		return TailCalls.call(fieldsUnderAttack(pieces.remove(), attackedFields.add(pieces.first().get().getAttackFields(this), true)));
	}

	public String toString() {
		return this.stringifyBoard(this.PIECES, 0, 0, "").invoke();
	}

	private TailCall<String> stringifyBoard(
		final Stack<Piece> pieces,
		final int x_coord,
		final int y_coord,
		final String result
	) {
		if (y_coord == 8)
			return TailCalls.done(result);

		if (this.BOARD_LAYOUT[y_coord][x_coord].isEmpty()) {
			return TailCalls.call(
					this.stringifyBoard(
						pieces,
						x_coord + 1 == 8 ? 0 : x_coord + 1,
						x_coord + 1 == 8 ? y_coord + 1 : y_coord,
						result + "|####" + (x_coord + 1 == 8 ? "|" + (y_coord + 1 == 8 ? "" : "\n") : "")
					)
				);
		}
			
		
		if (pieces.filter(piece -> piece.COORDINATES, new Stack<Coordinates>()
				.add(new Coordinates(x_coord, y_coord))).isEmpty()) {
			return TailCalls.call(
				this.stringifyBoard(
					pieces,
					x_coord + 1 == 8 ? 0 : x_coord + 1,
					x_coord + 1 == 8 ? y_coord + 1 : y_coord,
					result + "|    " + (x_coord + 1 == 8 ? "|" + (y_coord + 1 == 8 ? "" : "\n") : "")
				)
			);
		}

		return TailCalls.call(
			this.stringifyBoard(
				pieces,
				x_coord + 1 == 8 ? 0 : x_coord + 1,
				x_coord + 1 == 8 ? y_coord+ 1 : y_coord,
				result + "| " + pieces.filter(
						piece -> piece.COORDINATES,
						new Stack<Coordinates>().add(new Coordinates(x_coord, y_coord))
					).first().get().toString() + " " + (x_coord + 1 == 8 ? "|" + (y_coord + 1 == 8 ? "" : "\n") : "")
			)
		);
	}
}