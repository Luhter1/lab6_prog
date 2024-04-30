package data.validation.exception;

import exception.MyException;
/** 
 * Исключение, вызывающееся, если строка имеет вид "null".
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class NullValueException extends MyException{
    /**
     * поле, значение которого является "null"
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
     * @param o название поля, значение которого является "null" 
    */
    public NullValueException(String o){object = o;}
    
    public String toString(){
        return object + " cannot have a null value";
    }
}
