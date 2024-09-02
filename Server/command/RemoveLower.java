package command;

import receiver.VectorCollection;
import server.connect.Package;
import java.io.IOException;
import data.ticket.Ticket;
import java.sql.SQLException;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на удаление из коллекции всех обьектов с id меньше, чем указанное, и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class RemoveLower implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по удалению из коллекции всех обьектов с id меньше, чем указанное
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public String execute(Package pack, VectorCollection collection) throws IOException, SQLException{
        return VectorCollection.removeLower((Ticket)pack.arg(), (String)pack.login());
    }

    @Override
    public String Arg(){
        return "ticket";    
    }
    // создать обьект билет, получить его прайс и удалить
    public String call(){return "remove_lower";}

    public String getName(){
        return call() + " [name price]";
    }

    public String getDescription(){
        return "remove all elements from the collection if their price lower then new element\n[name] cannot be null or empty\n[price] must be greater then 0 or be null";
    }
}
