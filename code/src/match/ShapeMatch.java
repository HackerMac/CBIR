package match;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import mainAlgorithm.EdgeDetector;
import mainAlgorithm.HuMoment;
import mainAlgorithm.SobelAndBinarization;
import database.JDBCUtil;
/**
 * 形状不变矩
 *
 */
public class ShapeMatch {

	public static Map<String, Double> shapeMatch(String srcPath) {
		String str= srcPath.substring(0,srcPath.lastIndexOf('\\'));
		String fileName = str.substring(str.lastIndexOf('\\') + 1,str.length());// 文件相关名字，用于在数据库中索引
		Map<String, Double> lhm = new LinkedHashMap<String, Double>();// 返回结果
		//System.out.println(fileName);
		Map<String, Double> map = new HashMap<String, Double>();// 保存图片与数据库中符合条件的图片的距离值
		
		//两种方法进行边缘处理
		// 灰度化，中值滤波，soble，二值化
//		SobelAndBinarization s = new SobelAndBinarization();
//		s.sobel(srcPath);
//		s.getBinaryImg();
//		//得到不变矩
//		double[] hum = HuMoment.huMoment(s.getImg());

		EdgeDetector edgeDetector = new EdgeDetector();
		edgeDetector.setSourceImage(srcPath); // 设置边缘处理的参数
		edgeDetector.setThreshold(128);
		edgeDetector.setWidGaussianKernel(5);
		edgeDetector.process();
		 // 进行边缘处理
		double[] hum = HuMoment.huMoment(edgeDetector.getEdgeImage());
		// 连接数据库
		Connection conn = (Connection) JDBCUtil.getMysqlConn();
		String sql = "select * from shape where Name = ?";
		PreparedStatement preStmt;
		try {		
			preStmt = conn.prepareStatement(sql);
			preStmt.setObject(1, fileName);
			ResultSet rs = preStmt.executeQuery();

			double[] hum2 = new double[8];
		
			while (rs.next()) {
				int i = 0;
				// int id=rs.getInt(1);
				hum2[i++] = rs.getDouble(3);
				hum2[i++] = rs.getDouble(4);
				hum2[i++] = rs.getDouble(5);
				hum2[i++] = rs.getDouble(6);
				hum2[i++] = rs.getDouble(7);
				hum2[i++] = rs.getDouble(8);
				hum2[i++] = rs.getDouble(9);
				hum2[i++] = rs.getDouble(10);
				String path = rs.getString(11);
				// System.out.println(path);
				//进行相似性衡量
				double value = FeatureMeasure.euclideanDistance2(hum, hum2);
				map.put(path, value);
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
					System.out.println(tmpEntry.getKey()+tmpEntry.getValue());
					lhm.put(tmpEntry.getKey(), tmpEntry.getValue());
				}
			}
			// 可以将查找到的值写入类，然后返回相应的对象
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		
		return lhm;
	}
	public static void main(String[] args) {
		shapeMatch("C:\\Users\\刘开\\Desktop\\1\\ant\\image_0036.jpg");
		
	}

}
