package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * 进行图片灰度化
 */
public class Gray {

	private BufferedImage img;// 图像
	private int[] newPixel;// 图像所有像素点
	private int weigth, height;// 图像宽度，高度

	public int[] getNewPixels() {
		return newPixel;
	}

	public int getWeigth() {
		return weigth;
	}

	public int getHeight() {
		return height;
	}

	public BufferedImage getImg() {
		return img;
	}

	/**
	 * 图像灰度化
	 * 
	 * @param srcPath
	 *            原图片路径
	 */
	public void grayImage(String srcPath) {
		try {
			BufferedImage reader = ImageIO.read(new File(srcPath));
			if(reader==null)
				return;
			weigth = reader.getWidth();
			height = reader.getHeight();
			newPixel = new int[weigth * height];

			BufferedImage grayImage = new BufferedImage(weigth, height,
					BufferedImage.TYPE_BYTE_GRAY);// 无符号 byte,灰度级图像

			for (int i = 0; i < weigth; i++) {// 读入所有像素，转换图像信号,使其灰度化
				for (int j = 0; j < height; j++) {
					int rgb = reader.getRGB(i, j);
					int grey = (int) ((0.3 * ((rgb & 0xff0000) >> 16) + 0.59 * ((rgb & 0xff00) >> 8)) + 0.11 * ((rgb & 0xff)));
					rgb = 255 << 24 | grey << 16 | grey << 8 | grey;
					grayImage.setRGB(i, j, rgb);
				}
			}

			img = grayImage;//得到灰度化的图像
			PixelGrabber pg = new PixelGrabber(img, 0, 0, weigth, height,
					newPixel, 0, weigth);
			pg.grabPixels();// 将该灰度化后的图片所有像素点读入pixels数组
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Gray g = new Gray();
		g.grayImage("C:\\Users\\MINGKU\\Desktop\\023.jpg");
	}

}
