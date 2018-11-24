package system;

import sql.database.SQLite;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static BufferedReader reader;
    static final Path INPUTDATAFILE = Paths.get("D://project//InputData.txt");
    static final Path DATABASE_DRIVER = Paths.get("org.sqlite.JDBC");
    static final Path DATABASE_URL = Paths.get("D:/project/src/sql/sqlite_database.db");
    static ArrayList<String> namesOfDetails = new ArrayList<>();
    static SetOfDocuments standart = new SetOfDocuments();
    static ArrayList<Development> listOfDetail = new ArrayList<>();
    static ArrayList<SetOfDocuments>  setOfDocumentsArrayList = new ArrayList<>();

    public static void main(String[] args){

        try {
            SQLite sqliteDatabase = new SQLite(DATABASE_DRIVER, DATABASE_URL);
            if (sqliteDatabase.connectDatabase()) {
                String[] sqlQueryForCreatingTables = new String[7];
                sqlQueryForCreatingTables[0] = "CREATE TABLE IF NOT EXISTS details (" +
                                                "detail_id_pk INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                "name_of_detail VARCHAR(10) NOT NULL, " +
                                                "date_of_creating TIMESTAMP   NOT NULL, " +
                                                "date_of_completion    TIMESTAMP, " +
                                                "name_of_documents_set VARCHAR(10) NOT NULL, " +
                                                "set_of_documents_id   INTEGER DEFAULT 1); ";
                sqlQueryForCreatingTables[1] = "create unique index details_date_of_completion_uindex " +
                                                 "on details (date_of_completion); " +
                                                 "create unique index details_name_of_detail_uindex " +
                                                 "on details (name_of_detail); ";
                sqlQueryForCreatingTables[2] = "CREATE TABLE IF NOT EXISTS documents ( " +
                                                 "document_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                 "name_of_document VARCHAR(1) NOT NULL UNIQUE); ";
                sqlQueryForCreatingTables[3] = "create unique index documents_document_id_uindex " +
                                                 "on documents (document_id); ";
                                                 //"create unique index documents_name_of_document_uindex " +
                                                 //"on documents (name_of_document);";
                sqlQueryForCreatingTables[4] = "CREATE TABLE IF NOT EXISTS set_of_documents ( " +
                                                 "set_of_documents_id_fk INT NOT NULL " +
                                                 "constraint set_of_documents_id_fk " +
                                                 "references details (set_of_documents_id), " +
                                                 "document_id_fk         INT NOT NULL " +
                                                 "constraint document_id_fk " +
                                                 "references documents (document_id), " +
                                                 "state_of_document      BOOLEAN DEFAULT FALSE NOT NULL);";
                sqlQueryForCreatingTables[5] = "CREATE TABLE IF NOT EXISTS users (" +
                                                "user_id_pk INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                "name_of_user VARCHAR(20) NOT NULL, " +
                                                "users_password VARCHAR(20) NOT NULL, " +
                                                "prerogative BOOLEAN DEFAULT FALSE);";
                sqlQueryForCreatingTables[6] = "create unique index name_of_user_uindex " +
                                                "on users (name_of_user);";
                if(sqliteDatabase.createTables(sqlQueryForCreatingTables)) {

                    FileInputStream fileInputData = new FileInputStream(INPUTDATAFILE.toFile());
                    reader = new BufferedReader(new InputStreamReader(fileInputData, Charset.forName("UTF-8")));

                    int i = 1;
                    String dataAboutDeveloping = "";
                    namesOfDetails = new ArrayList<>();
                    Connection connection = sqliteDatabase.getConnection();
                    PreparedStatement preparedStatement = null;
                    preparedStatement = connection.prepareStatement("INSERT INTO details (name_of_detail, date_of_creating, set_of_documents_id, name_of_documents_set) VALUES (?,?,?,?)");
                    while ((dataAboutDeveloping = reader.readLine()) != null) {
                        for (String item : parsing(dataAboutDeveloping)) {
                            namesOfDetails.add(item);
                            preparedStatement.setString(1, item);
                            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                            preparedStatement.setInt(3, i++);
                            preparedStatement.setString(4, item);
                            preparedStatement.executeUpdate();
                            preparedStatement.clearParameters();
                        }
                    }
                    preparedStatement.closeOnCompletion();
                    reader.close();
                    for(int k=0;k<namesOfDetails.size();k++){
                        listOfDetail.add(k,new Development(namesOfDetails.get(k),k, SetOfDocuments.getSubSetOf(standart)));
                        setOfDocumentsArrayList.add(listOfDetail.get(k).getSetOfDocuments());
                    };

                    preparedStatement = connection.prepareStatement("INSERT INTO documents (document_id, name_of_document) VALUES (?,?)");
                    for (int amountOfDocuments = 1; amountOfDocuments <= standart.getCountOfDocuments(); amountOfDocuments++) {
                        preparedStatement.setInt(1, amountOfDocuments);
                        preparedStatement.setString(2, standart.getNameOfDocument(amountOfDocuments-1).toString());
                        preparedStatement.executeUpdate();
                        preparedStatement.clearParameters();
                    };
                    preparedStatement.closeOnCompletion();

                    preparedStatement = connection.prepareStatement("INSERT INTO set_of_documents (set_of_documents_id_fk, document_id_fk, state_of_document) VALUES (?,?,?)");
                    for (int j = 1; j <= (i-1); j++) {
                        SetOfDocuments setOfDocumentsForDetail = setOfDocumentsArrayList.get(j-1);
                        for (int amountOfDocuments = 0; amountOfDocuments < setOfDocumentsForDetail.getCountOfDocuments(); amountOfDocuments++) {
                            preparedStatement.setInt(1, j);
                            preparedStatement.setInt(2, setOfDocumentsForDetail.getNumberOfDocument(amountOfDocuments)+1);
                            preparedStatement.setBoolean(3, setOfDocumentsForDetail.getStateOfDocument(setOfDocumentsForDetail.getNameOfDocument(amountOfDocuments).toString()));
                            preparedStatement.executeUpdate();
                            preparedStatement.clearParameters();
                        }
                    }
                    preparedStatement.closeOnCompletion();

                    preparedStatement = connection.prepareStatement("INSERT INTO users (name_of_user, users_password, prerogative) VALUES (?,?,?)");
                    preparedStatement.setString(1,"ADMIN");
                    preparedStatement.setString(2,"PLEASE");
                    preparedStatement.setBoolean(3,true);
                    preparedStatement.executeUpdate();
                    preparedStatement.clearParameters();
                    preparedStatement.close();
                    connection.close();
                }
                else{
                    SetOfDocuments setOfDocuments;
                    ResultSet resultSet = sqliteDatabase.queryForExtraction("SELECT name_of_detail FROM details WHERE date_of_completion IS NULL");
                    while(resultSet.next())
                        namesOfDetails.add(resultSet.getString("name_of_detail"));
                    resultSet.close();
                    for(int i = 0 ; i < namesOfDetails.size(); i++) {
                        String sqlQuery = "SELECT name_of_document FROM documents WHERE document_id IN " +
                                "(SELECT document_id_fk FROM set_of_documents WHERE set_of_documents_id_fk IN " +
                                "(SELECT set_of_documents_id FROM details WHERE name_of_detail=\"" + namesOfDetails.get(i) + "\"));";
                        resultSet = sqliteDatabase.queryForExtraction(sqlQuery);
                        //System.out.println("Names of documents = " + resultSet.getString("name_of_document"));
                        ArrayList<Character> listOfAddedDocuments = new ArrayList<Character>();
                        while (resultSet.next())
                            listOfAddedDocuments.add(resultSet.getString("name_of_document").charAt(0));
                        setOfDocuments = new SetOfDocuments(namesOfDetails.get(i), listOfAddedDocuments);
                        resultSet.close();

                        sqlQuery = "SELECT state_of_document FROM set_of_documents WHERE set_of_documents_id_fk IN " +
                                "(SELECT set_of_documents_id FROM details WHERE name_of_detail=\"" + namesOfDetails.get(i) + "\");";
                        resultSet = sqliteDatabase.queryForExtraction(sqlQuery);
                        for (int j = 0; j < setOfDocuments.getCountOfDocuments(); j++, resultSet.next())
                            setOfDocuments.setStateofDoc(setOfDocuments.getNameOfDocument(j).toString(), resultSet.getBoolean("state_of_document"));
                        resultSet.close();

                        listOfDetail.add(i, new Development(namesOfDetails.get(i), i, setOfDocuments));
                        setOfDocumentsArrayList.add(setOfDocuments);
                    };
                };

                Authorization.createDialog(sqliteDatabase, listOfDetail, setOfDocumentsArrayList);
            }
            else
                System.exit(1);
        }catch (IOException ioExeption){
            ioExeption.printStackTrace();
        }catch (SQLException sqlExeption){
            sqlExeption.printStackTrace();
        }
    }
    public static ArrayList<String> parsing(String data){
        Pattern p = Pattern.compile("(\\x22)[a-z]+");
        Matcher m = p.matcher(data);
        ArrayList<String> list = new ArrayList<>();
        while(m.find()) {
            list.add(data.substring(m.start()+1,m.end()));
        };
        return list;
    };
}