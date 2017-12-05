package proxy;

import com.sun.tools.internal.jxc.ap.Const;
import proxy.bean.HttpRequest;
import proxy.bean.HttpResponse;
import proxy.tools.Constants;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private final static String DIRECTORY_DATA  = "responses";
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

    public void initialize(){
        try {
            System.out.println(Constants._I+Constants.I_CACHE_START);
            //create directory
            File dir = new File(this.DIRECTORY_DATA+"\\");
            if (!dir.exists()) {
                System.out.print(Constants._S+Constants.I_CACHE_CREATE_DIR.replace("%1", dir.getAbsolutePath()));
                if (dir.mkdir()) {
                    System.out.println(Constants.OK);
                } else {
                    System.out.println(Constants.NOK);
                    System.exit(0);
                }
            }else{
                System.out.println(Constants._S+Constants.I_CACHE_DIR_EXISTS.replace("%1", dir.getAbsolutePath()));
            }

            //Loading cache
            JAXBContext jaxbContext;
            Unmarshaller jaxbUnmarshaller;
            System.out.println(Constants._S+Constants.I_CACHE_LOAD_DATA);
            if(dir.listFiles().length == 0){
                System.out.println(Constants._S+Constants.I_CACHE_DATA_NFOUND);
            }else {
                for (File dirData : dir.listFiles()) {
                    System.out.print("- " + dirData.getAbsolutePath());
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
                        System.out.print(Constants.NOK);
                    } else {
                        System.out.print(Constants.OK);
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
            File dir = new File(DIRECTORY_DATA+"\\"+httpRequest.getHost()+"\\");
            if (dir.exists()) {
                System.out.println("Directory "+dir.getAbsolutePath()+" already exist");
                System.out.print("Delete "+dir.getAbsolutePath());
                if(dir.delete()){
                    System.out.println(" OK");
                }else {
                    System.out.println(" ERROR");
                }
            }
            System.out.print("Create directory " + dir.getAbsolutePath());
            if (dir.mkdir()) {
                System.out.println(" OK");
            } else {
                System.out.println(" ERROR");
                System.exit(0);
            }

            JAXBContext jaxbContext;
            Marshaller jaxbMarshaller;

            File requestFile = new File(dir.getAbsolutePath()+"\\"+FILE_REQUEST);
            jaxbContext = JAXBContext.newInstance(HttpRequest.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(httpRequest, requestFile);
            System.out.println("Save request to directory " + dir.getAbsolutePath());

            File responseFile = new File(dir.getAbsolutePath()+"\\"+FILE_RESPONSE);
            jaxbContext = JAXBContext.newInstance(HttpResponse.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(httpResponse, responseFile);
            System.out.println("Save response to directory " + dir.getAbsolutePath());

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
