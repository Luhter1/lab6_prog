package invoker;

import java.util.LinkedHashMap;
import command.*;
import invoker.exception.*;
import java.lang.NullPointerException;
import receiver.VectorCollection;
import exception.MyException;
import java.io.*;
import pack.Package;
import java.util.logging.*;

/** 
 * Класс, отвечающий за инициирование выполнения команды.
 * <p>
 * Он содержит ссылку на объект команды и может выполнить команду, вызвав ее метод execute().
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class CommandManager{
    private static String name = CommandManager.class.getName();
    private static Logger log;
    private static BufferedReader Scan;
    private static LinkedHashMap<String, BaseCommand> commandlist;
    private static LinkedHashMap<String, BaseCommand> Servercommandlist;
    private static LinkedHashMap<String, String> packArg;

    private static BaseCommand[] Servercommands = {
        new SaveServerCommand(), //
        new ExitServerCommand(), //
        new HelpServerCommand(), //
    };
  
    private static BaseCommand[] commands = {
        new AddCommand(), //
        new HelpCommand(), //
        new ShowCommand(), //
        new ClearCommand(), //
        new InfoCommand(), //
        new UpdateCommand(), //
        new RemoveByIdCommand(), //
        new AddIfMaxCommand(), //  
        new RemoveAnyByPrice(), //
        new RemoveLower(), //
        new FilterByPrice(), //
        new PrintFieldDescendingPrice(), // 
    };

    /**
     * Инициализирует пустой словарь с командами.
     * <p>
     * Ключ - значение {@link command.BaseCommand#call()} для конкретной команды
    */
    public CommandManager(Logger log){
        this.log = log;

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

    /**
     * Определяет введенную команду и исполняет ее.
     * <p>
     * Вызывает метод {@link command.BaseCommand#execute(String[])} для конкретной команды
     * Добавляет команду в очередь истории команд
     * @param line Введенная строка с командой
    */ 
    public static String execute(Package pack, VectorCollection collection){

        String output = commandlist.get(pack.command()).execute(pack, collection);

        return output;
    }

    public static void ServerExecute(String line){
            String method = "ServerExecute";
            String[] commandName;

            // обработка, если нет команды
            while(true){

                commandName = line.split(" ");

                log.logp(Level.INFO, name, method, "ввод серверной команды: "+commandName[0]); 

                try{
                    Servercommandlist.get(commandName[0]).execute(null, null);

                    log.logp(Level.FINE, name, method, "серверная команда выполнена"); 

                    break;
                }catch(NullPointerException e){

                        System.out.print("\u001B[31m\nError: \u001B[0m incorrect command \""+line+"\"\n\ttry again\n\n");

                        log.throwing(name, method, new CommandException(commandName[0]));

                        try{
                            line = Scan.readLine();
                        }catch(Exception er){
                            log.throwing(name, method, er);
                        };
                }catch(Exception e){
                    log.throwing(name, method, e);        
                }

            }

    }

    /**
     * Возвращает обьект, с которого считываются команды
     * @return обьект, с которого считываются команды
    */ 
    public static BufferedReader getScan(){
        return Scan;
    }

    /**
     * Устанавливает обьект, с которого считываются команды
     * @param scan обьект, с которого считываются команды
    */ 
    public static void setScan(BufferedReader scan){
        Scan = scan;
    }

    public static LinkedHashMap<String, String> getPackArg(){
        return packArg;
    }
} // сортировка по id, поиск по id
