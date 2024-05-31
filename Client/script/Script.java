package script;

import java.io.*;
import java.util.*;
import client.Client;

/** 
 * Класс, отвечающий за считывание команд из скрипта.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class Script{
    private static ArrayList<String> commands;
    private static BufferedReader scan = Client.getScan();
    private static ArrayDeque<String> script = Client.getScript();
    private static LinkedHashMap<String, Integer> recursion = new LinkedHashMap<>();
    public static void ExecuteScript(String path){

        read(path);
        ListIterator<String> iterComs = commands.listIterator(commands.size());
        
        while(iterComs.hasPrevious()) {
            script.push(iterComs.previous());  
        }
    
    }

    public static ArrayList<String> read(String path){
        commands = new ArrayList<>();

        while(true){
            try (BufferedReader br = new BufferedReader(new FileReader(path))){
                
                Integer val = recursion.getOrDefault(path, null);
                
                if(val==null){
                    val=0;
                }
                recursion.put(path, ++val);


                if( (val>1&script.size()>0) || (val>4) ){
                    //спросить о завершении
                    System.out.print("\u001B[31m\nLooks like recursion in the script\u001B[0m");

                    while(true){

                        System.out.print("\n    stop (yes or ...): ");

                        recursion = new LinkedHashMap<>();
                        commands = new ArrayList<>();                        

                        if(scan.readLine().toLowerCase().equals("yes")){
                            System.out.println();
                            return commands;                        
                        }

                        break;
                    }


                }

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
