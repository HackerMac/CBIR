package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

/**
 * 边缘方向角直方图
 * 
 */
public class EdgeHistogram {

	/**
	 * 得到边缘方向角直方图
	 * 
	 * @param srcPath原图片路径
	 * @param printFlag
	 *            是否输出到文件
	 */
	public double[] edgeHistogram(String srcPath, boolean printFlag) {
		String str = srcPath.substring(0, srcPath.lastIndexOf('\\'));
		String fileName = str.substring(str.lastIndexOf('\\') + 1, str.length());// 文件相关名字，用于在数据库中索引,
																			// 通过srcPath获得路径，保存相应的输出文件
		String name = srcPath.substring(srcPath.lastIndexOf('\\') + 1,
				srcPath.lastIndexOf('.'));
		BufferedImage reader;
		try {
			reader = ImageIO.read(new File(srcPath));
			double[] result=edgeHistogram(fileName,name,reader, printFlag);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public double[] edgeHistogram(String fileName,String name,int weigth, int height, int[] gray,
			boolean printFlag) {
		//System.out.println(weigth + " " + height + " " + gray.length);
		// 计算梯度矢量Gx,Gy
		// 计算边缘方向；范围：[-pi,pi]；弧度->角度： [0,360]
		double[][] dx = new double[weigth][height];
		double[][] dy = new double[weigth][height];
		double[][] theta = new double[weigth][height];// 边缘方向theta(x,y) =
														// atan(dy/dx)
		for (int i = 0; i < weigth - 1; i++) {// 读入所有像素，转换图像信号,使其灰度化
			for (int j = 0; j < height - 1; j++) {
				dx[i][j] = gray[(j + 1) * weigth + i] - gray[j * weigth + i];
				dy[i][j] = gray[j * weigth + i + 1] - gray[j * weigth + i];
				theta[i][j] = Math.atan(dx[i][j] / dy[i][j]) * 180 / 3.1415926 + 180;
			}
		}
		// 将[0,360]每10度分为一组，那么方向被量化为36 bin
		int[] num = new int[36];// 存储各方向像素数目的数组
		int sum = 0;
		for (int i = 0; i < weigth; i++) {
			for (int j = 0; j < height; j++) {
				if (gray[j * weigth + i] == 0) {
					num[(int) theta[i][j] / 10]++;
					sum++;
				}
			}
		}
		double[] result = new double[36];// 存储各方向像素数目的数组
		// 进行归一化
		for (int i = 0; i < num.length; i++) {
			result[i] = (double) num[i] / sum;

		}
		if (printFlag) {
			/*
			 * 将直方图相交法的特征值保存到文件 先建立好一个名为文件的文件夹，将对应数据放入其中
			 */
			String save = "C:\\Users\\MINGKU\\Desktop\\文件2\\" + fileName;
			File file = new File(save);
			// 如果文件夹不存在则创建
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir();
			}
			String destPath = save + "\\" + name + ".txt";
			PrintWriter pw;
			try {
				pw = new PrintWriter(new FileWriter(destPath));
				for (int i = 0; i < result.length; i++) {
					pw.println(result[i]);// 输出到文件
				}
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public double[] edgeHistogram(String fileName,String name,BufferedImage img, boolean printFlag) {
		int weigth = img.getWidth();
		int height = img.getHeight();

		int[] gray = new int[weigth * height];
		for (int i = 0; i < weigth; i++) {// 读入所有像素，得到灰度值
			for (int j = 0; j < height; j++) {
				int rgb = img.getRGB(i, j);
				gray[j * weigth + i] = (int) ((0.3 * ((rgb & 0xff0000) >> 16) + 0.59 * ((rgb & 0xff00) >> 8)) + 0.11 * ((rgb & 0xff)));
			}
		}
		double[] result=edgeHistogram(fileName,name,weigth, height, gray, printFlag);
		return result;
	}

	public static void main(String[] args) {

		//EdgeHistogram.edgeHistogram("C:\\Users\\刘开\\Desktop\\02.jpg", true);
		EdgeDetector edgeDetector = new EdgeDetector();
		edgeDetector.setSourceImage("C:\\Users\\MINGKU\\Desktop\\20170321151053365.jpg"); // 设置边缘处理的参数
		edgeDetector.setThreshold(128);
		edgeDetector.setWidGaussianKernel(5);
		edgeDetector.process();
		EdgeHistogram e=new EdgeHistogram();
		e.edgeHistogram(edgeDetector.getFileName(),edgeDetector.getFileName(),edgeDetector.getEdgeImage(), true);
	}
}
