import java.util.HashMap;
import java.util.Map;

/**
 * Created by seroga on 03-Jan-16.
 */
public class Translitor {

    private final TranslitorOperation operation;
    private final Map<String, String> alphabet = new HashMap<>(33);

    public Translitor(TranslitorOperation operation) {
        this.operation = operation;
        init();
    }

    private void init() {
        alphabet.put("а", "a");
        alphabet.put("б", "b");
        alphabet.put("в", "v");
        alphabet.put("г", "g");
        alphabet.put("д", "d");
        alphabet.put("е", "e");
        alphabet.put("ё", "jo");

        alphabet.put("ж", "zh");
        alphabet.put("з", "z");
        alphabet.put("и", "i");
        alphabet.put("й", "j");
        alphabet.put("к", "k");
        alphabet.put("л", "l");
        alphabet.put("м", "m");

        alphabet.put("н", "n");
        alphabet.put("о", "o");
        alphabet.put("п", "p");
        alphabet.put("р", "r");
        alphabet.put("с", "s");
        alphabet.put("т", "t");
        alphabet.put("у", "u");

        alphabet.put("ф", "f");
        alphabet.put("х", "h");
        alphabet.put("ц", "c");
        alphabet.put("ч", "4");
        alphabet.put("ш", "sh");
        alphabet.put("щ", "w");
        alphabet.put("ъ", "^");

        alphabet.put("ы", "y");
        alphabet.put("ь", "'");
        alphabet.put("э", "e");
        alphabet.put("ю", "ju");
        alphabet.put("я", "ja");
    }

    public String translitText(String text) {
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            String currentCharStr = String.valueOf(currentChar);

            if (alphabet.containsKey(currentCharStr) || alphabet.containsKey(currentCharStr.toLowerCase())) {

                if (Character.isLowerCase(currentChar)) {
                    String engRepresentation = alphabet.get(currentCharStr);
                    result.append(engRepresentation);

                } else if (Character.isUpperCase(currentChar)) {
                    char curCharLowercase = Character.toLowerCase(currentChar);
                    String curCharLowerCaseStr = String.valueOf(curCharLowercase);
                    String engRepresentationStr = alphabet.get(curCharLowerCaseStr);
                    String engRepresentationUpperStr = engRepresentationStr.toUpperCase();
                    result.append(String.valueOf(engRepresentationUpperStr));
                }

            } else {
                result.append(currentCharStr);
            }
        }
        return result.toString();
    }

    private String translitPath(String path) {
        int index = path.lastIndexOf('\\');
        String pathToSong = path.substring(0, index);
        String song = path.substring(index);
        return pathToSong + translitText(song);
    }

    public String processPath(String path) {
        switch (operation) {
            case NONE:
                return path;
            case TRANSLIT:
                return translitPath(path);
            default:
                throw new RuntimeException("Unrecognised translitor operation: " + operation);
        }
    }

}
