package tltsu.expertsystem;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * @author FADEEV
 */
public class EduProperties
{

    private static final String MAX_CHAPTER_STR = "MAX_CHAPTER";
    private static final String MAX_LEVEL_STR = "MAX_LEVEL";
    private static final String NORMAL_TEACH_TIME_STR = "NORMAL_TEACH_TIME";
    private static final String NORMAL_TEST_TIME_STR = "NORMAL_TEST_TIME";
    private static final String TEACHER_NAME_STR = "TEACHER_NAME";
    private static final String COURSE_STR = "COURSE";
    private static final String MAX_NUMBER_QUESTION_IN_GROUP_STR = "MAX_NUMBER_QUESTION_IN_GROUP_STR";

    private static final Logger log = Logger.getLogger(EduProperties.class);

    private static Properties properties;

    static
    {
        properties = new Properties();

        try
        {
            InputStream os = new DataInputStream(new FileInputStream("global.property"));
            properties.loadFromXML(os);
            log.debug("Successfully loaded configuration");
            testLogEduProperties();
        }
        catch (Exception e)
        {
            log.error("Can't load configuration, use default.", e);
            properties.setProperty(MAX_CHAPTER_STR, "10");
            properties.setProperty(MAX_LEVEL_STR, "2");
            properties.setProperty(NORMAL_TEACH_TIME_STR, "30");
            properties.setProperty(NORMAL_TEST_TIME_STR, "10");
            properties.setProperty(MAX_NUMBER_QUESTION_IN_GROUP_STR, "4");
            properties.setProperty(COURSE_STR, "diff_?_?.htm");

            try
            {
                OutputStream os = new DataOutputStream(new FileOutputStream("global.property"));
                properties.storeToXML(os, null);
            }
            catch (Exception e1)
            {
                log.error("Can't save configuration, in next run again created default properties.", e);
            }
        }
    }

    public static final int MAX_CHAPTER = Integer.parseInt((String) properties.get(MAX_CHAPTER_STR)) ;
    public static final int MAX_LEVEL = Integer.parseInt((String) properties.get(MAX_LEVEL_STR));
    public static final int MIN_LEVEL = 0;
    public static final int NORMAL_TEACH_TIME = Integer.parseInt((String) properties.get(NORMAL_TEACH_TIME_STR));
    public static final int NORMAL_TEST_TIME = Integer.parseInt((String) properties.get(NORMAL_TEST_TIME_STR)) * 1000 * 60;
    public static final int MAX_NUMBER_QUESTION_IN_GROUP = Integer.parseInt((String) properties.get(MAX_NUMBER_QUESTION_IN_GROUP_STR));
    public static final String COURSE =(String) properties.get(COURSE_STR) ;
    public static final String TEACHER_NAME =(String) properties.get(TEACHER_NAME_STR) ;

    public static void testLogEduProperties()
    {
        log.error("MAX_CHAPTER int ="+EduProperties.MAX_CHAPTER);
        log.error("MAX_LEVEL int ="+EduProperties.MAX_LEVEL);
        log.error("MIN_LEVEL int ="+EduProperties.MIN_LEVEL);
        log.error("NORMAL_TEACH_TIME int ="+EduProperties.NORMAL_TEACH_TIME);
        log.error("NORMAL_TEST_TIME int ="+EduProperties.NORMAL_TEST_TIME);
        log.error("MAX_NUMBER_QUESTION_IN_GROUP int ="+EduProperties.MAX_NUMBER_QUESTION_IN_GROUP);
        log.error("COURSE  ="+EduProperties.COURSE);
        log.error("TEACHER_NAME int ="+EduProperties.TEACHER_NAME);
    }
}