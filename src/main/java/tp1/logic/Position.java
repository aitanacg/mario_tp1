package tp1.logic;

import tp1.exceptions.PositionParseException;

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
		return new Position(row + dy, col + dx);
        //dy filas dx columnas
	}

    public Position up()    { return translate(0, -1); }
    public Position down()  { return translate(0, +1); }
    public Position left()  { return translate(-1, 0); }
    public Position right() { return translate(+1, 0); }

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
        if (o == null || o.getClass() != getClass())
			return false;
		Position p = (Position) o;
		return row == p.row && col == p.col;

	}
	@Override 
	public int hashCode() { return Objects.hash(row, col); }
    @Override 
	public String toString() { return "(" + row + "," + col + ")"; }

    public static Position parse(String s) throws PositionParseException {
        if (s == null)
            throw new PositionParseException("Null position");

        try {
            // Espera formato "(row,col)"
            String t = s.trim();

            if (!t.startsWith("(") || !t.endsWith(")"))
                throw new PositionParseException("Invalid position format: " + s);

            t = t.substring(1, t.length() - 1); // quitar paréntesis
            String[] parts = t.split(",");

            if (parts.length != 2)
                throw new PositionParseException("Invalid position format: " + s);

            int row = Integer.parseInt(parts[0].trim());
            int col = Integer.parseInt(parts[1].trim());

            return new Position(row, col);
        }
        catch (NumberFormatException e) {
            throw new PositionParseException("Invalid number in position: " + s, e);
        }
    }

}
