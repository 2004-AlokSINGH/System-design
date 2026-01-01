package creational.factory;

public class NotificationService {

    public void notifyUser() {
         
        Notification notification = NotificationSimpleFactory.createNotification("email", "this service is up");
        notification.sendNotification();
    }

    
}