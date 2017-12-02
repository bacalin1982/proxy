package baptiste;

public class Proxy {

    public static void main(String[] arg){
        //Default port set to 8081
        int port = 8081;
        //If a port was given by arg, replace default one.
        if(arg.length == 1){
            try{
                port = Integer.parseInt(arg[0]);
            }catch (NumberFormatException ex){
                System.out.println("Wrong argument format. Default port 8081 will be used");
            }
        }

        //Initialize cache
        Cache.getInstance().initialize();

        //Launching proxy cache server
        System.out.println("\n");
        System.out.println("Proxy cache server on port["+port+"]");
        new Thread(new Server(port)).start();
    }
}
