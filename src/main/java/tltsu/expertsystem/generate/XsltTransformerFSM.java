package tltsu.expertsystem.generate;

import org.apache.log4j.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * @author FADEEV
 */
public class XsltTransformerFSM
{
    private static XsltTransformerFSM ourInstance = new XsltTransformerFSM();
    private static final Logger log = Logger.getLogger(XsltTransformerFSM.class);
    private TransformerFactory tFactory;

    public static XsltTransformerFSM getInstance()
    {
        return ourInstance;
    }

    private XsltTransformerFSM()
    {
        tFactory = TransformerFactory.newInstance();
    }

    public void transform(OutputStream result, InputStream source, InputStream style) throws TransformerException
    {
        Transformer transformer = tFactory.newTransformer(new StreamSource(style));
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new StreamSource(source), new StreamResult(result));
    }

    public String transform(String source, String style) throws TransformerException, UnsupportedEncodingException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try
        {
            InputStream xml = new ByteArrayInputStream(source.getBytes("UTF-8"));
            InputStream xslt = new ByteArrayInputStream(style.getBytes("UTF-8"));
            transform(result, xml, xslt);
        }
        catch (TransformerException e)
        {
            log.error("Transform  error while processing FSM", e);
            throw e;
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("Transform  error while processing FSM", e);
            throw e;
        }
        return result.toString();
    }

    public String transform(InputStream source, InputStream style)
            throws UnsupportedEncodingException, TransformerException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try
        {
            transform(result, source, style);
        }
        catch (TransformerException e)
        {
            log.error("Transform  error while processing FSM", e);
            throw e;
        }
        return result.toString();
    }
}
