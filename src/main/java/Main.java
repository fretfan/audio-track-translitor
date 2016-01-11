/**
 * Created by seroga on 03-Jan-16.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");

        String folderLoc = "D:/java/projects/playlist-creator/test-music -backup";
        FolderReader folderReader = new FolderReader(folderLoc);
        PlayListParser playListParser = new PlayListParser(folderReader);
        playListParser.parsePlaylistFile();

    }
}
