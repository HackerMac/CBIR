package filePretreatment;

/*
 * 读取一个文件夹下的文件以及路径
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileGetPath {

	private static PrintWriter pw;// 输出到文件

	public FileGetPath(String decPath) {
		super();
		try {
			pw = new PrintWriter(new FileWriter(decPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getList(File file) {
		if (file.isDirectory())// 判断file是否是目录
		{
			File[] lists = file.listFiles();
			if (lists != null) {
				for (int i = 0; i < lists.length; i++) {
					getList(lists[i]);// 是目录就递归进入目录内再进行判断
				}
			}
		}
		if (file.isFile()) {
			// System.out.println(file);// file是文件，就输出它的路径名
			pw.println(file);
		}

	}

	public void close() {
		pw.close();
	}

	public static void main(String[] args) {
		FileGetPath fgp = new FileGetPath("C:\\Users\\MINGKU\\Desktop\\Path.txt");
		File file = new File("C:\\Users\\MINGKU\\Desktop\\综合课程设计\\图库"
				+ File.separator);// 使用系统有关的默认名称分隔符
		fgp.getList(file);
		fgp.close();
	}

}
