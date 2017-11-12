import java.net.*;
import java.io.*;

public final class Server {

    //Instance Singleton
    private static volatile Server instance = null;

    /*
    * Attributes of Server:
    * port = port of the server's socket
    * address = address ip of the server
    * sServer = socket object of the server (obj: ServerSocket)
    * sClients = initiate a socket for a client, can we make an array of client socket ?
    *            Should be a good idea for next time ;)
    * */
    private Integer port = null;
    private String address = null;
    private ServerSocket sServer = null;
    private Socket sClients = null;

    /*
    * private Constructor for Singleton instance
    * */
    private Server(Integer port, String address) {
        try {
            this.port = port;
            this.address = address;
            this.sServer = new ServerSocket(this.port); //Address is not configurable ? By default it is loop
            this.sClients = null;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /*
    * Singleton Server create with address loop and port 9090 by default
    * */
    public final static Server getInstance(){
        if (Server.instance == null){
            synchronized (Server.class){
                Server.instance = new Server(9090, "127.0.0.1");
            }
        }
        return Server.instance;
    }

    /*
    * Opening a new socket for a new client and handle it with a thread function for each.
    * in connectionHandler, each client is managed separately
    * This is in connectionHandler where decoding and coding shall be done
    * */
    public void openServer(){
        try {
            this.sClients = this.sServer.accept();
            Runnable connectionHandler = new ConnectionHandler(this.sClients);
            new Thread(connectionHandler).start();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    /*
    * This function close only the socket server not client ones.
    * */
    public Boolean closeServer(){
        Boolean closed = false;
        if(this.sServer != null){
            try {
                this.sServer.close();
                closed = true;
            } catch (IOException e){
                e.printStackTrace();
                this.sServer = null;
            }
        }
        return closed;
    }

    /*
    * Some getters
    * */
    public String getAddress(){
        return this.address;
    }

    public Integer getPort() {
        return this.port;
    }
}