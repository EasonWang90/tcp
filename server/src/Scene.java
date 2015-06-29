import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;
public class Scene {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Scene");
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("floorPlan.jpg"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(750, 750, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(dimg);
		JLabel label = new JLabel(imageIcon);
		frame.setContentPane(label);
		label.setLayout(new BorderLayout());
		ScenePanel locatepanel = new ScenePanel();
		locatepanel.setOpaque(false);
		label.add(locatepanel);
		//frame.getContentPane().add(locatepanel);
		frame.pack();
		frame.setVisible(true);
		
		
		    
	}

}

