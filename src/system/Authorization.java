package system;

import sql.database.SQLite;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Authorization {
    static JFrame frame;
    static JPanel panel;
    static JLabel usernameLabel;
    static JTextField usernameTextField;
    static JLabel passwordLabel;
    static JTextField passwordTextField;
    static JButton signUpButton;
    static JButton signInButton;

    public static void createDialog(SQLite sqliteDatabase, ArrayList<Development> listOfDetail, ArrayList<SetOfDocuments> setOfDocumentsArrayList) {
        frame = new JFrame("Authorization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill   = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth  = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        usernameLabel = new JLabel("Name:");
        panel.add(usernameLabel,constraints);

        constraints.gridheight = 1;
        constraints.gridwidth  = 3;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(5, 0, 10, 0);
        usernameTextField = new JTextField(20);
        panel.add(usernameTextField,constraints);

        constraints.gridheight = 1;
        constraints.gridwidth  = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(0, 0, 0, 0);
        passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel,constraints);

        constraints.gridheight = 1;
        constraints.gridwidth  = 3;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.insets = new Insets(5, 0, 10, 0);
        passwordTextField = new JTextField(20);
        panel.add(passwordTextField,constraints);

        constraints.gridheight = 1;
        constraints.gridwidth  = 1;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.insets = new Insets(0, 5, 0, 5);
        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputOfUsername = usernameTextField.getText();
                String inputOfPassword = passwordTextField.getText();
                if(inputOfPassword.equals("")||inputOfUsername.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please, enter user name and password!");
                }
                else{
                    try {
                        ResultSet usernamesAndPasswords = sqliteDatabase.queryForExtraction("SELECT * FROM users");
                        while(usernamesAndPasswords.next()) {
                            if (inputOfUsername.equals(usernamesAndPasswords.getString("name_of_user"))) {
                                JOptionPane.showMessageDialog(frame, "User \"" + inputOfUsername + "\" has already exists! Please, choose other user name.");
                                usernameTextField.setText("");
                                break;
                            }
                        }
                        Connection connection = sqliteDatabase.getConnection();
                        PreparedStatement preparedStatement = null;
                        preparedStatement = connection.prepareStatement("INSERT INTO users (name_of_user, users_password) VALUES (?,?)");
                        preparedStatement.setString(1,inputOfUsername);
                        preparedStatement.setString(2,inputOfPassword);
                        preparedStatement.executeUpdate();
                        preparedStatement.clearParameters();
                        preparedStatement.close();
                        frame.dispose();
                        GUI.createGUI(sqliteDatabase, inputOfUsername, listOfDetail, setOfDocumentsArrayList);
                        JOptionPane.showMessageDialog(frame, "Hello " + inputOfUsername + "!");
                    }
                    catch(SQLException sqlException){
                        System.err.println(sqlException.getMessage());
                    }
                }
            }
        });
        panel.add(signUpButton,constraints);

        constraints.gridheight = 1;
        constraints.gridwidth  = 1;
        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.insets = new Insets(0, 5, 0, 5);
        signInButton = new JButton("Sign In");
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputOfUsername = usernameTextField.getText();
                String inputOfPassword = passwordTextField.getText();
                if(inputOfPassword.equals("")||inputOfUsername.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please, enter user name and password!");
                }
                else {
                    try {
                        ResultSet usernamesAndPasswords = sqliteDatabase.queryForExtraction("SELECT * FROM users");
                        while (usernamesAndPasswords.next()) {
                            if (inputOfUsername.equals(usernamesAndPasswords.getString("name_of_user"))) {
                                if (inputOfPassword.equals(usernamesAndPasswords.getString("users_password"))) {
                                    frame.dispose();
                                    GUI.createGUI(sqliteDatabase, inputOfUsername, listOfDetail, setOfDocumentsArrayList);
                                    JOptionPane.showMessageDialog(frame, "Hello " + inputOfUsername + "!");
                                } else {
                                    JOptionPane.showMessageDialog(frame, "WRONG PASSWORD for user: " + inputOfUsername);
                                    passwordTextField.setText("");
                                    break;
                                }
                            }
                        }
                        /*String questionForSignUp = "There are no \"" + inputOfUsername + "\" in system :(\nDo you want to Sign Up: (username - " + inputOfUsername + ") ; (password - " + inputOfPassword + ")?";
                        if (JOptionPane.showConfirmDialog(null, questionForSignUp, null, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == 1) {
                            sqliteDatabase.queryForUpdate("INSERT INTO users (name_of_user, users_password) VALUES (" + inputOfUsername + ", " + inputOfPassword + ");");
                            JOptionPane.showMessageDialog(frame, "Hello " + inputOfUsername + "!");
                            frame.dispose();
                            GUI.createGUI(sqliteDatabase, listOfDetail, setOfDocumentsArrayList);
                        }*/
                    }
                    catch (SQLException sqlException)
                    {
                        System.err.println(sqlException.getMessage());
                    }
                }
            }
        });
        panel.add(signInButton,constraints);

        frame.getContentPane().add(panel);
        frame.setPreferredSize(new Dimension(400, 320));

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    };


}
