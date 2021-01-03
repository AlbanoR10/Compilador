package AnalisisLexico;
import java.util.Stack;
public class Sintactico {
    Stack pila = new Stack();
    Nodo control;
    
    Nodo2 controlLista = null;
    Nodo2 cabezaListaVariables = null;
    Nodo2 controlAux = null;
    Nodo2 cabezaAux = null;
    Nodo2 listaParaPosfijoC;
    Nodo2 listaParaPosfijo;
    Nodo2 listaPosfijoC;
    Nodo2 listaPosfijo;
    Nodo2 listaPolishC;
    Nodo2 listaPolish;
    int sigA=1;
    int sigC=1;
    int tipo;
    
    boolean error=true;

    public boolean isError() {
        return error;
    }
    
    public Sintactico(Nodo principio) {
        control = principio;
    }
   
 
    
    public boolean comprobar(int id){
        return (control != null && control.token == id);
    }

    public void program() {
        System.out.println("Analisis Sintactico");
        if (error && comprobar(219)) {
            control = control.sig;
            if (error && comprobar(100)) {
                iniciarListaVariables();
                control = control.sig;
                if (error && comprobar(124)) {
                    control = control.sig;
                    if (isBlock()) {
                        block();
                    }else{
                        mostrarMensajeError("<block>");
                    }
                    if (error) {
                        if (comprobar(122)) {

                        } else {
                            mostrarMensajeError(".");
                        }
                    }
                } else {
                    mostrarMensajeError(";");
                }
            } else {     
                mostrarMensajeError("id");
            }
        } else {
            mostrarMensajeError("program");
        }
        if (error) {
            System.out.println("Analisis Lexico, Sintactico y Semantico Terminado Correctamente!!");
        }
        if (error) {
            imprimirLista2(listaPolishC);
            //imprimirLista(cabezaListaVariables);
            
        }
        
    }
    
    public void block() {
        if (isVariableDeclarationPart()) {
            variableDeclarationPart();
        }
        if (error) {
            if (isStatementPart()) {
                statementPart();
            }else{
                mostrarMensajeError("<statementPart>");
            }
        }
    }
    
    public void iniciarListaVariables(){
        cabezaListaVariables = new Nodo2(control.lexema);
    }
    
    public void agregarListaAux(){
        if (cabezaAux == null) {
            cabezaAux = new Nodo2(control.lexema);
        }else{
            if (controlAux == null) {
                controlAux = new Nodo2(control.lexema);
                cabezaAux.sig = controlAux;
            }else{
                controlAux.sig = new Nodo2(control.lexema);
                controlAux = controlAux.sig;
            }   
        }
    }
    
    public void agregarLista(){
        if (cabezaListaVariables.sig == null) {
            cabezaListaVariables.sig = cabezaAux;
        }else{
            controlLista = cabezaListaVariables;
            do{
                controlLista = controlLista.sig;
            }while (controlLista.sig != null);
            controlLista.sig = cabezaAux;
        }
        cabezaAux = null;
        controlAux = null;
    }
    
    public void completarListaAux(){
        Nodo2 x = cabezaAux;
        do{
            x.token = tipo;
            x = x.sig;
        }while (x != null);
    }
    
