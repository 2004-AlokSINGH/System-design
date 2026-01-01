package creational.factory;

public class SMSNotificationCreator extends NotificationCreatorFactoryMethod{

    @Override
    public Notification createNotif(String message){
        return new SMSNotification(message);
    }
    
}
