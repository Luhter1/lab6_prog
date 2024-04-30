package command.exception;

import exception.MyException;

/** 
 * Исключение, вызывающееся при недостатке аргументов для команды.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class LackOfDataException extends MyException{
    /**
     * скольк есть, сколько должно быть аргументов
    */ 
    int current, need;

    /**
     * Указывает, нужно ли выводить информацию по аргументам команд, для этого исключения. 
     * @return нужно ли выводить информацию по аргументам команд
    */
    public boolean NeedArgs(){
        return true;
    }

    /**
     * Создает обьект типа данного класса.
     * @param cur сколько есть аргументов
     * @param n сколько должно быть аргументов    
    */
    public LackOfDataException(int cur, int n){
        current = cur;
        need = n;
    }

    public String toString(){
        return current + " data values are obtained, but " + need + " are required";
    }
}