     public void comprobarVariables(){ 
        if (cabezaAux == null && error) {//Si la lista auxiliar no esta iniciada
            Nodo2 x = cabezaListaVariables;
            while(true){
                if (control.lexema.equals(x.lexema)) {
                    if (control.lexema.equals(cabezaListaVariables.lexema) ) {
                        System.out.println("Variable igual al nombre del programa: "+control.lexema);
                    }else{
                        System.out.println("Variable ya declarada: "+control.lexema);
                    }
                    error = false;
                    break;
                }else{
                    if (x.sig == null) {
                        break;
                    }else{
                        x = x.sig;
                    }
                }
            }
        }else if(error){//Si la lista auxiliar esta iniciada
            Nodo2 x = cabezaAux;
            while(true){
                if (control.lexema.equals(x.lexema)) {
                    if (control.lexema.equals(cabezaListaVariables.lexema) ) {
                        System.out.println("Variable igual al nombre del programa: "+control.lexema);
                    }else{
                        System.out.println("Variable ya declarada: "+control.lexema);
                    }
                    error = false;
                    break;
                }else{
                    if (x.sig == null) {
                        break;
                    }else{
                        x = x.sig;
                    }
                }
            }
            if (error) {
                x = cabezaListaVariables;
                while (true) {
                    if (control.lexema.equals(x.lexema)) {
                        if (control.lexema.equals(cabezaListaVariables.lexema)) {
                            System.out.println("Variable igual al nombre del programa: " + control.lexema);
                        } else {
                            System.out.println("Variable ya declarada: " + control.lexema);
                        }
                        error = false;
                        break;
                    } else {
                        if (x.sig == null) {
                            break;
                        } else {
                            x = x.sig;
                        }
                    }
                }
            }
            
        }
    }
     
    public void variableExits(){
        Nodo2 s = cabezaListaVariables;
        if (s == null) {
            System.out.println("Variable no existe: "+control.lexema);
        }else{
            int contador = 0;
            while (true) {
                if (control.lexema.equals(s.lexema)){
                    contador++;
                    if (error) {
                        if (s.sig == null) {
                            break;
                        } else {
                            s = s.sig;
                        }
                    }
                }else{
                    if (s.sig == null) {
                        break;
                    }else{
                        s = s.sig;
                    }
                }
            }
            if (!(contador==1)) {
                System.out.println("Variable no declarada: "+control.lexema);
                error = false;
            }
        }
    }
     
    public void variableDeclarationPart() {
        if (comprobar(215)) {
            control = control.sig;
            while (true) {
                while (true) {
                    if (comprobar(100)) {
                        comprobarVariables();
                        if (!error) {
                            return;
                        }
                        agregarListaAux();
                        control = control.sig;
                        if (comprobar(123)) {
                            control = control.sig;
                        } else if (comprobar(127)) {
                            break;
                        } else {
                            mostrarMensajeError(":");
                            return;
                        }
                    } else {
                        mostrarMensajeError("id");
                        return;
                    }
                }
                if (comprobar(127)) {
                    control = control.sig;
                    if (comprobar(218) || comprobar(217) || comprobar(216)) {
                        tipo = control.token;
                        control = control.sig;
                        if (comprobar(124)) {
                            completarListaAux();
                            agregarLista();
                            control = control.sig;
                        } else {
                            mostrarMensajeError(";");
                            return;
                        }
                    } else {
                        mostrarMensajeError("el Tipo de las variables!");
                        return;
                    }
                }
                if (!comprobar(100)) {
                    return;
                }
            }
        }
    }
    
    public void mostrarMensajeError(String caracter){
        error = false;
        if (control==null) {
            System.out.printf("Se esperaba %s \n",caracter);
        }else{
            System.out.printf("Se esperaba %s en renglon: %d\n",caracter,control.renglon);
        }
        
    }
    
    public void statementPart() {
        int contador=0;
        if (error && comprobar(208)) {
            control = control.sig;
            while (true) {
                if (error && isStatement()) {
                    statement();
                    contador++;
                } else {
                    if (comprobar(209)&& contador>0) {
                        System.out.println("Sobra ; en renglon " + control.renglon);
                        error = false;
                    }else{
                        mostrarMensajeError("<statement>");
                    }
                    return;
                }
                if (error && comprobar(124)) {
                    control = control.sig;
                } else {
                    if (error && isStatement()) {
                        System.out.println("Falta ; en renglon "+ control.renglon);
                        error = false;
                    }
                    break;
                }
            }
            if (error) {
                if (comprobar(209)) {
                    control = control.sig;
                } else {
                    mostrarMensajeError("end");
                }
            }
        } else {
            mostrarMensajeError("begin");
        }
    }
    
