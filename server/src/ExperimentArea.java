
public class ExperimentArea {
	private Ceil[][] areaCeils;
	private double miniCeilLength;
	private int areaLength;
	private int areaWidth;
	
	public ExperimentArea(double miniCeilLength, int areaLength, int areaWidth){
		this.miniCeilLength = miniCeilLength;
		this.areaLength = areaLength;
		this.areaWidth = areaWidth;
	}
	public void init(){
		int ceilLength = (int)(areaLength/this.miniCeilLength);
		int ceilWidth = (int)(areaWidth/this.miniCeilLength);
		areaCeils = new Ceil[ceilWidth][ceilLength];
		
		double minWidth = 0.0;
		for (int i = 0; i < ceilWidth; i++) {
			double minLength = 0.0;
			double y = minWidth + miniCeilLength/2;
			
			for (int j = 0; j < ceilLength; j++) {
				double x = minLength + miniCeilLength/2;
				areaCeils[i][j] = new Ceil(miniCeilLength,x,y);
				minLength += miniCeilLength;
			}
			minWidth += miniCeilLength;
		}
	}
	
	public ExperimentArea andOperation(ExperimentArea otherarea){
		Ceil[][] otherCeils = otherarea.getAreaCeils();
		for (int i = 0; i < otherCeils.length; i++) {
			for (int j = 0; j < otherCeils[i].length; j++) {
				if (this.areaCeils[i][j].getContent() == 1 && otherCeils[i][j].getContent() == 0) {
					this.areaCeils[i][j].setContent(0);
				}
			}
		}
		return this;
	}
	
	public void printExArea(){
		int count = 0;
		for (int i = 0; i < areaCeils.length; i++) {
			for (int j = 0; j < areaCeils[i].length; j++) {
				System.out.print(areaCeils[i][j].getContent()+" ");
				count++;
			}
			System.out.println();
		}
		System.out.println(count);
	}
	
	public Ceil[][] getAreaCeils() {
		return areaCeils;
	}
	public void setAreaCeils(Ceil[][] areaCeils) {
		this.areaCeils = areaCeils;
	}
	public double getMiniCeilLength() {
		return miniCeilLength;
	}
	public void setMiniCeilLength(double miniCeilLength) {
		this.miniCeilLength = miniCeilLength;
	}
	public int getAreaLength() {
		return areaLength;
	}
	public void setAreaLength(int areaLength) {
		this.areaLength = areaLength;
	}
	public int getAreaWidth() {
		return areaWidth;
	}
	public void setAreaWidth(int areaWidth) {
		this.areaWidth = areaWidth;
	}
}
