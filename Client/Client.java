package client;

import java.io.*;
import java.net.*;
import java.util.*;
import pack.Package;
import data.CreateTicketObject;
import data.validation.*;
import data.ticket.Ticket;
import java.lang.StringBuilder;
import script.Script;
/** 
 * Класс отвечающий за создание обьектов команды, настройку их получателей и связывание с вызывающим.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class Client{
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static ArrayDeque<String> script = new ArrayDeque<>();
    private static ArrayDeque<String> history = new ArrayDeque<>();
    private static LinkedHashMap<String, String> packArg;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static Set<String> keys;
    private static int port = 1100;
    private static String address = "localhost";
    private static Socket socket;
    private static Package pack;

    
    // !!!!!!!!! execute_script перенести на клиента
    /* что нужно передавать:
                -- название команды
                -- название команды + билет
                -- название команды + число
        решение: создание обьекта transfer, он содержит поле type: null, ticket, int, id
                
    */
    public static void main(String[] args) throws IOException{
        getArgMap();
        System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
        CommandLoop();
    }

    public static void getArgMap() throws IOException{
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\u001B[35mПодключение к серверу\u001B[5;35m...\u001B[0m\n");
        while(true){
            try{

                socket = new Socket(address, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                out.writeObject("args");
                out.flush();
                
                packArg = (LinkedHashMap<String, String>)in.readObject();
                keys = packArg.keySet();
                
                
                in.close();
                out.close();
                socket.close();
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("\u001B[32mСоединение установлено\u001B[0m\n");
                while(input.ready()) input.readLine();
                break;

            }catch(ConnectException er){

            }catch(ClassNotFoundException er){
                System.out.println(er);
            }
        }
    }

    public static void CommandLoop() throws IOException{
        String line=null;

        //System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
        while(true){
            
            if(script.size()>0){
                line = script.pop();
                System.out.println(line);
                if(Execute(line, true)){break;}
            }else if(input.ready()){
                line = input.readLine();
                if(Execute(line, false)){break;}
            }
            
            
        }

  
    }

    public static boolean Execute(String line, boolean isScript) throws IOException{

        String[] command;
        boolean Success = false;   

        while(true){
            command = line.split(" ");

            if(command[0].equals("exit")){
                
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("\n\033[0;34mTerminating client...\033[0m");
                
                return true;

            }else if(command[0].equals("execute_script")){
                
                Script.ExecuteScript(command[1]);

                History(command[0]);

                break;

            }else if(command[0].equals("history")){

                HistoryToString();

                History(command[0]);

                break;

            }else if(keys.contains(command[0])){

                History(command[0]);

                switch(""+packArg.get(command[0])){

                        case "null": 
                            Success = NullOut(command);
                            break;

                        case "int": 
                            Success = IntOut(command, isScript);
                            break;

                        case "id": 
                            Success = IdOut(command, isScript);
                            break;

                        case "ticket": 
                            Success = TicketOut(command, isScript);
                            break;
                }

                if(Success){
                    Send();

                }

                break;

            }else{
                System.out.print("\u001B[31m\n  Command not found");

                if(!isScript){
                    System.out.println("\u001B[0m\n");
                    break;
                }else{
                    System.out.println(", try again\u001B[0m\n");
                    System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
                    try{line = input.readLine();}catch(IOException ex){}
                }    
            }

            

        }

        System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");

        return false;

    }

    public static void Send() throws IOException{
        try{

            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(pack);
            out.flush();
            
            System.out.println((String)in.readObject()); // ждем сервер

            in.close();
            out.close();
            socket.close();

        }catch(ConnectException er){
            System.out.println("\n\u001B[0;30;47mСервер временно не доступен\u001B[0m\n");
        }catch(SocketException er){
            script = new ArrayDeque<>();
            while(input.ready()) input.readLine();
            System.out.println("\n\u001B[0;31;47mСоединение с сервером потеряно\u001B[0m\n");
        }catch(ClassNotFoundException er){
            System.out.println(er);
        }

    }

    public static void HistoryToString(){
    
        Iterator<String> HistoryIter = history.descendingIterator();
        int num = 1;
        StringBuilder builder = new StringBuilder();
        builder.append("\nHistory of command (from the latest):\n");
        while(HistoryIter.hasNext()){
            builder.append("   "+num++ +")"+HistoryIter.next()+"\n");
        }
        System.out.println(builder.toString());

    }

    public static void History(String command){
        if(history.size()==5) history.removeLast();
        history.push(command);
    }

    public static boolean NullOut(String[] command){
        pack = new Package<String>(command[0], null);

        return true;      
    }

    public static boolean IntOut(String[] command, boolean isScript){
        String Snum;

        if(isScript){
            Snum = script.pop();
        }else if(command.length==1){
            Snum = "0";
        }else{
            Snum = command[1];            
        }

        Integer price = CreateTicketObject.priceGenerate(Snum); 

        pack = new Package<Integer>(command[0], price);

        return true;   
    }

    public static boolean IdOut(String[] command, boolean isScript){
        String Snum;

        if(isScript){
            Snum = script.pop();
        }else if(command.length==1){
            Snum = "null";
        }else{
            Snum = command[1];            
        }

        Long id = CreateTicketObject.idGenerate(Snum); 

        pack = new Package<Long>(command[0], id);

        return true;   
    }
    //id, price, name
    //price, name
    /*
    id - not null, >0
    price - >0
    name - not null, not ""
    */
    public static boolean TicketOut(String[] command, boolean isScript){
        String[] GArgs;     
   
        if(command.length==1){

            String[] args={command[0], "", "", ""};            
            GArgs = args;

        }else if(command.length==2){

            String[] args={command[0], command[1], "", ""};  
            GArgs = args;

        }else if(command.length==3){

            String[] args={command[0], command[1], command[2], ""};
            GArgs = args;  

        }else if(command.length==4&&(command[0].equals("update"))){

            String[] args={command[0], command[1], command[2], command[3]};
            GArgs = args;

        }else{
            System.out.println(command.length-1 + " data values are obtained, but " + (command[0].equals("update") ? 3 : 2) + " are required");
            return false;
        }

        Ticket tic; 
        if(isScript){
            tic = CreateTicketObject.generate(command[0], GArgs, true);
        }else{ 
            tic = CreateTicketObject.generate(command[0], GArgs, false);
        }        
        pack = new Package<Ticket>(command[0], tic);
        
        return true;


    }

    public static ArrayDeque<String> getScript(){
        return script;
    }
    public static BufferedReader getScan(){
        return input;
    }

    // получаем команды из кс
    // передаем строку коммандному менеджеру
    // он определяет команду и вызывает ее
    // она в свою очередь выполняется
}
