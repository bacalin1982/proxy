package baptiste;

import baptiste.bean.HttpRequest;
import baptiste.util.HttpRequestBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.Socket;

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

                //request
                DataInputStream dataInputStream = new DataInputStream(this.clientSocket.getInputStream());

                //read request
                String request = "";
                byte[] line = new byte[dataInputStream.available()];
                while(dataInputStream.available() > 0){
                    dataInputStream.read(line);
                    request += new String(line);
                }

                //make request
                if(!request.isEmpty()){
                    HttpRequest httpRequest = HttpRequestBuilder.makeHttpRequest(request);
                    System.out.println(httpRequest.toString());

                    File file = new File(".\\file.xml");
                    JAXBContext jaxbContext = JAXBContext.newInstance(HttpRequest.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                    jaxbMarshaller.marshal(httpRequest, file);
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
                    DataOutputStream dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());


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
