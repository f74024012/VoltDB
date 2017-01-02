import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookGraphException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.Page;

import org.voltdb.client.ClientFactory;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

public class F74024012 extends Thread{
	static int count=0;
	long[] A=new long[2];
	static org.voltdb.client.Client client;
	public void run(){
		try {
			FileReader fr=new FileReader("test.txt");
			BufferedReader br=new BufferedReader(fr);
			String line;
			for(int i=0;i<(count-1)*300;i++){
				br.readLine();
			}
			for(int i=0;i<300;i++){
				line=br.readLine();
				A=F74024012_read(line);
				try {
					if(A[0]==0 && A[1]==0){
					}else{
						client.callProcedure("@AdHoc", "INSERT INTO F74024012 VALUES("+"'"+line+"'"+","+A[0]+","+A[1]+")");
					}
				}catch(FacebookOAuthException foae){
					//System.out.println("error!");
				}catch(FacebookGraphException fge){
					//System.out.println("not exist!");
				}catch (ProcCallException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (NoConnectionsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		client=ClientFactory.createClient();
		try {
			client.createConnection("localhost");
			client.callProcedure("@AdHoc", "DROP TABLE F74024012 IF EXISTS;");
			client.callProcedure("@AdHoc", "CREATE Table F74024012(GID VARCHAR,UTALK BIGINT,ULIKE BIGINT);");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (ProcCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<10;i++){
			count++;
			Thread t = new F74024012();
			t.start();
			try {
				t.join(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static long[] F74024012_read(String GID){
		System.out.println(GID);
		long [] A=new long[2];
		Page like;
		Page talk;
		String accessToken="EAAE6F4yyC4IBAEl5FLeO9hMEXyMHRnr4zr1beVjXJbVIx36gEljNjLZAt8J7OIkK5WoSMTZC6wL7hnfYLPC2fK3mzpWNtUO7OYG2FZAacjBnZB8ZCDZAxBlASMsr85AuB0cSFCvRUY8z63DfrZARZAfPTqbthHcAkpsZD";
		FacebookClient fbClient=new DefaultFacebookClient(accessToken,Version.VERSION_2_8);
		try{
			like = fbClient.fetchObject(GID, Page.class,Parameter.with("fields","fan_count"));
			talk = fbClient.fetchObject(GID, Page.class,Parameter.with("fields","talking_about_count"));
			A[0]=talk.getTalkingAboutCount();
			A[1]=like.getFanCount();
		}catch(FacebookOAuthException foae){
			//System.out.println("error!");
		}catch(FacebookGraphException fge){
			//System.out.println("not exist!");
		} 
		return A;
	}
}