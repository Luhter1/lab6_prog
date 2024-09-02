package client;

import java.io.*;
import java.net.*;
import java.util.*;
import server.connect.Package;
import data.CreateTicketObject;
import data.validation.*;
import data.ticket.Ticket;
import java.lang.StringBuilder;
import script.Script;
import java.lang.Math;
import java.security.*;
import java.math.BigInteger;
import authentication.UserAuth;
/** 
 * Класс отвечающий за создание обьектов команды, настройку их получателей и связывание с вызывающим.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class Client{
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); // чтение из консоли
    
    private static ArrayDeque<String> script = new ArrayDeque<>(); // массив команд скрипта
    private static ArrayDeque<String> history = new ArrayDeque<>(); // история команд
    
    private static LinkedHashMap<String, String> CommandsArg; // словарь: команда - аргументы
    private static Set<String> commands; // возможные команды
    private static Set<String> users; // список пользователей
    
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    private static int port = 1100;
    private static String address = "localhost";
    private static Socket socket;
    
	private static String[] login_pswd;


    
    public static void main(String[] a) throws IOException{
        getArgMap();

        login_pswd = UserAuth.auth(users);

        //
        System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
        //
        
        CommandLoop();
    }

    public static void getArgMap() throws IOException{
        //
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\u001B[35mПодключение к серверу\u001B[5;35m...\u001B[0m\n");
        //
        
        /* получение аргументов для команд */
        while(true){
            try{
            CommandsArg = (LinkedHashMap<String, String>)Send(
                          new Package<String>("args", null, "", ""), 
                          false);
            }catch(java.lang.ClassCastException e){continue;}
                          
            if(CommandsArg==null) continue;
            
            commands = CommandsArg.keySet();
            break;
        }
        
        /* получение пользователей */
        while(true){
            users = (Set<String>)Send(
                          new Package<String>("users", null, "", ""), 
                          false);
                          
            if(users!=null) break;
        }
        
        //
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\u001B[32mСоединение установлено\u001B[0m\n");
        //
        
        /* очистка введенного до подключения текста */
        while(input.ready()) input.readLine(); 

    }
    
    public static Object Send(Package pack, boolean InputError) throws IOException{
        Object answer = null;
        
        try{

            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(pack);
            out.flush();
            
            answer = in.readObject();

            in.close();
            out.close();
            socket.close();

        }catch(EOFException er){
            
        }catch(ConnectException er){
        
            if(InputError)
            // 
            System.out.println("\n\u001B[0;30;47mСервер временно не доступен\u001B[0m\n");
            return "";
            //
            
        }catch(SocketException er){
        
            /* очистка всех запланированных команд для выключенного сервера */
            script = new ArrayDeque<>();
            while(input.ready()) input.readLine();
            
            if(InputError) 
            //
            System.out.println("\n\u001B[0;31;47mСоединение с сервером потеряно\u001B[0m\n");
            //
            
        }catch(ClassNotFoundException er){
        
            if(InputError) 
            //
            System.out.println(er);
            //
        }
        return answer;


    }
    
    
    public static void CommandLoop() throws IOException{
        String line=null;

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

        /*
        if(!(boolean)Send(new Package<String[]>("login", login_pswd), true)){
            //
            System.out.println("Error: access to the account is denied due to incorrect data");
            //
            return true;
        }
        */

        String[] command;
		
        while(true){
            command = line.split(" ");

            if(command[0].equals("exit")){
                //
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("\n\033[0;34mTerminating client...\033[0m");
                //
                return true;

            }else if(command[0].equals("execute_script")){
                if(command.length==2){
                    Script.ExecuteScript(command[1]);
                    History(command[0]);
                    break;
                }else{
                    System.out.println("\n\033[0;34mНе указан файл\033[0m");
                    break;
                }

            }else if(command[0].equals("history")){

                HistoryToString();
                History(command[0]);
                break;

            }else if(commands.contains(command[0])){

                History(command[0]);

                switch(""+CommandsArg.get(command[0])){

                        case "null": 
                            NullOut(command);
                            break;

                        case "int": 
                            IntOut(command, isScript);
                            break;

                        case "id": 
                            IdOut(command, isScript);
                            break;

                        case "ticket": 
                            TicketOut(command, isScript);
                            break;
                }
                break;

            }else{
            
                //
                System.out.print("\u001B[31m\n  Command not found");
                //
                
                if(!isScript){
                
                    //
                    System.out.println("\u001B[0m\n");
                    //
                    
                    break;
                }else{
                
                    //
                    System.out.println(", try again\u001B[0m\n");
                    System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
                    //
                    
                    try{line = input.readLine();}catch(IOException ex){}
                }    
            }

            

        }
        
        //
        System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
        //
        
        return false;

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

    public static void NullOut(String[] command) throws IOException{
        System.out.println((String)Send(new Package<String>(command[0], null, login_pswd[0], login_pswd[1]), true));
    }

    public static void IntOut(String[] command, boolean isScript) throws IOException{
        String Snum;

        if(isScript){
            Snum = script.pop();
        }else if(command.length==1){
            Snum = "0";
        }else{
            Snum = command[1];            
        }

        Integer price = CreateTicketObject.priceGenerate(Snum); 

        System.out.println((String)Send(new Package<Integer>(command[0], price, login_pswd[0], login_pswd[1]), true));
    }

    public static void IdOut(String[] command, boolean isScript) throws IOException{
        String Snum;

        if(isScript){
            Snum = script.pop();
        }else if(command.length==1){
            Snum = "null";
        }else{
            Snum = command[1];            
        }

        Long id = CreateTicketObject.idGenerate(Snum); 

        System.out.println((String)Send(new Package<Long>(command[0], id, login_pswd[0], login_pswd[1]), true));
    }

    public static boolean TicketOut(String[] command, boolean isScript) throws IOException{
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
            //
            System.out.println(command.length-1 + " data values are obtained, but " + (command[0].equals("update") ? 3 : 2) + " are required");
            //
            
            return false;
        }

        Ticket tic; 
        if(isScript){
            tic = CreateTicketObject.generate(command[0], GArgs, true);
        }else{ 
            tic = CreateTicketObject.generate(command[0], GArgs, false);
        }        

        System.out.println((String)Send(new Package<Ticket>(command[0], tic, login_pswd[0], login_pswd[1]), true));
        
        return true;
    }
    
    public static ArrayDeque<String> getScript(){
        return script;
    }

    public static BufferedReader getScan(){
        return input;
    }
}
