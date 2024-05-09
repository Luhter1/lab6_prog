package data.ticket;

import java.util.Date;
import data.coordinates.Coordinates;
import data.venue.Venue;
import  java.lang.Comparable;
import java.text.SimpleDateFormat;
import java.io.Serializable;
/** 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public record Ticket (Long id, String name, Coordinates coordinates, Date creationDate, Integer price, TicketType type, Venue venue) implements Comparable<Ticket>, Serializable{
    /** 
     * Формат, в котором представленны даты при считывании и записи в файл-коллекцию                     
     */
    public static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Последнее id, которое сравнивалось, то есть того обьекта, который подошел под сравнение
    */
    static long comparable_venue_id=0;

    /**
     * Последняя цена, которая была записана при сравнении сравнивалось, то есть цена, обьекта, который подошел под сравнение
    */
    static Integer lastPrice=null;

    /**
     * Возвращает id заведения для обьекта, который подошел под сравнение, используется для удаления id заведения из списка id для них
     * @return id заведения, для удаляемого билета
    */
    public static long lastComparableVenueId(){
        return comparable_venue_id;
    }

    /**
     * Возвращает цену за билет для обьекта, который подошел под сравнение, используется для удаления id заведения из словаря цен
     * @return цену заведения, для удаляемого билета
    */
    public static Integer lastPrice(){
        return lastPrice;
    }
    
    /**
     * Создает обьект-заглушку данного типа, который используется для удаления
    */
    private Ticket(long id){
        this(id, null, null, null, null, null, null); 
    }

    public Ticket(long id, Ticket ticket){
        this(id, ticket.name(), ticket.coordinates(), ticket.creationDate(), ticket.price(), ticket.type(), (ticket.venue()!=null) ? new Venue(ticket.venue()) : null);
    }

    /**
     * Создает обьект-заглушку типа данного класса, содержащий только id.
     * <p>
     * Используется для поиска обьекта с таким же id в коллекции и его удаления
     * @param id начение id для обьекта-заглушки
     * @return обьек-заглушка типа данного класса
    */
    public static Ticket toRemove(long id){
        return new Ticket(id);
    }

    /** 
     * Возвращает строковое представление обьекта данного класса, для его записи в файл-коллекцию.
     * @return строковое представление обьекта для записи в файл-коллекцию
     *
     * @see receiver.VectorCollection#Save()                       
     */
    public String toWrite(){
        String write = id + ";" + name + ";" + coordinates.toWrite() + ";" + formater.format(creationDate) + ";" + price + ";" + type;

        if(venue!=null){
            return write+";"+venue.toWrite();
        }else{
            return write+";null;;;;";
        }

    }

    /** 
     * Реализует сравнение обьектов данного класса по их id.
     * @param ticket обьект для сравнения
     * @return позитивное - значение обьекта больше сравниваемого, 0 - значения обьектов равны, негативное - значение обьекта меньше сравниваемого
     *                     
     */
    @Override
    public int compareTo(Ticket ticket) {
       return (int)(this.id-ticket.id());
    }

    @Override
    public String toString(){
        String out = "\u001B[47m\u001B[30mid: " + id + "\u001B[0m\nname: " + name + "\n" + coordinates + "\ncreation_date: " + formater.format(creationDate) + "\nprice: " + price + "\ntype: " + type;
        if(venue!=null){
            return out+"\n"+venue.toString();
        }else{
            return out+"\nvenue: "+null;
        }
    }

    @Override
    public int hashCode(){
        return (int)(long)id();
    }

    @Override    
    public boolean equals(Object o){
        if(this==o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket t = (Ticket)o;
        if (t.venue()!=null) comparable_venue_id = t.venue().id();
        if (t.venue()==null) comparable_venue_id = 0;
        lastPrice = t.price();
        return o.hashCode()==this.hashCode();
    }
}
