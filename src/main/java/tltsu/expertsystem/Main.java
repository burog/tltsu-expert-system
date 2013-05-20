package tltsu.expertsystem;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import tltsu.expertsystem.answeranalyzer.AnalyzeAnswer;
import tltsu.expertsystem.answeranalyzer.TestResult;
import tltsu.expertsystem.fsm.Event;
import tltsu.expertsystem.fsm.Fsm;
import tltsu.expertsystem.ui.*;
import tltsu.expertsystem.utils.SerializeException;
import tltsu.expertsystem.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.LinkedList;

/**
 * @author FADEEV
 */
public class Main
{
//    static String states = "C:\\Users\\fadeev\\IdeaProjects\\akerfsm\\es\\src\\generate\\configs\\states.xml";
//    static String events = "C:\\Users\\fadeev\\IdeaProjects\\akerfsm\\es\\src\\generate\\configs\\events.xml";
//    static String fsm = "C:\\Users\\fadeev\\IdeaProjects\\akerfsm\\es\\src\\generate\\configs\\FSM.xml";

    static private final int INDEX_NAME = 0;
    static private final int INDEX_NEED_CREATE = 1;

    static
    {
        String defaultLogConfig = "log4j.properties";

        File fis = new File(defaultLogConfig);
        if (fis.exists())
            PropertyConfigurator.configure(defaultLogConfig);
        else
            BasicConfigurator.configure();
    }


    private static final Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) throws Exception
    {


//                String javaSource = BuilderFSM.buildFsm(args[0], args[1], args[2], PACKAGE_NAME, "fsm0_0");
//                System.out.println(javaSource);

//        prepareAndRunEDU();

        TestQuestion question = new InputQuestion("Найти производную функция, f(x)= (4-4*x)*4*x", "");
        question.showLecture();

        TestResult questionResult = AnalyzeAnswer.analyzeFormula("2*x+y^2", "2^y+2*x");
        log.info(questionResult);


        new SimpleLecture("Correction of mistakes", questionResult.error).showLecture();
        
//        ConfigurationOfChapterTest configurationOfChapterTest = ConfigurationOfChapterTest.load("charph_?_?", 0, 1);
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(0, 0));
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(0, 1));
//
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(1, 0));
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(1, 1));
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(1, 2));
//
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(2, 0));
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(2, 1));
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(2, 2));
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(2, 3));
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(2, 4));
//
//
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(3, 0));
//        log.info(configurationOfChapterTest.getQuestionWithAnswer(3, 1));


//        configurationOfChapterTest.addPair("Решите уравнение: \nу=2*8+4", "20", 0);
//        configurationOfChapterTest.addPair("Решите уравнение: \nу=2*8+5", "21", 0);
//
//        configurationOfChapterTest.addPair("Решите уравнение: \nу=2*8+6", "22", 1);
//        configurationOfChapterTest.addPair("Решите уравнение: \nу=2*8+7", "23", 1);
//        configurationOfChapterTest.addPair("Решите уравнение: \nу=2*8+8", "24", 1);
//
//        configurationOfChapterTest.addPair("Решите уравнение: \nу=x*8+2*x", "10*x", 2);
//        configurationOfChapterTest.addPair("Решите уравнение: \nу=x*8+x+x+x+x", "12*x", 2);
//        configurationOfChapterTest.addPair("Решите уравнение: \nу=x*8+x+x", "10*x", 2);
//        configurationOfChapterTest.addPair("Решите уравнение: \nу=x*3+9*x", "12*x", 2);
//        configurationOfChapterTest.addPair("Решите уравнение: \nу=x*6+4*x", "10*x", 2);
//
//        configurationOfChapterTest.addPair("Решите уравнение Чему равно х: x=y*3+9*y", "12*y", 3);
//        configurationOfChapterTest.addPair("Решите уравнение Чему равно х: \nx=y*6+4*y", "10*y", 3);

//        configurationOfChapterTest.save("charph_?_?", 0, 1);




        //        Fsm fsm = m.newFunction(args[0], args[1], args[2]);
