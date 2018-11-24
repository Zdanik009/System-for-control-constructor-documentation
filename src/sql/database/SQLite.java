package sql.database;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class SQLite {
    private static Path DATABASE_DRIVER;
    private static Path DATABASE_URL;
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public SQLite(Path DATABASE_DRIVER, Path DATABASE_URL){
        this.DATABASE_DRIVER = DATABASE_DRIVER;
        this.DATABASE_URL = DATABASE_URL;
        System.out.println("SQLite created!");
    };

    public boolean connectDatabase () {
        try {
            Class.forName(DATABASE_DRIVER.toString());
            this.connection = DriverManager.getConnection("jdbc:sqlite:"+DATABASE_URL.toString());
            return true;
        }
        catch(ClassNotFoundException exception){
            System.err.println(exception.getMessage());
            return false;
        }
        catch (SQLException sqlException){
            System.err.println(sqlException.getMessage());
            return false;
        }
    };

    public void disconnectDatabase () throws SQLException{
        this.connection.close();
    };

    public boolean createDatabase(Path DATABASE_DRIVER, Path DATABASE_URL){
        try {
            Class.forName(DATABASE_DRIVER.toString());
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_URL.toString());
            //this.statement = this.connection.createStatement();
            //int result = this.statement.executeUpdate("CREATE DATABASE IF NOT EXISTS" + DATABASE_URL.getFileName());
            return true;
        }
        catch(ClassNotFoundException exception){
            System.err.println(exception.getMessage());
            return false;
        }
        catch (SQLException sqlException){
            System.err.println(sqlException.getMessage());
            return false;
        }
    };

    public int queryForUpdate(String sqlQuery)throws SQLException{
        this.statement = this.connection.createStatement();
        int resultOfUpdating = this.statement.executeUpdate(sqlQuery);
        return resultOfUpdating;
    };

    public ResultSet queryForExtraction (String sqlQuery) throws SQLException{
        this.statement = this.connection.createStatement();
        this.resultSet = this.statement.executeQuery(sqlQuery);
        return resultSet;
    };

    public boolean queryForExecute (String sqlQuery) throws  SQLException{
        this.statement = this.connection.createStatement();
        boolean result = this.statement.execute(sqlQuery);
        return result;
    };

    public boolean createTables (String[] tableDiscription){
        boolean result;
            for (String sqlQuery:tableDiscription
                 ) {
                try {
                    result = queryForExecute(sqlQuery);
                }
                catch (SQLException e) {
                    return false;
                }
            }
            return true;
    };

    public Connection getConnection(){
        return this.connection;
    };

    public boolean closeQuery(){
        try {
            this.resultSet.close();
            this.statement.close();
            return true;
        }catch (SQLException e){
            return false;
        }
    };
}
