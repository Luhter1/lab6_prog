package data.venue;

import java.io.Serializable;
/** 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public record Venue(long id, String name, Integer capacity, VenueType type) implements Serializable{

    /** 
     * Returns a string representation of this record class to be written to the collection file.
     * @return a string representation of this object to be written to the collection file
     *
     * @see receiver.VectorCollection#Save()                       
     */
    public String toWrite(){
        return id+";"+name+";"+capacity+";"+type+";";
    }

    @Override
    public String toString(){
        return "venue_id: "+id+"\nvenue_name: "+name+"\ncapacity: "+capacity+"\nvenue type: "+type;
    }

}
