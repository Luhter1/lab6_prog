package data;

import java.util.Date;
import receiver.VectorCollection;
import data.id.IDGenerator;
import data.coordinates.Coordinates;
import data.validation.validation;
import java.text.ParseException; // Data
import data.validation.exception.*;
import data.ticket.*;
import data.venue.*;
import data.price.Price;
import data.ticket.Ticket;

/** 
 * Класс, отвечающий за создание обьектов типа {@link data.ticket.Ticket}.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class CreateTicketObject{

    /* обработка нужна, так как значения считываются из файла, можно изменить файл
    ++ id (unique, not null, >0)
    ++ name (not null, not empty)
    ++ coord (not null, parse error) - так как состоит из 2 координат, не нужно проверять их, просто парсить
    ++ date (not null)
    ++ price (might be null, >0)
    ++ type (not null)
    ++ venue_id (>0, unique)
    ++ venue_name (not null, not empty)
    ++ venue_capacity (might be null, >0)
    ++ venue_type (might be null)
    */

    /** 
     * Создает обьект типа {@link data.ticket.Ticket} из значений, получаемых при чтении файла-коллекции.
     * <p>
     * Впоследствии записывает его в коллекцию с помощью {@link receiver.VectorCollection#addToCollection(Ticket, long, boolean)}.
     * <p>
     * Для каждого поля этого обьекта реализована валидация значений, по условиям варианта, если в ходе нее возникает ошибка, будет выведена строка в файле-коллекции, где она произошла.
     * <p>
     * Для каждого обьекта реализовано: добавление его id в генератор id, а также в словарь цен по ключу равному цене за билет, проверка цены обьекта на максимальность.
     * <p>
     * При значении venue не равном null, его id записывается в генератор id для venue
     *
     * @param line строка в файле-коллекции, на которой находятся получаемые данные
     * @param args значения содержащиеся на строке line в файле-коллекции
     *
     * @see data.id.IDGenerator                       
     */
    public static void create(int line, String[] args){

        Long id;
        String name;
        Coordinates coord;
        Date date;
        Integer price = null;
        TicketType type;
        Venue venue = null;

        try{
            // id validation
            id =  validation.ID("ticket", args[0], true);

            IDGenerator.addIdT(id);
            
            // name validation
            name = validation.name("ticket", args[1]);

            // coordinate validation
            coord = validation.coordinates(args[2], args[3]);

            // date validation
            try{
                date = Ticket.formater.parse(args[4]);
            }catch(ParseException e){throw new StringParseException("Date with format yyyy-MM-dd", args[4]);}
            
            // price validation
            price = validation.realInt(args[5], 1);
            if(price!=null) {
                Price.put(price, id);
                Price.maxPrice(price);
            }else{Price.put(0, id);}
            // type validation
            type = validation.type(TicketType.class, args[6]);
            
            if(!args[7].equals("null")){
                long venue_id;
                String venue_name;
                Integer venue_capacity;
                VenueType venue_type = null;

                // venue id validation
                venue_id = validation.ID("venue", args[7], true); // venue_id

                IDGenerator.addIdV(venue_id);

                // venue name validation
                venue_name = validation.name("venue", args[8]);
                
                // venue capacity validation
                venue_capacity = validation.realInt(args[9], 0);
                if(!args[10].equals("null")){venue_type = validation.type(VenueType.class, args[10]);}

                venue = new Venue(venue_id, venue_name, venue_capacity, venue_type);
            }
            VectorCollection.addToCollection(new Ticket(id, name, coord, date, price, type, venue));

        }catch(Exception e){System.out.println("Data reading error on line "+ line+ ":\n    "+e); System.exit(0);}

    }

}
