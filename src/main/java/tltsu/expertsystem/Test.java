package tltsu.expertsystem;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import tltsu.expertsystem.ui.AbstractDialog;
import tltsu.expertsystem.ui.LectureHTML;
import tltsu.expertsystem.ui.ShowLectureHTML;
import tltsu.expertsystem.ui.YesNoTest;
import tltsu.expertsystem.utils.Utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author FADEEV
 */
public class Test
{
    static
    {
        BasicConfigurator.configure();
    }

    private static final Logger log = Logger.getLogger(Test.class);

    public static void main(String[] args) throws Exception
    {
        //        AbstractShow dialog =  ShowLection.instanceOf("Тема 1", "gfhgfhgfhgksdj jdf sdf sdkfj ");
        String url1 = "file:///C:\\Users\\fadeevm\\Downloads\\Biglion. Это скидки..htm";
//        URL url = new URL(url1) ;
//        AbstractDialog dialog =  new YesNoTest("Тема 1", url);
//        dialog.showLection();
//        System.out.print("КОНЕЦ");

//        Question dialog = new Question();
//        dialog.showLecture();

        YesNoTest dialog1 = new YesNoTest("C sharp lection#", Utils.YES);
        dialog1.showLecture();
//        System.exit(0);
        log.info("exit");
    }
}