    public void ifStatement() {
        int A = sigA;
        sigA++;
        if (error && comprobar(205)) {//if
            control = control.sig;
            listaParaPosfijo = new Nodo2("Comienzo", 0);
            listaParaPosfijoC = listaParaPosfijo;
            
            if (error && isExpresion()) {
                expresion();
                //imprimirLista(listaParaPosfijoC);
                infijoAPosfijo();
                listaPolish.sig = new Nodo2("BRF-A"+A);
                listaPolish = listaPolish.sig;
                if (comprobar(206)) {//then
                    control = control.sig;
                    if (error && isStatement()) {
                        statement();
                        if (error && comprobar(207)) {//else
                            listaPolish.sig = new Nodo2("BRI-B"+A);
                            listaPolish = listaPolish.sig;
                            control = control.sig;
                            listaPolish.sig = new Nodo2("A"+A);
                            listaPolish = listaPolish.sig;
                            if (error && isStatement()) {
                                statement();
                                listaPolish.sig = new Nodo2("B"+A);
                                listaPolish = listaPolish.sig;
                            }else{
                                mostrarMensajeError("<statement>");
                            }
                        }else{
                            listaPolish.sig = new Nodo2("A"+A);
                            listaPolish = listaPolish.sig;
                        }
                    }else{//Aqui se tiene que corregir redundancia de error
                        mostrarMensajeError("<statement>");
                        //System.out.println("aqui2");
                    }
                }else{
                    mostrarMensajeError("then");
                }
            } else {
                mostrarMensajeError("<expresion>");
            }
        }
    }
    
    public void readStatement() {
        if (error && comprobar(211)) {
            control = control.sig;
            if (error && comprobar(125)) {
                control = control.sig;
                while (true) {
                    if (error && comprobar(100)) {
                        //System.out.println("antes de variableExits");
                        variableExits();
                        if (error) {
                            control = control.sig;
                            //System.out.println("1121");
                        }else{
                            break;
                        }
                        //System.out.println("1");
                    } else {
                        mostrarMensajeError("id");
                        break;
                    }
                    if (error && comprobar(123)) {
                        control = control.sig;
                    } else {
                        break;
                    }
                }
                if (error) {
                    if (comprobar(126)) {
                        control = control.sig;
                    }else{
                        mostrarMensajeError(")");
                    }
                }
            } else {
                mostrarMensajeError("(");
            }
        }
        
    }
    
    public boolean isStatement() {
        return comprobar(100) || comprobar(203) || comprobar(205) || comprobar(208) || comprobar(211) || comprobar(212);
    }
    
    public boolean isSimpleStatement(){
        switch (control.token) {
            case 100:
            case 211:
            case 212:
                return true;
            default:
                return false;
        }
    }
    
    public boolean isStructureStatement(){
        switch (control.token) {
            case 208:
            case 203:
            case 205:
                return true;
            default:
                return false;
        }
    }
    
    public void statement(){
        if (isSimpleStatement()) {
            simpleStatement();
        }else if(isStructureStatement()){
            structureStatement();
        }
    }
    
    public void simpleStatement(){
        if (error &&isAssignmentStatement()) {
            assignmentStatement();
        }else if(error && isReadStatement()){
            readStatement();
        }else if(error && isWriteStatement()){
            writeStatement();
        }
    }
    
    public void structureStatement(){
        if (error && isStatementPart()) {
            statementPart();
        }else if(error && isIfStatement()){
            ifStatement();
        }else if(error && isWhileStatement()){
            whileStatement();
        }
    }
    
    public void assignmentStatement(){
        if (error && comprobar(100)) {
            variableExits();
            if (error) {
                listaParaPosfijo = new Nodo2("Comienzo", 0);
                listaParaPosfijoC = listaParaPosfijo;
                listaParaPosfijo.sig = new Nodo2(control.lexema, control.token);
                listaParaPosfijo = listaParaPosfijo.sig;
                control = control.sig;
                if (error && comprobar(121)) {
                    listaParaPosfijo.sig = new Nodo2(control.lexema, control.token);
                    listaParaPosfijo = listaParaPosfijo.sig;
                    control = control.sig;
                    if (error && isExpresion()) {
                        expresion();
                    } else {
                        mostrarMensajeError("<expresion>");
                    }
                } else {
                    mostrarMensajeError(":=");
                }
            }
            
        }else{
            mostrarMensajeError("id");
        }
        //imprimirLista(listaParaPosfijoC);
        if (error) {
            infijoAPosfijo();
            listaParaPosfijoC = null;
            listaParaPosfijo = null;
        }
    }

