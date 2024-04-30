package server;

import java.net.*;
import java.io.*;
import invoker.CommandManager;
import pack.Package;
import receiver.VectorCollection;

public record Connection(Socket socket, ObjectInputStream in, ObjectOutputStream out){

    public Connection(Socket socket) throws IOException {
        this(socket, 
             new ObjectInputStream(socket.getInputStream()), 
             new ObjectOutputStream(socket.getOutputStream()));
        // если потоки ввода/вывода приведут к генерированию исключения, оно пробросится дальше
        this.run();
        this.socket.close();
    }

    private void run() throws IOException{
        VectorCollection collection = new VectorCollection();
        try {
            System.out.println("Соединение установлено");
            out.writeObject(CommandManager.getPackArg());
            while (true) {
                try{
                    Package word = (Package)in.readObject();
  
                    out.writeObject(CommandManager.execute(word, collection));    
  
                    if(word.equals("exit")) break;
                
                }catch(ClassNotFoundException er){
                    System.out.println(er);
                }
            }

        }catch(EOFException er){
            System.out.println("Соединение закрыто клиентом");
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
