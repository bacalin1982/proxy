package baptiste;


import baptiste.Client;

import java.net.ServerSocket;

public class Server implements Runnable{

    private int port;
    private ServerSocket serverSocket;

    public Server(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try {
            System.out.println("Server start...");
            this.serverSocket = new ServerSocket(this.port);
            while(true){
                new Client(this.serverSocket.accept()).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
