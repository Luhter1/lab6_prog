package receiver.exception;

import exception.MyException;

/** 
 * Исключение, вызывающееся при вызове криптом самого себя.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class RecursionError extends MyException{
    /**
     * Указывает, нужно ли выводить информацию по аргументам команд, для этого исключения. 
     * @return нужно ли выводить информацию по аргументам команд
    */
    public boolean NeedArgs(){
        return false;
    }

    @Override
    public String toString(){
        return "a loop has been detected in the executable script";
    }
}
