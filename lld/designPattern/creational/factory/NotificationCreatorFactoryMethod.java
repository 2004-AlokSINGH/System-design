package creational.factory;

public abstract class NotificationCreatorFactoryMethod {
    
    //abstract method to credate notification
    public abstract Notification createNotif(String message);

    public void send(String message){
        Notification notification=createNotif(message);
        notification.sendNotification();
    }


}
