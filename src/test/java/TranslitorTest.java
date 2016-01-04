import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by seroga on 03-Jan-16.
 */
public class TranslitorTest {

    @Test
    public void testTranslit() throws Exception {
        String[] before = new String[] {
                "№~`1!2@3#4$5%6^7&8*9(0)-_=+/|\\",
                "йЙцЦуУкКеЕнНгГшШщЩзЗхХъЪ",
                "фФыЫвВаАпПрРоОлЛдДжЖэЭ",
                "яЯчЧсСмМиИтТьЬбБюЮ.,",

                "qQwWeErRtTyYuUiIoOpP[{]}",
                "aAsSdDfFgGhHjJkKlL;:'\"",
                "zZxXcCvVbBnNmM,<.>/?"
        };
        String[] expected = new String[] {
                "№~`1!2@3#4$5%6^7&8*9(0)-_=+/|\\",
                "jJcCuUkKeEnNgGshSHwWzZhH^^",
                "fFyYvVaApPrRoOlLdDzhZHeE",
                "jaJA44sSmMiItT''bBjuJU.,",

                "qQwWeErRtTyYuUiIoOpP[{]}",
                "aAsSdDfFgGhHjJkKlL;:'\"",
                "zZxXcCvVbBnNmM,<.>/?"
        };
        for (int i = 0; i < before.length; i++) {
            String actual = Translitor.translit(before[i]);
            Assert.assertEquals(expected[i], actual);
        }

    }
}