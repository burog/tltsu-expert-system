package tltsu.expertsystem.ui;


import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SimpleLecture extends AbstractDialog
{
    private static final long serialVersionUID = -1897297236572271580L;
    //============================================== instance variables
   JTextArea _resultArea = new JTextArea(6, 20);

    //====================================================== constructor
    public SimpleLecture(String theme, String material) {
        super(theme);
        this.setSize(800, 600);
        //... Set textarea's initial text, scrolling, and border.
        _resultArea.setText(material);
        _resultArea.setEditable(false);
        JScrollPane scrollingArea = new JScrollPane(_resultArea);

        //... Get the content pane, set layout, add to center
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(scrollingArea, BorderLayout.CENTER);
        //... Set window characteristics.
        this.setContentPane(content);
//        this.setTitle("TextAreaDemo B");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
    }

    public static SimpleLecture instanceOf(String theme, String material)
    {
        return new SimpleLecture(theme, material);
    }


    //============================================================= main
    public static void main(String[] args) throws Exception
    {
//        AbstractShow dialog =  ShowLection.instanceOf("Тема 1", "gfhgfhgfhgksdj jdf sdf sdkfj ");
        String url1 = "file:///C:/Users/fadeev/Desktop/Biglion.htm";
        URL url = new URL(url1) ;
        AbstractDialog dialog =  new LectureHTML("Тема 1", url);
        dialog.showLecture();
        System.out.print("КОНЕЦ");

//        JFrame win = new ShowLection("lection #1");
//        win.setVisible(true);
    }

}
