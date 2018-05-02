package com.p6majo.plot.bennett;

import java.util.Stack;

public class Functionz {
    private Stack fctStack;     // Function stack in postfix
    private String fctString;   // Function definition in infix as string

    /**
     Standard Constructor
     @param s formula defining the function
     */
    public Functionz(String s) {
        fctString=s;
        fctStack=parseFctz(s);
    }

    /**
     overrides Object toString method
     @return formula defining the function
     */
    public String toString() {
        return fctString;
    }

    // ********************************
    // SUBROUTINES FOR PARSER parseFctz
    // ********************************

    // STRIP WHITE SPACE (of course)
    private char[] strip_whitespace(char[] s) {
        char[] stripped=new char[s.length];
        int count=0;
        for (int i=0;i<s.length;i++) {  // strip white space
            if ( !(s[i]==32 || s[i]==10 || s[i]==13 || s[i]==9) ) {
                stripped[count]=s[i];
                count++;
            }
        }
        if (count>0) {  // copy to new char[] of corrected size
            char[] ret=new char[count];
            for (int i=0;i<count;i++) ret[i]=stripped[i];
            return ret;
        }
        throw new NumberFormatException();
    }

    // CLASSIFY INPUT CHARACTER
    private int classify_char(char c) {
        if ((c>=97 && c<=122) || (c>=65 && c<=90))
            return 0;   // character
        if ((c>=48 && c<=57) || (c==46))
            return 1;   // numerical
        if (c=='+' || c=='-' || c=='*' || c=='/' || c=='^')
            return 2;
        if (c=='(')
            return 3;
        if (c==')')
            return 4;
        return 99;      // error code
    }

    // RANK PRECEDENCE OF OPERATORS
    private int precedence(String s) {
        if (s.equals("^")) return 4;
        if (s.equalsIgnoreCase("chs")) return 3;
        if (s.equals("*") || s.equals("/")) return 2;
        if (s.equals("+") || s.equals("-")) return 1;
        return 0;
    }

