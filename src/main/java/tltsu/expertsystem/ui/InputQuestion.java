package tltsu.expertsystem.ui;

import org.apache.log4j.BasicConfigurator;
import tltsu.expertsystem.Main;
import tltsu.expertsystem.answeranalyzer.TestResult;
import tltsu.expertsystem.answeranalyzer.AnalyzeAnswer;
import tltsu.expertsystem.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class InputQuestion extends AbstractQuestion implements TestQuestion
{
    private static final long serialVersionUID = 7020025158045272733L;
    private JTextArea answerField;

    public InputQuestion(String question, String correctAnswer)
    {
        super("Question", correctAnswer);
        this.setSize(800, 600);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        contentPane.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JButton buttonOk = new JButton("OK");
        buttonOk.addActionListener(new ActionListener()
        {
            @Override public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });
//        buttonYes.setSize(20, 10);

//        buttonNo.setSize(20, 10);
//        buttonNo.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JLabel questionLabel = new JLabel(question);
//        getRootPane().setDefaultButton(buttonYes);

//        contentPane.setLayout(new BorderLayout());
        Box box = Box.createVerticalBox();
        setContentPane(box);

        answerField = new JTextArea();
//        answerField.setColumns(20);
//        answerField.setSize(100, 20);
        box.add(questionLabel);
        questionLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(Box.createVerticalStrut(20));
        box.add(answerField);
        box.add(Box.createVerticalStrut(20));
        box.add(buttonOk);
        box.add(Box.createVerticalStrut(20));
//        this.setSize(100, 100);

    }

    protected void saveAnswer()
    {
        userAnswer = answerField.getText();
    }

    /**
     * забираем у UI ответ и сравниваем его с эталоном (который вытащили из xml) с помощью анализатора  и итоге получаем testResult
     * @return
     */
    public TestResult getTestResult() throws Exception
    {
        return AnalyzeAnswer.analyzeFormula(correctAnswer, userAnswer);
    }

    public static void main(String[] args) throws Exception
    {
        BasicConfigurator.configure();
//        Question dialog = new InputQuestion("First question");

//        Question dialog = new YesNoTest("TItle", "Questin!");
//                dialog.showLecture();
//        System.out.println(dialog.getUserAnswer());
//        System.exit(0);
        String pattern = "material/charph_?_?.htm";
        String chapter = "0";
        String level = "3";
//        System.out.println(Main.class.get)
        System.out.println(Utils.getUrlByPattern(pattern, chapter, level));

        System.out.println("Test old");

                String path = pattern.replaceFirst("\\?", chapter);
                path = path.replaceFirst("\\?", level);
                URL url = Main.class.getResource(path);
        System.out.println("OLD"+url);
    }


}
