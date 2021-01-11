import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class CustomTest {

    @Test
    public void custom() {
        Set<String> set = new HashSet<>();
        set.add("one");
        set.add("two");
        set.add("three");
        set.stream()
                .filter(s -> smth(s) != null)
                .forEach(s -> System.out.println(s));
    }

    public String smth(String string) {
        if (string.equals("two")) {
            return null;
        } else {
            return string;
        }
    }
}
