package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 计算7种HU不变矩，离心率
 * 
 */
public class HuMoment {

	public static void huMoment(String srcPath) {
		BufferedImage reader;
		try {
			reader = ImageIO.read(new File(srcPath));
			huMoment(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static double[] huMoment(int weigth,int height,int[] gray) {
		double[] moment = new double[8];// 7个不变矩和一个离心率		
		
		double m00 = 0, m01 = 0, m10 = 0, m11 = 0, m02 = 0, m20 = 0, m12 = 0, m21 = 0, m03 = 0, m30 = 0; // 几何矩
		double u00 = 0, u01 = 0, u10 = 0, u11 = 0, u02 = 0, u20 = 0, u12 = 0, u21 = 0, u03 = 0, u30 = 0; // 中心矩
		double t02 = 0, t20 = 0, t11, t12 = 0, t21 = 0, t03 = 0, t30 = 0;// 规范化后的中心矩
		for (int i = 0; i < height; i++) {// 读入所有像素，转换图像信号,使其灰度化
			for (int j = 0; j < weigth; j++) {
				m00 += gray[i * weigth + j];
				m01 += (j + 1) * gray[i * weigth + j];
				m10 += (i + 1) * gray[i * weigth + j];
				m11 += (i + 1) * (j + 1) * gray[i * weigth + j];
				m02 += (j + 1) * (j + 1) * gray[i * weigth + j];
				m20 += (i + 1) * (i + 1) * gray[i * weigth + j];
				m12 += (i + 1) * (j + 1) * (j + 1) * gray[i * weigth + j];
				m21 += (i + 1) * (i + 1) * (j + 1) * gray[i * weigth + j];
				m03 += (j + 1) * (j + 1) * (j + 1) * gray[i * weigth + j];
				m30 += (i + 1) * (i + 1) * (i + 1) * gray[i * weigth + j];
			}
		}
		double ii = m10 / m00, jj = m01 / m00;
		for (int i = 0; i < height; i++) {// 读入所有像素，转换图像信号,使其灰度化
			for (int j = 0; j < weigth; j++) {
				u00 += gray[i * weigth + j];
				u01 += (j + 1 - jj) * gray[i * weigth + j];
				u10 += (i + 1 - ii) * gray[i * weigth + j];
				u11 += (i + 1 - ii) * (j + 1 - jj) * gray[i * weigth + j];
				u02 += (j + 1 - jj) * (j + 1 - jj) * gray[i * weigth + j];
				u20 += (i + 1 - ii) * (i + 1 - ii) * gray[i * weigth + j];
				u12 += (i + 1 - ii) * (j + 1 - jj) * (j + 1 - jj)
						* gray[i * weigth + j];
				u21 += (i + 1 - ii) * (i + 1 - ii) * (j + 1 - jj)
						* gray[i * weigth + j];
				u03 += (j + 1 - jj) * (j + 1 - jj) * (j + 1 - jj)
						* gray[i * weigth + j];
				u30 += (i + 1 - ii) * (i + 1 - ii) * (i + 1 - ii)
						* gray[i * weigth + j];
			}
		}
		// 计算规范化后的中心矩: tij=uij/pow(u00,((i+j+2)/2)
		t20 = u20 / Math.pow(u00, 2);
		t02 = u02 / Math.pow(u00, 2);
		t11 = u02 / Math.pow(u00, 2);
		t30 = u30 / Math.pow(u00, 2.5);
		t03 = u03 / Math.pow(u00, 2.5);
		t12 = u12 / Math.pow(u00, 2.5);
		t21 = u21 / Math.pow(u00, 2.5);

		double s1 = 0, s2 = 0, s3 = 0, s4 = 0, s5 = 0;// 临时变量，
		// 计算中间变量
		s1 = t20 - t02;
		s2 = t30 - 3 * t12;
		s3 = 3 * t21 - t03;
		s4 = t30 + t12;
		s5 = t21 + t03;

		// 计算不变矩
		moment[0] = s1;
		moment[1] = s1 * s1 + 4 * t11 * t11;
		moment[2] = s2 * s2 + s3 * s3;
		moment[3] = s4 * s4 + s5 * s5;
		moment[4] = s2 * s4 * (s4 * s4 - 3 * s5 * s5) + s3 * s5
				* (3 * s4 * s4 - s5 * s5);
		moment[5] = s1 * (s4 * s4 - s5 * s5) + 4 * t11 * s4 * s5;
		moment[6] = s3 * s4 * (s4 * s4 - 3 * s5 * s5) - s2 * s5
				* (3 * s4 * s4 - s5 * s5);

		moment[7] = ((m20 - m02) * (m20 - m02) + 4 * m11)
				/ (weigth > height ? weigth : height);//近似计算
//		for (int i = 0; i < 8; i++) {
//			System.out.println(moment[i]);
//		}

		return moment;
	}
	public static double[] huMoment(BufferedImage img) {
		double[] moment = new double[8];// 7个不变矩和一个离心率
		if(img==null){
			return moment;
		}

		int weigth = img.getWidth();
		int height = img.getHeight();

		int[] gray = new int[weigth * height];
		for (int i = 0; i < weigth; i++) {// 读入所有像素，得到灰度值
			for (int j = 0; j < height; j++) {
				int rgb = img.getRGB(i, j);
				gray[j * weigth + i] = (int) ((0.3 * ((rgb & 0xff0000) >> 16) + 0.59 * ((rgb & 0xff00) >> 8)) + 0.11 * ((rgb & 0xff)));
			}
		}
		moment=huMoment(weigth, height,gray);
		return moment;
	}

	public static void main(String[] args) {
		HuMoment.huMoment("C:\\Users\\MINGKU\\Desktop\\02.jpg");
	}
}
