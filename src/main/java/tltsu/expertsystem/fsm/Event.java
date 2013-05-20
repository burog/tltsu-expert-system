package tltsu.expertsystem.fsm;

/**
 * @author FADEEV
 */
public class Event
{
    public final int eduTime;
    public final int lastResult;
    public final int avgResults;

    public Event(int eduTime, int lastResult, int avgResults)
    {
        this.eduTime = eduTime;
        this.lastResult = lastResult;
        this.avgResults = avgResults;
    }
    
    public String toString ()
    {
        return "eduTime="+eduTime+ ", lastResult=" +lastResult+", avgResults="+ avgResults;
    }
}
