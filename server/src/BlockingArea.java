
public class BlockingArea{
	private double originalx;
	private double originaly;
	private double areaLength;
	private double areaWidth;
	public BlockingArea(double areaLength, double areaWidth, double x, double y){
		this.areaLength = areaLength;
		this.areaWidth = areaWidth;
		this.originalx = x;
		this.originaly = y;
	}
	
	public double getAreaLength() {
		return areaLength;
	}

	public void setAreaLength(double areaLength) {
		this.areaLength = areaLength;
	}

	public double getAreaWidth() {
		return areaWidth;
	}

	public void setAreaWidth(double areaWidth) {
		this.areaWidth = areaWidth;
	}

	public double getOriginalx() {
		return originalx;
	}
	public void setOriginalx(int originalx) {
		this.originalx = originalx;
	}
	public double getOriginaly() {
		return originaly;
	}
	public void setOriginaly(int originaly) {
		this.originaly = originaly;
	}

	
}
