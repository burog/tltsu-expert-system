package tltsu.expertsystem.fsm;

import org.apache.log4j.Logger;
import pt.fsm.FSM;
import pt.fsm.State;

import static tltsu.expertsystem.answeranalyzer.TestResult.BAD;
import static tltsu.expertsystem.answeranalyzer.TestResult.EXCELLENT;
import static tltsu.expertsystem.answeranalyzer.TestResult.NORMAL;

/**
 * @author FADEEV
 */
public class EduFSM implements Fsm
{
    private static final Logger log = Logger.getLogger(EduFSM.class);
    private FSM<States, Event> a;

    public EduFSM()
    {
        a = createA();
    }

    //    public void main(){}

    public void handleEvent(Event event)
    {
        a.handleEvent(event);
    }

    public String getCurrentState()
    {
        return a.getCurrentState().getId().textualState;
    }

    enum States
    {

        /**
         * increase theme and don't change level.
         */
        S_U("0;1"),

        /**
         * dont change level and theme.
         */
        S_S("0;0"),

        /**
         * increase level and theme.
         */
        U_U("1;1"),

        /**
         * decrease level and don't change theme.
         */
        D_S("-1;0"),

        /**
         * decrease level and increase theme.
         */
        D_U("-1;1");

        public final String textualState;

        private States(String textualState)
        {
            this.textualState = textualState;
        }
    }

    @SuppressWarnings("unchecked")
    static private <E extends Object> FSM<States, Event> createA() {
        return new FSM<States, Event>(

                // STATE increase theme and don't change level.
                new State<States, Event>
                        (States.S_U) {
                    public void handleEvent()
                    {
                        Event e = e();
                        log.debug("handle event "+e);

                        if (e.lastResult == BAD)
                            next(States.S_S);
                        else if (e.avgResults == BAD)
                            next(States.D_U);
                        else if (e.eduTime == NORMAL && e.avgResults == EXCELLENT
                                 && (e.lastResult == NORMAL || e.lastResult == EXCELLENT))
                            next(States.U_U);
                        else
                            next(States.S_U);
                    }
                    public void enter()       { log.debug("enter into "+States.S_U.textualState); }
                },

                // STATE dont change level and theme.
                new State<States, Event>
                        (States.S_S) {
                    public void handleEvent()
                    {
                        Event e = e();
                        log.debug("handle event "+e);

                        if ( e.lastResult == EXCELLENT || e.lastResult == NORMAL && e.avgResults +e.eduTime >= EXCELLENT)
                            next(States.S_U);
                        else
                            next(States.D_S);
                    }
                    public void enter()       { log.debug("enter into "+States.S_S.textualState); }
                },

                // STATE increase level and theme.
                new State<States, Event>
                        (States.U_U) {
                    public void handleEvent()
                    {
                        log.debug("handle event "+e());

                        if ( e().lastResult == BAD)
                            next(States.S_S);
                        else
                            next(States.S_U);
                    }
                    public void enter()       { log.debug("enter into "+States.U_U.textualState); }
                },

                // STATE decrease level and don't change theme.
                new State<States, Event>
                        (States.D_S) {
                    public void handleEvent()
                    {
                        log.debug("handle event "+e());

                        if ( e().lastResult == EXCELLENT)
                            next(States.S_U);
                        else
                            next(States.D_S);
                    }
                    public void enter()       { log.debug("enter into "+States.D_S.textualState); }
                },

                // STATE decrease level and increase theme.
                new State<States, Event>
                        (States.D_U) {
                    public void handleEvent()
                    {
                        log.debug("handle event "+e());

                        if ( e().lastResult == BAD)
                            next(States.S_S);
                        else
                            next(States.S_U);
                    }
                    public void enter()       { log.debug("enter into "+States.D_U.textualState); }
                    //                    public void exit()        { log.debug("exit3"); }
                }
        );
    }
}
