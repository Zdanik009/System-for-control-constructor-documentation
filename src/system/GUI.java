package system;
import system.Controler;
import system.Development;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.security.DigestException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI{
    static JFrame frame;
    static JPanel panel;
    static JLabel detailsLabel;
    static JComboBox detailComboBox;
    static JTextArea textArea;
    static JButton addDetailDialogButton;
    static JLabel documentsLabel;
    static Checkbox stateCheckBox;
    static JComboBox documentComboBox;
    static JButton addDocDialogButton;
    static JButton addSetDialogButton;
    static int items;
    static JProgressBar progressBar;
    static JButton confirmButton;

    public static void createGUI(ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList, OutputStream fileOutputStream, StringBuffer report) {
        frame = new JFrame("System for control constructor documentation");
        //frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        detailsLabel = new JLabel("Details:");
        detailComboBox = new JComboBox();
        textArea = new JTextArea("<= Choose detail.",15,25);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        addDetailDialogButton = new JButton("Add Detail");
        documentsLabel = new JLabel("Documents:");
        addDocDialogButton = new JButton("Add Document");
        addSetDialogButton = new JButton("Add Set of Documents");
        stateCheckBox = new Checkbox();
        documentComboBox = new JComboBox();
        progressBar = new JProgressBar();
        confirmButton = new JButton("Confirm readiness");
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.WEST;
        c.fill   = GridBagConstraints.NONE;
        c.gridheight = 1;
        c.gridwidth  = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 10, 5);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        panel.add(detailsLabel,c);

        c.anchor = GridBagConstraints.CENTER;
        c.fill   = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth  = 2;
        c.gridx = 2;
        c.gridy = 0;
        /*for (Development items:listOfDetail) {
            detailComboBox.addItem(items.getName());
        };
        items = detailComboBox.getItemCount();
        detailComboBox.setSelectedIndex(-1);*/
        updateListOfDetails(listOfDetail,-1);
        panel.add(detailComboBox, c);

        c.fill   = GridBagConstraints.BOTH;
        c.gridheight = 5;
        c.gridwidth = 2;
        c.gridx = 4;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 10, 5);
        panel.add(textArea, c);

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        addDetailDialogButton.setEnabled(false);
        panel.add(addDetailDialogButton,c);

        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(50,5,10,5);
        panel.add(documentsLabel,c);

        c.anchor = GridBagConstraints.CENTER;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 2;
        stateCheckBox.setEnabled(false);
        panel.add(stateCheckBox, c);

        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 3;
        c.gridy = 2;
        documentComboBox.setEnabled(false);
        panel.add(documentComboBox, c);

        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0,5,10,5);
        addDocDialogButton.setEnabled(false);
        panel.add(addDocDialogButton,c);

        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        addSetDialogButton.setEnabled(false);
        panel.add(addSetDialogButton,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 5;
        panel.add(progressBar,c);

        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 4;
        c.gridy = 5;
        confirmButton.setEnabled(false);
        panel.add(confirmButton, c);

        frame.getContentPane().add(panel);
        frame.setPreferredSize(new Dimension(580, 380));

        frame.pack();
        frame.setVisible(true);
        Controler.ActionListening(listOfDetail, setOfDocumentsArrayList, fileOutputStream, report);
    };

    public static void updateStateOfDocuments(ArrayList<Development> listOfDetail){
        GUI.documentComboBox.removeAllItems();
        String docs = "";
        for(int i=0;i<listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).getCountOfDocuments();i++) {
            GUI.documentComboBox.addItem(listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).getNameOfDocument(i));
            docs += GUI.documentComboBox.getItemAt(i)+" ";
        }
        documentComboBox.setSelectedIndex(-1);
        GUI.progressBar.setValue(GUI.progressOfDocCollection(listOfDetail.get(GUI.detailComboBox.getSelectedIndex())));
        GUI.textArea.setText("You choose \"" + GUI.detailComboBox.getSelectedItem() + "\" " + (GUI.detailComboBox.getSelectedIndex() + 1)
                + "/" + GUI.items + ".\nDetail need to have documents: "+docs+".\n"+GUI.stateOfDocuments(listOfDetail.get(GUI.detailComboBox.getSelectedIndex())));
    };

    public static void updateListOfDetails(ArrayList<Development> listOfDetail, int indexOfNewDetail){
        GUI.detailComboBox.removeAllItems();
        for (Development items:listOfDetail) {
            GUI.detailComboBox.addItem(items.getName());
        };
        GUI.items = GUI.detailComboBox.getItemCount();
        GUI.detailComboBox.setSelectedIndex(indexOfNewDetail);
    }

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
            confirmButton.setEnabled(true);
            GUI.progressBar.setValue(100);
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
}