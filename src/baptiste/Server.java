package baptiste;

import java.net.ServerSocket;
import baptiste.tools.Constants;

public class Server implements Runnable{

    private int port;
    private ServerSocket serverSocket;

    public Server(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try {
            System.out.println(Constants.SERVER_START);
            this.serverSocket = new ServerSocket(this.port);
            while(true){
                new Client(this.serverSocket.accept()).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
