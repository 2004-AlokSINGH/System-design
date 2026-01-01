package creational.factory;


public class NotificationSimpleFactory {

   

    // this is as per simple factory
    public static Notification createNotification(String type, String msg) {
        switch (type) {
            case "email":
                return new EmailNotification(msg);
            case "sms":
                return new SMSNotification(msg);
            default:
                throw new IllegalArgumentException("Unknown notification type: " + type);
        }
    }
}
