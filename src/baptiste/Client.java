package baptiste;

import baptiste.bean.HttpRequest;
import baptiste.bean.HttpResponse;
import baptiste.util.ClientFetcher;
import baptiste.util.HttpRequestBuilder;
import sun.net.www.http.HttpClient;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {

    private int port;
    private Socket socket;
    private OutputStream outStream;
    private ArrayList<String> listObject;
    private boolean isConnceted = false;
    private String resourceName;
    private InputStream inputStreamFromServer;
    private OutputStream outputStreamFromServer;
    private Timer timer;
    private boolean isRunning;


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
                    if(!httpRequest.getMethod().equals("CONNECT")) {
                        System.out.println(httpRequest.toString());

                        HttpResponse httpResponse = Cache.getInstance().getResponseFromRequest(httpRequest);
                        if(httpResponse == null){
                            //get response from server
                            //ClientFetcher cf = new ClientFetcher(httpRequest.getHost(), 80, clientOutputStream);
                            //cf.fetchObject(clientRequest);

                            /*final URL url = new URL(httpRequest.getUrl());
                            final HttpURLConnection connectionHttp = (HttpURLConnection) url.openConnection();
                            connectionHttp.setRequestMethod(httpRequest.getMethod());
                            for (final String key : requestHeader.getParameter().keySet()) {
                                //if (isNotSecretToken(key)) {
                                    String value = requestHeader.getParameter().get(key);
                                    connectionHttp.addRequestProperty(key, value);
                                //}
                            }

                            // Send response back to client
                            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            final DataInputStream dis = new DataInputStream(connectionHttp.getInputStream());

                            // Add response header
                            final StringBuilder sb = new StringBuilder();
                            sb.append(requestHeader.getHttpVersion() + " "
                                    + connectionHttp.getResponseCode() + " "
                                    + connectionHttp.getResponseMessage() + "\r\n");
                            final Map<String, List<String>> map = connectionHttp.getHeaderFields();
                            for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
                                final String key = entry.getKey();
                                sb.append(key + " : " + entry.getValue().toString().replace("[", "")
                                        .replace("]", "").replace(",", " ")
                                        + "\r\n");
                            }
                            sb.append("\r\n");

                            // Add response content
                            baos.write(sb.toString().getBytes(), 0, sb.toString().getBytes().length);
                            final byte[] data = new byte[(int) Short.MAX_VALUE];
                            int index = dis.read(data, 0, (int) Short.MAX_VALUE);
                            while (index != -1) {
                                baos.write(data, 0, index);
                                index = dis.read(data, 0, (int) Short.MAX_VALUE);
                            }
                            final byte[] result = baos.toByteArray();
                            */
                            //while(true) {
                                //try {

                            HttpClient client = HttpClient.New(new URL(httpRequest.getUrl()));


                            Socket serverSocket = new Socket(httpRequest.getHost(), 80);

                                    //while (!connectionHttp.isClosed()) {
                                        DataInputStream serverRequestStream = new DataInputStream(client.getInputStream());
                                        DataOutputStream serverResponseStream = new DataOutputStream(client.getOutputStream());

                                        //read server request
                                        String serverRequest = "";
                                        byte[] serverLine = new byte[serverRequestStream.available()];
                                        while (clientRequestStream.available() > 0) {
                                            serverRequestStream.read(serverLine);
                                            serverRequest += new String(serverLine);
                                            clientOutputStream.write(serverLine);
                                            clientOutputStream.flush();
                                        }

                                        //make request
                                        if (!serverRequest.isEmpty()) {

                                        }
                                    //}
                                /*}catch(Exception e){
                                    continue;
                                }*/
                            //}
                        }else{

                        }

                        /*File file = new File(".\\file.xml");
                        JAXBContext jaxbContext = JAXBContext.newInstance(HttpRequest.class);
                        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                        jaxbMarshaller.marshal(httpRequest, file);
*/
                        //Response
                        //outputStreamFromClient = this.clientSocket.getOutputStream();
                    }

                    //close
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
    private void initTimer(Socket serverSocket, String host) {
        if (this.timer == null) {
            this.timer = new Timer("KAL Timer with : " + host);
        }
        if (isRunning) {
            timer.cancel();
            this.timer = new Timer("KAL Timer with : " + host);
        }
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                try {
                    socket.close();
                    isConnceted = false;
                    outStream.close();
                    timer.cancel();
                } catch (IOException ex) {
                   ex.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 1000 * 60);
    }
}
