package AnalisisLexico;

public class Nodo2 {
    String lexema;
    int token;
    Nodo2 sig = null;
    
    public Nodo2(String lexema, int tipo){
        this.lexema = lexema;
        this.token = tipo;
    }
    
    public Nodo2(String lexema){
        this.lexema = lexema;
    }   
    
    public Nodo2(int token){
        this.token = token;
    }
}
