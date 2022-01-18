package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;

/**
 * 直方图相交法(基于颜色的检索)
 * 
 */
public class HistogramIntersecting {

	/**
	 * 直方图相交法得到H(k)
	 * @param srcPath原图片路径
	 * @param printFlag 是否输出到文件
	 * @return直方图相交法特征值
	 */
	public static double[][] getHistogram1(String srcPath,boolean printFlag) {

		String str= srcPath.substring(0,srcPath.lastIndexOf('\\'));
		String fileName = str.substring(str.lastIndexOf('\\') + 1,str.length());// 文件相关名字，用于在数据库中索引, 通过srcPath获得路径，保存相应的输出文件
		String  name= srcPath.substring(srcPath.lastIndexOf('\\')+1, srcPath.lastIndexOf('.'));
		//System.out.println(pathName + " "+fileName);
		BufferedImage img;
		double[][] histgram = new double[3][256];
		try {
			img = ImageIO.read(new File(srcPath));
			if(img==null){
				return histgram;
			}
			int w = img.getWidth();// 图片宽度
			int h = img.getHeight();// 图片高度
			int[] pix = new int[w * h];// 像素数组
			int r, g, b;// 记录R、G、B的值
			pix = img.getRGB(0, 0, w, h, pix, 0, w);// 将图片的像素值存到数组里
			for (int i = 0; i < pix.length; i++) {
				// 提取R,G,B
				r = pix[i] >> 16 & 0xff;
				g = pix[i] >> 8 & 0xff;
				b = pix[i] & 0xff;
				histgram[0][r]++;
				histgram[1][g]++;
				histgram[2][b]++;
			}
			double red = 0, green = 0, blue = 0;
			for (int j = 0; j < 256; j++) {
				red += histgram[0][j];
				green += histgram[1][j];
				blue += histgram[2][j];
			}
			for (int j = 0; j < 256; j++)// 将直方图每个像素值的总个数进行量化
			{
				histgram[0][j] /= red;
				histgram[1][j] /= green;
				histgram[2][j] /= blue;
			}

			if(printFlag){
				/*
				 * 将直方图相交法的特征值保存到文件
				 * 先建立好一个名为文件的文件夹，将对应数据放入其中
				 */
				String save = "C:\\Users\\MINGKU\\Desktop\\文件\\"+fileName;
				File file = new File(save);
				// 如果文件夹不存在则创建
				if (!file.exists() && !file.isDirectory()) {
					file.mkdir();
				}
				String destPath = save + "\\" + name + ".txt";
				PrintWriter pw = new PrintWriter(new FileWriter(destPath));// 输出到文件
				for (int i = 0; i < histgram.length; i++) {
					for (int j = 0; j < histgram[0].length; j++) {
						pw.print(histgram[i][j] + " ");
					}
					pw.println();
				}
				pw.close();
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}

		return histgram;
	}
	/*
	 * 统计RGB,归一化后求交得到两个图片的相似度
	 */
	public static double getSimilarity1(double[][] Qhistgram,
			double[][] Dhistgram) {
		double similar = 0.0;// 相似度
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < Qhistgram[i].length; j++) {
				similar += Math.min(Qhistgram[i][j], Dhistgram[i][j]);
			}
		}
		similar = similar / 3;//Qhistgram[i][j]之和为3
		return similar;
	}

	// 欧式距离求图片的相似度
	public static double getSimilarity2(double[][] Qhistgram,
			double[][] Dhistgram) {
		double similar = (double) 0.0;// 相似度
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < Qhistgram[i].length; j++) {
				similar += (Qhistgram[i][j] - Dhistgram[i][j])
						* (Qhistgram[i][j] - Dhistgram[i][j]);
			}
		}
		//similar = similar / 6;
		similar = Math.sqrt(similar);
		return similar;
	}

//	public static void main(String[] args) throws IOException {
//		double similarity = (double) 0.0;
//
//		double[][] Qhistgram = getHistogram1("C:\\Users\\刘开\\Desktop\\图库\\accordion\\accordion1.jpg",false);
//		double[][] Dhistgram = getHistogram1("C:\\Users\\刘开\\Desktop\\图库\\accordion\\accordion1.jpg",false);
//		similarity = getSimilarity1(Qhistgram, Dhistgram);
//		System.out.print("传统的RGB直方图相交法求得的图像相似度:");
//		System.out.println(similarity);
//
//		System.out.print("传统的欧式距离求得的图像相似度:");
//		similarity = 1 - getSimilarity2(Qhistgram, Dhistgram);
//		System.out.println(similarity);
//
//	}

}
