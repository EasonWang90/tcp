import javax.swing.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;


public class ScenePanel extends JPanel implements Callback{
	final static int numOfGateways = 4;
	private double miniCeilLength = 0.02;
	private int numOfBlockArea = 2;
	private double areaLength = 11;
	private double areaWidth = 6.5;
	final static int queueCapacity = 1000;
	private long timeInterval = 2000;
	private double transRange = 4.5;
	ExperimentArea[] experimentAreas;
	ExperimentArea finalIntersectArea;
	private ArrayList<ArrayBlockingQueue<ReceiveMsg>> clients;
	Gateway[] gateways;
	BlockingArea[] blockAreas;
	int multiplyer = 100;
	Ceil tag;
	

	public ScenePanel() throws IOException{
		// experiment area initialization
				//readUserInput();
				experimentAreas = new ExperimentArea[numOfGateways];
				for (int i = 0; i < experimentAreas.length; i++) {
					experimentAreas[i] = new ExperimentArea(miniCeilLength, areaLength, areaWidth);
					experimentAreas[i].init();
				}
				// init final area
				finalIntersectArea = new ExperimentArea(miniCeilLength, areaLength, areaWidth);
				finalIntersectArea.init();
				
				/*
				 * set blocking area like pillars
				 */
				double blockLength = 0.7, blockWidth = 1.3;
				double originalX = 1, originalY = 2.4;
				blockAreas = new BlockingArea[numOfBlockArea];
				blockAreas[0] = new BlockingArea(blockLength, blockWidth, originalX, originalY);
				blockLength = 0.2; 
				blockWidth = 0.2;
				originalX = 7.6;
				originalY = 3;
				blockAreas[1] = new BlockingArea(blockLength, blockWidth, originalX, originalY);
				finalIntersectArea.intersectWithBlockAreas(blockAreas);
				//finalIntersectArea.printExArea();
				finalIntersectArea.noSignal();
				gateways = new Gateway[numOfGateways];
				
				clients = new ArrayList<ArrayBlockingQueue<ReceiveMsg>>();
				for (int i = 0; i < numOfGateways; i++) {
					ArrayBlockingQueue<ReceiveMsg> client = new ArrayBlockingQueue<ReceiveMsg>(queueCapacity);
					clients.add(client);
				}
				
				SocketServer newServer = new SocketServer(clients,this,timeInterval);
				
				// gateways initialization
				setPreferredSize(new Dimension((int)(areaLength*multiplyer), (int)(areaWidth*multiplyer)));
				
		
		
	}
	
	public void paintComponent(Graphics page) { // we assume transmission range change from large to small
		super.paintComponent(page);
		
		Color customColor = new Color(0, 102, 204, 100);
		Ceil[][] interCeils = finalIntersectArea.getAreaCeils();
		for (int i = 0; i < interCeils.length; i++) {
			for (int j = 0; j < interCeils[i].length; j++) {
				if (interCeils[i][j].getContent() == 1) {
					drawCeil(page, interCeils[i][j],customColor);
				}
				if (interCeils[i][j].getContent() == -1) {
					drawCeil(page, interCeils[i][j],Color.GRAY);
				}
			}
		}

		try {
			drawGateways(page, gateways, Color.black);
		} catch (Exception e) {
			// TODO: handle exception
			//System.out.println("No gateways draw");
		}
		
		
		//locateTagOnScreen(page);
		
	}
	
	public void readUserInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter the width of testing area(meter) (eg. 20): ");
		this.areaWidth = scanner.nextInt();
		System.out.println("Please enter the length of testing area(meter) (eg. 20): ");
		this.areaLength = scanner.nextInt();
		System.out.println("Please enter matrix precision of testing area(meter) (eg. 0.1): ");
		this.miniCeilLength = scanner.nextDouble();
		System.out.println("Please enter the update frequency(ms) (eg. 3000): ");
		this.timeInterval = scanner.nextLong();
		System.out.println("Please enter the transmission range of the tag(meter) (eg. 15): ");
		this.transRange = scanner.nextInt();
	}
	
	public void drawCeil(Graphics page, Ceil ceil, Color color) {
		Graphics2D page2d = (Graphics2D)page;
		double x = (ceil.getCenterOfCeilX() - ceil.getEdgeLength()/2)*multiplyer;
		double y = (ceil.getCenterOfCeilY() - ceil.getEdgeLength()/2)*multiplyer;
		double edge = ceil.getEdgeLength()*multiplyer;
		Rectangle2D.Double ceilShape = new Rectangle2D.Double(x, y, edge, edge);
		page2d.setColor(color);
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
			
			//page.drawOval((int)(current.getX()*multiplyer-current.getRadius()*multiplyer), (int)(current.getY()*multiplyer-current.getRadius()*multiplyer), (int)(2*current.getRadius()*multiplyer), (int)(2*current.getRadius()*multiplyer));
			page.setColor(Color.GREEN);
			int r = 30;
			int gatewayX = (int)(current.getX()*multiplyer);
			int gatewayY = (int)(current.getY()*multiplyer);
			//System.out.println(gatewayX+","+gatewayY);
			gatewayX = gatewayX - ( r / 2);
			gatewayY = gatewayY - ( r / 2);
			page.fillOval(gatewayX, gatewayY, r, r);
		}
	}
	public void locateTagOnScreen(Graphics page) {
		page.setColor(Color.black);
		int tagX = (int)tag.getCenterOfCeilX()*multiplyer;
		int tagY = (int)tag.getCenterOfCeilY()*multiplyer;
		page.fillOval(tagX, tagY, 3, 3);
	}

	@Override
	public void getMsg(int[] gateway) {
		// TODO Auto-generated method stub
			boolean totallyNoSignal = true;
			System.out.println("Locate Object every 2s!");
			/*
			 * reset 
			 */
			for (int i = 0; i < experimentAreas.length; i++) {
				experimentAreas[i].reset();
			}
			finalIntersectArea.reset();
			/*
			 * Define gateways and their locations
			 */
			gateways = new Gateway[numOfGateways];
			
			gateways[0] = new Gateway(this.transRange, 1.6, 0.3);
			gateways[1] = new Gateway(this.transRange, 7.5, 0.3);
			gateways[2] = new Gateway(this.transRange, 7.5, 6.2);
			gateways[3] = new Gateway(this.transRange, 1.6, 6.2);
			
			for (int i = 0; i < gateway.length; i++) {
				if (gateway[i] == 1) {
					System.out.println("gateway "+i+" get signal!");
					experimentAreas[i] = gateways[i].getSignal(experimentAreas[i]);
					totallyNoSignal = false;
				}
			}
			for (int j = 0; j < gateways.length; j++) {
				finalIntersectArea.andOperation(experimentAreas[j]);
			}
			
			if(totallyNoSignal == true){
				finalIntersectArea.noSignal();
			}
			//finalIntersectArea.printExArea();
			finalIntersectArea.intersectWithBlockAreas(blockAreas);
			this.repaint();
		
	}
	
	
}
