
The Builder Design Pattern is a **creational** pattern that lets you **construct complex objects** step-by-step, separating the construction logic from the final representation.

Required when- An object requires **many optional fields**, and not all of them are needed every time.

We can do that by creating-
```
1. Multiple overloaded constructor.

problem- multiple constrcutor required.
```

```
2. Can create setter and getter for optional field
problem - giving access to set anything and anywhere may be in between of object creation or also one can override object data using setter
```

Also - As the number of fields grows, this approach becomes hard to manage, error-prone, and violates the Single Responsibility Principle — mixing construction logic with business logic.


Solution - **separate builder class that handles the object creation process.** And keeping the final object immutable, consistent, and easy to create.


Imagine you're building a system that needs to configure and create HTTP requests. Each HttpRequest can contain a mix of required and optional fields depending on the use case.

Here’s what a typical HTTP request might include (6 fields — 1 required, 1 defaulted, 4 optional):

1. URL — required (count: 1)  
2. HTTP Method — defaults to GET (count: 1)  
3. Headers — optional (count: 0..n key-value pairs)  
4. Query Parameters — optional (count: 0..n key-value pairs)  
5. Request Body — optional (count: 0..1; typically used with POST/PUT)  
6. Timeout — optional (count: 1; default: 30 seconds)



Solution 1 — Telescoping constructors (example)

```java
import java.util.Map;
import java.util.HashMap;

class HttpRequestTelescoping {
    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private String body;
    private int timeout;

    public HttpRequestTelescoping(String url) {
        this(url, "GET");
    }

    public HttpRequestTelescoping(String url, String method) {
        this(url, method, null);
    }

    public HttpRequestTelescoping(String url, String method, Map<String, String> headers) {
        this(url, method, headers, null);
    }

    public HttpRequestTelescoping(String url, String method, Map<String, String> headers,
                                  Map<String, String> queryParams) {
        this(url, method, headers, queryParams, null);
    }

    public HttpRequestTelescoping(String url, String method, Map<String, String> headers,
                                  Map<String, String> queryParams, String body) {
        this(url, method, headers, queryParams, body, 30000);
    }

    public HttpRequestTelescoping(String url, String method, Map<String, String> headers,
                                  Map<String, String> queryParams, String body, int timeout) {
        this.url = url;
        this.method = method;
        this.headers = headers == null ? new HashMap<>() : headers;
        this.queryParams = queryParams == null ? new HashMap<>() : queryParams;
        this.body = body;
        this.timeout = timeout;

        System.out.println("HttpRequest Created: URL=" + url +
            ", Method=" + method +
            ", Headers=" + this.headers.size() +
            ", Params=" + this.queryParams.size() +
            ", Body=" + (body != null) +
            ", Timeout=" + timeout);
    }

    // Getters could be added here
}


```

This shows the telescoping-constructor approach — it works but becomes hard to manage as optional parameters grow, motivating the Builder pattern.
''
Wrong with This Approach?

1. Hard to Read and Write
Multiple parameters of the same type (e.g., String, Map) make it easy to accidentally swap arguments
2. Error-Prone Client need to pass null if want ignore
3. Inflexible and Fragile
If you want to set parameter 5 but not 3 and 4, you’re forced to pass null for 3 and 4.
4.  Poor Scalability
Adding a new optional parameter requires adding or changing constructors,



In the Builder Pattern:

The construction logic is encapsulated in a Builder.
The final object (the "Product") is created by calling a build() method.
The object itself typically has a private or package-private constructor, forcing construction through the builder.






