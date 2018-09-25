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
                GUI.checkbox1.setState(listOfDetail.get(GUI.comboBox.getSelectedIndex()).getStateOfDoc1());
                GUI.checkbox2.setState(listOfDetail.get(GUI.comboBox.getSelectedIndex()).getStateOfDoc2());
                GUI.checkbox3.setState(listOfDetail.get(GUI.comboBox.getSelectedIndex()).getStateOfDoc3());
                GUI.checkbox4.setState(listOfDetail.get(GUI.comboBox.getSelectedIndex()).getStateOfDoc4());
                GUI.textArea.setText("You choose \"" + GUI.comboBox.getSelectedItem() + "\" "
                        + (GUI.comboBox.getSelectedIndex() + 1) + "/" + GUI.items + "\n" + GUI.stateofDocuments());

            }
        });
        GUI.checkbox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                GUI.textArea.setText("You choose \""+ GUI.comboBox.getSelectedItem()+"\"\n"+ GUI.stateofDocuments());
            }
        });
        GUI.checkbox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                GUI.textArea.setText("You choose \""+ GUI.comboBox.getSelectedItem()+"\"\n"+ GUI.stateofDocuments());
            }
        });
        GUI.checkbox3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                GUI.textArea.setText("You choose \""+ GUI.comboBox.getSelectedItem()+"\"\n"+ GUI.stateofDocuments());
            }
        });
        GUI.checkbox4.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                GUI.textArea.setText("You choose \""+ GUI.comboBox.getSelectedItem()+"\"\n"+ GUI.stateofDocuments());
            }
        });
        GUI.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(GUI.checkbox1.getState()&& GUI.checkbox2.getState()&&
                        GUI.checkbox3.getState()&& GUI.checkbox4.getState()) {
                    String nameOfDeletedDetail = GUI.comboBox.getSelectedItem().toString();
                    int indexOfDeletedDetail = GUI.comboBox.getSelectedIndex();
                    //JOptionPane.showMessageDialog(null, nameOfDeletedDetail+" has been sent for development.");
                    /*GUI.textArea.setText(indexOfDeletedDetail+" index of combobox\n"
                            +listOfDetail.get(indexOfDeletedDetail).getName()+" name of detail with this index");*/
                    //listOfDetail.remove(indexOfDeletedDetail);
                    //GUI.comboBox.setSelectedIndex(-1);
                    //GUI.textArea.setText("<- Choose detail.");
                    report.append("\""+nameOfDeletedDetail+"\" has been sent for development\r\n");
                    GUI.saveReportToFile(fileOutputStream, report);
                    JOptionPane.showMessageDialog(null,report);
                    GUI.comboBox.removeItemAt(indexOfDeletedDetail);
                }
            }
        });
    }
}
