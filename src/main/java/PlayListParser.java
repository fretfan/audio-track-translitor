import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seroga on 10-Jan-16.
 */
public class PlayListParser {

    private FolderReader folderReader;
    private Translitor translitor = new Translitor();

    public PlayListParser(FolderReader folderReader) {
        this.folderReader = folderReader;
    }

    private List<String> readPlayListFile() {
        String originalName = folderReader.getPlayListFile().getOriginalName();
        try {
            return Files.readAllLines(Paths.get(originalName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read playlist file", e);
        }
    }

    public void parsePlaylistFile() {
        List<String> lines = readPlayListFile();
        List<String> newPlayListContent = new ArrayList<>();
        List<File> audioFiles = folderReader.getAudioFiles();
        for (String line : lines) {
//            System.out.println(line);
            if (line.startsWith("#")) {
                newPlayListContent.add(line);
            } else {
                parseAudioFilePath(newPlayListContent, audioFiles, line);
            }
        }
        updatePlaylist(newPlayListContent);
    }

    private void updatePlaylist(List<String> newPlayListContent) {
        Path path = Paths.get(folderReader.getPlayListFile().getOriginalName());
        try {
            Files.deleteIfExists(path);
            Files.write(path, newPlayListContent, StandardOpenOption.CREATE);

        } catch (IOException e) {
            throw new RuntimeException("Cannot rewrite playlist " + folderReader.getPlayListFile().getOriginalName(), e);
        }
    }

    private void parseAudioFilePath(List<String> newPlayListContent, List<File> audioFiles, String line) {
        Path pathToFolder = Paths.get(folderReader.getPathToFolder());
        for (File f : audioFiles) {
//                    System.out.println("Line: " + line);
//                    System.out.println("Original: " + originalName);
            if (f.getOriginalName().equals(line)) {
                String translitedLine = translitor.translitPath(line);
                f.setTranslitedName(translitedLine);
                newPlayListContent.add(translitedLine);

                renameAudioFile(pathToFolder, f);
                break;
            }
        }
    }

    private void renameAudioFile(Path pathToFolder, File f) {
        try {
            DirectoryStream<Path> audioFilePaths = Files.newDirectoryStream(pathToFolder);
            for (Path p : audioFilePaths) {
//                        System.out.println(p);
                if (f.getOriginalName().equals(p.toString())) {
//                    System.out.println("Renaming: " + f.getOriginalName());
//                    System.out.println("to: " + f.getTranslitedName());
                    Files.move(p, p.resolveSibling(f.getTranslitedName()));
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Cannot read directory " + pathToFolder, e);
        }
    }

}
