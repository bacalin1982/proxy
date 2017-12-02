import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Proxy2 {
    public static void main(String[] arg){
        try{
            //use pqrsing string
            String HOST = "perdu.com";
            String PROTO = "HTTP/1.1";
            Socket s = new Socket(InetAddress.getByName(HOST), 80);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.print("GET / "+PROTO+"\r\n");
            //pw.print("Connection: keep-alive\r\n");
            pw.print("Host: "+HOST+"\r\n\r\n");

            pw.print("Connection: close\r\n\r\n");

            pw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String t;
            while((t = br.readLine()) != null){
                System.out.println(t);
            }
            br.close();
            s.close();
            System.out.println("end app");

        }catch(Exception e){
            System.out.println("error:"+e.getMessage());
        }
    }
}

