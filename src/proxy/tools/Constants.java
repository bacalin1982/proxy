package proxy.tools;

public final class Constants {
    /*
    * Constant messages, if needed the char to replace by string is the % character.
    * */
    // Title message
    public static final String I_TITLE = " -------------------------------------- \n"+
                                         "|          Proxy Cache Server          |\n"+
                                         "|               made  by               |\n"+
                                         "|          B. Calin & F. Jamar         |\n"+
                                         " -------------------------------------- ";
    // General messages
    public static final String _I = "INFO   : ";
    public static final String _E = "ERROR  : ";
    public static final String _S = "        - ";
    public static final String _W = "WARNING: ";
    public static final String OK = "[SUCCESS]";
    public static final String NOK = "[ERROR]";

    // Messages for information (I_)
    // Proxy server messages
    public static final String I_SERVER_START = "Starting Proxy ... ";
    public static final String I_SERVER_START_PORT = "Proxy cache server configured on port [%1]";
    public static final String I_SERVER_READY = "Proxy ready for connections.";
    // Distant web server messages
    public static final String I_WEB_SERVER_CON = "Connecting to %1 on port 80 ... ";
    public static final String I_WEB_SERVER_WAIT = "waiting for server 1s ... ";
    // Cache messages
    public static final String I_CACHE_START = "Initializing cache ... ";
    public static final String I_CACHE_CREATE_DIR = "Creating cache %1 ... ";
    public static final String I_CACHE_DIR_EXISTS = "Cache already exist %1";
    public static final String I_CACHE_LOAD_DATA = "Loading data cache ... ";
    public static final String I_CACHE_DATA_NFOUND = "No data found in cache.";
    public static final String I_CACHE_SAVE_REQ = "Save request to directory %1";
    public static final String I_CACHE_SAVE_RES = "Save response to directory %1";
    public static final String I_CACHE_DEL_DIR = "Deleting %1 ... ";

    // Messages for errors (E_)
    // Proxy server messages
    public static final String E_WRONG_ARG_NMB = "Proxy takes exactly 1 argument (%1 given). Use: Proxy <server_port>";
    public static final String E_WRONG_ARG_FMT = "Wrong argument format. Default port 8081 will be used.";
}
