package invoker;

import java.util.LinkedHashMap;
import command.*;
import invoker.exception.*;
import java.lang.NullPointerException;
import receiver.VectorCollection;
import exception.MyException;
import java.io.*;
import pack.Package;

/** 
 * Класс, отвечающий за инициирование выполнения команды.
 * <p>
 * Он содержит ссылку на объект команды и может выполнить команду, вызвав ее метод execute().
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class CommandManager{
    /**
     * Обьект, с которого считываются команды
    */
    private static BufferedReader Scan;
    /**
     * Словарь команд, ключ - значение {@link command.BaseCommand#call()} для конкретной команды
    */
    private static LinkedHashMap<String, BaseCommand> commandlist;
    private static LinkedHashMap<String, BaseCommand> Servercommandlist;

    // Какие аргументы отсылать с пакетом
    private static LinkedHashMap<String, String> packArg;

    private static BaseCommand[] Servercommands = {
        new SaveServerCommand(), //
        new ExitServerCommand(), //
        new HelpServerCommand(), //
        new ConnectServerCommand() //
    };

    /**
     * Все доступные команды
    */    
    private static BaseCommand[] commands = {
        new AddCommand(), //
        new HelpCommand(), //
        new ExitCommand(), //
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
        new HistoryCommand(), //
        new ExecuteScriptCommand() //
    };

    /**
     * Инициализирует пустой словарь с командами.
     * <p>
     * Ключ - значение {@link command.BaseCommand#call()} для конкретной команды
    */
    public CommandManager(){
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

        if(collection.HistorySize()==5) collection.removeLastHistory();
        collection.pushHistory(pack.command());

        return output;
    }

    public static void ServerExecute(String line){

            String[] commandName = line.split(" ");
            
            // обработка, если нет команды
            try{
                Servercommandlist.get(commandName[0]).execute(null, null);
            }catch(NullPointerException e){

                    System.out.print("\u001B[31m\nError: \u001B[0m incorrect command \""+line+"\"\n\ttry again: ");
                    try{ServerExecute(Scan.readLine());}catch(Exception er){};
            }catch(Exception e){}

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
