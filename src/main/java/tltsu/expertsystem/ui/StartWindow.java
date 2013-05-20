package tltsu.expertsystem.ui;

import org.apache.log4j.Logger;
import tltsu.expertsystem.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author FADEEV
 */
public class StartWindow extends AbstractQuestion
{
    private static final Logger log = Logger.getLogger(StartWindow.class);
    private static final long serialVersionUID = 7611538486821153266L;
    private boolean needCreate = true;
    private String name ;

    public StartWindow()
    {
        super("Start education");

        Box box =  Box.createVerticalBox();

        final JRadioButton createUser = new JRadioButton("Создать нового пользователя");
        createUser.setSelected(true);

        final JRadioButton loadUser = new JRadioButton("Авторизированный пользователь");
        ButtonGroup group = new ButtonGroup();
        group.add(createUser);
        group.add(loadUser);
        final JTextArea  userName = new JTextArea (10, 20);
        userName.setSize(200, 18);
        userName.setCaretPosition(0);


        JButton buttonNext = new JButton("Ok");
        JButton buttonExit = new JButton("Exit");
        Box boxButton = Box.createHorizontalBox();
        boxButton.add(Box.createHorizontalGlue());
        boxButton.add(buttonNext);
        boxButton.add(Box.createHorizontalGlue());
        boxButton.add(buttonExit);
        boxButton.add(Box.createHorizontalGlue());
        //        buttonNo.setSize(20, 10);
        //        buttonNo.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JLabel labelInfo = new JLabel("<HTML><H2>Введите имя пользователя</H2></HTML>");
        labelInfo.setPreferredSize(new Dimension(200, 40));
        //        getRootPane().setDefaultButton(buttonYes);

        //        contentPane.setLayout(new BorderLayout());
//        Box box = Box.createVerticalBox();

        setContentPane(box);

        buttonNext.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (createUser.isSelected())
                    needCreate=true;
                else if (loadUser.isSelected())
                    needCreate=false;
                else
                    return;

                if (userName.getText()==null || userName.getText().isEmpty())
                    return;
                else
                    name = userName.getText();

                close();
            }
        });

        buttonExit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                name="";
                close();
            }
        });

        Box boxInfo = Box.createVerticalBox();
        boxInfo.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        boxInfo.add(labelInfo);
        boxInfo.add(userName);
        boxInfo.add(createUser);
        boxInfo.add(loadUser);


        box.add(boxInfo);
//        box.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        box.add(boxButton);
//        box.add(Box.createHorizontalGlue());

        setSize(350, 200);
//        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

    }

    @Override   // todo may be need returned some MAP
    protected void saveAnswer()
    {
        if (needCreate)
            userAnswer = name  + "#" + Utils.YES;
        else
            userAnswer = name  + "#" + Utils.NO;
    }

    public void close()
    {
        super.close();
        log.error(needCreate);
    }
}
