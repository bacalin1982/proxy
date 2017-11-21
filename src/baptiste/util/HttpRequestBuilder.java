package baptiste.util;

import baptiste.bean.HttpRequest;

import java.util.Scanner;

public class HttpRequestBuilder {
    public static HttpRequest makeHttpRequest(String request){
        if(request == null || request.isEmpty()){
            return null;
        }
        HttpRequest httpRequest = null;
        Scanner scan = new Scanner(request);
        while(scan.hasNext()){
            if(httpRequest == null){//first line
                String[] splitLine  = scan.nextLine().split(" ");
                String method       = splitLine[0];
                String url          = splitLine[1];
                String httpVersion  = splitLine[2];
                httpRequest = new HttpRequest(method, url, httpVersion);
            }else{
                String[] splitLine  = scan.nextLine().split(":");
                httpRequest.addParam(splitLine[0], splitLine[1]);
            }
        }
        return httpRequest;
    }
}
