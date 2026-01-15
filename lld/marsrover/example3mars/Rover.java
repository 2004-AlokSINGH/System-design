package marsrover.example3mars;

public class Rover {
    private Position position;

    public Rover(int x,int y, Direction direction){
        this.position=new Position(x, y, direction);
    }

    public Position getPosition(){
        return this.position;
    }



    

    
}
