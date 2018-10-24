package system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
import static system.Main.standart;

public class Dialog{
    static JDialog dialog;
    static JTextField inputTextField;
    static JTextArea outputTextArea;
    static Checkbox checkBox;
    static JComboBox comboBox;
    static JButton buttonAdd;
    static JButton buttonCancel;

    public static void createDialogAboutDoc(JFrame frame, ArrayList<Development> listOfDetail) {
        dialog = new JDialog(frame, "Add Document", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        dialog.setLayout(gridBagLayout);
        inputTextField = new JTextField(10);
        checkBox = new Checkbox("for all");
        buttonAdd = new JButton("Add");
        buttonCancel = new JButton("Cancel");
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 10, 10, 10);
        dialog.add(inputTextField, constraints);

        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        dialog.add(checkBox, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonAdd.setEnabled(false);
        dialog.add(buttonAdd, constraints);

        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 3;
        constraints.gridy = 1;
        dialog.add(buttonCancel, constraints);

        buttonAddDocActionListener(dialog, buttonAdd, listOfDetail);
        buttonCancelActionListener(dialog, buttonCancel);
        inputNameOfDocTextFieldActionListener(listOfDetail);

        dialog.setPreferredSize(new Dimension(350, 200));
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void createDialogAboutSet(JFrame frame, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList) {
        dialog = new JDialog(frame, "Add Set of Documents", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        dialog.setLayout(gridBagLayout);
        inputTextField = new JTextField(GUI.detailComboBox.getSelectedItem().toString());
        outputTextArea = new JTextArea("Set of documents for \""+ GUI.detailComboBox.getSelectedItem()+"\" contains:",4,15);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        checkBox = new Checkbox();
        comboBox = new JComboBox();
        buttonAdd = new JButton("Add");
        buttonCancel = new JButton("Cancel");
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 5, 7, 5);
        dialog.add(inputTextField, constraints);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 2;
        constraints.gridwidth = 1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        dialog.add(outputTextArea, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        checkBox.setEnabled(false);
        dialog.add(checkBox, constraints);

        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 1;
        for(int i=0;i<standart.getCountOfDocuments();i++)
            comboBox.addItem(standart.getNameOfDocument(i));
        comboBox.setSelectedIndex(-1);
        dialog.add(comboBox, constraints);

        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 2;
        dialog.add(buttonAdd, constraints);

        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 3;
        constraints.gridy = 2;
        dialog.add(buttonCancel, constraints);

        ArrayList<String> listOfNamesOfSet = new ArrayList<>();
        ArrayList<String> listOfExistingSets = new ArrayList<>();
        for (SetOfDocuments setOfDocuments:setOfDocumentsArrayList
        ) {
            listOfNamesOfSet.add(setOfDocuments.getNameOfSet());
            listOfExistingSets.add(setOfDocuments.getNameOfSet());
        }
        ArrayList<Character> listOfAddedDocs = new ArrayList<Character>();
        for(int i = 0; i< GUI.documentComboBox.getItemCount(); i++) {
            for(int j=0;j<comboBox.getItemCount();j++)
                if(comboBox.getItemAt(j).equals(GUI.documentComboBox.getItemAt(i))) {
                    listOfAddedDocs.add(comboBox.getItemAt(j).toString().charAt(0));
                }
        }
        infoTextArea(listOfNamesOfSet, listOfAddedDocs);
        buttonAddSetActionListener(dialog, listOfDetail, setOfDocumentsArrayList, listOfExistingSets, listOfAddedDocs);
        buttonCancelActionListener(dialog, buttonCancel);
        comboBoxAddActionListener(listOfAddedDocs);
        checkBoxStateListener(listOfNamesOfSet, listOfAddedDocs);
        inputNameOfSetTextFieldActionListener(listOfNamesOfSet, listOfAddedDocs);

        dialog.setPreferredSize(new Dimension(350, 200));
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void createDialogAboutDeatil(JFrame frame, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList) {
        dialog = new JDialog(frame,"Add Detail", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        dialog.setLayout(gridBagLayout);
        inputTextField = new JTextField(10);
        comboBox = new JComboBox();
        buttonAdd = new JButton("Add");
        buttonCancel = new JButton("Cancel");
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 5, 5, 5);
        dialog.add(inputTextField, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 2;
        constraints.gridy = 0;
        for (SetOfDocuments set:setOfDocumentsArrayList
             ) {
            comboBox.addItem(set.getNameOfSet());
        }
        comboBox.setSelectedIndex(-1);
        dialog.add(comboBox, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonAdd.setEnabled(false);
        dialog.add(buttonAdd, constraints);

        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 2;
        constraints.gridy = 1;
        dialog.add(buttonCancel, constraints);

        ArrayList<String> listOfNamesOfDetails = new ArrayList<String>();
        for (Development detail:listOfDetail
        ) {
            listOfNamesOfDetails.add(detail.getName());
        };
        inputNameOfDetailTextFieldActionListener(listOfNamesOfDetails);
        comboBoxAddSetActionListener();
        buttonAddDetailActionListener(dialog,listOfDetail, setOfDocumentsArrayList);
        buttonCancelActionListener(dialog, buttonCancel);

        dialog.setPreferredSize(new Dimension(350, 200));
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void buttonAddDocActionListener(JDialog dialog, JButton buttonAdd, ArrayList<Development> listOfDetail) {
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkBox.getState()) {
                    for (Development detail : listOfDetail
                    ) {
                        int indexOfLastDoc = listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).getCountOfDocuments();
                        detail.addDocument(inputTextField.getText().charAt(0), false);
                    }
                }
                else{
                    int indexOfLastDoc = listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).getCountOfDocuments();
                    listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).addDocument( inputTextField.getText().charAt(0), false);
                };
                dialog.dispose();
                GUI.updateStateOfDocuments(listOfDetail);
            }
        });
    };

