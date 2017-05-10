package snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Yard extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ROWS = 50;
	public static final int CLOS = 50;
	public static final int GRID_SIZE = 14;
	public static final int EXTRA_WIDTH = CLOS*GRID_SIZE/4+1;
	
	private Snake snake = new Snake();
	private Egg egg = new Egg();
	private Wall wall = new Wall();
	
	//���ư���
	private JPanel funPanel;
	private JButton btnPause;
	private JButton btnSpeedUp;
	private JButton btnSpeedDown;
	private JLabel nowScore;
	
	//��Ϸ������ؼ�
	private JPanel panel;
	private JButton restart;
	private JLabel gameOver;
	private JLabel score;
	
	//��ͼ
	private JPanel map;
	private Canvas canvas;
	
	/** ��Ϸ�Ƿ���� */
	private boolean isOver;
	/** �Ƿ��һ����� */
	private boolean isFirstPaint;
	/** �Ƿ���ͣ */
	private boolean isPause;
	
	private Renovate renovate;
	public Yard() {
		init();
		initEvent();

		renovate = new Renovate();
		Thread t1 = new Thread(renovate);
		t1.start();
	}
	
	private void init(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(20, 20, CLOS*GRID_SIZE+EXTRA_WIDTH, ROWS*GRID_SIZE);
		setResizable(false);
		setVisible(true);
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, CLOS*GRID_SIZE-4, ROWS*GRID_SIZE-4);
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setLayout(null);
		panel.setVisible(false);
		add(panel);
		
		map = new JPanel();
		map.setBounds(GRID_SIZE*CLOS-3, -6, (getWidth()-EXTRA_WIDTH)/4, getHeight()/4);
		map.setBackground(Color.WHITE);
		add(map);
		canvas = new Canvas();
		map.add(canvas);
		
		funPanel = new JPanel();
		funPanel.setLayout(null);
		funPanel.setBackground(Color.lightGray);
		funPanel.setBounds(getWidth()-EXTRA_WIDTH-3, getHeight()/4-6, getWidth()/4, getHeight()*3/4);
		add(funPanel);
		
		btnPause = new JButton("��ͣ");
		btnPause.setBounds(10, 10, 80, 30);
		funPanel.add(btnPause);
		
		btnSpeedUp = new JButton("����");
		btnSpeedUp.setBounds(10, 50, 80, 30);
		funPanel.add(btnSpeedUp);
		
		btnSpeedDown = new JButton("����");
		btnSpeedDown.setBounds(10, 90, 80, 30);
		funPanel.add(btnSpeedDown);
		
		nowScore = new JLabel("������0");
		nowScore.setBounds(10, 130, 80, 30);
		funPanel.add(nowScore);
		
		gameOver = new JLabel("GameOver",JLabel.CENTER);
		gameOver.setBounds((CLOS-10)*GRID_SIZE/2, (ROWS-5)*GRID_SIZE/2, GRID_SIZE*10, GRID_SIZE*3);
		gameOver.setFont(new Font("����", Font.BOLD, GRID_SIZE*2));
		panel.add(gameOver);
		
		score = new JLabel("",JLabel.CENTER);
		score.setBounds((CLOS-10)*GRID_SIZE/2, (ROWS-20)*GRID_SIZE/2, GRID_SIZE*10, GRID_SIZE*3);
		score.setFont(new Font("����", Font.ITALIC, GRID_SIZE*2));
		panel.add(score);
		
		restart = new JButton("���¿�ʼ");
		restart.setBounds((CLOS-15)*GRID_SIZE/2, (ROWS+20)*GRID_SIZE/2, GRID_SIZE*15, GRID_SIZE*3);
		restart.setFont(new Font("����", Font.ITALIC, GRID_SIZE*2));
		panel.add(restart);
	}
	
	private void initEvent(){
		addKeyListener(new KeyMo());
		restart.addActionListener(new ReStartLis());
		addFocusListener(new FocusLis());
		btnPause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestFocus();
				if(isOver)
					return;
				if(isPause){
					isPause = false;
					btnPause.setText("��ͣ");
				}
				else{
					isPause = true;
					btnPause.setText("��ʼ");
				}
				renovate.setPause(isPause);
			}
		});
		btnSpeedUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestFocus();
				snake.speedUp();
			}
		});
		btnSpeedDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestFocus();
				snake.speedDown();
			}
		});
	}
	
	private Shots s = new Shots(this);
	private Image image;
	private void updateMap(){
		map.remove(canvas);
		image = s.screenshots();
		canvas = s.compression(image);
		canvas.setLocation(0, 0);
		map.add(canvas);
	}
	
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		if(isFirstPaint){
			drawYard(g);
			isFirstPaint = false;
		}
		if(isOver){
			//��滭��
//			panel.paint(g);
		}
		else{
//			//�ָ�����
//			restoreBg(g);
			//�����
			snake.draw(g);
			//��浰
			egg.draw(g);
			//���С��ͼ
			if(isOver==false)
				updateMap();
			map.paint(g);
			canvas.paint(g);
		}
		funPanel.paint(g);
		//���ָ���
		g.setColor(Color.BLACK);
		g.drawLine(GRID_SIZE*CLOS*4, 0, GRID_SIZE*CLOS*4, GRID_SIZE*ROWS*4);
		g.setColor(c);
	}
	
	private void drawYard(Graphics g){
		//���Ժ�ӱ���
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, CLOS*GRID_SIZE, ROWS*GRID_SIZE);
		g.setColor(Color.DARK_GRAY);
		for(int i = 1; i < ROWS; i++)
			g.drawLine(0, GRID_SIZE*i, GRID_SIZE*CLOS, GRID_SIZE*i);
		for(int i = 1; i < CLOS; i++)
			g.drawLine(GRID_SIZE*i, 0, GRID_SIZE*i, GRID_SIZE*ROWS);
		//���ǽ
		wall.draw(g);
	}
	
