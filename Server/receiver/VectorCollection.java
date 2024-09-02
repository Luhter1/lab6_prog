package receiver;

import java.time.LocalDate;
import data.ticket.Ticket;
import java.util.*;
import command.BaseCommand;
import java.io.*;
import java.lang.StringBuilder;
import server.Server;
import java.util.logging.*;
import database.SQLManager;
import server.connect.Package;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

// Receiver

/**
* Класс, который выполняет фактическую операцию во время выполнении команды
* 
* @author   Timofei Kaparulin
* @version  1.0
*/
public class VectorCollection{
    private static String name = VectorCollection.class.getName();
    private static ArrayList<BaseCommand> Commands = new ArrayList<BaseCommand>(); // list of all available commands
    private static ArrayList<BaseCommand> ServerCommands = new ArrayList<BaseCommand>(); // list of all available server commands
    private static ConcurrentHashMap<Ticket, Boolean> table; // collections of Tickets
    private LocalDate date; // inizialization date
    private static Logger log;

    /* setting up inizialization date for each user */
    public VectorCollection(){
        date = LocalDate.now();
    }

    /* initial data after server starting */
    public static void initVectorCollection(Logger log){
        table = new ConcurrentHashMap<>();
        VectorCollection.log = log;
    }

    public static void getCollectionFromDatabase() throws IOException, NullPointerException, SQLException{
        table = new ConcurrentHashMap<>();
        for (Ticket ticket : SQLManager.getCollectionFromDatabase()) {
            table.put(ticket, true);
        }
    }


    // print_field_descending_price command start
    public static String PrintFieldDescendingPrice() throws IOException, SQLException{
        TreeSet<Integer> Prices = new TreeSet<Integer>(SQLManager.PriceList());
        Iterator<Integer> PricesIter = Prices.descendingIterator();
        StringBuilder builder = new StringBuilder();
        builder.append("\nPrice by descending:\n");
        int i = 1;
        while(PricesIter.hasNext()){
            int price = PricesIter.next();
            builder.append(i++ + ") " + price+"\n");
        }
        return builder.toString();
    }
    // print_field_descending_price command end



    // filter_by_price command start
    public static String FilterByPrice(Integer price) throws IOException, SQLException{
        ArrayList<Long> PriceId = SQLManager.getTicketIdByPrice(price);

        if(PriceId.isEmpty()){
            return "\033[1;97m\nThere is no ticket with a value equals to the value entered\u001B[0m";
        }else{
            StringBuilder builder = new StringBuilder();
            
            table.keySet().stream().filter( x -> PriceId.contains(x.id()) ).forEach(x -> builder.append("\n"+x+"\n"));

            return builder.toString();
        }
    }
    // filter_by_price command end



    // remove_lower command start
    public static String removeLower(Ticket ticket, String login) throws IOException, SQLException{
        Integer price = ticket.price();

        ArrayList<Long> delete = SQLManager.LowerPrice(price);


        StringBuilder builder = new StringBuilder();
        if(delete.isEmpty()){
              return "\033[1;97m\nThere is no ticket with a value lower than the value entered\u001B[0m";          
        }

        for(long id: delete){     
            builder.append(remove(id, login));
        }
        builder.append(add(ticket, login));
        return builder.toString();
    }
    // remove_lower command end



    // remove_any_by_price command start
    public static String removeAnyByPrice(Integer price, String login) throws IOException, SQLException{
        ArrayList<Long> PriceId = SQLManager.getTicketIdByPrice(price);
    
        if(PriceId.isEmpty()){return "\033[1;97m\nThere is no ticket with a value equals to the value entered\u001B[0m\n";}
        else{
            return remove(PriceId.get(0), login);
        }
    }
    // remove_any_by_price command end 



    // add_if_max command start
    public static String addIf(Ticket ticket, String login) throws IOException, SQLException{
        
        if(ticket.price()!=null && ticket.price()>SQLManager.MaxPrice()){
        	return add(ticket, login);
    	}else{
            return "\033[1;97m\nElement will not be created because it does not have a maximum price value\n\u001B[0m";
        }
    }
    // add_if_max command end


    // remove command start
    public static String remove(long id, String login) throws IOException, SQLException{
        if(!SQLManager.isUsered(id, login)) return "\nYou cannot modify other user's object";
        
		SQLManager.removeTicket(id);
        Ticket ticket = Ticket.toRemove(id);
        if(table.containsKey(ticket)){
            deepRemove(ticket);
            return "\n\u001B[32mTicket was succesfully deleted by id equals " + id+"\u001B[0m\n";
        }else{
            return "\033[1;97m\nThere is no ticket with that value in the collection\n\u001B[0m";
        }
    }
    // remove command end



