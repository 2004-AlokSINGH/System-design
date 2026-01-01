package creational.builder;

import java.util.Map;

public class HttpReqBuilder implements Builder {

    private String url;
    private String method;
    private Map<String, String> queryParams;
    private String body;

    public HttpReqBuilder() { }

    @Override
    public Builder buildUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public Builder buildMethod(String method) {
        this.method = method;
        return this;
    }

    @Override
    public Builder buildBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public Builder buildQueryParam(Map<String, String> params) {
        this.queryParams = params;
        return this;
    }

    @Override
    public HttpReq build() {
        // validate required fields
        if (this.url == null || this.url.trim().isEmpty()) {
            throw new IllegalStateException("url is required");
        }

        // default method if not set
        String m = (this.method == null) ? "GET" : this.method;

        return new HttpReq(this.url, m, this.queryParams, this.body);
    }
}