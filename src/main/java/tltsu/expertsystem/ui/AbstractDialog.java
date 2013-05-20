package tltsu.expertsystem.ui;

import org.apache.log4j.Logger;
import tltsu.expertsystem.utils.SerializeException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

/**
 * @author FADEEV
 */
public abstract class AbstractDialog extends JFrame
{
    private static final Logger log = Logger.getLogger(AbstractDialog.class);
    private static final long serialVersionUID = -1893884932673668941L;
    protected final CountDownLatch doneSignal = new CountDownLatch(1);
    protected boolean isExit=false;

    protected AbstractDialog(String title) throws HeadlessException
    {
        super(title);
    }

    public void showLecture()
    {
//        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt)
            {
                close();
            }
        });
//        this.setSize(800, 600);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        try
        {

            doneSignal.await();
        }
        catch (InterruptedException e)
        {
            log.error("Dialog can't wait a user.", e);
        }

        if (isExit)
            throw new RuntimeException("Force exit. Save user is needed");
    }

    protected void close()
    {
        log.trace("window is closed.");
        doneSignal.countDown();
        dispose();
    }
}
