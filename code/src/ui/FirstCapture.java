package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.sun.media.protocol.vfw.DataSource;

public class FirstCapture extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CaptureDeviceInfo captureDeviceInfo = null;
	private MediaLocator mediaLocator = null;
	private static Player player = null;
	// private ImagePanel imagePanel=null;
	private JButton capture;
	private Buffer buffer = null;
	private VideoFormat videoFormat = null;
	private BufferToImage bufferToImage = null;
	private Image image = null;

	public FirstCapture() {

		setLayout(new BorderLayout());
		setSize(320, 550);
		captureDeviceInfo = CaptureDeviceManager
				.getDevice("vfw:Microsoft WDM Image Capture (Win32):0"); // 这里放置的是视频驱动

		mediaLocator = new MediaLocator("vfw://0"); // 这里是视频地址
		// imagePanel=new ImagePanel();
		capture = new JButton("拍照");
		capture.addActionListener(this);
		DataSource ds = new DataSource();
		ds.setLocator(mediaLocator);
		try {
			player = Manager.createRealizedPlayer(mediaLocator);
			player.start();
			Component comp;
			Component comp1;
			if ((comp = player.getVisualComponent()) != null)
				add(comp, BorderLayout.NORTH);
			// if((comp1=player.getControlPanelComponent())!=null)
			// add(comp1,BorderLayout.CENTER);
		} catch (NoPlayerException e) {

		} catch (CannotRealizeException e) {
		} catch (IOException e) {

			System.out.println("ioe");
		}
		// add(imagePanel,BorderLayout.SOUTH);
		add(capture, BorderLayout.CENTER);
	}

	@Override
	public void print(Graphics g) {
		super.print(g);
		g.setColor(new Color(255, 0, 0));
		g.drawLine(0, 0, 100, 100);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("FirstCapture");
		FirstCapture cf = new FirstCapture();

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				player.close();
				System.exit(0);
			}
		});

		f.add("Center", cf);
		f.pack();
		f.setSize(new Dimension(320, 550));
		f.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		FrameGrabbingControl fgc = (FrameGrabbingControl) player
				.getControl("javax.media.control.FrameGrabbingControl");
		buffer = fgc.grabFrame();
		bufferToImage = new BufferToImage((VideoFormat) buffer.getFormat());
		image = bufferToImage.createImage(buffer);
		try {
			ImageIO.write((RenderedImage) image, "jpg",
					new File("F:\\test.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
