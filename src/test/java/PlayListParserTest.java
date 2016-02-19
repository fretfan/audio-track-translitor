import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by seroga on 22-Jan-16.
 */
public class PlayListParserTest {

    private PlayListParser playListParser;
    private final Path FOLDER_LOC = Paths.get("test-music");

    private static String playListFileName = "test-playlist.m3u8";

    private static final String[] testMp3FilesBefore = new String[]{
            "OneRepublic%20-%20Стоп%20анд%20Старе.mp3",
            "Лучшая группа_-_ЛУЧШИЙ_Трэк 23.mp3",
            "Scorpions_-_Still_loving_you_(get-tune.net).mp3",
            "тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3"
    };

    private static final String[] testMp3FilesTranslited = new String[]{
            "OneRepublic%20-%20Stop%20and%20Stare.mp3",
            "Lu4shaja gruppa_-_LU4SHIJ_Trek 23.mp3",
            "Scorpions_-_Still_loving_you_(get-tune.net).mp3",
            "tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3"
    };

    private static final String[] testMp3FilesTranslitedIndexed = new String[]{
            "1 - OneRepublic%20-%20Stop%20and%20Stare.mp3",
            "3 - Lu4shaja gruppa_-_LU4SHIJ_Trek 23.mp3",
            "4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3",
            "2 - tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3"
    };

    private static final String[] testMp3FilesIndexed = new String[]{
            "1 - OneRepublic%20-%20Стоп%20анд%20Старе.mp3",
            "3 - Лучшая группа_-_ЛУЧШИЙ_Трэк 23.mp3",
            "4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3",
            "2 - тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3"
    };

    private static String playListContentBefore = "#EXTM3U\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\OneRepublic%20-%20Стоп%20анд%20Старе.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Няньки) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:233,Лучшая группа - ЛУЧШИЙ_Трэк\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\Лучшая группа_-_ЛУЧШИЙ_Трэк 23.mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    private static String playListContentTranslited = "#EXTM3U\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\OneRepublic%20-%20Stop%20and%20Stare.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Njan'ki) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:233,Lu4shaja gruppa - LU4SHIJ_Trek\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\Lu4shaja gruppa_-_LU4SHIJ_Trek 23.mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    private static String playListContentTranslitedIndexed = "#EXTM3U\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\1 - OneRepublic%20-%20Stop%20and%20Stare.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Njan'ki) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\2 - tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:233,Lu4shaja gruppa - LU4SHIJ_Trek\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\3 - Lu4shaja gruppa_-_LU4SHIJ_Trek 23.mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    private static String playListContentIndexed = "#EXTM3U\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\1 - OneRepublic%20-%20Стоп%20анд%20Старе.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Няньки) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\2 - тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:233,Лучшая группа - ЛУЧШИЙ_Трэк\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\3 - Лучшая группа_-_ЛУЧШИЙ_Трэк 23.mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\test-music\\4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    @Before
    public void setUp() throws Exception {
        deleteFolderContent();
        createFolderContent(testMp3FilesBefore);
        createPlayListFile(playListContentBefore);
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
        playListParser.execute();

        checkFolderContents(playListContentBefore, testMp3FilesBefore);
    }

    @Test
    public void testExecuteTranslitorTranslitIndexerNone() throws Exception {
        FolderReader folderReader = new FolderReader(FOLDER_LOC);
        Indexer indexer = new Indexer(IndexerOperation.NONE);
        Translitor translitor = new Translitor(TranslitorOperation.TRANSLIT);
        playListParser = new PlayListParser(folderReader, indexer, translitor);
        playListParser.execute();

        checkFolderContents(playListContentTranslited, testMp3FilesTranslited);
    }

    @Test
    public void testExecuteTranslitorNoneIndexerIndex() throws Exception {
        FolderReader folderReader = new FolderReader(FOLDER_LOC);
        Indexer indexer = new Indexer(IndexerOperation.INDEX);
        Translitor translitor = new Translitor(TranslitorOperation.NONE);
        playListParser = new PlayListParser(folderReader, indexer, translitor);
        playListParser.execute();

        checkFolderContents(playListContentIndexed, testMp3FilesIndexed);
    }

    @Test
    public void testExecuteTranslitorTranslitIndexerIndex() throws Exception {
        FolderReader folderReader = new FolderReader(FOLDER_LOC);
        Indexer indexer = new Indexer(IndexerOperation.INDEX);
        Translitor translitor = new Translitor(TranslitorOperation.TRANSLIT);
        playListParser = new PlayListParser(folderReader, indexer, translitor);
        playListParser.execute();

        checkFolderContents(playListContentTranslitedIndexed, testMp3FilesTranslitedIndexed);
    }

    @Test
    public void testExecuteTranslitorNoneIndexerUnIndex() throws Exception {
        deleteFolderContent();
        createFolderContent(testMp3FilesIndexed);
        createPlayListFile(playListContentIndexed);

        FolderReader folderReader = new FolderReader(FOLDER_LOC);
        Indexer indexer = new Indexer(IndexerOperation.UNINDEX);
        Translitor translitor = new Translitor(TranslitorOperation.NONE);
        playListParser = new PlayListParser(folderReader, indexer, translitor);
        playListParser.execute();

        checkFolderContents(playListContentBefore, testMp3FilesBefore);
    }

    private void checkFolderContents(String expectedPlayListContent, final String[] expectedFolderFiles) throws Exception {
        DirectoryStream<Path> dir = Files.newDirectoryStream(FOLDER_LOC);
        Iterator<Path> iterator = dir.iterator();

        boolean playlistFileFound = false;

        while (iterator.hasNext()) {
            Path next = iterator.next();

            String fileName = next.getFileName().toString();
            if (fileName.endsWith(".m3u8")) {
                Path absolutePath = next.toAbsolutePath();
                String playListContent = readPlayListFile(absolutePath);
                assertEquals("Playlist content is not equal to expected", playListContent, expectedPlayListContent);
                playlistFileFound = true;
            } else {
                boolean musicFileFound = false;
                for (String mp3Name : expectedFolderFiles) {
                    if (fileName.equals(mp3Name)) {
//                        System.out.println("MATCH: " + mp3Name);
                        musicFileFound = true;
                        break;
                    }
                }
                assertTrue("No match for music file " + fileName, musicFileFound);
            }
        }
        assertTrue("Playlist file not found", playlistFileFound);
    }

    private void deleteFolderContent() {
        java.io.File file = FOLDER_LOC.toFile();
        for (String fileName : file.list()) {
            Path filePath = Paths.get(fileName);
            try {
                Files.delete(FOLDER_LOC.resolve(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createFolderContent(final String[] mp3FilesList) {
        for (String fileName : mp3FilesList) {
            Path filePath = FOLDER_LOC.resolve(fileName);
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createPlayListFile(String playListContent) {
        Path path = FOLDER_LOC.resolve(playListFileName);
        try {
            Files.write(path, playListContent.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readPlayListFile(Path playlistPath) {
        try {
            byte[] playlistFileContent = Files.readAllBytes(playlistPath);
            String playlistFileContentStr = new String(playlistFileContent);
            playlistFileContentStr = playlistFileContentStr.replace("\r", "");
            if (playlistFileContentStr.endsWith("\n")) {
                playlistFileContentStr = playlistFileContentStr.substring(0, playlistFileContentStr.length() - 1);
            }
            return playlistFileContentStr;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read playlist file", e);
        }
    }
}