package creational.factory;

public class SMSNotification implements Notification {
    private String message;

    SMSNotification(String msg){
        this.message=msg;
    }

    @Override
    public void sendNotification(){
        System.out.println("this is SMS service");
        System.out.println(message);
    }
}
