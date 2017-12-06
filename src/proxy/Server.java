package proxy;

import java.net.ServerSocket;
import proxy.tools.Constants;

public class Server implements Runnable{

    // Attributes
    private int port;
    private ServerSocket serverSocket;

    // Constructor
    public Server(int port){
        this.port = port;
    }

    // Thread Run method
    @Override
    public void run() {
        try {
            // Server is opening a new Socket
            System.out.print(Constants._I+Constants.SERVER_START);
            this.serverSocket = new ServerSocket(this.port);
            System.out.println(Constants.OK);
            System.out.println(Constants._I+Constants.SERVER_READY);
            /*
            * [REQ_08]
            * The server is able to handle multiples client connections in the same time.
            * */
            // Server is waiting infinitely a new client with its opened socket.
            while(true){
                // Each time a client is accepted, a new Thread client is defined.
                new Client(this.serverSocket.accept()).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}