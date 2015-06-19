import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;


public class SocketClient implements Runnable{
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private boolean running = true;
	private ArrayList<ArrayBlockingQueue<ReceiveMsg>> clients;
	//private Callback newPanel; 

	public SocketClient(Socket client, ArrayList<ArrayBlockingQueue<ReceiveMsg>> clients){
		try {
			BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
			in = new DataInputStream(bis);
			out = new DataOutputStream(client.getOutputStream());
			//this.newPanel = panel;
			this.clients = clients;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(running){
			try {
				byte[] gatewayIdByte = new byte[4];	
				in.read(gatewayIdByte);
				byte[] rssiByte = new byte[4];
				in.read(rssiByte);
				byte[] timeByte = new byte[4];
				in.read(timeByte);
//				for (byte b : timeByte) {
//					   System.out.format("0x%x ", b);
//				}
				
				int gatewayID = gatewayIdByte[0];
		        int rssi = rssiByte[0];
		        int timeStamp = getIntFromByteArray(timeByte);
		        
		        System.out.println("GatewayID: "+gatewayID);
		        System.out.println("rssi: "+rssi);
				System.out.println("Time stamp: "+timeStamp);
				//newPanel.getMsg(gatewayID, rssi, transmissionRange);
				ReceiveMsg newMsg = new ReceiveMsg(gatewayID, rssi, timeStamp);
				if (gatewayID == 0) {
					clients.get(0).put(newMsg);
				}
				else if (gatewayID == 1) {
					clients.get(1).put(newMsg);
				}
				else if(gatewayID == 2){
					clients.get(2).put(newMsg);
				}
				else{
					clients.get(3).put(newMsg);
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				running = false;
			}
		}
	}
	private int getIntFromByteArray(byte[] b){
		int x = java.nio.ByteBuffer.wrap(b).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
		return x;
	}
	public void startReceive(int command) throws IOException{
		out.writeInt(command);
		//out.close();
	}
	
}
