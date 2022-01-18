package match;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mainAlgorithm.EdgeDetector;
import mainAlgorithm.EdgeHistogram;
import mainAlgorithm.HistogramIntersecting;

/**
 * 边缘方向直方图法
 * 
 */
public class ShapeEdgeMathch {

	/**
	 * 颜色特征，调用文件数据
	 * 
	 * @param srcPath
	 * @return 检索图片以及差距
	 */
	public static Map<String, Double> seMatch(String srcPath) {

		String style = srcPath.substring(srcPath.lastIndexOf('.') + 1,
				srcPath.length());// 文件类型
		String fileDis = srcPath.substring(0, srcPath.lastIndexOf('\\'));// 图片所在文件夹
		String fileName = fileDis.substring(fileDis.lastIndexOf('\\') + 1,
				fileDis.length());// 文件相关名字，用于在数据库中索引
		Map<String, Double> lhm = new LinkedHashMap<String, Double>();// 返回结果
		// 得到边缘直方图
		EdgeDetector edgeDetector = new EdgeDetector();
		edgeDetector.setSourceImage(srcPath); // 设置边缘处理的参数
		edgeDetector.setThreshold(128);
		edgeDetector.setWidGaussianKernel(5);
		edgeDetector.process();
		EdgeHistogram eh = new EdgeHistogram();
		//得到归一化后的边缘方向直方图
		double[] result = eh.edgeHistogram(edgeDetector.getFileName(),
				edgeDetector.getName(), edgeDetector.getEdgeImage(), true);

		String pathName = "C:\\Users\\MINGKU\\Desktop\\文件2\\" + fileName;// 通过srcPath获得路径

		File file = new File(pathName);
		BufferedReader br = null;// 输入流
		Map<String, Double> map = new HashMap<String, Double>();// 保存图片与数据库中符合条件的图片的距离值
		if (file.isDirectory()) {
			// 获得一个目录下的所有文件
			File[] lists = file.listFiles();
			// System.out.println(lists.length);
			if (lists != null) {
				for (int l = 0; l < lists.length; l++) {
					try {
						br = new BufferedReader(new FileReader(lists[l]));
						String read = "";
						double[] result2 = new double[36];
						// 初始化result2
						int i = 0;
						while ((read = br.readLine()) != null) {
							result2[i] = Double.parseDouble(read);
							i++;
						}
						br.close();
						// 调用一维数组的欧式距离度量
						double similarity = FeatureMeasure.euclideanDistance(
								result, result2);// 得到欧式距离
						map.put(lists[l].getAbsolutePath(), similarity);
					} catch (Exception e) {
					}
				}

				// 对map按值排序
				List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(
						map.entrySet());
				Collections.sort(entryList,
						new Comparator<Map.Entry<String, Double>>() {

							@Override
							public int compare(Entry<String, Double> o1,
									Entry<String, Double> o2) {
								return o1.getValue().compareTo(o2.getValue());

							}
						});
				Iterator<Map.Entry<String, Double>> iter = entryList.iterator();
				Map.Entry<String, Double> tmpEntry = null;

				// 返回最多12个路径
				for (int i = 0; i < 12; i++) {
					if (iter.hasNext()) {
						tmpEntry = iter.next();
						String key = tmpEntry.getKey();
						String path = fileDis
								+ "\\"
								+ key.substring(key.lastIndexOf('\\') + 1,
										key.lastIndexOf('.')) + "." + style;
						lhm.put(path, tmpEntry.getValue());
						System.out.println(path+" " +tmpEntry.getValue());
					}
				}
				// 遍历map
				// for (Map.Entry<String, Double> entry : lhm.entrySet()) {
				// System.out.println("Key = " + entry.getKey() + ", Value = "
				// + entry.getValue());
				// }

			}
		}
		return lhm;
	}

	public static void main(String[] args) {
		seMatch("C:\\Users\\刘开\\Desktop\\\\图库\\accordion\\accordion10.jpg");
	}

}
