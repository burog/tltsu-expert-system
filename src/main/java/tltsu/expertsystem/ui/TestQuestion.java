package tltsu.expertsystem.ui;


import tltsu.expertsystem.answeranalyzer.TestResult;

/**
 * @author FADEEV
 */
public interface TestQuestion
{
    public TestResult getTestResult() throws Exception;
    public void showLecture();
}
