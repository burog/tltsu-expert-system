package tltsu.expertsystem;

import org.apache.log4j.Logger;
import tltsu.expertsystem.answeranalyzer.TestResult;
import tltsu.expertsystem.ui.InputQuestion;
import tltsu.expertsystem.ui.TestQuestion;
import tltsu.expertsystem.ui.SimpleLecture;
import tltsu.expertsystem.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author FADEEV
 */
public class ChapterTest implements Serializable
{
    private static final long serialVersionUID = 374520639802143839L;
    private static final Logger log = Logger.getLogger(ChapterTest.class);

    int level;
    int chapter;

    protected ArrayList<Double> results;
    protected Integer testParts;
    protected ArrayList<Integer> testQuestion;

    ConfigurationOfChapterTest chapterTestConfig;

    public ChapterTest(String course, int chapter, int level) throws Exception
    {
        this.chapter = chapter;
        this.level = level;

        results = new ArrayList<Double>();
        testParts = 0;
//        testParts.add(0);
        testQuestion = new ArrayList<Integer>();
//        testQuestion.add(0);

        // initialize config, parse count of PARTS
        chapterTestConfig = ConfigurationOfChapterTest.load(course, level ,chapter);
    }

    @Deprecated
    public Double getCurrentPartResult()
    {
        return results.get(results.size() - 1);
    }

    public void setCurrentPartResult(Double result)
    {
        this.results.add(result);
    }

    public Double getPartResult(int part)
    {
        return results.get(part);
    }

    public int getCurrentPart()
    {
        return testParts;
    }

    public void nextPart()
    {
        testParts += 1;
    }

    public boolean isTestComplete()
    {         // getCurrentPart()+1  becouse currentParn begining with 0.
        return getCurrentPart() >= chapterTestConfig.getCountOfParts() /*|| или 2 ответа зафейлины или другие "ништянки"*/;
    }

    private void mistakesWorking(TestResult questionResult)
    {
//        System.out.println(questionResult.error != null +"||"+ !questionResult.error.isEmpty());
        if(questionResult.error != null && !questionResult.error.isEmpty() && !questionResult.error.equals("null"))
            new SimpleLecture("Correction of mistakes", questionResult.error).showLecture();
    }

    public void testCurrPart() throws Exception
    {   // i - need for account of attempts to answer a part.
        int i = 0;
//        List<TestResult> resultQuestion = new LinkedList<TestResult>();

        do
        {
            // 1) достаём данные для выдачи - она включает в себя данные о конкретном вопросе
//            int maxAvailableInCurrentPart = chapterTestConfig.getCountOfQuestion(getCurrentPart());
            TestQuestion question = parseQuestionXML(Utils.getNextRandom(/*maxAvailableInCurrentPart*/));

            // 2.a) отдаём вопрос к UI -рисуем вопрсо и пишем ответ в нутрях класса
            question.showLecture();

            // 2.b) забираем у UI ответ и сравниваем его с эталоном (который вытащили из xml) с помощью анализатора  и итоге получаем testResult
            TestResult questionResult = question.getTestResult();
//            resultQuestion.add(questionResult);
            
            mistakesWorking(questionResult);

            i++;

            if (questionResult.result != TestResult.BAD )
                break;
        }
        while (i < chapterTestConfig.getCountOfQuestion(getCurrentPart()));

        double result = (100 + 30 * (i-1) ) / i;
        setCurrentPartResult(result);
    }

    private TestQuestion parseQuestionXML(Integer questionNumber)
    {
        log.debug("delivery question: level#"+level+" chapter#"+chapter+" part#"+getCurrentPart()+" question#"+questionNumber);
        ConfigurationOfChapterTest.Pair pair = chapterTestConfig.getQuestionWithAnswer(getCurrentPart(), questionNumber);
        return new InputQuestion("Question: "+pair.question, pair.answer);
//        return new InputQuestion("Question: level#"+level + " chapter#"+chapter + " part#"+getCurrentPart()+ " number#"+questionNumber, "3*y+y^2");
    }

}
