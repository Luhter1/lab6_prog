package data.validation.exception;

import exception.MyException;

/** 
 * Исключение, вызывающееся, если строка является пустой.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class EmptyStringException extends MyException{
    /**
     * поле, значение которого пусто
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
     * @param o название поля, значение которого пусто 
    */
    public EmptyStringException(String o){object = o;}
    
    public String toString(){
        return object + " cannot have an empty value";
    }
}
