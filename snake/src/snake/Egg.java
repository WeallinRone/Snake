package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class Egg {

	int size = Yard.GRID_SIZE;
	int row,col;
	
	public Egg() {
		Random rand = new Random();
		row = rand.nextInt(Yard.ROWS-10)+5;
		col = rand.nextInt(Yard.CLOS-10)+5;
	}
	
	public Rectangle getRect() {
		return new Rectangle(col*size, row*size, size, size);
	}
	
	public Point renovate(){
		Random rand = new Random();
		row = rand.nextInt(Yard.ROWS-10)+5;
		col = rand.nextInt(Yard.CLOS-10)+5;
		return new Point(row,col);
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.GREEN);
		g.fillOval(row*size, col*size, size, size);
		g.setColor(c);
	}
}
