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

import mainAlgorithm.HistogramIntersecting;

/**
 * 直方图相交法匹配
 * 
 */
public class HistogramIntersectingMathch {

	/**
	 * 颜色特征，调用文件数据
	 * @param srcPath
	 * @return 检索图片以及差距
	 */
	public static Map<String, Double> hiMatch(String srcPath) {

		String style = srcPath.substring(srcPath.lastIndexOf('.') + 1,
				srcPath.length());// 文件类型
		String fileDis = srcPath.substring(0, srcPath.lastIndexOf('\\'));// 图片所在文件夹
		String fileName = fileDis.substring(fileDis.lastIndexOf('\\') + 1,fileDis.length());// 文件相关名字，用于在数据库中索引
		Map<String, Double> lhm = new LinkedHashMap<String, Double>();// 返回结果
		double[][] Qhistgram = HistogramIntersecting.getHistogram1(srcPath,
				false);
		String pathName = "C:\\Users\\MINGKU\\Desktop\\文件\\"+fileName;// 通过srcPath获得路径
		
		File file = new File(pathName);
		BufferedReader br = null;// 输入流
		Map<String, Double> map = new HashMap<String, Double>();// 保存图片与数据库中符合条件的图片的距离值
		if (file.isDirectory()) {
			// 获得一个目录下的所有文件
			File[] lists = file.listFiles();
			//System.out.println(lists.length);
			if (lists != null) {
				for (int l = 0; l < lists.length; l++) {
					try {
						br = new BufferedReader(new FileReader(lists[l]));
						String read = "";
						int i = 0;
						double[][] Dhistgram = new double[3][256];
						// 初始化Dhistgram
						while ((read = br.readLine()) != null) {
							String[] sp = read.split("( )+");
							for (int j = 0; j < sp.length; j++) {
								Dhistgram[i][j] = Double.parseDouble(sp[j]);
							}
							i++;
						}
						br.close();
						//调用二维数组的欧式距离度量
						double similarity = FeatureMeasure.euclideanDistance(
								Qhistgram, Dhistgram);// 得到欧式距离
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
						System.out.println(path+" "+tmpEntry.getValue());
					}
				}
				// 遍历map
//				for (Map.Entry<String, Double> entry : lhm.entrySet()) {
//					System.out.println("Key = " + entry.getKey() + ", Value = "
//							+ entry.getValue());
//				}

			}
		}
		return lhm;
	}

	public static void main(String[] args) {
		hiMatch("C:\\Users\\刘开\\Desktop\\2\\anchor\\anchor1.jpg");
	}

}
