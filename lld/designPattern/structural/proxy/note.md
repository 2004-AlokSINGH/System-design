# Proxy Design Pattern — Notes

## Overview
The Proxy pattern provides a surrogate or placeholder for another object to control access to it. Clients use the proxy through the same interface as the real object, so the proxy can defer expensive operations, add behavior (logging, caching, access control), or handle remote communication without changing client code.

## Intent / What it solves
- Defer expensive initialization (lazy loading).
- Add cross-cutting concerns (logging, authentication, caching) without modifying the real subject.
- Control or restrict access to the real subject.
- Keep client-side code unchanged while changing access semantics.

## Key participants
- Subject: common interface implemented by RealSubject and Proxy (e.g., display()).
- RealSubject: the real, potentially expensive object.
- Proxy: implements Subject, holds a reference to RealSubject, and controls access/behavior.

## Typical proxy variants
- Virtual Proxy: lazy initialization of RealSubject.
- Protection Proxy: enforces access control.
- Remote Proxy: handles communication with remote objects.
- Caching Proxy: stores results to avoid repeated expensive calls.
- Smart Proxy: performs housekeeping (e.g., logging, reference counting).

## Example: Image loading (eager vs. proxy)

1) Interface
```java
interface Image {
    void display();
    String getFileName();
}
```

2) Real subject (expensive on construction)
```java
class HighResolutionImage implements Image {
    private String fileName;
    private byte[] imageData;

    public HighResolutionImage(String fileName) {
        this.fileName = fileName;
        loadImageFromDisk(); // Expensive operation
    }

    private void loadImageFromDisk() {
        System.out.println("Loading image: " + fileName);
        try {
            Thread.sleep(2000); // simulate I/O
            this.imageData = new byte[10 * 1024 * 1024];
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void display() {
        System.out.println("Displaying image: " + fileName);
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
```

3) Proxy (virtual proxy + logging)
```java
class ImageProxy implements Image {
    private String fileName;
    private HighResolutionImage realImage;

    public ImageProxy(String fileName) {
        this.fileName = fileName;
    }

    private synchronized void ensureLoaded() {
        if (realImage == null) {
            System.out.println("Proxy: loading real image now for " + fileName);
            realImage = new HighResolutionImage(fileName);
        }
    }

    @Override
    public void display() {
        ensureLoaded();         // lazy load
        System.out.println("Proxy: logging display action for " + fileName);
        realImage.display();
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
```

4) Client usage (unchanged)
```java
Image image = new ImageProxy("photo.jpg");
image.getFileName(); // cheap
image.display();     // real image loaded and displayed here
```

## Best practices / When to use
- Use when constructing the real object is expensive and you want to delay it.
- Use to add authorization, logging, or caching without touching RealSubject.
- Keep proxy logic simple; complex behavior may belong to separate decorators or services.

## Summary (condensed)
The Proxy pattern provides a stand-in for another object to control creation, access, and additional behavior (lazy loading, auth, logging, caching, remoting) while preserving the original interface so clients remain unchanged.


//
This is the perfect use case for a Virtual Proxy — a proxy that stands in for a costly object and defers its creation until absolutely necessary.

By introducing just one small class (ImageProxy), we improved performance, saved memory, and kept our design clean, extensible, and easy to maintain — all without touching the original image class or altering client logic.

```
Adding a Protection Proxy
A Protection Proxy controls access to sensitive operations based on authorization rules. For example, only users with an ADMIN role should be able to view confidential images.

We can extend the display() method in ImageProxy to check access before loading or displaying the image.
```


```java
private boolean checkAccess(String userRole) {
    System.out.println("ProtectionProxy: Checking access for role: " + userRole + " on file: " + fileName);
    // Simulate a basic access control rule
    return "ADMIN".equals(userRole) || !fileName.contains("secret");
}

public void display(String userRole) {
    if (!checkAccess(userRole)) {
        System.out.println("ProtectionProxy: Access denied for " + fileName);
        return;
    }

    if (realImage == null) {
        System.out.println("ImageProxy: Loading image for authorized access...");
        realImage = new HighResolutionImage(fileName);
    }
    
    realImage.display();
}
```