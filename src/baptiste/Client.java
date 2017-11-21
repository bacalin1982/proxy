package baptiste;

import baptiste.bean.HttpRequest;
import baptiste.util.HttpRequestBuilder;

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
