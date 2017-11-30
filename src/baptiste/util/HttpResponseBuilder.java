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
        return httpResponse;
    }
}
