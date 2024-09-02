package data.venue;

import java.io.Serializable;

/** 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public record Venue(long id, String name, Integer capacity, VenueType type) implements Serializable{

    public Venue(long id, Venue venue){
        this(id, venue.name(), venue.capacity(), venue.type());
    }


    @Override
    public String toString(){
        return "venue_id: "+id+"\nvenue_name: "+name+"\ncapacity: "+capacity+"\nvenue type: "+type;
    }

}
