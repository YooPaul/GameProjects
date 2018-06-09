package netOmok;
import java.io.IOException;
import java.net.*;
import java.awt.Point;
import java.io.*;
public class NetOmokServer {
	private ServerSocket ss,chatss;
	
	private Socket[] clients=new Socket[2];
	private ObjectOutputStream[] outStreams=new ObjectOutputStream[2];
	private ObjectInputStream[] inStreams=new ObjectInputStream[2];
	
	public NetOmokServer(){
		try {
			ss=new ServerSocket(9698);
			chatss=new ServerSocket(9699);
			System.out.println("Server Started");
			for(int a=0;a<2;a++){
				clients[a]=ss.accept();
				System.out.println("Client Number "+(a+1)+" Connected");
				outStreams[a]=new ObjectOutputStream(clients[a].getOutputStream());
				inStreams[a]=new ObjectInputStream(clients[a].getInputStream());
				outStreams[a].writeObject(a+1+"");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Black().start();
		new White().start();
		new ChatServer().start();
	}
	private class Black extends Thread{
		private Point p;
		public void run(){
			while(inStreams[0]!=null){
				try {
					p=(Point)inStreams[0].readObject();
					outStreams[1].writeObject(p);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				} 
			}
		}
	}
	private class White extends Thread{
		private Point p;
		public void run(){
			while(inStreams[1]!=null){
				try {
					p=(Point)inStreams[1].readObject();
					outStreams[0].writeObject(p);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				} 
			}
		}
	}
	public static void main(String[] args){
		new NetOmokServer();
	}
	
	private class ChatServer extends Thread{
		DataOutputStream[] dos;
		DataInputStream[] dis;
		Socket[] chatClients;
		public ChatServer(){
			chatClients=new Socket[2];
			dos=new DataOutputStream[2];
			dis=new DataInputStream[2];
		}
		@Override
		public void run(){
			for(int i=0;i<2;i++){
				try {
					chatClients[i]=chatss.accept();
					dos[i]=new DataOutputStream(chatClients[i].getOutputStream());
					dis[i]=new DataInputStream(chatClients[i].getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			new ReceiveandSend0().start();
			new ReceiveandSend1().start();
		}
		private class ReceiveandSend0 extends Thread{
			String msg="";
			public void run(){
				while(dis[0]!=null){
					try {
						msg=dis[0].readUTF();
						dos[1].writeUTF(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
				}
			}
		}
		
		private class ReceiveandSend1 extends Thread{
			String msg="";
			public void run(){
				while(dis[1]!=null){
					try {
						msg=dis[1].readUTF();
						dos[0].writeUTF(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
				}
			}
		}
	}
	
}
