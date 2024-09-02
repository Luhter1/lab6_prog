package command;

import receiver.VectorCollection;
import server.connect.Package;
import java.io.IOException;
import java.sql.SQLException;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на очистку коллекции и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ExecuteScriptCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по очистке коллекции
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public String execute(Package pack, VectorCollection collection) throws IOException, SQLException{
        return "";
    }


    public String call(){return "execute_script";}

    public String getName(){
        return call() + " [path_to_script]";
    }

    public String getDescription(){
        return "execute commands from script";
    }
}
