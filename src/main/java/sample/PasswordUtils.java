package sample;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.IntPredicate;

public class PasswordUtils {
    public static final String LOW = "Легкий";
    public static final String MEDIUM = "Средний";
    public static final String HARD = "Сложный";
    public static final int MAX_PSWD_COMPLEXITY = 6;

    public static NavigableMap<Integer, String> complexityMap = new TreeMap() {{
        put(0, LOW);
        put(3, MEDIUM);
        put(6, HARD);
    }};

    public static int passwordComplexity(String password) {
        return password.isEmpty() ? 0 :
                (password.length() >= 6 ? 2 : 1) +
                        (containsDigit(password) ? 1 : 0) +
                        (containsLowerCase(password) ? 1 : 0) +
                        (containsUpperCase(password) ? 1 : 0) +
                        (containsSpecChar(password) ? 1 : 0);

    }

    private static boolean containsLowerCase(String value) {
        return contains(value, i -> Character.isLetter(i) && Character.isLowerCase(i));
    }

    private static boolean containsUpperCase(String value) {
        return contains(value, i -> Character.isLetter(i) && Character.isUpperCase(i));
    }

    private static boolean containsDigit(String value) {
        return contains(value, Character::isDigit);
    }

    private static boolean containsSpecChar(String value) {
        return contains(value, i -> !Character.isLetterOrDigit(i));
    }

    private static boolean contains(String value, IntPredicate predicate) {
        return value.chars().anyMatch(predicate);
    }

    public static double expectation(List<Long> list) {
        return list.stream().mapToLong(Long::longValue).sum() / list.size();
    }

    public static double dispersion(List<Long> list) {
        double expectation = expectation(list);
        return list.stream()
                .mapToDouble(x -> (x - expectation) * (x - expectation))
                .sum() / list.size();
    }
}
