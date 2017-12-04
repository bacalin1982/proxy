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
            System.out.println(Constants._I+Constants.I_SERVER_START);
            this.serverSocket = new ServerSocket(this.port);
            System.out.println(Constants._I+Constants.I_SERVER_READY);
            while(true){
                new Client(this.serverSocket.accept()).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}