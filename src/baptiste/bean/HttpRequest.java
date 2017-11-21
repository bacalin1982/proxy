package baptiste.bean;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String url;
    private String httpVersion;

    private Map<String, String> params;

    public HttpRequest(String method, String url, String httpVersion){
        this.method         = method;
        this.url            = url;
        this.httpVersion    = httpVersion;

        this.params = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void addParam(String key, String value){
        this.params.put(key, value);
    }

    public String getParam(String key){
        return this.params.get(key);
    }

    @Override
    public String toString() {
        String request = "Request:\n"+
                this.method + " " + this.url + " " + this.httpVersion + "\n" +
                "Properties:";
        for(String key: this.params.keySet()){
            request += "\n- "+key+":"+this.params.get(key);
        }

        return request;
    }
}
