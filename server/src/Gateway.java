
public class Gateway {
	private int radius,x,y;

	public Gateway(int rad, int centreX, int centreY){
		radius=rad;
		x=centreX;
		y=centreY;
	}
	
	public ExperimentArea getSignal(ExperimentArea area){
		ExperimentArea currentArea = area;
		Ceil[][] ceils = currentArea.getAreaCeils();
		for (int i = 0; i < ceils.length; i++) {
			for (int j = 0; j < ceils[i].length; j++) {
				if (!ceils[i][j].insideRangeOfGateway(this)) {
					ceils[i][j].setContent(0);
				}
			}
		}
		return currentArea;
	}
	public ExperimentArea notGetSignal(ExperimentArea area){
		ExperimentArea currentArea = area;
		Ceil[][] ceils = currentArea.getAreaCeils();
		for (int i = 0; i < ceils.length; i++) {
			for (int j = 0; j < ceils[i].length; j++) {
				if (!ceils[i][j].insideRangeOfGateway(this)) {
					ceils[i][j].setContent(1);
				}
			}
		}
		return currentArea;
	}
	
	
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
	
}

