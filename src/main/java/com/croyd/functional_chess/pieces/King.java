package com.croyd.functional_chess.pieces;

import java.util.Optional;

import com.croyd.functional_chess.Stack;
import com.croyd.functional_chess.TailCalls;
import com.croyd.functional_chess.TailCalls.TailCall;

public class King extends Piece {

	public King(final String name, final String color, final int x, final int y) {
		super(name, color, new Coordinates(x, y));
	}

	@Override
	public boolean moveValidation(final int x, final int y, final Stack<Piece> pieces) {
		if (!this.getValidMoves().contains(new Coordinates(x, y)))
			return false;

		final Optional<Piece> piece = pieces
				.filter(Piece::getCoordinates, new Stack<Coordinates>().add(new Coordinates(x, y))).first();
		
		if (!piece.isPresent() || !piece.get().COLOR.equals(super.COLOR))
			return true;

		return false;
	}

	private Stack<Coordinates> getValidMoves() {
		return validMoves(-1, -1, new Stack<Coordinates>()).invoke();
	}

	private TailCall<Stack<Coordinates>> validMoves(final int x, final int y, final Stack<Coordinates> result) {
		if (y == 2)
			return TailCalls.done(result);

		if (super.COORDINATES.X() + x > -1 && super.COORDINATES.X() + x < 8 && super.COORDINATES.Y() + y > -1
				&& super.COORDINATES.Y() + y < 8)
			return TailCalls.call(validMoves(x + 1 > 1 ? -1 : x + 1, x + 1 > 1 ? y + 1 : y,
					x == 0 && y == 0 ? result : result.add(new Coordinates(x, y))));

		return TailCalls
				.call(validMoves(x + 1 > 1 ? -1 : x + 1, x + 1 > 1 ? y + 1 : y, x == 0 && y == 0 ? result : result));
	}

	@Override
	public String toString() {
		return super.COLOR + super.NAME + super.COORDINATES.X() + super.COORDINATES.Y();
	}

}
