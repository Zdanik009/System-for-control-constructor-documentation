package system;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.OutputStream;
import java.util.ArrayList;

public class Controler {
    public static void ActionListening(ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList, OutputStream fileOutputStream, StringBuffer report){
        GUI.detailComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(GUI.detailComboBox.getItemCount()!=0) {
                    GUI.addDetailDialogButton.setEnabled(true);
                    GUI.addDocDialogButton.setEnabled(true);
                    GUI.addSetDialogButton.setEnabled(true);
                    GUI.stateCheckBox.setEnabled(true);
                    GUI.documentComboBox.setEnabled(true);
                    GUI.updateStateOfDocuments(listOfDetail);
                }
            }
        });

        GUI.addDetailDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutDeatil(GUI.frame,listOfDetail,setOfDocumentsArrayList);
            }
        });

        GUI.addDocDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutDoc(GUI.frame,listOfDetail);
            }
        });

        GUI.addSetDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.createDialogAboutSet(GUI.frame,listOfDetail,setOfDocumentsArrayList);
            }
        });

        GUI.stateCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(GUI.documentComboBox.getSelectedIndex()!=-1) {
                    if (GUI.stateCheckBox.getState()) {
                        listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).setStateofDoc(GUI.documentComboBox.getSelectedItem().toString(), true);
                    } else
                        listOfDetail.get(GUI.detailComboBox.getSelectedIndex()).setStateofDoc(GUI.documentComboBox.getSelectedItem().toString(), false);
                    GUI.updateStateOfDocuments(listOfDetail);
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
                    report.append("\""+nameOfDeletedDetail+"\" has been sent for development\r\n");
                    GUI.saveReportToFile(fileOutputStream, report);
                    JOptionPane.showMessageDialog(null,report);
                    GUI.detailComboBox.removeItemAt(indexOfDeletedDetail);
                    listOfDetail.remove(indexOfDeletedDetail);
                }
            }
        });
    }
}
