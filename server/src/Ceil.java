
public class Ceil {
	private double edgeLength;
	private double centerOfCeilX;
	private double centerOfCeilY;
	private int content = 0;
	
	public int getContent() {
		return content;
	}
	public void setContent(int content) {
		this.content = content;
	}
	public Ceil(double edge, double centerX, double centerY){
		this.edgeLength = edge;
		this.centerOfCeilX = centerX;
		this.centerOfCeilY = centerY;
	}
	public Ceil(double edge){
		this.edgeLength = edge;
		this.centerOfCeilX = 0.0;
		this.centerOfCeilY = 0.0;
	}
	public Ceil(){
		this.edgeLength = 0.0;
		this.centerOfCeilX = 0.0;
		this.centerOfCeilY = 0.0;
	}
	public boolean insideRangeOfGateway(Gateway gateway){
		double temp = Math.pow(centerOfCeilX-gateway.getX(), 2) + Math.pow(centerOfCeilY-gateway.getY(), 2);
		if (temp <= Math.pow(gateway.getRadius(), 2)) {
			return true;
		}
		else{
			return false;
		}
	}
	public boolean insideRangeOfBlcokingArea(BlockingArea blockingArea) {
		int x = blockingArea.getOriginalx();
		int y = blockingArea.getOriginaly();
		double length = blockingArea.getAreaLength();
		double width = blockingArea.getAreaWidth();
		if (centerOfCeilX >= x && centerOfCeilX <= x + length && centerOfCeilY >= y && centerOfCeilY <= y + width) {
			return true;
		}
		else{
			return false;
		}
	}
	public static Ceil generateLoc(int width, int height) {
		int x = (int)(Math.random()*width);
		int y = (int)(Math.random()*height);
		Ceil newTag = new Ceil(0, x, y);
		return newTag;
	}
	
	public double getEdgeLength() {
		return edgeLength;
	}
	public void setEdgeLength(double edgeLength) {
		this.edgeLength = edgeLength;
	}
	public double getCenterOfCeilX() {
		return centerOfCeilX;
	}
	public void setCenterOfCeilX(double centerOfCeilX) {
		this.centerOfCeilX = centerOfCeilX;
	}
	public double getCenterOfCeilY() {
		return centerOfCeilY;
	}
	public void setCenterOfCeilY(double centerOfCeilY) {
		this.centerOfCeilY = centerOfCeilY;
	}
	
	
}
