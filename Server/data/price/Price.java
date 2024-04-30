package data.price;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/** 
 * Класс, задающий структуру для значений данного типа.
 * <p>
 * Также содержит словарь, где ключем выступает значение цены, а значением - список id билетов с этой ценой и максимальное значение цены среди всех обьектов типа {@link data.ticket.Ticket}.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class Price{

    /**
     * Словарь, где ключем выступает значение цены, а значением - список id билетов с этой ценой.
    */
    private static LinkedHashMap<Integer, ArrayList<Long>> pricelist;
    
    /**
     * Максимальное значение цены среди всех билетов.
    */
    private static int MaxPrice;

    /**
     * Возвращает максимальную цену среди всех обьектов типа {@link data.ticket.Ticket}.
     *
     * @return максимальная цена среди билетов
    */
    public static int getMaxPrice(){
        return MaxPrice;
    }

    /**
     * Сравнивает переданную цену с максимальной, и записывает большую в максимум.
     *
     * @param i цена для сравнения
    */
    public static void maxPrice(int i){
        MaxPrice = Math.max(MaxPrice, i);
    }

    /**
     * Инициализирует пустой словарь цен.
    */
    public Price(){
        pricelist = new LinkedHashMap<>();
        
    }

    /**
     * Возвращает список id билетов, у которых указана данная цена.
     *
     * @param key значение цены за билет
     * @return список id билетов с указанной ценой
    */
    public static ArrayList<Long> get(int key){
        return pricelist.get(key);
    }

    /**
     * Добавляет в словарь цен, переданное id, по ключу равному цене за билет.
     *
     * @param key значение цены за билет
     * @param id id билета
    */
    public static void put(int key, long id){
        if(pricelist.get(key)==null){
            pricelist.put(key, new ArrayList<Long>());
            
        }
        pricelist.get(key).add(id);
        
    }

    /**
     * Удаляет из в словаря цен, переданное id, по ключу равному цене за билет.
     *
     * @param key значение цены за билет
     * @param id id билета
    */
    public static void removeIdPrice(int key, long id){
        pricelist.get(key).remove(id);
    }

    /**
     * Возвращает множестов всех цен за билеты.
     *
     * @return множество, содержащее все цены за билеты
    */
    public static Set<Integer> keySet(){
        return pricelist.keySet();
    }

}
