package AnalisisLexico;
import java.io.*;

public class Lexico2 {
    Nodo cabeza = null;
    Nodo p;
    int columna;
    int fila = 0;
    int numeroRenglon = 1;
    char caracter;
    int index;
    int token;
    String lexema = "";
    boolean error = false;
    
    /*
    Errores
    500 Se esperaba numero
    501 Fin de documento inesperado
    502 
    503 Fin de linea inesperado
    504 Caracter Desconocido
    */
    
    int matriz[][] = {
        // L    D    +    -    *    =    <     >   '    :    (    )    .    ,    ;    _    eb  tab  eol  eof   oc
        // 0    1    2    3    4    5    6     7    8   9    10   11   12   13   14   15   16   17   18   19   20
  /*0*/ {   1,   2, 103, 104, 105, 106,   5,   6,   7,   8,   9, 126, 122, 123, 124, 504,   0,   0,   0,   0, 504},
  /*1*/ {   1,   1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 1  , 100, 100, 100, 100, 100},
  /*2*/ { 101,   2, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101,   3, 101, 101, 101, 101, 101, 101, 101, 101},
  /*3*/ { 500,   4, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500},
  /*4*/ { 102,   4, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102},
  /*5*/ { 108, 108, 108, 108, 108, 109, 108, 107, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108},
  /*6*/ { 111, 111, 111, 111, 111, 110, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111},
  /*7*/ {   7,   7,   7,   7,   7,   7,   7,   7, 120,   7,   7,   7,   7,   7,   7,   7,   7,   7, 503, 501,   7},
  /*8*/ { 127, 127, 127, 127, 127, 121, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127},
  /*9*/ { 125, 125, 125, 125,  10, 125, 125, 125, 125, 125, 125, 125, 125, 125, 125, 125, 125, 125, 125, 125, 125},
  /*10*/{  10,  10,  10,  10,  11,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10, 503, 501,  10},
  /*11*/{  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,   0,  10,  10,  10,  10,  10,  10, 503, 501,  10},
    };
    
    String palabrasReservadas[] = {"or","and","not","while","do",     
                                    "if","then","else","begin","end",
                                    "div", "read","write","true","false",
                                    "var","integer","real","string", "program"};

    
    public void leerArchivo(){
        File archivo = new File("C:\\PruebasJava\\Diana.txt");
        try {
            BufferedReader entrada = new BufferedReader(new FileReader(archivo));
            
            String lectura;
            lectura = entrada.readLine();
            
            while (lectura != null) {
                for (index = 0; index < lectura.length(); index++) {
                    caracter = lectura.charAt(index);
                    escogerColumna();
                    analizarMatriz();
                }
                lectura = entrada.readLine();
                if (caracter != '\u0000') {
                 
                    if (lectura != null) {
                        columna = 18;
                        //System.out.println(lexema);
                        analizarMatriz();
                        //System.out.println("end of line");
                    } else {
                        columna = 19;
                        analizarMatriz();
                        //System.out.println("end of file");
                    }
                }
                numeroRenglon++;
                
            }
            entrada.close();
            //imprimirLista();
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Nodo getCabeza() {
        return cabeza;
    }
    
    
    public void escogerColumna() {
        if (isCaracter(caracter)) {
            columna = 0;
        } else if (isNumber(caracter)) {
            columna = 1;
        } else {
            switch (caracter) {
                case '+':
                    columna = 2;
                    break;
                case '-':
                    columna = 3;
                    break;
                case '*':
                    columna = 4;
                    break;
                case '=':
                    columna = 5;
                    break;
                case '<':
                    columna = 6;
                    break;
                case '>':
                    columna = 7;
                    break;
                case ':':
                    columna = 9;
                    break;
                case 39:
                    columna = 8;
                    break;
                case '(':
                    columna = 10;
                    break;
                case ')':
                    columna = 11;
                    break;
                case '.':
                    columna = 12;
                    break;
                case ',':
                    columna = 13;
                    break;
                case ';':
                    columna = 14;
                    break;
                case '_':
                    columna = 15;
                    break;
                case ' ':
                    columna = 16;
                    break;
                case 9:
                    columna = 17;
                    break;
                default:
                    columna = 20;
            }
            //System.out.println(columna);
        }
    }
    
    public void limpiar(){
            fila = 0;
            columna = 0;
            lexema = "";
            caracter = '\u0000';
    }
    
    public void analizarMatriz() {
        token = matriz[fila][columna];
        if (token == 500) {
            agregarALista();
            index--;
            limpiar();
            error = true;
        }else if(token==504){
            lexema = lexema + caracter;
            agregarALista();
            limpiar();
            error = true;
        }else if(token == 501 || token == 503){
            agregarALista();
            limpiar();
            error = true;
        }
        else if(token == 0){
            limpiar();
        }else if (token < 100) {
            fila = token;
            lexema = lexema + caracter;
        } else if (token >= 100 && token <= 200) {
            switch (token) {
                case 100:
                    compararString();
                    agregarALista();
                    index--;
                    break;
                case 101:
                case 102:
                case 125:
                case 127:
                case 111:
                case 108:        
                    agregarALista();
                    index--;
                    break;
                default:
                    lexema = lexema+caracter;
                    agregarALista();
            }
            limpiar();
        }
    }
    
    public void compararString(){
        int i;
        for (i = 0; i < palabrasReservadas.length; i++) {
            if (palabrasReservadas[i].equals(lexema)) {
              token = i+200;
            }
        } 
    }
    
    
    public void agregarALista() {
        
        if (!lexema.equals("")) {
            if (cabeza == null) {
                cabeza = new Nodo(lexema, token, numeroRenglon);
                //System.out.printf("%s %d %d\n", lexema, token, numeroRenglon);
            } else {
                if (p == null) {
                    p = new Nodo(lexema, token, numeroRenglon);
                    //System.out.printf("%s %d %d\n", lexema, token, numeroRenglon);
                    cabeza.sig = p;
                } else {
                    p.sig = new Nodo(lexema, token, numeroRenglon);
                    //System.out.printf("%s %d %d\n", lexema, token, numeroRenglon);
                    p = p.sig;
                }
            }
        }
        limpiar();
    }
    
    public boolean isCaracter(char caracter) {
        if ((int) caracter >= 97 && (int) caracter <= 122
                || (int) caracter >= 65 && (int) caracter <= 90) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNumber(char caracter) {
        if ((int) caracter >= 48 && (int) caracter <= 57) {
            return true;
        } else {
            return false;
        }
    }

    public void imprimirLista() {
        Nodo control = cabeza;
        while (control != null) {
            System.out.printf("%30s %5d %3d\n", control.lexema, control.token, control.renglon);
            control = control.sig;
        }
        
        
    }
}
