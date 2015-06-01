import javax.swing.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ScenePanel extends JPanel implements Callback{
	final static int numOfGateways = 4;
	final static double miniCeilLength = 0.1;
	final static int areaLength = 20;
	final static int areaWidth = 20;
	ExperimentArea[] experimentAreas;
	ExperimentArea finalIntersectArea;
	Gateway[] gateways;
	int gateway, rssi, range;
	int multiplyer = 30;
	Ceil tag;
	

	public ScenePanel() throws IOException{
		// experiment area initialization
				
				experimentAreas = new ExperimentArea[numOfGateways];
				for (int i = 0; i < experimentAreas.length; i++) {
					experimentAreas[i] = new ExperimentArea(miniCeilLength, areaLength, areaWidth);
					experimentAreas[i].init();
				}
				// init final area
				finalIntersectArea = new ExperimentArea(miniCeilLength, areaLength, areaWidth);
				finalIntersectArea.init();
				Ceil[][] ceils = finalIntersectArea.getAreaCeils();
				for (int i = 0; i < ceils.length; i++) {
					for (int j = 0; j < ceils[i].length; j++) {
							ceils[i][j].setContent(1);
						
					}
				}
				
				gateways = new Gateway[numOfGateways];
				SocketServer newServer = new SocketServer(this);
				System.out.println("Thread is alive: "
		                + newServer.t.isAlive());
				
				// gateways initialization
				setPreferredSize(new Dimension(20*multiplyer, 20*multiplyer));
				
		
		
	}
	
	public void paintComponent(Graphics page) { // we assume transmission range change from large to small
		super.paintComponent(page);
		
		Ceil[][] interCeils = finalIntersectArea.getAreaCeils();
		for (int i = 0; i < interCeils.length; i++) {
			for (int j = 0; j < interCeils[i].length; j++) {
				if (interCeils[i][j].getContent() == 1) {
					drawCeil(page, interCeils[i][j]);
				}
				
			}
		}
		try {
			drawGateways(page, gateways, Color.black);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("No gateways draw");
		}
		
		
		//locateTagOnScreen(page);
		
	}
	
	public void drawCeil(Graphics page, Ceil ceil) {
		Graphics2D page2d = (Graphics2D)page;
		double x = (ceil.getCenterOfCeilX() - ceil.getEdgeLength()/2)*multiplyer;
		double y = (ceil.getCenterOfCeilY() - ceil.getEdgeLength()/2)*multiplyer;
		double edge = ceil.getEdgeLength()*multiplyer;
		Rectangle2D.Double ceilShape = new Rectangle2D.Double(x, y, edge, edge);
		page2d.setColor(Color.blue);
		page2d.fill(ceilShape);
	}
	
	public void drawGateways(Graphics page, Gateway[] gateways, Color color) {
		for (int i = 0; i < gateways.length; i++) {
			
			Gateway current = gateways[i];
			if (i > 3) {
				page.setColor(Color.yellow);
			}
			else{
				page.setColor(color);
			}
			
			page.drawOval(current.getX()*multiplyer-current.getRadius()*multiplyer, current.getY()*multiplyer-current.getRadius()*multiplyer, 2*current.getRadius()*multiplyer, 2*current.getRadius()*multiplyer);
		}
	}
	public void locateTagOnScreen(Graphics page) {
		page.setColor(Color.black);
		int tagX = (int)tag.getCenterOfCeilX()*multiplyer;
		int tagY = (int)tag.getCenterOfCeilY()*multiplyer;
		page.fillOval(tagX, tagY, 3, 3);
	}

	@Override
	public void getMsg(int gateway, int rssi, int tranmissionRange) {
		// TODO Auto-generated method stub
		if (rssi != 0) {
			gateways[0] = new Gateway(tranmissionRange, 0, 0);
			gateways[1] = new Gateway(tranmissionRange, 20, 0);
			gateways[2] = new Gateway(tranmissionRange, 0, 20);
			gateways[3] = new Gateway(tranmissionRange, 20, 20);
			experimentAreas[gateway] = gateways[gateway].getSignal(experimentAreas[gateway]);
			/*
			for (int i = 0; i < gateways.length; i++) {
				if (tag.insideRangeOfGateway(gateways[i])) {
					int getSignal = (int)(Math.random()*2);
					System.out.println(getSignal);
					if (getSignal == 1) {
						experimentAreas[i] = gateways[i].getSignal(experimentAreas[i]);
						System.out.println("Gateway "+i+ " get signal!");
					}
					else{
						System.out.println("Gateway "+i+ " not get signal!");
					}
				}
				
				//experimentAreas[i].printExArea();
				//System.out.println("--------------------------------");
			}*/
			
			//ExperimentArea finalArea;
			

			for (int j = 0; j < gateways.length; j++) {
				finalIntersectArea.andOperation(experimentAreas[j]);
			}
			repaint();
		}
		
	}
	
	
}
