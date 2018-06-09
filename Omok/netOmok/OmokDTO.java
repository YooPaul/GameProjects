package netOmok;

import java.io.Serializable;

public class OmokDTO implements Serializable {
	private int player;
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	private int xpos;
	private int ypos;
	private int winnerIs;
	public int getXpos() {
		return xpos;
	}
	public void setXpos(int xpos) {
		this.xpos = xpos;
	}
	public int getYpos() {
		return ypos;
	}
	public void setYpos(int ypos) {
		this.ypos = ypos;
	}
	public int getWinnerIs() {
		return winnerIs;
	}
	public void setWinnerIs(int winnerIs) {
		this.winnerIs = winnerIs;
	}
}
