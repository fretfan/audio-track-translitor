import java.nio.file.Path;
import java.util.Objects;

/**
 * Created by seroga on 10-Jan-16.
 */
public class File {

    private Path originalPath;
    private Path translitedPath;
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

    public Path getTranslitedPath() {
        return translitedPath;
    }

    public void setTranslitedPath(Path translitedPath) {
        this.translitedPath = translitedPath;
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
