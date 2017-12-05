package proxy;

import proxy.bean.HttpRequest;
import proxy.bean.HttpResponse;
import proxy.util.HttpRequestBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

public class Client extends Thread {

    private Thread serverThread;
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
                    HttpRequest httpRequest = HttpRequestBuilder.makeHttpRequest(this.clientRequest);
                    if (httpRequest.getMethod().equals("GET")) {
                        System.out.println(httpRequest.toString());
                        HttpResponse httpResponse = Cache.getInstance().getResponseFromRequest(httpRequest);
                        if (httpResponse == null || !httpResponse.isValid(httpRequest)) {
                            //response does not exist in cache
                            System.out.println("Response for " + httpRequest.getHost() + " does not exist in cache");
                            serverThread = new Request(this.clientSocket, this.clientRequest, this.clientOutputStream);
                            serverThread.start();

                            while(!serverThread.isInterrupted()){
                                continue;
                            }

                        } else {
                            //response exist in cache
                            System.out.println("Response for " + httpRequest.getHost() + " exist in cache");
                            System.out.println(httpResponse.toString());

                            List<byte[]> serverResponseList = httpResponse.getServerResponseList();
                            for (int i = 0; i <= serverResponseList.size(); i++) {
                                clientOutputStream.write(serverResponseList.get(0));
                            }
                            clientOutputStream.flush();

                            //close client socket
                            clientSocket.close();
                            System.out.println("Client " + this.clientSocket.getRemoteSocketAddress().toString() + " close...");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().interrupt();
        }
    }
}