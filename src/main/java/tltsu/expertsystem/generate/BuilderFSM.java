package tltsu.expertsystem.generate;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * @author FADEEV
 */
public class BuilderFSM
{
    public static String buildFsm(String statesUrl, String eventsUrl, String fsmUrl, String packageName, String className)
            throws FileNotFoundException, TransformerException, UnsupportedEncodingException
    {
        XsltTransformerFSM transformerFsm = XsltTransformerFSM.getInstance();

        String states;
        String events;
        String fsm;
        if (statesUrl == null || eventsUrl == null || fsmUrl == null)
        {
            states = transformerFsm.transform(BuilderFSM.class.getResourceAsStream("configs/states.xml"),
                            BuilderFSM.class.getResourceAsStream("styles/states.xslt"));

            events = transformerFsm.transform(BuilderFSM.class.getResourceAsStream("configs/events.xml"),
                    BuilderFSM.class.getResourceAsStream("styles/events.xslt"));

            fsm = transformerFsm.transform(BuilderFSM.class.getResourceAsStream("configs/FSM.xml"),
                            BuilderFSM.class.getResourceAsStream("styles/FSM.xslt"));
        }
        else
        {
            states = transformerFsm.transform(new FileInputStream(new File(statesUrl)),
                    BuilderFSM.class.getResourceAsStream("styles/states.xslt"));

            events = transformerFsm.transform(new FileInputStream(new File(eventsUrl)),
                    BuilderFSM.class.getResourceAsStream("styles/events.xslt"));

            fsm = transformerFsm.transform(new FileInputStream(new File(fsmUrl)),
                    BuilderFSM.class.getResourceAsStream("styles/FSM.xslt"));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("package "+ packageName +";\n")
                .append("import tltsu.expertsystem.fsm.Fsm;\n")
                .append("import pt.fsm.FSM;\n")
                .append("import pt.fsm.State;\n")
                .append("/**\n * @author BuilderFSM.java\n**/\n")
                .append("public class "+ className +"  implements Fsm \n")
                .append("{\n")
//                .append("public void main() ")
//                .append("{\n")
//                .append("   System.out.println(\"Test FirstTokenPrintFSM\");\n")
//                .append("   Fsm a = new "+ className +"();\n")
//                .append("   String testStr = \" wdwdweded e2efef wefwef ewf wef  \\n eefef  wewefef wefefefwe wefe \\nf\";\n")
//                .append("   for (int i = 0; i < testStr.length(); i++)\n")
//                .append("       a.handleEvent(String.valueOf(testStr.charAt(i)));")
//                .append("}")
                .append("\n")
                .append(" public " + className + "()\n")
                .append("    {\n" )
                .append("        a = createA();\n" )
                .append("    }\n")
                .append("    public void handleEvent(String event)\n" )
                .append("    {\n" )
                .append("        a.handleEvent(event);\n")
                .append("    }")
                .append("\n")
                .append("\n")
                .append("    public A getCurrentState()\n")
                .append("    {\n")
                .append("        return a.getCurrentState().getId();\n")
                .append("    }\n")
                .append("\n")
                .append("    private FSM<A, String> a;")
                .append(events)
                .append(states)
                .append(replaceEscapeAndMacros(fsm))
                .append("\n }\n");

        return sb.toString();
    }

    static String replaceEscapeAndMacros(String source)
    {
        return source.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("#EVENT#", "e()");
    }
}