    // ***************************************
    // THE MAIN PARSER FUNCTION: parseFctz
    // ***************************************
    private Stack parseFctz(String s) {

        boolean num=false;  // currently processing numeric data flag
        boolean str=false;  // currently processing string data flag
        boolean goodstr=true;   // recognized string flag
        char[] instring=s.toCharArray();    // input as char[]
        int pre;    // precedence value for operator
        Stack parsed=new Stack();   // returned rpn stack
        Stack ops=new Stack();      // stack of operators in conversion
        String temp=new String();   // temp string for token conversion
        String popop=new String();  // dummy for operators popped from ops

        instring=strip_whitespace(instring);
        for (int i=0;i<instring.length;i++) {
            process_char:   // label for break (needed in case 4)
            switch (classify_char(instring[i])) {

                case 0: // character
                    if (str) {
                        temp+=instring[i];
                        break process_char;
                    }
                    if (num) throw new NumberFormatException();
                    temp=new String().valueOf(instring[i]);
                    num=false;
                    str=true;
                    break process_char;

                case 1: // numeric
                    if (num) {
                        temp+=instring[i];
                        break process_char;
                    }
                    if (str) throw new NumberFormatException();
                    temp=new String().valueOf(instring[i]);
                    str=false;
                    num=true;
                    break process_char;

                case 2: // symbol
                    if ( i==0 || (!str && !num && instring[i-1]!=')') ) {
                        // UNARY MODE FROM CONTEXT
                        if ( i>1 && classify_char(instring[i-1])==2 && classify_char(instring[i-2])==2)
                            throw new NumberFormatException();
                        if (instring[i]=='+') break process_char;
                        if (instring[i]=='-') {
                            temp="chs";
                            pre=precedence(temp);
                            if (!ops.isEmpty()) {
                                while (!ops.isEmpty()) {
                                    popop=(String) ops.pop();
                                    if (precedence(popop)>=pre) parsed.push(popop);
                                    else {
                                        ops.push(popop);
                                        break; // while loop
                                    }
                                }
                            }
                            ops.push(temp);
                            break process_char;
                        }
                        throw new NumberFormatException();
                    }
                    // BINARY FUNCTION FROM CONTEXT
                    if (str) {  // variable or constant from context
                        goodstr=false;
                        if (temp.equalsIgnoreCase("z")) {
                            parsed.push("z");
                            goodstr=true;
                        }
                        if (temp.equalsIgnoreCase("e")) {
                            parsed.push("e");
                            goodstr=true;
                        }
                        if (temp.equalsIgnoreCase("pi")) {
                            parsed.push("pi");
                            goodstr=true;
                        }
                        if (temp.equalsIgnoreCase("i")) {
                            parsed.push("i");
                            goodstr=true;
                        }
                        if (!goodstr) throw new NumberFormatException();
                    }
                    if (num) parsed.push(temp);
                    str=false;
                    num=false;
                    temp=new String().valueOf(instring[i]);
                    // UPDATE OPS STACK ACCORDING TO PRECEDENCE
                    pre=precedence(temp);
                    if (!ops.isEmpty()) {
                        while (!ops.isEmpty()) {
                            popop=(String) ops.pop();
                            if (precedence(popop)>=pre) parsed.push(popop);
                            else {
                                ops.push(popop);
                                break; // while loop
                            }
                        }
                    }
                    ops.push(temp);
                    break process_char;

                case 3: // open parenthesis "("
                    if (str) {  // function from context
                        str=false;
                        if (temp.equalsIgnoreCase("sin")) {
                            ops.push("(");
                            ops.push("sin");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("cos")) {
                            ops.push("(");
                            ops.push("cos");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("sinh")) {
                            ops.push("(");
                            ops.push("sinh");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("cosh")) {
                            ops.push("(");
                            ops.push("cosh");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("tan")) {
                            ops.push("(");
                            ops.push("tan");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("exp")) {
                            ops.push("(");
                            ops.push("exp");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("log")) {
                            ops.push("(");
                            ops.push("log");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("ln")) {
                            ops.push("(");
                            ops.push("log");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("sqrt")) {
                            ops.push("(");
                            ops.push("sqrt");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("mod")) {
                            ops.push("(");
                            ops.push("mod");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("abs")) {
                            ops.push("(");
                            ops.push("mod");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("arg")) {
                            ops.push("(");
                            ops.push("arg");
                            temp="(";
                            break process_char;
                        }
                        if (temp.equalsIgnoreCase("conj")) {
                            ops.push("(");
                            ops.push("conj");
                            temp="(";
                            break process_char;
                        }
                        throw new NumberFormatException();
                    }
                    if (num) throw new NumberFormatException();
                    ops.push("(");
                    temp=null;
                    break process_char;

                case 4: // close parenthesis ")"
                    if (!str && !num && !(i>0 && instring[i-1]==')')) throw new NumberFormatException();
                    if (num) parsed.push(temp);
                    if (str) {  // variable or constant from context
                        goodstr=false;
                        if (temp.equalsIgnoreCase("z")) {
                            parsed.push("z");
                            goodstr=true;
                        }
                        if (temp.equalsIgnoreCase("e")) {
                            parsed.push("e");
                            goodstr=true;
                        }
                        if (temp.equalsIgnoreCase("pi")) {
                            parsed.push("pi");
                            goodstr=true;
                        }
                        if (temp.equalsIgnoreCase("i")) {
                            parsed.push("i");
                            goodstr=true;
                        }
                    }
                    num=false;
                    str=false;
                    while (!ops.isEmpty()) { // clear ops stack to previous "("
                        popop=(String) ops.pop();
                        if (!popop.equals("(")) parsed.push(popop);
                        else break process_char; // break from switch (not while)
                    }
                case 99: default:   // error code
                    throw new NumberFormatException();
            }
        }
        // CLEAN UP AFTER LOOP FINISHED
        // clear temp
        if (str) {  // variable or constant from context
            goodstr=false;
            if (temp.equalsIgnoreCase("z")) {
                parsed.push("z");
                goodstr=true;
            }
            if (temp.equalsIgnoreCase("e")) {
                parsed.push("e");
                goodstr=true;
            }
            if (temp.equalsIgnoreCase("pi")) {
                parsed.push("pi");
                goodstr=true;
            }
            if (temp.equalsIgnoreCase("i")) {
                parsed.push("i");
                goodstr=true;
            }
            if (!goodstr) throw new NumberFormatException();
        }
        if (num) parsed.push(temp);
        // clear ops
        while (!ops.isEmpty()) {
            popop=(String) ops.pop();
            if (!popop.equals("(")) parsed.push(popop);
            else throw new NumberFormatException();
        }
        // reverse stack
        Stack reversed=new Stack();
        while (!parsed.isEmpty()) reversed.push(parsed.pop());
        return reversed;
    }

