import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seroga on 05-Jan-16.
 */
public class FolderReader {

    private Path pathToFolder;
    private File playListFile;
    private List<File> audioFiles = new ArrayList<>();

    public FolderReader(Path pathToFolder) {
        this.pathToFolder = pathToFolder;
        readFolderForFiles(pathToFolder);
    }

    private void readFolderForFiles(Path path) {
//        String filteredPath = filterPath(path);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            for (Path p : directoryStream) {
//                System.out.println(p.getFileName().toString());
//                System.out.println(p.toAbsolutePath().toString());
                String fileName = p.getFileName().toString();

                if (fileName.endsWith(".m3u8")) {
                    playListFile = new File();
                    playListFile.setType(FileType.PLAYLIST_FILE);
                    playListFile.setOriginalPath(p);
                } else if (fileName.endsWith(".mp3") || fileName.endsWith(".ogg") || fileName.endsWith(".wav")) {
                    File f = new File();
                    f.setType(FileType.MUSIC_FILE);
                    f.setOriginalPath(p.toAbsolutePath());
                    audioFiles.add(f);
                } else {
                    System.err.println("Cannot parse file: " + fileName );
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read: " + path, ex);
        }
    }

//    private String filterPath(Path path) {
//        String newPath = path.replace("/", "\\");
//        return newPath;
//    }

    public File getPlayListFile() {
        if (playListFile == null) {
            throw new RuntimeException("No .m3u8 playlist file was found in " + pathToFolder);
        }
        return playListFile;
    }

    public List<File> getAudioFiles() {
        return audioFiles;
    }

    public Path getPathToFolder() {
        return pathToFolder;
    }

    public void setPathToFolder(Path pathToFolder) {
        this.pathToFolder = pathToFolder;
    }
}
