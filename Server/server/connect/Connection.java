package server.connect;

import java.net.*;
import java.io.*;
import invoker.CommandManager;
import receiver.VectorCollection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import database.SQLManager;

public class Connection extends Thread{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());


        this.run();
    }

    public void run(){
        VectorCollection collection = new VectorCollection();
        
         /* обработка окончания пакета и приведения типов*/
        try {

            Package pack = (Package)in.readObject();

            String command = pack.command(); 
            
            Runnable r;

            switch(command){
                case "args":
                    /* аргументы для команд */
                    Map<String, String> packArg = CommandManager.getPackArg(); 

                    r = () -> {
                        try{out.writeObject(packArg);
                        }catch(IOException e){}
                    };
                    break;
                             
                             
                case "users":
                    /* множество логинов пользователей */
                    Set<String> users = VectorCollection.getUsers();
                    
                    r = () -> {
                        try{out.writeObject(users);
                        }catch(IOException e){}
                    };
                    break;        
                       
                             
                case "login":
                    /* true or false */
                    boolean success = VectorCollection.login(pack);

                    r = () -> {
                        try{out.writeObject(success);
                        }catch(IOException e){}
                    };
                    break;
                
                
                case "register":
                    VectorCollection.register(pack);
                    r = () -> {};
                    break;
            
            
                default:
                    /* command's output */
                    String out_message;
                    if(SQLManager.login(pack.login(), pack.pswd())){
                        out_message = CommandManager.execute(pack, collection);
                    }else{
                        out_message = "Error: incorrect login or data";

                    }

                    r = () -> {
                        try{out.writeObject(out_message);
                        }catch(IOException e){}
                    };
                    break;
            }
            Thread t = new Thread(r);
            t.start();
            try{
                t.join();
            }catch(InterruptedException e){}
            out.flush();
            this.in.close();
            this.out.close();
            this.socket.close();

        }catch(EOFException|ClassCastException|ClassNotFoundException er){


        }catch(SQLException DBAcessError){
        
            System.out.println(DBAcessError);
            try{
                out.writeObject(null);
                out.flush();
            }catch(IOException err){}
        }catch(IOException e){}
    }
}
