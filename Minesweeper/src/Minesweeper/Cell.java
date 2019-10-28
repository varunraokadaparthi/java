package Minesweeper;

public class Cell {
	private boolean isOpened;
	private boolean isBomb;
	private boolean isFlag;
	private int neighbourBombCount;
	
	public int getNeighbourBombCount() {
		return neighbourBombCount;
	}
	public void setNeighbourBombCount(int neighbourBombCount) {
		this.neighbourBombCount = neighbourBombCount;
	}
	public boolean isOpened() {
		return isOpened;
	}
	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}
	public boolean isBomb() {
		return isBomb;
	}
	public void setBomb(boolean isBomb) {
		this.isBomb = isBomb;
	}
	
	public boolean isFlag() {
		return isFlag;
	}
	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}
	public Cell(boolean isOpened, boolean isBomb, boolean isFlag) {
		super();
		this.isOpened = isOpened;
		this.isBomb = isBomb;
		this.isFlag = isFlag;
	}
	public Cell() {
		super();
	}
	
	

}
