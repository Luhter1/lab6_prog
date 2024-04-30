package pack;

import java.io.Serializable;

public record Package<T>(String command, T arg) implements Serializable{}

/*
public class Package<T>{
    private String command;
    private T arg;    

    public Package(String command, T arg){
        this.command = command;
        this.arg = arg;    
    }

    public String command(){
        return command;
    }
}
*/

