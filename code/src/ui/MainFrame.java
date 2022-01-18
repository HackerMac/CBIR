package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import filePretreatment.FileGetPath;
import mainAlgorithm.HistogramIntersecting;
import match.DataBaseAndFileOperating;
import match.HistogramIntersectingMathch;
import match.HSICenterMomentMatch;
import match.ShapeEdgeMathch;
import match.ShapeMatch;
import match.TextureMatch;

/**
 * 主界面
 */
public class MainFrame {

	private Map<String, Double> lhmResultImage;// 通过调用各种方法返回的检索结果
	private JFrame jf;// 界面类
	private JTree treeFileList;// 树形控件,显示文件路径
	private DefaultTreeModel model;// 上面JTree对象对应的model
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("图库");// 定义Tree初始节点
	private JLabel checkImage = new JLabel();// 需要检测的图片显示
	private JPanel result = new JPanel();// 查询结果图片显示
	private JTextField jTpath = new JTextField(60);// 一个60列的单行文本域显示图片路径
	private JLabel jlMethod = new JLabel("检索方法");
	private List<JLabel> listjl = new ArrayList<JLabel>();// 显示图片结果
	private List<JTextField> listjtf = new ArrayList<JTextField>();// 显示差距结果
	private boolean isDeleteFile = false;// 判断点击树形控件是否是要删除文件

	private int count;// 图片的总数目
	boolean isCount = false;// 是否进行统计过

	InfiniteProgressPanel glasspane = new InfiniteProgressPanel();

