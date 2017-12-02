package baptiste;

import baptiste.bean.HttpRequest;
import baptiste.bean.HttpResponse;
import baptiste.util.HttpRequestBuilder;
import baptiste.util.HttpResponseBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
                    this.makeRequest();
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

    public void makeRequest() {
        try {
            HttpRequest httpRequest = HttpRequestBuilder.makeHttpRequest(this.clientRequest);
            if (httpRequest.getMethod().equals("GET")) {
                System.out.println(httpRequest.toString());
                HttpResponse httpResponse = Cache.getInstance().getResponseFromRequest(httpRequest);
                if (httpResponse == null) {
                    //get response from server
                    String host = httpRequest.getHost();
                    Socket serverSocket = new Socket(host, 80);
                    System.out.println("Connect to " + host + " on port 80");

                    int nbrWaiting = 10;
                    S:
                    while (!serverSocket.isClosed()) {
                        try {
                            DataInputStream serverRequestStream = new DataInputStream(serverSocket.getInputStream());
                            DataOutputStream serverResponseStream = new DataOutputStream(serverSocket.getOutputStream());

                            //send client request to server
                            serverResponseStream.writeBytes(this.clientRequest);

                            //tempo
                            while (serverRequestStream.available() == 0) {
                                sleep(1000);
                                System.out.println("waiting for server 1s");
                                if (nbrWaiting == 0) {
                                    serverSocket.close();
                                    continue S;
                                }
                                nbrWaiting--;
                            }

                            //read server response
                            String serverResponse = ""; //for debug
                            List<byte[]> serverResponseList = new ArrayList<>();
                            byte[] serverLine = new byte[serverRequestStream.available()];
                            while (serverRequestStream.available() > 0) {
                                serverRequestStream.read(serverLine);
                                serverResponse += new String(serverLine);
                                //send to client
                                this.clientOutputStream.write(serverLine);
                                this.clientOutputStream.flush();
                                //add
                                serverResponseList.add(serverLine);
                            }

                            //make response
                            if (!serverResponse.isEmpty()) {

                                httpResponse = HttpResponseBuilder.makeHttpResponse(serverResponse, serverResponseList);
                                System.out.println(httpResponse.toString());

                                //Put in cache
                                Cache.getInstance().putResponse(httpRequest, httpResponse);
                                //save data
                                Cache.getInstance().saveToFile(httpRequest, httpResponse);

                                //close server socket
                                serverSocket.close();
                            }

                        } catch (Exception e) {
                            continue S;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
