import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
public class Task extends TimerTask {
	private ArrayList<ArrayBlockingQueue<ReceiveMsg>> clients;
	private Callback callback;
	private int[] receivedSignalGateway;
	public Task(ArrayList<ArrayBlockingQueue<ReceiveMsg>> clients, Callback callback) {
		this.clients = clients;
		this.callback = callback;
		receivedSignalGateway = new int[clients.size()];
	}
	public void run() {
		
		for (int i = 0; i < clients.size(); i++) {
			ArrayBlockingQueue<ReceiveMsg> gateway = clients.get(i);
			if (gateway.size() != 0) {
				receivedSignalGateway[i] = 1;
				gateway.clear();
			}
		}
		callback.getMsg(receivedSignalGateway);
		resetGatewaySignal();
    }
	public void resetGatewaySignal() {
		for (int i = 0; i < receivedSignalGateway.length; i++) {
			receivedSignalGateway[i] = 0;
		}
	}
}
