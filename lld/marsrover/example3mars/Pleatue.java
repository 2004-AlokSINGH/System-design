package marsrover.example3mars;

public class Pleatue {

    private final int x;
    private final int y;


    public Pleatue(int x,int y){
        this.x=x;
        this.y=y;
    }

    public boolean isValid(int x,int y){
        return x>=0 && x<this.x && y>=0 && y<this.y;
    }
    
}
