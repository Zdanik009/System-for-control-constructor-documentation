package system;

import sql.database.SQLite;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Controler {
    public static void ActionListening(SQLite sqliteDatabase, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList){
        GUI.helpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutHelp(GUI.frame);
            }
        });

        GUI.aboutProgrammMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutProgramm(GUI.frame);
            }
        });

        GUI.aboutAuthorMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutAuthor(GUI.frame);
            }
        });

        GUI.databaseTableForDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Dialog.createTableForDetails(GUI.frame, sqliteDatabase);
            }
        });

        GUI.databaseTableForDocuments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createTableForDocuments(GUI.frame, sqliteDatabase);
            }
        });

        GUI.databaseTableForSetOfDocuments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Dialog.createTableForSetOfDocuments(GUI.frame, sqliteDatabase);
            }
        });

        GUI.saveSessionMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.fileChooser = new JFileChooser("D://project//");
                GUI.fileChooser.setDialogTitle("Save Session");
                GUI.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                GUI.fileChooser.setSelectedFile(new File("LOG.txt"));
                int result = GUI.fileChooser.showSaveDialog(GUI.frame);
                Path FILETOSAVESESSION = Paths.get(GUI.fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILETOSAVESESSION.toFile()), Charset.forName("UTF-8")));
                    for (String actionAndTime:GUI.listOfActionsTime
                    ) {
                        writer.write(actionAndTime);
                    }
                    writer.close();
                }
                catch(IOException exc){
                    exc.printStackTrace();
                };
                if (result == JFileChooser.APPROVE_OPTION )
                    JOptionPane.showMessageDialog(GUI.frame, "История операция за текущую сессию сохранена в файл " + GUI.fileChooser.getSelectedFile());
            }
        });

        GUI.detailComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                GUI.addingDetailMenuItem.setEnabled(true);
                GUI.addingDocMenuItem.setEnabled(true);
                GUI.addingSetMenuItem.setEnabled(true);
                if(GUI.prerogative(sqliteDatabase)) {
                    GUI.saveSessionMenuItem.setEnabled(true);
                    GUI.databaseStateMenu.setEnabled(true);
                }
                GUI.addDetailDialogButton.setEnabled(true);
                GUI.addDocDialogButton.setEnabled(true);
                GUI.addSetDialogButton.setEnabled(true);
                GUI.stateCheckBox.setEnabled(true);
                GUI.documentComboBox.setEnabled(true);
                if(GUI.detailComboBox.getItemCount()!= 0) {
                    GUI.updateStateOfDocuments(sqliteDatabase, listOfDetail);
                }
            }
        });

        GUI.addingDetailMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutDeatil(GUI.frame, sqliteDatabase, listOfDetail,setOfDocumentsArrayList);
            }
        });

        GUI.addingDocMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutDoc(GUI.frame, sqliteDatabase, listOfDetail);
            }
        });

        GUI.addingSetMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutSet(GUI.frame, sqliteDatabase, listOfDetail, setOfDocumentsArrayList);
            }
        });

        GUI.addDetailDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutDeatil(GUI.frame, sqliteDatabase, listOfDetail, setOfDocumentsArrayList);
            }
        });

        GUI.addDocDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutDoc(GUI.frame, sqliteDatabase, listOfDetail);
            }
        });

        GUI.addSetDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutSet(GUI.frame, sqliteDatabase, listOfDetail,setOfDocumentsArrayList);
            }
        });

        GUI.stateCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(GUI.documentComboBox.getSelectedIndex()!= -1) {
                    if (GUI.stateCheckBox.getState()) {
                        listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).setStateofDoc(GUI.documentComboBox.getSelectedItem().toString(), true);
                    } else
                        listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).setStateofDoc(GUI.documentComboBox.getSelectedItem().toString(), false);
                    GUI.updateStateOfDocuments(sqliteDatabase, listOfDetail);
                    GUI.stateCheckBox.setState(false);
                }
                else
                    GUI.stateCheckBox.setState(false);
            }
        });

        GUI.documentComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(GUI.documentComboBox.getSelectedIndex()!=-1)
                    GUI.stateCheckBox.setState(listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).getStateOfDocument(GUI.documentComboBox.getSelectedItem().toString()));
                else
                    GUI.stateCheckBox.setState(false);
            }
        });

        GUI.confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if("You have all documents for development.".equals(GUI.stateOfDocuments(listOfDetail.get(GUI.detailComboBox.getSelectedIndex())))) {
                    String nameOfDeletedDetail = GUI.detailComboBox.getSelectedItem().toString();
                    int indexOfDeletedDetail = GUI.detailComboBox.getSelectedIndex();
                    try{
                        Connection connection = sqliteDatabase.getConnection();
                        PreparedStatement preparedStatement = null;
                        String sqlQuery = "UPDATE details SET date_of_completion =? WHERE name_of_detail =?;";
                        preparedStatement = connection.prepareStatement(sqlQuery);
                        preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setString(2, nameOfDeletedDetail);
                        preparedStatement.executeUpdate();
                        preparedStatement.clearParameters();
                        preparedStatement.close();
                    }catch (SQLException ex){
                        System.out.println(ex.getMessage());
                    };
                    String report = "\""+nameOfDeletedDetail+"\" has been sent for development\r\n";
                    JOptionPane.showMessageDialog(null,report);
                    GUI.writeActionHistory(report);
                    GUI.detailComboBox.removeItemAt(indexOfDeletedDetail);
                    listOfDetail.remove(indexOfDeletedDetail);
                }
            }
        });
    }
}
