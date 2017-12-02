package baptiste;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {

    private Socket clientSocket;
    private String clientRequest;
    private DataInputStream clientRequestStream;
    private DataOutputStream clientOutputStream;

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            System.out.println("--");
            System.out.println("Client " + this.clientSocket.getRemoteSocketAddress().toString() + " start...");

            while (!this.clientSocket.isClosed()) {

                //client request
                this.clientRequestStream = new DataInputStream(this.clientSocket.getInputStream());
                this.clientOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());

                //read client request
                this.clientRequest = "";
                byte[] clientLine = new byte[ this.clientRequestStream.available()];
                while ( this.clientRequestStream.available() > 0) {
                    this.clientRequestStream.read(clientLine);
                    this.clientRequest += new String(clientLine);
                }

                //make request
                if (!this.clientRequest.isEmpty()) {
                    new Request(this.clientRequest, this.clientOutputStream).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Client " + this.clientSocket.getRemoteSocketAddress().toString() + " close...");
                this.clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
