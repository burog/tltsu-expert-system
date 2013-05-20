package tltsu.expertsystem.fsm;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import tltsu.expertsystem.answeranalyzer.TestResult;

/**
 * @author FADEEV
 */
public class MiddleUser extends AbstractUser
{
    private static final Logger log = Logger.getLogger(MiddleUser.class);
    private static final long serialVersionUID = 3957424036423506327L;
    private ArrayList<Long> teachTime;
    private ArrayList<Long> testTime;
    private Long normalTeachTime = 30L;
    private Long normalTestTime = 10L;

    public MiddleUser(String pathToMaterial, String name, String normalTeachTimeInMinutes, String normalTestTimeInMinutes)
    {
        super(pathToMaterial, name);
        teachTime = new ArrayList<Long>();
        testTime = new ArrayList<Long>();
        try
        {
            this.normalTeachTime = Long.valueOf(normalTeachTimeInMinutes) * 1000 * 60;
            this.normalTestTime = Long.valueOf(normalTestTimeInMinutes) * 1000 * 60;
        }
        catch (NumberFormatException nfe)
        {
            log.error("Use default value of normal test time = 10, teach time = 30 minutes", nfe);
        }
    }



    @Override
    protected void changeEduState(String state)
    {
        String[] characteristic = state.split(";");
        int resultLevel = Integer.parseInt(characteristic[0]) + getCurrentLevel();
        int resultChapter = Integer.parseInt(characteristic[1]) + getCurrentChapter();
        setLevel(resultLevel);
        setChapter(resultChapter);
    }

    @Override
    public String calculateEduState()
    {
        if (getEduStep() == 1)
            return "0;1";

        int themeChange = 0;
        int levelChange = 0;

        // TODO сделать вычисление динамики
        if (teachTime.get(teachTime.size()) > normalTeachTime || testTime.get(testTime.size()) > normalTestTime)
            levelChange--;

//        if (getCurrentResult() + getCurrentResult(getCurrentPart()-1)  <= 5)
//            themeChange--;

        if (teachTime.get(teachTime.size()) > normalTeachTime && testTime.get(testTime.size()) > normalTestTime
                && getCurrentResult() >= 5)
            levelChange++;

//        if (getChapter() == 3 || getChapter() == 7 )
//            return "1;1";

        return  levelChange +";"+ themeChange;
    }

    @Override
    public boolean isEduComplete()
    {
        log.trace(getInfo());
        return getCurrentChapter() == 10 && getCurrentResult() >= TestResult.NORMAL_RESULT;
    }

    public void testCurrentChapter() throws Exception
    {
        long startTestTime = System.currentTimeMillis();
        super.testCurrentChapter();
        testTime.add(testTime.size(), System.currentTimeMillis() - startTestTime);
    }


    public void teach() throws Exception
    {
        long startTeachTime = System.currentTimeMillis();
        super.teach();
        teachTime.add(teachTime.size(), System.currentTimeMillis() - startTeachTime);
    }

    public String getInfo()
    {
        return super.getInfo() + " Teach Time:"+teachTime + " Test time:" + testTime;
    }
}
