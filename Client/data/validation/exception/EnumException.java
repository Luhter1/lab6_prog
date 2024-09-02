package data.validation.exception;

import exception.MyException;

/** 
 * Исключение, вызывающееся, если указанное значение отсутствует в перечислении.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class EnumException extends MyException{
    /**
     * значение, которого нет в перечислении
    */ 
    String object;

    /**
     * Указывает, нужно ли выводить информацию по аргументам команд, для этого исключения 
     * @return нужно ли выводить информацию по аргументам команд
    */
    public boolean NeedArgs(){
        return true;
    }

    /**
     * Создает обьект типа данного класса.
     * @param o значение, которого нет в перечислении 
    */
    public EnumException(String o){object = o;}
    
    public String toString(){
        return "This enum cannot take a value of " + object;
    }
}
