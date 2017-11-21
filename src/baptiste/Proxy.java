package baptiste;


public class Proxy {

    public static void main(String[] arg){
        int port = 8081;

        System.out.println("Proxy cache server on port["+port+"]");
        new Thread(new Server(port)).start();
    }
}
