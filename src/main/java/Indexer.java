/**
 * Created by seroga on 20-Jan-16.
 */
public class Indexer {

    private static final String PREFIX = "_";
    private IndexerOperation operation;

    public Indexer(IndexerOperation operation) {
        this.operation = operation;
    }

    public Indexer() {
        this.operation = IndexerOperation.NONE;
    }

    public String performOperation(String fileName, int index) {
        switch (operation) {
            case NONE:
                return fileName;
            case INDEX:
                return indexFileName(fileName, index);
            case UNINDEX:
                return unindexFileName(fileName);
            default:
                throw new RuntimeException("Unrecognized indexer operation: " + operation);
        }
    }

    private String indexFileName(String fileName, int index) {
        return index + PREFIX + fileName;
    }

    private String unindexFileName(String fileName) {
        int pos = fileName.indexOf(PREFIX) + PREFIX.length();
        return fileName.substring(pos);
    }
}
