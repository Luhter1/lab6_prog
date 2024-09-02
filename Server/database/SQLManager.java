package database;


import data.ticket.Ticket;
import data.ticket.TicketType;
import data.venue.Venue;
import data.venue.VenueType;
import data.coordinates.Coordinates;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.lang.NullPointerException;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class SQLManager{
    private static Properties info;
    private static String address = "jdbc:postgresql://localhost:8800/studs";
    
    public SQLManager() throws IOException{
            info = new Properties();
            info.load(this.getClass().getResourceAsStream("../config/db.cfg"));
    }


    public static Connection DBConnect() throws SQLException{
        return DriverManager.getConnection(address, info);
    }


    /* OK */
    public static LinkedHashSet<Ticket> getCollectionFromDatabase() throws IOException, NullPointerException, SQLException{
        LinkedHashSet<Ticket> data = new LinkedHashSet<>(); // collections of tickets

        Connection connection = DBConnect();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(Requests.COLLECTION.getRequest());
        
        while (resultSet.next()) {
            /* id */
            long id = resultSet.getLong("id");

            /* name */
            String name = resultSet.getString("name");

            /* coordinates */
            Coordinates coordinates = new Coordinates(
                    resultSet.getDouble("x"), 
                    resultSet.getDouble("y")
            );

            /* creation date */
            java.util.Date creationDate = new java.util.Date(resultSet.getDate("creation_date").getTime());
            
            /* price */
            Integer price = resultSet.getInt("price");
            if (resultSet.wasNull()) {
                price = null;
            }                
            
            /* ticket type */
            TicketType type = TicketType.valueOf(
                    resultSet.getString("ticket_type")
                );

            /* venue */
            Venue venue = null;
            Long venue_id = resultSet.getLong("venue_id");
            if (!resultSet.wasNull()) {
                /* venue_name */
                String venue_name = resultSet.getString("venue_name");
                /* capacity */
                Integer capacity = resultSet.getInt("capacity");
                if (resultSet.wasNull()) {
                    capacity = null;
                }  
                /* venue_type */
                VenueType venue_type = null;    
                String str_venue_type = resultSet.getString("venue_type");
                if (!resultSet.wasNull()) {
                    venue_type = VenueType.valueOf(str_venue_type);
                }
                
                venue = new Venue(venue_id, venue_name, capacity, venue_type);
            }  

            Ticket ticket = new Ticket(id, name, coordinates, creationDate, price, type, venue);
            data.add(ticket);
        }
        
        resultSet.close();
        statement.close();
        connection.close();

        return data;
    }

    /* OK */
    public static HashSet<Integer> PriceList() throws IOException, SQLException{

        HashSet<Integer> priceList = new HashSet<>();

        Connection connection = DBConnect();
        Statement statement = connection.createStatement();


        ResultSet resultSet = statement.executeQuery(Requests.PRICE_LIST.getRequest());
        
        Integer price;
        while (resultSet.next()) {
            price = resultSet.getInt("price");
            if (resultSet.wasNull()) {
                price = null;
            }  
            
            priceList.add(price);
        }
        
        resultSet.close();
        statement.close();
        connection.close();

        return priceList;
    }
    
    public static ArrayList<Long> getTicketIdByPrice(Integer price) throws IOException, SQLException{

        ArrayList<Long> idList = new ArrayList<>();

        Connection connection = DBConnect();

		String query;
		ResultSet resultSet;
		if(price != null){
        	query = "SELECT id FROM ticket WHERE price=?;";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, price);
            resultSet = ps.executeQuery();
            ps.close();
		}else{
            Statement statement = connection.createStatement();
			query = "SELECT id FROM ticket WHERE price IS NULL;";
            resultSet = statement.executeQuery(query);
            statement.close();
		}        
        
        while (resultSet.next()) {
            idList.add(resultSet.getLong("id"));
        }
        
        resultSet.close();
        connection.close();

        return idList;
    }
    
    public static ArrayList<Long> LowerPrice(Integer price) throws IOException, SQLException{
        ArrayList<Long> idList = new ArrayList<>();

    	if(price==null) return idList;
    	

            Connection connection = DBConnect();

        	String query = "SELECT id FROM ticket WHERE price < ?;";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, price);
            ResultSet resultSet = ps.executeQuery();
    		   
            
            while (resultSet.next()) {
                idList.add(resultSet.getLong("id"));
            }
            
            resultSet.close();
            ps.close();
            connection.close();

        return idList;
    
    }
    
    public static Integer MaxPrice() throws IOException, SQLException{
    	Integer price = null;

        Connection connection = DBConnect();

        Statement statement = connection.createStatement();
		String query = "SELECT MAX(price) FROM ticket;";
        ResultSet resultSet = statement.executeQuery(query);
		   
        if (resultSet.next()) {
            price = resultSet.getInt(1);
        }
        if (resultSet.wasNull()) {
            price = 0;
        }  
        
        resultSet.close();
        statement.close();
        connection.close();


        return price;
    }
    
    /* OK */
    public static boolean isUsered(long id, String login)throws SQLException{
        Connection connection = DBConnect();
		PreparedStatement ps = connection.prepareStatement(
                    Requests.IS_USERED.getRequest()
        );
		ps.setString(1, login);
		ps.setLong(2, id);
		ResultSet resultSet = ps.executeQuery();
		
		int res = 0;
		if (resultSet.next()) {
		        res = resultSet.getInt(1);
        }
        
        return res == 0 ? false : true;
    }
    
    /* OK */
    public static Long[] updateTicket(Ticket ticket, String login) throws IOException, SQLException{
    
    	Long[] id = {null, null};

        /* добавление координаты */
        Connection connection = DBConnect();
		PreparedStatement ps = connection.prepareStatement(
                    Requests.COORDINATES.getRequest()
        );
		ps.setDouble(1, ticket.coordinates().x());
		ps.setDouble(2, ticket.coordinates().y());
		ResultSet resultSet = ps.executeQuery();
			
		int coordinates_id = 0;
		if (resultSet.next()) {
			coordinates_id = resultSet.getInt(1);
		}
		
		/* добавление заведения, если есть */
		if(ticket.venue()!=null){
			ps = connection.prepareStatement(
                Requests.VENUE.getRequest()
            );
			Venue venue = ticket.venue();
			ps.setString(1, venue.name());
			
			if(venue.capacity()!=null){
    			ps.setInt(2, venue.capacity());
			}else{
				ps.setNull(2, java.sql.Types.NULL);
			}
			
			if(venue.type()!=null){
				ps.setString(3, venue.type().getType().toUpperCase());
			}else{
				ps.setNull(3, java.sql.Types.NULL);
			}
		    /* получаем id заведения */
			resultSet = ps.executeQuery();
			
			if (resultSet.next()) {
				id[1] = resultSet.getLong(1);
			}
		}

        /* добавление билета */
		ps = connection.prepareStatement(
		    Requests.UPDATE_TICKET.getRequest()
		);
		ps.setLong(1, ticket.id());
		ps.setString(2, ticket.name());
		ps.setInt(3, coordinates_id);
		ps.setDate(4, new Date(ticket.creationDate().getTime() ));
		
		if(ticket.price()!=null){
		    ps.setInt(5, ticket.price());
		}else{
			ps.setNull(5, java.sql.Types.NULL);
		}
		
		ps.setString(6, ticket.type().getType().toUpperCase());
		
		if(id[1]==null){
			ps.setNull(7, java.sql.Types.NULL);
		}else{
			ps.setLong(7, id[1]);
		}
		
		/* получаем id билета */
		resultSet = ps.executeQuery();
		if (resultSet.next()) {
			id[0] = resultSet.getLong(1);
		}
		
        /* добавление владельца билета */
		ps = connection.prepareStatement(
		    Requests.TICKET_OWNER.getRequest()
		);
		ps.setString(1, login);
		ps.setLong(2, id[0]);
		ps.executeUpdate();
		
        resultSet.close();
        ps.close();
        connection.close();
   
        return id;
    }
    
    
    /* OK */
    public static Long[] addTicket(Ticket ticket, String login) throws IOException, SQLException{
    
    	Long[] id = {null, null};

        /* добавление координаты */
        Connection connection = DBConnect();
		PreparedStatement ps = connection.prepareStatement(
                    Requests.COORDINATES.getRequest()
        );
		ps.setDouble(1, ticket.coordinates().x());
		ps.setDouble(2, ticket.coordinates().y());
		ResultSet resultSet = ps.executeQuery();
			
		int coordinates_id = 0;
		if (resultSet.next()) {
			coordinates_id = resultSet.getInt(1);
		}
		
		/* добавление заведения, если есть */
		if(ticket.venue()!=null){
			ps = connection.prepareStatement(
                Requests.VENUE.getRequest()
            );
			Venue venue = ticket.venue();
			ps.setString(1, venue.name());
			
			if(venue.capacity()!=null){
    			ps.setInt(2, venue.capacity());
			}else{
				ps.setNull(2, java.sql.Types.NULL);
			}
			
			if(venue.type()!=null){
				ps.setString(3, venue.type().getType().toUpperCase());
			}else{
				ps.setNull(3, java.sql.Types.NULL);
			}
		    /* получаем id заведения */
			resultSet = ps.executeQuery();
			
			if (resultSet.next()) {
				id[1] = resultSet.getLong(1);
			}
		}

        /* добавление билета */
		ps = connection.prepareStatement(
		    Requests.ADD_TICKET.getRequest()
		);
		ps.setString(1, ticket.name());
		ps.setInt(2, coordinates_id);
		ps.setDate(3, new Date(ticket.creationDate().getTime() ));
		
		if(ticket.price()!=null){
		    ps.setInt(4, ticket.price());
		}else{
			ps.setNull(4, java.sql.Types.NULL);
		}
		
		ps.setString(5, ticket.type().getType().toUpperCase());
		
		if(id[1]==null){
			ps.setNull(6, java.sql.Types.NULL);
		}else{
			ps.setLong(6, id[1]);
		}
		
		/* получаем id билета */
		resultSet = ps.executeQuery();
		if (resultSet.next()) {
			id[0] = resultSet.getLong(1);
		}
		
        /* добавление владельца билета */
		ps = connection.prepareStatement(
		    Requests.TICKET_OWNER.getRequest()
		);
		ps.setString(1, login);
		ps.setLong(2, id[0]);
		ps.executeUpdate();
		
        resultSet.close();
        ps.close();
        connection.close();
   
        return id;
    }
    
    
    
    
    /* OK */
    public static void removeTicket(long id) throws IOException, SQLException{

        Connection connection = DBConnect();

        /* удаляем координату */
		PreparedStatement ps = connection.prepareStatement(
		        Requests.DELETE_COORDINATES.getRequest()
		        );
		ps.setLong(1, id);
        ps.executeUpdate();
        
        /* удаляем заведение */
		ps = connection.prepareStatement(
		        Requests.DELETE_VENUE.getRequest()
		        );
		ps.setLong(1, id);
        ps.executeUpdate();
        
        /* удаляем владение */
		ps = connection.prepareStatement(
		        Requests.DELETE_USER_ITEM.getRequest()
		        );
		ps.setLong(1, id);
        ps.executeUpdate(); 
        
        /* удаляем билет */
		ps = connection.prepareStatement(
		        Requests.DELETE_TICKET.getRequest()
		        );
		ps.setLong(1, id);
        ps.executeUpdate();        

        ps.close();
        connection.close();
    }

    /* OK */
    public static void DropDB(String login) throws IOException, SQLException{

        Connection connection = DBConnect();

        /* drop user's coordinates */
		PreparedStatement ps = connection.prepareStatement(
		        Requests.DROP_COORDINATES.getRequest()
		        );
		ps.setString(1, login);
        ps.executeUpdate();


        /* drop user's venue */
		ps = connection.prepareStatement(
		        Requests.DROP_VENUE.getRequest()
		        );
		ps.setString(1, login);
        ps.executeUpdate();

        /* drop user's item */
		ps = connection.prepareStatement(
		        Requests.DROP_USER_ITEM.getRequest()
		        );
		ps.setString(1, login);
        ps.executeUpdate();
        
        /* drop user's ticket */
		ps = connection.prepareStatement(
		        Requests.DROP_TICKET.getRequest()
		        );
		ps.setString(1, login);
        ps.executeUpdate();

        
        ps.close();
        connection.close();

    }

    /* OK */
    public static Set<String> getUsers() throws SQLException{

        HashSet<String> priceList = new HashSet<>();
    
        Connection connection = DBConnect();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(Requests.USERS.getRequest());

        while (resultSet.next()) {
            priceList.add( resultSet.getString("login") );
        }
        
        resultSet.close();
        statement.close();
        connection.close();
    
        return priceList;
    
    }
    
    /* OK */
    public static void register(String login, String pswd, String salt) throws SQLException{

    	Connection connection = DBConnect();
		PreparedStatement ps = connection.prepareStatement(Requests.REGISTER.getRequest());
		ps.setString(1, login);
		ps.setString(2, hash(pswd+salt));
		ps.setString(3, salt);
		ps.executeUpdate();
        
        ps.close();
        connection.close();


    }

    /* OK */
    public static boolean login(String login, String pswd) throws SQLException{

    	Connection connection = DBConnect();
		PreparedStatement ps = connection.prepareStatement(Requests.LOGIN.getRequest());
		ps.setString(1, login);

        ResultSet resultSet = ps.executeQuery();
        
        String hash_pswd = null;
        String salt = null;
        
        if (resultSet.next()) {
            hash_pswd = resultSet.getString("psswrd");
            salt = resultSet.getString("salt");
        }
        
        resultSet.close();
        ps.close();
        connection.close();
        
        return hash(pswd+salt).equals(hash_pswd);
    }    
    
    
    public static String hash(String plaintext){
    
	    MessageDigest m = null; 

        try{ 
            m = MessageDigest.getInstance("MD5");
        }catch(NoSuchAlgorithmException alg){
            //
			System.out.println("\nError: wrong hash algoritm");
			//
			
			System.exit(1);
        }

	    m.reset();
	    m.update(plaintext.getBytes());
	    
	    byte[] digest = m.digest();
	    BigInteger bigInt = new BigInteger(1,digest);
	    
	    return bigInt.toString(16);
    
    }

}
