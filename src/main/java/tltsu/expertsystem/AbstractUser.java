package tltsu.expertsystem;

import org.apache.log4j.Logger;
import tltsu.expertsystem.answeranalyzer.TestResult;
import tltsu.expertsystem.fsm.Event;
import tltsu.expertsystem.ui.LectureHTML;
import tltsu.expertsystem.utils.SerializeException;
import tltsu.expertsystem.utils.Utils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author FADEEV
 */
public abstract class AbstractUser implements Serializable
{
    private static final Logger log = Logger.getLogger(AbstractUser.class);
    private static final long serialVersionUID = 3778333413425545224L;

    protected LinkedList<Integer> chapters;
    protected LinkedList<Integer> levels;

    protected LinkedList<Integer> chapterResults; // IN procent
    protected ChapterTest chapterTest;
    protected String course;
    private int eduStep;
    
    public final String userName;

    public AbstractUser(String pathToXml, String name)
    {
        userName = name; // TODO сделать какойнитьбудь поиск по уже существующим пользователям
        eduStep = 0;
        chapters = new LinkedList<Integer>();
        levels = new LinkedList<Integer>();
        chapterResults = new LinkedList<Integer>();
        setCourse(pathToXml);
        chapters.addLast(0);
        levels.addLast(0);
    }

    public int getEduStep()
    {
        return eduStep;
    }

    public String getCourse()
    {
        return course;
    }

    public void setCourse(String course)
    {
        this.course = course;
    }

    public abstract void changeEduState(String state);

    public abstract Event calculateEduState(); //TODO придумать умную вычислялку состояния для юзера

    public abstract boolean isEduComplete(); //TODO придумать умную вычислялку состояния для юзера


    public void processChapterTestResult(ChapterTest chapterTest)
    {   //  тут нахождение средней результатов теста : находим среднее по всем групповым тестам.
        Double partResult = 0.0;
        for (int part = 0; part < chapterTest.getCurrentPart(); part++)
        {
            partResult += chapterTest.getPartResult(part);
            log.info("step"+part+"partResult increased = "+partResult);
        }

        partResult /= chapterTest.getCurrentPart();

        if (partResult > 90)
            chapterResults.addLast(TestResult.EXCELLENT);
        else if (partResult > 60)
            chapterResults.addLast(TestResult.NORMAL);
        else
            chapterResults.addLast(TestResult.BAD);

        log.info("set Current result = "+ chapterResults.getLast()+ " where % ~= "+ partResult);
    }

    public void teach() throws Exception
    {
        log.debug("now teach! material from chapter#" + chapters.getLast() + " with level#" + levels.getLast());
//        String path = getCourse().replaceFirst("\\?", String.valueOf( getCurrentLevel()));
//        path = path.replaceFirst("\\?", String.valueOf(chapters.getLast()));
//        URL url = this.getClass().getResource(path);
        URL url = Utils.getUrlByPattern("material/"+getCourse(), levels.getLast(), chapters.getLast());

        try
        {
            LectureHTML dialog = new LectureHTML("C sharp lection#"+ chapters.getLast(), url);
            dialog.showLecture();
        }
        catch (Exception e)
        {
            log.error("error while show lecture! where URL="+ url + ". Rollback edu. ", e);
            levels.remove(levels.size()-1);
            chapters.remove(chapters.size()-1);
            throw e;
        }

    }

    public void testCurrentChapter() throws Exception
    {
//        if (chapterTest != null)
//            throw new RuntimeException("Why chaptertest are keep saved ? this instance will be nullable in finish work method test");

        chapterTest = new ChapterTest(getCourse(), chapters.getLast(), levels.getLast());
        while (!chapterTest.isTestComplete()) // ходим по всем частям
        {
            chapterTest.nextPart();
            chapterTest.testCurrPart();      // в testCurrPart ходим по вопросам пока не ответим. там же считаем результат.
            
            // SHOW error in userAnswer if exist
//            chapterTest.mistakesWorking();
        }

        processChapterTestResult(chapterTest);
        eduStep++;
    }


    /**
     * serialize to project directory in file "&lt;userName&gt;.usf"
     */
    public void save() throws SerializeException // saved to project directory
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(userName + ".usf");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            log.trace("Successfully serialize user:"+getInfo()+" to " + userName + ".usf");
        }
        catch (IOException e)
        {
            log.error("Some error while serialize user", e);
            throw new SerializeException("cant save user "+userName, e);
        }
    }

    /**
     * Deserialize from project directory file "&lt;userName&gt;.usf"
     * @param userName users name searched in directory by him
     * @return instanse of deserialized object
     */
    public static AbstractUser load(String userName) throws SerializeException
    {
        try
        {
            FileInputStream fis = new FileInputStream(userName + ".usf");
            ObjectInputStream oin = new ObjectInputStream(fis);
            return (AbstractUser) oin.readObject();
        }
        catch (Exception e)
        {
            log.error("Some error while deserialize user", e);
            throw new SerializeException("Can't load user "+userName, e);
        }
    }

    public String getInfo()
    {
        return "Name-"+userName+" Chapters:"+chapters +" Levels:" + levels + "  Results"+ chapterResults + " Course" + course +
               " Edu step"+ eduStep ;
    }

}
