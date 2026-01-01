package creational.builder;

import java.util.Map;

public interface Builder {

    Builder buildUrl(String url);
    Builder buildMethod(String method);
    Builder buildBody(String body);
    Builder buildQueryParam(Map<String, String> params);
    HttpReq build();
}

