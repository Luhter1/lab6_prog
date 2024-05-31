package server;

import invoker.CommandManager;
import receiver.VectorCollection;
import read.ReadCSV;
import server.Connection;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.logging.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * Класс отвечающий за создание обьектов команды, настройку их получателей и связывание с вызывающим.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class Server{
    private static int port = 1100;
    private static ServerSocket server;
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static String name = Server.class.getName();
    private static Logger log = Logger.getLogger(name);

    //Настройка логгирования
    static{
        try{
            LogManager.getLogManager().readConfiguration(
                Server.class.getResourceAsStream("../logging.properties")
            );
        }catch(IOException|ExceptionInInitializerError e){
            System.out.println("\nНе удается получить конфигурацию для логгирования\n");
            System.exit(0);
        }
        log.setUseParentHandlers(false);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        try{
            FileHandler fh = new FileHandler("./Log/log"+date+".log", true);
            log.addHandler(fh);
        }catch(IOException e){
            System.out.println("\nНе удается создать файл для логгирования\n");
            System.exit(0);
        }
    }

    public static void main(String[] args){
            String method = "main";

            try{
                log.logp(Level.INFO, name, method, "запуск сервера");

                String path = System.getenv("LAB5FILE");
              
                if(path==null){
                    throw new EnvError();
                }

                log.logp(Level.FINE, name, method, "переменая окружения получена");        

            
                new Server().start(path);
            }catch(IOException e){
                System.out.println("\u001B[31m\nError: \u001B[0m "+e+"\n");
                log.throwing(name, method, e);
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
        String method = "start";        

        new VectorCollection(path, log); //создает коллекцию для хранения и генератор id (пока нулевой)    
        new CommandManager(log);
        try{
            
            ReadCSV.read(path);
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\u001B[32m" + "The collection has been read, all data is correct");

            log.logp(Level.FINE, name, method, "коллекция считана");        
            System.out.println("Server is running...\n" +  "\u001B[0m");
            log.logp(Level.FINE, name, method, "сервер запущен");        
            VectorCollection.sort();
        }catch(IOException e){

            log.logp(Level.WARNING, name, method, "коллекция не найдена");
            log.logp(Level.INFO, name, method, "новые значения сохраняются в "+path);        

            System.out.println("\u001B[31mКоллекция не была загружена\u001B[0m\n\nНовые значения будут сохраняться в файле:\n    "+path+"\n");

        }
        
        server = new ServerSocket(port);
        server.setSoTimeout(1000);

        String str;
        Socket socket=null;
        System.out.print("\u001B[33m" + "Entry server command: " +"\u001B[0m");

        while(true){

            if(input.ready()){
                str = input.readLine();
                CommandManager.ServerExecute(str);
                System.out.print("\u001B[33m" + "Entry server command: " +"\u001B[0m");
            }else{
                
                try{
                    socket = server.accept();
                    new Connection(socket, log);
                }catch(SocketTimeoutException e){
                    continue;
                }catch(IOException e){
                    System.out.println(e);
                    socket.close();
                }

            }

        }
        
  
    }

    public static ServerSocket getServer(){
        return server;
    }
}
