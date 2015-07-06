
public class Gateway {
	private double radius,x,y;

	public Gateway(double rad, double centreX, double centreY){
		this.radius=rad;
		this.x=centreX;
		this.y=centreY;
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
//	public ExperimentArea notGetSignal(ExperimentArea area){
//		ExperimentArea currentArea = area;
//		Ceil[][] ceils = currentArea.getAreaCeils();
//		for (int i = 0; i < ceils.length; i++) {
//			for (int j = 0; j < ceils[i].length; j++) {
//				if (!ceils[i][j].insideRangeOfGateway(this)) {
//					ceils[i][j].setContent(1);
//				}
//			}
//		}
//		return currentArea;
//	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	

	
	
	
}

