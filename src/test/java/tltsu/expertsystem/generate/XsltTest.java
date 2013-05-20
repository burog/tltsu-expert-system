package tltsu.expertsystem.generate;

import org.junit.Assert;
import org.junit.Test;
import tltsu.expertsystem.fsm.XsltTransformer;

import java.io.*;

/**
 * @author FADEEV
 */
public class XsltTest extends Assert
{
    @Test
    public void testStatesViaXlst() throws Throwable
    {
        String result = XsltTransformer.getInstance()
                .transform(getResource("xsl/states.xml"), getResource("xsl/states.xslt"));
        assertEquals(getStringResource("xsl/statesResult.txt"), result);
    }

    @Test
    public void testEventsViaXlst() throws Throwable
    {
        String result = XsltTransformer.getInstance()
                .transform(getResource("xsl/events.xml"), getResource("xsl/events.xslt"));
        assertEquals(getStringResource("xsl/eventsResult.txt"), result);
    }

    @Test
    public void testTransitionsViaXlst() throws Throwable
    {
        String result = XsltTransformer.getInstance()
                .transform(getResource("xsl/fsm.xml"), getResource("xsl/fsm.xslt"));
        assertEquals(getStringResource("xsl/fsmResult.txt"), result);
    }

    private InputStream getResource(String resource)
    {
        return XsltTest.class.getResourceAsStream(resource);
    }

    private String getStringResource(String resource) throws IOException
    {
        InputStream is = XsltTest.class.getResourceAsStream(resource);

        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        return new String(bytes);
    }


}
