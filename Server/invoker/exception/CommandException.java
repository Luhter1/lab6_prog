package invoker.exception;

import exception.MyException;
/** 
 * Исключение, вызывающееся, если команда не существует.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class CommandException extends MyException{
    /**
     * Имя не существующей команды
    */ 
    String commandName;

    /**
     * Указывает, нужно ли выводить информацию по аргументам команд, для этого исключения. 
     * @return нужно ли выводить информацию по аргументам команд
    */
    public boolean NeedArgs(){
        return true;
    }

    /**
     * Создает обьект типа данного класса.
     * @param cN имя не существующей команды
    */
    public CommandException(String cN){

        commandName = cN;

    }

    public String toString(){

        return "command \"" + commandName + "\" was not found";

    }
    
}