    public static void buttonAddSetActionListener(JDialog dialog, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList, ArrayList<String> listOfNameOfSet, ArrayList<Character> listOfAddedDocs){
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listOfNameOfSet.contains(inputTextField.getText())) {
                    int select = JOptionPane.showConfirmDialog(null, "\"" + inputTextField.getText() + "\" set of documents is already exists! Do you want replace the existing set?",
                            "WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (select == JOptionPane.YES_OPTION) {
                        SetOfDocuments addedSet = new SetOfDocuments(inputTextField.getText(), listOfAddedDocs);
                        listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).setSetOfDocuments(addedSet);
                        setOfDocumentsArrayList.add(addedSet);
                        dialog.dispose();
                        GUI.updateStateOfDocuments(listOfDetail);
                    }
                    if (select == JOptionPane.NO_OPTION)
                        inputTextField.selectAll();
                }
                else{
                    SetOfDocuments addedSet = new SetOfDocuments(inputTextField.getText(), listOfAddedDocs);
                    listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).setSetOfDocuments(addedSet);
                    setOfDocumentsArrayList.add(addedSet);
                    dialog.dispose();
                    GUI.updateStateOfDocuments(listOfDetail);
                }
            }
        });
    }

    public static void buttonAddDetailActionListener(JDialog dialog, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList){
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextField.getActionListeners()[0].actionPerformed(new ActionEvent(inputTextField,BUTTON1_DOWN_MASK,"Input"));
                if(!inputTextField.getText().equals("")) {
                    int indexOfNewDetail = listOfDetail.size();
                    listOfDetail.add(indexOfNewDetail, new Development(inputTextField.getText(), indexOfNewDetail, setOfDocumentsArrayList.get(comboBox.getSelectedIndex())));
                    dialog.dispose();
                    GUI.updateListOfDetails(listOfDetail, indexOfNewDetail);
                }
            }
        });
    }

    public static void buttonCancelActionListener(JDialog dialog, JButton buttonCancel){
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

    public static void comboBoxAddActionListener(ArrayList<Character> listOfAddedDocs){
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                checkBox.setEnabled(true);
                checkBox.setState(false);
                if(listOfAddedDocs.contains(comboBox.getSelectedItem().toString().charAt(0)))
                    checkBox.setState(true);
            }
        });
    }

    public static void comboBoxAddSetActionListener(){
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(!inputTextField.getText().equals(""))
                    buttonAdd.setEnabled(true);
            }
        });
    }

    public static void checkBoxStateListener(ArrayList<String> listOfNameOfSet, ArrayList<Character> listOfAddedDocs){
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int AMOUNT_OF_DOCS = listOfAddedDocs.size();
                if(checkBox.getState()) {
                    if (!listOfAddedDocs.contains(comboBox.getSelectedItem().toString().charAt(0)))
                        listOfAddedDocs.add(comboBox.getSelectedItem().toString().charAt(0));
                }
                else
                    listOfAddedDocs.remove((Character) comboBox.getSelectedItem().toString().charAt(0));
                if(AMOUNT_OF_DOCS!=listOfAddedDocs.size())
                    infoTextArea(listOfNameOfSet, listOfAddedDocs);
            }
        });
    }

    public static void inputNameOfDocTextFieldActionListener(ArrayList<Development> listOfDetail) {
        inputTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (Dialog.validityOfInput(listOfDetail, inputTextField.getText(), checkBox.getState())) {
                    case 1: {
                        Dialog.buttonAdd.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Press button to \"Add\" document " + inputTextField.getText() + " for the selected detail.");
                        break;
                    }
                    case 2: {
                        Dialog.buttonAdd.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Press button to \"Add\" document " + inputTextField.getText() + " for all details.");
                        break;
                    }
                    case 3: {
                        JOptionPane.showMessageDialog(null, "\"" + inputTextField.getText() + "\"  - WRONG NAME OF DOCUMENT!\nIt must contain one capital latin letter.");
                        inputTextField.setText("");
                        break;
                    }
                    case 0: {
                        JOptionPane.showMessageDialog(null, "\"" + inputTextField.getText() + "\"  - WRONG NAME OF DOCUMENT!\nThis document is already exists.");
                        inputTextField.setText("");
                        break;
                    }
                    default: {
                        JOptionPane.showMessageDialog(null, "\"" + inputTextField.getText() + "\"  - WRONG NAME OF DOCUMENT!");
                        inputTextField.setText("");
                        break;
                    }
                }
            }
        });
    }

    public static void inputNameOfSetTextFieldActionListener(ArrayList<String> listOfNameOfSet, ArrayList<Character> listOfAddedDocs){
        inputTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern p = Pattern.compile("[\\w\\d]+");
                Matcher m = p.matcher(inputTextField.getText());
                if (!m.matches()) {
                    JOptionPane.showMessageDialog(null, "\"" + inputTextField.getText() + "\"  - WRONG NAME OF DOCUMENT!\nName of the set of documents must not contain whitespace.");
                    inputTextField.selectAll();
                }
                else {
                    listOfNameOfSet.add(inputTextField.getText());
                    infoTextArea(listOfNameOfSet, listOfAddedDocs);
                }
            }
        });
    }

    public static void inputNameOfDetailTextFieldActionListener(ArrayList<String> listOfNamesOfDetails){
        inputTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern p = Pattern.compile("[\\w\\d]+");
                Matcher m = p.matcher(inputTextField.getText());
                if (!m.matches()) {
                    JOptionPane.showMessageDialog(null, "\"" + inputTextField.getText() + "\"  - WRONG NAME OF DOCUMENT!\nName of the set of documents must not contain whitespace.");
                    inputTextField.setText("");
                }
                if(listOfNamesOfDetails.contains(inputTextField.getText())){
                    JOptionPane.showMessageDialog(null, "\"" + inputTextField.getText() + "\"  - WRONG NAME OF DOCUMENT!\nSuch detail already exists!");
                    inputTextField.setText("");
                }
                if(!inputTextField.getText().equals("")&&comboBox.getSelectedIndex()!=-1)
                    buttonAdd.setEnabled(true);
            }

        });
    }

    public static int validityOfInput(ArrayList<Development> details, String text, boolean state) {
        int validity = 0;
        ArrayList<Character> listOfExistingDocuments = new ArrayList<Character>();
        String REGEX = "^[A-Z]{1}$";
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(text);
        if (!m.find()) {
            validity = 3;
        }
        if(state){
            for (int i=0; i<details.size();i++)
                for(int j=0; j<details.get(i).getCountOfDocuments();j++)
                    if(!listOfExistingDocuments.contains(details.get(i).getNameOfDocument(j))){
                        listOfExistingDocuments.add(details.get(i).getNameOfDocument(j));
                    };
            if(!listOfExistingDocuments.contains(text.charAt(0)))
                validity = 2;
        }
        else{
            for(int i = 0; i<details.get(GUI.detailComboBox.getSelectedIndex()).getCountOfDocuments(); i++)
                listOfExistingDocuments.add(details.get(GUI.detailComboBox.getSelectedIndex()).getNameOfDocument(i));
            if(!listOfExistingDocuments.contains(text.charAt(0)))
                validity = 1;
        }
        return validity;
    }

    public static void infoTextArea(ArrayList<String> listOfNameOfSet, ArrayList<Character> listOfAddedDocs){
        String nameOfSet = "";
        if(inputTextField.getText().equals(nameOfSet))
            nameOfSet = listOfNameOfSet.get(listOfNameOfSet.indexOf(GUI.detailComboBox.getSelectedItem()));
        else {
            nameOfSet = listOfNameOfSet.get(listOfNameOfSet.indexOf(inputTextField.getText()));
        }
        outputTextArea.setText("Set of documents \""+nameOfSet+"\" contains:"+listOfAddedDocs);
    }
}
