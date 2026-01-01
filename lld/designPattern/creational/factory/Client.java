package creational.factory;

public class Client {
    
    public static void main(String[] args) {
        // NotificationService notificationService=new NotificationService();
        // notificationService.notifyUser();


        // HERE we are creating object of abstract class
        NotificationCreatorFactoryMethod creator;


        creator= new EmailNotificationCreator();
        creator.send("this is my message service");


        creator=new SMSNotificationCreator();
        creator.send("this is ur OTP");


        

    }
}
