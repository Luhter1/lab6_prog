package command;

import server.Server;
import java.io.IOException;
import pack.Package;
import receiver.VectorCollection;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на очистку коллекции и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ConnectServerCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по очистке коллекции
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public String execute(Package pack, VectorCollection collection){
        try{Server.connecting();}catch(IOException e){}
        return "";
        
    }

    public String call(){return "connect";}

    public String getName(){
        return call();
    }

    public String getDescription(){
        return "open connection for client";
    }
}
