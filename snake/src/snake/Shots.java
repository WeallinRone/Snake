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

//截图压缩
public class Shots {
	
	private JFrame f;
	public Shots(JFrame f) {
		this.f = f;
	}

	private int offWidth = 3;
	private int offHeight = 22;
	//截图
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
			   	// 此方法没有申明过 ，因为无法得知上下文 。因为不影响执行效果 ，
				//先注释掉它 ex.printStackTrace();
		}
		return null;
	}
	
//	//复制图片并使之图片透明
//	private BufferedImage alpha(Image image){
//		BufferedImage temp = (BufferedImage)image;
//		int imgWidth = image.getWidth(null);
//		int imgHeight = image.getHeight(null);
//		BufferedImage bi = new BufferedImage(imgWidth, imgHeight,
//				BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage
//		for(int i = 0; i < imgWidth; ++i)//把原图片的内容复制到新的图片，同时设为透明
//			for(int j = 0; j < imgHeight; ++j)
//				bi.setRGB(i, j, temp.getRGB(i, j)-0x30000000);
//		return bi;
//	}
	
	private Canvas canvas;
	private float scale = 0.25f;//压缩比例
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
			at.scale(scale,scale);//压缩比例
			int rule = AlphaComposite.SRC_OVER;
			float alpha = 0.6f;
			g2d.setComposite(AlphaComposite.getInstance(rule, alpha));
			g2d.setTransform(at);
			g2d.drawImage(image,(int)(Yard.CLOS*Yard.GRID_SIZE/scale),9*Yard.GRID_SIZE-(Yard.GRID_SIZE-10)*9,f);
		}
		
	}
	
}
