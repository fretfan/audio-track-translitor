import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by seroga on 20-Jan-16.
 */
public class IndexerTest {

    private static Indexer indexer;

    @Test
    public void testPerformOperationIndex() {
        indexer = new Indexer(IndexerOperation.INDEX);
        String before = "Enter Sandman - Metallica.mp3";
        for (int i = 1; i <= 10; i++) {
            String expected = i + Indexer.PREFIX + before;
            String actual = indexer.performOperation(before, i);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testPerformOperationUnindex() {
        indexer = new Indexer(IndexerOperation.UNINDEX);
        String before = "1" + Indexer.PREFIX +  "Enter Sandman - Metallica.mp3";
        String expected = "Enter Sandman - Metallica.mp3";
        String actual = indexer.performOperation(before, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testPerformOperationNone() {
        indexer = new Indexer(IndexerOperation.NONE);
        String before = "Enter Sandman - Metallica.mp3";
        String expected = "Enter Sandman - Metallica.mp3";
        String actual = indexer.performOperation(before, 0);
        assertEquals(expected, actual);
    }

}