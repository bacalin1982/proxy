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
    public Client(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            System.out.println("--");
            System.out.println("Client "+this.clientSocket.getRemoteSocketAddress().toString()+" start...");

            while(!this.clientSocket.isClosed()){

                //client request
                DataInputStream clientRequestStream = new DataInputStream(this.clientSocket.getInputStream());
                DataOutputStream clientOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());

                //read client request
                String clientRequest = "";
                byte[] clientLine = new byte[clientRequestStream.available()];
                while(clientRequestStream.available() > 0){
                    clientRequestStream.read(clientLine);
                    clientRequest += new String(clientLine);
                }

                //make request
                if(!clientRequest.isEmpty()){
                    HttpRequest httpRequest = HttpRequestBuilder.makeHttpRequest(clientRequest);
                    if(httpRequest.getMethod().equals("GET")) {
                        System.out.println(httpRequest.toString());

                        HttpResponse httpResponse = Cache.getInstance().getResponseFromRequest(httpRequest);
                        if(httpResponse == null){
                            //get response from server
                            String host = httpRequest.getHost();
                            Socket serverSocket = new Socket(host, 80);
                            System.out.println("Connect to "+host+" on port 80");

                            int nbrWaiting = 10;
                            S:while(!serverSocket.isClosed()) {
                                try {
                                    DataInputStream serverRequestStream = new DataInputStream(serverSocket.getInputStream());
                                    DataOutputStream serverResponseStream = new DataOutputStream(serverSocket.getOutputStream());

                                    //send client request to server
                                    serverResponseStream.writeBytes(clientRequest);

                                    //tempo
                                    while (serverRequestStream.available() == 0) {
                                        sleep(1000);
                                        System.out.println("waiting for server 1s");
                                        if(nbrWaiting == 0){
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
                                        clientOutputStream.write(serverLine);
                                        clientOutputStream.flush();
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
                        }else{
                            //response exist in cache
                            System.out.println("Response for "+httpRequest.getHost()+" exist in cache");
                            System.out.println(httpResponse.toString());

                            List<byte[]> serverResponseList = httpResponse.getServerResponseList();
                            for(int i=0;i<=serverResponseList.size();i++) {
                                clientOutputStream.write(serverResponseList.get(0));
                            }
                            clientOutputStream.flush();

                        }

                        /*File file = new File(".\\file.xml");
                        JAXBContext jaxbContext = JAXBContext.newInstance(HttpRequest.class);
                        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                        jaxbMarshaller.marshal(httpRequest, file);
*/
                        //Response
                        //outputStreamFromClient = this.clientSocket.getOutputStream();
                    }

                    //close client socket
                    if(clientSocket != null) {
                        clientSocket.close();
                    }
                    //Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    //Customer customer = (Customer) jaxbUnmarshaller.unmarshal(file);
                    //System.out.println(customer);

                    /*
                    File file = new File("C:\\file.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(customer, file);
		jaxbMarshaller.marshal(customer, System.out);
                     */
                    //todo Cache

                    //response
                    //DataOutputStream dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());


                }
            }




            /*READ:while(!this.clientSocket.isClosed()){

                //data
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

                //keep socket open
                if(!this.clientSocket.isClosed() && dataInputStream.available() == 0){
                    System.out.println("Wainting ...");
                    Thread.sleep(1000);
                    continue READ;
                }

                //read socket
                try {
                    byte[] line = new byte[dataInputStream.available()];
                    while(!this.clientSocket.isClosed() && dataInputStream.available() > 0){
                        dataInputStream.read(line);
                        System.out.println(new String(line));
                    }
                }catch(EOFException eof){

                }

                //close
                if(dataInputStream != null) {
                    dataInputStream.close();
                }
            }*/
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                System.out.println("Client "+this.clientSocket.getRemoteSocketAddress().toString()+" close...");
                this.clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
