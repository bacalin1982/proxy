package baptiste.bean;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private String httpVersion;
    private String statusCode;
    private String statusValue;

    private Map<String, String> params;

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

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void addParam(String key, String value){
        this.params.put(key, value);
    }

    public String getParam(String key){
        return this.params.get(key);
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        String request = "Response:\n"+
                "\tProperties:";
        for(String key: this.params.keySet()){
            request += "\n\t\t- "+key+":"+this.params.get(key);
        }

        return request;
    }
}
