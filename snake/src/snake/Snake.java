package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Snake {
	private Node head;
	private Node tail;
	private int size = 0;
	private int moveTime = 128;//移动间隔
	
	public Snake() {
		Node n = new Node(40,20,Dir.L);
		tail = n;
		head = n;
		size = 1;
		addToHead();
		addToHead();
	}
	
	public int size(){
		return size;
	}
	
	public void speedUp(){
		if(moveTime>32)
			moveTime /= 2;
	}
	
	public void speedDown(){//30~300  300-y=30 =300
		if(moveTime<512)
			moveTime *= 2;
	}
	
	public int getMoveTime(){
		return moveTime;
	}
	
	public void addToHead(){
		Node n = null;
		switch(head.d){
		case L:
			n = new Node(head.row-1, head.col, head.d);
			break;
		case R:
			n = new Node(head.row+1, head.col, head.d);
			break;
		case U:
			n = new Node(head.row, head.col-1, head.d);
			break;
		case D:
			n = new Node(head.row, head.col+1, head.d);
			break;
		}
		head.next=n;
		head=n;
		size ++;
	}
	
	public void draw(Graphics g){
		if(size<=0) return;
		for(Node n = tail;n!=null;n=n.next)
			n.draw(g);
		Node n = move();
		restoreBg(g, n);
	}
	
	/**
	 * 恢复背景色
	 * @param g
	 * @param n
	 */
	private void restoreBg(Graphics g,Node n){
		//恢复背景色
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(n.row*n.size, n.col*n.size, n.size, n.size);
		//恢复线条
		g.setColor(Color.DARK_GRAY);
		g.drawLine(n.row*n.size, n.col*n.size, n.row*n.size, n.col*n.size+n.size);//L
		g.drawLine(n.row*n.size, n.col*n.size, n.row*n.size+n.size, n.col*n.size);//U
//		g.drawLine(n.row*n.size, n.col*n.size+n.size, n.row*n.size+n.size, n.col*n.size+n.size);//D
//		g.drawLine(n.row*n.size+n.size, n.col*n.size, n.row*n.size+n.size, n.col*n.size+n.size);//R
	}
	
	/**
	 * 移动
	 * @return
	 */
	private Node move(){
		addToHead();
		Node n = deleteToTail();
		return n;
	}
	
	private Node deleteToTail() {
		if(size==0) return null;
		Node n = tail;
		tail=tail.next;
		size --;
		return n;
	}

	private Point p;
	public void eat(Egg egg){
		if(getHeadRect().intersects(egg.getRect())){
			do{
				p = egg.renovate();
			}while(eggOnSnake(p));
			addToHead();
		}
	}
	
	private boolean eggOnSnake(Point p){
		for(Node n = tail; n != null; n = n.next)
			if(n.row==p.x&&n.col==p.y)
				return true;
		return false;
	}
	
	//蛇死亡
	public boolean die(Wall wall){
		//撞到墙
		if(wall.intersection(getHeadRect())){
			return true;
		}
		//咬到自己
		for(Node n = tail; n!=head; n=n.next){
			if(getHeadRect().intersects(getNodeRect(n)))
				return true;
		}
		return false;
	}
	
	private Rectangle getHeadRect() {
//		return new Rectangle(head.size*head.col, head.size*head.row,
//				head.size, head.size);
		return getNodeRect(head);
	}
	
	private Rectangle getNodeRect(Node n) {
		return new Rectangle(n.size*n.col, n.size*n.row,
				n.size, n.size);
	}

	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			if(head.d.equals(Dir.R))
				return;
			head.d = Dir.L;
			break;
		case KeyEvent.VK_UP:
			if(head.d.equals(Dir.D))
				return;
			head.d = Dir.U;
			break;
		case KeyEvent.VK_RIGHT:
			if(head.d.equals(Dir.L))
				return;
			head.d = Dir.R;
			break;
		case KeyEvent.VK_DOWN:
			if(head.d.equals(Dir.U))
				return;
			head.d = Dir.D;
			break;
		default:
			break;
		}
	}
	
	private class Node{
		int size = Yard.GRID_SIZE;
		int row,col;//行,列
		Dir d;
		Node next = null;
		
		Node(int row, int col, Dir d) {
			this.row = row;
			this.col = col;
			this.d = d;
		}
		
		void draw(Graphics g){
			g.setColor(Color.BLACK);
			g.fillRect(row*size, col*size, size, size);
		}
	}
}
