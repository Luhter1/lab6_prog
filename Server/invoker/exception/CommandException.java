package invoker.exception;

/** 
 * Исключение, вызывающееся, если команда не существует.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class CommandException extends Throwable{
    String commandName;

    /**
     * Создает обьект типа данного класса.
     * @param cN имя не существующей команды
    */
    public CommandException(String cN){

        commandName = cN;

    }

    public String toString(){

        return "серверная команда \"" + commandName + "\" не найдена";

    }
    
}
