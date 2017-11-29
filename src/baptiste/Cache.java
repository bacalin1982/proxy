package baptiste;

import baptiste.bean.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private Map<HttpRequest, String> requestList = new HashMap<>();

    private static Cache instance = null;
    private Cache(){}
    public static Cache getInstance(){
        if(instance == null){
            instance = new Cache();
        }
        return instance;
    }

    public Map<HttpRequest, String> getRequestList() {
        return requestList;
    }

    public String getRequest(HttpRequest request){
        if(requestList.containsKey(request)){

        }
    }
}
