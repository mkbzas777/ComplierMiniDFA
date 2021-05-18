package gui;

import entity.NFA;
import process.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Stack;

public class MyFrame extends JFrame {
    ImageIcon imageIcon = new ImageIcon();
    JLabel showImage = new JLabel();
    JPanel panel1 = new JPanel();
    JButton showNFA = new JButton("NFA");
    JButton showDFA = new JButton("DFA");
    JButton showMiniDFA = new JButton("MiniDFA");
    JTextField lambda = new JTextField(20);
    JLabel suffix = new JLabel();
    JButton confirm = new JButton("Confirm");
    public MyFrame()
    {
        setBounds(300,300,1400,600);
        setTitle("NFA->DFA->miniDFA");
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(IsLegal(lambda.getText())) {
                    Main.input = lambda.getText();
                    Main.input = Main.Format(Main.input);
                    System.out.println(Main.input);
                    String Suffix = InfixToSuffix.Convert(Main.input);
                    suffix.setText("逆波兰表达式:" + Suffix);
                    System.out.println(Suffix);
                    SuffixToNFA.Convert(Suffix);
                    NFAToDFA.Convert();
                    DFAToMiniDFA.Convert();
                    Main.RemoveAll();
                }
                else
                    JOptionPane.showMessageDialog(null,"Input error, please check input!");
            }
        });
        showDFA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File("Z:\\Idea_Project\\ComplierMiniDFA\\DFA.png");
                Image image = null;
                try {
                    image = ImageIO.read(file);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                imageIcon.setImage(image);
                repaint();
            }
        });
        showNFA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File("Z:\\Idea_Project\\ComplierMiniDFA\\NFA.png");
                Image image = null;
                try {
                    image = ImageIO.read(file);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                imageIcon.setImage(image);
                repaint();
            }
        });
        showMiniDFA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File("Z:\\Idea_Project\\ComplierMiniDFA\\miniDFA.png");
                Image image = null;
                try {
                    image = ImageIO.read(file);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                assert image != null;
                imageIcon.setImage(image);
                repaint();
            }
        });

        panel1.add(lambda);
        panel1.add(confirm);

        panel1.add(showNFA);
        panel1.add(showDFA);
        panel1.add(showMiniDFA);
        panel1.add(suffix);
//        suffix.setText("逆波兰表达式:"+Main.input);
        add(panel1, BorderLayout.NORTH);

        showImage.setBounds(40,40,1000,500);

        showImage.setIcon(imageIcon);

        add(showImage,BorderLayout.CENTER);
        File file = new File("Z:\\Idea_Project\\ComplierMiniDFA\\load.png");
        Image image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        assert image != null;
        imageIcon.setImage(image);
        setVisible(true);
    }
    public String judeg = "abcdefghigjlmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ()*|";
    public boolean IsLegal(String str)
    {
        Stack<Character> stack = new Stack<>();
        for(int i=0;i<str.length();i++)
        {
            if(judeg.contains(String.valueOf(str.charAt(i))))
            {
                if(str.charAt(i)=='(')
                    stack.push(str.charAt(i));
                if(str.charAt(i)==')')
                {
                    if(!stack.isEmpty())
                        stack.pop();
                    else
                        return false;
                }
            }
            else
                return false;
        }
        return stack.isEmpty();
    }

}
