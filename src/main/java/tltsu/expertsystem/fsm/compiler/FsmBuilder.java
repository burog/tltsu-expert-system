package tltsu.expertsystem.fsm.compiler;

import org.apache.log4j.Logger;
import tltsu.expertsystem.fsm.CharSequenceCompilerException;
import tltsu.expertsystem.fsm.Fsm;
import tltsu.expertsystem.generate.BuilderFSM;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * @author FADEEV
 */
public class FsmBuilder
{
    private final CharSequenceCompiler<Fsm> compiler = new CharSequenceCompiler<Fsm>(getClass().getClassLoader(),
            Arrays.asList(new String[]{"-target", "1.5"}));
    private int classNameSuffix = 0;
    private static final String PACKAGE_NAME = "test.fsm.runtime";
    private static final Random random = new Random();
    private static final Logger log = Logger.getLogger(FsmBuilder.class);

    /**
     *
     * @param statesUrl getting as resources if null
     * @param eventsUrl getting as resources if null
     * @param fsmUrl getting as resources if null
     * @return new instance of generating FSM by incoming params
     */
    public Fsm newFsm(String statesUrl, String eventsUrl, String fsmUrl) throws TransformerException
    {

        try
        {
            final String packageName = PACKAGE_NAME + digits();
            final String className = "Fsm_" + (classNameSuffix++) + digits();
            final String qName = packageName + '.' + className;

            final String source = BuilderFSM.buildFsm(statesUrl, eventsUrl, fsmUrl, packageName, className);
            log.debug("Source of FSM \n" +source);
            final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();
            Class<Fsm> compiledFunction = compiler.compile(qName, source, errs, Fsm.class);
            log(errs);

            return compiledFunction.newInstance();
        }
        catch (CharSequenceCompilerException e)
        {
            log(e.getDiagnostics());
        }
        catch (InstantiationException e)
        {
            log.error(e.getMessage());
        }
        catch (IllegalAccessException e)
        {
            log.error(e.getMessage());
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        return null;
    }

    private String digits()
    {
        return '_' + Long.toHexString(random.nextLong());
    }

    private void log(final DiagnosticCollector<JavaFileObject> diagnostics)
    {
        final StringBuilder msgs = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics())
        {
            msgs.append(diagnostic.getMessage(null)).append("\n");
        }
        log.info(msgs.toString());
    }
}
