package tltsu.expertsystem.fsm;

/**
 * @author FADEEV
 */
public interface Fsm
{
    void handleEvent(Event event);
    String getCurrentState();
}
