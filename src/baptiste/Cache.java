package baptiste;

import baptiste.bean.HttpRequest;
import baptiste.bean.HttpResponse;

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
        return responseList;
    }

    public HttpResponse getResponseFromRequest(HttpRequest request){
        if(responseList.containsKey(request)){
            return responseList.get(request);
        }
        return null;
    }

    public void putResponse(HttpRequest httpRequest, HttpResponse httpResponse){
        responseList.put(httpRequest, httpResponse);
    }

    public void initialize(){
        try {
            System.out.println("Initialize cache ...");

            //create directory
            File dir = new File(DIRECTORY_DATA+"\\");
            if (!dir.exists()) {
                System.out.print("- Create directory " + dir.getAbsolutePath());
                if (dir.mkdir()) {
                    System.out.println(" OK");
                } else {
                    System.out.println(" ERROR");
                    System.exit(0);
                }
            }else{
                System.out.println("- Directory "+dir.getAbsolutePath()+" already exist");
            }

            //Loading cache
            JAXBContext jaxbContext;
            Unmarshaller jaxbUnmarshaller;
            System.out.println("Loading data cache...");
            if(dir.listFiles().length == 0){
                System.out.println("- No data found in cache");
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
                        System.out.print(" ERROR");
                    } else {
                        System.out.print(" OK");
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
