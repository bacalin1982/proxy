package baptiste.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class HttpRequest {

    private String method;
    private String url;
    private String httpVersion;

    private Map<String, String> params;

    public HttpRequest(){
    }

    public HttpRequest(String method, String url, String httpVersion){
        this.method         = method;
        this.url            = url;
        this.httpVersion    = httpVersion;

        this.params = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    @XmlElement
    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    @XmlElement
    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    @XmlElement
    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
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

    @XmlElement
    public void setParams(Map<String, String> params) {
        this.params = params;
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
