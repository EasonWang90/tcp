import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketClient implements Runnable{
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private boolean running = true;
	ServerSocket server;
	int gatewayID;
	Callback newPanel; 
	int rssi;
	int transmissionRange;
	
	public SocketClient(Socket client, Callback panel){
		try {
			in = new DataInputStream(client.getInputStream());
			out = new DataOutputStream(client.getOutputStream());
			this.newPanel = panel;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(running){
			byte[] b = new byte[12];
			try {
				in.read(b);
				this.gatewayID = b[0];
		        this.rssi = b[4];
		        this.transmissionRange = b[8];
		        
		        System.out.println("GatewayID: "+gatewayID);
		        System.out.println("rssi: "+rssi);
				
				newPanel.getMsg(gatewayID, rssi, transmissionRange);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				running = false;
			}
		}
	}
	
	public void sendMsg(String s){
		
	}
	
}
