import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketServer implements Runnable{
	 ServerSocket server;
	 int gatewayID;
	 Callback newPanel; 
	 int rssi;
	 int transmissionRange;
	 Thread t;
	public SocketServer(ScenePanel panel) throws IOException{
		newPanel = panel;
		System.out.println("Start server...");
		server=new ServerSocket(5258);
		t = new Thread(this, "abc");
		t.start();
		System.out.println("server waiting..");
	}
	public void run(){

		while (true) {
			
			Socket client=null;
			try {
				client=server.accept();
				System.out.println("client accept");
				SocketClient socketClient= new SocketClient(client, this.newPanel);
				Thread tt = new Thread(socketClient);
				tt.start();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	
			
		}
	}
}
