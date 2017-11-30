package baptiste;

import baptiste.bean.HttpRequest;
import baptiste.bean.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class Cache {

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
}
