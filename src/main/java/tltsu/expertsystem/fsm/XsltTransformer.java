package tltsu.expertsystem.fsm;

import org.apache.log4j.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author FADEEV
 */
public class XsltTransformer
{
    private static final Logger log = Logger.getLogger(XsltTransformer.class);
    private static XsltTransformer ourInstance = new XsltTransformer();

    TransformerFactory tFactory;

    public static XsltTransformer getInstance()
    {
        return ourInstance;
    }

    private XsltTransformer()
    {
        tFactory = TransformerFactory.newInstance();
    }

    public String transform(String source, String style) throws Throwable
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try
        {
            InputStream xml = new ByteArrayInputStream(source.getBytes("UTF-8"));
            InputStream xslt = new ByteArrayInputStream(style.getBytes("UTF-8"));
            transform(result, xml, xslt);
        }
        catch (Throwable t)
        {
            log.error("Transform  error while processing FSM", t);
            throw t;
        }
        return result.toString();
    }

    public String transform(InputStream source, InputStream style) throws Throwable
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try
        {
            transform(result, source, style);
        }
        catch (Throwable t)
        {
            log.error("Transform  error while processing FSM",t);
            throw t;
        }
        return result.toString();
    }

    public void transform(OutputStream result, InputStream source, InputStream style) throws TransformerException
    {
        Transformer transformer = tFactory.newTransformer(new StreamSource(style));
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new StreamSource(source), new StreamResult(result));
    }
}
