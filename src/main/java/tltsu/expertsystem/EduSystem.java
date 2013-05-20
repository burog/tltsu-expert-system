package tltsu.expertsystem;

import org.apache.log4j.Logger;
import tltsu.expertsystem.fsm.EduFSM;
import tltsu.expertsystem.fsm.Event;
import tltsu.expertsystem.fsm.Fsm;
import tltsu.expertsystem.ui.AbstractDialog;
import tltsu.expertsystem.ui.AbstractQuestion;
import tltsu.expertsystem.ui.SimpleLecture;
import tltsu.expertsystem.ui.StartWindow;
import tltsu.expertsystem.utils.SerializeException;
import tltsu.expertsystem.utils.Utils;

/**
 * @author FADEEV
 */
public class EduSystem
{
    private static final Logger log = Logger.getLogger(EduSystem.class);

    static private final int INDEX_NAME = 0;
    static private final int INDEX_NEED_CREATE = 1;

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
    /**
     * use default {@link tltsu.expertsystem.fsm.EduFSM}
     * @param user
     * @throws Exception
     */
    public static void runEdu(AbstractUser user) throws Exception
    {
        runEdu(new EduFSM(), user);
    }

    public static void runEdu(Fsm education, AbstractUser user) throws Exception
    {
        while (!user.isEduComplete()) //вайл НЕ закончено обучение
        {
            log.debug("user state after check isEduComplete "+user.getInfo());
            Event eduState = user.calculateEduState();

            education.handleEvent(eduState);                   // посылаем результат тестирования на автомат (level up/down/hold : theme up/down/hold )
            String newState = education.getCurrentState();  // вернёт строку вида "0;1" или ??"0;0;0;1"
            user.changeEduState(newState);
            log.debug("user state after changeEduState "+user.getInfo());

            user.teach();
            log.debug("user state after teach "+ user.getInfo());

            user.testCurrentChapter();
            log.debug("user state after testCurrentChapter "+ user.getInfo());

        }


        SimpleLecture dialog = new SimpleLecture("Education", "Education is complete!");
        dialog.showLecture();
        log.info("Education is complite!");

    }
}
