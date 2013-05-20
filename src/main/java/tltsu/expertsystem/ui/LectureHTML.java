package tltsu.expertsystem.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

/**
 * @author FADEEV
 */
public class LectureHTML extends AbstractDialog
{
    public LectureHTML(String theme, URL url) throws IOException
    {
        super(theme);
        this.setSize(800, 600);
        JEditorPane pane = new JEditorPane(url);
        pane.setEditable(false);
        pane.setContentType("text/html");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(4, 4));
        JTextField textField = new JTextField(32);
        panel.add(textField, BorderLayout.CENTER);

        JButton buttonExit = new JButton("Exit");
        buttonExit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                isExit = true;
                close();
            }
        });

        JButton buttonNext = new JButton("Next");
        buttonNext.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });

        Box box = Box.createHorizontalBox();
        box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(Box.createHorizontalGlue());
        box.add(buttonExit);
        box.add(Box.createHorizontalGlue());
        box.add(buttonNext);
        box.add(Box.createHorizontalGlue());

        getContentPane().add(box, BorderLayout.SOUTH);
        getContentPane().add(new JScrollPane(pane), BorderLayout.CENTER);
    }

}
