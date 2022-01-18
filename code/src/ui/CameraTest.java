package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFrame;

public class CameraTest extends JFrame {

	public CameraTest() throws Exception {
		// 先启动摄像头，再做后续的初始化窗体，可保证正确显示摄像头
		initCapture();

		// 设置窗体的一些属性
		this.setTitle("CaptureTest");
		this.setBounds(500, 100, 800, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * 初始化摄像头
	 */
	private void initCapture() throws Exception {
		// 获取所有音频、视频设备
		String str2 = "vfw:Microsoft WDM Image Capture (Win32):0";
		CaptureDeviceInfo deviceList = CaptureDeviceManager.getDevice(str2);
//cameraDevice = ;
		// 获取视频设备，视频设备以vfw打头
		CaptureDeviceInfo cameraDevice =deviceList;
//		for (CaptureDeviceInfo cameraDeviceTmp : deviceList) {
//			if (cameraDeviceTmp.getName().startsWith("vfw")) {
//				
//				break;
//			}
//		}

		if (cameraDevice == null) {
			throw new Exception("找不到摄像头设备");
		}

		// 创建视频播放器
		MediaLocator ml = cameraDevice.getLocator();
		Player player = Manager.createRealizedPlayer(ml);

		if (player == null) {
			throw new Exception("创建摄像头播放器失败");
		}

		// 播放视频
		player.start();

		// 将播放器加入窗体
		Component comp = null;
		if ((comp = player.getVisualComponent()) != null)
			add(comp, BorderLayout.CENTER);

	}

	public static void main(String[] args) {
		try {

			CameraTest mc = new CameraTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
