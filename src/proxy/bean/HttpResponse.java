package proxy.bean;

import proxy.tools.Constants;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@XmlRootElement
public class HttpResponse {

    private static final String CACHE_CONTROL   = "Cache-Control";
    private static final String MAX_AGE         = "max-age";
    private static final String EXPIRES         = "Expires";
    private static final String DATE            = "Date";

    private String httpVersion;
    private String statusCode;
    private String statusValue;

    private Map<String, String> params;

    private List<byte[]> serverResponseList;

    public HttpResponse(){
        this.params = new HashMap<>();
    }
    public HttpResponse(String httpVersion, String statusCode, String statusValue){
        this();
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.statusValue = statusValue;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    @XmlElement
    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getStatusCode() {
        return statusCode;
    }

    @XmlElement
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusValue() {
        return statusValue;
    }

    @XmlElement
    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public void addParam(String key, String value){
        this.params.put(key, value);
    }

    public String getParam(String key){
        return this.params.get(key);
    }

    @XmlElement
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public List<byte[]> getServerResponseList() {
        return serverResponseList;
    }

    @XmlElement
    public void setServerResponseList(List<byte[]> serverResponseList) {
        this.serverResponseList = serverResponseList;
    }

    @Override
    public String toString() {
        String response = Constants._S+"Response:\n"+
                "\t\t\tProperties:";
        for(String key: this.params.keySet()){
            response += "\n\t\t\t\t- "+key+":"+this.params.get(key);
        }
        //response += "\n\t\t\tResponse:";
        //for(byte[] serverResponse: this.serverResponseList){
        //   response += "\n\t\t\t\t"+serverResponse;
        //}
        return response;
    }

    public boolean isPutInCache(){
        System.out.print(Constants._I+Constants.RESPONSE_CAN_BE_CACHED);
        //Check property Cache-Control
        String cacheControl = getParam(CACHE_CONTROL);
        if(cacheControl == null || cacheControl.indexOf("no-cache") == -1){
            System.out.println(Constants.YES);
            System.out.println(Constants._S+CACHE_CONTROL+"="+cacheControl);
            return true;
        }else{
            System.out.println(Constants.NO);
        }
        System.out.println(Constants._S+CACHE_CONTROL+"="+cacheControl);
        return false;
    }

    public boolean isValid(HttpRequest httpRequest){
        System.out.print(Constants._I+Constants.RESPONSE_IS_VALID);
        //Check property Cache-Control
        String cacheControl = getParam(CACHE_CONTROL);

        //max age
        int maxAge = -1;
        if(cacheControl != null) {
            String[] cacheControlSplit = cacheControl.split(",");
            for (int i = 0; i < cacheControlSplit.length; i++) {
                String[] cacheControleVal = cacheControlSplit[i].split("=");
                if (cacheControleVal.length == 2 && MAX_AGE.equals(cacheControleVal[0])) {
                    maxAge = Integer.parseInt(cacheControleVal[1]);
                }
            }
        }

        //expire
        ZonedDateTime expiresDate = null;
        if(getParam(EXPIRES) != null) {
            expiresDate =  ZonedDateTime.parse(getParam(EXPIRES), DateTimeFormatter.RFC_1123_DATE_TIME);
        }

        //request date
        ZonedDateTime requestDate = null;
        if(getParam(DATE) != null) {
            requestDate = ZonedDateTime.parse(getParam(DATE), DateTimeFormatter.RFC_1123_DATE_TIME);
        }

        Date dToCompare = null;
        if(maxAge > -1) {
            requestDate = requestDate.plusSeconds(maxAge);
            dToCompare = Date.from(requestDate.toInstant());
        }else if(expiresDate != null) {
            dToCompare = Date.from(expiresDate.toInstant());
        }

        Date now = new Date();
        if(dToCompare != null && now.after(dToCompare)) {
            System.out.println(Constants.NO);
            System.out.println(Constants._S+MAX_AGE+"="+maxAge+",");
            System.out.println(Constants._S+EXPIRES + "=" + expiresDate + ",");
            System.out.println(Constants._S+DATE+"="+requestDate);
            System.out.println(Constants._S+CACHE_CONTROL + "=" + cacheControl);
            return false;
        }
        System.out.println(Constants.YES);
        System.out.println(Constants._S+MAX_AGE+"="+maxAge+",");
        System.out.println(Constants._S+EXPIRES + "=" + expiresDate + ",");
        System.out.println(Constants._S+DATE+"="+requestDate);
        System.out.println(Constants._S+CACHE_CONTROL+"="+cacheControl);
        return true;
    }

}
