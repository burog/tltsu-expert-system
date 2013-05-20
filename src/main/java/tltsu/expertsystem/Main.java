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


        EduSystem.prepareAndRunEDU();
//
//        TestQuestion question = new InputQuestion("Найти производную функция, f(x)= (4-4*x)*4*x", "");
        /*ConfigurationOfChapterTest cct = ConfigurationOfChapterTest.load("diff_?_?.htm",0,1);
        for (int i = 0; i <= 1; i++)
        {
            for (int j = 0;j <= 2; j++)
            {
                ConfigurationOfChapterTest.Pair pair = cct.getQuestionWithAnswer(i, j);
                TestQuestion question = new InputQuestion(pair.question, pair.answer);
                question.showLecture();
                log.debug(question.getTestResult());
            }
        }*/

//        TestResult questionResult = AnalyzeAnswer.analyzeFormula("2*x+y^2", "2^y+2*x");
//        log.info(questionResult);
//        new SimpleLecture("Correction of mistakes", questionResult.error).showLecture();
//
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


        /*ConfigurationOfChapterTest configurationOfChapterTest = new ConfigurationOfChapterTest();

        configurationOfChapterTest.addPair("Разложите многочлен на множители: \nx^3 + x^2", "x^2 * (x+1)", 0);
        configurationOfChapterTest.addPair("Разложите многочлен на множители: \n4*x^3+6*x^2 ", "2*x^2 * ( 2*x+3 ) ", 0);
        configurationOfChapterTest.addPair("Разложите многочлен на множители: \n3x+6y", "3*(x+2*y)", 0);

        configurationOfChapterTest.addPair("Найдите производную функции : \nf(x)=2*cos(x) - 4*x^2", "-2*sin(x) - 8 *x", 1);
        configurationOfChapterTest.addPair("Найдите производную функции : \nf(x)=x^3 + 3*x^2 - 72*x + 90", "3*x^2 + 6*x -72", 1);
        configurationOfChapterTest.addPair("Найдите производную функции : \nf(x)=-3/x", "2/x^2", 1);

        configurationOfChapterTest.save("diff_?_?.ccf", 0, 1);*/





        //        Fsm fsm = m.newFunction(args[0], args[1], args[2]);
//        Fsm fsm = new FsmBuilder().newFsm(args[0], args[1], args[2]);
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
