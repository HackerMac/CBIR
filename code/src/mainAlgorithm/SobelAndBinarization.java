package mainAlgorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SobelAndBinarization {

	private BufferedImage img;// 图像
	private int weigth, height;// 图像宽度，高度
	private int[] pixel;// 经过sobel锐化处理后的像素点
	private int max, min;// 处理后的像素最大最小值

	public BufferedImage getImg() {
		return img;
	}
	public SobelAndBinarization() {
		super();
	}

	public void sobel(String srcPath) {
		Gray g = new Gray();
		g.grayImage(srcPath);
		// 得到图像宽度，高度
		weigth = g.getWeigth();
		height = g.getHeight();
		MedianFilter mf = new MedianFilter();
		mf.medianFiltering(g.getImg());
		// 得到经灰度化、中值滤波后的像素
		int[] pix = mf.getNewPixel();

		pixel = new int[weigth * height];
		for (int j = 1; j < height - 1; j++) {
			for (int i = 1; i < weigth - 1; i++) {
				int s1 = pix[i - 1 + (j + 1) * weigth] + 2
						* pix[i + (j + 1) * weigth]
						+ pix[i + 1 + (j + 1) * weigth]
						- pix[i - 1 + (j - 1) * weigth] - 2
						* pix[i + (j - 1) * weigth]
						- pix[i + 1 + (j - 1) * weigth];
				int s2 = pix[i + 1 + (j - 1) * weigth] + 2
						* pix[i + 1 + (j) * weigth]
						+ pix[i + 1 + (j + 1) * weigth]
						- pix[i - 1 + (j - 1) * weigth] - 2
						* pix[i - 1 + (j) * weigth]
						- pix[i - 1 + (j + 1) * weigth];
				int s = Math.abs(s1) + Math.abs(s2);
				if (s < 0)
					s = 0;
				if (s > 255)
					s = 255;
				pixel[j * weigth + i] = s;
				// 得到处理后的像素最大最小值
				if (s > max) {
					max = s;
				}
				if (s < min) {
					min = s;
				}
			}
		}

	}

	/**
	 * 用迭代阈值法进行二值化
	 * 
	 * @return
	 */
	public int[] getBinaryImg() {
		if(weigth * height==0){
			return null;
		}
		int[] newPixel = new int[weigth * height];

		// 获取灰度直方图
		int i, j, t, count1 = 0, count2 = 0, sum1 = 0, sum2 = 0;
		int bp, fp;
		int[] histogram = new int[256];
		for (t = min; t <= max; t++) {
			for (int index = 0; index < weigth * height; index++) {
				if (pixel[index] == t)
					histogram[t]++;
			}
		}

		/*
		 * 迭代法求出最佳分割阈值
		 */
		int T = 0;
		int newT = (max + min) / 2;// 初始阈值
		while (T != newT) {// 求出背景和前景的平均灰度值bp和fp
			for (i = 0; i < T; i++) {
				count1 += histogram[i];// 背景像素点的总个数
				sum1 += histogram[i] * i;// 背景像素点的灰度总值
			}
			bp = (count1 == 0) ? 0 : (sum1 / count1);// 背景像素点的平均灰度值

			for (j = i; j < histogram.length; j++) {
				count2 += histogram[j];// 前景像素点的总个数
				sum2 += histogram[j] * j;// 前景像素点的灰度总值
			}
			fp = (count2 == 0) ? 0 : (sum2 / count2);// 前景像素点的平均灰度值
			T = newT;
			newT = (bp + fp) / 2;
		}
		//System.out.println(max + " " + min + " " + newT);
		BufferedImage bi = new BufferedImage(weigth, height,
				BufferedImage.TYPE_BYTE_BINARY);
		// 二值化
		for (int index = 0; index < weigth * height; index++) {
			if (pixel[index] <= newT) {
				newPixel[index] = Color.WHITE.getRGB();
			} else {
				newPixel[index] = Color.BLACK.getRGB();
			}
			bi.setRGB(index % weigth, index / weigth, newPixel[index]);
		}

//		try {
//			ImageIO.write(bi, "jpg", new File("C:\\Users\\刘开\\Desktop\\02.jpg"));// 输出图像
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		img = bi;
		return newPixel;
	}

	public static void main(String[] args) {
		SobelAndBinarization s = new SobelAndBinarization();
		s.sobel("C:\\Users\\MINGKU\\Desktop\\20170321151053365.jpg");
		s.getBinaryImg();
	}

}
