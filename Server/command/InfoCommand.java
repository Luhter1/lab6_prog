package command;

import receiver.VectorCollection;
import pack.Package;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на вывод информации о коллекции и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class InfoCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по выводу информации о коллекции
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public String execute(Package pack, VectorCollection collection){
        return collection.info();
    }

    public String call(){return "info";}

    public String getName(){
        return call();   
    }

    public String getDescription(){
        return "output informations about the collection to the standard output stream";

    }
}
