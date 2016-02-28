import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.junit.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
    This whole testing class seems to be taking wrong approach.
    Could be better if test-music-final-source contained 1 short mp3 file.
    Testing directory contents are created based on arrays with names. Meta data is appended too.
    Then there is no need to hold 5 mp3 files.
 */
public class PlayListParserTest {

    private PlayListParser playListParser;
    private static final Path DESTINATION_FOLDER = Paths.get("src/test/resources/test-music");
    private static final Path TEST_DATA_SOURCE_FOLDER = Paths.get("src/test/resources/test-music-final-source");

    private static String playListFileName = "test-playlist.m3u8";

    private static final File[] testMp3FilesBefore = new File[]{
            new File(DESTINATION_FOLDER.resolve("rammstein_-_mein_ланд.mp3"), "Rammstein", null),
            new File(DESTINATION_FOLDER.resolve("OneRepublic%20-%20Stop%20and%20Старе.mp3"), "OneRepublic", "Stop And Старе"),
            new File(DESTINATION_FOLDER.resolve("Scorpions_-_Still_loving_you_(get-tune.net).mp3"), "Scorpions", "Still loving you"),
            new File(DESTINATION_FOLDER.resolve("test-playlist.m3u8"), null, null),
            new File(DESTINATION_FOLDER.resolve("тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3"),
                    "Тим McMorris", "Were Going Up (OST Няньки) (zaycev.net)")
    };

    private static final File[] testMp3FilesTranslited = new File[]{
            new File(DESTINATION_FOLDER.resolve("rammstein_-_mein_land.mp3"), "Rammstein", null),
            new File(DESTINATION_FOLDER.resolve("OneRepublic%20-%20Stop%20and%20Stare.mp3"), "OneRepublic", "Stop And Stare"),
            new File(DESTINATION_FOLDER.resolve("Scorpions_-_Still_loving_you_(get-tune.net).mp3"), "Scorpions", "Still loving you"),
            new File(DESTINATION_FOLDER.resolve("test-playlist.m3u8"), null, null),
            new File(DESTINATION_FOLDER.resolve("tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3"),
                    "Tim McMorris", "Were Going Up (OST Njan'ki) (zaycev.net)")
    };

    private static final File[] testMp3FilesTranslitedIndexed =  new File[]{
            new File(DESTINATION_FOLDER.resolve("1 - rammstein_-_mein_land.mp3"), "Rammstein", null),
            new File(DESTINATION_FOLDER.resolve("2 - OneRepublic%20-%20Stop%20and%20Stare.mp3"), "OneRepublic", "Stop And Stare"),
            new File(DESTINATION_FOLDER.resolve("4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3"), "Scorpions", "Still loving you"),
            new File(DESTINATION_FOLDER.resolve("test-playlist.m3u8"), null, null),
            new File(DESTINATION_FOLDER.resolve("3 - tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3"),
                    "Tim McMorris", "Were Going Up (OST Njan'ki) (zaycev.net)")
    };

    private static final File[] testMp3FilesIndexed = new File[]{
            new File(DESTINATION_FOLDER.resolve("1 - rammstein_-_mein_ланд.mp3"), "Rammstein", null),
            new File(DESTINATION_FOLDER.resolve("2 - OneRepublic%20-%20Stop%20and%20Старе.mp3"), "OneRepublic", "Stop And Старе"),
            new File(DESTINATION_FOLDER.resolve("4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3"), "Scorpions", "Still loving you"),
            new File(DESTINATION_FOLDER.resolve("test-playlist.m3u8"), null, null),
            new File(DESTINATION_FOLDER.resolve("3 - тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3"),
                    "Тим McMorris", "Were Going Up (OST Няньки) (zaycev.net)")
    };

    private static String playListContentBefore = "#EXTM3U\n" +
            "#EXTINF:233,Rammstein - rammstein_-_mein_ланд.mp3\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\rammstein_-_mein_ланд.mp3\n" +
            "#EXTINF:223,OneRepublic - Stop And Старе\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\OneRepublic%20-%20Stop%20and%20Старе.mp3\n" +
            "#EXTINF:182,Тим McMorris - Were Going Up (OST Няньки) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    private static String playListContentTranslited = "#EXTM3U\n" +
            "#EXTINF:233,Rammstein - rammstein_-_mein_land.mp3\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\rammstein_-_mein_land.mp3\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\OneRepublic%20-%20Stop%20and%20Stare.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Njan'ki) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    private static String playListContentTranslitedIndexed = "#EXTM3U\n" +
            "#EXTINF:233,Rammstein - rammstein_-_mein_land.mp3\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\1 - rammstein_-_mein_land.mp3\n" +
            "#EXTINF:223,OneRepublic - Stop And Stare\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\2 - OneRepublic%20-%20Stop%20and%20Stare.mp3\n" +
            "#EXTINF:182,Tim McMorris - Were Going Up (OST Njan'ki) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\3 - tim_mak_morris_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    private static String playListContentIndexed = "#EXTM3U\n" +
            "#EXTINF:233,Rammstein - rammstein_-_mein_ланд.mp3\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\1 - rammstein_-_mein_ланд.mp3\n" +
            "#EXTINF:223,OneRepublic - Stop And Старе\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\2 - OneRepublic%20-%20Stop%20and%20Старе.mp3\n" +
            "#EXTINF:182,Тим McMorris - Were Going Up (OST Няньки) (zaycev.net)\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\3 - тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3\n" +
            "#EXTINF:387,Scorpions - Still loving you\n" +
            "D:\\java\\projects\\playlist-creator\\src\\test\\resources\\test-music\\4 - Scorpions_-_Still_loving_you_(get-tune.net).mp3";

