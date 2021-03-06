import com.mpatric.mp3agic.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

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
        Path originalName = folderReader.getPlayListFile().getOriginalPath();
        try {
            return Files.readAllLines(originalName);
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
                String parsedLine = line;
                if (translitor.getOperation() == TranslitorOperation.TRANSLIT) {
                    parsedLine = translitor.translitText(line);
                }
                newPlayListContent.add(parsedLine);
            } else {
                trackCount++;
                parseAudioFilePath(newPlayListContent, audioFiles, line, trackCount);
            }
        }
        updatePlaylist(newPlayListContent);
    }

    private void updatePlaylist(List<String> newPlayListContent) {
        Path path = folderReader.getPlayListFile().getOriginalPath();
        try {
            Files.deleteIfExists(path);
            Files.write(path, newPlayListContent, StandardOpenOption.CREATE);

        } catch (IOException e) {
            throw new RuntimeException("Cannot rewrite playlist " + folderReader.getPlayListFile().getOriginalPath(), e);
        }
    }

    private void parseAudioFilePath(List<String> newPlayListContent, List<File> audioFiles, String line, int trackCount) {
        Path pathToFolder = folderReader.getPathToFolder();
        for (File f : audioFiles) {
            if (f.getOriginalPath().toString().equals(line)) {
                String processedLine = line;
                if (translitor.getOperation() == TranslitorOperation.TRANSLIT) {
                    processedLine = translitor.processPath(line);
                    processMp3Tags(f);
                }
                if (indexer.getOperation() == IndexerOperation.INDEX) {
                    Path path = Paths.get(processedLine);
                    String indexedFileName = indexer.performOperation(path.getFileName().toString(), trackCount);
                    Path pathIndexed = path.resolveSibling(indexedFileName);
                    processedLine = pathIndexed.toString();
                } else if (indexer.getOperation() == IndexerOperation.UNINDEX) {
                    Path path = Paths.get(processedLine);
                    String unIndexedFileName = indexer.performOperation(path.getFileName().toString(), 0);
                    Path pathIndexed = path.resolveSibling(unIndexedFileName);
                    processedLine = pathIndexed.toString();
                }
                f.setProcessedPath(Paths.get(processedLine));
                newPlayListContent.add(processedLine);

                renameAudioFile(pathToFolder, f);
                break;
            }
        }
    }

    private void processMp3Tags(File f) {

        try {
            Mp3File mp3File = new Mp3File(f.getOriginalPath().toAbsolutePath().toString());
            Path originalAbsolutePath = f.getOriginalPath().toAbsolutePath();
            if (mp3File.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3File.getId3v2Tag();
                f.setProcessedArtist(id3v2Tag.getArtist());
                f.setProcessedTitle(id3v2Tag.getTitle());
                id3v2Tag.setArtist(translitor.translitText(id3v2Tag.getArtist()));
                id3v2Tag.setTitle(translitor.translitText(id3v2Tag.getTitle()));

                // KOSTYLI BLEAT!!!!
                String fileName = originalAbsolutePath.getFileName().toString();
                int indexOf = fileName.indexOf(".mp3");
                String newName = fileName.substring(0, indexOf) + "_temp_copy" + fileName.substring(indexOf, fileName.length());
                Path tempPath = originalAbsolutePath.getParent().resolve(newName);
                /*
                Mp3agic doesn't allowto rewrite file. Creating file with _temp_copy postfix.
                Delete original file. Create new mp3 file with original name and saved tags.
                Delete temp file.
                 */
                mp3File.save(tempPath.toString());
                Mp3File newMp3File = new Mp3File(tempPath.toString());

                Files.deleteIfExists(originalAbsolutePath);
                newMp3File.save(originalAbsolutePath.toString());
                Files.deleteIfExists(tempPath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedTagException e) {
            throw new RuntimeException(e);
        } catch (InvalidDataException e) {
            throw new RuntimeException(e);
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void renameAudioFile(Path pathToFolder, File f) {
        try {
            DirectoryStream<Path> audioFilePaths = Files.newDirectoryStream(pathToFolder);
            for (Path p : audioFilePaths) {
                if (f.getOriginalPath().equals(p.toAbsolutePath())) {
                    Files.move(p, p.resolveSibling(f.getProcessedPath()));
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot read directory " + pathToFolder, e);
        }
    }

}
