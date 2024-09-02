package authentication;

import authentication.registration.Registration;
import client.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;
import server.connect.Package;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class UserAuth{
    private static BufferedReader input = Client.getScan();

    public static String[] auth(Set<String> users) throws IOException{
        String[] answer = {"", "", ""};
        String line; 
        
        /* выбор регистрация или авторизация */
        while(true){
            //
			System.out.print("\nInput login or register:\n\t");
        	//
        	line = input.readLine().toLowerCase();
        	if(line.equals("login") || line.equals("register")) break;	
        }
        
        /* выполнение выбранного процесса */
        switch(line){
        
            /* авторизация */
            case "login":
                while(true){
                    answer = LoginPswd(); // login, pswd, ""

                    if(!users.contains(answer[0])){
                        //
                        System.out.println("\nError: non-existent login, try again");
                        //
                        continue;
                    }
                    
                    boolean output = (boolean)Client.Send(
                            new Package<String[]>("login", answer, "", ""), 
                            true);
                        
                    if(output) break;
                    
                    //
                    System.out.println("\nError: wrong password, try again");
                    //
                }
                //
                System.out.println("\nLogged in account \"" + answer[0] + "\" completed");
                //
                break;
                
            /* регистрация */
            case "register":
                answer = Registration.newUser(users); // login, pswd, salt
                Client.Send(new Package<String[]>("register", answer, "", ""), true);
                break;
        
        }
        return answer;
    }
    
    public static String[] LoginPswd()throws IOException{
        String[] log_psw = {"", "", ""};
        
        String[] values = {"login", "password"};
        
        int i = 0;
        for(String value : values){
        
            /* ввод значения и его валидация */
            while(true){
                //
			    System.out.print("\nInput your " + value + "(length>2):\n\t");
            	//
            	
            	log_psw[i] = input.readLine().toLowerCase();
            	
            	if(log_psw[i].length()>2){
            	    i++;
            	    break;
        	    }
            	
            	//
			    System.out.println("Error: incorrect length");
                //
            }
            
        }

        return log_psw;
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
