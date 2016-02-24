import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.Assert.*;

public class FolderReaderTest {

    private static FolderReader folderReader;
    private static final Path DESTINATION_FOLDER = Paths.get("src/test/resources/test-music");
    private static final Path PLAY_LIST_FILE_NAME = Paths.get("D:/java/projects/playlist-creator/src/test/resources/test-music/test-playlist.m3u8");
    private static final Path TEST_DATA_SOURCE_FOLDER = Paths.get("src/test/resources/test-music-final-source");

    private static File[] testFiles = new File[]{
            new File(DESTINATION_FOLDER.resolve("rammstein_-_mein_ланд.mp3"), "Rammstein", "Mein Land"),
            new File(DESTINATION_FOLDER.resolve("OneRepublic%20-%20Stop%20and%20Старе.mp3"), "OneRepublic", "Stop And Старе"),
            new File(DESTINATION_FOLDER.resolve("Scorpions_-_Still_loving_you_(get-tune.net).mp3"), "Scorpions", "Still loving you"),
            new File(DESTINATION_FOLDER.resolve("test-playlist.m3u8"), null, null),
            new File(DESTINATION_FOLDER.resolve("тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3"),
                    "Тим McMorris", "Were Going Up (OST Няньки) (zaycev.net)")
    };

    @BeforeClass
    public static void setUp() throws Exception {
        deleteFiles();
        initTestFiles();
        folderReader = new FolderReader(DESTINATION_FOLDER);
    }

    private static void initTestFiles() throws Exception {

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

    private static void deleteFiles() throws IOException {
        Path folderLoc = Paths.get(DESTINATION_FOLDER.toString());
        if (Files.isDirectory(folderLoc)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderLoc)) {
                for (Path entry : stream) {
                    String s = entry.toAbsolutePath().toString();
                    Files.deleteIfExists(entry);
                    System.out.println("Deleted: " + s);
                }
            }
        }
    }

    @Test
    public void testGetPlayListFile() throws Exception {
        File playListFile = folderReader.getPlayListFile();
        assertNotNull(playListFile);

        FileType fileType = playListFile.getType();
        assertEquals(FileType.PLAYLIST_FILE, fileType);

        Path originalName = playListFile.getOriginalPath();
        assertEquals(PLAY_LIST_FILE_NAME, originalName.toAbsolutePath());
    }

    @Test
    public void testGetAudioFiles() throws Exception {
        List<File> audioFiles = folderReader.getAudioFiles();
        assertNotNull(audioFiles);

        for (File audioFileRead : audioFiles) {
            FileType type = audioFileRead.getType();
            assertEquals(FileType.MUSIC_FILE, type);

            assertNotNull(audioFileRead.getOriginalPath());

            boolean originalPathFound = false;

            for (File testFile : testFiles) {
                if (testFile.getOriginalPath().toAbsolutePath().equals(audioFileRead.getOriginalPath().toAbsolutePath())) {
                    originalPathFound = true;

                    assertEquals("Bad artist: " + audioFileRead.getOriginalArtist() ,testFile.getOriginalArtist(), audioFileRead.getOriginalArtist());
                    assertEquals("Bad title: " + audioFileRead.getOriginalTitle(), testFile.getOriginalTitle(), audioFileRead.getOriginalTitle());
                    break;
                }
            }
            assertTrue("Bad originalPath: " + audioFileRead.getOriginalPath(), originalPathFound);

        }
    }
}