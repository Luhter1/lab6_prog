package data.validation;
 
import java.lang.Exception;
import data.validation.exception.*;
import data.id.IDGenerator;
import data.coordinates.Coordinates;
import data.ticket.*;
import java.lang.IllegalArgumentException;
import java.lang.Enum;

/** 
 * Класс, использующийся при валидации создаваемых обьектов.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class validation{

    /** 
     * Используется для валидации id обоих типов.
     *
     * @param L тип обькта
     * @param Sid строковое представление id
     * @param uniq нужно ли проверять на уникальность 
     * @exception UniqueException срабатывает, если указана проверка на уникальность и значение не уникально 
     * @exception PositiveException срабатывает, если значение id не положительно
     * @exception StringParseException срабатывает, если строковое значение id не приводится к типу long  
     * @return возвращает значение id типа long             
     */
    public static long ID(String L, String Sid, boolean uniq) throws UniqueException,PositiveException, StringParseException{
    
        long id;

        try{
            id = Long.parseLong(Sid);
        }catch(Exception e){throw new StringParseException("long", Sid);}

        if(id<=0) throw new PositiveException(L+" id");

        if(uniq&&!IDGenerator.unique(L, id)) throw new UniqueException(L+" id");

        return id;

    }

    /** 
     * Используется для валидации имени.
     *
     * @param L тип обькта
     * @param name строковое представление имени
     *
     * @exception NullValueException срабатывает, если имя равно "null"
     * @exception EmptyStringException срабатывает, если имя равно ""               
     * @return возвращает значение name типа String
     */
    public static String name(String L, String name) throws NullValueException, EmptyStringException{

        if(name.equals("null")) throw new NullValueException(L+" name");

        if(name.equals("")) throw new EmptyStringException(L+" name"); //проверки id, пробуем парсить в число, проверка >0, проверка на уникальность
        
        return name;

    }

    /** 
     * Используется для валидации координат.
     *
     * @param Sx строковое представление х
     * @param Sy строковое представление y
     *
     * @exception StringParseException срабатывает, если одна из координат не приводится к типу double               
     * @return возвращает значение коодинат типа {@link data.coordinates.Coordinates}
     */
    public static Coordinates coordinates(String Sx, String Sy) throws StringParseException{

        Coordinates coord;
        double x,y;

        try{
            x = Double.parseDouble(Sx);
        }catch(Exception e){throw new StringParseException("double", Sx);}

        try{
            y = Double.parseDouble(Sy);
        }catch(Exception e){throw new StringParseException("double", Sy);}

        return new Coordinates(x,y);
    }

    /** 
     * Используется для валидации числовых значения.
     *
     * @param Svalue строковое представление числового значения
     * @param i что за тип поля валидируется
     *
     * @exception PositiveException срабатывает, если числовое значение оказалось не положительным
     * @exception StringParseException срабатывает, если строковое значение не приводится к int        
     * @return возвращает числовое значение типа {@link Integer}
     */
    public static Integer realInt(String Svalue, int i) throws PositiveException, StringParseException{
    
        int value;

        String name = i==1 ? "price" : "capacity";
        
        if(Svalue.equals("null")){return null;}
        try{
            value = (int)Double.parseDouble(Svalue);
        }catch(Exception e){throw new StringParseException("int", Svalue);}

        if(value<=0) throw new PositiveException(name);

        return value;

    }
    
    /** 
     * Используется для валидации значений типов перечислений.
     *
     * @param enumType класс обьекта перечисления
     * @param Stype строковое представление знаяения перечисления
     * @param <T> класс типа enum
     * @exception EnumException срабатывает, если в перечислении отсутствует переданное значение    
     * @return возвращает значение типа перечисления
     */
    public static <T extends Enum<T>> T type(Class<T> enumType, String Stype) throws EnumException{
        T type;
        String ClassName = enumType.getSimpleName();

        try{

            type = Enum.valueOf(enumType, Stype); // если null, то это исключение поймается
                                                  // смысла в NullValueException нет

        }catch(IllegalArgumentException e){throw new EnumException(Stype);}
        

        return type;
    }
    

}
