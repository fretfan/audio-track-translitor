/**
 * Created by seroga on 20-Jan-16.
 */
public class Indexer {

    public static final String PREFIX = "_";
    private final IndexerOperation operation;

    public Indexer(IndexerOperation operation) {
        this.operation = operation;
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
                throw new RuntimeException("Unrecognised indexer operation: " + operation);
        }
    }

    private String indexFileName(String fileName, int index) {
        return index + PREFIX + fileName;
    }

    private String unindexFileName(String fileName) {
        int pos = fileName.indexOf(PREFIX) + PREFIX.length();
        return fileName.substring(pos);
    }

    public IndexerOperation getOperation() {
        return operation;
    }
}
