package server;

import java.net.*;
import java.io.*;
import invoker.CommandManager;
import pack.Package;
import receiver.VectorCollection;
import java.util.logging.*;

public record Connection(Socket socket, ObjectInputStream in, ObjectOutputStream out, Logger log, String name, SocketAddress addr){

    public Connection(Socket socket, Logger log) throws IOException {
        this(socket, 
             new ObjectInputStream(socket.getInputStream()), 
             new ObjectOutputStream(socket.getOutputStream()),
             log,
             Connection.class.getName(),
             socket.getLocalSocketAddress()
        );

        log.logp(Level.FINE, this.name, "", "получено подключение to " + this.addr);
        // если потоки ввода/вывода приведут к генерированию исключения, оно пробросится дальше
        this.run();
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    private void run() throws IOException{
        String method = "run";
        VectorCollection collection = new VectorCollection();

        try {
            //System.out.println("Соединение установлено");

            try{
                Package pack = (Package)in.readObject();  
 
            log.logp(Level.INFO, name, method, "получен запрос \""+pack.command()+"\" from "+this.addr);

                String outer = CommandManager.execute(pack, collection);

                out.writeObject(outer);    
                out.flush(); 
                log.logp(Level.FINE, name, method, "отправлен ответ to "+this.addr);
            }catch(ClassCastException er){
                log.logp(Level.FINE, name, method, "отправлены аргументы для комманд to "+this.addr);

                out.writeObject(CommandManager.getPackArg());
                out.flush(); 
            
            }catch(ClassNotFoundException er){
                log.throwing(name, method, er);
            }


        }catch(EOFException er){
                log.throwing(name, method, er);
        }
        /*
        do {// gthtytcnb exit to command and clear too
            System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
            str = input.readLine();
            if(str==null){
                CommandManager.execute("exit", false);
            }
            CommandManager.execute(str, false);

        } while(true);
        */

    }
}
