package tltsu.expertsystem.fsm;

import org.apache.log4j.BasicConfigurator;
import pt.fsm.FSM;
import pt.fsm.State;

import org.apache.log4j.Logger;

import static  tltsu.expertsystem.answeranalyzer.TestResult.EXCELLENT;
import static  tltsu.expertsystem.answeranalyzer.TestResult.NORMAL;
import static  tltsu.expertsystem.answeranalyzer.TestResult.BAD;

/**
 * @author FADEEV
 */

public class ComplexEvent {

    static
    {
        BasicConfigurator.configure();


    }
    private static final Logger log = Logger.getLogger(ComplexEvent.class.getName());

    static private FSM<States, Event> a;

    /**
     * S - do not change characteristic
     * U - increase characteristic
     * D - decrease characteristic
     *
     * FIrst symbol - equals change the level, second - theme.
     */
    static private enum States
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
                },

                // STATE dont change level and theme.
                new State<States, Event>
                        (States.S_S) {
                    public void handleEvent()
                    {
                        Event e = e();
                        if ( e.lastResult == EXCELLENT || e.lastResult == NORMAL && e.avgResults +e.eduTime >= EXCELLENT)
                            next(States.S_U);
                        else
                            next(States.D_S);
                    }
                },

                // STATE increase level and theme.
                new State<States, Event>
                        (States.U_U) {
                    public void handleEvent()
                    {
                        if ( e().lastResult == BAD)
                            next(States.S_S);
                        else
                            next(States.S_U);
                    }
                },

                // STATE decrease level and don't change theme.
                new State<States, Event>
                        (States.D_S) {
                    public void handleEvent()
                    {
                        if ( e().lastResult == EXCELLENT)
                            next(States.S_U);
                        else
                            next(States.D_S);
                    }
                },

                // STATE decrease level and increase theme.
                new State<States, Event>
                        (States.D_U) {
                    public void handleEvent()
                    {
                        if ( e().lastResult == BAD)
                            next(States.S_S);
                        else
                            next(States.S_U);
                    }
//                    public void enter()       { log.info("enter3"); }
//                    public void exit()        { log.info("exit3"); }
                }
        );
    }
    
    public static void main(String[] args)
    {
        a = createA();

        Event a1 = new Event (BAD, BAD, BAD);
        Event a2 = new Event (BAD, BAD, NORMAL);
        Event a3 = new Event(NORMAL, EXCELLENT, EXCELLENT);

        log.info("curr state: "+a.getCurrentState());

        a.handleEvent(a1);
        log.info("curr state: "+a.getCurrentState());
//        log.info("a1 sended");

        a.handleEvent(a1);
        log.info("curr state: "+a.getCurrentState());
//        log.info("a2 sended");


        a.handleEvent(a3);
        log.info("curr state: "+a.getCurrentState());
//        log.info("a3 sended");


        a.handleEvent(a3);
        log.info("curr state: "+a.getCurrentState());
    }
}

