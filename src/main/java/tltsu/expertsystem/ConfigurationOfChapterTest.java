package tltsu.expertsystem;

import org.apache.log4j.Logger;
import tltsu.expertsystem.utils.SerializeException;
import tltsu.expertsystem.utils.Utils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FADEEV
 */
public class ConfigurationOfChapterTest implements Serializable
{
    private static final long serialVersionUID = -7104947342610224018L;
    private static final Logger log = Logger.getLogger(Utils.class);

    private List<List<Pair>> parts = new ArrayList<List<Pair>>();
    
    public Pair getQuestionWithAnswer(int part, int question)
    {
        return parts.get(part).get(question);
    }
    
    public int getCountOfParts()
    {
        return parts.size();
    }

    public int getCountOfQuestion(int part)
    {
        return parts.get(part).size();
    }

    public void addPair(String question, String answer, int part)
    {
        List<Pair> currentPart;
        try
        {
            currentPart = parts.get(part);
        }
        catch (IndexOutOfBoundsException e)
        {
            currentPart = new ArrayList<Pair>();
            parts.add(part, currentPart);
        }
        currentPart.add(new Pair(question, answer));
    }


    public static ConfigurationOfChapterTest load(String course, int level, int chapter) throws SerializeException
    {
        try
        {
//            URL path = Utils.getUrlByPattern(course, level, chapter);
            String path = course.substring(0, course.indexOf("."));
            path = path.replaceFirst("\\?", String.valueOf(level)).replaceFirst("\\?", String.valueOf(chapter)) + ".cct";

            FileInputStream fis = new FileInputStream(new File(path));
            ObjectInputStream oin = new ObjectInputStream(fis);
            return (ConfigurationOfChapterTest) oin.readObject();
        }
        catch (Exception e)
        {
            log.error("Some error while deserialize config of chapter test : course ="+course+" level ="+level+ " chapter=" +chapter, e);
            throw new SerializeException("Can't load config of chapter test", e);
        }
    }

    public void save(String course, int level, int chapter) throws SerializeException // saved to project directory
    {
        try
        {
            String path = course.substring(0, course.indexOf("."));
            path = path.replaceFirst("\\?", String.valueOf(level)).replaceFirst("\\?", String.valueOf(chapter)) + ".cct";

            FileOutputStream fos = new FileOutputStream( new File(path));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            log.trace("Successfully serialize config of chapter test  " +path+ ".usf");
        }
        catch (Exception e)
        {
            log.error("Some error while deserialize config of chapter test : course ="+course+" level ="+level+ " chapter=" +chapter, e);
            throw new SerializeException("cant save config of chapter test ", e);
        }
    }

    public class Pair implements Serializable
    {
        private static final long serialVersionUID = -5833210109861521414L;
        final public String question;
        final public String answer;

        private Pair(String question, String answer)
        {
            this.question = question;
            this.answer = answer;
        }
        
        public String toString()
        {
            return question+"#"+answer;
        }
    }
}