    /**
     Evaluate function at given value
     @param z given value
     @return function value
     */
    public Complex eval(Complex z) {
        //  input p is p value as a Double

        String elem;        // elements of fstack
        boolean goodelem;   // flag for element handled
        Stack tstack=new Stack();   // stack of Complex's for evaluation
        Complex t1,t2;              // tstack values
        Complex e=new Complex(Math.E,0);    // complex e
        Complex pi=new Complex(Math.PI,0);  // complex pi
        Complex i=new Complex(0,1);         // complex i
        Stack fstack=(Stack) fctStack.clone();
        // this clone of fctStack will be destroyed
        // in evaluating the function

        while(!fstack.isEmpty()) {  // run through the function stack
            elem=(String) fstack.pop();
            goodelem=false;
            if (elem.equalsIgnoreCase("z")) {   // z variable
                tstack.push(z);
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("e")) {   // constant e
                tstack.push(e);
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("pi")) {  // constant pi
                tstack.push(pi);
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("i")) {   // constant i
                tstack.push(i);
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("sin")) { // unary function sin
                tstack.push(((Complex) tstack.pop()).sin());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("cos")) { // unary function cos
                tstack.push(((Complex) tstack.pop()).cos());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("tan")) { // unary function tan
                tstack.push(((Complex) tstack.pop()).tan());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("sinh")) {// unary function sinh
                tstack.push(((Complex) tstack.pop()).sinh());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("cosh")) {// unary function cosh
                tstack.push(((Complex) tstack.pop()).cosh());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("exp")) { // unary function exp
                tstack.push(((Complex) tstack.pop()).exp());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("log")) { // unary function log
                tstack.push(((Complex) tstack.pop()).log());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("sqrt")) { // unary function sqrt
                tstack.push(((Complex) tstack.pop()).sqrt());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("chs")) { // unary function -
                tstack.push(((Complex) tstack.pop()).chs());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("arg")) { // unary function arg
                tstack.push(new Complex(((Complex) tstack.pop()).arg(),0));
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("mod")) { // unary function mod
                tstack.push(new Complex(((Complex) tstack.pop()).mod(),0));
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("conj")) { // unary function conj
                tstack.push(((Complex) tstack.pop()).conj());
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("+")) {   // binary function +
                t1=(Complex) tstack.pop();
                t2=(Complex) tstack.pop();
                tstack.push(t2.plus(t1));
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("-")) {   // binary function -
                t1=(Complex) tstack.pop();
                t2=(Complex) tstack.pop();
                tstack.push(t2.minus(t1));
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("*")) {   // binary function *
                t1=(Complex) tstack.pop();
                t2=(Complex) tstack.pop();
                tstack.push(t2.times(t1));
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("/")) {   // binary function /
                t1=(Complex) tstack.pop();
                t2=(Complex) tstack.pop();
                tstack.push(t2.div(t1));
                goodelem=true;
            }
            if (elem.equalsIgnoreCase("^")) {   // binary function ^
                t1=(Complex) tstack.pop();
                t2=(Complex) tstack.pop();
                if (t2.real()!=0 || t2.imag()!=0 || t1.real()<0 || t1.imag()!=0) {
                    tstack.push(t1.times(t2.log()).exp());
                    goodelem=true;
                } else {
                    if (t1.real()>0) {
                        tstack.push(new Complex(0,0));
                        goodelem=true;
                    } else {
                        tstack.push(new Complex(1,0));
                    }
                }
            }
            if (!goodelem) {    /* if not previously handled, must be
	                               a numerical constant */
                tstack.push(new Complex(new Double(elem).doubleValue(),0));
            }
        }
        return (Complex) tstack.pop();
    }
}
