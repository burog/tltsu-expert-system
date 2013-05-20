package tltsu.expertsystem.fsm;

import org.apache.log4j.Logger;
import tltsu.expertsystem.answeranalyzer.TestResult;

/**
 * @author FADEEV
 */
public class SimpleUser extends AbstractUser
{
    private static final Logger log = Logger.getLogger(SimpleUser.class);
    private static final long serialVersionUID = -6415631134928185183L;

    public SimpleUser(String pathToMaterial, String name)
    {
        super(pathToMaterial, name);
    }


    public void changeEduState(String state)
    {
        String[] characteristic = state.split(";");
        int currentLevel = Integer.parseInt(characteristic[0]) + getCurrentLevel();
        int currentChapter = Integer.parseInt(characteristic[1]) + getCurrentChapter();

        setLevel(currentLevel);
        setChapter(currentChapter);
    }

    @Override
    public String calculateEduState() //TODO придумать умную вычислялку состояния для юзера
    {                                                //TODO учитывать историю прохождения лекций
        if (getCurrentChapter() == 3 || getCurrentChapter() == 7 )
            return "1;1";
        return  "0;1";
    }

    @Override
    public boolean isEduComplete()
    {
        return getCurrentChapter() == 10 && chapterTest.getCurrentResult().result >= TestResult.NORMAL_RESULT;
    }

}
