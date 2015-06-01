import javax.swing.*;

import java.awt.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;
public class Scene {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Scene");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new ScenePanel());
		frame.pack();
		frame.setVisible(true);
		
		
		    
	}

}

