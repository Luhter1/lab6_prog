package command;

import receiver.VectorCollection;
import pack.Package;
import data.ticket.Ticket;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на добавление пользователем нового обьекта в коллекцию и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class AddCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по добавлению пользователем нового обьекта
     * @param args название команды и параметры для нее
    */
    public String execute(Package pack, VectorCollection collection){

        return VectorCollection.add((Ticket)pack.arg());
    }


    @Override
    public String Arg(){
        return "ticket";    
    }

    public String call(){return "add";}

    public String getName(){
        return call() + " [name price]";
    }

    public String getDescription(){
        return "add new element to the end of vector's collection\n[name] cannot be null or empty\n[price] must be greater then 0 or be null";
    }
}
