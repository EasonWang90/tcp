
public class ReceiveMsg {
	private int gatewayId; 
	private int rssi;
	private int timeStamp;
	
	public ReceiveMsg(){
		this.gatewayId = 0;
		this.rssi = 0;
		this.timeStamp = 0;
	}
	public ReceiveMsg(int gatewayId, int rssi, int timeStamp){
		this.gatewayId = gatewayId;
		this.rssi = rssi;
		this.timeStamp = timeStamp;
	}
	public int getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(int gatewayId) {
		this.gatewayId = gatewayId;
	}
	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	public int getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
