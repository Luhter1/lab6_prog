package server;

import invoker.CommandManager;
import receiver.VectorCollection;
import java.io.*;
import read.ReadCSV;
import server.Connection;
import java.net.*;
import java.util.LinkedList;


/** 
 * Класс отвечающий за создание обьектов команды, настройку их получателей и связывание с вызывающим.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class Server{
    private static int port = 1111;
    private static ServerSocket server;
    private static LinkedList<Connection> connectList = new LinkedList<>();
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    

    public static void main(String[] args){
            
            String path = System.getenv("LAB5FILE");
          
            if(path==null){
                System.out.println("\u001B[31m\nError: \u001B[0m переменая окружения не обнаружена\n");
                System.exit(0);
            }
           
            try{
                new Server().start(path);
            }catch(IOException e){
                e.printStackTrace();
            }
    }  

    // <a>{@link BufferedReader}</a>
    // @see invoker.CommandManager#CommandManager()

    /** 
     * Запускает работу клиента
     * <p>
     * <ul>
     *   <li>Инициализирует коллекцию для хранения и генератор id.</li>
     *   <li>Считывает значения из файла-коллекции и записывает в коллекцию.</li>
     *   <li>Добавляет тэги для вызова команд.</li>
     *   <li>Сохраняет ссылку на обьект для считывания значений</li>
     *   <li>Запускает цикл ожидания и исполнения.</li>
     * </ul>
     * <p>
     * Если при попытке чтения файла-коллекции возникнет ошибка {@link IOException}, то все значения будут сохраняться в новый файл, по пути path.
     * <p>
     * После считывания всей коллекции из файла, реализует сортировку по значению id
     * @param path путь до файла, хранящего коллекцию
     * @param input обьект откуда происходит считывание команд, введенных пользователем
     * @exception IOException Если произошло исключение при чтении из input 
     *                       
     */
    public void start(String path) throws IOException{
        new VectorCollection(path); //создает коллекцию для хранения и генератор id (пока нулевой)    
        new CommandManager();
        try{
            
            ReadCSV.read(path);
            System.out.println("\u001B[32m" + "The collection has been read, all data is correct");
            System.out.println("Server is running...\n" +  "\u001B[0m");
            VectorCollection.sort();
        }catch(IOException e){
            System.out.println("\u001B[31mКоллекция не была загружена\u001B[0m\n\nНовые значения будут сохраняться в файле:\n    "+path);
        }
        
        server = new ServerSocket(port);
        
        String str;
        // должна быть реализация exit, save, connect
        System.out.print("\u001B[33m" + "Entry server command: " +"\u001B[0m");
        while(true){
            if(input.ready()){
                str = input.readLine();
                CommandManager.ServerExecute(str);
                System.out.print("\u001B[33m" + "Entry server command: " +"\u001B[0m");
            }else{
                Socket socket = server.accept();
                try{
                    connectList.add(new Connection(socket));
                }catch(IOException e){
                    System.out.println(e);
                }finally{
                    socket.close();
                }
            }
            //System.out.print("\u001B[33m" + "Entry server command: " +"\u001B[0m");

            //this.connecting();
        }
        //server.close();
  
    }


    public static void connecting() throws IOException{
        System.out.println("Ожидание установления соединения с клиентом");
        while(true){
            Socket socket = server.accept();
            try{
                connectList.add(new Connection(socket));
            }catch(IOException e){
                System.out.println(e);
            }finally{
                socket.close();
            }
        }
    }
    // получаем команды из client
    // передаем строку коммандному менеджеру
    // он определяет команду и вызывает ее
    // она в свою очередь выполняется
}
