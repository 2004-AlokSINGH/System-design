package creational.singleton;

public class ThreadSafeSingleton {

    
    private static ThreadSafeSingleton threadSafeSingletonObject;

    private ThreadSafeSingleton (){

    }

    //The synchronization keyword ensures that only 
    //one thread can perform the (instance == null) check and create the object.

    public static synchronized ThreadSafeSingleton getSingletonObject(){
        if (threadSafeSingletonObject==null){
            threadSafeSingletonObject=new ThreadSafeSingleton();
        }
        return threadSafeSingletonObject;
    }

    //Although this approach is straightforward, using synchronized can cause substantial overhead and reduce performance, which can be a bottleneck if called frequently.

    
}
