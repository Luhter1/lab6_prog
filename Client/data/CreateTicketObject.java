package data;

import java.util.Date;
import java.util.Scanner;

import data.coordinates.Coordinates;
import data.validation.validation;
import java.text.ParseException; // Data
import data.validation.exception.*;
import data.ticket.*;
import data.venue.*;
import java.io.BufferedReader;
import java.io.IOException;
import client.Client;
import java.util.*;

/** 
 * Класс, отвечающий за создание обьектов типа {@link data.ticket.Ticket}.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class CreateTicketObject{
    private static ArrayDeque<String> script = Client.getScript();
     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     * @param SPrice стоимость билета, вводимая пользователем вместе с командой
     *          
     * @return возвращает стоимость билета       
     */
    public static Integer priceGenerate(String SPrice){
        BufferedReader scan = Client.getScan();
        Integer price;
        try{
            if(SPrice==null) throw new NullValueException("price");
            price = validation.realInt(SPrice, 1);
        }catch(Exception e){

            System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);

            while(true){
                try{
                    System.out.print("\nInput ticket price (int or null): ");
                    price = validation.realInt(scan.readLine(), 1);  
                    break;

                } catch(Exception ex){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+ex);}
            }
        }
        return price;
    }

     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     * @param SId значение id, вводимое пользователем вместе с командой update
     *          
     * @return возвращает значение id билета        
     */
    public static long idGenerate(String SId){
        BufferedReader scan = Client.getScan();
        long Id;
        try{
                Id = validation.ID(SId);

            }catch(Exception e){

                System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);

                while(true){
                    try{
                        System.out.print("\nInput id (long): ");
                        Id = validation.ID(scan.readLine());  
                        break;

                    } catch(Exception ex){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+ex);}

                }
            }
        return Id;

    }

     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     * @param name имя билета, вводимое пользователем вместе с командой
     *          
     * @return возвращает тип билета         
     */
    public static String nameGenerate(String name){
        BufferedReader scan = Client.getScan();
        try{
            name = validation.name("ticket", name);
        }catch(Exception e){

            System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);

            while(true){
                try{
                    System.out.print("\nInput ticket name (String): ");
                    name = validation.name("ticket", scan.readLine());  
                    break;

                } catch(Exception ex){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+ex);}

            }
        }

        return name;
    }

     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     *          
     * @return возвращает координаты места         
     */
    public static Coordinates coordinateGenerate(boolean isScript){
        BufferedReader scan = Client.getScan();
        String x=null;
        String y=null;
        Coordinates coord;
        boolean first_script = false;
        if(isScript){
            x = script.pop();
            y = script.pop();
            System.out.print("\nInputed x: " + x);
            System.out.println("\nInputed y: " + y);
            first_script = true;
        }
        while(true){
            try{
                if(!first_script){
                System.out.print("\nInput x (double): ");
                x = scan.readLine(); 

                System.out.print("Input y (double): ");
                y = scan.readLine();   
                }else{
                    first_script = false;        
                }
                coord = validation.coordinates(x, y);
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: \u001B[0m"+e);}
        }

        return coord;
    }


     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     *          
     * @return возвращает тип билета         
     */
    public static TicketType typeGenerate(boolean isScript){
        BufferedReader scan = Client.getScan();
        TicketType type;
        boolean first_script=false;
        String Stype=null;

        if(isScript){
            Stype = script.pop();
            System.out.println("\nInputed Ticket type: " + Stype);
            first_script = true;
        }

        while(true){
            try{
                if(!first_script){
                    System.out.print("\nSelect ticket type from:\n-- \"vip\"\n-- \"usual\"\n-- \"budgetary\"\n-- \"cheap\"\nInput: ");
                    Stype = scan.readLine();
                }else{
                    first_script = false;
                }
                type = validation.type(TicketType.class, Stype.toUpperCase());                 
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);}

        }
    
        return type;
    }

    /* нужна обработка
    ++ name
    ++ price
    ++ Coordinates(x,y)
    ++ type
    */
    /** 
     * Создает обьект типа {@link data.ticket.Ticket} из значений, вводимых пользователем.
     * <p>
     * Впоследствии записывает его в коллекцию с помощью {@link receiver.VectorCollection#addToCollection(Ticket, long, boolean)}.
     * <p>
     * Для каждого поля этого обьекта реализована валидация значений, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести некорректное значение заново.
     * <p>
     * Для каждого обьекта реализовано: генерация id обьекта с помощью генератора id, 
     * если, он не передается вручную, также в словарь цен по ключу равному цене за билет записывается id обьекта и идет проверка цены на максимальность.
     *
     * @param SId нужен для задания id пользователем вручную, в противном случае содержит значение "null"
     * @param args значения, получаемые при вызове пользователем команды,
     *             содержат название команды, имя обьекта и его стоимость
     *          
     * @see data.id.IDGenerator              
     */
    public static Ticket generate(String command, String[] args, boolean isScript){
        Coordinates coord;
        TicketType type;
        Venue venue;
        String name;
        Integer price;
        long id = 0;

        BufferedReader scan = Client.getScan();

        if(command.equals("update")){
            args = Arrays.copyOfRange(args, 1, 4);
            id = idGenerate(args[0]);
        }

        // name generation
        name = nameGenerate(args[1]);        

        // price generation
        price = priceGenerate(args[2]);
    
        // Coordinates generation
        coord = coordinateGenerate(isScript);

        // Date generation
        Date date = new Date();
        
        // Ticket Type generation
        type = typeGenerate(isScript);       

        // Is user want to add venue
        String ans=null;
        boolean first_script=false;
        if(isScript){
            ans = script.pop();
            first_script = true;
        }
        while(true){

            System.out.print("\nCreate venue (yes or no): ");
            try{
                if(!first_script){
                    ans = scan.readLine().toLowerCase();
                }else{
                    first_script = false;
                    System.out.println(ans);            
                }
                if(ans.equals("no")){venue = null; break;}
                else if(ans.equals("yes")){venue = CreateTicketObject.venueGenerate(isScript); break;}
            }catch(IOException e){
                System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m incorrect velue");
            }          
        }
        return new Ticket(id, name, coord, date, price, type, venue);
    }


    /* нужна обработка  и id генератор
    ++ name
    ++ capacity
    ++ type
    */
    /** 
     * Создает обьект типа {@link data.venue.Venue} из значений, вводимых пользователем.
     * Вызывается, если пользователь укажет создать обьект данного типа, как одно из полей, во время создания обьекта типа {@link data.ticket.Ticket}.
     * <p>
     * Для каждого поля этого обьекта реализована валидация значений, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести некорректное значение заново.
     * <p>
     * Для каждого обьекта реализовано: генерация id обьекта с помощью генератора id для обьектов данного типа. 
     * @return возвращает заведение для билета
     * @see data.id.IDGenerator                        
     */
    public static Venue venueGenerate(boolean isScript){
        long id = 0;
        String name=null;
        Integer capacity;
        VenueType type = null;
        boolean first_script = false;
        BufferedReader scan = Client.getScan();

        // venue name generation
        if(isScript){
            first_script = true;
            name = script.pop();
            System.out.println("\nInputed venue name: "+ name);    
        }
        while(true){
            try{
                if(!first_script){
                    System.out.print("\nInput venue name (String): ");
                    name = scan.readLine();
                }else{
                    first_script=false;
                }
                name = validation.name("venue", name);                
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);}

        }

        // venue capacity generation
        String Scap=null;
        if(isScript){
            first_script = true;
            Scap = script.pop();
            System.out.println("\nInputed venue capacity: "+ Scap);    
        }
        while(true){
            try{
                if(!first_script){
                    System.out.print("\nInput venue capacity (int or null): ");
                    Scap = scan.readLine();
                }else{first_script=false;}
                capacity = validation.realInt(Scap, 0); //Передать на валидацию,                 
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);}

        }


        // venue type generation
        String line=null;
        if(isScript){
            first_script = true;
            line = script.pop();
            System.out.println("\nInputed venue type: "+ line);    
        }
        while(true){
            try{
                if(!first_script){
                System.out.print("\nSelect venue type from:\n-- \"pub\"\n-- \"bar\"\n-- \"mall\"\n-- null\nInput: ");
                line = scan.readLine();
                }else{first_script=false;}

                if(!line.equals("null")){
                    type = validation.type(VenueType.class, line.toUpperCase());
                }                 
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);}

        }

        return new Venue(id, name, capacity, type);
    }

}
