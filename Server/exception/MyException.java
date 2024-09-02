package exception;

/** 
 * Исключение, являющееся родителем всех исключений данной работы.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class MyException extends Exception{
    /**
     * Указывает, нужно ли выводить информацию по аргументам команд, для этого исключения. 
     * @return нужно ли выводить информацию по аргументам команд
    */
    public boolean NeedArgs(){
        return false;
    }
}
