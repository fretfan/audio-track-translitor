import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by seroga on 03-Jan-16.
 */
public class TranslitorTest {

    private Translitor translitor;

    @Before
    public void setUp() {
        translitor = new Translitor(TranslitorOperation.TRANSLIT);
    }

    @Test
    public void testTranslitText() throws Exception {
        String[] before = new String[]{
                "№~`1!2@3#4$5%6^7&8*9(0)-_=+/|\\",
                "йЙцЦуУкКеЕнНгГшШщЩзЗхХъЪ",
                "фФыЫвВаАпПрРоОлЛдДжЖэЭ",
                "яЯчЧсСмМиИтТьЬбБюЮ.,",

                "qQwWeErRtTyYuUiIoOpP[{]}",
                "aAsSdDfFgGhHjJkKlL;:'\"",
                "zZxXcCvVbBnNmM,<.>/?"
        };
        String[] expected = new String[]{
                "№~`1!2@3#4$5%6^7&8*9(0)-_=+/|\\",
                "jJcCuUkKeEnNgGshSHwWzZhH^^",
                "fFyYvVaApPrRoOlLdDzhZHeE",
                "jaJA44sSmMiItT''bBjuJU.,",

                "qQwWeErRtTyYuUiIoOpP[{]}",
                "aAsSdDfFgGhHjJkKlL;:'\"",
                "zZxXcCvVbBnNmM,<.>/?"
        };
        for (int i = 0; i < before.length; i++) {
            String actual = translitor.translitText(before[i]);
            assertEquals(expected[i], actual);
        }
    }

    @Test
    public void testTranslitPath() {
        String pathBefore = "D:\\Music\\кувалда\\Кувалда_-_лорелей_mp3davalka.com";
        String pathAfter = "D:\\Music\\кувалда\\Kuvalda_-_lorelej_mp3davalka.com";

        String pathActual = translitor.processPath(pathBefore);
        assertEquals(pathAfter, pathActual);
    }

    @Test
    public void testTranslitPathDoNothing() {
        translitor = new Translitor(TranslitorOperation.NONE);
        String pathBefore = "D:\\Music\\кувалда\\Кувалда_-_лорелей_mp3davalka.com";
        String pathAfter = "D:\\Music\\кувалда\\Кувалда_-_лорелей_mp3davalka.com";

        String pathActual = translitor.processPath(pathBefore);
        assertEquals(pathAfter, pathActual);
    }
}