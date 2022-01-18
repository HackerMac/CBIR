package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtil {

	public static Connection getMysqlConn() {
		try {
			// 加载驱动类
			Class.forName("com.mysql.jdbc.Driver");
			//防止中文乱码
			return DriverManager
					.getConnection("jdbc:mysql://blog.sakuramk.cn:3306/cbir?user=root&password=652632&serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void close(ResultSet rs, Statement ps, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(Statement ps, Connection conn) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void close(Statement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}