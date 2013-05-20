package tltsu.expertsystem.answeranalyzer;

import org.apache.log4j.Logger;

/**
 * @author FADEEV
 */
public class Answer
{
    private static final Logger log = Logger.getLogger(Answer.class);
    String error;
    boolean haveError;

    public Answer(String error)
    {
        this.error = error;
        haveError = !(error == null || error.isEmpty());
    }

    public Answer(Throwable error)
    {
        if (error != null)
        {
            this.error = "User input not correctly mathematical expression";
            haveError = true;
        }
        else
            log.error("not correctly using API");
    }

    public Answer()
    {
        haveError = false;
    }

    public String toString()
    {
        return haveError ? " Error is "+error : "Have no error";
    }
}