	public MainFrame() {

		jf = new JFrame("基于内容的图像检索1.0");
		// 菜单栏
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("文件(F)");
		JMenu color = new JMenu("基于颜色检索(CS)");
		JMenu shape = new JMenu("基于形状检索(SS)");
		JMenu texture = new JMenu("基于纹理检索(VS)");
//		JMenu face = new JMenu("人脸识别(FF)");
		JMenu database = new JMenu("数据库管理(DB)");
		JMenu help = new JMenu("帮助(H)");
		// 设置助记符为F，按下ALT + F 可以触发该菜单
		file.setMnemonic('F');
		// 设置助记符为F，按下ALT + H 可以触发该菜单
		help.setMnemonic('H');
		// 创建“打开”菜单项，并为之指定图标
		JMenuItem open = new JMenuItem("打开图片", new ImageIcon("icon/open.png"));
		// 创建“退出”菜单项，并为之指定图标
		JMenuItem quit = new JMenuItem("退出", new ImageIcon("icon/exit.png"));
		file.add(open);
		// 设置菜单分隔符
		file.addSeparator();
		file.add(quit);
		// 创建“使用说明”菜单项，并为之指定图标
		JMenuItem use = new JMenuItem("使用说明", new ImageIcon("icon/use.png"));
		// 创建“关于”菜单项，并为之指定图标
		JMenuItem about = new JMenuItem("关于", new ImageIcon("icon/about.png"));
		help.add(use);
		// 设置菜单分隔符
		help.addSeparator();
		help.add(about);
		// 创建“建立新数据库”菜单项,并为之指定图标
		JMenuItem mdb = new JMenuItem("建立新数据库", new ImageIcon("icon/db.png"));
		// 创建“增加图像”菜单项,并为之指定图标
		JMenuItem madd = new JMenuItem("增加图像", new ImageIcon("icon/add.png"));
		// 创建“删除图像”菜单项,并为之指定图标
		JMenuItem mdetele = new JMenuItem("删除图像", new ImageIcon(
				"icon/delete.png"));
		// 创建“图像统计”菜单项,并为之指定图标
		JMenuItem mcount = new JMenuItem("图像统计",
				new ImageIcon("icon/count.png"));
		// 创建“图像显示”菜单项,并为之指定图标
		JMenuItem mdisplay = new JMenuItem("图像显示", new ImageIcon(
				"icon/show.png"));
		database.add(mdb);
		database.addSeparator();
		database.add(madd);
		database.addSeparator();
		database.add(mdetele);
		database.addSeparator();
		database.add(mcount);
		database.addSeparator();
		database.add(mdisplay);
		// 创建“建立形状不变矩法”菜单项
		JMenuItem mHU = new JMenuItem("形状不变矩法");
		// 创建“边缘方向直方图法”菜单项
		JMenuItem mEdge = new JMenuItem("边缘方向直方图法");
		shape.add(mHU);
		shape.addSeparator();
		shape.add(mEdge);
		
		// 创建“HSI中心矩法”菜单项
		JMenuItem mHSI = new JMenuItem("HSI中心矩法");
		// 创建“直方图相交法”菜单项
		JMenuItem mHI = new JMenuItem("直方图相交法");
		// 创建“灰度共生矩阵法”菜单项
		JMenuItem mGLCM = new JMenuItem("灰度共生矩阵法");
		color.add(mHSI);
		color.addSeparator();
		color.add(mHI);
		texture.add(mGLCM);

		menuBar.add(file);
		menuBar.add(color);
		menuBar.add(shape);
		menuBar.add(texture);
//		menuBar.add(face);
		menuBar.add(database);
		menuBar.add(help);

		// 创建“打开照相机(Haar-like特征)”菜单项，并为之指定图标
		JMenuItem mcamera = new JMenuItem("打开照相机(Haar-like特征)", new ImageIcon(
				"icon/camera.png"));
//		face.add(mcamera);
		// 设置菜单栏，使用这种方式设置菜单栏可以不占用布局空间
		jf.setJMenuBar(menuBar);

		treeList();// 得到所有文件路径的树形结构

		treeFileList = new JTree(root);
		// 获取JTree对应的TreeModel对象
		model = (DefaultTreeModel) treeFileList.getModel();
		// 设置JTree可编辑
		treeFileList.setEditable(true);
		// 设置只能选择一个
		treeFileList.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		// 设置事件监听器，获得点击的路径
		treeFileList.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode note = (DefaultMutableTreeNode) treeFileList
						.getLastSelectedPathComponent();

				if (isDeleteFile) {// 删除

					if (note != null && note.getParent() != null) {
						String name = note.toString();// 获得这个结点的名称
						int n = JOptionPane.showConfirmDialog(null, "确认要删除"
								+ name, "提示对话框", JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							// 调用类从数据库中删除数据
							boolean flag = DataBaseAndFileOperating
									.deleteData(name);
							if (flag) {
								model.removeNodeFromParent(note);// 删除指定节点
								JOptionPane.showMessageDialog(jf, "删除成功! ",
										"提示 ", JOptionPane.INFORMATION_MESSAGE);
								// 删除Path.txt的内容
								delete("C:\\Users\\MINGKU\\Desktop\\Path.txt", name);
							}

						}

					}
					isDeleteFile = false;// 一次删除一个
				} else {// 仅仅是显示
					if (note != null && note.getParent() != null) {
						String name = note.toString();// 获得这个结点的名称
						jTpath.setText(name);
						// 显示图片
						checkImage.setIcon(new ImageIcon(name));
					}

				}

			}

		});
		// 定义一个确定按钮,并为之指定图标
		Icon okIcon = new ImageIcon("icon/ok.png");
		JButton ok = new JButton("确认", okIcon);

		// 添加事件监听器，得到使用的方法，调用相关的类，输出结果
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断路径是否已经点过一个文件
				if ("".equals(jTpath.getText()) || jTpath.getText() == null) {
					JOptionPane.showMessageDialog(jf, "未选择文件 ", "警告 ",
							JOptionPane.ERROR_MESSAGE);
				} else {
					// 打开一个转圈的进度条
					final InfiniteProgressPanel glasspane = new InfiniteProgressPanel();
					Dimension dimension = Toolkit.getDefaultToolkit()
							.getScreenSize();
					glasspane.setBounds(900, 900, (dimension.width) / 2,
							(dimension.height) / 2);
					jf.setGlassPane(glasspane);
					glasspane.start();// 开始动画加载效果

					final String path = jTpath.getText();
					final String methodStr = jlMethod.getText();
					// 启动一个线程来加载数据
					new Thread() {
						@Override
						public void run() {
							switch (methodStr) {
							case "直方图相交法":
								lhmResultImage = (LinkedHashMap<String, Double>) HistogramIntersectingMathch
										.hiMatch(path);
								break;
							case "灰度共生矩阵法":
								lhmResultImage = (LinkedHashMap<String, Double>) TextureMatch
										.textMatch(path);

								break;
							case "HSI中心矩法":
								lhmResultImage = (LinkedHashMap<String, Double>) HSICenterMomentMatch
										.hsiCenterMatch(path);

								break;
							case "形状不变矩法":
								lhmResultImage = (LinkedHashMap<String, Double>) ShapeMatch
										.shapeMatch(path);

								break;
							case "边缘方向直方图法":
								lhmResultImage = (LinkedHashMap<String, Double>) ShapeEdgeMathch.seMatch(path);

								break;
							default:
								JOptionPane.showMessageDialog(jf, "方法无法使用 ",
										"警告 ", JOptionPane.ERROR_MESSAGE);
								break;

							}
							int index = 0;
							if (lhmResultImage != null) {// 不为空
								for (Map.Entry<String, Double> entry : lhmResultImage
										.entrySet()) {
									// 设置图片和差距
									listjl.get(index).setIcon(
											new ImageIcon(entry.getKey()));
									listjtf.get(index).setText(
											"差距为:" + entry.getValue());
									index++;
								}
							}
							glasspane.stop();
						}
					}.start();
				}

			}
		});
		String[] method = new String[] { "一般欧氏距离函数", "最邻近查询算法", "区间查询算法",
				"加权距离函数", "相交法度量函数", "中心矩法", "余弦夹角" };
		// 定义一个下拉选择框
		JComboBox<String> methodChooser = new JComboBox<>(method);

		jlMethod.setFont(new Font("幼圆", 1, 20));
		jlMethod.setForeground(Color.red);
		// 创建一个JPanel放在最下面
		JPanel bottom = new JPanel();
		bottom.add(jlMethod);
		bottom.add(new JLabel("   选择特征向量度量方法 :"));
		bottom.add(methodChooser);
		bottom.add(new JLabel("    图像文件路径:"));
		bottom.add(jTpath);
		bottom.add(ok);

		// 为三个组件设置最佳大小
		treeFileList.setPreferredSize(new Dimension(150, 10000));// 将高度设置很大为了能够完整的查看
		checkImage.setPreferredSize(new Dimension(300, 200));
		result.setPreferredSize(new Dimension(800, 150));
		result.setLayout(new GridLayout(3, 4, 10, 10));
		saveListener sl = new saveListener();
		JLabel jli1 = new JLabel();
		listjl.add(jli1);
		jli1.addMouseListener(sl);
		JLabel jli2 = new JLabel();
		listjl.add(jli2);
		jli2.addMouseListener(sl);
		JLabel jli3 = new JLabel();
		listjl.add(jli3);
		jli3.addMouseListener(sl);
		JLabel jli4 = new JLabel();
		listjl.add(jli4);
		jli4.addMouseListener(sl);
		JLabel jli5 = new JLabel();
		listjl.add(jli5);
		jli5.addMouseListener(sl);
		JLabel jli6 = new JLabel();
		listjl.add(jli6);
		jli6.addMouseListener(sl);
		JLabel jli7 = new JLabel();
		listjl.add(jli7);
		jli7.addMouseListener(sl);
		JLabel jli8 = new JLabel();
		listjl.add(jli8);
		jli8.addMouseListener(sl);
		JLabel jli9 = new JLabel();
		listjl.add(jli9);
		jli9.addMouseListener(sl);
		JLabel jli10 = new JLabel();
		listjl.add(jli10);
		jli10.addMouseListener(sl);
		JLabel jli11 = new JLabel();
		listjl.add(jli11);
		jli11.addMouseListener(sl);
		JLabel jli12 = new JLabel();
		listjl.add(jli12);
		jli12.addMouseListener(sl);

		JTextField jtf1 = new JTextField("差距为:");
		listjtf.add(jtf1);
		JTextField jtf2 = new JTextField("差距为:");
		listjtf.add(jtf2);
		JTextField jtf3 = new JTextField("差距为:");
		listjtf.add(jtf3);
		JTextField jtf4 = new JTextField("差距为:");
		listjtf.add(jtf4);
		JTextField jtf5 = new JTextField("差距为:");
		listjtf.add(jtf5);
		JTextField jtf6 = new JTextField("差距为:");
		listjtf.add(jtf6);
		JTextField jtf7 = new JTextField("差距为:");
		listjtf.add(jtf7);
		JTextField jtf8 = new JTextField("差距为:");
		listjtf.add(jtf8);
		JTextField jtf9 = new JTextField("差距为:");
		listjtf.add(jtf9);
		JTextField jtf10 = new JTextField("差距为:");
		listjtf.add(jtf10);
		JTextField jtf11 = new JTextField("差距为:");
		listjtf.add(jtf11);
		JTextField jtf12 = new JTextField("差距为:");
		listjtf.add(jtf12);
		// 加入Label和TextField
		for (int i = 0; i < 12; i++) {

			JPanel jpr = new JPanel();
			jpr.setLayout(new BorderLayout());
			jpr.add(listjtf.get(i), BorderLayout.NORTH);
			jpr.add(listjl.get(i), BorderLayout.SOUTH);
			result.add(new JScrollPane(jpr));
		}

		// 创建一个垂直的分割面板，
		// 将tree放在上面，将checkImage放在下面, 支持连续布局
		JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				new JScrollPane(treeFileList), new JScrollPane(checkImage));
		// 打开“一触即展”的特性
		left.setOneTouchExpandable(true);
		left.setDividerSize(10);// 设置分割条的大小
		// 设置该分割面板根据所包含组件的最佳大小来调整布局
		left.resetToPreferredSizes();
		// 创建一个水平的分割面板,将left组件放在左边，将resultImage组件放在右边
		JSplitPane content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left,
				result);
		jf.add(new JScrollPane(content));
		jf.add(bottom, BorderLayout.SOUTH);
		jf.pack();
		// jf.setSize(900, 600);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setIconImage(new ImageIcon("icon/icon.png").getImage());// 为界面指定一个图片
		jf.setVisible(true);

		final JFileChooser dOpen = new JFileChooser();// 创建文件选择器
		dOpen.setCurrentDirectory(new File("."));// 设置当前目录
		dOpen.setDialogTitle("选择需要检测的图片");

		// 显示文件源文件
		dOpen.addChoosableFileFilter(new MyFileFilter("jpg"));// 导入可选择的文件的后缀名类型
		dOpen.addChoosableFileFilter(new MyFileFilter("png"));
		dOpen.addChoosableFileFilter(new MyFileFilter("gif"));
		dOpen.addChoosableFileFilter(new MyFileFilter("bmp"));
		// 为打开菜单添加事件
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 得到用户选择的图片路径和文件名
				dOpen.showDialog(new JLabel(), "选择");
				File f = dOpen.getSelectedFile();
				if (f != null) {
					jTpath.setText(f.getPath());
					// 显示图片
					checkImage.setIcon(new ImageIcon(f.getPath()));
				}

			}

		});
		// 为创建新数据库菜单添加事件
		mdb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null,
						"原有数据库会被删除，并且建立新的图像特征数据库时间较长,确认要创建新数据库吗?", "选择对话框",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {

					final JFileChooser jfc = new JFileChooser();// 创建文件选择器
					jfc.setDialogTitle("选择导入数据库图片的目录");
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						// 打开一个转圈的进度条
						final InfiniteProgressPanel glasspane = new InfiniteProgressPanel();
						Dimension dimension = Toolkit.getDefaultToolkit()
								.getScreenSize();
						glasspane.setBounds(900, 900, (dimension.width) / 2,
								(dimension.height) / 2);
						jf.setGlassPane(glasspane);
						glasspane.start();// 开始动画加载效果
						// System.out.println(jfc.getSelectedFile().getAbsolutePath());
						// 启动一个线程来加载数据
						new Thread() {
							@Override
							public void run() {
								// 得到Path.txt文件
								String srcPath = "C:\\Users\\MINGKU\\Desktop\\Path.txt";// 设置一个路径
								FileGetPath fgp = new FileGetPath(srcPath);
								File file = new File(jfc.getSelectedFile()
										.getAbsolutePath() + File.separator);// 使用系统有关的默认名称分隔符
								fgp.getList(file);
								fgp.close();// 关闭流
								// 先删除“文件”与“文件2”文件夹里原有的文件
								String s = "C:\\Users\\MINGKU\\Desktop\\文件";
								deleteAllFilesOfDir(new File(s));
								// 然后创建一个空文件夹
								File f = new File(s);
								// 如果文件夹不存在则创建
								if (!f.exists() && !f.isDirectory()) {
									f.mkdir();
								}
								s = "C:\\Users\\MINGKU\\Desktop\\文件2";
								deleteAllFilesOfDir(new File(s));
								f = new File(s);
								if (!f.exists() && !f.isDirectory()) {
									f.mkdir();
								}
								
								// 初始化
								boolean flag = DataBaseAndFileOperating
										.init(srcPath);
								if (flag) {
									//移除树形控件除root外的使用节点
									root.removeAllChildren();
									model.reload();
									// 关闭进度条
									glasspane.stop();
									JOptionPane.showMessageDialog(jf, "创建成功! ",
											"提示 ",
											JOptionPane.INFORMATION_MESSAGE);
									//重新建立树形结构
									 treeList();// 得到所有文件路径的树形结构
									 //实现显示新节点（自动展开父节点）
									 TreeNode[] nodes =
									 model.getPathToRoot(root);
									 TreePath path = new TreePath(nodes);
									 treeFileList.scrollPathToVisible(path);
									 treeFileList.updateUI();
								}
							}
						}.start();

					}

				}
			}

		});
		// 为增加图像菜单添加事件
		madd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfc = new JFileChooser();// 创建文件选择器
				jfc.setDialogTitle("选择导入数据库图片");
				// 得到用户选择的图片路径和文件名
				jfc.showDialog(new JLabel(), "选择");
				File f = jfc.getSelectedFile();
				if (f != null) {
					String name = JOptionPane
							.showInputDialog("请输入图片标识(属于什么类别):");
					// System.out.println(f.getPath());
					// System.out.println(name);
					// 增加数据
					boolean flag = DataBaseAndFileOperating.addData(name,
							f.getPath());
					if (flag) {
						// 往树形中添加
						DefaultMutableTreeNode dmu = new DefaultMutableTreeNode(
								name);
						dmu.add(new DefaultMutableTreeNode(f.getPath()));
						root.add(dmu);
						// 实现显示新节点（自动展开父节点）
						TreeNode[] nodes = model.getPathToRoot(dmu);
						TreePath path = new TreePath(nodes);
						treeFileList.scrollPathToVisible(path);
						treeFileList.updateUI();

						JOptionPane.showMessageDialog(jf, "添加成功 ", "提示 ",
								JOptionPane.INFORMATION_MESSAGE);
						// 采用默认路径向Path.txt的添加内容
						add("C:\\Users\\MINGKU\\Desktop\\Path.txt", f.getPath());
						// 直方图相交法，输出文件到相应的file文件夹之下，名字相同，为.txt文件
						HistogramIntersecting.getHistogram1(f.getPath(), true);
					}
				}

			}
		});
		// 为删除图像菜单添加事件
		mdetele.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null,
						"在树形显示结构中点击进行删除,确认要删除吗?", "提示对话框",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					isDeleteFile = true;
				}

			}
		});

		// 为图像统计菜单添加事件
		mcount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 调用方法统计数目
				count = DataBaseAndFileOperating.count();
				JOptionPane.showMessageDialog(jf, "图像总数目为" + count, "提示 ",
						JOptionPane.INFORMATION_MESSAGE);
				isCount = true;

			}
		});
		// 为图像显示菜单添加事件
		mdisplay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isCount) {
					// 没有统计过,调用方法统计数目
					count = DataBaseAndFileOperating.count();
				}

				new FrameShow(count);

			}
		});
		// 为退出菜单添加事件
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jf.dispose();
			}
		});
		// 为说明菜单添加事件
		use.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new UseFrame();// 打开说明文档
			}
		});
		// 为关于菜单添加事件
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jf, "基于内容的图像检索1.0版本", "说明 ",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		MenuListener ml = new MenuListener();
		// 为"直方图相交法"菜单添加事件
		mHI.addActionListener(ml);
		// 为"HSI中心矩法"菜单添加事件
		mHSI.addActionListener(ml);
		// 为"灰度共生矩阵法"菜单添加事件
		mGLCM.addActionListener(ml);
		// 为"形状不变矩法"菜单添加事件
		mHU.addActionListener(ml);
		// 为"边缘方向直方图法"菜单添加事件
		mEdge.addActionListener(ml);
		// 为"打开照相机"菜单添加事件
		mcamera.addActionListener(ml);
	}

	class MenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String str = e.getActionCommand();
			System.out.println(str);
			switch (str) {
			case "直方图相交法":
				jlMethod.setText("直方图相交法");
				break;
			case "灰度共生矩阵法":
				jlMethod.setText("灰度共生矩阵法");

				break;
			case "HSI中心矩法":
				jlMethod.setText("HSI中心矩法");

				break;
			case "形状不变矩法":
				jlMethod.setText("形状不变矩法");
				break;
			case "边缘方向直方图法":
				jlMethod.setText("边缘方向直方图法");

				break;
			case "打开照相机(Haar-like特征)":
				jlMethod.setText("人脸识别");

				break;
			default:
				break;

			}

		}

	}

	/*
	 * 文件选择过滤器类
	 */
	class MyFileFilter extends FileFilter {
		private String ext;

		public MyFileFilter(String ext) {
			this.ext = ext;
		}

		// 是一个目录而不是文件时,返回true值,将此目录显示出来
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			}
			String fileName = file.getName();
			int index = fileName.lastIndexOf('.');
			if (index > 0 && index < fileName.length() - 1) {
				String extension = fileName.substring(index + 1).toLowerCase();
				// 文件扩展名为要显示的扩展名(即变量ext值),则返回true
				if (extension.equals(ext))
					return true;
			}
			return false;
		}

		// 实现getDescription()方法,返回描述文件的说明字符串
		public String getDescription() {
			if (ext.equals("png")) {
				return "图片(*.png)";
			} else if (ext.equals("jpg")) {
				return "图片(*.jpg)";
			} else if (ext.equals("gif")) {
				return "图片(*.gif)";
			} else if (ext.equals("bmp")) {
				return "图片(*.bmp)";
			}
			return "";
		}
	}

	/*
	 * 文件保存过滤器类
	 */
	class MyFileFilter2 extends FileFilter {

		private String ends; // 文件后缀
		private String description; // 文件描述文字

		public MyFileFilter2(String ends, String description) { // 构造函数
			this.ends = ends; // 设置文件后缀
			this.description = description; // 设置文件描述文字
		}

		@Override
		// 只显示符合扩展名的文件，目录全部显示
		public boolean accept(File file) {
			if (file.isDirectory())
				return true;
			String fileName = file.getName();
			if (fileName.toUpperCase().endsWith(this.ends.toUpperCase()))
				return true;
			return false;
		}

		@Override
		// 返回这个扩展名过滤器的描述
		public String getDescription() {
			return this.description;
		}

		// 返回这个扩展名过滤器的扩展名
		public String getEnds() {
			return this.ends;
		}
	}

	/*
	 * 保存文件监听器
	 */
	class saveListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			// jli1.getIcon();

			int n = JOptionPane.showConfirmDialog(null, "确认保存吗?", "保存对话框",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				// FileDialog dSave = new FileDialog(jf,
				// "选择保存图片的路径,默认.jpg文件",
				// FileDialog.SAVE);// 点击图片保存
				// dSave.setVisible(true);

				JFileChooser dSave = new JFileChooser();// 创建文件选择器
				dSave.setCurrentDirectory(new File("."));// 设置当前目录
				dSave.setDialogTitle("选择需要保存图片的路径");
				// 建立几个过滤器
				MyFileFilter2 mf1 = new MyFileFilter2(".jpg", "图片(*.jpg)");
				MyFileFilter2 mf2 = new MyFileFilter2(".png", "图片(*.png)");
				MyFileFilter2 mf3 = new MyFileFilter2(".gif", "图片(*.gif)");
				MyFileFilter2 mf4 = new MyFileFilter2(".bmp", "图片(*.bmp)");
				// 显示文件源文件
				dSave.addChoosableFileFilter(mf1);// 导入可选择的文件的后缀名类型
				dSave.addChoosableFileFilter(mf2);
				dSave.addChoosableFileFilter(mf3);
				dSave.addChoosableFileFilter(mf4);
				dSave.showDialog(new JLabel(), "保存");
				File file = dSave.getSelectedFile(); // 获得文件名
				// System.out.println(file.getAbsolutePath());
				// 获得被选中的过滤器
				MyFileFilter2 filter = (MyFileFilter2) dSave.getFileFilter();
				// 获得过滤器的扩展名
				String ends = filter.getEnds();
				File newFile = null;
				if (file.getAbsolutePath().toUpperCase()
						.endsWith(ends.toUpperCase())) {
					// 如果文件是以选定扩展名结束的，则使用原名
					newFile = file;
				} else {
					// 否则加上选定的扩展名
					newFile = new File(file.getAbsolutePath() + ends);
					System.out.println(ends);
				}
				try {
					// 使用e.getSource()得到组件名称强转为JLabel
					Image image = ((ImageIcon) ((JLabel) (e.getSource()))
							.getIcon()).getImage();
					BufferedImage bi = new BufferedImage(
							image.getHeight((JLabel) (e.getSource())),
							image.getHeight((JLabel) (e.getSource())),
							BufferedImage.TYPE_INT_RGB);
					Graphics2D g2d = bi.createGraphics();
					g2d.drawImage(image, 0, 0, null);
					// 输出图片
					ImageIO.write(bi, ends.substring(1,ends.length()), newFile);
					
					JOptionPane.showMessageDialog(jf, "保存成功 ", "提示 ",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e1) {
					e1.printStackTrace();
				}// 输出图像
			}
		}
	}

	/*
	 * 显示树形控件
	 */
	public void treeList() {
		// 定义树形控件，显示所有图片的路径
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"C:\\Users\\MINGKU\\Desktop\\Path.txt"));// 得到所有文件的路径
			String read = "";
			String name = "";
			if ((read = br.readLine()) != null) {
				String str = read.substring(0, read.lastIndexOf('\\'));
				name = str.substring(str.lastIndexOf('\\') + 1, str.length());// 文件相关名字，用于在数据库中索引
				// System.out.println(name);
			}
			DefaultMutableTreeNode dmu = new DefaultMutableTreeNode(name);
			dmu.add(new DefaultMutableTreeNode(read));// 加入第一个节点
			while ((read = br.readLine()) != null) {
				String str = read.substring(0, read.lastIndexOf('\\'));
				String fileName = str.substring(str.lastIndexOf('\\') + 1,
						str.length());// 文件相关名字，用于在数据库中索引
				if (name.equals(fileName)) {// 名字未改变
					dmu.add(new DefaultMutableTreeNode(read));// 最底层
				} else {
					root.add(dmu);
					name = fileName;
					dmu = new DefaultMutableTreeNode(name);
					dmu.add(new DefaultMutableTreeNode(read));// 加入第一个节点
				}
			}
			root.add(dmu);// 加入最后一个
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除Path.txt的一条数据
	 * 
	 * @param srcPath
	 *            Path路径
	 * @param text
	 *            删除的数据
	 */
	public void delete(String srcPath, String text) {
		File temp = null;
		File file = new File(srcPath);
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			temp = File.createTempFile("temp", "temp");
			pw = new PrintWriter(temp);
			br = new BufferedReader(new FileReader(file));

			while (br.ready()) {
				String line = br.readLine();
				if (line.equals(text)) {
					continue;
				}
				pw.println(line);
			}
			pw.flush();
			br.close();
			pw.close();
			if (temp != null) {
				file.delete();
				temp.renameTo(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向Path.txt的添加一条数据
	 * 
	 * @param srcPath
	 *            Path路径
	 * @param text
	 *            删除的数据
	 */
	public void add(String srcPath, String text) {
		File temp = null;
		File file = new File(srcPath);
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			temp = File.createTempFile("temp", "temp");
			pw = new PrintWriter(temp);
			br = new BufferedReader(new FileReader(file));

			while (br.ready()) {
				String line = br.readLine();
				pw.println(line);
			}
			pw.println(text);
			pw.flush();
			br.close();
			pw.close();
			if (temp != null) {
				file.delete();
				temp.renameTo(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除一个文件下的所有文件
	 * 
	 * @param f
	 *            文件
	 */
	public void deleteAllFilesOfDir(File f) {
		if (!f.exists())
			return;
		if (f.isFile()) {
			f.delete();
			return;
		}
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAllFilesOfDir(files[i]);
		}
		f.delete();
	}

	public static void main(String[] args) {
		// 设置为Windows风格
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		new MainFrame();
	}

}
