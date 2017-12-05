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
    public static final String _S = "        -> ";
    public static final String _W = "WARNING: ";
    public static final String OK = "[SUCCESS]";
    public static final String NOK = "[ERROR]";
    public static final String YES = "[YES]";
    public static final String NO = "[NO]";

    // Messages for information (I_)
    // Proxy server messages
    public static final String SERVER_START = "Starting Proxy ... ";
    public static final String SERVER_START_PORT = "Proxy cache server configured on port [%1]";
    public static final String SERVER_READY = "Proxy ready for connections ... ";
    // Distant web server messages
    public static final String WEB_SERVER_CON = "Connecting to %1 on port 80 ... ";
    public static final String WEB_SERVER_WAIT = "waiting for server 1s ... ";
    // Cache messages
    public static final String CACHE_START = "Initializing cache ... ";
    public static final String CACHE_CREATE_DIR = "Creating cache %1 ... ";
    public static final String CACHE_ALR_EXISTS = "A cache is detected ...";
    public static final String CACHE_LOAD_DATA = "Loading data cache ... ";
    public static final String CACHE_DATA_NFOUND = "Cache is empty";
    public static final String CACHE_SAVE_REQ = "Save request to directory %1";
    public static final String CACHE_SAVE_RES = "Save response to directory %1";
    public static final String CACHE_DEL_DIR = "Deleting %1 ... ";
    public static final String CACHE_DIR_EXISTS = "Directory %1 already exists ... ";
    // Client messages
    public static final String CLIENT_NEW = "New client detected : %1";
    public static final String CLIENT_CLOSE = "Closing client : %1";
    public static final String CLIENT_RES_NO_CACHE = "Response for %1 does not exist in cache";
    // Http Response
    public static final String RESPONSE_CAN_BE_CACHED = "Checking if Response can be cached ... ";
    public static final String RESPONSE_IS_VALID = "Checking if Response is valid ... ";
    public static final String RESPONSE_CANNOT_BE_CACHED = "Response %1 cannot be cached";
    public static final String RESPONSE_IS_ALR_CACHED = "Response %1 already cached";

    // Messages for errors (E_)
    // Proxy server messages
    public static final String WRONG_ARG_NMB = "Proxy takes exactly 1 argument (%1 given). Use: Proxy <server_port>";
    public static final String WRONG_ARG_FMT = "Wrong argument format. Default port 8081 will be used.";
}
