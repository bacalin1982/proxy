package proxy;

import proxy.bean.HttpRequest;
import proxy.bean.HttpResponse;
import proxy.util.HttpRequestBuilder;
import proxy.tools.Constants;

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
            System.out.println(Constants._I+Constants.CLIENT_NEW.replace("%1", this.clientSocket.getRemoteSocketAddress().toString()));

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
                            System.out.println(Constants._I+Constants.CLIENT_RES_NO_CACHE.replace("%1", httpRequest.getHost()));
                            serverThread = new Request(this.clientSocket, this.clientRequest, this.clientOutputStream);
                            serverThread.start();

                            while(!serverThread.isInterrupted()){
                                continue;
                            }

                        } else {
                            //response exist in cache
                            System.out.println(Constants._I+Constants.RESPONSE_IS_ALR_CACHED.replace("%1", httpRequest.getHost()));
                            System.out.println(httpResponse.toString());

                            List<byte[]> serverResponseList = httpResponse.getServerResponseList();
                            for (int i = 0; i <= serverResponseList.size(); i++) {
                                clientOutputStream.write(serverResponseList.get(0));
                            }
                            clientOutputStream.flush();

                            //close client socket
                            clientSocket.close();
                            System.out.println(Constants._I+Constants.CLIENT_CLOSE.replace("%1", this.clientSocket.getRemoteSocketAddress().toString()));
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
