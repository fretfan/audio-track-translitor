import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Created by seroga on 22-Jan-16.
 */
public class PlayListParserTest {

    private PlayListParser playListParser;
    private final Path FOLDER_LOC = Paths.get("test-music");

    private static String playListFileName = "test-playlist.m3u8";

    private static String[] testMp3FilesBefore = new String[]{
            "OneRepublic%20-%20Стоп%20анд%20Стареe.mp3",
            "Лучшая группа_-_ЛУЧШИЙ_Трэк 23.mp3",
            "Scorpions_-_Still_loving_you_(get-tune.net).mp3",
            "тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3"
    };

    private static String[] testMp3FilesTranslited = new String[]{
            "OneRepublic%20-%20Stop%20and%20Stare.mp3",
            "Lu4shaja gruppa_-_LU4SHIJ_Trek 23.mp3",
            "Scorpions_-_Still_loving_you_(get-tune.net).mp3",
            "tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3"
    };

    private static String[] testMp3FilesTranslitedIndexed = new String[]{
            "1 - OneRepublic%20-%20Stop%20and%20Stare.mp3",
            "3 - Lu4shaja gruppa_-_LU4SHIJ_Trek 23.mp3",
            "4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3",
            "2 - tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3"
    };

    private static String[] testMp3FilesIndexed = new String[]{
            "1 - OneRepublic%20-%20Стоп%20анд%20Стареe.mp3",
            "3 - Лучшая группа_-_ЛУЧШИЙ_Трэк 23.mp3",
            "4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3",
            "2 - тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3"
    };

    private static String playListContentBefore = "#EXTM3U\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\OneRepublic%20-%20Стоп%20анд%20Старе.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Няньки) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:233,Лучшая группа - ЛУЧШИЙ_Трэк\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\Лучшая группа_-_ЛУЧШИЙ_Трэк 23.mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    private static String playListContentTranslited = "#EXTM3U\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\OneRepublic%20-%20Stop%20and%20Stare.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Njan'ki) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:233,Lu4shaja gruppa - LU4SHIJ_Trek\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\Lu4shaja gruppa_-_LU4SHIJ_Trek 23.mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    private static String playListContentAfterTranslitedIndexed = "#EXTM3U\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\1 - OneRepublic%20-%20Stop%20and%20Stare.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Njan'ki) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\2 - tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:233,Lu4shaja gruppa - LU4SHIJ_Trek\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\3 - Lu4shaja gruppa_-_LU4SHIJ_Trek 23.mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    private static String playListContentIndexed = "#EXTM3U\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\1 - OneRepublic%20-%20Стоп%20анд%20Старе.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Няньки) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\2 - тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:233,Лучшая группа - ЛУЧШИЙ_Трэк\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\3 - Лучшая группа_-_ЛУЧШИЙ_Трэк 23.mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\test-music -backup\\4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    @Before
    public void setUp() throws Exception {
        deleteFolderContent();
        createFolderContent();

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testExecuteTranslitorNoneIndexerNone() throws Exception {
        FolderReader folderReader = new FolderReader(FOLDER_LOC);
        Indexer indexer = new Indexer(IndexerOperation.NONE);
        Translitor translitor = new Translitor(TranslitorOperation.NONE);
        playListParser = new PlayListParser(folderReader, indexer, translitor);
//        playListParser.execute();

        DirectoryStream<Path> paths = Files.newDirectoryStream(FOLDER_LOC);
        Iterator<Path> iterator = paths.iterator();
        while (iterator.hasNext()) {
            Path next = iterator.next();
            
            System.out.println(next);
        }

    }

    private void deleteFolderContent() {
        Path path = FOLDER_LOC;
        java.io.File file = path.toFile();
        for (String fileName : file.list()) {
            Path filePath = Paths.get(fileName);
            try {
                Files.delete(path.resolve(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createFolderContent() {
        Path path = FOLDER_LOC;
        for (String fileName : testMp3FilesBefore) {
            Path filePath = path.resolve(fileName);
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        createPlayListFile();
    }

    private void createPlayListFile() {
        Path path = FOLDER_LOC;
        path = path.resolve(playListFileName);
        try {
            Files.write(path, playListContentBefore.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}