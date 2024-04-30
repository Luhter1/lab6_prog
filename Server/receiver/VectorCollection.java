package receiver;

import java.time.LocalDate;
import data.ticket.Ticket;
import data.id.IDGenerator;
import java.util.*;
import command.BaseCommand;
import data.validation.validation;
import data.price.Price;
import receiver.exception.*;
import java.io.*;
import exception.MyException;
import read.exception.*;
import java.lang.StringBuilder;
// Receiver

/**
* Класс, который выполняет фактическую операцию во время выполнении команды
* 
* @author   Timofei Kaparulin
* @version  1.0
*/
public class VectorCollection{
    /**
    * Список, содержащий все дотупные команды
    */
    private static ArrayList<BaseCommand> Commands = new ArrayList<BaseCommand>();

    private static ArrayList<BaseCommand> ServerCommands = new ArrayList<BaseCommand>();
    /**
    * Коллекция, содержащая все данные о билетах
    */
    private static LinkedHashSet<Ticket> table;
    /**
    * Дата инициализации коллекции
    */
    private LocalDate date;
    /**
    * Список, содержащий историю о последних 5-ти командах
    */
    private ArrayDeque<String> history;
    /**
    * Путь до файла-коллекции
    */
    private static String PATH=null;

    /**
     * Инициализирует начальные данный для работы с коллекцией
     * <p>
     * <ul>
     *  <li>Инициализирует пустое множество для коллекции</li>
     *  <li>Установка даты инициализации коллекции</li>
     *  <li>Пустой список id</li>
     *  <li>Пустой список цен</li>
     * </ul>
     * @param path путь до пути к файлу-коллекции
    */
    public VectorCollection(String path){
        table = new LinkedHashSet<>();
        new IDGenerator();
        new Price();
        PATH = path;
    }

    public VectorCollection(){
        date = LocalDate.now();
        history = new ArrayDeque<>(5);
    }

    // execute_script command end

    // save command start
    /** 
     * Выполнение команды save.
     * <p>
     * <ul>
     *   <li>Итерация по коллекции</li>
     *   <li>Получение {@link data.ticket.Ticket#toWrite()} вида для билета</li>
     *   <li>Запись в файл-коллекцию</li>
     * </ul>
     */
    public static void Save(){
        try(PrintWriter printer = new PrintWriter(new FileWriter(PATH))){
            for(Ticket t: table){
                printer.println(t.toWrite());        
            }
            System.out.println("\n\u001B[32mCollection was succesfully saved into file\u001B[0m\n");
        }catch(IOException e){
                System.out.println("Ошибка записи в файл"); 
        } 
    }
    // save command end


    // history command start
    /** 
     * Выполнение команды history.
     * <p>
     * <ul>
     *   <li>Итерация по очереди команд в обратном порядке</li>
     *   <li>Вывод последних пяти команд, где под 1 находится наиболее старая</li>
     * </ul>
     */
    public String History(){
        Iterator<String> HistoryIter = history.descendingIterator();
        int num = 1;
        StringBuilder builder = new StringBuilder();
        builder.append("\nHistory of command (from the latest):\n");
        while(HistoryIter.hasNext()){
            builder.append(num++ +")"+HistoryIter.next()+"\n");
        }
        builder.append("\n");
        return builder.toString();
    }
    // history command end



    // print_field_descending_price command start
    /** 
     * Выполнение команды print_field_descending_price.
     * <p>
     * <ul>
     *   <li>Получение множества всех цен</li>
     *   <li>Запись его в структуру {@link java.util.TreeSet}</li>
     *   <li>Итерация в обратном порядке</li>
     *   <li>Вывод цены</li>
     * </ul>
     */
    public static String PrintFieldDescendingPrice(){
        TreeSet<Integer> Prices = new TreeSet<Integer>(Price.keySet());
        Iterator<Integer> PricesIter = Prices.descendingIterator();
        StringBuilder builder = new StringBuilder();
        builder.append("\nPrice by descending:\n");
        int i = 1;
        while(PricesIter.hasNext()){
            int price = PricesIter.next();
            ArrayList<Long> PriceId = Price.get(price);
            if(PriceId!=null&&!PriceId.isEmpty()){
                if(price!=0) builder.append(i++ + ") " + price+"\n");
                if(price==0) builder.append(i + ") " + null+"\n");
            }
        }
        return builder.toString();
    }
    // print_field_descending_price command end



    // filter_by_price command start
    /** 
     * Выполнение команды filter_by_price.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения цены</li>
     *   <li>Получения всех id билетов с данной ценой</li>
     *   <li>Вывод этих билетов, если они есть</li>
     *   <li>Вывод цены</li>
     * </ul>
     * 
     * @param args название команды и строковое значение цены
     * @exception PositiveException Причину возникновения ошибки смотрите в {@link data.validation.validation#realInt(String, int)}
     * @exception StringParseException Причину возникновения ошибки смотрите в {@link data.validation.validation#realInt(String, int)}
     */
    public static String FilterByPrice(int price){
        ArrayList<Long> PriceId = Price.get(price);

        if(PriceId==null||PriceId.isEmpty()){
            return "\033[1;97m\nThere is no ticket with a value equals to the value entered\u001B[0m";
        }else{
            StringBuilder builder = new StringBuilder();
            Iterator<Ticket> tableIter = table.iterator();
            Ticket ticket;
            while(tableIter.hasNext()){
                ticket = tableIter.next();
                if(PriceId.contains(ticket.id())){
                    builder.append("\n"+ticket+"\n");
                }
            }
            builder.append("\n");
            return builder.toString();
        }
    }
    // filter_by_price command end



