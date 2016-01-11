import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by seroga on 10-Jan-16.
 */
public class FolderReaderTest {

    private static FolderReader folderReader;
    private static String testMusicFolderName = "\\test-music";
    private String playListFileName = "D:\\java\\projects\\playlist-creator\\test-music\\test-playlist.m3u8";
    private static String[] testFiles = new String[]{
            "Death - Voice of the Soul.mp3",
            "OneRepublic%20-%20Stop%20and%20Stare.mp3",
            "Лучшая группа_-_ЛУЧШИЙ_Трэк 23.mp3",
            "Scorpions_-_Still_loving_you_(get-tune.net).mp3",
            "test-playlist.m3u8",
            "тим_мак_моррис_-_were_going_up_ost_nyanki_(zaycev.net).mp3"
    };

    private static String folderLocStr = "D:/java/projects/playlist-creator/test-music";
    private static Path folderLocPath;


    @BeforeClass
    public static void setUp() throws Exception {

        String curDir = Paths.get("").toAbsolutePath().toString();
        folderLocPath = Paths.get(curDir + "\\" + testMusicFolderName);

        initTestFiles();
        folderReader = new FolderReader(folderLocStr);

    }

    private static void initTestFiles() throws Exception {
//        String curLoc = Paths.get("").toAbsolutePath().toString();
//        String testMusicLoc = curLoc + testMusicFolderName;
//        boolean directory = Files.isDirectory(Paths.get(testMusicLoc));

        for (String testFileName : testFiles) {

            Path filePath = Paths.get(folderLocPath.toString() + "\\" + testFileName);
            try {
                Files.createFile(filePath);
                System.out.println("Created: " + filePath);

            } catch (FileAlreadyExistsException e) {
                System.err.println("File already exists, ignoring");
                continue;
            } catch (IOException e) {
                throw new RuntimeException("Parent directory does not exist", e);
            }
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Path folderLoc = Paths.get(folderLocPath.toString());
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

        String originalName = playListFile.getOriginalName();
        assertEquals(playListFileName, originalName);
    }

    @Test
    public void testGetAudioFiles() throws Exception {
        List<File> audioFiles = folderReader.getAudioFiles();
        assertNotNull(audioFiles);

        for (File f : audioFiles) {
            FileType type = f.getType();
            assertEquals(FileType.MUSIC_FILE, type);
        }
    }
}