package behavioral.iterator;


public class PlayListItertor implements Iteratorr<String>{

    private final ConretePlaylist playlist;
    private int idx=0;

    public PlayListItertor(ConretePlaylist playlist){
        this.playlist=playlist;
    }

    @Override
    public boolean hasNext() {
        return this.idx<playlist.getSize();
    }

    @Override
    public String next() {
       return  playlist.getSongAt(idx++);
       
    }
    

     



}
