import java.nio.file.Path;
import java.util.Objects;

/**
 * Contains original and parsed (translited/indexed/unindexed) paths.
 */
public class File {

    private Path originalPath;
    private Path processedPath;
    private FileType type;

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
