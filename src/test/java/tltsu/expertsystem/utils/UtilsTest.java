package tltsu.expertsystem.utils;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Test;
import tltsu.expertsystem.Main;

import java.io.IOException;
import java.net.URL;

/**
 * @author FADEEV
 */
public class UtilsTest  extends Assert
{
    static {
        BasicConfigurator.configure();
    }

    @Test
    public void testGetUrlByPattern() throws IOException
    {
        String pattern = "material/charph_?_?.htm";
        String chapter = "0";
        String level = "1";
        URL url = Utils.getUrlByPattern(pattern, chapter, level);

        int chapterINT = 0;
        int levelINT = 1;
        URL url2 = Utils.getUrlByPattern(pattern, chapterINT, levelINT);

        String etalonPath = "material/charph_0_1.htm";
        URL etalonURL = Main.class.getResource(etalonPath);

        assertEquals(etalonURL.toExternalForm(), url.toExternalForm());
        assertEquals(etalonURL.toExternalForm(), url2.toExternalForm());

    }


    @Test
    public void testConvertArrays() throws IOException
    {
        String[] expected = new String[] {"1", "2", "2", "4", "2314"};
        int[] tested = new int[] {1, 2, 2, 4, 2314};

        assertArrayEquals(Utils.convert(tested), expected);
    }

}
