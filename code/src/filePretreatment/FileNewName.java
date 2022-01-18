package filePretreatment;

/*
 * 读取文件的名字
 */
import java.io.File;

public class FileNewName {
	private static String name;// 文件夹的名字
	private static int id = 1;// 序号

	public static void getList(File file) {
		if (file.isDirectory())// 判断file是否是目录
		{
			name = file.getName();
			File[] lists = file.listFiles();
			if (lists != null) {
				for (int i = 0; i < lists.length; i++) {
					getList(lists[i]);// 是目录就递归进入目录内再进行判断
				}
			}
			id = 1;// 换下一个目录，编号从1开始
		}
		if (file.isFile()) {
			file.renameTo(new File(file.getParent() + File.separator + name
					+ id + ".jpg"));// 更改名字
			id++;
		}

	}

	public static void main(String[] args) {
		File file = new File("C:\\Users\\MINGKU\\Desktop\\综合课程设计\\图库" + File.separator);// 使用系统有关的默认名称分隔符
		getList(file);
	}

}
