package baptiste.util;

import baptiste.bean.HttpRequest;
import baptiste.bean.HttpResponse;

import java.util.List;
import java.util.Scanner;

public class HttpResponseBuilder {
    public static HttpResponse makeHttpResponse(String serverResponse, List<byte[]> serverResponseList){
        if(serverResponse == null || serverResponse.isEmpty()){
            return null;
        }
        StringBuilder r = new StringBuilder();
        HttpResponse httpResponse = null;
        Scanner scan = new Scanner(serverResponse);
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
        httpResponse.setServerResponseList(serverResponseList);
        return httpResponse;
    }
}
