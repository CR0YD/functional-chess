package com.croyd.functional_chess.pieces;

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

	public abstract boolean moveValidation(int x, int y, Stack<Piece> pieces);
	
	@Override
	public abstract String toString();

	protected String getColor() {
		return this.COLOR;
	}

	protected Coordinates getCoordinates() {
		return this.COORDINATES;
	}
}
