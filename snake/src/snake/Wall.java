package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
	int size = Yard.GRID_SIZE;
	int row = Yard.ROWS*size;
	int col = Yard.CLOS*size;
	
	public boolean intersection(Rectangle r){
		if(r.intersects(new Rectangle(0, 0, 3*size, row))||
				r.intersects(new Rectangle(0, 0, col, size))||
				r.intersects(new Rectangle(0, row-size, col, row))||
				r.intersects(new Rectangle(col-size, 0, col, row)))
			return true;
		return false;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.RED);
		g.fillRect(0, 0, size, row);//left
		g.fillRect(0, 0, col, 3*size);//up
		g.fillRect(row-size,0,  size, row);//right
		g.fillRect(0,col-size,  col, size);//down
	}
}