    // remove_lower command start
    /** 
     * Выполнение команды remove_lower.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения цены</li>
     *   <li>Получения всех id билетов с данной ценой</li>
     *   <li>Удаление этих билетов, если они есть</li>
     *   <li>Создание обьекта</li>
     * </ul>
     * @param args список с названием команды и параметрами для нее
     *
     */
    public static String removeLower(Ticket ticket){
        String name = ticket.name();
        Integer price = ticket.price();

        price = price == null ? 0 : price;
        ArrayList<Long> delete = new ArrayList<>();

        for(int key : Price.keySet()){
            if(key<price){
                for(long id : Price.get(key)){
                    delete.add(id);
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        if(delete.isEmpty()){
              return "\033[1;97m\nThere is no ticket with a value lower than the value entered\u001B[0m";          
        }

        for(long id: delete){     
            builder.append(remove(id));
        }
        builder.append(add(ticket));
        return builder.toString();
    }
    // remove_lower command end



    // remove_any_by_price command start
    /** 
     * Выполнение команды remove_any_by_price.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения цены</li>
     *   <li>Получения всех id билетов с данной ценой</li>
     *   <li>Удаление 1 из этих билетов, если они есть</li>
     *   <li>Вывод цены</li>
     * </ul>
     * 
     * @exception PositiveException Причину возникновения ошибки смотрите в {@link data.validation.validation#realInt(String, int)}
     * @exception StringParseException Причину возникновения ошибки смотрите в {@link data.validation.validation#realInt(String, int)}
     * @param args список с названием команды и ценой для удаления
     */
    public static String removeAnyByPrice(Integer price){
        price = price == null ? 0 : price;
        ArrayList<Long> PriceId;
        if(price!=null){
            PriceId = Price.get(price);
        }else{
            PriceId = Price.get(0);
        }        
        if(PriceId==null||PriceId.isEmpty()){return "\033[1;97m\nThere is no ticket with a value equals to the value entered\u001B[0m\n";}
        else{
            return remove(PriceId.get(0));
        }
    }
    // remove_any_by_price command end 



    // add_if_max command start
    /** 
     * Выполнение команды remove_any_by_price.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения цены</li>
     *   <li>Создание обьекта, если цена больше максимальной в коллекции</li>
     * </ul>
     * @param args список с названием команды и параметрами команды
     */
    public static String addIf(Ticket ticket){
        
        if(ticket.price()>Price.getMaxPrice()){return add(ticket);}
        else{
            return "\033[1;97m\nElement will not be created because it does not have a maximum price value\n\u001B[0m";
        }
    }
    // add_if_max command end



    // remove command start
    /** 
     * Выполнение команды remove.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения id</li>
     *   <li>Создание обьекта-заглушки по id (смотрите: {@link data.ticket.Ticket#toRemove(int)})</li>
     *   <li>Если обьект с данным id есть в коллекции, он удаляется</li>
     * </ul>
     * @param args список с названием команды и id для удаления
     */
    public static String remove(long id){

        Ticket ticket = Ticket.toRemove(id);
        if(table.contains(ticket)){
            deepRemove(ticket, true);
            return "\n\u001B[32mTicket was succesfully deleted by id equals " + id+"\u001B[0m\n";
        }else{
            return "\033[1;97m\nThere is no ticket with that value in the collection\n\u001B[0m";
        }
    }
    // remove command end



    // info command start
    /** 
     * Выполнение команды info.
     * <p>
     * Выведение информации о коллекции
     */
    public String info(){ // тип, дата инициализации, кол-во эл-ов
        return "\n\033[4;37mElement's type:\u001B[0m Ticket" + "\n\033[4;37mNumber of elements:\u001B[0m " + table.size()  + "\n\033[4;37mInitialization date:\u001B[0m " + date + "\n";
    }
    // info command end


    
    // show command start
    /** 
     * Выполнение команды show.
     * <p>
     * Выведение информации обо всех элементах коллекции
     */
    public static String show(){
        if(table.size()==0) return "\nCollection is an empty\n";
        StringBuilder builder = new StringBuilder();
        for(Ticket i: table){
            builder.append("\n"+i+"\n\n");
        }
        return builder.toString();
    }
    // show command end



    // help command start
    /** 
     * Выполнение команды help.
     * <p>
     * Выведение информации обо всех доступных командах и их аргументах
     */
    public static String help(){
        StringBuilder builder = new StringBuilder();
        for(BaseCommand i: Commands){
            builder.append("\n\u001B[47m\u001B[30mCommand:\u001B[0m "+i.getName()+"\n");
            builder.append("  -- \033[4;37m" +  i.getDescription() + "\u001B[0m\n");
        }
        builder.append("\n");
        return builder.toString();
    }


    public static void ServerHelp(){
        for(BaseCommand i: ServerCommands){
            System.out.println("\n\u001B[47m\u001B[30mCommand:\u001B[0m "+i.getName());
            System.out.println("  -- \033[4;37m" +  i.getDescription() + "\u001B[0m");
        }
        System.out.println();
    }
    /** 
     * Добавление команды в список команд.
     * <p>
     * @param command команда, реализованная в программе
     */
    public static void addCommand(BaseCommand command){
        Commands.add(command);
    }
    //help command end

    public static void addServerCommand(BaseCommand command){
        ServerCommands.add(command);
    }

    // clear command start
    /** 
     * Выполнение команды clear.
     * <p>
     * <ul>
     *   <li>Очистка коллекции</li>
     *   <li>Обновление даты инициализации коллекции</li>
     *   <li>Очистка истории</li>
     *   <li>Очистка списков id</li>
     *   <li>Очистка списка цен</li>
     * </ul>
     * 
     */
    public static String clear(){
        new VectorCollection(PATH);
        return "\n\u001B[32mCollection was succesfully cleared\u001B[0m\n";
    }
    // clear command end

    
    // add and update command start
    /** 
     * Выполнение команды add.
     * <p>
     * Генерация обьекта.
     * <p>
     * Для выполнения add передается 1 аргумент - билет.
     */
    public static String add(Ticket ticket){
        long[] id = IDGenerator.currentIdT();
        ticket = new Ticket(id[0], ticket);
        Price.maxPrice(ticket.price());
        Price.put(ticket.price(), ticket.id());
        table.add(ticket);
        if(id[1]==1) VectorCollection.sort();

        return "\n\u001B[Ticket was succesfully added\u001B[0m\n";
        // вернуть, что успешно создано!!!!!!!!!!!!!!!!!!!!!!!
    }


    /** 
     * Добавление обьекта в коллекцию.
     * 
     * @param ticket обьект для добавления в коллекцию
     * @param not_last нужна ли сортировка коллекции
     * @param remove нужно ли удалить обьект с тем же id (для update)
     */
    public static String update(Ticket ticket){
        deepRemove(ticket, false);
        Price.maxPrice(ticket.price());
        Price.put(ticket.price(), ticket.id());
        table.add(ticket);
        VectorCollection.sort();
        return "\n\u001B[Ticket was succesfully added\u001B[0m\n";
    }
    // add and update command end

    public static void addToCollection(Ticket ticket){
        table.add(ticket);
    }


    // exit command start
    /** 
     * Выполнение команды exit.
     * <p>
     * Завершение работы программы без сохранения коллеции в файл
     */
    public static void exit(){
        System.out.println("\n\033[0;34mTerminating programm...\033[0m");
        System.exit(0);
    }
    // exit command end


    // starts some non-command functions
    /** 
     * Возвращает размер истории комманд.
     * @return размер истории команд     
    */
    public int HistorySize(){
        return history.size();
    }

    /** 
     * Удаление самой старой команды из истории.
     */
    public void removeLastHistory(){
        history.removeLast();
    }

    /** 
     * Добавление новой комманды.
     * @param command имя команды     
    */
    public void pushHistory(String command){
        history.push(command);
    }
    /** 
     * Сортировка коллекции.
     */
    public static void sort(){
        table = new LinkedHashSet<Ticket>(new TreeSet<Ticket>(table));
    }
    /** 
     * Возвращает дату инициализации коллекции.
     * @return дата инициализаии коллекции
     */
    public LocalDate getDate(){
        return date;
    }

    /** 
     * Удаление данных для обьектов коллекции
     * <p>
     * <ul>
     *   <li>При команде update несуществующего обьекта, добавляем его id</li>
     *   <li>Удаляем старую цену</li>
     *   <li>Если venue был не null, то удаляем его id</li>
     *   <li>При команде remove удаляем id обьекта</li>
     * @param ticket обьект для полного или частичного удаления значений
     * @param isTicketIdRemove удалять ли id обьекта
     */
    private static void deepRemove(Ticket ticket, boolean isTicketIdRemove){
        //System.out.println(IDGenerator.toAr());
        //System.out.println(IDGenerator.toArT()+"\n");
        //for(int i: Price.keySet()){
        //    System.out.println(i+ " " +Price.get(i));
        //}

        if(!table.remove(ticket)){IDGenerator.addIdT(ticket.id());}
        else if(Ticket.lastPrice()!=null){Price.removeIdPrice(Ticket.lastPrice(), ticket.id());}
        else{Price.removeIdPrice(0, ticket.id());}

        //for(int i: Price.keySet()){
        //    System.out.println(i+ " "+Price.get(i));
        //}


        if(Ticket.lastComparableVenueId()!=0){
            IDGenerator.removeIdV(Ticket.lastComparableVenueId());
            
        }
        
        if(isTicketIdRemove){
            IDGenerator.removeIdT(ticket.id());
                  
        }

        //System.out.println(IDGenerator.toAr());
        //System.out.println(IDGenerator.toArT());

    }
    

}
