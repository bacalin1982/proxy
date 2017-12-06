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

                /*
                * [REQ_03]
                * The proxy cache server shall support client HTTP versions such as 1.0 and 1.1, HTTPS is out of scope.
                 */

                //client request
                this.clientRequestStream = new DataInputStream(this.clientSocket.getInputStream());
                this.clientOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());

                /*
                * [REQ_05]
                * Get and decode a HTTP request from a client
                * */
                //read client request
                this.clientRequest = "";
                byte[] clientLine = new byte[ this.clientRequestStream.available()];
                while ( this.clientRequestStream.available() > 0) {
                    this.clientRequestStream.read(clientLine);
                    this.clientRequest += new String(clientLine);
                }

                /*
                * [REQ_04]
                * Only the GET method and static websites shall be compatible.
                *
                * [REQ_07] Evaluating part and forwarding to the client part.
                * If the requested web page is not available locally, the server shall forward the request
                * to the web server, get and decode the response, saving files locally and forward the response
                * to the client. This feature is in reality a cache system with persistence.
                * */
                //make request
                if (!this.clientRequest.isEmpty()) {
                    HttpRequest httpRequest = HttpRequestBuilder.makeHttpRequest(this.clientRequest);
                    if (httpRequest.getMethod().equals("GET")) {
                        System.out.println(httpRequest.toString());
                        HttpResponse httpResponse = Cache.getInstance().getResponseFromRequest(httpRequest);
                        if (httpResponse == null || !httpResponse.isValid(httpRequest)) {
                            /*
                            * [REQ_09]
                            * The server implements a request/response pipelining system to optimize the response for the client.
                            * Thread solution
                            * */
                            //response does not exist in cache
                            System.out.println(Constants._I+Constants.CLIENT_RES_NO_CACHE.replace("%1", httpRequest.getHost()));
                            serverThread = new Request(this.clientSocket, this.clientRequest, this.clientOutputStream);
                            serverThread.start();

                            while(!serverThread.isInterrupted()){
                                continue;
                            }
                        } else {
                            /*
                            * [REQ_06]
                            * If the requested web page is available locally, the server shall transmit it to the client.
                            * */
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
