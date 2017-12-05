package proxy;

import java.net.ServerSocket;
import proxy.tools.Constants;

public class Server implements Runnable{

    private int port;
    private ServerSocket serverSocket;

    public Server(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try {
            System.out.print(Constants._I+Constants.SERVER_START);
            this.serverSocket = new ServerSocket(this.port);
            System.out.println(Constants.OK);
            System.out.println(Constants._I+Constants.SERVER_READY);
            while(true){
                new Client(this.serverSocket.accept()).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}