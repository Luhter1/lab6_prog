package data.id;

import java.util.ArrayList;
import java.util.Collections;

/** 
 * Класс, реализующий работу c id.
 * <p>
 * Содержит два списка id для обьектов типа {@link data.ticket.Ticket} и {@link data.venue.Venue}, а также текущие значения id для каждого из них.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class IDGenerator{
    /**
     * Список, содержащий значения id для билетов
    */
    private static ArrayList<Long> idT;
    /**
     * Список, содержащий значения id для заведений
    */
    private static ArrayList<Long> idV;
    /**
     * Текущее значение id для билетов
    */
    private static long currentTID = 1;
    /**
     * Текущее значение id для заведений
    */
    private static long currentVID = 1;

    /**
     * Инициализирует пустые списки для id и сбрасывает значения текущего id до 1 для обоих типов обьектов.
    */
    public IDGenerator(){
    
    idT = new ArrayList<>();
    idV = new ArrayList<>();
    currentTID = 1;
    currentVID = 1;

    }

    /**
     * Возвращает текущий список id для заведений.
     *
     * @return список id для заведений
    */
    public static ArrayList<Long> toAr(){
        return idV;
    }

    /**
     * Возвращает текущий список id для билетов.
     *
     * @return списко id для билетов
    */
    public static ArrayList<Long> toArT(){
        return idT;
    }
    
    /**
     * Нужен для однотипного добавления значений в списки id через одну функцию.
    */
    interface ADD{
        void add(long i);
    }
    

    /**
     * Добавление id в списки id для билетов.
     * @param i id для добавления в список id билетов
    */
    public static void addIdT(long i){idT.add(i);}

    /**
     * Добавление id в списки id для заведений.
     * @param i id для добавления в список id заведения
    */
    public static void addIdV(long i){idV.add(i);}


    /**
     * Удаляет id из списка id для билетов.
     * @param i id для удаления из списка id билетов
     * @return возвращает true, если обьект был найдет и удален, иначе false
    */
    public static boolean removeIdT(long i){return idT.remove(i);}

    /**
     * Удаляет id из списка id для завдений.
     * @param i id для удаления из списка id заведений
     * @return возвращает true, если обьект был найдет и удален, иначе false
    */
    public static boolean removeIdV(long i){return idV.remove(i);}

    /**
     * Возвращает значение id для следующего создаваемого билета.
     * <p>
     * Если коллекция не пустая, и максимальное значение id, деленное на 2, больше ее размера,
     * то следующее значение генерируется в промежутке от 1 до максимального значения.
     *
     * @return под индексом 0 возвращает значение id
     *         под индексом 1 указывает нужно ли отсортировать коллекцию
    */
    public static long[] currentIdT(){

        if(currentTID==1 && !idT.isEmpty()){
            currentTID = maxE(idT);
        }

        if(idT.size()>1&&currentTID/2>=idT.size()){
            long[] isSort =  {randId(idT, currentTID, IDGenerator::addIdT), 1};
            return isSort;
        }

        addIdT(currentTID);
        long[] isSort = {currentTID++, 0};
        return isSort;

    }

    /**
     * Возвращает значение id для следующего создаваемого заведения.
     * <p>
     * Если список id для заведений не пуст, и максимальное значение id оттуда, деленное на 2, больше размера его размера,
     * то следующее значение генерируется в промежутке от 1 до максимального значения.
     *
     * @return под индексом 0 возвращает значение id
     *         под индексом 1 указывает нужно ли отсортировать коллекцию
    */
    public static long currentIdV(){      
        if(currentVID==1 && !idV.isEmpty()){
            currentVID = maxE(idV);
        }

        if(idV.size()>1&&currentVID/2>=idV.size()){
            return randId(idV, currentVID, IDGenerator::addIdV);
        }
        addIdV(currentVID);
        return currentVID++;

    }

    /**
     * Проверяет id на уникальность
     *
     * @param list в каком из списков id проверять
     * @param id значение id
     *
     * @return уникально ли переданное значение
    */
    public static boolean unique(String list, long id){

        switch(list){
            
            case "ticket": return !idT.contains(id);

            case "venue": return !idV.contains(id);

        }

        return false;
    
    }

    /**
     * Возвращает максимальное значение id из передаваемого списка
     *
     * @param id список, для которого ищется максимум
     *
     * @return максимальное значение в списке
    */
    private static long maxE(ArrayList<Long> id){

            Collections.sort(id, Collections.reverseOrder());
            long maxE = id.get(0);
            return ++maxE;

    }

    /**
     * Возвращает уникальное значение в промежутке  от 1 до currentId
     *
     * @param idA список id для проверки уникальности
     * @param currentId текущее макимально значение id
     * @param add интерфейс, реализующий добавление значения в один из списков id
     *
     * @return уникальное значение id от 1 до currentId
     */
    private static long randId(ArrayList<Long> idA, long currentId, ADD add){
            long id;

            do{
                id = (long) (Math.floor(Math.random() * currentId));
            }while(id==0||idA.contains(id));

            add.add(id);

            return id;
    }

    
    




}