    // info command start
    public String info(){ // тип, дата инициализации, кол-во эл-ов
        return "\n\033[4;37mElement's type:\u001B[0m Ticket" + "\n\033[4;37mNumber of elements:\u001B[0m " + table.size()  + "\n\033[4;37mInitialization date:\u001B[0m " + date + "\n";
    }
    // info command end


    
    // show command start
    public static String show(){
        if(table.size()==0) return "\nCollection is an empty\n";
        StringBuilder builder = new StringBuilder();
		sort();
		
        table.keySet().stream().forEach(x-> builder.append("\n"+x+"\n"));

        return builder.toString();
    }
    // show command end



    // help command start
    public static String help(){
        StringBuilder builder = new StringBuilder();
        for(BaseCommand i: Commands){
            builder.append("\n\u001B[47m\u001B[30mCommand:\u001B[0m "+i.getName()+"\n");
            builder.append("  -- \033[4;37m" +  i.getDescription() + "\u001B[0m\n");
        }
        return builder.toString();
    }


    public static void ServerHelp(){
        for(BaseCommand i: ServerCommands){
            System.out.println("\n\u001B[47m\u001B[30mCommand:\u001B[0m "+i.getName());
            System.out.println("  -- \033[4;37m" +  i.getDescription() + "\u001B[0m");
        }
        System.out.println();
    }
    // help command end


    // clear command start
    public static String clear(String login) throws IOException, SQLException{
    	SQLManager.DropDB(login);
        getCollectionFromDatabase();
        return "\n\u001B[32mCollection was succesfully cleared\u001B[0m\n";
    }
    // clear command end

    
    // add command start
    public static String add(Ticket ticket, String login) throws IOException, SQLException{
        Long[] id = SQLManager.addTicket(ticket, login);
        ticket = new Ticket(id, ticket);
        table.put(ticket, true);


        return "\n\u001B[32mTicket was succesfully added\u001B[0m\n";
    }
    // add command end


    // update command start
    public static String update(Ticket ticket, String login) throws IOException, SQLException{
        Long[] id;
        if(!SQLManager.isUsered(ticket.id(), login)) return "\nYou cannot modify other user's object";

        // если есть в коллекции, то удалить
        if(table.contains(ticket)){
            deepRemove(ticket);
        }
        
        
        SQLManager.removeTicket(ticket.id());
        id = SQLManager.updateTicket(ticket, login);
        ticket = new Ticket(id, ticket);
        table.put(ticket, true);
        
        return "\n\u001B[32mTicket was succesfully updated\u001B[0m\n";
    }
    // update command end


    // exit command start
    public static void exit(){
        System.out.println("\n\033[0;34mTerminating server...\033[0m");
        log.logp(Level.FINE, name, "exit", "выключение сервера"); 
        //try{Server.getServer().close();}catch(Exception e){System.out.println(e);}
        System.exit(0);
    }
    // exit command end






    // starts some non-command functions
    
    public static void addCommand(BaseCommand command){
        Commands.add(command);
    }

    public static void addServerCommand(BaseCommand command){
        ServerCommands.add(command);
    }
     
    /* Сортировка коллекции */
    public static void sort(){
        ConcurrentHashMap <Ticket, Boolean> t = new ConcurrentHashMap <Ticket, Boolean>();
        for (Ticket ticket : new LinkedHashSet<Ticket>(new TreeSet<Ticket>(table.keySet()))) {
            t.put(ticket, true);
        }
        table = t;
    }

     
    /* Возвращает дату инициализации коллекции */
    public LocalDate getDate(){
        return date;
    }

     
    /* Удаление данных об обьектах из коллекции */
    private static boolean deepRemove(Ticket ticket){
		return table.remove(ticket);
    }
    
    public static Set<String> getUsers() throws SQLException{
        return SQLManager.getUsers();
    }
    
    
    public static void register(Package pack) throws SQLException{
        String [] arg = (String[])pack.arg();
        SQLManager.register(arg[0], arg[1], arg[2]);
    }
    
    public static boolean login(Package pack) throws SQLException{
        String [] arg = (String[])pack.arg();
        return SQLManager.login(arg[0], arg[1]);
        
    }


}
