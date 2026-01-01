package creational.factory;

public class EmailNotificationCreator extends NotificationCreatorFactoryMethod{

    @Override
    public Notification createNotif(String message){
        return new EmailNotification(message);
    }
    
}
