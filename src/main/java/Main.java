import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seroga on 03-Jan-16.
 */
public class Main {

    private static Translitor translitor = new Translitor();
    private static List<String> audioFilesNames = new ArrayList<>();

    public static void readFolderForFileList(String path) {

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(path))) {
            for (Path p : directoryStream) {
//                System.out.println(p.getFileName().toString());
//                System.out.println(p.toAbsolutePath().toString());
                String fileName = p.getFileName().toString();
                if (fileName.endsWith(".m3u8")) {
                    parsePlaylistFile(p);
                } else if (fileName.endsWith(".mp3") || fileName.endsWith(".ogg") || fileName.endsWith(".wav")) {
                    audioFilesNames.add(p.toAbsolutePath().toString());
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
//                System.out.println(line);
                parseLine(line);
//                System.out.println("TRANSLIT: " + translitText(line));

            }
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    private static void parseLine(String line) {
        line = line.trim();
        if (audioFilesNames.contains(line)) {
            int index = audioFilesNames.indexOf(line);
            String oldAudioFilePath = audioFilesNames.get(index);
            int lastSlashIndex = oldAudioFilePath.lastIndexOf('\\');
            String oldAudioFilePathCut = oldAudioFilePath.substring(0, lastSlashIndex);
            String oldAudioFileName = oldAudioFilePath.substring(lastSlashIndex);
            String transliteAudioFileName = translitText(oldAudioFileName);

            String newAudioFullPath = oldAudioFilePathCut + transliteAudioFileName;
            System.out.println("newAudioFullPath = " + newAudioFullPath);

            Path oldAudioFile = Paths.get(oldAudioFilePath);
        }

    }

    private static String translitText(String text) {
        return translitor.translit(text);

    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        String folderLoc = "D:/java/projects/playlist-creator/test-music";

//        readFolderForFileList(folderLoc);


    }
}
