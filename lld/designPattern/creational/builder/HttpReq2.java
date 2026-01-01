package creational.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpReq2 {

    private final String url;
    private final String method;
    private final Map<String, String> queryParams;
    private final String body;

    // private constructor: only the nested Builder can create instances
    private HttpReq2(String url, String method, Map<String, String> queryParams, String body) {
        this.url = url;
        this.method = method;
        this.queryParams = queryParams;
        this.body = body;
    }

    // convenience factory to obtain a new Builder
    public static Builder builder() {
        return new Builder();
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

    // static nested Builder for stricter encapsulation
    public static class Builder {
        private String url;
        private String method;
        private Map<String, String> queryParams;
        private String body;

        public Builder() { }

        // fluent API — choose names you prefer (url(), method(), body(), queryParams())
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder queryParams(Map<String, String> params) {
            this.queryParams = params;
            return this;
        }

        public HttpReq2 build() {
            if (this.url == null || this.url.trim().isEmpty()) {
                throw new IllegalStateException("url is required");
            }
            String m = (this.method == null) ? "GET" : this.method;

            Map<String, String> qp = (this.queryParams == null)
                    ? Collections.emptyMap()
                    : Collections.unmodifiableMap(new HashMap<>(this.queryParams));

            return new HttpReq2(this.url, m, qp, this.body);


        }
    }
}