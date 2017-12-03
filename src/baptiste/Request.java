package baptiste;

import baptiste.bean.HttpRequest;
import baptiste.bean.HttpResponse;
import baptiste.tools.Constants;
import baptiste.util.HttpRequestBuilder;
import baptiste.util.HttpResponseBuilder;
import sun.util.resources.cldr.saq.CurrencyNames_saq;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Request extends Thread  {

    private String clientRequest;
    private DataOutputStream clientOutputStream;

    public Request(String clientRequest, DataOutputStream clientOutputStream){
        this.clientRequest = clientRequest;
        this.clientOutputStream = clientOutputStream;
    }

    @Override
    public void run() {
        try {
            HttpRequest httpRequest = HttpRequestBuilder.makeHttpRequest(this.clientRequest);
            if (httpRequest.getMethod().equals("GET")) {
                System.out.println(httpRequest.toString());
                HttpResponse httpResponse = Cache.getInstance().getResponseFromRequest(httpRequest);
                if (httpResponse == null) {
                    //get response from server
                    String host = httpRequest.getHost();
                    Socket serverSocket = new Socket(host, 80);
                    System.out.println(Constants.I_WEB_SERVER_CON.replace("%", host));

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
                                System.out.println(Constants.I_WEB_SERVER_WAIT);
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
        finally {
            Thread.currentThread().interrupt();
        }
    }
}
