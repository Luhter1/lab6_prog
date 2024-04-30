package read;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import data.CreateTicketObject;
import java.io.IOException;

/** 
 * Класс, отвечающий за считывание обьектов из файла-коллекции.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ReadCSV{

    /** 
     * Считывает значения из файла-коллекции побайтно, пока не дойдет до конца файла.
     * <p>
     * Обьединяет chars в строки, при обнаружении символа перевода строки, каждую полученную строку передает в {@link data.CreateTicketObject#create(int, String[])} для создания обьекта типа {@link data.ticket.Ticket}.
     * <p>
     * При ошибке чтения данных, будет выведена строка с указанием номера строки файла-коллекции, где находится ошибка.
     *
     * @param path путь до файла, хранящего коллекцию
     * 
     * @exception IOException Если произошло исключение при чтении файла-коллекции
     *                       
     */
    public static void read(String path) throws IOException{

        FileInputStream fis = new FileInputStream(path);
        BufferedInputStream bis = new BufferedInputStream(fis);
        
        int line = 1;
        char[] char_word = new char[100]; // текущее слово в char
        int current_char; // текущий char
        int char_len = 0; // длинна текущего слова

        while((current_char = bis.read()) != -1){

            if(current_char==10){
                String word = new String(char_word);
                CreateTicketObject.create(line, word.split(";")); 

                char_word = new char[100];
                char_len = 0;
                line++;
            }else{
                char_word[char_len] = (char)current_char;
                char_len++; //44 - запятая, 32 - пробел, 10 - перевод строки, 59 - ;
            }
        }

        bis.close();

    }
}
