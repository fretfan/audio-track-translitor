/**
 * Created by seroga on 03-Jan-16.
 */
public class Main {

    public static void main(String[] args) {
        // TODO Test for PlayListParser.
        // Create some .mp3 + playlist before test (move creation of stuff into TestHelper class???)
        // TODO rename parsePlayListFile into execute???
        // TODO Add UI like did in thesis. Option 1 - Translit, 2 Index, 3 Unindex, 4 Translit + index???
        System.out.println("Hello world!");

        String folderLoc = "D:/java/projects/playlist-creator/test-music -backup";
        FolderReader folderReader = new FolderReader(folderLoc);
        Indexer indexer = new Indexer();
        PlayListParser playListParser = new PlayListParser(folderReader, indexer);
        playListParser.parsePlaylistFile();

    }
}
