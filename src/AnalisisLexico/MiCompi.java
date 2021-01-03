package AnalisisLexico;

public class MiCompi {

    public static void main(String[] args) {
        //Comienzo analisis lexico
        Lexico2 lexico = new Lexico2();
        lexico.leerArchivo();
        //Comienzo analisis sintactico
        if (!lexico.error) {
            Sintactico sintactico = new Sintactico(lexico.getCabeza());
            sintactico.program();
        }
    }
}
