package baptiste.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class HttpResponse {

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
        String response = "Response:\n"+
                "\tProperties:";
        for(String key: this.params.keySet()){
            response += "\n\t\t- "+key+":"+this.params.get(key);
        }
        response += "\n\tResponse:";
        for(byte[] serverResponse: this.serverResponseList){
            response += "\n\t\t"+serverResponse;
        }
        return response;
    }
}
