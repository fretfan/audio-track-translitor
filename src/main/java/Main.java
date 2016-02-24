import com.mpatric.mp3agic.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {

    public static void main(String[] args) throws IOException {
        // TODO Add UI like did in thesis.
        // Option 1 - Translit, 2 Index, 3 Unindex, 4 Translit + index???
        // TODO add console arguments support
        // TODO Refactor. Replace If -else if - ENUMs statement?
        // TODO delete system.out stuff
        // TODO review and refactor?
        // TODO mock FolderReader in tests to return array with files
        // TODO make unit tests without file write??
        System.out.println("Started");

        String folderLocStr = "D:/java/projects/playlist-creator/test-music-final";
//        String folderLocStr = "D:\\Music\\muse";
//        new UserInterface(Paths.get(folderLoc)).startDialogue();
        Path folderLoc = Paths.get(folderLocStr);
        if (Files.isDirectory(folderLoc)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderLoc)) {
                for (Path entry : stream) {
                    if (!entry.toString().toLowerCase().endsWith(".mp3")) {
                        System.out.println("Skipped " + entry);
                        continue;
                    }
//                    String s = entry.toAbsolutePath().toString();
//                    Files.deleteIfExists(entry);
                    try {
                        Mp3File fileContainer = new Mp3File(entry.toString());
                        System.out.println(entry);
                        System.out.println("Has ID3v1 tag?: " + (fileContainer.hasId3v1Tag() ? "YES" : "NO"));
                        System.out.println("Has ID3v2 tag?: " + (fileContainer.hasId3v2Tag() ? "YES" : "NO"));
                        System.out.println("Has custom tag?: " + (fileContainer.hasCustomTag() ? "YES" : "NO"));

                        if (fileContainer.hasId3v1Tag()) {
                            System.out.println("ID3v1 tag");
                            ID3v1 id3v1Tag = fileContainer.getId3v1Tag();
                            String artist = id3v1Tag.getArtist();
                            String title = new String(id3v1Tag.getTitle().getBytes(), Charset.defaultCharset());
                            String track = id3v1Tag.getTrack();

                            System.out.println("Artist: " + artist);
                            System.out.println("Title: " + title);
                            System.out.println("Track: " + track);
                        }

                        if (fileContainer.hasId3v2Tag()) {
                            System.out.println("ID3v2 tag");
                            ID3v2 id3v2Tag = fileContainer.getId3v2Tag();
                            String artist = id3v2Tag.getArtist();
                            String title = id3v2Tag.getTitle();
                            String track = id3v2Tag.getTrack();

                            System.out.println("Artist: " + artist);
                            System.out.println("Title: " + title);
                            System.out.println("Track: " + track);
                        }

                        System.out.println();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedTagException e) {
                        e.printStackTrace();
                    } catch (InvalidDataException e) {
                        e.printStackTrace();
                    }

                }
            }
        }


        System.out.println("Finished");
    }
}
