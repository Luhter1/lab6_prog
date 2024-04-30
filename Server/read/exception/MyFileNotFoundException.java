package read.exception;

import exception.MyException;

/** 
 * Исключение, вызывающееся, если при попытке чтения файла, возникает ошибка чтения.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class MyFileNotFoundException extends MyException{
    /**
     * Содержит путь до файла
    */
    private String path;

    /**
     * Указывает, нужно ли выводить информацию по аргументам команд, для этого исключения 
     * @return нужно ли выводить информацию по аргументам команд
    */
    public boolean NeedArgs(){
        return false;
    }

    /**
     * Создает обьект типа данного класса
     * @param path путь до файла 
    */
    public MyFileNotFoundException(String path){
        this.path = path;
    }

    @Override
    public String toString(){
        return path + " (No such file or directory)";
    }
}
