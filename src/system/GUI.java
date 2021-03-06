package system;

import sql.database.SQLite;
import system.Controler;
import system.Development;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;



public class GUI {
    static JFrame frame;
    static JMenuBar menuBar;
    static JMenu systemMenuBar;
    static JMenuItem addingDetailMenuItem;
    static JMenuItem addingDocMenuItem;
    static JMenuItem addingSetMenuItem;
    static JMenu databaseStateMenu;
    static JMenuItem databaseTableForDetails;
    static JMenuItem databaseTableForDocuments;
    static JMenuItem databaseTableForSetOfDocuments;
    static JMenuItem saveSessionMenuItem;
    static JMenu informationMenuBar;
    static JMenuItem helpMenuItem;
    static JMenuItem aboutProgrammMenuItem;
    static JMenuItem aboutAuthorMenuItem;
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
    static ArrayList<String> listOfActionsTime;
    static JFileChooser fileChooser;
    static String username;

    public static void createGUI(SQLite sqliteDatabase, String username, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList) {
        GUI.username = username;
        frame = new JFrame("System for control constructor documentation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        menuBar = new JMenuBar();
        systemMenuBar = new JMenu("System");
        addingDetailMenuItem = new JMenuItem("Add Detail");
        addingDetailMenuItem.setEnabled(false);
        addingDocMenuItem = new JMenuItem("Add Document");
        addingDocMenuItem.setEnabled(false);
        addingSetMenuItem = new JMenuItem("Add Set of Documents");
        addingSetMenuItem.setEnabled(false);
        listOfActionsTime = new ArrayList<String>();
        saveSessionMenuItem = new JMenuItem("Save Session");
        saveSessionMenuItem.setEnabled(false);
        databaseStateMenu = new JMenu("Database tables");
        databaseTableForDetails = new JMenuItem("Details");
        databaseTableForDocuments = new JMenuItem("Documents");
        databaseTableForSetOfDocuments = new JMenuItem("Sets of Documents");
        databaseStateMenu.add(databaseTableForDetails);
        databaseStateMenu.add(databaseTableForDocuments);
        databaseStateMenu.add(databaseTableForSetOfDocuments);
        databaseStateMenu.setEnabled(false);
        informationMenuBar = new JMenu("Information");
        helpMenuItem = new JMenuItem("Help");
        aboutProgrammMenuItem = new JMenuItem("About Programm");
        aboutAuthorMenuItem = new JMenuItem("About the Author");
        detailsLabel = new JLabel("Details:");
        detailComboBox = new JComboBox();
        detailComboBox.setToolTipText("list of details. Choose anyone.");
        textArea = new JTextArea("<= Please, choose any detail for start.", 15, 25);
        textArea.setToolTipText("information output field.");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        addDetailDialogButton = new JButton("   Add Detail", createIcon("cliparts/gear1.png"));
        addDetailDialogButton.setToolTipText("create and add detail");
        documentsLabel = new JLabel("Documents:");
        addDocDialogButton = new JButton("  Add Document", createIcon("cliparts/document.png"));
        addDocDialogButton.setToolTipText("create and add document");
        addSetDialogButton = new JButton("  Add Set of Documents", createIcon("cliparts/set_of_documents.png"));
        addSetDialogButton.setToolTipText("create and add set of documents");
        stateCheckBox = new Checkbox();
        documentComboBox = new JComboBox();
        documentComboBox.setToolTipText("list of documents");
        progressBar = new JProgressBar();
        progressBar.setToolTipText("document collection progress");
        confirmButton = new JButton("   Confirm readiness", createIcon("cliparts/tick.png"));
        confirmButton.setToolTipText("send detail for development");
        GridBagConstraints c = new GridBagConstraints();

        systemMenuBar.add(addingDetailMenuItem);
        systemMenuBar.add(addingDocMenuItem);
        systemMenuBar.add(addingSetMenuItem);
        systemMenuBar.add(saveSessionMenuItem);
        systemMenuBar.add(databaseStateMenu);
        menuBar.add(systemMenuBar);

        informationMenuBar.add(helpMenuItem);
        informationMenuBar.add(aboutProgrammMenuItem);
        informationMenuBar.add(aboutAuthorMenuItem);
        menuBar.add(informationMenuBar);

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 10, 5);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        panel.add(detailsLabel, c);

        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 2;
        c.gridy = 0;
        updateListOfDetails(listOfDetail, -1);
        panel.add(detailComboBox, c);

        c.fill = GridBagConstraints.BOTH;
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
        panel.add(addDetailDialogButton, c);

        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(50, 5, 10, 5);
        panel.add(documentsLabel, c);

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
        c.insets = new Insets(0, 5, 10, 5);
        addDocDialogButton.setEnabled(false);
        panel.add(addDocDialogButton, c);

        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        addSetDialogButton.setEnabled(false);
        panel.add(addSetDialogButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 5;
        panel.add(progressBar, c);

        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 4;
        c.gridy = 5;
        confirmButton.setEnabled(false);
        panel.add(confirmButton, c);

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(panel);
        frame.setPreferredSize(new Dimension(580, 380));
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        Controler.ActionListening(sqliteDatabase, listOfDetail, setOfDocumentsArrayList);
    }

    public static void updateStateOfDocuments(SQLite sqliteDatabase, ArrayList<Development> listOfDetail) {
        GUI.documentComboBox.removeAllItems();
        String docs = "";
        Character nameOfDocument;
        Development choosenDetail = listOfDetail.get(GUI.detailComboBox.getSelectedIndex());
        for (int i = 0; i < choosenDetail.getCountOfDocuments(); i++) {
            nameOfDocument = choosenDetail.getNameOfDocument(i);
            GUI.documentComboBox.addItem(nameOfDocument);
            docs += nameOfDocument + " ";

            try {
                Connection connection = sqliteDatabase.getConnection();
                PreparedStatement preparedStatement = null;
                String sqlQuery = "UPDATE set_of_documents SET state_of_document=? WHERE set_of_documents_id_fk IN ( " +
                                    "SELECT set_of_documents_id FROM details WHERE name_of_detail = \"" + choosenDetail.getName() + "\");";
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setBoolean(1, choosenDetail.getStateOfDocument(nameOfDocument.toString()));
                preparedStatement.executeUpdate();
                preparedStatement.clearParameters();
                preparedStatement.close();
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }
        }
        documentComboBox.setSelectedIndex(-1);
        GUI.progressBar.setValue(GUI.progressOfDocCollection(listOfDetail.get(GUI.detailComboBox.getSelectedIndex())));
        GUI.textArea.setText("You choose \"" + GUI.detailComboBox.getSelectedItem() + "\" " + (GUI.detailComboBox.getSelectedIndex() + 1)
                + "/" + GUI.items + ".\nDetail need to have documents: " + docs + ".\n" + GUI.stateOfDocuments(listOfDetail.get(GUI.detailComboBox.getSelectedIndex())));
    }

    public static void writeActionHistory(String action) {
        Date dateOfAction = new Date();
        String timeAndAction = dateOfAction + " - " + action;
        listOfActionsTime.add(timeAndAction);
    }

    public static void updateListOfDetails(ArrayList<Development> listOfDetail, int indexOfNewDetail) {
        GUI.detailComboBox.removeAllItems();
        for (Development items : listOfDetail) {
            GUI.detailComboBox.addItem(items.getName());
        }
        ;
        GUI.items = GUI.detailComboBox.getItemCount();
        GUI.detailComboBox.setSelectedIndex(indexOfNewDetail);
    }

    public static String stateOfDocuments(Development detail) {
        String text = new String();
        int key = detail.getCountOfDocuments();
        for (int i = 0; i < detail.getCountOfDocuments(); i++) {
            if (!detail.getStateOfDocument(detail.getNameOfDocument(i).toString())) {
                text += "There is NO Document  " + detail.getNameOfDocument(i).toString() + "!\n";
                key--;
            }
        }
        if (key == detail.getCountOfDocuments()) {
            text = "You have all documents for development.";
            confirmButton.setEnabled(true);
            GUI.progressBar.setValue(100);
        }
        return text;
    }

    public static int progressOfDocCollection(Development detail) {
        double progress = 0;
        double progressForOneDoc = 100 / detail.getCountOfDocuments();
        for (int i = 0; i < detail.getCountOfDocuments(); i++) {
            if (detail.getStateOfDocument(detail.getNameOfDocument(i).toString())) {
                progress += progressForOneDoc;
            }
        }
        return (int) progress;
    }

    public static boolean prerogative(SQLite sqliteDatabase){
        try {
            ResultSet resultSet = sqliteDatabase.queryForExtraction("SELECT prerogative FROM users WHERE name_of_user = \""+username+"\";");
            return resultSet.getBoolean("prerogative");
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            return  false;
        }
    };

    protected static ImageIcon createIcon(String path) {
        URL imgURL = GUI.class.getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image image = icon.getImage();
            Image newImage = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            return new ImageIcon(newImage);
        } else {
            System.err.println("File not found " + path);
            return null;
        }
    };
}