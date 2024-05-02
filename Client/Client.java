package client;

import java.io.*;
import java.net.*;
import java.util.*;
import pack.Package;
import data.CreateTicketObject;
import data.validation.*;
import data.ticket.Ticket;

/** 
 * Класс отвечающий за создание обьектов команды, настройку их получателей и связывание с вызывающим.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class Client{
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static Socket socket; //сокет для общения
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static LinkedHashMap<String, String> packArg;
    private static Set<String> keys;
    private static boolean Success = true;
    
    // !!!!!!!!! execute_script перенести на клиента
    /* что нужно передавать:
                -- название команды
                -- название команды + билет
                -- название команды + число
        решение: создание обьекта transfer, он содержит поле type: null, ticket, int, id
                
    */
    public static void main(String[] args){
        System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
        while(true){
            Main();
        }
    }

    public static void Main(){
        try {

            try {
                socket = new Socket("localhost", 1111);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                //System.out.println("Соединение с сервером успешно");
                // если соединение произошло и потоки успешно созданы
                // - предложить клиенту что то ввести
                // если нет - вылетит исключение

                // Получаем аргументы, для передачи
                try{
                    packArg = (LinkedHashMap<String, String>)in.readObject();
                    keys = packArg.keySet();
                }catch(ClassNotFoundException er){System.out.println(er);}

                // Запускаем основной цикл
                String word;
                String[] command;
                String serverWord;




                    word = input.readLine();

                    command = word.split(" ");

                    if(keys.contains(command[0])){

                        switch(""+packArg.get(command[0])){

                            case "null": 
                                NullOut(command);
                                break;

                            case "int": 
                                IntOut(command);
                                break;

                            case "id": 
                                IdOut(command);
                                break;

                            case "ticket": 
                                TicketOut(command);
                                break;
                        }
                        // отправляем сообщение на сервер

                        if(Success){
                            try{
                                serverWord = (String)in.readObject(); // ждём, что скажет сервер
                                System.out.println(serverWord); // получив - выводим на экран
                            }catch(ClassNotFoundException er){System.out.println(er);}
                        }

                        //if(command[0].equals("exit")) break;
                        //else if(command[0].equals("execute_script")) break;
                    }else{
                        System.out.println("Command not found\n");    
                    }
   
            } finally { // в любом случае необходимо закрыть сокет и потоки
                //System.out.println("Клиент был закрыт...");
                System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
  
    }

    public static void NullOut(String[] command) throws IOException{
        out.writeObject(new Package<String>(command[0], null));
        out.flush(); 
        Success = true;      
    }

    public static void IntOut(String[] command) throws IOException{
        String Snum;

        if(command.length==1){
            Snum = "0";
        }else{
            Snum = command[1];            
        }

        Integer price = CreateTicketObject.priceGenerate(Snum); 
        out.writeObject(new Package<Integer>(command[0], price));
        out.flush();
        Success = true;   
    }

    public static void IdOut(String[] command) throws IOException{
        String Snum;

        if(command.length==1){
            Snum = "null";
        }else{
            Snum = command[1];            
        }

        Long id = CreateTicketObject.idGenerate(Snum); 
        out.writeObject(new Package<Long>(command[0], id));
        out.flush();
        Success = true;   
    }
    //id, price, name
    //price, name
    /*
    id - not null, >0
    price - >0
    name - not null, not ""
    */
    public static void TicketOut(String[] command) throws IOException{
        Ticket tic;        
        if(command.length==1){
            String[] args={command[0], "", "", ""};

            tic = CreateTicketObject.generate(command[0], args, false);
            out.writeObject(new Package<Ticket>(
                                            command[0],
                                            tic 
                                                ));
            out.flush();
            Success = true;   

        }else if(command.length==2){
            String[] args={command[0], command[1], "", ""};

            tic = CreateTicketObject.generate(command[0], args, false);
            out.writeObject(new Package<Ticket>(
                                            command[0],
                                            tic 
                                                ));
            out.flush(); 
            Success = true;   

        }else if(command.length==3){
            String[] args={command[0], command[1], command[2], ""};

            tic = CreateTicketObject.generate(command[0], args, false);
            out.writeObject(new Package<Ticket>(
                                            command[0],
                                            tic 
                                                ));
            out.flush(); 
            Success = true;   

        }else if(command.length==4&&(command[0].equals("update"))){
            String[] args={command[0], command[1], command[2], command[3]};

            tic = CreateTicketObject.generate(command[0], args, false);
            out.writeObject(new Package<Ticket>(
                                            command[0],
                                            tic 
                                                ));
            out.flush(); 
            Success = true;   

        }else{
            System.out.println(command.length-1 + " data values are obtained, but " + (command[0].equals("update") ? 3 : 2) + " are required");
            Success = false;

        }


    }

    public static BufferedReader getScan(){
        return input;
    }

    // получаем команды из кс
    // передаем строку коммандному менеджеру
    // он определяет команду и вызывает ее
    // она в свою очередь выполняется
}
