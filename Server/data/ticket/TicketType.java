package data.ticket;

/** 
 * Перечисление, содержащее все доступные типы билетов.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public enum TicketType {
    /**
    * Ticket type of vip
    */
    VIP("vip"),
    /**
    * Ticket type of usual
    */
    USUAL("usual"),
    /**
    * Ticket type of budgetaru
    */
    BUDGETARY("budgetary"),
    /**
    * Ticket type of cheap
    */
    CHEAP("cheap");


    private String name;

    TicketType(String name){
        this.name = name;
    }
    /** 
     * Returns a string representation of this enum class.
     * @return a string representation of this object              
     */
    public String getType(){return name;}
}
