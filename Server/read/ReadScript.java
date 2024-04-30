package read;

import java.io.*;
import java.util.ArrayList;
import invoker.CommandManager;

/** 
 * Класс, отвечающий за считывание команд из скрипта.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ReadScript{


    /** 
     * Считывает построчно значения из скрипта.
     * <p>
     * В случае, если файла не существует выведет строку, сообщающую это.
     *
     * @param path путь до скрипта
     * 
     * @return возвращает список содержащий считанные строки с командами
     * @exception MyFileNotFoundException Если скрипт не был найден
     *                       
     */
    public static ArrayList<String> read(String path){
        ArrayList<String> commands = new ArrayList<>();
        BufferedReader scan = CommandManager.getScan();

        while(true){
            try (BufferedReader br = new BufferedReader(new FileReader(path))){
                String line;

                while((line = br.readLine()) != null) {
                    commands.add(line);
                }
                
                return commands;    
            } catch (IOException e) {
                System.out.print("\nIncorrect path, try again: ");
                try{path = scan.readLine();}catch(IOException ex){}
            }
        }

        
        


    }
}
