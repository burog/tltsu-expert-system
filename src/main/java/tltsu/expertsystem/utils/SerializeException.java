package tltsu.expertsystem.utils;

/**
 * @author FADEEV
 */
public class SerializeException extends Exception
{
    private static final long serialVersionUID = 821184313224051938L;

    public SerializeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SerializeException(String message)
    {
        super(message);
    }
}
