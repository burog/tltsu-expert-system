package tltsu.expertsystem.fsm;

import tltsu.expertsystem.answeranalyzer.TestResult;
import tltsu.expertsystem.ui.InputQuestion;
import tltsu.expertsystem.ui.TestQuestion;
import tltsu.expertsystem.ui.SimpleLecture;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author FADEEV
 */
public class ChapterTest
{
//    private static final long serialVersionUID = 374520639802143839L;
    int chapter;
    int level;
    int step;

    public ChapterTest(int chapter, int level)
    {
        this.chapter = chapter;
        this.level = level;
        step = 1;

        results = new ArrayList<TestResult>();
        testParts = new ArrayList<Integer>();
        testParts.add(0);
        testQuestion = new ArrayList<Integer>();
        testQuestion.add(0);

    }

    protected ArrayList<TestResult> results;
    protected ArrayList<Integer> testParts;
    protected ArrayList<Integer> testQuestion;

    public TestResult getCurrentResult()
    {
        return results.get(results.size() - 1);
    }

    public void setResult(TestResult result)
    {
        this.results.add(result);
    }

    public TestResult getResult(int step)
    {
        return results.get(step);
    }


    public int getCurrentPart()
    {
        return testParts.get(testParts.size() - 1);
    }

    public void setPart(int part)
    {
        this.testParts.add(this.testParts.size(), part );
    }
    public int getPart(int step)
    {
        return testParts.get(step);
    }


    public void setQuestion(int question)
    {
        this.testQuestion.add(this.testQuestion.size(), question );
    }

    public int getCurrentQuestion()
    {
        return testQuestion.get(testQuestion.size() - 1);
    }

    public int getQuestion(int step)
    {
        return testQuestion.get(step);
    }


    public String calculateTestState()
    {//TODO make normal adapter to calculate state
        // изменяем вопросы/темы по зависимостям трабличи (которую я должен нарисовать по нарисованнмоу автомату)
        return "0;1";
    }


    protected void changeTestState(String state)
    {
        String[] characteristic = state.split(";");
        int resultQuestion = Integer.parseInt(characteristic[0]) + getCurrentQuestion();
        int resultPart = Integer.parseInt(characteristic[1]) + getCurrentPart();
        setPart(resultPart);
        setQuestion(resultQuestion);
    }

    public boolean isTestComplete()
    {
        return getCurrentPart() >= 2 /*|| или 2 ответа зафейлины или другие "ништянки"*/;
    }

    public void mistakesWorking()
    {
        if(getCurrentResult().error != null || !getCurrentResult().error.isEmpty())
            new SimpleLecture("Correction of mistakes", getCurrentResult().error).showLecture();

    }

    void test() throws Exception
    {
        // 1) достаём данные для выдачи XML - она включает в себя данные о конкретном вопросе
        TestQuestion question = parseQuestionXML();

        // 2.a) отдаём вопрос к UI -рисуем вопрсо и пишем ответ в нутрях класса
        question.showLecture();
        // 2.b) забираем у UI ответ и сравниваем его с эталоном (который вытащили из xml) с помощью анализатора  и итоге получаем testResult
        TestResult testResult = question.getTestResult();

        // 3) полученый бин сохраняем
        setResult(testResult);
        step++;
    }

    public TestQuestion parseQuestionXML()
    {
        // TODO change stub to real parsing - getting needed values (question, correctAnswer)  by params part, chapter, level

        return new InputQuestion("Question p#"+getCurrentPart()+ " c#"+chapter + " l#"+level + " q#"+getCurrentQuestion(), "3*y+y^2");
    }

}
