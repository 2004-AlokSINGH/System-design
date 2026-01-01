package behavioral.iterator;


public class MusicPlayer {
    public static void main(String[] args) {
        ConretePlaylist playlist = new ConretePlaylist();
        playlist.addSong("Shape of You");
        playlist.addSong("Bohemian Rhapsody");
        playlist.addSong("Blinding Lights");

        Iteratorr<String> iterator = playlist.createIterator();

        System.out.println("Now Playing:");
        while (iterator.hasNext()) {
            System.out.println(" ---- " + iterator.next());
        }
    }
}