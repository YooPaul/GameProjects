package netOmok;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.*;
public class OmokServer {
	private ServerSocket ss;
	private Socket socket;
	private ArrayList<ObjectOutputStream> client_list;
	public OmokServer(){
		try {
			client_list=new ArrayList<ObjectOutputStream>();
			Collections.synchronizedList(client_list);
			ss=new ServerSocket(20016);
			for(int player=1;player<=2;player++){
				socket=ss.accept();
				StoneInfo si=new StoneInfo(socket,player);
				si.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public class StoneInfo extends Thread {
		Socket socket;
		ObjectOutputStream oos;
		ObjectInputStream ois;
		int player;
		public StoneInfo(Socket socket,int player){
			this.socket=socket;
			this.player=player;
			try{
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			client_list.add(oos);
			Integer a=player;
			oos.writeObject(a);
			}catch(IOException e){}
		}
		public void run(){
			while(true){
				try {
					OmokDTO dto=(OmokDTO)ois.readObject();
					if(player==1){
						client_list.get(player).writeObject(dto);
					}else if(player==2){
						client_list.get(0).writeObject(dto);
					}
						
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
	}
	public static void main(String[] args){
		new OmokServer();
		System.out.println("서버 시작중");
	}
}
