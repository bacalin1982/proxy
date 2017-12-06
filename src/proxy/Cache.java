package proxy;

import proxy.bean.HttpRequest;
import proxy.bean.HttpResponse;
import proxy.tools.Constants;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;

public class Cache {

    private final static String DIRECTORY_DATA  = "cache";
    private final static String FILE_REQUEST    = "request.xml";
    private final static String FILE_RESPONSE   = "response.xml";

    private Map<HttpRequest, HttpResponse> responseList = new HashMap<>();

    private static Cache instance = null;
    private Cache(){}
    public static Cache getInstance(){
        if(instance == null){
            instance = new Cache();
        }
        return instance;
    }

    public Map<HttpRequest, HttpResponse> getResponseList() {
        return this.responseList;
    }

    public HttpResponse getResponseFromRequest(HttpRequest request){
        if(this.responseList.containsKey(request)){
            return this.responseList.get(request);
        }
        return null;
    }

    public void putResponse(HttpRequest httpRequest, HttpResponse httpResponse){
        this.responseList.put(httpRequest, httpResponse);
    }
    /*
    * [REQ_07] Cache/persistence part
    * If the requested web page is not available locally, the server shall forward the request
    * to the web server, get and decode the response, saving files locally and forward the response
    * to the client. This feature is in reality a cache system with persistence.
    * */
    public void initialize(){
        try {
            System.out.println(Constants._I+Constants.CACHE_START);
            //create directory
            File dir = new File(this.DIRECTORY_DATA+"/");
            if (!dir.exists()) {
                System.out.print(Constants._S+Constants.CACHE_CREATE_DIR.replace("%1", dir.getAbsolutePath()));
                if (dir.mkdir()) {
                    System.out.println(Constants.OK);
                } else {
                    System.out.println(Constants.NOK);
                    System.exit(0);
                }
            }else{
                System.out.println(Constants._I+Constants.CACHE_ALR_EXISTS);
            }

            //Loading cache
            JAXBContext jaxbContext;
            Unmarshaller jaxbUnmarshaller;
            System.out.println(Constants._S+Constants.CACHE_LOAD_DATA);
            if(dir.listFiles().length == 0){
                System.out.println(Constants._S+Constants.CACHE_DATA_NFOUND);
            }else {
                for (File dirData : dir.listFiles()) {
                    System.out.print(Constants._S+dirData.getAbsolutePath()+" ");
                    HttpRequest httpRequest = null;
                    HttpResponse httpResponse = null;
                    for (File data : dirData.listFiles()) {
                        if (data.getName().equals(FILE_REQUEST)) {
                            jaxbContext = JAXBContext.newInstance(HttpRequest.class);
                            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                            httpRequest = (HttpRequest) jaxbUnmarshaller.unmarshal(data);
                        }

                        if (data.getName().equals(FILE_RESPONSE)) {
                            jaxbContext = JAXBContext.newInstance(HttpResponse.class);
                            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                            httpResponse = (HttpResponse) jaxbUnmarshaller.unmarshal(data);
                        }
                    }
                    if (httpRequest == null || httpResponse == null) {
                        System.out.println(Constants.NOK);
                    } else {
                        System.out.println(Constants.OK);
                        putResponse(httpRequest, httpResponse);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveToFile(HttpRequest httpRequest, HttpResponse httpResponse){
        try{
            /*
            * For each request an MD5 print is calculated to have a response for each files in web page
            * MessageDigest is integrated to java.security classes and do the job.
            * Get MD5 print with a StringBuffer and its done.
            * */
            byte[] bytesRequest = httpRequest.toString().getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytesRequest = md.digest(bytesRequest);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < bytesRequest.length; i++)
                sb.append(Integer.toString((bytesRequest[i] & 0xff) + 0x100, 16).substring(1));

            File dir = new File(DIRECTORY_DATA+"/"+sb.toString()+"/");
            if (dir.exists()) {
                System.out.println(Constants._I+Constants.CACHE_DIR_EXISTS.replace("%1", dir.getAbsolutePath()));
                System.out.print(Constants._I+Constants.CACHE_DEL_DIR.replace("%1", dir.getAbsolutePath()));
                if(dir.delete()){
                    System.out.println(Constants.OK);
                }else {
                    System.out.println(Constants.NOK);
                }
            }
            System.out.print(Constants._I+Constants.CACHE_CREATE_DIR.replace("%1", dir.getAbsolutePath()));
            if (dir.mkdir()) {
                System.out.println(Constants.OK);
            } else {
                System.out.println(Constants.NOK);
                System.exit(0);
            }

            JAXBContext jaxbContext;
            Marshaller jaxbMarshaller;

            File requestFile = new File(dir.getAbsolutePath()+"/"+FILE_REQUEST);
            jaxbContext = JAXBContext.newInstance(HttpRequest.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(httpRequest, requestFile);
            System.out.println(Constants._I+Constants.CACHE_SAVE_REQ.replace("%1", dir.getAbsolutePath()));

            File responseFile = new File(dir.getAbsolutePath()+"/"+FILE_RESPONSE);
            jaxbContext = JAXBContext.newInstance(HttpResponse.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(httpResponse, responseFile);
            System.out.println(Constants._I+Constants.CACHE_SAVE_RES.replace("%1", dir.getAbsolutePath()));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
