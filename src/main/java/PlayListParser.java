import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seroga on 10-Jan-16.
 */
public class PlayListParser {

    private FolderReader folderReader;
    private Translitor translitor;
    private Indexer indexer;

    public PlayListParser(FolderReader folderReader, Indexer indexer, Translitor translitor) {
        this.folderReader = folderReader;
        this.indexer = indexer;
        this.translitor = translitor;
    }

    private List<String> readPlayListFile() {
        String originalName = folderReader.getPlayListFile().getOriginalName();
        try {
            return Files.readAllLines(Paths.get(originalName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read playlist file", e);
        }
    }

    public void execute() {
        if (indexer.getOperation() == IndexerOperation.NONE
                && translitor.getOperation() == TranslitorOperation.NONE) {
            return;
        }
        List<String> lines = readPlayListFile();
        List<String> newPlayListContent = new ArrayList<>();
        List<File> audioFiles = folderReader.getAudioFiles();
        int trackCount = 0;
        for (String line : lines) {
//            System.out.println(line);
            if (line.startsWith("#")) {
                newPlayListContent.add(line);
            } else {
//                trackCount++;
                parseAudioFilePath(newPlayListContent, audioFiles, line, trackCount);
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

    private void parseAudioFilePath(List<String> newPlayListContent, List<File> audioFiles, String line, int trackCount) {
        Path pathToFolder = folderReader.getPathToFolder();
        for (File f : audioFiles) {
//                    System.out.println("Line: " + line);
//                    System.out.println("Original: " + originalName);
            if (f.getOriginalName().equals(line)) {
                String translitedLine = translitor.processPath(line);
//                translitedLine = indexer.performOperation(translitedLine, trackCount);
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
