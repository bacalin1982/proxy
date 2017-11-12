/*
* Authors:
* Baptiste Calin
* Jamar Frédéric
* */

public class proxy {
    public static void main(String[] arg){
        //Initializing proxy server instance
        Server proxy = Server.getInstance();
        //Displaying configuration of proxy server
        if (proxy!=null) System.out.println("Server correctly initialized (@"+proxy.getAddress()+":"+proxy.getPort()+")");
        try {
            /*
            * To handle multiple connections from client, a thread function was added.
            * Loop created to catch any client connection with the proxy openServer function
            * Each time a client is connected or left, a text message shall be displayed in console
            * */
            while (true){
                try{
                    proxy.openServer();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            /*
            * If application is correctly closed, the proxy server is closed and a text message shall be displayed in console
            * */
            if(proxy.closeServer()) System.out.println("Server correctly closed (@"+proxy.getAddress()+":"+proxy.getPort()+")");
        }
    }
}