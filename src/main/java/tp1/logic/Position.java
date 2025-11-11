package tp1.logic;

import java.util.Objects;

public final class Position {

	private final int col;
	private final int row;

	public Position(int row, int col){
		this.row = row;
		this.col = col;
	}

	public int getRow() { return row; }
	public int getCol() { return col; }
	
	public Position translate(int dx, int dy){
		return new Position(row + dy, col + dx);  //dy filas dx columnas
	}

    //comprueba que la pos esta en el tablero
	public boolean isInBounds(int width, int height){
		return (row >= 0 && row < height && col >= 0 && col < width);
	}

    public Position next(Action a) {
        switch (a) {
            case LEFT:  return translate(-1, 0);
            case RIGHT: return translate(+1, 0);
            case UP:    return translate(0, -1);
            case DOWN:  return translate(0, +1);
            default:    return this;
        }
    }

	@Override
	public boolean equals(Object o) {

		//me banearon el uso de instanceof :(
		// if(this == o) return true; //mismo objeto, iguales
		// if (!(o instanceof Position)) return false; //si no es position no
		// Position p = (Position) o;
		// return row == p.row && col == p.col; //comparo que sean iguales

		if (o == null || o.getClass() != getClass())
			return false;
		Position p = (Position) o;
		return row == p.row && col == p.col;

	}
	@Override 
	public int hashCode() { return Objects.hash(row, col); }
    @Override 
	public String toString() { return "(" + row + "," + col + ")"; }

}