    public void writeStatement() {
        if (error && comprobar(212)) {
            control = control.sig;
            if (error && comprobar(125)) {
                control = control.sig;
                while (true) {
                    if (error && isExpresion()) {
                        listaParaPosfijo = new Nodo2("Comienzo", 0);   
                        listaParaPosfijoC = listaParaPosfijo;
                        expresion();
                        //imprimirLista(listaParaPosfijoC);
                        infijoAPosfijo();
                    } else {
                        mostrarMensajeError("<expresion>");
                    }
                    if (error && comprobar(123)) {
                        control = control.sig;
                    }else{
                        break;
                    }
                }
                if (error) {
                    if (comprobar(126)) {
                        control = control.sig;
                        listaPolish.sig = new Nodo2("write");
                        listaPolish = listaPolish.sig;
                    }else{
                        mostrarMensajeError(")");
                    }
                }
            }else{
                mostrarMensajeError("(");
            }
        }
    }
    
    public void expresion() {
        if (error &&isExpresion()) {
            simpleExpresion();
            
            if (error && isRelationalOperators()) {
                listaParaPosfijo.sig = new Nodo2(control.lexema, control.token);
                listaParaPosfijo = listaParaPosfijo.sig;
                control = control.sig;  
                
                if (error && isExpresion()) {
                    simpleExpresion();
                } else {
                    mostrarMensajeError("<expresion>");
                }
            }
        }
    }
    
    public void whileStatement(){
        int C = sigC;
        sigC++;
        if (listaPolishC == null) {
            listaPolishC = new Nodo2("D"+C);
            listaPolish = listaPolishC;
        }else{
            listaPolish.sig = new Nodo2("D"+C);
            listaPolish = listaPolish.sig;
        }
        if (isWhileStatement()) {
            control = control.sig;
            if (isExpresion()) {
                listaParaPosfijo = new Nodo2("Comienzo", 0);
                listaParaPosfijoC = listaParaPosfijo;
                expresion();
                infijoAPosfijo();
                listaPolish.sig = new Nodo2("BRF-C"+C);
                listaPolish = listaPolish.sig;
                if (comprobar(204)) {
                    control = control.sig;
                    if (isStatement()) {
                        statement();
                        listaPolish.sig= new Nodo2("BRI-D"+C);
                        listaPolish = listaPolish.sig;
                    }else{
                        mostrarMensajeError("<statement>");
                    }
                }else{
                    mostrarMensajeError("do");
                }
            }else{
                mostrarMensajeError("<expresion>");
            }
            listaPolish.sig = new Nodo2("C" + C);
            listaPolish = listaPolish.sig;
        }
    }
    
    public void simpleExpresion(){
        if (error && isSing()) {
            if (control.token == 103) {
                listaParaPosfijo.sig = new Nodo2("@",130);
                listaParaPosfijo = listaParaPosfijo.sig;
            }else{
                listaParaPosfijo.sig = new Nodo2("&",131);
                listaParaPosfijo = listaParaPosfijo.sig;
            }
            control = control.sig;
        }
        if (error && isTerm()) {
            term();
        }else{
            mostrarMensajeError("<term>");
        }
        if (error && isAddingOperator()) {
            listaParaPosfijo.sig = new Nodo2(control.lexema, control.token);
            listaParaPosfijo = listaParaPosfijo.sig;
            control = control.sig;
            if (error && isTerm()) {
                term();
            }else{
                mostrarMensajeError("<term>");
            }
        }
    }
    
    public void term(){
        if (error && isFactor()) {
            factor();
        }
        if(error && isMultiplyingOperator()){
            listaParaPosfijo.sig = new Nodo2(control.lexema, control.token);
            listaParaPosfijo = listaParaPosfijo.sig;
            control = control.sig;
            if (isFactor()) {
                factor();
            }else{
                mostrarMensajeError("<factor>");
            }
        }
    }
    
