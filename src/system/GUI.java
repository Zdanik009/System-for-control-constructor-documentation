package system;
import system.Controler;
import system.Development;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI{
    static JFrame frame;
    static JPanel panel;
    static JComboBox comboBox;
    static JTextArea textArea;
    static JLabel textLabel;
    static JTextField textField;
    static Checkbox textCheckBox;
    static JButton textButton;
    static JLabel checkLabel;
    static Checkbox checkbox;
    static JComboBox checkComboBox;
    static int items;
    static JProgressBar progressBar;
    static JButton button;
    public static void createGUI(ArrayList<Development> listOfDetail, OutputStream fileOutputStream, StringBuffer report) {
        frame = new JFrame("Test");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        comboBox = new JComboBox();
        textArea = new JTextArea("<- Choose detail.",15,24);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textLabel = new JLabel("Add new document:");
        textField = new JTextField(10);
        textCheckBox = new Checkbox("for all");
        textButton = new JButton("Add");
        checkLabel = new JLabel("Documents:");
        checkbox = new Checkbox();
        checkComboBox = new JComboBox();
        progressBar = new JProgressBar();
        button = new JButton("Confirm readiness");


        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill   = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth  = 4;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 10, 10);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        for (Development items:listOfDetail) {
            comboBox.addItem(items.getName());
        };
        items = comboBox.getItemCount();
        comboBox.setSelectedIndex(-1);
        panel.add(comboBox, c);

        c.gridheight = 5;
        c.gridwidth = 2;
        c.gridx = 4;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        panel.add(textArea, c);

        c.gridheight = 1;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(textLabel,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(10, 10, 10, 10);
        textField.setEnabled(false);
        panel.add(textField,c);

        c.fill = GridBagConstraints.NONE;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 2;
        c.insets = new Insets(10, 10, 10, 10);
        textCheckBox.setEnabled(false);
        panel.add(textCheckBox,c);

        c.fill = GridBagConstraints.NONE;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 3;
        c.gridy = 2;
        c.insets = new Insets(10, 10, 10, 10);
        textButton.setEnabled(false);
        panel.add(textButton,c);

        c.gridheight = 1;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(checkLabel,c);

        c.fill = GridBagConstraints.NONE;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        checkbox.setEnabled(false);
        panel.add(checkbox, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        checkComboBox.setEnabled(false);
        panel.add(checkComboBox, c);

        c.gridheight = 1;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(0, 10, 0, 10);
        panel.add(progressBar,c);

        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 4;
        c.gridy = 5;
        button.setEnabled(false);
        panel.add(button, c);

        frame.getContentPane().add(panel);
        frame.setPreferredSize(new Dimension(580, 380));

        frame.pack();
        frame.setVisible(true);
        Controler.ActionListening(listOfDetail, fileOutputStream, report);
    };

    public static String stateOfDocuments(Development detail){
        String text = new String();
        int key=detail.getCountOfDocuments();
        for(int i=0;i<detail.getCountOfDocuments();i++) {
            if(!detail.getStateOfDocument(detail.getNameOfDocument(i).toString())) {
                text += "There is NO Document  " + detail.getNameOfDocument(i).toString() + "!\n";
                key--;
            }
        }
        if(key==detail.getCountOfDocuments()) {
            text = "You have all documents for development.";
            button.setEnabled(true);
        }
        return text;
    };

    public static int progressOfDocCollection(Development detail){
        double progress = 0;
        double progressForOneDoc = 100/detail.getCountOfDocuments();
        for(int i=0;i<detail.getCountOfDocuments();i++) {
            if(detail.getStateOfDocument(detail.getNameOfDocument(i).toString())) {
               progress += progressForOneDoc;
            }
        }
        return (int) progress;
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
    };

    public static int validOfDoc(ArrayList<Development> details, String text, boolean state){
        int validity = 0;
        String REGEX = "^[A-Z]{1}$";
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(text);
        if(!m.find())
            validity = 3;
        if(state){
            int maxCountOfDoc = 0;
            int indexOfDetail = 0;
            for (int i=0;i<details.size();i++) {
               if(maxCountOfDoc<details.get(i).getCountOfDocuments()) {
                   maxCountOfDoc = details.get(i).getCountOfDocuments();
                   indexOfDetail=i;
               }
            };
            String REGEX2 = "^[\\x"+(41+maxCountOfDoc)+"-Z]{1}$";
            p = Pattern.compile(REGEX2);
            m = p.matcher(text);
            if(m.find()){
                validity = 2;
            }
        }
        else {
            String REGEX1 = "^[\\x" + (41+details.get(comboBox.getSelectedIndex()).getCountOfDocuments())+ "-Z]{1}$";
            p = Pattern.compile(REGEX1);
            m = p.matcher(text);
            if(m.find()){
                validity = 1;
            }
        }
        return validity;
    };
}
