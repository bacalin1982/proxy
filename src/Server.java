import java.net.*;
import java.io.*;

public final class Server {

    //Attributes
    private static volatile Server instance = null;
    private Integer port = null;
    private String address = null;
    private ServerSocket sServer = null;
    private Socket sClients = null;

    //Private Singleton Constructor
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

    public final static Server getInstance(){
        if (Server.instance == null){
            synchronized (Server.class){
                Server.instance = new Server(9090, "127.0.0.1");
            }
        }
        return Server.instance;
    }

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

    public String getAddress(){
        return this.address;
    }

    public Integer getPort() {
        return this.port;
    }
}