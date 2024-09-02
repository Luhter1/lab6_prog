package command;

import receiver.VectorCollection;
import server.connect.Package;
import data.ticket.Ticket;
import java.io.IOException;
import java.sql.SQLException;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на добавление пользователем нового обьекта в коллекцию, если его цена больше максимальной в коллекции, и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class AddIfMaxCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по добавлению пользователем нового обьекта, если его цена больше максимальной в коллекции
     * @param args название команды и параметры для нее
    */
    public String execute(Package pack, VectorCollection collection) throws IOException, SQLException{
        return VectorCollection.addIf((Ticket)pack.arg(), (String)pack.login());
    }

    @Override
    public String Arg(){
        return "ticket";    
    }

    public String call(){return "add_if_max";}

    public String getName(){
        return call() + " [name price]";
    }

    public String getDescription(){
        return "add new element to the end of vector's collection if element's price is the greatest\n[name] cannot be null or empty\n[price] must be greater then 0 or be null";
    }
}
