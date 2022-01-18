package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

/**
 * 图像纹理特征的提取
 */
public class GLCM {

	private int GLCM_DISTEN = 3; // 偏离距离
	private int GLCM_CLASS = 8; // 灰度降级
	
	private int weigth, height;// 图像宽度，高度

	/**
	 * 求图像4个方向的灰度共生矩阵，并计算4个特征值，保存于featherVector[]
	 * 
	 * @param gray
	 *            灰度值
	 * @param angleDirection
	 *            方向
	 * @param featureVector
	 *            保存特征值
	 */
	public void callGLCM(int[] gray, int angleDirection, double[] featureVector) {

		int[] glcm = new int[GLCM_CLASS * GLCM_CLASS];// 灰度共生矩 8 * 8 一维数组
		int[] histImage = new int[weigth * height];// 降级后的图像

		// 降低图像灰度等级，把图像256个灰度级按顺序分为8个区间
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < weigth; j++) {
				histImage[i * weigth + j] = (int) (gray[weigth * i + j]
						* GLCM_CLASS / 256);// 除32取整 =降级后的灰度
				// System.out.print(histImage[i * width + j]+" ");
			}
		}
		// 初始化共生矩阵
		for (int i = 0; i < GLCM_CLASS * GLCM_CLASS; i++) {
			glcm[i] = 0;// 8 x 8 置零

		}
		// 计算灰度共生矩阵
		int k, l;
		// 0°
		if (angleDirection == 0) {
			// 遍历降级后的图像
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < weigth; j++) {
					l = histImage[i * weigth + j];// 取图像点（i,j）像素值
					if (j + GLCM_DISTEN < weigth) {
						k = histImage[i * weigth + j + GLCM_DISTEN];// 取图像点(i,j+d)像素值
						glcm[l * GLCM_CLASS + k]++;// 将灰度共生矩中（value(i,j),value(i,j+d)）值加1
					}
				}
			}
		}
		// 90°
		else if (angleDirection == 90) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < weigth; j++) {
					l = histImage[i * weigth + j];// 取图像点（i,j）像素值
					if (i + GLCM_DISTEN < height) {
						k = histImage[(i + GLCM_DISTEN) * weigth + j];// 取图像点（i+d,j）像素值
						glcm[l * GLCM_CLASS + k]++;// 将灰度共生矩中（value(i,j),value(i+d,j)）值加1
					}
				}
			}
		}
		// 45°
		else if (angleDirection == 45) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < weigth; j++) {
					l = histImage[i * weigth + j];// 取图像点（i,j）像素值
					if (j + GLCM_DISTEN < weigth && i + GLCM_DISTEN < height) {
						k = histImage[(i + GLCM_DISTEN) * weigth + j
								+ GLCM_DISTEN];// 取图像点（i+d,j+d）像素值
						glcm[l * GLCM_CLASS + k]++;// 将灰度共生矩中（value(i,j),value(i+d,j+d)）值加1
					}
				}
			}
		}
		// 135°
		else if (angleDirection == 135) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < weigth; j++) {
					l = histImage[i * weigth + j];// 取图像点（i,j）像素值

					if (j - GLCM_DISTEN >= 0 && i - GLCM_DISTEN >= 0) {
						k = histImage[(i - GLCM_DISTEN) * weigth + j
								- GLCM_DISTEN];// 取图像点（i+d,j-d）像素值
						glcm[l * GLCM_CLASS + k]++;// 将灰度共生矩中（value(i,j),value(i+d,j-d)）值加1
					}
				}
			}
		}
		// 计算特征值
		double entropy = 0, energy = 0, contrast = 0, homogenity = 0;

		for (int i = 0; i < GLCM_CLASS; i++) {
			for (int j = 0; j < GLCM_CLASS; j++) {
				// 纹理熵
				if (glcm[i * GLCM_CLASS + j] > 0)
					entropy -= glcm[i * GLCM_CLASS + j]
							* Math.log((glcm[i * GLCM_CLASS + j]))
							/ Math.log(10);
				// 纹理相关性
				energy += glcm[i * GLCM_CLASS + j] * glcm[i * GLCM_CLASS + j];
				// 纹理对比度
				contrast += (i - j) * (i - j) * glcm[i * GLCM_CLASS + j];
				//纹理一致性
				homogenity += 1.0 / (1 + (i - j) * (i - j))
						* glcm[i * GLCM_CLASS + j];
			}
		}
		// 返回特征值
		featureVector[0] = entropy;
		featureVector[1] = energy;
		featureVector[2] = contrast;
		featureVector[3] = homogenity;
	}

	/**
	 * 纹理特征：灰度共生矩
	 * @param srcPath原图片路径
	 * @return 8 个特征值
	 */
	public double[] getEigenValue(String srcPath) {
		double[] expStadv = new double[8];// 保存 8 个特征值
		try {
			BufferedImage reader = ImageIO.read(new File(srcPath));
			if(reader==null){
				return expStadv;
			}
			weigth = reader.getWidth();
			height = reader.getHeight();

			int[] gray = new int[weigth * height];
			for (int i = 0; i < weigth; i++) {// 读入所有像素，转换图像信号,使其灰度化
				for (int j = 0; j < height; j++) {
					int rgb = reader.getRGB(i, j);
					gray[j * weigth + i] = (int) ((0.3 * ((rgb & 0xff0000) >> 16) + 0.59 * ((rgb & 0xff00) >> 8)) + 0.11 * ((rgb & 0xff)));
				}
			}

			// 垂直 0°
			double[] GLCM_0 = new double[4];
			callGLCM(gray, 0, GLCM_0);
			double Average0 = 0.25 * (GLCM_0[0] + GLCM_0[1] + GLCM_0[2] + GLCM_0[3]);
			//System.out.println(" 0°均值：" + Average0);
			double temp0 = 1;
			for (int i = 0; i < 4; i++) {
				temp0 += (GLCM_0[i] - Average0) * (GLCM_0[i] - Average0);
			}
			double StaDev0 = Math.sqrt(0.25 * temp0);
			//System.out.println(" 0°标准差：" + StaDev0);
			// 水平 90°
			double[] GLCM_90 = new double[4];
			callGLCM(gray, 90, GLCM_90);
			double Average90 = 0.25 * (GLCM_90[0] + GLCM_90[1] + GLCM_90[2] + GLCM_90[3]);
			//System.out.println(" 90°均值：" + Average0);
			double temp90 = 1;
			for (int i = 0; i < 4; i++) {
				temp90 += (GLCM_90[i] - Average90) * (GLCM_90[i] - Average90);
			}
			double StaDev90 = Math.sqrt(0.25 * temp90);
			//System.out.println(" 90°标准差：" + StaDev90);
			// 45°
			double[] GLCM_45 = new double[4];
			callGLCM(gray, 45, GLCM_45);
			double Average45 = 0.25 * (GLCM_45[0] + GLCM_45[1] + GLCM_45[2] + GLCM_45[3]);
			//System.out.println(" 45°均值：" + Average0);
			double temp45 = 1;
			for (int i = 0; i < 4; i++) {
				temp45 += (GLCM_45[i] - Average45) * (GLCM_45[i] - Average45);
			}
			double StaDev45 = Math.sqrt(0.25 * temp45);
			//System.out.println(" 45°标准差：" + StaDev45);
			// 135°
			double[] GLCM_135 = new double[4];
			callGLCM(gray, 135, GLCM_135);
			double Average135 = 0.25 * (GLCM_135[0] + GLCM_135[1] + GLCM_135[2] + GLCM_135[3]);
			//System.out.println(" 135°均值：" + Average135);
			double temp135 = 1;
			for (int i = 0; i < 4; i++) {
				temp135 += (GLCM_135[i] - Average135)
						* (GLCM_135[i] - Average135);
			}
			double StaDev135 = Math.sqrt(0.25 * temp135);
			//System.out.println(" 135°标准差：" + StaDev135);
			DecimalFormat df = new DecimalFormat("0.0000000000");
			expStadv[0] = Double.parseDouble(df.format(Average0));
			expStadv[1] = Double.parseDouble(df.format(Average90));
			expStadv[2] = Double.parseDouble(df.format(Average45));
			expStadv[3] = Double.parseDouble(df.format(Average135));
			expStadv[4] = Double.parseDouble(df.format(StaDev0));
			expStadv[5] = Double.parseDouble(df.format(StaDev90));
			expStadv[6] = Double.parseDouble(df.format(StaDev45));
			expStadv[7] = Double.parseDouble(df.format(StaDev135));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expStadv;

	}

//	public static void main(String[] args) {
//		GLCM glcm = new GLCM();
//		glcm.getEigenValue("C:\\Users\\刘开\\Desktop\\图库\\accordion\\02.jpg");
//
//	}

}
