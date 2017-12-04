package proxy.bean;

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
        this.params = new HashMap<>();
    }

    public HttpRequest(String method, String url, String httpVersion){
        this();
        this.method         = method;
        this.url            = url;
        this.httpVersion    = httpVersion;
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
                "\t"+this.method + " " + this.url + " " + this.httpVersion + "\n" +
                "\tProperties:";
        for(String key: this.params.keySet()){
            request += "\n\t\t- "+key+":"+this.params.get(key);
        }

        return request;
    }

    public String getHost(){
        return params.get("Host");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpRequest)) return false;

        HttpRequest that = (HttpRequest) o;

        if (getMethod() != null ? !getMethod().equals(that.getMethod()) : that.getMethod() != null) return false;
        if (getUrl() != null ? !getUrl().equals(that.getUrl()) : that.getUrl() != null) return false;
        if (getHttpVersion() != null ? !getHttpVersion().equals(that.getHttpVersion()) : that.getHttpVersion() != null)
            return false;
        return getParams() != null ? getParams().equals(that.getParams()) : that.getParams() == null;
    }

    @Override
    public int hashCode() {
        int result = getMethod() != null ? getMethod().hashCode() : 0;
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        result = 31 * result + (getHttpVersion() != null ? getHttpVersion().hashCode() : 0);
        result = 31 * result + (getParams() != null ? getParams().hashCode() : 0);
        return result;
    }
}
