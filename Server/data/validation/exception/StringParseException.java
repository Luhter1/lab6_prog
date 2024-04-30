package data.validation.exception;

import exception.MyException;

/** 
 * Исключение, вызывающееся при неудачной попытки приведения строки к требуемому типу.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class StringParseException extends MyException{
    /**
     * тип, к которому нужно было привести
    */
    String type;
    /**
     * значение, которое нельзя привести к типу, который нужен
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
     * @param t строковое значение типа, к которому нужно было привести
     * @param o значение, которое нельзя привести к типу, который нужен
    */
    public StringParseException(String t, String o){type = t; object = o;}
    
    public String toString(){
        return "Cannot parse \"" + object + "\" type String to type " + type;
    }
}
