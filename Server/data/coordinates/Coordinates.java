package data.coordinates;

import java.io.Serializable;

/** 
 * Класс, задающий структуру для значений данного типа.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class Coordinates implements Serializable{
    private double x;
    private Double y; //Поле не может быть null

    /** 
     * Создает экземпляр класса Coordinates.
     *
     * @param x числовое значение x
     * @param y числовое значение y                       
     */
    public Coordinates(double x, Double y){
        this.x = x;
        this.y = y;
    }

    /** 
     * Создает экземпляр класса Coordinates.
     * <p>
     * Преобразует строковое значение к требуемым типам.
     *
     * @param x строковое значение x
     * @param y строковое значение y                       
     */
    public Coordinates(String x, String y){
        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
    }

    /** 
     * Возвращает строковое представление обьекта данного класса, для его записи в файл-коллекцию.
     * @return строковое представление обьекта для записи в файл-коллекцию
     *
     * @see receiver.VectorCollection#Save()                       
     */
    public String toWrite(){
        return x+";"+y;
    }

    @Override
    public String toString(){
        return "x: " + x + "\ny: " + y;
    }
}
