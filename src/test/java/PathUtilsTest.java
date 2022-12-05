import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.avwa.utils.PathUtils;
import org.junit.jupiter.api.Test;

public class PathUtilsTest {

    @Test
    public void getPathLevels() {
        String path = "/aa/bb/cc/dd/";
        List<String> levels = PathUtils.getPathLevels(path);
        assertEquals(4, levels.size());
    }

    @Test
    public void accessingOneOfThePaths() {
        String path = "/aa/bb/";
        List<String> paths = Arrays.asList("aa/cc", "aa/dd");
        boolean assertsFalse = PathUtils.accessingOneOfThePaths(path, paths);
        assertFalse(assertsFalse);

        paths = Arrays.asList("aa/cc", "aa/bb/cc");
        boolean assertsTrue = PathUtils.accessingOneOfThePaths(path, paths);
        assertTrue(assertsTrue);
    }
}