    @Before
    public void setUp() throws Exception {
        deleteFolderContent();
//        createFolderContent(testMp3FilesBefore);
        initFolderContent();
        initPlayListFile(playListContentBefore);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testExecuteTranslitorTranslitIndexerNone() throws Exception {
        FolderReader folderReader = new FolderReader(DESTINATION_FOLDER);
        Indexer indexer = new Indexer(IndexerOperation.NONE);
        Translitor translitor = new Translitor(TranslitorOperation.TRANSLIT);
        playListParser = new PlayListParser(folderReader, indexer, translitor);
        playListParser.execute();

        checkFolderContents(playListContentTranslited, testMp3FilesTranslited);
    }

    @Test
    public void testExecuteTranslitorNoneIndexerIndex() throws Exception {
        FolderReader folderReader = new FolderReader(DESTINATION_FOLDER);
        Indexer indexer = new Indexer(IndexerOperation.INDEX);
        Translitor translitor = new Translitor(TranslitorOperation.NONE);
        playListParser = new PlayListParser(folderReader, indexer, translitor);
        playListParser.execute();

        checkFolderContents(playListContentIndexed, testMp3FilesIndexed);
    }

    @Test
    public void testExecuteTranslitorTranslitIndexerIndex() throws Exception {
        FolderReader folderReader = new FolderReader(DESTINATION_FOLDER);
        Indexer indexer = new Indexer(IndexerOperation.INDEX);
        Translitor translitor = new Translitor(TranslitorOperation.TRANSLIT);
        playListParser = new PlayListParser(folderReader, indexer, translitor);
        playListParser.execute();

        checkFolderContents(playListContentTranslitedIndexed, testMp3FilesTranslitedIndexed);
    }
    @Ignore("Ignored test. Testing setup should be adjusted. Comment in header of class\n")
    @Test
    public void testExecuteTranslitorNoneIndexerUnIndex() throws Exception {
        deleteFolderContent();
//        createFolderContent(testMp3FilesIndexed);
        initPlayListFile(playListContentIndexed);

        FolderReader folderReader = new FolderReader(DESTINATION_FOLDER);
        Indexer indexer = new Indexer(IndexerOperation.UNINDEX);
        Translitor translitor = new Translitor(TranslitorOperation.NONE);
        playListParser = new PlayListParser(folderReader, indexer, translitor);
        playListParser.execute();

        checkFolderContents(playListContentBefore, testMp3FilesBefore);
    }

    private void checkFolderContents(String expectedPlayListContent, final File[] expectedFolderFiles) throws Exception {
        DirectoryStream<Path> dir = Files.newDirectoryStream(DESTINATION_FOLDER);
        Iterator<Path> directoryIter = dir.iterator();

        boolean playlistFileFound = false;

        while (directoryIter.hasNext()) {
            Path folderFile = directoryIter.next();

            String dirFileName = folderFile.getFileName().toString();
            if (dirFileName.endsWith(".m3u8")) {
                Path absolutePath = folderFile.toAbsolutePath();
                String playListContent = readPlayListFile(absolutePath);
                assertEquals("Playlist content is not equal to expected", playListContent, expectedPlayListContent);
                playlistFileFound = true;
            } else {
                boolean musicFileFound = false;
                for (File expectedFile : expectedFolderFiles) {
                    if (dirFileName.equals(expectedFile.getOriginalPath().getFileName().toString())) {
//                        System.out.println("MATCH: " + mp3Name);
                        musicFileFound = true;
                        if (musicFileFound) {
                            String processedArtist = expectedFile.getOriginalArtist();
                            String processedTitle = expectedFile.getOriginalTitle();
                            Mp3File mp3File = new Mp3File(folderFile.toAbsolutePath().toString());
                            if (!mp3File.hasId3v2Tag()) {
                                Assert.fail("Mp3 file missing Id3v2 tag");
                            }
                            ID3v2 id3v2Tag = mp3File.getId3v2Tag();
                            String expectedTitle = id3v2Tag.getTitle();
                            String expectedArtist = id3v2Tag.getArtist();

                            assertEquals("No match for title", expectedTitle, processedTitle);
                            assertEquals("No match for artist", expectedArtist, processedArtist);
                        }
                        break;
                    }
                }
                assertTrue("No match for music file " + dirFileName, musicFileFound);
            }
        }
        assertTrue("Playlist file not found", playlistFileFound);
    }

    private void deleteFolderContent() {
        java.io.File file = DESTINATION_FOLDER.toFile();
        for (String fileName : file.list()) {
            Path filePath = Paths.get(fileName);
            try {
                Files.delete(DESTINATION_FOLDER.resolve(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createFolderContent(final String[] mp3FilesList) {
        for (String fileName : mp3FilesList) {
            Path filePath = DESTINATION_FOLDER.resolve(fileName);
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initFolderContent() throws Exception {
        if (Files.isDirectory(TEST_DATA_SOURCE_FOLDER)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(TEST_DATA_SOURCE_FOLDER)) {
                for (Path entry : stream) {
                    Path destination = DESTINATION_FOLDER.resolve(entry.getFileName());
                    Files.copy(entry, destination, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Copied: " + destination);
                }
            }
        }
    }

    private void initPlayListFile(String playListContent) {
        Path path = DESTINATION_FOLDER.resolve(playListFileName);
        try {
            Files.deleteIfExists(path);
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