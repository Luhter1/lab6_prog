package command;

import receiver.VectorCollection;
import pack.Package;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на выход без сохранения коллекции в файл и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ExitServerCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по выходу без сохранения коллекции в файл
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public String execute(Package pack, VectorCollection collection){
        VectorCollection.Save();
        VectorCollection.exit();  
        return ""; 
    }


    public String call(){return "exit";}

    public String getName(){
        return call();
    }

    public String getDescription(){
        return "terminate the program with saving to a file";
    }
}
