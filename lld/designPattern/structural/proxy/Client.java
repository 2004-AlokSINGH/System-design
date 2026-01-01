package structural.proxy;

public class Client {

    public static void main(String[] args) {
        Image img=new ImageProxy("img_01.jpg");
        img.display();
    }
    
}
