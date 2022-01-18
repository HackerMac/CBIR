package match;

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

import database.JDBCUtil;
import mainAlgorithm.GLCM;

/**
 * 纹理匹配
 * 
 */
public class TextureMatch {

	public static Map<String, Double> textMatch(String srcPath) {
		String str= srcPath.substring(0,srcPath.lastIndexOf('\\'));
		String fileName = str.substring(str.lastIndexOf('\\') + 1,str.length());// 文件相关名字，用于在数据库中索引
		Map<String, Double> lhm = new LinkedHashMap<String, Double>();// 返回结果
		//System.out.println(fileName);
		Map<String, Double> map = new HashMap<String, Double>();// 保存图片与数据库中符合条件的图片的距离值
		
		GLCM glcm = new GLCM();
		double[] expStadv = glcm
				.getEigenValue(srcPath);
		// 连接数据库
		Connection conn = (Connection) JDBCUtil.getMysqlConn();
		String sql = "select * from texture where Name = ?";
		PreparedStatement preStmt;
		try {		
			preStmt = conn.prepareStatement(sql);
			preStmt.setObject(1, fileName);
			ResultSet rs = preStmt.executeQuery();
			
			
			double[] expStadv2 = new double[8];
			// rs.last();
			// int rowcount = rs.getRow();//获得总条数
			// System.out.println(rowcount);
			while (rs.next()) {
				int i = 0;
				// int id=rs.getInt(1);
				expStadv2[i++] = rs.getFloat(3);
				expStadv2[i++] = rs.getFloat(4);
				expStadv2[i++] = rs.getFloat(5);
				expStadv2[i++] = rs.getFloat(6);
				expStadv2[i++] = rs.getFloat(7);
				expStadv2[i++] = rs.getFloat(8);
				expStadv2[i++] = rs.getFloat(9);
				expStadv2[i++] = rs.getFloat(10);
				String path = rs.getString(11);
				// System.out.println(path);
				double value = FeatureMeasure.Cosine(expStadv, expStadv2);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return lhm;
	}
	public static void main(String[] args) {
		textMatch("C:\\Users\\刘开\\Desktop\\综合实训\\图库\\accordion\\accordion10.jpg");
		
	}

}
