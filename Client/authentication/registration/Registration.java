package authentication.registration;

import authentication.UserAuth;
import java.util.Set;
import java.lang.Math;
import java.io.IOException;

public class Registration{
	private static int minValue = 1;
	private static int maxValue = 10000;


    public static String[] newUser(Set<String> users) throws IOException{
        String[] log_psw = {"", "", ""};
        
        /* проверка на уникальность логина */
        while(true){
         
            log_psw = UserAuth.LoginPswd();

            if(!users.contains(log_psw[0])) break;
            
            //
			System.out.println("Error: login is not unique");
            //
        }
        
	    
	    String salt = String.valueOf(minValue + (int)(Math.random() * (maxValue - minValue + 1)));
	    
	    log_psw[2] = salt;
        
        
        return  log_psw;
        
        
        
        
        
        
        /*
        String line; 
        while(true){
			System.out.println("\n\tlogin or register:");
        	line = input.readLine();
        	if(line.equals("login") || line.equals("register")){break;}
        	
        }
		
		
		// register
		if(line.equals("register")){
		    
		    while(true){
				System.out.println("\ninput login (len>1):");
		    	login = input.readLine();
		    	if(login.length()>1 || login != null || !users.contains(login)){
		    		break;
		    	}
		    	System.out.println("\nлогин не уникальный или не той длинны");
		    }
		    
		    String password; 
		    while(true){
				System.out.println("\ninput password (len>1):");
		    	password = input.readLine();
		    	if(password.length()>1 || password != null){
		    		break;
		    	}
		    	System.out.println("\nпароль не соответствует требованиям");
		    	
		    }
		    
		    String salt = String.valueOf(minValue + (int) (Math.random() * (maxValue - minValue + 1)));
		    
		    String plaintext = password+salt;

		    MessageDigest m = null; 

		    try{ 
		        m = MessageDigest.getInstance("MD5");
		    }catch(NoSuchAlgorithmException alg){}

			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.

		    String[] newUser = {login, hashtext, salt};
		    pack = new Package<String[]>("register", newUser);
		    Send();
		}else if(line.equals("login")){
		// login
		// отправить логин, получить соль и пароль, сравнить, иначе выйти
			String pswrd_salt;
			String password;
		    //отправить имя пользователя, получить хэш, соль
		    while(true){
		    
				while(true){
					System.out.println("\ninput login (len>1):");
					login = input.readLine();
					if(login.length()>1 || login != null || !users.contains(login)){
						break;
					}
					System.out.println("\nлогин не найден");
				}
				
				while(true){
					System.out.println("\ninput password (len>1):");
					password = input.readLine();
					if(password.length()>1 || password != null){
						break;
					}
					System.out.println("\nпароль не соответствует требованиям");	
				}
				pack = new Package<String>("auth", login);
		    
				try{

				    socket = new Socket(address, port);
				    out = new ObjectOutputStream(socket.getOutputStream());
				    in = new ObjectInputStream(socket.getInputStream());

				    out.writeObject(pack);
				    out.flush();
				    
				    pswrd_salt = (String)in.readObject(); // ждем сервер

				    in.close();
				    out.close();
				    socket.close();
					break;
				}catch(ConnectException er){
				    System.out.println("\n\u001B[0;30;47mСервер временно не доступен\u001B[0m\n");
				}catch(SocketException er){
				    script = new ArrayDeque<>();
				    while(input.ready()) input.readLine();
				    System.out.println("\n\u001B[0;31;47mСоединение с сервером потеряно\u001B[0m\n");
				}catch(ClassNotFoundException er){
				    System.out.println(er);
				}
			}
			
			String[] args = pswrd_salt.split(" ");
			
			String plaintext = password+args[1];

		    MessageDigest m = null; 

		    try{ 
		        m = MessageDigest.getInstance("MD5");
		    }catch(NoSuchAlgorithmException alg){}

			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			hashtext = bigInt.toString(16);
			
			if(hashtext.equals(args[0])){
				System.out.println("Вход выполнен");
			}else{
				System.out.println(args[0]);
				System.out.println("Неверный пароль");
				System.exit(0);
			}
	    
		}
		*/
	}
}
