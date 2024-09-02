package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class InitCreate {

    
    public static void create() throws IOException{

        new SQLManager();

        Connection connection;
        String tables = readSQLRequest();

        try{
            connection = SQLManager.DBConnect();
            Statement statement = connection.createStatement();
            statement.execute(tables);

            statement.close();
            connection.close();
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private static String readSQLRequest(){
        StringBuilder res = new StringBuilder();
        String currentDir = System.getProperty("user.dir");

        try{
            BufferedReader in = new BufferedReader(new FileReader(currentDir+"/bin_server/config/database.sql"));
            String str;
            while((str = in.readLine())!= null){
                res.append(str);
            }
            in.close();
        }catch(IOException e){
            System.out.println(e);
        }

        return res.toString();
    }
}
