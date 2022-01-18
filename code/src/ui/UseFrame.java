package ui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 使用说明文档
 * 
 */
public class UseFrame {
	private JFrame frame = new JFrame("使用说明");// 界面类
	private JTextArea jtf = new JTextArea(25, 60);

	public UseFrame() {

		jtf.setFont(new Font("幼圆", 1, 20));
		jtf.setText("1.左边树形控件显示的是所有图像特征文件，点击可进行打开（关闭）" +
				    "控件，点击路\n径选择文件，也可通过打开菜单选择需要进行检索的图片。\n" +
				"2.选择检索方法后点击确定进行检索，会在窗口右部显示结果，最多显示12张图片，\n" +
				"按照相似度大小排列，图像上方为对应的差距，点击其中一张图片可进行保存，\n选择路径以及图片格式后保存图片。\n"
				+ "3.数据库管理中可进行插入新的图片，选择图片文件，输入图片标识符进行增加操作，\n" +
				"点击删除数据后，可在树形控件红选择需要删除的数据，仅代表图片从数据库中删除，\n" +
				"图片本身并没有被删除。点击统计菜单，显示图片总数目。点击图片显示菜单，使用\n另一个界面显示所有图片，一行四张。");
		jtf.setWrapStyleWord(false);
		frame.add(new JScrollPane(jtf));
		jtf.setEditable(false);// 不可编辑
		frame.pack();
		frame.setVisible(true);
		//添加事件监听器
		jtf.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mouseEvent) {
				jtf.setCursor(new Cursor(Cursor.TEXT_CURSOR)); // 鼠标进入Text区后变为文本输入指针
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent) {
				jtf.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 鼠标离开Text区后恢复默认形态
			}
		});
		jtf.getCaret().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				jtf.getCaret().setVisible(true); // 使Text区的文本光标显示
			}
		});
	}

	public static void main(String[] args) {
		new UseFrame();
	}
}
//"1.如果没有数据库需要先点击数据库管理菜单进行新建，选择文件夹导" +
//		"入，文件较多\n时创建时间较长，请耐心等待！\n" +