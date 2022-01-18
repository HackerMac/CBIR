package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * 直方图均衡化
 */
public class HistogramEqualization {

	private BufferedImage img;//图像
	private int[] pixels;// 图像所有像素点
	private int[] histogram = new int[256];
	private int weigth, height;//图像宽度，高度 

	/**
	 * 构造函数，调用Gray类进行灰度化
	 * @param srcPath 原图片路径
	 */
	public HistogramEqualization(String srcPath) {
		Gray g = new Gray();
		g.grayImage("srcPath");
		// 得到图像宽度，高度
		weigth = g.getWeigth();
		height = g.getHeight();
		// 得到经灰度化后的像素
		pixels = g.getNewPixels();
	}

	/**
	 *  直方图均衡
	 * @param destPath 输出文件
	 */
	public void histogramEqualization(String destPath) {
		BufferedImage greyImage = new BufferedImage(weigth, height,
				BufferedImage.TYPE_BYTE_GRAY);// 无符号 byte,灰度级图像
		for (int i = 0; i < height - 1; i++) {// 计算每一个灰度级的像素数
			for (int j = 0; j < weigth - 1; j++) {
				int grey = pixels[i * weigth + j] & 0xff;
				histogram[grey]++;
			}
		}

		double a = (double) 255 / (weigth * height);
		double[] c = new double[256];
		c[0] = (a * histogram[0]);
		for (int i = 1; i < 256; i++)
			c[i] = c[i - 1] + (int) (a * histogram[i]);// 直方图均衡化(离散情况)
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < weigth; j++) {
				int grey = pixels[i * weigth + j] & 0x0000ff;
				int hist = (int) c[grey];
				pixels[i * weigth + j] = 255 << 24 | hist << 16 | hist << 8 | hist;
				greyImage.setRGB(j, i, pixels[i * weigth + j]);
			}
		}

		img = greyImage;

		try {
			ImageIO.write(greyImage, "jpg", new File(destPath));//输出图像
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//
//		HistogramEqualization he = new HistogramEqualization("C:\\Users\\刘开\\Desktop\\图库\\accordion\\02.jpg");
//		he.histogramEqualization("C:\\Users\\刘开\\Desktop\\图库\\accordion\\oo4.jpg");// 均衡化并输出
//	}

}
