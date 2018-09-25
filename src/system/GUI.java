package system;
import system.Controler;
import system.Development;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GUI{
    static JFrame frame;
    static JPanel panel;
    static JComboBox comboBox;
    static JTextArea textArea;
    static Checkbox checkbox1;
    static Checkbox checkbox2;
    static Checkbox checkbox3;
    static Checkbox checkbox4;
    static int items;
    static JButton button;
    public static void createGUI(ArrayList<Development> listOfDetail, OutputStream fileOutputStream, StringBuffer report) {
        frame = new JFrame("Test");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        comboBox = new JComboBox();
        textArea = new JTextArea("<- Choose detail.");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        checkbox1 = new Checkbox();
        checkbox2 = new Checkbox();
        checkbox3 = new Checkbox();
        checkbox4 = new Checkbox();
        /*checkboxes = new ArrayList<Checkbox>();
        checkboxes.add(0,checkbox1);
        checkboxes.add(1,checkbox2);
        checkboxes.add(2,checkbox3);
        checkboxes.add(3,checkbox4);*/
        button = new JButton("Confirm readiness");
        /*AddElementsOnFrame();
    }

    public static void AddElementsOnFrame(){*/
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 10 , 10, 10);
        for (Development items:listOfDetail) {
            comboBox.addItem(items.getName());
        };
        items = comboBox.getItemCount();
        comboBox.setSelectedIndex(-1);
        panel.add(comboBox, c);

        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 6;
        c.gridwidth = 1;
        c.gridx = 4;
        c.gridy = 0;
        panel.add(textArea, c);

        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(40, 10 , 10, 10);
        panel.add(checkbox1, c);
        c.gridx = 1;
        c.gridy = 5;
        panel.add(checkbox2, c);
        c.gridx = 2;
        c.gridy = 5;
        panel.add(checkbox3, c);
        c.gridx = 3;
        c.gridy = 5;
        panel.add(checkbox4, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 4;
        c.gridy = 6;
        c.insets = new Insets(0, 0 , 0, 0);
        button.setEnabled(false);
        panel.add(button, c);

        frame.getContentPane().add(panel);
        frame.setPreferredSize(new Dimension(450, 250));

        frame.pack();
        frame.setVisible(true);
        Controler.ActionListening(listOfDetail, fileOutputStream, report);
    };

    public static String stateofDocuments(){
        String text = new String();
        if(GUI.checkbox1.getState()&&GUI.checkbox2.getState()&&
                GUI.checkbox3.getState()&&GUI.checkbox4.getState()){
            text = "You have all documents for development.";
            button.setEnabled(true);
        }
        else{
            if (!GUI.checkbox1.getState())
                text += "There is NO Document 1!\n";
            if (!GUI.checkbox2.getState())
                text += "There is NO Document 2!\n";
            if (!GUI.checkbox3.getState())
                text += "There is NO Document 3!\n";
            if (!GUI.checkbox4.getState())
                text += "There is NO Document 4!\n";
        }
        return text;
    };

    public static void saveReportToFile(OutputStream fileOutputStream, StringBuffer report){
        try {
            for(int i=0;i<report.length();i++) {
                fileOutputStream.write(report.charAt(i));
            };
            fileOutputStream.flush();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
