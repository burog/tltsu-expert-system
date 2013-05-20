package tltsu.expertsystem.fsm;

import pt.fsm.FSM;
import pt.fsm.State;

/**
 * @author FADEEV
 */
public class FirstTokenPrintFSM implements Fsm
{
    private static final long serialVersionUID = -8001547810519151777L;
    private FSM<A, String> a;

    public FirstTokenPrintFSM()
    {
        a = createA();
    }

    public void main(){}

    public void handleEvent(Event event)
    {
        if (event.lastResult == 0)
            a.handleEvent("A");
        else if (event.lastResult > 0)
            a.handleEvent(" ");
        else if (event.lastResult < 0)
            a.handleEvent("\n");
    }

    public String getCurrentState()
    {
        return a.getCurrentState().getId().toString();
    }

    enum A
    {
        BEFORE("BMW"), INSIDE("TOYOTA"), AFTER("FIAT");
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
		(A.BEFORE) {
			public void handleEvent()
            {
                if(e().equals("\n"))
                {
                    System.out.println();
                }
                else if(!e().equals(" "))
                {
                    System.out.print(e());
                    next(A.INSIDE);
                }
            }
		},
		new State<A, E>
		(A.INSIDE) {
           public void handleEvent()
            {
                if (e().equals("\n"))
                {
                    next(A.BEFORE);
                    System.out.print(e());
                }
                else if (e().equals(" "))
                    next(A.AFTER);
                else
                    System.out.print(e());
            }
		},
		new State<A, E>
		(A.AFTER) {
			public void handleEvent()
            {
                if (e().equals("\n"))
                {
                    next(A.BEFORE);
                    System.out.print(e());
                }
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