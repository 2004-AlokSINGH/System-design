package behavioral.iterator;

import java.util.ArrayList;
import java.util.List;

public class ConretePlaylist implements IterableCollection<String> {

    private final List<String> songs= new ArrayList<>();

    public void addSong(String song) {
        songs.add(song);
    }

    public String getSongAt(int index) {
        return songs.get(index);
    }

    public int getSize() {
        return songs.size();
    }

    @Override
    public Iteratorr<String> createIterator(){
        return new PlayListItertor(this);
    }


    
}