//        Fsm fsm = new FsmBuilder().newFsm(args[0], args[1], args[2]);
    }



    static void prepareAndRunEDU()
    {
        log.trace("Now start EDU System");
        AbstractQuestion needCreateUser = new StartWindow();
        needCreateUser.showLecture();
        AbstractUser user;

        String[] userInput = needCreateUser.getUserAnswer().split("#");
        log.trace("witch user and are need create user? " + needCreateUser.getUserAnswer());
        try
        {
            user = Utils.createOrLoadUser(userInput[INDEX_NAME], userInput[INDEX_NEED_CREATE]);
        }
        catch (SerializeException e)
        {
            log.error("Error while create/load user, is need create "+userInput[INDEX_NEED_CREATE] + " with name = "+userInput[INDEX_NAME],e);
            AbstractDialog error = new SimpleLecture("Error", e.getMessage()+"\nEducation is over");
            error.setResizable(false);
            error.showLecture();
            return;
        }


        try
        {
            EduSystem.runEdu(user);
        }
        catch (Exception e)
        {
            log.error("Some error.", e);
        }
        finally
        {
            log.info("Finally try save user");

            try
            {
                user.save();
            }
            catch (SerializeException e)
            {
                log.error("Can't save user");
                AbstractDialog error = new SimpleLecture("Error", "Can't save user.");
                error.setResizable(false);
                error.showLecture();
            }
        }

    }

    static void testSimpleFsm(Fsm a)
    {
//        System.out.println("Test FirstTokenPrintFSM");
        //        Fsm a = new FirstTokenPrintFSM();
        //        FSM<FirstTokenPrintFSM.A, String> a = new FirstTokenPrintFSM();

        String testStr = "\n000000000 000000 000000 000 000  \n 00000  0000000 000000000 0000 \n0";
        for (int i = 0; i < testStr.length(); i++)
        {
            if (testStr.charAt(i) == '0')
                a.handleEvent(new Event(0,Integer.parseInt(String.valueOf(testStr.charAt(i))),0));
            else if (testStr.charAt(i) == '\n')
                a.handleEvent(new Event(0, -10, 0));
            else
                a.handleEvent(new Event(0, 10, 0));
        }
    }

    @Deprecated
    static void getEduMaterial (AbstractUser user)
    {
        log.debug("this is material from chapter#" + user.chapters.getLast() + " with level#" + user.levels.getLast());
        String path = user.getCourse().replaceFirst("\\?", String.valueOf( user.levels.getLast()));
        path = path.replaceFirst("\\?", String.valueOf( user.chapters.getLast()));

        URL url = Main.class.getResource(path);
        try
        {
            LectureHTML dialog = new LectureHTML("C sharp lection#"+ user.chapters.getLast(), url);
            dialog.showLecture();
        }
        catch (Exception e)
        {
            log.error("error while read from console!");
        }
    }

    public static void testGenerateRandInGroup()
    {
        for (int i =0; i< EduProperties.MAX_NUMBER_QUESTION_IN_GROUP * 3 +2; i++)
        {
            if (i % 7==0)
            {
                log.info("NEXT group");
            }
            log.info(Utils.getNextRandom());
        }

    }

    public static void testLogEduProperties()
    {
        log.error("MAX_CHAPTER int ="+EduProperties.MAX_CHAPTER);
        log.error("TEACHER_NAME int ="+EduProperties.TEACHER_NAME);
        log.error("MAX_LEVEL int ="+EduProperties.MAX_LEVEL);
        log.error("NORMAL_TEST_TIME int ="+EduProperties.NORMAL_TEST_TIME);
        log.error("NORMAL_TEACH_TIME int ="+EduProperties.NORMAL_TEACH_TIME);
        log.error("MAX_NUMBER_QUESTION_IN_GROUP int ="+EduProperties.MAX_NUMBER_QUESTION_IN_GROUP);
    }

    @Deprecated
    static void testUser (AbstractUser user)
    {
        log.info("this is test for  chapter#" + user.chapters.getLast() + " with level#" + user.levels.getLast());

        try
        {
            SimpleLecture dialog = new SimpleLecture("C sharp test#"+ user.chapters.getLast(), "this is test #" + user.chapters.getLast() + " in level "+ user.levels.getLast());
            dialog.showLecture();
        }
        catch (Exception e)
        {
            log.error("error while read from console!", e);
        }
    }

}
