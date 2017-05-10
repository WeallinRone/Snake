package snake;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.geom.AffineTransform;
//import java.awt.image.BufferedImage;

import javax.swing.JFrame;

//��ͼѹ��
public class Shots {
	
	private JFrame f;
	public Shots(JFrame f) {
		this.f = f;
	}

	private int offWidth = 3;
	private int offHeight = 22;
	//��ͼ
	public Image screenshots() {
		try {
		   Robot rbt = new Robot();
		   Image image = rbt.createScreenCapture(new Rectangle(
				   f.getX()+offWidth, f.getY()+offHeight, 
				   f.getWidth()-Yard.EXTRA_WIDTH-2*offWidth, 
				   f.getHeight()-offHeight-offWidth));
		   return image;
		} catch (Exception ex) {
			   	// p(ex.toString());
			   	// �˷���û�������� ����Ϊ�޷���֪������ ����Ϊ��Ӱ��ִ��Ч�� ��
				//��ע�͵��� ex.printStackTrace();
		}
		return null;
	}
	
//	//����ͼƬ��ʹ֮ͼƬ͸��
//	private BufferedImage alpha(Image image){
//		BufferedImage temp = (BufferedImage)image;
//		int imgWidth = image.getWidth(null);
//		int imgHeight = image.getHeight(null);
//		BufferedImage bi = new BufferedImage(imgWidth, imgHeight,
//				BufferedImage.TYPE_4BYTE_ABGR);//�½�һ������֧��͸����BufferedImage
//		for(int i = 0; i < imgWidth; ++i)//��ԭͼƬ�����ݸ��Ƶ��µ�ͼƬ��ͬʱ��Ϊ͸��
//			for(int j = 0; j < imgHeight; ++j)
//				bi.setRGB(i, j, temp.getRGB(i, j)-0x30000000);
//		return bi;
//	}
	
	private Canvas canvas;
	private float scale = 0.25f;//ѹ������
	public Canvas compression(Image image){
		canvas = new CPCanvas(image);
		canvas.setLocation(Yard.GRID_SIZE, 3*Yard.GRID_SIZE);
		return canvas;
	}
	
	private class CPCanvas extends Canvas{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Image image;
		public CPCanvas(Image image) {
			this.image = image;
		}
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d=(Graphics2D) g;
			AffineTransform at=new AffineTransform();
			at.scale(scale,scale);//ѹ������
			int rule = AlphaComposite.SRC_OVER;
			float alpha = 0.6f;
			g2d.setComposite(AlphaComposite.getInstance(rule, alpha));
			g2d.setTransform(at);
			g2d.drawImage(image,(int)(Yard.CLOS*Yard.GRID_SIZE/scale),9*Yard.GRID_SIZE-(Yard.GRID_SIZE-10)*9,f);
		}
		
	}
	
}
