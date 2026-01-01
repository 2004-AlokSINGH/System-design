package structural.proxy;

public class ImageProxy implements Image {

    private String fileName;
    private HighResolutionImage realImage;

    public ImageProxy(String fileName){
        this.fileName=fileName;
    }

    @Override
    public void display() {
       if (realImage == null) {
            System.out.println("ImageProxy: display() requested for " + fileName + ". Loading high-resolution image...");
            realImage = new HighResolutionImage(fileName);
        } else {
            System.out.println("ImageProxy: Using cached high-resolution image for " + fileName);
        }

        // Delegate the display call to the real image
        realImage.display();
    }

    @Override
    public String getFileName() {
       return this.fileName;
    }
    }

    
    

