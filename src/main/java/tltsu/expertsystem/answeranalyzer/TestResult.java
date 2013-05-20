package tltsu.expertsystem.answeranalyzer;

/**
 * This class keep result (int) and error is occured.
 * @author FADEEV
 */
public class TestResult // TODO migrate this class to enum ?
{
    public static final int BAD = 0;
    public static final int NORMAL = 1;
    public static final int EXCELLENT = 2;
    public static final TestResult TEACHER_FAILED = new TestResult(BAD, "Error occured in Test System, please contact to the teacher");
    public static final TestResult EXCELLENT_PASSED = new TestResult();

    public final int result;
    public final String error;

    public TestResult(int result, String error)
    {
        this.result = result;
        this.error = error;
    }

    public TestResult()
    {
        this.result = EXCELLENT;
        error="";
    }

    public String toString ()
    {
        return "Result: "+result + (error.isEmpty()? "" : " Error: "+error);
    }
}
