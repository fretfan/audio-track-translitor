import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FolderReader {

    private Path pathToFolder;
    private File playListFile;
    private List<File> audioFiles = new ArrayList<>();

    public FolderReader(Path pathToFolder) {
        this.pathToFolder = pathToFolder;
        readFolderForFiles(pathToFolder);
    }

    private void readFolderForFiles(Path path) {

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            for (Path p : directoryStream) {
                String fileName = p.getFileName().toString();

                if (fileName.endsWith(".m3u8")) {
                    playListFile = new File();
                    playListFile.setType(FileType.PLAYLIST_FILE);
                    playListFile.setOriginalPath(p);
                } else if (fileName.toLowerCase().endsWith(".mp3")) {
                    File f = new File();
                    f.setType(FileType.MUSIC_FILE);
                    f.setOriginalPath(p.toAbsolutePath());

                    try {
                        Mp3File mp3File = new Mp3File(p.toAbsolutePath().toString());
                        if (mp3File.hasId3v2Tag()) {
                            ID3v2 id3v2Tag = mp3File.getId3v2Tag();
                            f.setOriginalArtist(id3v2Tag.getArtist());
                            f.setOriginalTitle(id3v2Tag.getTitle());
                        }
                    } catch (UnsupportedTagException | InvalidDataException e ) {
                        throw new RuntimeException(e);
                    }
                    audioFiles.add(f);
                } else {
                    throw new RuntimeException("Cannot parse file: " + fileName);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read: " + path, ex);
        }
    }

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
