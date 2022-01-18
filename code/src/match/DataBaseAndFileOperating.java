package match;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.JDBCUtil;
import mainAlgorithm.ColorHSICenterMoment;
import mainAlgorithm.EdgeDetector;
import mainAlgorithm.EdgeHistogram;
import mainAlgorithm.GLCM;
import mainAlgorithm.HistogramIntersecting;
import mainAlgorithm.HuMoment;
import mainAlgorithm.SobelAndBinarization;

public class DataBaseAndFileOperating {
	/**
	 * 初始化数据库,以及生成必要的文件
	 * @param srcPath 所有图像的路径文件
	 * @return
	 */
	public static boolean init(String srcPath) {
		try {
			// 得到Path.txt，默认目录(先不进行选择)
			BufferedReader br = new BufferedReader(new FileReader(new File(srcPath)));
			String read = "";

			// 连接数据库，获得连接
			Connection conn = (Connection) JDBCUtil.getMysqlConn();

			// 使用truncate清空原有表，为了使ID能够从1开始
			conn.prepareStatement("truncate table texture").executeUpdate();
			conn.prepareStatement("truncate table color").executeUpdate();
			conn.prepareStatement("truncate table shape").executeUpdate();

			// 插入数据到texture表
			String sql = "insert into texture (Name,Exp1,Exp2,Exp3,Exp4,Stadv1,Stadv2,Stadv3,Stadv4,Path) values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt = conn.prepareStatement(sql);

			// 插入数据到color表
			String sql2 = "insert into color (Name,H1,H2,H3,S1,S2,S3,I1,I2,I3,Path) values(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt2 = conn.prepareStatement(sql2);

			// 插入数据到shape表
			String sql3 = "insert into shape (Name,Shape0,Shape1,Shape2,Shape3,Shape4,Shape5,Shape6,Shape7,Path) values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt3 = conn.prepareStatement(sql3);// 用于操作shape表
			while ((read = br.readLine()) != null) {
				String path = read;
				String str = read.substring(0, read.lastIndexOf('\\'));
				String name = str.substring(str.lastIndexOf('\\') + 1,
						str.length());
				// System.out.println(name);

				// 插入纹理特征数据
				preStmt.setString(1, name);
				GLCM glcm = new GLCM();
				double[] expStadv = glcm.getEigenValue(path);
				for (int i = 0; i < expStadv.length; i++) {
					// System.out.println(expStadv[i]);
					preStmt.setFloat((i + 2), (float) expStadv[i]);
				}
				preStmt.setString(10, path);
				preStmt.executeUpdate();

				// 插入颜色特征数据
				preStmt2.setString(1, name);

				float[][] mhsi = ColorHSICenterMoment.rgbTohsi(path);
				for (int i = 0; i < mhsi.length; i++) {
					for (int j = 0; j < mhsi[0].length; j++) {
						if (Float.isNaN(mhsi[i][j]))
							mhsi[i][j] = 0;
						preStmt2.setFloat((i * mhsi.length + j + 2), mhsi[i][j]);
					}
				}
				preStmt2.setString(11, path);
				preStmt2.executeUpdate();

				// 插入形状特征数据
				preStmt3.setString(1, name);
//				// 灰度化，中值滤波，soble，二值化
//				SobelAndBinarization s = new SobelAndBinarization();
//				s.sobel(path);
//				s.getBinaryImg();
//				// 得到不变矩
//				double[] hum = HuMoment.huMoment(s.getImg());

				EdgeDetector edgeDetector = new EdgeDetector();
				edgeDetector.setSourceImage(path); // 设置边缘处理的参数
				edgeDetector.setThreshold(128);
				edgeDetector.setWidGaussianKernel(5);
				edgeDetector.process();
				 // 进行边缘处理
				double[] hum = HuMoment.huMoment(edgeDetector.getEdgeImage());
				
				for (int i = 0; i < hum.length; i++) {
					if (Double.isNaN(hum[i]))
						hum[i] = 0;
					// System.out.println(hum[i]);
					preStmt3.setFloat((i + 2), (float) hum[i]);
				}
				preStmt3.setString(10, path);
				preStmt3.executeUpdate();

				// 直方图相交法，输出文件到相应的文件文件夹之下，名字相同，.txt文件
				HistogramIntersecting.getHistogram1(path, true);
				
				//边缘方向直方图法
				EdgeHistogram e=new EdgeHistogram();
				e.edgeHistogram(edgeDetector.getFileName(),edgeDetector.getName(),edgeDetector.getEdgeImage(), true);

			}
			br.close();
			// 关闭流
			JDBCUtil.close(preStmt);
			JDBCUtil.close(preStmt2);
			JDBCUtil.close(preStmt3, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 往数据库添加数据
	 * 
	 * @param name
	 *            名称
	 * @param path
	 *            路径
	 * @return 是否增加成功
	 */
	public static boolean addData(String name, String path) {

		try {
			// 连接数据库，获得连接
			Connection conn = (Connection) JDBCUtil.getMysqlConn();

			// 插入数据到texture表
			String sql = "insert into texture (Name,Exp1,Exp2,Exp3,Exp4,Stadv1,Stadv2,Stadv3,Stadv4,Path) values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt = conn.prepareStatement(sql);

			// 插入数据到color表
			String sql2 = "insert into color (Name,H1,H2,H3,S1,S2,S3,I1,I2,I3,Path) values(?,?,?,?,?,?,?,?,?,?,?)";

			PreparedStatement preStmt2 = conn.prepareStatement(sql2);
			// 插入数据到shape表
			String sql3 = "insert into shape (Name,Shape0,Shape1,Shape2,Shape3,Shape4,Shape5,Shape6,Shape7,Path) values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt3 = conn.prepareStatement(sql3);

			String str = path.substring(0, path.lastIndexOf('\\'));
			// 插入纹理特征数据
			preStmt.setString(1, name);
			GLCM glcm = new GLCM();
			double[] expStadv = glcm.getEigenValue(path);
			for (int i = 0; i < expStadv.length; i++) {

				preStmt.setFloat((i + 2), (float) expStadv[i]);
			}
			preStmt.setString(10, path);
			preStmt.executeUpdate();

			// 插入颜色特征数据
			preStmt2.setString(1, name);

			float[][] mhsi = ColorHSICenterMoment.rgbTohsi(path);
			for (int i = 0; i < mhsi.length; i++) {
				for (int j = 0; j < mhsi[0].length; j++) {
					if (Float.isNaN(mhsi[i][j]))
						mhsi[i][j] = 0;
					preStmt2.setFloat((i * mhsi.length + j + 2), mhsi[i][j]);
				}

			}
			preStmt2.setString(11, path);
			preStmt2.executeUpdate();

			// 插入形状特征数据
			preStmt3.setString(1, name);
			// 灰度化，中值滤波，soble，二值化
			SobelAndBinarization s = new SobelAndBinarization();
			s.sobel(path);
			s.getBinaryImg();
			// 得到不变矩
			double[] hum = HuMoment.huMoment(s.getImg());
			for (int i = 0; i < hum.length; i++) {
				// System.out.println(hum[i]);
				preStmt3.setFloat((i + 2), (float) hum[i]);
			}
			preStmt3.setString(10, path);
			preStmt3.executeUpdate();

			// 直方图相交法，输出文件到相应的file文件夹之下，名字相同，.txt文件
			HistogramIntersecting.getHistogram1(path, true);

			// 关闭流
			JDBCUtil.close(preStmt);
			JDBCUtil.close(preStmt2);
			JDBCUtil.close(preStmt3, conn);
		} catch (SQLException e) {
			return false;
		}

		return true;

	}

	/**
	 * 删除数据库中某条数据
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteData(String path) {

		try {
			// 连接数据库，获得连接
			Connection conn = (Connection) JDBCUtil.getMysqlConn();
			// 从texture表删除数据
			String sql = "delete from texture where Path=?";
			PreparedStatement preStmt = conn.prepareStatement(sql);

			// 从color表删除数据
			String sql2 = "delete from color where Path=?";

			PreparedStatement preStmt2 = conn.prepareStatement(sql2);
			// 从shape表删除数据
			String sql3 = "delete from shape where Path=?";
			PreparedStatement preStmt3 = conn.prepareStatement(sql3);

			preStmt.setString(1, path);
			preStmt.executeUpdate();
			preStmt2.setString(1, path);
			preStmt2.executeUpdate();
			preStmt3.setString(1, path);
			preStmt3.executeUpdate();

			// 关闭流
			JDBCUtil.close(preStmt);
			JDBCUtil.close(preStmt2);
			JDBCUtil.close(preStmt3, conn);

		} catch (SQLException e) {
			return false;
		}

		return true;

	}

	/**
	 * 统计图像数目
	 * @return 数目
	 */
	public static int count() {
		int result=0;
		// 连接数据库，获得连接
		Connection conn = (Connection) JDBCUtil.getMysqlConn();
		Statement stmt;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rset = stmt.executeQuery("select * from color");//任意选择一张表，每张表的列数就是图像总数
			rset.last();
			result = rset.getRow(); // 获得ResultSet的总行数
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static void main(String[] args) {

		// init();
		// deleteData("C:\\Users\\刘开\\Desktop\\综合实训\\图库\\flamingo_head\\flamingo_head2.jpg");
		//System.out.println(count());
		//init("C:\\Users\\刘开\\Desktop\\Path.txt");
	}

}
