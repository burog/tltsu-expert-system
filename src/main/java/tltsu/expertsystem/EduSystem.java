package tltsu.expertsystem;

import org.apache.log4j.Logger;
import tltsu.expertsystem.fsm.EduFSM;
import tltsu.expertsystem.fsm.Event;
import tltsu.expertsystem.fsm.Fsm;
import tltsu.expertsystem.ui.SimpleLecture;

/**
 * @author FADEEV
 */
public class EduSystem
{
    private static final Logger log = Logger.getLogger(EduSystem.class);

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
