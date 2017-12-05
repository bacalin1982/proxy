package proxy;

import proxy.tools.Constants;

public class Proxy {

    public static void main(String[] arg){
        System.out.println(Constants.I_TITLE);
        int port = 8081;
        /*
        [REQ_002]
        The user shall be able to configure the listening port of the proxy cache server.
        --
        A default 8081 port is set. In case of a wrong arg given, even if it is a string or something else,
        the default port will be used by the proxy cache server.
        If the first arg given is a correct integer, the proxy cache will take it into account.
        --
        */
        if(arg.length == 1){
            // Trying to convert first arg to a port.
            try{
                port = Integer.parseInt(arg[0]);
            }catch (NumberFormatException ex){
                System.out.println(Constants._E+Constants.WRONG_ARG_FMT);
            }

            // Initialize cache instance
            Cache.getInstance().initialize();
            System.out.println(Constants._I+Constants.CACHE_START+Constants.OK);

            // Launching proxy cache server
            System.out.println(Constants._I+Constants.SERVER_START_PORT.replace("%1", Integer.toString(port)));
            new Thread(new Server(port)).start();
        }
        else{
            System.out.println(Constants._E+Constants.WRONG_ARG_NMB.replace("%1", Integer.toString(arg.length)));
        }


    }
}
