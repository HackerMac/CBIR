package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

/*
 * 中值滤波进行图像降噪
 */
public class MedianFilter {
	
	private BufferedImage img;// 图像
	private int weigth,height;//图像宽度，高度
	private int[] newPixel;//经过中值滤波处理后的像素点
	
	public int getWeigth() {
		return weigth;
	}

	public int getHeight() {
		return height;
	}

	public int[] getNewPixel() {
		return newPixel;
	}

	/**
	 * 中值滤波
	 * @param srcPath 原图片路径
	 */
	public void medianFiltering(String srcPath) {
		try {
			medianFiltering(ImageIO.read(new File(srcPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 中值滤波，重载可以进行灰度化+中值滤波
	 * @param img 图片
	 */
	public void medianFiltering(BufferedImage img) {
		try {
			if(img==null){
				return;
			}
			weigth = img.getWidth();
			height = img.getHeight();
			int[] pix = new int[weigth * height];
			img.getRGB(0, 0, weigth, height, pix, 0, weigth);
			
			newPixel=new int[weigth * height];
			//进行中值滤波
			int[] temp = new int[9];
			ColorModel cm = ColorModel.getRGBdefault();
			int r = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < weigth; x++) {
					if (x != 0 && x != weigth - 1 && y != 0 && y != height - 1) {
						temp[0] = cm.getRed(pix[x - 1 + (y - 1) * weigth]);
						temp[1] = cm.getRed(pix[x + (y - 1) * weigth]);
						temp[2] = cm.getRed(pix[x + 1 + (y - 1) * weigth]);
						temp[3] = cm.getRed(pix[x - 1 + (y) * weigth]);
						temp[4] = cm.getRed(pix[x + (y) * weigth]);
						temp[5] = cm.getRed(pix[x + 1 + (y) * weigth]);
						temp[6] = cm.getRed(pix[x - 1 + (y + 1) * weigth]);
						temp[7] = cm.getRed(pix[x + (y + 1) * weigth]);
						temp[8] = cm.getRed(pix[x + 1 + (y + 1) * weigth]);
						Arrays.sort(temp);
						r = temp[4];
						newPixel[y * weigth + x] = 255 << 24 | r << 16 | r << 8 | r;
					} else {
						newPixel[y * weigth + x] = pix[y * weigth + x];
					}
				}
			}		
			img.setRGB(0, 0, weigth, height, newPixel, 0, weigth);
			this.img=img;
			//ImageIO.write(img, "jpg", new File("C:\\Users\\刘开\\Desktop\\图库\\accordion\\oo33.jpg"));//输出中值滤波后的图像
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		MedianFilter mf = new MedianFilter();
		mf.medianFiltering("C:\\Users\\MINGKU\\Desktop\\综合课程设计\\图库\\accordion\\02.jpg");
	}
}
