package tltsu.expertsystem.fsm;

import pt.fsm.FSM;
import pt.fsm.State;

import java.io.Serializable;

/**
 * @author FADEEV
 */                                      // may be need implement serialize mechanism to xml or simply save current state ant type FSM in AbstractUser
public class EduStateFSM implements Fsm, Serializable
{
    private static final long serialVersionUID = -6268776641319527208L;
    private FSM<A, String> a;

    public EduStateFSM()
    {
        a = createA();
    }

//    public void main(){}

    public void handleEvent(String event)
    {
        a.handleEvent(event);
    }

    public String getCurrentState()
    {
        return a.getCurrentState().getId().getCalcState();
    }

    enum A
    {
        UP_CHAPTER("0;1"), DOWN_LEVEL("-1;0"), UP_CHAPTER_LEVEL("1;1");
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
                new State<A, E>(A.UP_CHAPTER)
                {
                    public void handleEvent()
                    {
                        if (e().equals(A.UP_CHAPTER.calcState))
                            next(A.UP_CHAPTER);
                        else if (e().equals(A.DOWN_LEVEL.calcState))
                            next(A.DOWN_LEVEL);
                        else if (e().equals(A.UP_CHAPTER_LEVEL.calcState))
                            next(A.UP_CHAPTER_LEVEL);
                    }
                }, new State<A, E>(A.DOWN_LEVEL)
        {
            public void handleEvent()
            {
                if (e().equals(A.UP_CHAPTER.calcState))
                {
                    next(A.UP_CHAPTER);
                }
            }
        }, new State<A, E>(A.UP_CHAPTER_LEVEL)
        {
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