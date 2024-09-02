package invoker;

import java.util.LinkedHashMap;
import command.*;
import invoker.exception.*;
import java.lang.NullPointerException;
import receiver.VectorCollection;
import exception.MyException;
import java.io.*;
import server.connect.Package;
import java.util.logging.*;
import java.sql.SQLException;


public class CommandManager{
    private static String name = CommandManager.class.getName();
    private static Logger log;
    private static BufferedReader Scan;
    private static LinkedHashMap<String, BaseCommand> commandlist; // available commands
    private static LinkedHashMap<String, BaseCommand> Servercommandlist; // available server commands
    private static LinkedHashMap<String, String> packArg; // arguments for commands
	//public static String login;

    private static BaseCommand[] Servercommands = {
        //new SaveServerCommand(),
        new ExitServerCommand(),
        new HelpServerCommand(),
    };
  
    private static BaseCommand[] commands = {
        new AddCommand(),
        new HelpCommand(),
        new ShowCommand(),
        new ClearCommand(),
        new InfoCommand(), 
        new UpdateCommand(),
        new RemoveByIdCommand(),
        new AddIfMaxCommand(),   
        new RemoveAnyByPrice(),
        new RemoveLower(),
        new FilterByPrice(),
        new PrintFieldDescendingPrice(), 
    };

    /* 
        filling all of 3 dictionaries 
        filling vercotrcollection's list of commands
    */
    public static void initCommandManager(Logger log){
        CommandManager.log = log;

        commandlist = new LinkedHashMap<>();
        Servercommandlist = new LinkedHashMap<>();
        packArg = new LinkedHashMap<>();

        for(BaseCommand i : commands){
            commandlist.put(i.call(), i);
            VectorCollection.addCommand(i);
            packArg.put(i.call(), i.Arg());
        }

        for(BaseCommand i : Servercommands){
            Servercommandlist.put(i.call(), i);
            VectorCollection.addServerCommand(i);
        }
    }

    /* Определяет введенную команду и исполняет ее */
    public static String execute(Package pack, VectorCollection collection) throws IOException, SQLException{

        return commandlist.get(pack.command().split(" ")[0]).execute(pack, collection);
    }

    public static void ServerExecute(String line){
            String method = "ServerExecute";
            String[] commandName;

            commandName = line.split(" ");

            log.logp(Level.INFO, name, method, "ввод серверной команды: "+commandName[0]); 

            try{
                Servercommandlist.get(commandName[0]).execute(null, null);

                log.logp(Level.FINE, name, method, "серверная команда выполнена"); 

            }catch(NullPointerException e){

                System.out.print("\u001B[31m\nError: \u001B[0m incorrect command \""+line+"\"\n\ttry again\n");

                log.throwing(name, method, new CommandException(commandName[0]));

            }catch(Exception e){
                log.throwing(name, method, e);        
            }
    }

    public static LinkedHashMap<String, String> getPackArg(){
        return packArg;
    }
} // сортировка по id, поиск по id
