package command;

import pack.Package;
import receiver.VectorCollection;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на выход без сохранения коллекции в файл и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ExitCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по выходу без сохранения коллекции в файл
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public String execute(Package pack, VectorCollection collection){
        return "Соединение с сервером закрыто";
        // реализовано в цикле while в Connection.run()
    }


    public String call(){return "exit";}

    public String getName(){
        return call();
    }

    public String getDescription(){
        return "disconnect the client without saving collection to the file";
    }
}
