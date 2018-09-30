package system;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayList;

public class Controler {
    public static void ActionListening(ArrayList<Development> listOfDetail, OutputStream fileOutputStream, StringBuffer report){
        GUI.comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                GUI.textField.setEnabled(true);
                GUI.textCheckBox.setEnabled(true);
                GUI.textButton.setEnabled(false);
                GUI.checkbox.setEnabled(true);
                GUI.checkbox.setState(false);
                GUI.checkComboBox.setEnabled(true);
                GUI.checkComboBox.setSelectedIndex(-1);
                GUI.checkComboBox.removeAllItems();
                String docs = "";
                for(int i=0;i<listOfDetail.get(GUI.comboBox.getSelectedIndex()).getCountOfDocuments();i++) {
                    GUI.checkComboBox.addItem(listOfDetail.get(GUI.comboBox.getSelectedIndex()).getNameOfDocument(i));
                    docs += GUI.checkComboBox.getItemAt(i)+" ";
                }
                GUI.progressBar.setValue(GUI.progressOfDocCollection(listOfDetail.get(GUI.comboBox.getSelectedIndex())));
                GUI.textArea.setText("You choose \"" + GUI.comboBox.getSelectedItem() + "\" " + (GUI.comboBox.getSelectedIndex() + 1)
                        + "/" + GUI.items + ".\nDetail need to have documents: "+docs+".\n"+GUI.stateOfDocuments(listOfDetail.get(GUI.comboBox.getSelectedIndex())));
            }
        });

        GUI.textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (GUI.validOfDoc(listOfDetail, GUI.textField.getText(), GUI.textCheckBox.getState())){
                    case 1:{
                        GUI.textButton.setEnabled(true);
                        //GUI.textArea.setText(GUI.textArea.getText()+"Press button to \"Add\" document:"+GUI.textField.getText());
                        JOptionPane.showMessageDialog(null,"Press button to \"Add\" document "+GUI.textField.getText()+" for the selected detail.");
                        break;
                    }
                    case 2:{
                        GUI.textButton.setEnabled(true);
                        //GUI.textArea.setText(GUI.textArea.getText()+"Press button to \"Add\" document:"+GUI.textField.getText());
                        JOptionPane.showMessageDialog(null,"Press button to \"Add\" document "+GUI.textField.getText()+" for all details.");
                        break;
                    }
                    case 3:{
                        JOptionPane.showMessageDialog(null,"\""+GUI.textField.getText()+"\"  - WRONG NAME OF DOCUMENT!\nIt must consit of one capital letter.");
                        GUI.textField.setText("");
                        break;
                    }
                    case 0:{
                        //GUI.textArea.setText(GUI.textArea.getText()+"WRONG NAME OF DOCUMENT!");
                        JOptionPane.showMessageDialog(null,"\""+GUI.textField.getText()+"\"  - WRONG NAME OF DOCUMENT!\nThis document is already exists.");
                        GUI.textField.setText("");
                        break;
                    }
                    default: {
                        //GUI.textArea.setText(GUI.textArea.getText() + "WRONG NAME OF DOCUMENT!");
                        JOptionPane.showMessageDialog(null,"\""+GUI.textField.getText()+"\"  - WRONG NAME OF DOCUMENT!");
                        GUI.textField.setText("");
                        break;
                    }
                }
            }
        });

        GUI.textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!"".equals(GUI.textField.getText()))
                    if(GUI.textCheckBox.getState()) {
                        for (Development detail : listOfDetail
                        ) {
                            int indexOfLastDoc = listOfDetail.get(GUI.comboBox.getSelectedIndex()).getCountOfDocuments();
                            detail.addDocument(GUI.textField.getText().charAt(0), false);
                        }
                    }
                    else{
                        int indexOfLastDoc = listOfDetail.get(GUI.comboBox.getSelectedIndex()).getCountOfDocuments();
                        listOfDetail.get(GUI.comboBox.getSelectedIndex()).addDocument( GUI.textField.getText().charAt(0), false);
                    };
                    GUI.checkComboBox.addItem(GUI.textField.getText());
                    GUI.progressBar.setValue(GUI.progressOfDocCollection(listOfDetail.get(GUI.comboBox.getSelectedIndex())));
                    GUI.textArea.setText("You choose \"" + GUI.comboBox.getSelectedItem() + "\" " + (GUI.comboBox.getSelectedIndex() + 1)
                        + "/" + GUI.items + "\n" + GUI.stateOfDocuments(listOfDetail.get(GUI.comboBox.getSelectedIndex())));
                    GUI.textField.setText("");
                    GUI.textButton.setEnabled(false);
            }
        });

        GUI.checkbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(GUI.checkbox.getState()) {
                    listOfDetail.get(GUI.comboBox.getSelectedIndex()).setStateofDoc(GUI.checkComboBox.getSelectedItem().toString(), true);
                }
                else
                    listOfDetail.get(GUI.comboBox.getSelectedIndex()).setStateofDoc(GUI.checkComboBox.getSelectedItem().toString(), false);
                GUI.textArea.setText("You choose \""+ GUI.comboBox.getSelectedItem()+"\"\n"+ GUI.stateOfDocuments(listOfDetail.get(GUI.comboBox.getSelectedIndex())));
                GUI.progressBar.setValue(GUI.progressOfDocCollection(listOfDetail.get(GUI.comboBox.getSelectedIndex())));
            }
        });

        GUI.checkComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(GUI.checkComboBox.getSelectedIndex()!=-1)
                    GUI.checkbox.setState(listOfDetail.get(GUI.comboBox.getSelectedIndex()).getStateOfDocument(GUI.checkComboBox.getSelectedItem().toString()));
            }
        });

        GUI.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if("You have all documents for development.".equals(GUI.stateOfDocuments(listOfDetail.get(GUI.comboBox.getSelectedIndex())))) {
                    String nameOfDeletedDetail = GUI.comboBox.getSelectedItem().toString();
                    int indexOfDeletedDetail = GUI.comboBox.getSelectedIndex();
                    report.append("\""+nameOfDeletedDetail+"\" has been sent for development\r\n");
                    GUI.saveReportToFile(fileOutputStream, report);
                    JOptionPane.showMessageDialog(null,report);
                    GUI.comboBox.removeItemAt(indexOfDeletedDetail);
                }
            }
        });
    }
}
