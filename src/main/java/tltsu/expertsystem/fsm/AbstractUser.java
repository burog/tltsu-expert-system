package tltsu.expertsystem.fsm;

import org.apache.log4j.Logger;
import tltsu.expertsystem.answeranalyzer.TestResult;
import tltsu.expertsystem.ui.LectureHTML;
import tltsu.expertsystem.utils.SerializeException;
import tltsu.expertsystem.utils.Utils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author FADEEV
 */
public abstract class AbstractUser implements Serializable
{
    private static final long serialVersionUID = 3778333413425545224L;

    protected ArrayList<Integer> chapters;
    protected ArrayList<Integer> levels;
    protected ArrayList<Integer> results; // IN procent

    static ChapterTest chapterTest;
    protected String course;
    private static final Logger log = Logger.getLogger(AbstractUser.class);
    private int eduStep;
    
    public final String userName;

    public AbstractUser(String pathToXml, String name)
    {
        userName = name; // TODO сделать какойнитьбудь поиск по уже существующим пользователям
        eduStep = 0;
        chapters = new ArrayList<Integer>();
        levels = new ArrayList<Integer>();
        results = new ArrayList<Integer>();
        setCourse(pathToXml);
        setChapter(0);
        setLevel(0);
    }

    public int getEduStep()
    {
        return eduStep;
    }

    public int getCurrentChapter()
    {
        return chapters.get(chapters.size() - 1);
    }

    public int getChapter(int step)
    {
        return chapters.get(step);
    }

    public void setChapter(int chapter)
    {
        this.chapters.add(this.chapters.size(), chapter );
    }

    public int getCurrentResult()
    {
        return results.get(results.size() - 1);
    }

    public int getResult(int step)
    {
        return results.get(step);
    }

    public void setResult(int result)
    {
        this.results.add(result);
    }

    public int getCurrentLevel()
    {
        return levels.get(levels.size() - 1);
    }

    public int getLevel(int step)
    {
        return levels.get(step);
    }

    public void setLevel(int level)
    {
        this.levels.add(this.levels.size(), level );
    }

    public String getCourse()
    {
        return course;
    }

    public void setCourse(String course)
    {
        this.course = course;
    }


    public void processEduState(String state)
    {
        changeEduState(state);
    }

    protected abstract void changeEduState(String state);

    public abstract String calculateEduState(); //TODO придумать умную вычислялку состояния для юзера

    public abstract boolean isEduComplete(); //TODO придумать умную вычислялку состояния для юзера


    public void processChapterTestResult(ChapterTest chapterTest)
    {
//        results.add(chapterTest.get)
        this.chapterTest = null;
        // TODO  shange STUB написать слчиталку исходя из . помоему тут долджна быть средняя рзультатов тста
        setResult( TestResult.EXCELLENT_RESULT);
    }

    public void teach() throws Exception
    {
        log.info("this is material from chapter#" + getCurrentChapter() + " with level#" + getCurrentLevel());
//        String path = getCourse().replaceFirst("\\?", String.valueOf( getCurrentLevel()));
//        path = path.replaceFirst("\\?", String.valueOf(getCurrentChapter()));
//        URL url = this.getClass().getResource(path);
        URL url = Utils.getUrlByPattern(getCourse(), getCurrentLevel(), getCurrentChapter());

        try
        {
            LectureHTML dialog = new LectureHTML("C sharp lection#"+ getCurrentChapter(), url);
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
        Fsm fsmForTest = new EduStateFSM();

//        if (chapterTest != null)
//            throw new RuntimeException("Why chaptertest are keep saved ? this instance will be nullable in finish work method test");

        chapterTest = new ChapterTest(getCurrentChapter(), getCurrentLevel());
        while (!chapterTest.isTestComplete())
        {
//            String state = chapterTest.calculateTestState();
            fsmForTest.handleEvent(chapterTest.calculateTestState()); // посылаем результат тестирования на автомат (level up/down/hold : theme up/down/hold )
//            String newState = testing.getCurrentState();  // вернёт строку вида "0;1" или ??"0;0;0;1"

            chapterTest.changeTestState(fsmForTest.getCurrentState());
//            testing();
            chapterTest.test();
            
            // SHOW error in userAnswer if exist
            chapterTest.mistakesWorking();
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
            log.trace("Successfully serialize user to " + userName + ".usf");
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
        return "Name-"+userName+" Chapters:"+chapters +" Levels:" + levels + "  Results"+results + " Course" + course +
               " Edu step"+ eduStep ;
    }

}
