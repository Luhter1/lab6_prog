package server;

import java.io.IOException;


public class EnvError extends IOException{

    public String toString(){
        return "переменая окружения не обнаружена";
    }
}
