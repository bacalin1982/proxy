package baptiste;

import baptiste.bean.HttpRequest;
import baptiste.bean.HttpResponse;
import baptiste.util.HttpRequestBuilder;
import baptiste.util.HttpResponseBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Request extends Thread  {

    private Socket clientSocket;
    private String clientRequest;
    private DataOutputStream clientOutputStream;

    public Request(Socket clientSocket, String clientRequest, DataOutputStream clientOutputStream){
        this.clientSocket = clientSocket;
        this.clientRequest = clientRequest;
        this.clientOutputStream = clientOutputStream;
    }

    @Override
    public void run() {
        try {
            HttpRequest httpRequest = HttpRequestBuilder.makeHttpRequest(this.clientRequest);
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

                        HttpResponse httpResponse = HttpResponseBuilder.makeHttpResponse(serverResponse, serverResponseList);
                        System.out.println(httpResponse.toString());

                        //Check if response may be put in cache
                        //if(httpResponse.isPutInCache()) {
                            //Put in cache
                            Cache.getInstance().putResponse(httpRequest, httpResponse);
                            //save data
                            Cache.getInstance().saveToFile(httpRequest, httpResponse);
                        /*}else{
                            System.out.println("Response for "+httpRequest.getHost()+" not put in cache");
                        }*/

                        //close server socket
                        serverSocket.close();
                    }

                } catch (Exception e) {
                    continue S;
                }
            }

            //close client socket
            this.clientSocket.close();
            System.out.println("Client " + this.clientSocket.getRemoteSocketAddress().toString() + " close...");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().interrupt();
        }
    }
}
