package tltsu.expertsystem.ui;

import tltsu.expertsystem.answeranalyzer.TestResult;
import tltsu.expertsystem.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author FADEEV
 */
public class YesNoTest extends AbstractQuestion implements TestQuestion
{
    private static final long serialVersionUID = -8393586358712545993L;
    private Boolean yesOrNo = null;

    public YesNoTest(String question, String correctAnswer)
    {
        super("Question", correctAnswer);
        this.setSize(800, 600);

//        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Box contentPane = Box.createHorizontalBox();
        //        contentPane.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JButton buttonYes = new JButton(Utils.YES);
        //        buttonYes.setSize(20, 10);

        JButton buttonNo = new JButton(Utils.NO);
        //        buttonNo.setSize(20, 10);
        //        buttonNo.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JLabel questionLabel = new JLabel(question);
        //        getRootPane().setDefaultButton(buttonYes);

        //        contentPane.setLayout(new BorderLayout());
        Box box = Box.createVerticalBox();
        setContentPane(box);

        buttonNo.addActionListener(new ActionListener()
        {
            @Override public void actionPerformed(ActionEvent e)
            {
                yesOrNo = false;
                close();
            }
        });

        buttonYes.addActionListener(new ActionListener()
        {
            @Override public void actionPerformed(ActionEvent e)
            {
                yesOrNo = true;
                close();
            }
        });

        box.add(questionLabel);
        questionLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(Box.createVerticalStrut(20));
        box.add(Box.createVerticalGlue());
        contentPane.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(contentPane);
        contentPane.add(Box.createHorizontalGlue());
        contentPane.add(buttonNo);
        contentPane.add(Box.createHorizontalGlue());
        contentPane.add(buttonYes);
        contentPane.add(Box.createHorizontalGlue());
        box.add(Box.createVerticalStrut(20));
        //        this.setSize(100, 100);
    }

    @Override
    protected void saveAnswer()
    {
        if (yesOrNo == null)
            return;

        if (yesOrNo)
            userAnswer = Utils.YES;
        else
            userAnswer = Utils.NO;
    }

    @Override public TestResult getTestResult()
    {
        if (userAnswer.equalsIgnoreCase(correctAnswer))
            return TestResult.EXCELLENT_PASSED;
        else
            return new TestResult(3, "You are enter wrong answer");
    }
}


