package database;

public enum Requests {
    COLLECTION(
               "SELECT * FROM ticket "+
               "LEFT JOIN venue USING(venue_id) "+
               "JOIN coordinates USING(coordinates_id);"
    ),
    PRICE_LIST(
            "SELECT price FROM ticket GROUP BY price;"
    ),
    USERS(
          "SELECT login FROM users;"
    ),
    LOGIN(
          "SELECT psswrd, salt FROM users "+
          "WHERE login = ?;"
    ),
    REGISTER(
             "INSERT INTO users VALUES(?, ?, ?);"
    ),
    COORDINATES(
            "INSERT INTO coordinates VALUES(default, ?, ?) "+
            "RETURNING coordinates_id;"
    ),
    VENUE(
        "INSERT INTO venue VALUES(default, ?, ?, ?) "+
        "RETURNING venue_id;"
    ),
    ADD_TICKET(
            "INSERT INTO ticket VALUES(default, ?, ?, ?, ?, ?, ?) "+
            "RETURNING id;"
    ),
    UPDATE_TICKET(
            "INSERT INTO ticket VALUES(?, ?, ?, ?, ?, ?, ?) "+
            "RETURNING id;"
    ),
    TICKET_OWNER(
            "INSERT INTO user_item VALUES(?, ?);"
    ),
    IS_USERED(
            "SELECT COUNT(*) FROM user_item "+
            "WHERE login = ? AND id = ?;"
    ),
    DROP_COORDINATES(
            "DELETE FROM coordinates WHERE coordinates_id IN "+
            "(SELECT coordinates_id FROM ticket WHERE id IN "+
            "(SELECT id FROM user_item WHERE login = ?));"
    ),
    DROP_VENUE(
            "DELETE FROM venue WHERE venue_id IN "+
            "(SELECT venue_id FROM ticket WHERE id IN "+
            "(SELECT id FROM user_item WHERE login = ?));"
    ),
    DROP_USER_ITEM(
            "DELETE FROM user_item WHERE login = ?;"
    ),
    DROP_TICKET(
            "DELETE FROM ticket WHERE id IN "+
            "(SELECT id FROM user_item WHERE login = ?);"
    ),
    DELETE_COORDINATES(
            "DELETE FROM coordinates "+
            "WHERE coordinates_id IN "+
            "(SELECT coordinates_id FROM ticket WHERE id = ?);"
    ),
    DELETE_VENUE(
            "DELETE FROM venue "+
            "WHERE venue_id IN "+
            "(SELECT venue_id FROM ticket WHERE id = ?);"
    ),
    DELETE_USER_ITEM(
            "DELETE FROM user_item "+
            "WHERE id = ?;"
    ),
    DELETE_TICKET(
            "DELETE FROM ticket "+
            "WHERE id = ?;"
    );
    
    private String name;

    Requests(String name){
        this.name = name;
    }

    public String getRequest(){return name;}
}
