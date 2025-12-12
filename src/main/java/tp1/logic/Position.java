package tp1.logic;

import tp1.exceptions.PositionParseException;
import java.util.Objects;
/**
 * Position es una coordenada del tablero
 * Ubica objetos, mueve, detecta colisiones, valida limites y lee posiciones desde ficheros
 */
public final class Position {

	private final int col;
	private final int row;

	public Position(int row, int col){
		this.row = row; //15, mov vertical
		this.col = col; //30, mov horizontal
	}

	public int getRow() { return row; }
	public int getCol() { return col; }
	
	public Position translate(int dx, int dy){
		return new Position(row + dy, col + dx);
        //dy filas dx columnas
	}

    //no mas translates sucios por el codigo
    public Position up()    { return translate(0, -1); }
    public Position down()  { return translate(0, +1); }
    public Position left()  { return translate(-1, 0); }
    public Position right() { return translate(+1, 0); }
    public Position rightup() { return translate(+1, -1); }
    public Position rightdown() { return translate(+1, 1); }
    public Position leftup() { return translate(-1, -1); }
    public Position leftdown() { return translate(-1, 1); }

    //comprueba que la pos esta en el tablero
	public boolean isInBounds(int height, int width){ //PQ TENGO TODO GIRADO HELP  QUE ME TOME
		return (row >= 0 && row < height && col >= 0 && col < width);
	}

    public Position next(Action a) {//conv accion a position
        switch (a) {
            case LEFT:  return translate(-1, 0);
            case RIGHT: return translate(+1, 0);
            case UP:    return translate(0, -1);
            case DOWN:  return translate(0, +1);
            case RIGHTUP:  return rightup();
            case RIGHTDOWN: return rightdown();
            case LEFTUP: return leftup();
            case LEFTDOWN: return leftdown();
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

    public static Position parse(String s) throws PositionParseException { //leo pos desde ficheros (pal load)

        if (s == null) throw new PositionParseException("Null position");
        try {
            String text = s.trim();//espera format "(fila,col)", quito espacios de " (

            if (!text.startsWith("(") || !text.endsWith(")"))  //valido ()
                text = text.substring(1, text.length() - 1); //quito ((()()()()()( y me queda "a,e"
            String[] parts = text.split(",");

            if (parts.length != 2) throw new PositionParseException("Invalid position format: " + s);

            int row = Integer.parseInt(parts[0].trim()); //conv a num
            int col = Integer.parseInt(parts[1].trim());

            return new Position(row, col);
        }
        catch (NumberFormatException e) {
            throw new PositionParseException("Invalid number in position: " + s, e);
        }
    }
}