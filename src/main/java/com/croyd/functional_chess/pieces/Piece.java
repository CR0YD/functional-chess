package com.croyd.functional_chess.pieces;

import com.croyd.functional_chess.Board;
import com.croyd.functional_chess.Stack;

public abstract class Piece {

	public final String NAME;
	public final String COLOR;
	public final Coordinates COORDINATES;

	public Piece(final String name, final String color, final Coordinates coordinates) {
		this.NAME = name;
		this.COLOR = color;
		this.COORDINATES = coordinates;
	}

	public abstract boolean moveValidation(final int x, final int y, final Board board);
	
	public abstract Stack<Coordinates> getAttackFields(final Board board);
	
	@Override
	public abstract String toString();

	protected String getColor() {
		return this.COLOR;
	}

	public Coordinates getCoordinates() {
		return this.COORDINATES;
	}
	
	public abstract Piece move(final Coordinates coordinates);
}
