import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by seroga on 10-Jan-16.
 */
public class PlayListParser {

    //TODO test for readFolder method. Check if returns list of paths to files
    // TODO PlayList file parser. If no .m3u8 file - error.
//    if .m3u8 found. Ignore lines beginning with #M3U8.
//    Start creating new Playlist file (replace old filenames with new line by line.
//    Find lastIndex of SLASH. Substring by path / filename.mp3.
//    Translit filename.mp3. Join strings.
//    Move old file to new file.

    private FolderReader folderReader;

    public PlayListParser(FolderReader folderReader) {
        this.folderReader = folderReader;
    }

    private void parsePlaylistFile(Path playlistFile) {
        try (InputStream in = Files.newInputStream(playlistFile);
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//                parseLine(line);
//                System.out.println("TRANSLIT: " + translitText(line));

            }
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}
