package server;

import invoker.CommandManager;
import receiver.VectorCollection;
import receiver.exception.RecursionError;
import database.InitCreate;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import server.connect.*;
/* TODO
 - output is null if server unavailable and user try to execute commands
 - create database if non exist
 - same name isn't available
 - output creator's name
 - clear only user's items

*/ 


public class Server extends RecursiveAction{
    private static int port = 1100;
    private static ServerSocket server;
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static String name = Server.class.getName();
    private static Logger log = Logger.getLogger(name);
    private static ResourceBundle r = ResourceBundle.getBundle("i18n.ru.Server");
    /* Настройка логгирования */
    static{
        try{
            LogManager.getLogManager().readConfiguration(
                Server.class.getResourceAsStream("../logging.properties")
            );
        }catch(IOException|ExceptionInInitializerError e){
            System.out.print(r.getString("unavail_config"));
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
            System.out.println(r.getString("unavail_cr_file"));
            System.exit(0);
        }
    }

    /* запуск сервера */
    public static void main(String[] args){
        String method = "main";

        log.logp(Level.INFO, name, method, "запуск сервера");

        new Server().start();
    }  


    public void start(){
        String method = "start";        
        try{
            // create tables if non exists
            InitCreate.create();

            /* initialize an empty collection */
            VectorCollection.initVectorCollection(log);
            
            /* add commands */
            CommandManager.initCommandManager(log);
            
            /* upload collection */
            VectorCollection.getCollectionFromDatabase();
            //
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\u001B[32m" + r.getString("read"));
            //
            
            log.logp(Level.FINE, name, method, "коллекция получена");  
            //      
            System.out.println(r.getString("run") +  "\u001B[0m");
            //
            log.logp(Level.FINE, name, method, "сервер запущен");        
            VectorCollection.sort();
        }catch(IOException|NullPointerException|SQLException e){

            log.throwing(name, method, e);
            //
            System.out.println("\u001B[31m\t"+ r.getString("fail") +"\u001B[0m");
            //
            e.printStackTrace();
            System.exit(0);

        }
        
        try{
        	server = new ServerSocket(port);
            // время ожидания подключения 1с
	        //server.setSoTimeout(1000);
		}catch(IOException e){
		    log.throwing(name, method, e);
		    //
            System.out.println("\u001B[31m\n"+ r.getString("error") +"\u001B[0m "+e+"\n");
            //
            System.exit(0);
		}
		
        Runnable run = () -> {
            try{
                while(true){
                    if(input.ready()){
                            CommandManager.ServerExecute(input.readLine());
                            //
                            System.out.print("\u001B[33m" + r.getString("serv_comm") +"\u001B[0m");
                            //
                        }
                }
            }catch(IOException e){System.err.println(e);}
        };
        Thread t = new Thread(run);
        t.start();
        //
        System.out.print("\u001B[33m" + r.getString("serv_comm") +"\u001B[0m");
        //
        ForkJoinPool fjp = new ForkJoinPool();
        fjp.invoke(new Server());
        fjp.close();
        // вынести в обдельный поток чтение с консоли, отдельный поток для исполнения
        // в команды добавить lock на коллекцию
        // в главном потоке 
        // ожидания подключения, при подключении создание нового потока ожидания, а в этом обработка текущего
        // while(true){

        //     try{
        //         if(input.ready()){
        //             str = input.readLine();
        //             CommandManager.ServerExecute(str);
        //             //
        //             System.out.print("\u001B[33m" + r.getString("serv_comm") +"\u001B[0m");
        //             //
        //         }else{
                    
        //             try{
        //                 socket = server.accept();
        //                 new Connection(socket).start();
        //             }catch(SocketTimeoutException e){
        //                 continue;
        //             }catch(IOException e){
        //                 //
        //                 System.out.println("\u001B[31m\n"+ r.getString("error") +"\u001B[0m "+e+"\n");
        //                 //
        //                 log.throwing(name, method, e);
        //             }
        //         }
        //     }catch(IOException inputError){
        //         //
        //         System.out.println("\u001B[31m\n"+ r.getString("error") +"\u001B[0m "+inputError+"\n");
        //         //
        //         log.throwing(name, method, inputError);
        //         System.exit(0);
        //     }
        // }   
    }

    public static ServerSocket getServer(){
        return server;
    }

    protected void compute(){
        try{
        Socket socket = server.accept();
        new Connection(socket).start();
        invokeAll(new Server());
        }catch(IOException e){System.err.println(e);}
    } 
}
