package system;

import sql.database.SQLite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
//import static system.Main.standart;

public class Dialog{
    static JDialog dialog;
    static JTextField inputTextField;
    static JTextArea outputTextArea;
    static Checkbox checkBox;
    static JComboBox comboBox;
    static JButton buttonAdd;
    static JButton buttonCancel;
    static Path MANUALFILE = Paths.get("D://project//Manual.txt");

    public static void createDialogAboutDoc(JFrame frame, SQLite sqliteDatabase, ArrayList<Development> listOfDetail) {
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

        buttonAddDocActionListener(sqliteDatabase, dialog, buttonAdd, listOfDetail);
        buttonCancelActionListener(dialog, buttonCancel);
        inputNameOfDocTextFieldActionListener(listOfDetail);

        dialog.setPreferredSize(new Dimension(350, 200));
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void createDialogAboutSet(JFrame frame, SQLite sqliteDatabase, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList) {
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
        try{
            ResultSet resultSet = sqliteDatabase.queryForExtraction("SELECT name_of_document FROM documents;");
            while(resultSet.next())
                comboBox.addItem(resultSet.getString("name_of_document"));
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
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
        buttonAddSetActionListener(sqliteDatabase, dialog, listOfDetail, setOfDocumentsArrayList, listOfExistingSets, listOfAddedDocs);
        buttonCancelActionListener(dialog, buttonCancel);
        comboBoxAddActionListener(listOfAddedDocs);
        checkBoxStateListener(listOfNamesOfSet, listOfAddedDocs);
        inputNameOfSetTextFieldActionListener(listOfNamesOfSet, listOfAddedDocs);

        dialog.setPreferredSize(new Dimension(350, 200));
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void createDialogAboutDeatil(JFrame frame, SQLite sqliteDatabse, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList) {
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
        buttonAddDetailActionListener(sqliteDatabse, dialog,listOfDetail, setOfDocumentsArrayList);
        buttonCancelActionListener(dialog, buttonCancel);

        dialog.setPreferredSize(new Dimension(350, 200));
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void createDialogAboutHelp(JFrame frame){
        dialog = new JDialog(frame, "HELP", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        dialog.setLayout(gridBagLayout);
        StringBuffer stringBuffer = new StringBuffer("MANUAL FILE NOT FOUND :(");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(MANUALFILE.toFile()), Charset.forName("UTF-8")));
            stringBuffer.setLength(0);
            String stringLine = "";
            while((stringLine = reader.readLine())!=null){
                if(stringLine.equals("Базовые инструкции пользования программой:")) {
                    stringLine = "";
                    while((stringLine = reader.readLine())!=null)
                        stringBuffer.append(stringLine.replaceAll("[\\t]", " "));
                }
            }
            reader.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        outputTextArea = new JTextArea(stringBuffer.toString(),15,30);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        buttonAdd = new JButton("Read more");
        buttonCancel = new JButton("Cancel");
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 3;
        constraints.gridwidth = 4;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 5, 10, 5);
        dialog.add(outputTextArea,constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 3;
        dialog.add(buttonAdd,constraints);

        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 3;
        constraints.gridy = 3;
        dialog.add(buttonCancel,constraints);

        buttonOpenManualActionListener(buttonAdd);
        buttonCancelActionListener(dialog, buttonCancel);

        dialog.setPreferredSize(new Dimension(380, 380));
        dialog.pack();
        dialog.setVisible(true);
    };

    public static void createDialogAboutProgramm(JFrame frame){
        dialog = new JDialog(frame, "About Programm", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        dialog.setLayout(gridBagLayout);
        StringBuffer stringBuffer = new StringBuffer("MANUAL FILE NOT FOUND :(");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(MANUALFILE.toFile()), Charset.forName("UTF-8")));
            stringBuffer.setLength(0);
            String stringLine = "";
            while((stringLine = reader.readLine())!=null){
                if(stringLine.contains("Версия:"))
                    stringBuffer.append(stringLine.replace("[\\t]", " "));
                if(stringLine.equals("Описание главного окна программы:")) {
                    while(!(stringLine = reader.readLine()).equals("Принцип работы элементов главного окна:")) {
                        if(!stringLine.matches("[\\n]$")&&!stringLine.equals(""))
                            stringBuffer.append("\n"+stringLine.replaceAll("[\\t]", " "));
                    }
                    break;
                }
            }
            reader.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        outputTextArea = new JTextArea(stringBuffer.toString(),10,34);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        buttonAdd = new JButton("Read more");
        buttonCancel = new JButton("Cancel");
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 3;
        constraints.gridwidth = 4;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 5, 10, 5);
        dialog.add(outputTextArea,constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 3;
        dialog.add(buttonAdd,constraints);

        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 3;
        constraints.gridy = 3;
        dialog.add(buttonCancel,constraints);

        buttonOpenManualActionListener(buttonAdd);
        buttonCancelActionListener(dialog, buttonCancel);

        dialog.setPreferredSize(new Dimension(380, 380));
        dialog.pack();
        dialog.setVisible(true);
    };

    public static void createDialogAboutAuthor(JFrame frame){
        dialog = new JDialog(frame, "About Author", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        dialog.setLayout(gridBagLayout);
        StringBuffer stringBuffer = new StringBuffer("MANUAL FILE NOT FOUND :(");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(MANUALFILE.toFile()), Charset.forName("UTF-8")));
            stringBuffer.setLength(0);
            String stringLine = "";
            while((stringLine = reader.readLine())!=null){
                if(stringLine.equals("Об авторе:")) {
                    while(!(stringLine = reader.readLine()).equals("Описание программы \"System For Control Constructor Documentation\""))
                        stringBuffer.append(stringLine.replaceAll("[\\t]", " ")+"\n");
                    break;
                }
            }
            reader.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        outputTextArea = new JTextArea(stringBuffer.toString(),15,30);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        buttonAdd = new JButton("Read more");
        buttonCancel = new JButton("Cancel");
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 3;
        constraints.gridwidth = 4;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 5, 10, 5);
        dialog.add(outputTextArea,constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 3;
        dialog.add(buttonAdd,constraints);

        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 3;
        constraints.gridy = 3;
        dialog.add(buttonCancel,constraints);

        buttonOpenManualActionListener(buttonAdd);
        buttonCancelActionListener(dialog, buttonCancel);

        dialog.setPreferredSize(new Dimension(380, 380));
        dialog.pack();
        dialog.setVisible(true);
    };

    /*public static void createTableForDetails(JFrame frame, SQLite sqliteDatabase){
        dialog = new JDialog(frame, "About Author", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        dialog.setLayout(gridBagLayout);
        JLabel labelForTables = new JLabel("TABLES");
        JButton buttonForDetails = new JButton("Details");
        JButton buttonForDocuments = new JButton("Documents");
        JButton buttonForSetOfDocuments = new JButton("Set of documents");
        buttonCancel = new JButton("Cancel");
        try{

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        JTable table = new JTable();
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 3;
        constraints.gridwidth = 4;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 5, 10, 5);
        dialog.add(,constraints);

        buttonCancelActionListener(dialog, buttonCancel);

        dialog.setPreferredSize(new Dimension(380, 380));
        dialog.pack();
        dialog.setVisible(true);
    };*/

    public static void createTableForDocuments(JFrame frame, SQLite sqliteDatabase){
        dialog = new JDialog(frame, "About Author", true);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        String[][] dataForTable = {};
        ArrayList<String> nameOfDocument = new ArrayList<>();
        ArrayList<Integer> document_id = new ArrayList<>();
        ArrayList<String> name_of_sets = new ArrayList<>();
        try{
            ResultSet resultSet = sqliteDatabase.queryForExtraction("SELECT * FROM documents;");
            while(resultSet.next()) {
                nameOfDocument.add(resultSet.getString("name_of_document"));
                document_id.add(resultSet.getInt("document_id"));
            }
            resultSet.close();
            dataForTable = new String[nameOfDocument.size()][2];
            for(int i = 0; i < nameOfDocument.size(); i++) {
                dataForTable[i][0] = nameOfDocument.get(i);
                dataForTable[i][1] = "";
                resultSet = sqliteDatabase.queryForExtraction("SELECT name_of_documents_set FROM details WHERE set_of_documents_id IN (SELECT set_of_documents_id_fk FROM set_of_documents WHERE document_id_fk = " + document_id.get(i) + ");");
                while (resultSet.next())
                    name_of_sets.add(resultSet.getString("name_of_documents_set"));
                for (int j = 0; j < name_of_sets.size(); j++)
                    dataForTable[i][1] = dataForTable[i][1] + name_of_sets.get(j) + " ";
                name_of_sets.clear();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        Object[] namesOfColumn = new String[] {"Name of document", "Sets containing document"};
        JTable table = new JTable(dataForTable, namesOfColumn);
        panel.add(table);
        dialog.add(panel);
        dialog.setPreferredSize(new Dimension(500, 380));
        dialog.pack();
        dialog.setVisible(true);
    };

    /*public static void createTableForSetOfDocuments(JFrame frame, SQLite sqliteDatabase){
        dialog = new JDialog(frame, "About Author", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        dialog.setLayout(gridBagLayout);
        JLabel labelForTables = new JLabel("TABLES");
        JButton buttonForDetails = new JButton("Details");
        JButton buttonForDocuments = new JButton("Documents");
        JButton buttonForSetOfDocuments = new JButton("Set of documents");
        buttonCancel = new JButton("Cancel");
        try{

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        JTable table = new JTable();
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 3;
        constraints.gridwidth = 4;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 5, 10, 5);
        dialog.add(,constraints);

        buttonCancelActionListener(dialog, buttonCancel);

        dialog.setPreferredSize(new Dimension(380, 380));
        dialog.pack();
        dialog.setVisible(true);
    };*/

    public static void buttonAddDocActionListener(SQLite sqliteDatabase, JDialog dialog, JButton buttonAdd, ArrayList<Development> listOfDetail) {
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int document_id = 0;
                    ResultSet resultSet = sqliteDatabase.queryForExtraction("SELECT * FROM documents;");
                    while(resultSet.next())
                        if(inputTextField.getText().equals(resultSet.getString("name_of_document")))
                            document_id = resultSet.getInt("document_id");
                    resultSet.close();
                    if(document_id == 0) {
                        Connection connection = sqliteDatabase.getConnection();
                        PreparedStatement preparedStatement = null;
                        String sqlQuery = "INSERT INTO documents (name_of_document) VALUES  (?);";
                        preparedStatement = connection.prepareStatement(sqlQuery);
                        preparedStatement.setString(1, inputTextField.getText());
                        preparedStatement.executeUpdate();
                        preparedStatement.clearParameters();
                        preparedStatement.close();
                        resultSet = sqliteDatabase.queryForExtraction("SELECT document_id FROM documents WHERE name_of_document = \"" + inputTextField.getText() + "\";");
                        document_id = resultSet.getInt("document_id");
                    }
                    if(checkBox.getState()) {
                            int numberOfSet = 1;
                            for (Development detail : listOfDetail
                            ) {
                                int indexOfLastDoc = listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).getCountOfDocuments();
                                detail.addDocument(inputTextField.getText().charAt(0), false);
                                Connection connection = sqliteDatabase.getConnection();
                                PreparedStatement preparedStatement = null;
                                String sqlQuery = "INSERT INTO set_of_documents (set_of_documents_id_fk, document_id_fk, state_of_document) VALUES  (?,?,?);";
                                preparedStatement = connection.prepareStatement(sqlQuery);
                                preparedStatement.setInt(1, numberOfSet++);
                                preparedStatement.setInt(2, document_id);
                                preparedStatement.setBoolean(3, false);
                                preparedStatement.executeUpdate();
                                preparedStatement.clearParameters();
                                preparedStatement.close();
                            }
                        GUI.writeActionHistory("Document "+inputTextField.getText().charAt(0)+" created and added for all details");
                    }
                    else{
                        Development detail = listOfDetail.get(GUI.detailComboBox.getSelectedIndex());
                        int indexOfLastDoc = detail.getCountOfDocuments();
                        listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).addDocument( inputTextField.getText().charAt(0), false);

                        Connection connection = sqliteDatabase.getConnection();
                        PreparedStatement preparedStatement = null;
                        String sqlQuery = "INSERT INTO set_of_documents (set_of_documents_id_fk, document_id_fk, state_of_document) VALUES  (?,?,?);";
                        preparedStatement = connection.prepareStatement(sqlQuery);
                        preparedStatement.setInt(1, detail.getIndexOfDetail()+1);
                        preparedStatement.setInt(2, document_id);
                        preparedStatement.setBoolean(3, false);
                        preparedStatement.executeUpdate();
                        preparedStatement.clearParameters();
                        preparedStatement.close();

                        GUI.writeActionHistory("Document "+inputTextField.getText().charAt(0)+" created and added for "+listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).getName());
                    }
                }
                catch (SQLException ex){
                    System.out.println(ex.getMessage());
                }
                dialog.dispose();
                GUI.updateStateOfDocuments(sqliteDatabase, listOfDetail);
            }
        });
    };

    public static void buttonAddSetActionListener(SQLite sqliteDatabase, JDialog dialog, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList, ArrayList<String> listOfNameOfSet, ArrayList<Character> listOfAddedDocs){
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

                        try{
                            ResultSet resultSet = sqliteDatabase.queryForExtraction("SELECT set_of_documents_id FROM details WHERE name_of_detail = \"" + GUI.detailComboBox.getSelectedItem().toString() + "\";");
                            boolean result = sqliteDatabase.queryForExecute("DELETE FROM set_of_documents WHERE set_of_documents_id_fk IN (SELECT set_of_documents_id FROM details WHERE name_of_detail = \"" + GUI.detailComboBox.getSelectedItem().toString() + "\");");
                            Connection connection = sqliteDatabase.getConnection();
                            PreparedStatement preparedStatement = null;
                            preparedStatement = connection.prepareStatement("INSERT INTO set_of_documents (set_of_documents_id_fk, document_id_fk) VALUES (?,?);");
                            for(int i = 0 ; i < listOfAddedDocs.size(); i++) {
                                preparedStatement.setInt(1, resultSet.getInt("set_of_documents_id"));
                                preparedStatement.setInt(2, addedSet.getNumberOfDocument(i));
                                preparedStatement.executeUpdate();
                                preparedStatement.clearParameters();
                                preparedStatement.close();
                            }
                        }catch (SQLException e1){
                            System.err.println(e1.getMessage());
                        }

                        dialog.dispose();
                        GUI.updateStateOfDocuments(sqliteDatabase, listOfDetail);
                    }
                    if (select == JOptionPane.NO_OPTION)
                        inputTextField.selectAll();
                }
                else{
                    SetOfDocuments addedSet = new SetOfDocuments(inputTextField.getText(), listOfAddedDocs);
                    listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).setSetOfDocuments(addedSet);
                    setOfDocumentsArrayList.add(addedSet);
                    try{
                        ResultSet resultSet = sqliteDatabase.queryForExtraction("SELECT set_of_documents_id FROM details WHERE name_of_detail = \"" + GUI.detailComboBox.getSelectedItem().toString() + "\";");
                        boolean result = sqliteDatabase.queryForExecute("DELETE FROM set_of_documents WHERE set_of_documents_id_fk IN (SELECT set_of_documents FROM details WHERE name_of_detail = \"" + GUI.detailComboBox.getSelectedItem().toString() + "\");");
                        Connection connection = sqliteDatabase.getConnection();
                        PreparedStatement preparedStatement = null;
                        preparedStatement = connection.prepareStatement("INSERT INTO set_of_documents (set_of_documents_id_fk, document_id_fk) WHERE set_of_documents_id_fk VALUES (?,?);");
                        for(int i = 0 ; i < listOfAddedDocs.size(); i++) {
                            preparedStatement.setInt(1, resultSet.getInt("set_of_documents_id"));
                            preparedStatement.setInt(2, addedSet.getNumberOfDocument(i));
                            preparedStatement.executeUpdate();
                            preparedStatement.clearParameters();
                            preparedStatement.close();
                        }
                    }catch (SQLException e1){
                        System.err.println(e1.getMessage());
                    }
                    dialog.dispose();
                    GUI.updateStateOfDocuments(sqliteDatabase, listOfDetail);
                    GUI.writeActionHistory("Set of documents \""+addedSet.getNameOfSet()+"\" created and added for "+listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).getName());
                }
            }
        });
    }

    public static void buttonAddDetailActionListener(SQLite sqliteDatabase, JDialog dialog, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList){
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextField.getActionListeners()[0].actionPerformed(new ActionEvent(inputTextField,BUTTON1_DOWN_MASK,"Input"));
                if(!inputTextField.getText().equals("")) {
                    int set_of_documents_id = 0;
                    int indexOfNewDetail = listOfDetail.size();
                    String nameOfChoosenSet = setOfDocumentsArrayList.get(comboBox.getSelectedIndex()).getNameOfSet();
                    listOfDetail.add(indexOfNewDetail, new Development(inputTextField.getText(), indexOfNewDetail, setOfDocumentsArrayList.get(comboBox.getSelectedIndex())));
                    try{
                        ResultSet resultSet = sqliteDatabase.queryForExtraction("SELECT set_of_documents_id FROM details WHERE name_of_documents_set = \"" + nameOfChoosenSet + "\";");
                        set_of_documents_id = resultSet.getInt("set_of_documents_id");
                        resultSet.close();
                        Connection connection = sqliteDatabase.getConnection();
                        PreparedStatement preparedStatement = null;
                        preparedStatement = connection.prepareStatement("INSERT INTO details (name_of_detail, date_of_creating, set_of_documents_id, name_of_documents_set) VALUES (?,?,?,?)");
                        preparedStatement.setString(1, inputTextField.getText());
                        preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setInt(3, set_of_documents_id);
                        preparedStatement.setString(4, nameOfChoosenSet);
                        preparedStatement.executeUpdate();
                        preparedStatement.clearParameters();
                        preparedStatement.close();
                    }catch (SQLException e1){
                        System.err.println(e1.getMessage());
                    }
                    dialog.dispose();
                    GUI.updateListOfDetails(listOfDetail, indexOfNewDetail);
                    GUI.writeActionHistory("Detail "+listOfDetail.get(indexOfNewDetail).getName()+" created");
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
                    case 4:{
                        int result = JOptionPane.showConfirmDialog(null, "\"" + inputTextField.getText() + "\"  - This document is already exists and added for some details.\nDo you want to add this document for remaining details?", "WARNING", JOptionPane.YES_NO_OPTION);
                        if(result == JOptionPane.YES_OPTION) {
                            buttonAdd.setEnabled(true);
                            buttonAdd.doClick();
                        } else
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

    public static void buttonOpenManualActionListener(JButton buttonOpen){
        buttonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(MANUALFILE.toFile());
                }
                catch (IOException ex){ ex.printStackTrace(); }
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
                        validity = 4;
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
