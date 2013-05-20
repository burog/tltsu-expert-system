package tltsu.expertsystem;

import java.util.LinkedList;

import org.apache.log4j.Logger;
import tltsu.expertsystem.answeranalyzer.TestResult;
import tltsu.expertsystem.fsm.Event;

/**
 * @author FADEEV
 */
public class MiddleUser extends AbstractUser
{
    private static final Logger log = Logger.getLogger(MiddleUser.class);
    private static final long serialVersionUID = 3957424036423506327L;
//    private ArrayList<Long> teachTime;
    private LinkedList<Long> testTimes;

    public MiddleUser(String pathToMaterial, String name)
    {
        super(pathToMaterial, name);
//        teachTime = new ArrayList<Long>();
        testTimes = new LinkedList<Long>();
    }



    @Override
    public void changeEduState(String state)
    {
        String[] characteristic = state.split(";");

        int resultLevel = Integer.parseInt(characteristic[0]) + levels.getLast();
        if (resultLevel>EduProperties.MAX_LEVEL)
        {
            log.debug("resultLevel is too big ("+resultLevel+") for this configuration, use maximum level = "+EduProperties.MAX_LEVEL);
            resultLevel = EduProperties.MAX_LEVEL;
        }
        else if (resultLevel < EduProperties.MIN_LEVEL)
        {
            log.debug("resultLevel is too small ("+resultLevel+") for this configuration, use minimum level = "+EduProperties.MIN_LEVEL);
            resultLevel = EduProperties.MIN_LEVEL;
        }

        int resultChapter = Integer.parseInt(characteristic[1]) + chapters.getLast();

        levels.addLast(resultLevel);
        chapters.addLast(resultChapter);

        log.trace("set (add to list) new level =" + resultLevel);
        log.trace("set (add to list) new chapter =" + resultChapter);
    }

    @Override
    public Event calculateEduState()
    {
        if (getEduStep() == 0)
        {
            log.info("on first step not need calculate. All results = 1, need for delivery next chapter");
            return new Event(TestResult.NORMAL, TestResult.NORMAL, TestResult.NORMAL);
        }

        double dynamicResult=0;
        int testTime;
        log.info("calculate new edu state");

        if (testTimes.size() == 0) // for initial state (first iteration "while" )
        {
            log.info("testTime = TestResult.NORMAL (1), because testTimes.size() = 0");
            testTime = TestResult.NORMAL;
        }
        else if (testTimes.getLast() <= EduProperties.NORMAL_TEST_TIME)
        {
            log.info("testTime = TestResult.NORMAL (1), because last testTime ("+testTimes.getLast()
                     +") <= EduProperties.NORMAL_TEST_TIME ("+EduProperties.NORMAL_TEST_TIME+")");
            testTime = TestResult.NORMAL;
        }
        else
        {
            log.info("testTime = TestResult.BAD (0), because last testTime ("+testTimes.getLast()
                     +") > EduProperties.NORMAL_TEST_TIME ("+EduProperties.NORMAL_TEST_TIME+")");
            testTime = TestResult.BAD;
        }


        for (Integer chResult : chapterResults)
            dynamicResult += chResult;
        dynamicResult/=chapterResults.size();
        log.info(" dynamicResult = "+dynamicResult + " and round = "+Math.round(dynamicResult));

        Event resultEvent = new Event(testTime, chapterResults.getLast(), (int) Math.round(dynamicResult));
        log.info("Total event - "+resultEvent.toString());
        return  resultEvent;
    }

    @Override
    public boolean isEduComplete()
    {
        return chapters.getLast() == EduProperties.MAX_CHAPTER && chapterResults.getLast() >= TestResult.NORMAL;
    }

    public void testCurrentChapter() throws Exception
    {
        long startTestTime = System.currentTimeMillis();
        super.testCurrentChapter();
        testTimes.addLast(System.currentTimeMillis() - startTestTime);
    }

    public String getInfo()
    {
        return super.getInfo() + " Test time:" + testTimes;
    }
}
