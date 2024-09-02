package data.validation.exception;

import exception.MyException;

/** 
 * Исключение, вызывающееся, если значение не является уникальным.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class UniqueException extends MyException{
    /**
     * значение, не являющееся уникальным
    */ 
    String object;

    /**
     * Указывает, нужно ли выводить информацию по аргументам команд, для этого исключения. 
     * @return нужно ли выводить информацию по аргументам команд
    */
    public boolean NeedArgs(){
        return true;
    }

    /**
     * Создает обьект типа данного класса.
     * @param o значение, не являющееся уникальным
    */
    public UniqueException(String o){object = o;}
    
    public String toString(){
        return object + " value is not unique";
    }
}
