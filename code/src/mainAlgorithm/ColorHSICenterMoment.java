package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图像颜色特征提取
 * 
 */
public class ColorHSICenterMoment {
	/**
	 * 将图像从RGB颜色空间转至HSI颜色空间,以HSI空间的三个分量的前三阶中心矩
	 * 
	 * @param srcPath
	 *            原图片路径
	 */
	public static float[][] rgbTohsi(String srcPath) {
		float[][] mhsi = new float[3][3];// 3个分量的前三阶中心矩
		try {
			
			BufferedImage bufferedImage = ImageIO.read(new File(srcPath));
			if(bufferedImage==null){
				return mhsi;
			}
			int w = bufferedImage.getWidth();
			int h = bufferedImage.getHeight();
			// 每个像素的h s i值
			float[] hh = new float[w * h];
			float[] ss = new float[w * h];
			float[] ii = new float[w * h];
			
			float sumH = 0, sumS = 0, sumI = 0;// 所有像素三个分量之和
			float sumH2 = 0, sumS2 = 0, sumI2 = 0;// 所有像素三个分量减去M1的平方之和
			float sumH3 = 0, sumS3 = 0, sumI3 = 0;// 所有像素三个分量减去M1的三次方之和

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					int color = bufferedImage.getRGB(i, j);
					int r = (color >> 16) & 0xff;
					int g = (color >> 8) & 0xff;
					int b = color & 0xff;
					// System.out.println(r+" "+g+" "+b);
					// int max = Math.max(Math.max(r, g), b);
					int min = Math.min(Math.min(r, g), b);

					float hue = 0;
					float sat = 0;
					// Intensity分量[0,1]
					float in = (float) ((r + g + b) / 3) / 255;
					// Saturation分量[0,1]
					sat = 1 - (float) (3 * min) / (r + g + b);
					// 计算theta角
					float numerator = (r - g + r - b) / 2;
					float denominator = (float) Math.sqrt(Math.pow((r - g), 2)
							+ (r - b) * (g - b));
					// 计算Hue分量
					if (denominator != 0) {
						float theta = (float) ((float) Math.acos(numerator
								/ denominator) * 180 / 3.1415926);
						if (b <= g) {
							hue = theta;
						} else {
							hue = 360 - theta;
						}
					} else {
						hue = 0;
					}
					hh[j * w + i] = hue;
					ss[j * w + i] = sat;
					ii[j * w + i] = in;
					sumH += hue;
					sumS += sat;
					sumI += in;
				}
			}
			mhsi[0][0] = sumH / (w * h);
			mhsi[1][0] = sumS / (w * h);
			mhsi[2][0] = sumI / (w * h);
			for (int i = 0; i < w * h; i++) {
				sumH2 += (hh[i] - mhsi[0][0]) * (hh[i] - mhsi[0][0]);
				sumS2 += (ss[i] - mhsi[1][0]) * (ss[i] - mhsi[1][0]);
				sumI2 += (ii[i] - mhsi[2][0]) * (ii[i] - mhsi[2][0]);

				sumH3 += sumH2 * (hh[i] - mhsi[0][0]);
				sumS3 += sumS2 * (hh[i] - mhsi[1][0]);
				sumI3 += sumI2 * (hh[i] - mhsi[2][0]);
			}
			// 3个分量的二阶中心矩
			mhsi[0][1] = (float) Math.pow(sumH2 / (w * h), 1 / 2);
			mhsi[1][1] = (float) Math.pow(sumS2 / (w * h), 1 / 2);
			mhsi[2][1] = (float) Math.pow(sumI2 / (w * h), 1 / 2);
			// 3个分量的三阶中心矩
			mhsi[0][2] = (float) Math.pow(sumH3 / (w * h), 1 / 3);
			mhsi[1][2] = (float) Math.pow(sumS3 / (w * h), 1 / 3);
			mhsi[2][2] = (float) Math.pow(sumI3 / (w * h), 1 / 3);

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (Float.isNaN(mhsi[i][j]))
						mhsi[i][j] = 0;
					//System.out.println(mhsi[i][j]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mhsi;
	}

	public static void main(String[] args) throws IOException {
		rgbTohsi("C:\\Users\\MINGKU\\Desktop\\综合课程设计\\图库\\accordion\\02.jpg");
	}
}
