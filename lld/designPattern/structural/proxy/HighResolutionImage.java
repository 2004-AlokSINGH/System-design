package structural.proxy;

public class HighResolutionImage implements Image {

    private String image;

    public HighResolutionImage(String imge){
        this.image=imge;
    }
    @Override
    public void display() {
        System.out.println("running high resolution process");
        System.out.println("enhancing image");
        System.out.println("image is good...  "+this.getFileName());

    }

    @Override
    public String getFileName() {
        System.out.println("actual image "+this.image);
        return "image is looking good.";
    }

}
