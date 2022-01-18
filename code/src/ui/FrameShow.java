package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 图片显示
 * 
 */
public class FrameShow {
	private JFrame jf = new JFrame("图片显示");// 界面类
	private JPanel result = new JPanel();// 查询结果图片显示

	public FrameShow(final int count) {
		//打开一个转圈的进度条
		final InfiniteProgressPanel glasspane = new InfiniteProgressPanel();
		Dimension dimension = Toolkit.getDefaultToolkit()
				.getScreenSize();
		glasspane.setBounds(900, 900, (dimension.width) / 2,
				(dimension.height) / 2);
		jf.setGlassPane(glasspane);
		glasspane.start();// 开始动画加载效果
		jf.setVisible(true);		
		jf.setSize(1500, 900);
		// 启动一个线程来加载数据
		new Thread() {
			@Override
			public void run() {
				int row = count / 4 + 1;
				result.setLayout(new GridLayout(row, 4, 5, 5));// 共有4列
				BufferedReader br;
				try {

					br = new BufferedReader(new FileReader(new File(
							"C:\\Users\\MINGKU\\Desktop\\Path.txt")));
					String read = "";
					// 一一加入到界面上，最多显示count*4个
					int index = 0;
					while ((read = br.readLine()) != null) {
						if (index < count) {
							JLabel label = new JLabel();
							// System.out.println(read);
							label.setIcon(new ImageIcon(read));
							label.setSize(30, 30);
							result.add(new JScrollPane(label));
						}
						index++;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				JPanel jp = new JPanel();
				jp.setLayout(new BorderLayout());
				JLabel jl = new JLabel("所有图片显示如下:");
				jl.setFont(new Font("幼圆", 1, 20));
				jl.setForeground(Color.red);
				jp.add(jl, BorderLayout.NORTH);
				jp.add(new JScrollPane(result));
				jf.add(jp);
				jf.pack();
				glasspane.stop();	
			}
		}.start();
		
	}

	public static void main(String[] args) {
		new FrameShow(40);
	}
}
