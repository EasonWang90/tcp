import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;

import jdk.nashorn.internal.codegen.CompilerConstants.Call;


public class SocketServer implements Runnable{
	 private ServerSocket server;
	 private ArrayList<SocketClient> clientList;
	 private ArrayList<ArrayBlockingQueue<ReceiveMsg>> clients;
	 private Callback callback;
	 private long timedelay = 20;
	 private long timeInterval;
	public SocketServer(ArrayList<ArrayBlockingQueue<ReceiveMsg>> clients, Callback callback,long timeInterval) throws IOException{
		clientList = new ArrayList<SocketClient>();
		this.clients = clients;
		this.callback = callback;
		this.timeInterval = timeInterval;
		System.out.println("Start server...");
		server=new ServerSocket(5258);
		Thread t = new Thread(this);
		t.start();
		System.out.println("server waiting..");
	}
	public void run(){

		while (true) {
			
			Socket client=null;
			try {
				client=server.accept();
				System.out.println("client accept");
				SocketClient socketClient= new SocketClient(client, clients);
				Thread tt = new Thread(socketClient);
				tt.start();
				clientList.add(socketClient);
				if(clientList.size() == 4){
					Scanner scan = new Scanner(System.in);
					System.out.println("Enter 1 to start all clients!");
					int command = scan.nextInt();
					if(command == 1){
						for(SocketClient oneClient : clientList)
					      oneClient.startReceive(command);
						Timer time = new Timer();
						time.schedule(new Task(clients,callback,timeInterval), timedelay, timeInterval);
					}
					else{
						System.out.println("Did not find the command!");
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	 
			
		}
	}
}
