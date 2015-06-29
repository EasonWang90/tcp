import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
public class Task extends TimerTask {
	private ArrayList<ArrayBlockingQueue<ReceiveMsg>> clients;
	private Callback callback;
	private int[] receivedSignalGateway;
	private long timeInterval;
	public Task(ArrayList<ArrayBlockingQueue<ReceiveMsg>> clients, Callback callback, long timeInterval) {
		this.clients = clients;
		this.callback = callback;
		this.timeInterval = timeInterval;
		receivedSignalGateway = new int[clients.size()];
	}
	public void run() {
//		Calendar rightNow = Calendar.getInstance();
//		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
//		int min = rightNow.get(Calendar.MINUTE);
//		int second = rightNow.get(Calendar.SECOND);
//		int milisecond = rightNow.get(Calendar.MILLISECOND);
//		int totalTime = ((hour*60+min)*60+second)*1000+milisecond;
//		System.out.println(hour+":"+min+":"+second+"."+milisecond);
		for (int i = 0; i < clients.size(); i++) {
			ArrayBlockingQueue<ReceiveMsg> gateway = clients.get(i);
			
//			while (!gateway.isEmpty()) {
//				ReceiveMsg msg = gateway.poll();
//				int timeStamp = msg.getTimeStamp();
//				System.out.println(msg.getTimeStamp()+" , "+(totalTime+timeInterval));
//				if (timeStamp <= totalTime+timeInterval) {
//					receivedSignalGateway[i] = 1;
//				}
//			}
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
