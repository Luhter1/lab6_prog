package data.venue;

/** 
 * Перечисление, содержащее все доступные типы заведений.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public enum VenueType {
    /**
    * Venue type of pub
    */
    PUB("pub"),
    /**
    * Venue type of bar
    */
    BAR("bar"),
    /**
    * Venue type of mall
    */
    MALL("mall");

    /** 
     * Содержит строковое представление enum                    
     */
    private String name;

    VenueType(String name){
        this.name = name;
    }

    /** 
     * Returns a string representation of this enum class.
     * @return a string representation of this object              
     */
    public String getType(){return name;}
}
