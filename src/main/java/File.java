/**
 * Created by seroga on 10-Jan-16.
 */
public class File {

    private String originalName;
    private String translitedName;
    private FileType type;

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getTranslitedName() {
        return translitedName;
    }

    public void setTranslitedName(String translitedName) {
        this.translitedName = translitedName;
    }
}
