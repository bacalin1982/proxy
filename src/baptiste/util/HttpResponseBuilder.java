package baptiste.util;

import baptiste.bean.HttpRequest;
import baptiste.bean.HttpResponse;

import java.util.Scanner;

public class HttpResponseBuilder {
    public static HttpResponse makeHttpResponse(String response){
        if(response == null || response.isEmpty()){
            return null;
        }
        StringBuilder r = new StringBuilder();
        HttpResponse httpResponse = null;
        Scanner scan = new Scanner(response);
        while(scan.hasNext()){
            if(httpResponse == null){//first line
                String[] splitLine  = scan.nextLine().split(" ");
                String httpVersion  = splitLine[0];
                String statusCode   = splitLine[1];
                String statusValue  = splitLine[2];
                httpResponse = new HttpResponse(httpVersion, statusCode, statusValue);
            }else{
                String[] splitLine  = scan.nextLine().split(": ");
                if(splitLine.length < 2){
                    if(!splitLine.equals("")) {
                        r.append(splitLine[0]);
                    }
                    continue;
                }
                httpResponse.addParam(splitLine[0], splitLine[1]);
            }
        }
        httpResponse.addParam("Response", r.toString());
        httpResponse.setResponse(response);
        return httpResponse;
    }

    public static byte[] reponseToByte(HttpResponse httpResponse){

        StringBuilder r = new StringBuilder();
        r.append(httpResponse.getHttpVersion()).append(" ");
        r.append(httpResponse.getStatusCode()).append(" ");
        r.append(httpResponse.getStatusValue()).append(" ").append("\r\n");
        for(String key: httpResponse.getParams().keySet()){
            if(key.equals("response")){
                continue;
            }
            r.append(key).append(": ").append(httpResponse.getParam(key)).append("\r\n");
        }
        r.append("\r\n");
        r.append(httpResponse.getParams().get("response"));
        return r.toString().getBytes();

    }
}
