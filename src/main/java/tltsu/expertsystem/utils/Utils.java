package tltsu.expertsystem.utils;

import org.apache.log4j.Logger;
import tltsu.expertsystem.*;
import tltsu.expertsystem.AbstractUser;
import tltsu.expertsystem.User;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author FADEEV
 */
public class Utils
{
    private static final Logger log = Logger.getLogger(Utils.class);
    public static final String YES = "YES";
    public static final String NO = "NO";

    private static List<Integer> maxQuestionInGroup = new LinkedList<Integer>();

    public static AbstractUser createOrLoadUser(String name, String  needCreate) throws SerializeException
    {
        if (needCreate.equals(YES))
            return createOrLoadUser(name, true);
        else if (needCreate.equals(NO))
            return createOrLoadUser(name, false);
        else
            throw new IllegalArgumentException("Argument needCreate is not correct (YES/NO) = "+needCreate);
    }

    public static AbstractUser createOrLoadUser(String name, boolean needCreate) throws SerializeException
    {
        AbstractUser user;
        if(name.equals("null") || name.isEmpty())
            throw new IllegalArgumentException("User name is empty");
        else if (Pattern.compile("\\W", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE).matcher(name).find())
            throw new IllegalArgumentException("User name not correct = " + name);

        if (needCreate)
        {
            log.debug("your user "+name+"are CREATED!");
            user = new User(EduProperties.COURSE, name);
        }
        else
        {
            user = AbstractUser.load(name);
            log.debug("your user "+name+"are load! continue education with user characteristic"+user.getInfo());
        }
        return user;
    }
    
    /**
     * Replace in patternCourse symbols "?" on arguments (args) step by step.
     * Example patternCourse= "material/sharph_?_?.html", args[0] = "2", args[1] = "hard" and main class located in c:/education/.
     * The invoke of this method return URL with path = file:///C:/education/material/sharph_2_hard.html
     * @param patternCourse a relative (relative from the main class) or absolute path
     * @param args arguments witch be replaced in  patternCourse
     * @return url to file
     */
    public static URL getUrlByPattern(String patternCourse, String... args)
    {
        String resultPath = patternCourse;
        for (String arg : args)
        {
            resultPath = resultPath.replaceFirst("\\?", arg);
        }
        if (resultPath == null)
            throw new IllegalArgumentException("Argument pattern course can't be null.");
        log.trace("resultPath = "+resultPath);
        return Main.class.getResource(resultPath);
    }

    public static InputStream getStreamByPattern(String patternCourse, String... args)
    {
        String resultPath = patternCourse;
        for (String arg : args)
        {
            resultPath = resultPath.replaceFirst("\\?", arg);
        }
        if (resultPath == null)
            throw new IllegalArgumentException("Argument pattern course can't be null.");
        log.trace("resultPath = "+resultPath);
        return Main.class.getResourceAsStream(resultPath);
    }

    /**
     * See {@link #getUrlByPattern(String, String[]) getUrlByPattern}
     */
    public static URL getUrlByPattern(String patternCourse, int... args)
    {
        return getUrlByPattern(patternCourse, convert(args));
    }

    /**
     * Convert int array to string array.
     * @param converted array of int numbers
     * @return array of numbers in text representation
     */
    static String[] convert(int[] converted)
    {
        String[] result = new String[converted.length];
        int i = 0;
        for (int var :converted)
            result[i++] = String.valueOf(var);

        return result;
    }

    /**
     * Return numbers in interval from 1 to {@link  tltsu.expertsystem.EduProperties#MAX_NUMBER_QUESTION_IN_GROUP}
     */
    public static int getNextRandom()
    {
        if (maxQuestionInGroup.isEmpty())
        {
            for(int i=0; i< EduProperties.MAX_NUMBER_QUESTION_IN_GROUP; i++)
                maxQuestionInGroup.add(i);
        }

        Random rand = new Random();
        Integer randVal;
        do
        {
            randVal = rand.nextInt(EduProperties.MAX_NUMBER_QUESTION_IN_GROUP);
        }
        while (!maxQuestionInGroup.contains(randVal));

        maxQuestionInGroup.remove(randVal);
        return randVal;
    }

    public static int getNextRandom(int max)
    {
        if (maxQuestionInGroup.isEmpty())
        {
            for(int i=0; i< max; i++)
                maxQuestionInGroup.add(i);
        }
        Random rand = new Random();
        Integer randVal;
        do
        {
            randVal = rand.nextInt(max);
        }
        while (!maxQuestionInGroup.contains(randVal));

        maxQuestionInGroup.remove(randVal);
        return randVal;
    }


}