    public void factor(){
        if (comprobar(100)||comprobar(101)||comprobar(102)||comprobar(213)||comprobar(214)||comprobar(120)) {
            if (control.token==100) {
                variableExits();
            }
            if (error) {
                listaParaPosfijo.sig = new Nodo2(control.lexema, control.token);
                listaParaPosfijo = listaParaPosfijo.sig;
                control = control.sig;
            }
        }else if(error && comprobar(202)){
            listaParaPosfijo.sig = new Nodo2(control.lexema, control.token);
            listaParaPosfijo = listaParaPosfijo.sig;
            control = control.sig;
            if (error && isFactor()) {
                factor();
            }else{
                mostrarMensajeError("<factor>");
            }
        }else if(error && comprobar(125)){
            listaParaPosfijo.sig = new Nodo2(control.lexema, control.token);
            listaParaPosfijo = listaParaPosfijo.sig;
            control = control.sig;
            if (error && isExpresion()) {
                expresion();
                if (error && comprobar(126)) {
                    listaParaPosfijo.sig = new Nodo2(control.lexema, control.token);
                    listaParaPosfijo = listaParaPosfijo.sig;
                    control = control.sig;
                }else{
                    mostrarMensajeError(")");
                }
            }else{
                mostrarMensajeError("<expresion>");
            }
        }
    }
    
    public boolean isAssignmentStatement(){
        return comprobar(100);
    }
    
    public boolean isAddingOperator(){
        return comprobar(103)||comprobar(104)||comprobar(200);
    }
    
    public boolean isMultiplyingOperator(){
        return comprobar(105) || comprobar(210)||comprobar(201);
    }
    
    public boolean isTerm(){
        return (comprobar(100)||comprobar(101)||comprobar(102)||comprobar(202)|| comprobar(120)||comprobar(213)||comprobar(214)|| isExpresion());
    }
    
    public boolean isSing(){
        return comprobar(103) || comprobar(104);
    }
    
    public boolean isRelationalOperators(){
        return comprobar(106)||comprobar(107)||comprobar(108)||comprobar(109)||comprobar(110)||comprobar(111);
    }
    
    public boolean isFactor(){
        return comprobar(100)||comprobar(101)||comprobar(102)||comprobar(125)||comprobar(202)||comprobar(213)||comprobar(214)||comprobar(120);
    }
    
    public boolean isExpresion(){
        return comprobar(100) ||comprobar(101) ||comprobar(102) || comprobar(103) || comprobar(104) ||
                comprobar(202) || comprobar(120) ||comprobar(125)||comprobar(213)||comprobar(214);
    }
    
    public boolean isWriteStatement(){
        return comprobar(212);
    }
    
    public boolean isReadStatement(){
        return comprobar(211);
    }
    
    public boolean isWhileStatement(){
        return comprobar(203);
    }
    
    public boolean isBlock(){
        return comprobar(215) || comprobar(208) ;
    }
    
    public boolean isVariableDeclarationPart(){
        return comprobar(215);
    }
     
    public boolean isStatementPart(){
        return comprobar(208);
    }
    
    public boolean isIfStatement(){
        return comprobar(205);
    }
    
