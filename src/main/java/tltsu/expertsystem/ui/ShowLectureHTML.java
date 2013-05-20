package tltsu.expertsystem.ui;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import javax.swing.*;

public class ShowLectureHTML extends JFrame {
//    private final CountDownLatch doneSignal;

    private static final Logger log = Logger.getLogger(ShowLectureHTML.class);

    public ShowLectureHTML(String theme, URL url) throws IOException
  {
    super(theme);
//      this.doneSignal = doneSignal;
      JEditorPane pane = new JEditorPane(url);
    pane.setEditable(false); // Read-only
    getContentPane().add(new JScrollPane(pane), "Center");

      JLabel jlbHelloWorld = new JLabel("Hello World");
      add(jlbHelloWorld);
      this.setSize(100, 100);
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout(4, 4));
//    JLabel urlLabel = new JLabel("URL: ", JLabel.RIGHT);
//    panel.add(urlLabel, "West");
      JTextField textField = new JTextField(100);
    panel.add(textField, "Center");
//      String simpleHtml = "<html>\n" + "<body>\n" + "\n" + "<h1>My First Heading</h1>\n" + "\n"
//                          + "<p>My first paragraph.</p>\n" + "\n" + "</body>\n" + "</html>";

      pane.setContentType("text/html");
//      pane.setText(simpleHtml);
//    getContentPane().add(panel, "South");

    // Change page based on text field
//    textField.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent evt) {
//          doneSignal.countDown();
//        String url = textField.getText();
//          // Try to display the page
//          pane.setContentType("text/html");
//          pane.setText("<html>\n" + "<body>\n" + "\n" + "<h1>My First Heading</h1>\n" + "\n"
//                       + "<p>My first paragraph.</p>\n" + "\n" + "</body>\n" + "</html>");//Page(url);
//      }
//    });
  }


    public static void test1() throws IOException, InterruptedException
    {
        try
        {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        }
        catch (Exception evt)
        {
            log.error("expection while lookup style");
        }
        final CountDownLatch doneSignal = new CountDownLatch(1);
        
        URL url = new URL("file:///C:\\Users\\fadeevm\\Downloads\\Biglion. Это скидки..htm");
        String theme = "Lection #1";
        final JFrame f = new ShowLectureHTML(theme, url);

        f.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt)
            {
                doneSignal.countDown();
//                System.exit(0);
                f.dispose();
            }
        });
        f.setSize(800, 600);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        doneSignal.await();
    }

    public static void mainTest() throws Exception{
    String url = "file:///C:/Users/fadeev/Desktop/Biglion.htm";
//    String url = "http://java.sun.com";
    JEditorPane editorPane = new JEditorPane(url);
    editorPane.setEditable(false);

    JFrame frame = new JFrame();
    frame.getContentPane().add(editorPane, BorderLayout.CENTER);
    frame.setSize(300, 300);
    frame.setVisible(true);
  }

}