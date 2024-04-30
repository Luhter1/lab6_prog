package command;

import receiver.VectorCollection;
import pack.Package;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на удаление 1-ого обьекта по указанному id и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class RemoveByIdCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по удалению 1-ого обьекта по указанному id
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */       
    public String execute(Package pack, VectorCollection collection){
        return VectorCollection.remove((long)pack.arg());
    }

    @Override
    public String Arg(){
        return "id";    
    }

    public String call(){return "remove_by_id";}

    public String getName(){
        return call() + " [id]";
    }

    public String getDescription(){
        return "remove element from collection by id\n[id] must be greater then 0 and not null";
    }
}