    public void imprimirLista(Nodo2 cabeza) {
        Nodo2 x = cabeza;
        while (x != null) {
            System.out.printf("%15s  %d\n", x.lexema, x.token);
            x = x.sig;
        }
        //System.out.println("=====================================================================");
    }
    public void imprimirLista2(Nodo2 cabeza) {
        int contador = 0;
        Nodo2 x = cabeza;
        while (x != null) {
            System.out.printf("%10s  |  ", x.lexema);
            x = x.sig;
            contador++;
            if (contador%5==0) {
                System.out.println();
            }
        }
        System.out.println( );
        System.out.println("=====================================================================");
    }
    
    
    public void infijoAPosfijo(){
        pila = new Stack();
        Nodo2 asd;
        listaParaPosfijo = listaParaPosfijoC.sig;
        while (listaParaPosfijo != null) {
            
            if (listaParaPosfijo.token == 100 || listaParaPosfijo.token == 101 || listaParaPosfijo.token == 102 || listaParaPosfijo.token == 120) {
                if (listaPosfijoC == null) {
                    //listaPosfijoC = listaParaPosfijo;
                    listaPosfijoC = new Nodo2(listaParaPosfijo.lexema,listaParaPosfijo.token );
                    listaPosfijo = listaPosfijoC;
                }else{
                    listaPosfijo.sig = new Nodo2(listaParaPosfijo.lexema,listaParaPosfijo.token );
                    listaPosfijo = listaPosfijo.sig;
                }
            }else{
                if (listaParaPosfijo.token == 125) {
                        
                    pila.push(new Nodo2(listaParaPosfijo.lexema, listaParaPosfijo.token));
                        
                }else if(prioridad(listaParaPosfijo.token)==7){
                    while (true) {//Posible error
                        
                        Nodo2 ayudante = (Nodo2)pila.pop();
                        if (prioridad(ayudante.token)==0) {
                            break;
                        }
                        listaPosfijo.sig = new Nodo2(ayudante.lexema, ayudante.token);
                        listaPosfijo = listaPosfijo.sig;
                    }
                }else{
                    while (true) {
                        if (pila.empty()) {//esto no se ejecuta a menos que ya haya pasado alguna ves
                            pila.push(listaParaPosfijo);
                            break;
                        }else{
                            asd = (Nodo2)pila.peek();
                        }
                        if (prioridad(asd.token) == prioridad(listaParaPosfijo.token)) {
                            //System.out.println("Prioridad "+ prioridad(asd.token)+" mas "+prioridad(listaParaPosfijo.token));
                            listaPosfijo.sig = new Nodo2(asd.lexema, asd.token);
                            listaPosfijo = listaPosfijo.sig;
                            pila.pop();
                        }else if (prioridad(asd.token) < prioridad(listaParaPosfijo.token)) {
                            //System.out.println("Prioridad "+ prioridad(asd.token)+" mas "+prioridad(listaParaPosfijo.token));
                            pila.push(listaParaPosfijo);
                            break;
                        } else {
                            //System.out.println("Prioridad "+ prioridad(asd.token)+" mas "+prioridad(listaParaPosfijo.token));
                            Nodo2 ayudante = (Nodo2)pila.pop();
                            listaPosfijo.sig = new Nodo2(ayudante.lexema, ayudante.token);
                            listaPosfijo = listaPosfijo.sig;
                        }
                    }
                }
            }
            listaParaPosfijo = listaParaPosfijo.sig;
        }
        while (!pila.empty()) {
            Nodo2 ayudante = (Nodo2)pila.pop();
            listaPosfijo.sig = new Nodo2(ayudante.lexema, ayudante.token);
            listaPosfijo = listaPosfijo.sig;
        }
        if (listaPolishC == null) {
            listaPolishC = listaPosfijoC;
            listaPolish = listaPosfijo;
        }else{
            listaPolish.sig = listaPosfijoC;
            listaPolish = listaPosfijo;
        }
        cambiarVariablesPosfijo();
        verificacionDeTipos();
        //imprimirLista(listaPosfijoC);
        
        listaPosfijoC=null;
    } 
    
    
    public void cambiarVariablesPosfijo(){
        listaPosfijo = listaPosfijoC;
        while (listaPosfijo != null) {
            if (listaPosfijo.token == 100) {
                controlLista = cabezaListaVariables;
                while (true) {
                    //System.out.println(controlLista.lexema+" = "+listaPosfijo.lexema);
                    if (controlLista.lexema.equals(listaPosfijo.lexema)) {
                        if (controlLista.token == 216) {
                            listaPosfijo.token = 101;
                        }
                        if (controlLista.token == 217) {
                            listaPosfijo.token = 102;
                        }
                        if (controlLista.token == 218) {
                            listaPosfijo.token = 120;
                        }
                        break;
                    }else{
                        controlLista = controlLista.sig;
                    }
                }
            }
            listaPosfijo = listaPosfijo.sig;
        }
    }
    
    
    public int prioridad(int prioridadd) {
        int prioridad;
        switch (prioridadd) {
            case 200:
            case 201:
                prioridad = 1;
                break;
            case 202:
                prioridad = 2;
                break;
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
                prioridad = 3;
                break;
            case 103:
            case 104:
                prioridad = 4;
                break;
            case 105:
            case 210:
                prioridad = 5;
                break;
            case 130:
            case 131:
                prioridad = 6;
                break;
            case 126:
                prioridad = 7;
                break;
            case 125:
                prioridad = 0;
                break;
            case 121:
                prioridad = -11;
                break;
            default:
                System.out.println(prioridadd);
                throw new AssertionError();
        }
        return prioridad;
    }
    
