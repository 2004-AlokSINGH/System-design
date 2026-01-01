package creational.singleton;

public class DoubleCheckedSingleton {
    
    // It uses the volatile keyword
    // to ensure that changes to the 
    // instance variable are immediately 
    // visible to other  threads.

    private static volatile DoubleCheckedSingleton doubleCheckedSingletonObject;

    private DoubleCheckedSingleton(){}

    public static DoubleCheckedSingleton getSingletonObject(){
        //first check
        if(doubleCheckedSingletonObject==null){

// If the first check (instance == null) passes, we synchronize on the class object.
// We check the same condition one more time because multiple threads may have passed the first check.
// The instance is created only if both checks pass.
            
            // minimizes performance overhead from synchronization 
            // by only synchronizing when the object is first created.
            synchronized (DoubleCheckedSingleton.class){
                
                if(doubleCheckedSingletonObject == null){
                    doubleCheckedSingletonObject=new DoubleCheckedSingleton();
                }

            }
        }
        return doubleCheckedSingletonObject;
    }
}
