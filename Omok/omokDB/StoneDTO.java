package omokDB;
import java.util.*;
public class StoneDTO {
	private ArrayList<int[]> axis;
	private String stone;
	private int[][] arr=new int[3][3];
	public ArrayList<int[]> getAxis() {
		return axis;
	}
	public void setAxis(ArrayList<int[]> axis) {
		this.axis = axis;
	}
	public String getStone() {
		return stone;
	}
	public void setStone(String stone) {
		this.stone = stone;
	}
	public static void main(String[] args){
		StoneDTO d=new StoneDTO();
		int[][] a=d.arr;
		//for(int i=0;i<a.length;i++){
			//System.out.println(a[i][i]);
		//}
		for(int x=0;x<1;x++){
			System.out.println(x);
		}
	}
}
