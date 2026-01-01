package creational.builder;

import java.util.Map;

public class Client {
        public static void main(String[] args) {
            
            HttpReq req = new HttpReqBuilder()
                      .buildUrl("https://api.example.com")
                      .buildMethod("POST")
                      .buildBody("{\"name\":\"Alokkk\"}")
                      .buildQueryParam(Map.of("q","1"))
                      .build();
            System.out.println(req);



            // Example usage (place in your client code)
            HttpReq2 req2 = HttpReq2.builder()
                .url("https://api.example.com/resource")
                .method("POST")
                .body("{\"name\":\"Alokk\"}")
                .queryParams(Map.of("q","1"))
                .build();

            System.out.println(req2);

        }

        
    
}
