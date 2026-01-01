package creational.builder;

import java.util.Map;

public class HttpReq {

    private final String url;
    private final String method;
    private final Map<String, String> queryParams;
    private final String body;

    // package-private constructor called by the builder

    //It's package-private (no modifier) so only classes in the same package (here: builder) can call it.
    // Purpose: let your HttpReqBuilder create instances while preventing callers in other packages 
    // from instantiating HttpReq directly.
    //  That enforces use of the builder, centralizes validation/setting of defaults, and 
    //  keeps HttpReq immutable and its constructor surface small.
    
    // If you want stricter encapsulation, make the constructor private and 
    // use a static nested Builder inside HttpReq 
    // (or provide a public static factory method that delegates to the builder).

    HttpReq(String url, String method, Map<String, String> queryParams, String body) {
        this.url = url;
        this.method = method;
        this.queryParams = queryParams;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpReq{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", queryParams=" + queryParams +
                ", body='" + body + '\'' +
                '}';
    }
}