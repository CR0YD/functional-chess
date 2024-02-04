package com.croyd.functional_chess.pieces;

import com.croyd.functional_chess.Board;
import com.croyd.functional_chess.Stack;
import com.croyd.functional_chess.TailCalls;
import com.croyd.functional_chess.TailCalls.TailCall;

public class King extends Piece {

	public King(final String name, final String color, final int x, final int y) {
		super(name, color, new Coordinates(x, y));
	}
	
	public King(final String name, final String color, final Coordinates coordinates) {
		super(name, color, coordinates);
	}

	@Override
	public boolean moveValidation(final int x, final int y, final Board board) {
		return this.getValidMoves(board).contains(new Coordinates(x, y));
	}

	private Stack<Coordinates> getValidMoves(final Board board) {
		return validMoves(board, -1, -1, new Stack<Coordinates>()).invoke();
	}

	private TailCall<Stack<Coordinates>> validMoves(
		final Board board,
		final int x,
		final int y,
		final Stack<Coordinates> result
	) {
		if (y == 2)
			return TailCalls.done(result);

		if (!board.coordinatesInBounds(new Coordinates(super.COORDINATES.X() + x, super.COORDINATES.Y() + y)) ||
			board.getAttackedFields(this.COLOR).contains(new Coordinates(super.COORDINATES.X() + x, super.COORDINATES.Y() + y)) ||
			!board.PIECES
				.filter(piece -> piece.COLOR, new Stack<String>().add(super.COLOR))
				.filter(piece -> piece.COORDINATES, new Stack<Coordinates>().add(new Coordinates(super.COORDINATES.X() + x, super.COORDINATES.Y() + y)))
				.isEmpty()
		) {
			return TailCalls.call(
				validMoves(
					board,
					x + 1 > 1 ? -1 : x + 1,
					x + 1 > 1 ? y + 1 : y,
					result
				)
			);
		}

		return TailCalls.call( 
			validMoves(
				board,
				x + 1 > 1 ? -1 : x + 1,
				x + 1 > 1 ? y + 1 : y,
				x == 0 && y == 0 ?
					result :
					result.add(new Coordinates(super.COORDINATES.X() + x, super.COORDINATES.Y() + y))
			)
		);
	}

	@Override
	public String toString() {
		return super.COLOR + super.NAME;
	}

	@Override
	public King move(final Coordinates coordinates) {
		return new King(super.NAME, super.COLOR, coordinates);
	}

	@Override
	public Stack<Coordinates> getAttackFields(final Board board) {
		return attackingFields(board, -1, -1, new Stack<Coordinates>()).invoke();
	}
	
	private TailCall<Stack<Coordinates>> attackingFields(
		final Board board,
		final int x,
		final int y,
		final Stack<Coordinates> result
	) {
		if (y == 2)
			return TailCalls.done(result);

		if (!board.coordinatesInBounds(new Coordinates(super.COORDINATES.X() + x, super.COORDINATES.Y() + y))) {
			return TailCalls.call(
				attackingFields(
					board,
					x + 1 > 1 ? -1 : x + 1,
					x + 1 > 1 ? y + 1 : y,
					result
				)
			);
		}

		return TailCalls.call( 
			attackingFields(
				board,
				x + 1 > 1 ? -1 : x + 1,
				x + 1 > 1 ? y + 1 : y,
				x == 0 && y == 0 ? result : result.add(new Coordinates(super.COORDINATES.X() + x, super.COORDINATES.Y() + y))
			)
		);
	}
}
