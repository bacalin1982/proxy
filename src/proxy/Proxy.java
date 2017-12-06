package proxy;

import proxy.tools.Constants;

public class Proxy {

    public static void main(String[] args){
        System.out.println(Constants.I_TITLE);
        int port = 8080;
        String dir = "cache/";
        /*
        [REQ_002]
        The user shall be able to configure the listening port of the proxy cache server.
        --
        A default 8081 port is set. In case of a wrong arg given, even if it is a string or something else,
        the default port will be used by the proxy cache server.
        If the first arg given is a correct integer, the proxy cache will take it into account.
        --
        */

        for(int i=0;i<args.length;i++) {
            String param = args[i];
            switch (param.trim()) {
                //param port
                case "-p":
                    //get next arg for port
                    try {
                        String tmpPort = args[i + 1];
                        port = Integer.parseInt(tmpPort);
                    } catch (NumberFormatException e) {
                        System.out.println(Constants._I + Constants.WRONG_PORT_ARG.replace("%1", Integer.toString(port)));
                    }
                    i++;
                    break;

                //param directory
                case "-d":
                    //get next arg for dir
                    try {
                        String tmpDir = args[i + 1];
                        if (!tmpDir.endsWith("/")) {
                            tmpDir += "/";
                        }
                        dir = tmpDir;
                    } catch (NumberFormatException e) {
                        System.out.println(Constants._I + Constants.WRONG_DIR_ARG.replace("%1", dir));
                    }
                    i++;
                    break;

                //inc. i
                default:
                    i++;
            }
        }

        // Initialize cache instance
        Cache.getInstance().initialize(dir);
        System.out.println(Constants._I+Constants.CACHE_START.replace("%1", dir)+Constants.OK);

        // Launching proxy cache server
        System.out.println(Constants._I+Constants.SERVER_START_PORT.replace("%1", Integer.toString(port)));
        new Thread(new Server(port)).start();
    }
}
