import java.nio.file.Path;
import java.util.Objects;

/**
 * Contains original and parsed (translited/indexed/unindexed) paths.
 */
public class File {

    private Path originalPath;
    private Path processedPath;
    private String originalTitle;
    private String processedTitle;
    private String originalArtist;
    private String processedArtist;
    private FileType type;

    public File(Path originalPath, String originalArtist, String originalTitle) {
        this.originalPath = originalPath;
        this.originalArtist = originalArtist;
        this.originalTitle = originalTitle;
    }

    public File() {
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public Path getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(Path originalPath) {
        this.originalPath = originalPath;
    }

    public Path getProcessedPath() {
        return processedPath;
    }

    public void setProcessedPath(Path processedPath) {
        this.processedPath = processedPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getProcessedTitle() {
        return processedTitle;
    }

    public void setProcessedTitle(String processedTitle) {
        this.processedTitle = processedTitle;
    }

    public String getOriginalArtist() {
        return originalArtist;
    }

    public void setOriginalArtist(String originalArtist) {
        this.originalArtist = originalArtist;
    }

    public String getProcessedArtist() {
        return processedArtist;
    }

    public void setProcessedArtist(String processedArtist) {
        this.processedArtist = processedArtist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return Objects.equals(originalPath, file.originalPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalPath);
    }
}