//	/**
//	 * �ָ���ͼ�±ߵı���ɫ
//	 * @param g
//	 * @param n
//	 */
//	private void restoreBg(Graphics g){
//		int col = CLOS/4;
//		int row = ROWS/4;
//		int offX = GRID_SIZE;
//		int offY = 3*GRID_SIZE;
//		//�ָ�Ժ�ӱ���
//		g.setColor(Color.LIGHT_GRAY);
//		g.fillRect(offX, offY, col*GRID_SIZE, row*GRID_SIZE);
//		g.setColor(Color.DARK_GRAY);
//		//�ָ�����
//		g.setColor(Color.DARK_GRAY);
//		for(int i = 0; i < row; i++)
//			g.drawLine(offX, GRID_SIZE*i+offY, GRID_SIZE*col+offX, GRID_SIZE*i+offY);
//		for(int i = 0; i < col; i++)
//			g.drawLine(GRID_SIZE*i+offX, offY, GRID_SIZE*i+offX, GRID_SIZE*row+offY);
//		//�ָ�ǽ
//		g.setColor(Color.RED);
//		g.fillRect(0, offY, GRID_SIZE, row*GRID_SIZE+offY);//left
//		g.fillRect(offX, 0, col*GRID_SIZE+offX, 3*GRID_SIZE);//up
//	}

	/**
	 * ˢ�»���
	 */
	private class Renovate implements Runnable {

		private boolean isPause;
		private Object obj = new Object();
		@Override
		public void run() {
			panel.setVisible(false);
			isOver = false;
			isFirstPaint = true;
			while(!isOver){
				synchronized (obj) {
					if(isPause)
						try {
							obj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
				if(snake.die(wall))
					isOver = true;
				Yard.this.repaint();
				snake.eat(egg);
				nowScore.setText("����:"+(snake.size()-3));
				try {
					Thread.sleep(snake.getMoveTime());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			panel.setVisible(true);
			score.setText("����:"+(snake.size()-3));
		}
		
		public void setPause(boolean isPause) {
			if(!isPause)
				synchronized (obj) {
					obj.notify();
				}
			this.isPause = isPause;
		}

	}
	
	private class KeyMo extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			if(isPause)
				return;
			snake.keyPressed(e);
		}
	}
	
	private class ReStartLis implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			requestFocus();
			snake = new Snake();
			Thread t = new Thread(renovate);
			t.start();
		}
	}
	
	private class FocusLis extends FocusAdapter{
		@Override
		public void focusGained(FocusEvent e) {
			if(!isOver)
				isFirstPaint = true;
			repaint();
		}
	}

	public static void main(String[] args) {
		new Yard();
	}
}
