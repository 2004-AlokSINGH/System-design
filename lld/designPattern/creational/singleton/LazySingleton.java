package creational.singleton;
 // create one and only instance and use it when required.


public class LazySingleton {

    // why is it static?
    // Ans=
    private static LazySingleton lazyIntializationObject;

    private LazySingleton(){
        // can be use to set any ppt
        // private so no object created
    }

    public static LazySingleton getSingleObject(){
        if (lazyIntializationObject == null){
            lazyIntializationObject=new LazySingleton();
        }
        return lazyIntializationObject;

        // This implementation is not thread-safe. If multiple threads call getInstance() simultaneously when instance is null, it's possible to create multiple instances.
    }
    
}
