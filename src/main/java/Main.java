import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by seroga on 03-Jan-16.
 */
public class Main {


    public static void readFolderForFileList(String path) {

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(path))) {
            for (Path p : directoryStream) {
                System.out.println(p.getFileName().toString());
                String fileName = p.getFileName().toString();
                if (fileName.endsWith(".m3u8")) {
                    parsePlaylistFile(p);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private static void parsePlaylistFile(Path playlistFile) {
        try (InputStream in = Files.newInputStream(playlistFile);
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                System.out.println("TRANSLIT: " + translitText(line));
            }
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    private static String translitText(String text) {
        return Translitor.translit(text);

    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        String folderLoc = "D:/java/projects/playlist-creator/test-music";

        readFolderForFileList(folderLoc);



    }
}
