package command;

import pack.Package;
import receiver.VectorCollection;
/** 
 * Интерфейс, который определяет метод, необходимый для выполнения команды.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public interface BaseCommand{
    /**
     * Метод, определяющий операции для выполнения команды
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public String execute(Package pack, VectorCollection collection);

    /**
     * Возвращает строковое представление команды для команды {@link command.ShowCommand}
     * @return строковое представление команды 
    */
    public String getName(); // представление команды (вызов - параметры)

    /**
     * Возвращает описание команды
     * @return описание команды
    */
    public String getDescription(); // описание команды

    /**
     * Возвращает название команды для вызова
     * @return название команды для вызова
    */
    public String call(); // название команды для вызова в консоли

    // Какие аргументы отсылать с пакетом
    public default String Arg(){
        return null;    
    }
}
