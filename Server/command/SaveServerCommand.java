package command;

import receiver.VectorCollection;
import pack.Package;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на сохранение коллекции в файл и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class SaveServerCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по сохранению коллекции в файле
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */  
    public String execute(Package pack, VectorCollection collection){
        VectorCollection.Save();
        return "";
    }

    public String call(){return "save";}

    public String getName(){
        return call();   
    }

    public String getDescription(){
        return "save the collection to file";

    }
}
