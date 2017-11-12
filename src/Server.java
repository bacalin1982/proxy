import com.sun.jndi.ldap.Connection;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.net.*;
import java.io.*;

public class Server {

    //Attributes
    private static Server instance = new Server(9090, "127.0.0.1");
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
        } finally {
            System.out.println("Server correctly initialized (@"+this.address+":"+this.port+")");
        }
    }

    public Server getServerInstance(){
        return this.instance;
    }

    public Boolean openServer(){
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
}