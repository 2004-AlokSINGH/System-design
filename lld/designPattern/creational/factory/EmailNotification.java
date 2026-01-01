package creational.factory;

public class EmailNotification implements Notification {

    private String message;

    EmailNotification(String msg){
        this.message=msg;
    }

    @Override
    public void sendNotification(){
        System.out.println("this is emaail service");
        System.out.println(message);
    }
    
}