    int arregloDeTipos[][]=  {{103,101,101,101},{103,101,102,102},{103,102,101,102},{103,102,102,102},
                                {103,120,120,120},{104,101,101,101},{104,101,102,102},{104,102,101,102},
                                {104,102,102,102},{105,101,101,101},{105,101,102,102},{105,102,101,102},
                                {105,102,102,102},{210,101,101,102},{210,101,102,102},{210,102,101,102},
                                {210,102,102,102},{106,101,101,300},{106,101,102,300},{106,102,101,300},
                                {106,102,102,300},{106,102,120,300},{107,101,101,300},{107,101,102,300},
                                {107,102,101,300},{107,102,102,300},{107,101,101,300},{108,101,102,300},{108,101,101,300},
                                {108,102,101,300},{108,102,102,300},{109,101,101,300},{109,101,102,300},
                                {109,102,101,300},{109,102,102,300},{110,101,101,300},{110,101,102,300},
                                {110,102,101,300},{110,102,102,300},{111,101,101,300},{111,101,102,300},
                                {111,102,101,300},{111,102,102,300},{200,300,300,300},{201,300,300,300},
                                {121,102,102,400},{121,102,101,400},{121,102,102,400},{121,120,120,400},
                                {121,101,101,400},{202,300,0,300},{130,101,0,101},{130,102,0,102},
                                {131,101,0,101},{131,102,0,102}};
    
    public void verificacionDeTipos(){
        
        int op1, op2, operador, resultado=0;
        listaPosfijo = listaPosfijoC;
        while (listaPosfijo != null) {
            if (listaPosfijo.token == 101 || listaPosfijo.token == 102 || listaPosfijo.token == 120) {
                pila.push(listaPosfijo.token);
                op1 = 0;
                op2 = 0;
                operador = 0;
                resultado = 0;
            } else if (listaPosfijo.token == 130 || listaPosfijo.token == 131 || listaPosfijo.token == 202) {
                operador = listaPosfijo.token;
                int apoyo = (int)pila.pop();
                op1 = apoyo;
                op2 = 0;
            } else {
                operador = listaPosfijo.token;
                int apoyo = (int)pila.pop();
                op2 = apoyo;
                apoyo = (int)pila.pop();
                op1 = apoyo;
            }
            if (operador != 0) {
                for (int i = 0; i < arregloDeTipos.length; i++) {
                    //System.out.println(arregloDeTipos[i][0] + " " + arregloDeTipos[i][1] + " " + arregloDeTipos[i][2] + " " + arregloDeTipos[i][3]);
                    if (operador == arregloDeTipos[i][0] && op1 == arregloDeTipos[i][1] && op2 == arregloDeTipos[i][2]) {
                        resultado = arregloDeTipos[i][3];
                        //System.out.println(resultado);
                        break;
                    }
                }
                //System.out.println("Resultado es: "+resultado);
                if (resultado != 0) {
                    pila.push(resultado);
                } else {
                    System.out.println("Error de tipos en: ");
                    imprimirLista(listaPosfijoC);
                    error = false;
                    break;
                }
                op1 = 0;
                op2 = 0;
                operador = 0;
                resultado = 0;
            }
            listaPosfijo = listaPosfijo.sig;
        }
        
    }
}
