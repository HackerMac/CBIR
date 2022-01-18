package match;

/**
 * HSI中心距法匹配
 */
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
import mainAlgorithm.ColorHSICenterMoment;

import database.JDBCUtil;

public class HSICenterMomentMatch {

	public static Map<String, Double> hsiCenterMatch(String srcPath) {
		String str= srcPath.substring(0,srcPath.lastIndexOf('\\'));
		String fileName = str.substring(str.lastIndexOf('\\') + 1,str.length());// 文件相关名字，用于在数据库中索引
		Map<String, Double> lhm = new LinkedHashMap<String, Double>();// 返回结果
		//System.out.println(fileName);
		Map<String, Double> map = new HashMap<String, Double>();// 保存图片与数据库中符合条件的图片的距离值	
		
		float[][] mshi = ColorHSICenterMoment.rgbTohsi(srcPath);
		
		// 连接数据库
		Connection conn = (Connection) JDBCUtil.getMysqlConn();
		String sql = "select * from color where Name = ?";
		PreparedStatement preStmt;
		try {		
			preStmt = conn.prepareStatement(sql);
			preStmt.setObject(1, fileName);
			ResultSet rs = preStmt.executeQuery();			
			float[][] mshi2 = new float[9][3];
			while (rs.next()) {
				
				// int id=rs.getInt(1);
				mshi2[0][0] = rs.getFloat(3);
				mshi2[0][1] = rs.getFloat(4);
				mshi2[0][2] = rs.getFloat(5);
				mshi2[1][0] = rs.getFloat(6);
				mshi2[1][1] = rs.getFloat(7);
				mshi2[1][2] = rs.getFloat(8);
				mshi2[2][0] = rs.getFloat(9);
				mshi2[2][1] = rs.getFloat(10);
				mshi2[2][2] = rs.getFloat(11);
				String path = rs.getString(12);
				double value = FeatureMeasure.centerMomentDistance(mshi, mshi2);
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
	
//	
//	public static void main(String[] args) {
//		hsiCenterMatch("C:\\Users\\刘开\\Desktop\\综合实训\\图库\\accordion\\accordion10.jpg");
//	}

}
