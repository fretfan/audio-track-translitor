/**
 * Created by seroga on 10-Jan-16.
 */
public class File {

    private String originalName;
    private String translitedName;
    private String prefix;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        return !(originalName != null ? !originalName.equals(file.originalName) : file.originalName != null);

    }

    @Override
    public int hashCode() {
        return originalName != null ? originalName.hashCode() : 0;
    }
}
