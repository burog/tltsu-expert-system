package tltsu.expertsystem.ui;

import org.apache.log4j.Logger;

import java.awt.*;

/**
 * @author FADEEV
 */
public abstract class AbstractQuestion extends AbstractDialog
{
    private static final long serialVersionUID = 5374227222366493600L;
    private static final Logger log = Logger.getLogger(AbstractQuestion.class);
    protected String userAnswer;
    protected String correctAnswer;

    protected AbstractQuestion(String title, String correctAnswer) throws HeadlessException
    {
        super(title);
        this.correctAnswer = correctAnswer;
    }

    protected AbstractQuestion(String title) throws HeadlessException
    {
        super(title);
    }

    public String getUserAnswer()
    {
        return userAnswer;
    }

    protected abstract void saveAnswer();

    protected void close()
    {
        saveAnswer();
        super.close();
        log.error("invoke Close() userAnswer="+userAnswer+" correctAnswer="+correctAnswer+" isExit="+isExit);
    }
}
