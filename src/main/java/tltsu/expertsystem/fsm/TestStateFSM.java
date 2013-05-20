package tltsu.expertsystem.fsm;

import pt.fsm.FSM;
import pt.fsm.State;

/**
 * @author FADEEV
 */
public class TestStateFSM implements Fsm
{
    private FSM<A, String> a;

    public TestStateFSM()
    {
        a = createA();
    }

    //    public void main(){}

    public void handleEvent(Event event)
    {
        a.handleEvent(event.lastResult+ ";"+event.eduTime);
    }

    public String getCurrentState()
    {
        return a.getCurrentState().getId().getCalcState();
    }

    enum A
    {
        DOWN_LEVEL("-1;0"), UP_CHAPTER("0;1"), UP_CHAPTER_LEVEL("1;1");
        private String calcState;

        private A(String calcState)
        {
            this.calcState = calcState;
        }

        public String getCalcState()
        {
            return calcState;
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends Object> FSM<A, E> createA() {
        return new FSM<A, E>(

                new State<A, E>
                        (A.DOWN_LEVEL) {
                    public void handleEvent()
                    {
                        if(e().equals(A.UP_CHAPTER.calcState))
                        {
                            next(A.UP_CHAPTER);
                        }
                    }
                },
                new State<A, E>
                        (A.UP_CHAPTER) {
                    public void handleEvent()
                    {
                        if (e().equals(A.UP_CHAPTER.calcState))
                            next(A.UP_CHAPTER);
                        else if (e().equals(A.DOWN_LEVEL.calcState))
                            next(A.DOWN_LEVEL);
                        else if (e().equals(A.UP_CHAPTER_LEVEL.calcState))
                            next(A.UP_CHAPTER_LEVEL);
                    }
                },
                new State<A, E>
                        (A.UP_CHAPTER_LEVEL) {
                    public void handleEvent()
                    {
                        if (e().equals(A.UP_CHAPTER.calcState))
                            next(A.UP_CHAPTER);
                    }
                }

                //		new State<A, E>
                //		(A.END) {
                //			public void handleEvent()
                //            {
                //
                //            }
                //		}
        );
    }
}
