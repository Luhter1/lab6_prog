package data.validation.exception;

import exception.MyException;

/** 
 * Исключение, вызывающееся, если число не позитивное.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class PositiveException extends MyException{
    /**
     * поле, значение которого не позитивно
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
     * @param o название поля, значение которого не позитивно 
    */
    public PositiveException(String o){object = o;}
    
    public String toString(){
        return object + " value must be a positive";
    }
}
