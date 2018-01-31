package com.p6majo.info.aachen;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.StringTokenizer;

/** Klasse Kon (=Konsole)
  * @author  (C) Johannes Grabler, D-86444 Aulzhausen
  * @see <A href="http://www.grabler.de"> Zur Grabler-Hompage </A>
  * @version 08.2004 */
public class Kon  // =Konsole  //extends Thread // Con = registrierter Dateiname
{//CCCCCCCCCCCCC

   //RandomAccessFile - Dateien
   // Zeichen f�r Sonder-Prompt - Cursor bleibt an der Eingabestelle (Ansi.sys)
   final private static String AT  = "@";
   // Default-Zeichen f�r Fehler-Prompt - erscheint bei Falsch-Eingabe
   final private static String ERRP= ">";
   //final public static double E   = Math.E;   // Kreiszahl PI 3.1415...
   //public static byte maxByte()  { return Byte.MAX_VALUE; }
   /** Start-Zeit -- System.currentTimeMillis() */
   final public static long    TIME0     = time();
   /** MIN... liefert den kleinsten Wert des DatenTyps. */
   final public static byte    MIN_BYTE  = Byte.MIN_VALUE;
   /** MAX... liefert den gr��ten Wert des DatenTyps. */
   final public static byte    MAX_BYTE  = Byte.MAX_VALUE;
   final public static short   MIN_SHORT = Short.MIN_VALUE;
   final public static short   MAX_SHORT = Short.MAX_VALUE;
   final public static int     MIN_INT   = Integer.MIN_VALUE;
   final public static int     MAX_INT   = Integer.MAX_VALUE;
   final public static long    MIN_LONG  = Long.MIN_VALUE;
   final public static long    MAX_LONG  = Long.MAX_VALUE;
   final public static float   MIN_FLOAT = Float.MIN_VALUE;
   final public static float   MAX_FLOAT = Float.MAX_VALUE;
   final public static double  MIN_DOUBLE= Double.MIN_VALUE;
   final public static double  MAX_DOUBLE= Double.MAX_VALUE;
   final public static char    MIN_CHAR  = Character.MIN_VALUE; //     0
   final public static char    MAX_CHAR  = Character.MAX_VALUE; // 65635

   /** OUT= System.out */
   final public static PrintStream  OUT= System.out;
   /** ERR= System.err */
   final public static PrintStream  ERR= System.err;
   /** IN= System.in */
   final public static InputStream  IN = System.in;
   /** Eulersche Zahl = 2,7182... = Math.E */
   final public static double  E   = Math.E;
   /** Kreiszahl PI = 3.1415... = Math.PI */
   final public static double  PI  = Math.PI;
   /** CON = "CON" (CONsole) */
   final public static String  CON = "CON";
   /** PRN = "PRN" (PRiNter) */
   final public static String  PRN = "PRN";
   /** LPT1= "LPT1" (LinePrinTer1 - parallel) */
   final public static String  LPT1= "LPT1";
   /** LPT2= "LPT2" (LinePrinTer2 - parallel) */
   final public static String  LPT2= "LPT2";
   /** LPT3= "LPT3" (LinePrinTer3 - parallel) */
   final public static String  LPT3= "LPT3";
   /** COM1= "COM1" (COMmunication1 - serial) */
   final public static String  COM1= "COM1";
   /** COM2= "COM2" (COMmunication2 - serial) */
   final public static String  COM2= "COM2";
   /** COM3= "COM3" (COMmunication3 - serial) */
   final public static String  COM3= "COM3";
   
   /** End of TeXt (3) */
   final public static String  ETX = ""+(char)3;    //"\003";  \0 ... \377 (=255)  C-Kompatibilit�t
   /** BEEP (7) */
   final public static String  BEEP= ""+(char)7;    //"\007";
   /** BELL (7) */
   final public static String  BELL= ""+(char)7;    //"\007";
   /** BackSpace "\b" (8) */
   final public static String  BS  = ""+(char)8;    //"\010";
   /** BackSpace+DELete "\b \b" */
   final public static String  BSDEL="\b \b";       // BackSpace
   /** DeLetE (16) */
   final public static String  DLE = ""+(char)16;   //"\020";
   /** DELete - Entf (127) */
   final public static String  DEL = ""+(char)127;  //"\177";
   /** NewPage (12) */
   final public static String  NP  = ""+(char)12;   //"\014";
   //final public static Stri  FF  = ""+(char)12;   //"\014";
   /** FormFeed "\f" (12) */
   final public static String  FF  = "\f";          // u000c";
   /** Horizontal TABulator "\t" (9) */
   final public static String  TAB = "\t";
   /** Horizontal Tabulator "\t" (9) */
   final public static String  HT  = ""+(char)9;
   /** Vertical Tabulator (11) */
   final public static String  VT  = ""+(char)11;   //"\013";
   /** NewLine "\n" (10) */
   final public static String  NL  = ""+(char)10;   //"\012";
   /** LineFeed "\n" (10) */
   final public static String  LF  = "\n";
   /** Carriage-Return "\r" (13) */
   final public static String  CR  = ""+(char)13;   //"\015";
   /** CarriageReturn+LineFeed "\r\n" */
   final public static String  CRLF= "\r\n";  // System.getProperty("line.separator");
   /** LineFeed+CarriageReturn "\n\r" */
   final public static String  LFCR= "\n\r";  // System.getProperty("line.separator");
   /** ESCape (27) */
   final public static String  ESC = ""+(char)27;   //"\033";

   //final public static String  BSL = "\\";
   //final public static String  QUOT= "\"";
   //final public static String  QUO = "\'";

   //final public static String  UNI177= (char)177 + "";
   //final public static String  UNI219= (char)219 + "";
   //final public static char[]String  UNI177= (char)177 + "";

//   /** Euro - Umrechnung */
//   /** 1 Euro: DEM = 1.95583f (Deutsche Mark) */
//   final public static float  DEM = 1.95583f;
//   /** 1 Euro: BEF = 40.3399f (Belgische France) */
//   final public static float  BEF = 40.3399f;
//   /** 1 Euro: LUF = 40.3399f (Luxemburgische France) */
//   final public static float  LUF = 40.3399f;
//   /** 1 Euro: ESP = 166.386f (Spanische Peseten) */
//   final public static float  ESP = 166.386f;
//   /** 1 Euro: FRF = 6.55957f (Franz�sische France) */
//   final public static float  FRF = 6.55957f;
//   /** 1 Euro: IEP = 0.787564f (Irisches Pfund) */
//   final public static float  IEP = 0.787564f;
//   /** 1 Euro: ITL = 1936.27f (Italienische Lire) */
//   final public static float  ITL = 1936.27f;
//   /** 1 Euro: NLG = 2.20371f (Niederl�ndische Gulden) */
//   final public static float  NLG = 2.20371f;
//   /** 1 Euro: ATS = 13.7603f (�sterreichische Schilling) */
//   final public static float  ATS = 13.7603f;
//   /** 1 Euro: PTE = 200.482f (Portugiesische Peseten) */
//   final public static float  PTE = 200.482f;
//   /** 1 Euro: FIM = 5.94573f (Finnische Marka) */
//   final public static float  FIM = 5.94573f;
//   /** 1 Euro: FMK = 5.94573f (Finnische Marka) */
//   final public static float  FMK = 5.94573f;
//   /** 1 Euro: GRD = 340.716f??? (Griechische Drachmen) */
//   final public static float  GRD = 340.750f;
//
   // java.lang.System.out.println("Hallo!");   out.println("Hallo!");
   // final public static PrintWriter out= new PrintWriter(System.out);  // out.print
   // final public static PrintWriter err= new PrintWriter(System.out);  // err.print
   // in  = new BufferedReader (new InputStreamReader (System.in , "Cp437"));
   // out = new PrintWriter    (new OutputStreamWriter(System.out, "Cp437"), true);
   // err = new PrintWriter    (new OutputStreamWriter(System.err, "Cp437"), true);


   // BufferedReader = Klasse, um gepuffert von der Tastatur einzulesen.
   // private static BufferedReader reader1                    // Deklaration
   // = new BufferedReader(new InputStreamReader(System.in));  // Initialisierung

   private static String codePage= "Cp850";  // Cp850=IBM-Zeichensatz Latin1
   /** Liefert die eingestellte CodePage: Cp850 = Voreinstellung
     * CodePages: http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html */
   public  static String getCodePage(        )  { return codePage;  }
   /** Setzt die CodePage: z.B Cp850, Dos, Cp1252, Windows */
   public  static String setCodePage(String cp)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     if      (cp.equalsIgnoreCase("Win"    ))  { codePage= "Cp1252"; }  // Latin1
     else if (cp.equalsIgnoreCase("Windows"))  { codePage= "Cp1252"; }  // Latin1
     else if (cp.equalsIgnoreCase("Dos"    ))  { codePage= "Cp850" ; }  // Latin1
     else                                      { codePage= cp;  print(""); }
     return "";
   }

   private static String errP= ERRP;  // Prompt-Zeichen bei Falsch-Eingabe
   /** Prompt der bei fehlerhafter Eingabe erscheint abfragen. */
   public static String getPrompt()  { return errP; }
   /** Prompt der bei fehlerhafter Eingabe erscheint setzen. */
   public static String setPrompt(String s)  { errP= s;  return ""; }
   /** Default-Prompt der bei fehlerhafter Eingabe erscheint setzen. Default: ">" */
   public static String setPrompt()  { errP= ERRP;  return ""; }

   /** Prompt auf AT setzen. --  Alternative: setPrompt(Kon.AT) - Kon.AT: "@" */
   public static String  readMenuModeOn ()  { setPrompt(AT);   return ""; }
   /** Prompt auf ERRP setzen. - Alternative: setPrompt(Kon.ERRP) - Kon.ERRP: ">" */
   public static String  readMenuModeOff()  { setPrompt(ERRP);  return ""; }

   /** read... -Methoden ohne Parameter d.h. ohne Eingabe-Prompt */
   public static char     readChar   ()  { return readChar   (""); }
   private static boolean readOK= true;
   public static String   readString ()  { return readString (""); }
   public static String   readEnter  ()  { return readEnter  (""); }
   public static byte     readByte   ()  { return readByte   (""); }
   public static short    readShort  ()  { return readShort  (""); }
   public static int      readInt    ()  { return readInt    (""); }
   public static long     readLong   ()  { return readLong   (""); }
   public static float    readFloat  ()  { return readFloat  (""); }
   public static double   readDouble ()  { return readDouble (""); }
   public static boolean  readBoolean()  { return readBoolean(""); }
   public static boolean  readBoolean(String wahr, String falsch)
                                         { return readBoolean("",wahr,falsch); }
   /** Liest eine Zeile von der Tastatur ein.  s = EingabePrompt<BR>
     * (Enter = ung�ltige Eingabe!) */
   public static String readString(String s)  // Statische Meth. sind objektlos!
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String inputString    = "";
      BufferedReader reader2= null;
      if (errP.equals(AT)) {
         if (readOK==true) { print(s);  ansiSaveCursor(); }
      }else { print(s); }
      if (errP.equals(AT))  { ansiClrEol(); }
      try {
         reader2= new BufferedReader(new InputStreamReader(System.in, codePage));
         //reader2.skip(reader2.available());
         //System.in.flush();
         inputString= reader2.readLine();  // inputString.getBytes(codePage);
         while (inputString.equals("")) {  // Wiederhole bis was eingegeben wurde.
            readOK= false;
            if (errP.equals(AT))  { ansiRestoreCursor();  ansiClrEol(); }
            else if (s.endsWith(errP+errP+errP+errP+errP+errP) && (errP.length()>0)){
               s= s.substring(0, s.length()-6);  println("<Ctrl+C>=Cancel\n");
            }
            if (errP.equals(AT))  {
               ansiRestoreCursor();  ansiClrEol();  inputString= readString(s);
            }else { inputString= readString(s+errP); }  // Wiederhole, falls Fehler
            inputString.getBytes(codePage);   // String umkodieren
         }
         //reader2.close();  // n�tig?
      }catch (UnsupportedEncodingException e) {  // Kodierungs-Fehler abfangen
         System.err.println(e);
      }catch (IOException e) {
         System.err.println("\n" +e +"\n" +e.getMessage() +"\n" +e.toString());
         System.exit(1);                   // Programm mit Fehlercode beenden.
      }catch (Exception e) {
         readOK= false;
         if (errP.equals(AT))  {
            ansiRestoreCursor();  ansiClrEol();  inputString= readString(s);
         }else { inputString= readString(s+errP); }  // Wiederhole, falls Fehler
      }finally {
         if (errP.equals(AT))  { ansiRestoreCursor();  ansiClrEol(); }
         readOK= true;
      }
      return inputString;
   }

   /** String so lange einlesen, bis ein String aus der list eingegeben wird.
     * Strings in der list sind durch Kommas getrennt. Bsp: "S1, S2, S3" */
   public static String readString(String s, String list)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String eingabe= readString(s);
      if ((list.length()==0)||(list.equals(",")))  { return ""; }
      if (list.charAt(list.length()-1)!=',')  { list= list + ","; }  // falls , am list-Ende vergessen wird!
      while (list.indexOf(eingabe + ",") < 0) {
         readOK= false;  eingabe= readString(s);
      }
      readOK= true;  println(eingabe);  return eingabe;
   }

   /** Liest ein String-Array ein.  s = EingabePrompt  (Separatoren: "\t  ") */
   public static String[] readStringArray(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String      eingabe= readString(s);
      StringTokenizer  tk= new StringTokenizer(s);  // if (sAr==null)  {
      String[]     strArr= new String[tk.countTokens()];
      for (int i=0; i < strArr.length; i++)  { strArr[i]= tk.nextToken(); } //while ( tk.hasMoreTokens() )
      return strArr;
   }
   
   /** Text in Token/Worte zerlegen und im String-Array zur�ckgeben.
     * Separatoren: "\r \t \n " */
   public static String[] textToStringArray(String text)  //
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return textToStringArray(text, "\t\n\r ");
   }
   /** text in Token/Worte zerlegen und im String-Array zur�ckgeben.
     * Seperatoren/Trennzeichen k�nnen angegeben werden. */
   public static String[] textToStringArray(String text, String seperators)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((text==null)||(seperators==null))  { return null; }
      //StringTokenizer tk= new StringTokenizer(text);
      StringTokenizer tk= new StringTokenizer(text, seperators);  // delimiter
      String[]    strArr= new String[tk.countTokens()];
      for (int i=0; i < strArr.length; i++)  { strArr[i]= tk.nextToken(); }
      return strArr;
   }

   /** Wartet auf die Enter-Taste.  s = Eingabe-Prompt */
   public static String readEnter(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      BufferedReader reader1= null;
      //try { reader1= new BufferedReader(new InputStreamReader(System.in));
         //reader1.mark(2222);  reader1.reset();  reader1.close();
      //}catch (Exception e)  { /*nix*/  }
      try {
         reader1= new BufferedReader(new InputStreamReader(System.in));  // Initialisierung
         print(s);  reader1.readLine();  //reader1.close();
      }catch (Exception e)  { /*nix*/  }
      //try {System.in.read();} catch(Exception e) {System.err.println(e.toString());}
      return "";
   }
   
   /** Liest einen Wahrheits-Wert ein. <BR>
     * Akzeptierte Eingaben f�r true :  True   Wahr    Ja    J  Yes  Y  1 <BR>
     * Akzeptierte Eingaben f�r false:  False  Falsch  Nein  N  No   N  0   */
   public static boolean readBoolean(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String st= readString(s);
      if (      st.equalsIgnoreCase("True" ) || st.equalsIgnoreCase("Wahr")
         ||     st.equalsIgnoreCase("Ja"   ) || st.equalsIgnoreCase("Yes" )
         ||     st.equalsIgnoreCase("J"    ) || st.equalsIgnoreCase("Y"   )
         ||     st.equalsIgnoreCase("1"    )) {
         return true;
      }else if (st.equalsIgnoreCase("False") || st.equalsIgnoreCase("Falsch")
         ||     st.equalsIgnoreCase("Nein" ) || st.equalsIgnoreCase("No"    )
         ||     st.equalsIgnoreCase("N"    ) || st.equalsIgnoreCase("0"     )){
         return false;
      }else {
         return readBoolean(s+errP);  // Eingabe wiederholen.
      }
   }
   
   /** Liest einen Wahrheits-Wert ein: s=Prompt, wahr/true, falsch/false<BR>
     * Akzeptierte Eingaben siehe 2. und 3. Methoden-Parameter. */
   public static boolean readBoolean(String s, String wahr, String falsch)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String st= readString(s);
      if ((wahr.length()==0) || (falsch.length()==0))  { System.exit(2); }
      if      (st.equalsIgnoreCase( wahr ))  { return true ; }
      else if (st.equalsIgnoreCase(falsch))  { return false; }
      else  { return readBoolean(s+errP, wahr, falsch); }  // Eing. wiederholen
   }

   /** Liest ein einzelnes Zeichen von der Tastatur ein.  s = EingabePrompt */
   public static char readChar(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String st= readString(s);
      if (st.equals("")) { return readChar(s); }  //return 0; }
      else               { return st.charAt(0); }  // Das erstes Eingabe-Zeichen
   }

   /** Liest eine Zeile von der Tastatur und wandelt sie in eine byte-Zahl. */
   public static byte readByte(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try                             { return Byte.parseByte(readString(s)); }
      catch (NumberFormatException e) { max(new Byte("0")); return readByte(s+errP); }
      /* max-Methode mit annonymen Wrapper-Objekt als aktuellen Parameter */
   }

   /** Liest eine Zeile von der Tastatur und wandelt sie in eine short-Zahl. */
   public static short readShort(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try                             { return Short.parseShort(readString(s)); }
      catch (NumberFormatException e) { max(new Short("0")); return readShort(s+errP); }
   }

   /** Liest eine Zeile von der Tastatur und wandelt sie in eine int-Zahl. */
   public static int readInt(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try                             { return Integer.parseInt(readString(s)); }
      catch (NumberFormatException e) { max(new Integer("0")); return readInt(s+errP); }
   }

   /** Liest eine Zeile von der Tastatur und wandelt sie in eine long-Zahl. */
   public static long readLong(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try                             { return Long.parseLong(readString(s)); }
      catch (NumberFormatException e) { max(new Long("0")); return readLong(s+errP); }
   }

   /** Liest eine Zeile von der Tastatur u. verwandelt sie in eine foat-Zahl.*/
   public static float readFloat(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try                             { return Float.parseFloat(readString(s));}
      catch (NumberFormatException e) { max(new Float("0")); return readFloat(s+errP); }
   }

   /** Liest eine Zeile von der Tastatur und wandelt sie in eine double-Zahl. */
   public static double readDouble(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try                             { return Double.parseDouble(readString(s)); }
      catch (NumberFormatException e) { max(new Double("0")); return readDouble(s+errP); }
   }

   private static void max(Object o)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM  // Gibt numerischen Wertebereich bei FalschEingabe aus.
      print(NORMAL);
      if      (o instanceof Byte   )  { println("["+(MIN_BYTE) +" ... "+(MAX_BYTE) +"]"); }
      else if (o instanceof Short  )  { println("["+(MIN_SHORT)+" ... "+(MAX_SHORT)+"]"); }
      else if (o instanceof Integer)  { println("["+(MIN_INT)  +" ... "+(MAX_INT)  +"]"); }
      else if (o instanceof Long   )  { println("["+(MIN_LONG) +" ... "+(MAX_LONG) +"]"); }
      else if (o instanceof Float  )  { println("[-3.4E+38 ... 3.4E+38]"    ); }  //" + (Float.MIN_VALUE)   + ".." + (Float.MAX_VALUE)   + "]"); }
      else if (o instanceof Double )  { println("[-1.79E+308 ... 1.79E+308]"); }  //"[" + (Double.MIN_VALUE)  + ".." + (Double.MAX_VALUE)  + "]"); }
   }

   /** Neue Zeile - NewLine = System.out.print(\n); */
   public static String nl()  { System.out.print("\n");  return ""; }

   /** System.out.println-Ersatz -> korrekte Umlaute in der DOS-Konsole! */
   public static void println(          )  { prinT (    NL); } //; System.out.println(); }
   public static void print  (char     c)  { prinT (c + ""  ); }
   public static void println(char     c)  { prinT (c + NL); }
   public static void print  (char[]   c)  { prinT (c + ""  ); }
   public static void println(char[]   c)  { prinT (c + NL); }
   public static void print  (long     l)  { prinT (l + ""  ); }
   public static void println(long     l)  { prinT (l + NL); }
   public static void print  (double   d)  { prinT (d + ""  ); }
   public static void println(double   d)  { prinT (d + NL); }
   public static void print  (boolean  b)  { prinT (b + ""  ); }
   public static void println(boolean  b)  { prinT (b + NL); }
   public static void print  (Object   o)  { prinT (o + ""  ); }
   public static void println(Object   o)  { prinT (o + NL); }

   //public static void println(String tar,           )  { prinT (tar,     nl()); }
   /** Ausgabe in eine Datei  tar = target = ZielDateiName */
   public static void print  (String tar, char     c)  { prinT (tar, c + ""); }
   public static void println(String tar, char     c)  { prinT (tar, c + CRLF); }
   public static void print  (String tar, char[]   c)  { prinT (tar, c + ""); }
   public static void println(String tar, char[]   c)  { prinT (tar, c + CRLF); }
   public static void print  (String tar, long     l)  { prinT (tar, l + ""); }
   public static void println(String tar, long     l)  { prinT (tar, l + CRLF); }
   public static void print  (String tar, double   d)  { prinT (tar, d + ""); }
   public static void println(String tar, double   d)  { prinT (tar, d + CRLF); }
   public static void print  (String tar, boolean  b)  { prinT (tar, b + ""); }
   public static void println(String tar, boolean  b)  { prinT (tar, b + CRLF); }
   public static void print  (String tar, Object   o)  { prinT (tar, o + ""  ); }
   public static void println(String tar, Object   o)  { prinT (tar, o + CRLF); }
   //public static void print  (String tar, String   s)  { prinT (tar, s + ""  ); }
   //public static void println(String tar, String   s)  { prinT (tar, s + "\n"); }

   private static void prinT(Object str)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try {
         //if (toFile==true)  { out.print(str.toString()); }
         //else {
         PrintWriter out;
         out= new PrintWriter(new OutputStreamWriter(System.out, codePage));
         out.print(str.toString());  out.flush();
         //sBak= new PrintStream(new FileOutputStream(dName));
      }catch (UnsupportedEncodingException e)  { System.err.println(e); }
   }

   private static void prinT(String tar, Object str)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      tar= tar.toUpperCase();  String text= str.toString();
      if ((tar.equals("LPT1")) || (tar.equals("LPT2"))
      ||  (tar.equals("LPT3")) || (tar.equals("PRN" ))) {  // /dev/lp = UNIX
         try {
            println("drucken...  (Drucker eingeschaltet?)");
            FileOutputStream fos= new FileOutputStream(tar);
            PrintWriter      pw = new PrintWriter(fos);
            pw.println(text+"\f");
            pw.close();
         } catch (FileNotFoundException e)  { println(e.toString()); }
      }else if (tar.indexOf("NEW:")==0) {
         tar= tar.substring(4);  tar= tar.trim();
         if (tar.length()>0)  { fileWriteString2(tar, text); }
      }else if (tar.indexOf("ADD:")==0) {
         tar= tar.substring(4);  tar= tar.trim();
         if (tar.length()>0)  { fileAppendString(tar, text); }
         //appendFile(tar.substring(6), str.toString());
      }else {
         tar= tar.trim();
         if (tar.length()>0)  { fileAppendString(tar, text); }
      }
      /*
      PrintWriter out;
      try {
         out= new PrintWriter(new OutputStreamWriter(System.out, codePage));
         out.print(str.toString());  out.flush();  // toString() ist unn�tig
      }catch (UnsupportedEncodingException e)  { System.err.println(e); }
      */
   }

/*
   private static void prinTln(Object str)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try {
         PrintWriter out;
         out= new PrintWriter(new OutputStreamWriter(System.out, codePage));
         out.println(str.toString());  out.flush();
      }catch (UnsupportedEncodingException e)  { System.err.println(e); }
   }
*/

   /** Gibt text mit max. width Zeichen pro Zeile linksb�ndig aus. */
   public static void  printText  (String text, int width)
   { print  (formatTextLeft(text, width, 0)); }
   /** Gibt text mit max. width Zeichen pro Zeile linksb�ndig aus. */
   public static void  printlnText(String text, int width)
   { println(formatTextLeft(text, width, 0)); }

   /** Gibt text mit max. width Zeichen pro Zeile linksb�ndig aus.  xPos = linker Rand */
   public static void  printText  (String text, int xPos, int width)
   { print  (formatTextLeft(text, xPos, width)); }
   /** Gibt text mit max. width Zeichen pro Zeile linksb�ndig aus.  xPos= linker Rand */
   public static void  printlnText(String text, int xPos, int width)
   { println(formatTextLeft(text, xPos, width)); }

   /* Formatiert text mit maximal width Zeichen pro Zeile linksb�ndig. */
   //public static String formatText(String text, int width)
   //{ return  formatText(text, width, 0); }

   /** Formatiert text mit linkem Rand xPos. - F�gt vor jeder Zeile xPos Leerzeichen ein.*/
   public static String formatTextPos(String text0, int xPos)
   { return  formatTextPos(text0, xPos, ' '); }
   /** Formatiert text mit linkem Rand xPos.  xPosChar = Rand-F�ll-Zeichen. */
   public static String formatTextPos(String text0, int xPos, char xPosChar)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     //int xPos2 = (int)xPos;
     String leerZ= "";
     String text2= text0;
     if ((text2==null)||(text2.length()==0)||(xPos<0))  { return ""; }
     for (int i=0; i < xPos; i++)  { leerZ= leerZ + xPosChar; }
     text2= leerZ + text2;
     text2= stringReplace(text2, "\n", leerZ + "\n");
     return  text2;
   }

   /* Formatiert text mit maximal width Zeichen pro Zeile linksb�ndig.  xPos = linker Rand */
   private static String formatText(String text, int xPos, int width)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if (text.length()==0)  { return ""; }
      width= Math.abs(width);   if (xPos<0) { xPos= 0; }
      String text2         = "";
      String word          = "";
      String strxPos       = "";
      int currentLineWidth = 0;
      boolean firstIsSpace = false;
      for (int i=0; i < xPos; i++)  { strxPos= strxPos + " "; }
      if (xPos > 0)  { text2= strxPos; }
      StringTokenizer  st= new StringTokenizer(text, " ", true);
      while (st.hasMoreTokens()) {
         int wordWidth= (word= st.nextToken()).length();
         if ((currentLineWidth + wordWidth <= width) || (width < wordWidth)) {
            text2= text2 + word;
            currentLineWidth= currentLineWidth + wordWidth;
         }else if ((currentLineWidth + word.lastIndexOf('-') + 1 <= width)
            && (word.lastIndexOf('-') > 0))   {
            text2= text2 + word.substring(0, word.lastIndexOf('-')+1);
            word= word.substring(word.lastIndexOf('-')+1, word.length()-1);
            firstIsSpace= (word.charAt(0) == ' ');
            text2= text2 + "\n" + strxPos + (firstIsSpace ? "" : word);
            currentLineWidth= firstIsSpace ? 0 : wordWidth;
         }else if ((currentLineWidth + word.indexOf('-')+1 <= width)
            && (word.indexOf('-') > 0))   {
            text2= text2 + word.substring(0, word.indexOf('-')+1);
            word= word.substring(word.indexOf('-')+1, word.length()-1);
            firstIsSpace= (word.charAt(0) == ' ');
            text2= text2 + "\n" + strxPos + (firstIsSpace ? "" : word);
            currentLineWidth= firstIsSpace ? 0 : wordWidth;
         }else {
            firstIsSpace= (word.charAt(0) == ' ');
            text2= text2 + "\n" + strxPos + (firstIsSpace ? "" : word);
            currentLineWidth= firstIsSpace ? 0 : wordWidth;
         }
      }
      return text2;
   }

   private static String fileSep(String datei)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM  // Datei-Trennzeichen ersetzen
      char sep= (System.getProperty("file.seperator", "\\")).charAt(0);
      datei= datei.replace('\\', sep);  datei= datei.replace('/', sep);
      return datei;
   }

   /** Liest String aus Text-Datei. */
   public static String fileReadString2(String datei)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String s= null;
      StringBuffer str= new StringBuffer("");
      BufferedReader in= null;
      datei= fileSep(datei);
      try {
         in= new BufferedReader(new FileReader(datei));  //in.skip(in.available());
         while ((s= in.readLine()) != null)  { str.append(s); }  // lese Datei
         in.close();
      }catch (IOException e)  { System.err.println(e); }
      return str.toString();
   }

   /** Schreibt String in Text-Datei und gibt bei Erfolg true zur�ck. */
   public static boolean fileWriteString2(String datei, String text)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      datei= fileSep(datei);
      try {
         BufferedWriter out= new BufferedWriter(new FileWriter(datei));
         out.write(text);  out.close();  // schreibe und schlie�e Datei
      }catch (IOException e)  { System.err.println(e);  return false; }
      return true;  // R�ckgabe von true, wenn Schreibvorgang erfolgreich.
   }

   /*
   public static boolean strWriteTextFile(String datei, String text)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      text= changeCrLf(text);  return writeFile(datei, text);
   }
   */

   /** H�ngt Text an eine Text-Datei und gibt bei Erfolg true zur�ck. */
   public static boolean fileAppendString(String datei, String text)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return fileWriteString2(datei, fileReadString2(datei) + text);
   }

   /** F�gt Text bei Position pos in eine Text-Datei und gibt bei Erfolg true zur�ck. */
   public static boolean fileAppendString(String datei, String text, int pos)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      long dSize= (new File(fileSep(datei)).length());
      if      (pos   < 0                )  { pos= 0;          }
      else if (dSize > Integer.MAX_VALUE)  { pos= 0;          }
      else if (pos   > dSize            )  { pos= (int)dSize; }
      StringBuffer alt= new StringBuffer(fileReadString2(datei));
      return  fileWriteString2(datei, (alt.insert(pos, text)).toString());
   }

   /** Erzeugt eine neue Datei mit Leer-String "" und gibt bei Erfolg true zur�ck. */
   public static boolean fileMake(String datei) {return fileWriteString2(datei, "");}

   /** L�scht Datei und gibt bei Erfolg true zur�ck. */
   public static boolean fileDelete(String datei)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(datei))).delete();
   }
   
   /*
   // Gibt alle DateiInfos zur�ck. /
   public static String fileInfos(String datei)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(datei))).toString();
      if (fileExists(datei)) {
         return (new File(fileSep(datei))).toString();
      }else {
         return "";
      }
   }
   */

   /** �berpr�ft, ob die Datei existiert. */
   public static boolean fileExists(String datei)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(datei))).exists();
      //File file=new File("file.txt");
      //file.isDirectory(); file.getName(); file.getAbsolutePath();
   }

   /** �berpr�ft, ob die Datei gelesen werden kann. */
   public static boolean fileCanRead(String datei)
      { return (new File(fileSep(datei))).canRead(); }

   /** �berpr�ft, ob die Datei geschrieben werden kann. */
   public static boolean fileCanWrite(String datei)
      { return (new File(fileSep(datei))).canWrite(); }

   /** �berpr�ft, ob datei eine Datei ist. */
   public static boolean fileIsFile(String datei)
      { return (new File(fileSep(datei))).isFile(); }

   /** �berpr�ft, ob datei ein Verzeichnis ist. */
   public static boolean fileIsDir(String datei)
      { return (new File(fileSep(datei))).isDirectory(); }

   /** �berpr�ft, ob datei versteckt (hidden) ist. */
   public static boolean fileIsHidden(String datei)
      { return (new File(fileSep(datei))).isHidden(); }

   /** Gibt den Datei-Namen zur�ck. */
   public static String fileGetName(String datei)
      { return (new File(fileSep(datei))).getName(); }

   /** Gibt den Pfad zur�ck. */
   public static String fileGetPath(String datei)
      { return (new File(fileSep(datei))).getPath(); }

   /** Gibt den Pfad ausgehend von der Wurzel zur�ck. */
   public static String fileGetAbsolutePath(String datei)
      { return (new File(fileSep(datei))).getAbsolutePath(); }

   /** Zeit der letzten �nderung in Millisekunden ab dem 1. Januar 1970.  0 bei Fehler */
   public static long fileLastModified(String datei)
      { return (new File(fileSep(datei))).lastModified(); }

   /** Zeit der letzten �nderung in Millisekunden ab dem 1. Januar 1970.  0 bei Fehler */
   public static boolean fileSetLastModified(String datei, long msTime)
      { return (new File(fileSep(datei))).setLastModified(Math.abs(msTime)); }

   /** Erzeugt eine neue Datei und gibt bei Erfolg true zur�ck. */
   public static boolean fileNewFile(String datei) {
      try  { return (new File(fileSep(datei))).createNewFile(); }
      catch (IOException e)  { System.err.println(e.toString()); return false; }
   }

   /** Erstellt ein Unterverzeichnis - Make-Direktory */
   public static boolean fileMkDir(String dir)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(dir))).mkdir();
   }

   /** Erstellt ein Unterverzeichnis - Make-Direktory */
   public static boolean fileMkDirs(String dir)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(dir))).mkdirs();
   }

   /** Listet Dateien, die in einem Verzeichnis liegen. */
   public static String[] fileList(String dir)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(dir))).list();
   }

   /** Listet Dateien, die in einem Verzeichnis liegen.*/
   public static File[] fileList2(String dir)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(dir))).listFiles();
   }
   
   /** Gibt die Datei-Gr��e zur�ck. */
   public static long fileSize(String datei)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(datei))).length();
   }

   /** readOnly-DateiAttribut setzen. */
   public static boolean fileSetReadOnly(String datei)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(datei))).setReadOnly();
   }

   /** Laufwerke auflisten. Liefert Wurzeln der Dateisysteme oder null.*/
   public static File[] fileDriveList(String datei)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(datei))).listRoots();
   }

   /** Gibt das Arbeitsverzeichnis zur�ck. */
   public static String getUserDir()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      //return System.getProperty("getAbsolutePath", "");
      return System.getProperty("user.dir", "");
   }

   /** Gibt den angemeldeten Benutzer zur�ck. */
   public static String getUserName()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      //return System.getProperty("getAbsolutePath", "");
      return System.getProperty("user.name", "");
   }

   /** Datei umbenennen und bei Erfolg true zur�ckgeben. */
   public static boolean fileRename(String alt, String neu)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (new File(fileSep(alt))).renameTo(new File(fileSep(neu)));
   }

   //public static void clrScr()  { cls(); }  // CLeaR-SCReen = 25 Leerzeilen
   /** cls=ClearScreen mit "\n\n\n..." + Cursor nach links-unten setzen */
   public static String cls()
   {//MMMMMMMMMMMMMMMMMMMMMMM
      //printlns(25);  //for (int i=0; i<25; i++)  { System.out.println(); }
      System.out.print("\n\n\n\n\n\n\n\n\n\n \n\n\n\n\n\n\n\n\n\n \n\n\n\n\n");
      return "";
   }
   /** Cursor zum Zeilen-Anfang  (ohne Ansi.sys) */
   public static String home() { System.out.print(HOME);  return ""; }
   
   /** Cursor zum Zeilen-Anfang + l�schen  (ohne Ansi.sys) */
   public static String delHome()
   { print("\b");   for (int i=0; i < 20; i++) { print(" \b\b \b\b \b\b \b\b \b\b"); };   return ""; }


   /** n Leerzeilen ausgeben. */
   public static String printLines(int n)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      for (int i=1; i <= n; i++)  { System.out.println(); }  // Leerzeilen ausg.
      return "";
   }

   /** System-Info: OS-Architektur, OS-Name, OS-Version */
   public static String systemInfo()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (System.getProperty("os.arch"   , "?") + " /\\ "
      +       System.getProperty("os.name"   , "?") + " /\\ "
      +       System.getProperty("os.version", "?") );
   }

   /** Alle Java-JRE, Java-JREs und Java-VM, Java-VMs Infos <BR>
     * (4-InfoZeilen) */
   public static String javaInfo()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (""
      + javaInfoJRE() + "\n" + javaInfoJREs() + "\n" + javaInfoVM() + "\n" + javaInfoVMs());
   }
   /** Java-RunTimeEnvironment JRE-Info */
   public static String javaInfoJRE()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (System.getProperty("java.version"   , "?") + " /\\ "
      +       System.getProperty("java.vendor"    , "?") + " /\\ "
      +       System.getProperty("java.vendor.url", "?"));
   }
   /** Java-RunTimeEnvironment JRE-Specification-Info */
   public static String javaInfoJREs()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (System.getProperty("java.specification.name"   , "?") + " /\\ "
      +       System.getProperty("java.specification.version", "?") + " /\\ "
      +       System.getProperty("java.specification.vendor" , "?"));
   }
   /** Java-VirtualMachine VM-Info */
   public static String javaInfoVM()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (System.getProperty("java.vm.name"   , "?") + " /\\ "
      +       System.getProperty("java.vm.version", "?") + " /\\ "
      +       System.getProperty("java.vm.vendor" , "?"));
   }
   /** Java-VirtualMachine VM-Specification-Info */
   public static String javaInfoVMs()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return (System.getProperty("java.vm.specification.name"   , "?") + " /\\ "
      +       System.getProperty("java.vm.specification.version", "?") + " /\\ "
      +       System.getProperty("java.vm.specification.vendor" , "?"));
   }

   /** System-Info: Alle System-Informationen */
   public static String systemInfoAll()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return ((System.getProperties()).toString());
   }

   /** Zeit seit Programm-Start in Milli-Sekunden anfordern */
   public static long time()
   {//MMMMMMMMMMMMMMMMMMMMMM
      return System.currentTimeMillis();
   }

   /** Zeit seit Programm-Start in ganzen Sekunden anfordern */
   public static long timeSec()
   {//MMMMMMMMMMMMMMMMMMMMMMMMM
      return System.currentTimeMillis()/1000;
   }

   private static AudioClip clip= null;

   /** WAV- und MIDI-Dateien abspielen (Spieldauer) */
   public static String soundStart(int dauer)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if (clip!=null) { clip.play(); wait(dauer); clip.stop(); }   return "";
   }
   /** WAV- und MIDI-Dateien starten */
   public static String soundStart() { if (clip!=null) {clip.play();} return ""; }
   /** WAV- und MIDI-Dateien stoppen */
   public static String soundStop()  { if (clip!=null) {clip.stop();} return ""; }
   /** WAV- und MIDI-Dateien starten mit Endlosschleife */
   public static String soundLoop()  { if (clip!=null) {clip.loop();} return ""; }

   /** WAV- und MIDI-Dateien abspielen. <BR>
     * Java 1.2 unterst�tzt z.B. Audio (AIFF, AU, WAV) und
     * MIDI (Type0 MIDI, Type1 MIDI, RMF) */
   public static String sound(String datei)  { sound(datei, 1);  return ""; }
   /** WAV- und MIDI-Dateien abspielen (dauer=Spieldauer) */
   public static String sound(String datei, long dauer)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if (fileExists(datei)) {
         /*
         if (dauer <= 0) {
            try {
               ThreadSound sd= new ThreadSound(datei);
               ///Thread t= new Thread(sd);
               if      (dauer==0)  { sd.setDaemon(true); }
               else if (dauer< 0)  { sd.setDaemon(false); }
               sd.start();  //NoClassDefFoundError
            }catch (Exception e)  { System.err.println("sound: "+e.toString()); }
         }else {
         */
         try {
            URL url= new URL("file:///" + datei);  // getAudioClip l�dt nicht
            clip= Applet.newAudioClip(url);  if (dauer > 0)  { clip.play(); }
            try  { Thread.sleep(Math.abs(dauer)*1000); }
            catch (InterruptedException e) { clip= null; System.out.println(e.toString()); }
         }catch (MalformedURLException e) { clip= null; System.out.println(e.toString()); }
      }else  {
         clip= null;  println("Datei:  " + datei + "  nicht gefunden!");  beep(2);
      }
      return "";
   }
   
/*
   static class ThreadSound extends Thread // implements Runnable
   {//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
      private String datei= "";
      public ThreadSound(String datei)  { this.datei= datei; }
      public void run()  { Kon.sound(datei, 1); }
   }
*/

   /** ms Milli-Sekunden warten. */
   public static String wait(int ms)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      //AudioClip a= newAudioClip("Klirr.wav");
      //a.play();
      long zeit= System.currentTimeMillis();
      while (ms > (System.currentTimeMillis()-zeit))  {;}
      return "";
   }

   /** Thread schlafend legen.  ms = Milli-Sekunden  */
   public static String sleep(long ms)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try                             { Thread.sleep(Math.abs(ms)); }
      catch (InterruptedException e)  { System.out.println(e.toString()); }
      finally  { return ""; }
   }
   
   /** Pieps-Ton ausgeben und 500ms warten */
   public static String beep() {print(BEEP); wait(500); System.out.flush(); return "";}
   /** n Pieps-T�ne ausgeben und jeweils 500ms warten */
   public static String beep(int n) {
     for (int i=0; i<n; i++)  { print(BEEP); wait(500); System.out.flush(); }
     return "";
   }

   /** Zeichen nach links l�schen "\b \b" */
   public static String backSpace()  { System.out.print("\b \b");  return ""; }
   /** n Zeichen nach links l�schen "\b \b..." */
   public static String backSpace(int n)
      { for (int i=0; i<n; i++) { System.out.print("\b \b"); };    return ""; }
      
   /** Uhrzeit anfordern HH:MM */
   public static String getTime()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMM
      GregorianCalendar cal= new GregorianCalendar();  // Initialisierung mit aktuellem Datum
      return (cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
   }

   /** Datum anfordern DD.MM.YY */
   public static String getDate()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMM
      GregorianCalendar cal= new GregorianCalendar();  // Initialisierung mit aktuellem Datum
      return (cal.get(Calendar.DAY_OF_MONTH)
      + "." + (cal.get(Calendar.MONTH)+1) + "." + cal.get(Calendar.YEAR));
   }

   /** Alle Datums- und Zeit-Informationen anfordern. */
   public static String getDateAll()
      { return (new GregorianCalendar()).toString(); }

   /** Liefert die TagesNummer des aktuellen Datums [1..31] -  Day.Month.Year */
   public static int getDay    ()     { return getDate(Calendar.DAY_OF_MONTH); }
   /** Liefert die WochentagsNummer des aktuellen Datums [1..7] 1=Sonntag */
   public static int getWeekDay()     { return getDate(Calendar.DAY_OF_WEEK);  }
   /** Liefert die TagesNummer des aktuellen Datums [0..365] -  Day.Month.Year */
   public static int getYearDay()     { return getDate(Calendar.DAY_OF_YEAR);  }
   /** Liefert die WochenNummer des aktuellen Datums [1..52] */
   public static int getWeek   ()     { return getDate(Calendar.WEEK_OF_YEAR);  }
   /** Liefert die MonatsNummer des aktuellen Datums [1..12] -  Day.Month.Year */
   public static int getMonth  ()     { return getDate(Calendar.MONTH)+1;      }
   /** Liefert die JahresZahl des aktuellen Datums -  Day.Month.Year */
   public static int getYear   ()     { return getDate(Calendar.YEAR);         }
   /** Liefert die Stuenden H der momentanen Uhrzeit -  H:M:s:ms */
   public static int getHour   ()     { return getDate(Calendar.HOUR);         }
   /** Liefert die Minuten M der momentanen Uhrzeit -  H:M:s:ms */
   public static int getMinute ()     { return getDate(Calendar.MINUTE);       }
   /** Liefert die Sekunden s der momentanen Uhrzeit -  H:M:s:ms */
   public static int getSecond ()     { return getDate(Calendar.SECOND);       }
   /** Liefert die Millisekunden ms der momentanen Uhrzeit -  H:M:s:ms */
   public static int getMilliSecond() { return getDate(Calendar.MILLISECOND);  }

   /** Datums- oder Zeit-Information mit der Nummer nr anfordern. */
   private static int getDate(int nr)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      GregorianCalendar cal= new GregorianCalendar();  return cal.get(nr);
   }

   /** Zufallszahl aus dem gesamten Werte-Bereich des Datentyps */
   public static byte   randomByte  () { return (byte) Math.round(random(Byte.MIN_VALUE, Byte.MAX_VALUE)); }
   public static short  randomShort () { return (short)Math.round(random(Short.MIN_VALUE, Short.MAX_VALUE)); }
   public static int    randomInt   () { return (int)  Math.round(random(Integer.MIN_VALUE, Integer.MAX_VALUE)); }
   public static long   randomLong  () { return (long) Math.round(random(Long.MIN_VALUE, Long.MAX_VALUE)); }
   public static float  randomFloat () { return (float) random(Float.MIN_VALUE, Float.MAX_VALUE); }
   public static double randomDouble() { return (double)random(Double.MIN_VALUE, Double.MAX_VALUE); }
   public static char   randomChar  () { return (char)  random(0, 65535); } //Character.MAX_VALUE); }
   //public static char   AsciiRandom () { return (char)  random(0, 255); }

   /** Zufallszahl von 0 bis max */
   public static byte   randomByte  (double max) { return (byte) Math.round(random(0, max)); }
   public static short  randomShort (double max) { return (short)Math.round(random(0, max)); }
   public static int    randomInt   (double max) { return (int)  Math.round(random(0, max)); }
   public static long   randomLong  (double max) { return (long) Math.round(random(0, max)); }
   public static float  randomFloat (double max) { return (float) random(0,   max); }
   public static double randomDouble(double max) { return (double)random(0,   max); }
   public static char   randomChar  (double max) { return (char)  random(0, max); } //Character.MAX_VALUE=65540
   public static String randomString(int laenge) { return randomString(laenge, 32, 255); }
   public static String randomString(int laenge, double min, double max) {
      if (max==0)  { return ""; }
      else {
         int sLenge= (int)Math.abs(laenge);
         StringBuffer sBuf= new StringBuffer(sLenge);
         for (int i=0; i<sLenge; i++) { sBuf.append(randomChar(min, max)); }
         return sBuf.toString();
      }
   }

   //Character.MAX_VALUE=65540
   //public static char   AsciiRandom (char max) { return (char)  random(0, max); }

   /** Zufallszahl von min bis max */
   public static byte   randomByte  (double min, double max) { return (byte) Math.round(random(min, max)); }
   public static short  randomShort (double min, double max) { return (short)Math.round(random(min, max)); }
   public static int    randomInt   (double min, double max) { return (int)  Math.round(random(min, max)); }
   public static long   randomLong  (double min, double max) { return (long) Math.round(random(min, max)); }
   public static float  randomFloat (double min, double max) { return (float) random(min,   max); }
   public static double randomDouble(double min, double max) { return (double)random(min,   max); }
   public static char   randomChar  (double min, double max) { return (char)  random(min, max); }
   //public static char   AsciiRandom (char   min, char max)   { return (char)  random(min, max); }
   public static boolean randomBoolean()
      { Random rd= new Random();  return rd.nextBoolean(); }
      //{ return random(0, 1)<0.5 ? false : true; }

   //public static double random(double max)  { return random(0, max); }
   private static double random(double min, double max)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if (max == min) { return 0; };
      if (max < min) { double bak= max;  max= min;  min= bak; }
      return ((Math.random()*(max-min)) + min);
      //Random rd= new Random();  return (rd.nextDouble()*(max-Math.abs(min))+min);
   }
   
   /** n! -- Fakult�t von n (n=[0..23]) Bei Fehler wird -1 zur�ckgegeben. */
   public static long mathFak(long n) {
      long fak= 1L;
      if ((n<0)||(n>23))  {                            fak= -1;      }
      else                { for (int i=2; i<=n; i++) { fak= fak*i; } }
      return fak;
   }

   /** n! -- Fakult�t von n (n=[0..170]) Bei Fehler wird -1 zur�ckgegeben. */
   public static double mathFak2(long n) {
      double fak= 1.0;
      if ((n<0)||(n>170))  {                            fak= -1;      }
      else                 { for (int i=2; i<=n; i++) { fak= fak*i; } }
      return fak;
   }

   // auf- / ab-runden
   //public static long round(double zahl)  { return Math.round(zahl); }
   /** Logarithmus von zahl zur Basis base */
   public static double mathLog  (double zahl, double base)  { return Math.log(zahl)/Math.log(base); }  // Logarithmus zur Basis base
   /** Logarithmus von zahl zur Basis 10 */
   public static double mathLog10(double zahl)  { return Math.log(zahl)/Math.log(10); }  // lg() Logarithmus zur Basis 10
   /** Logarithmus von zahl zur Basis e -- nat�rlicher Logarithmus wie ln(?) */
   public static double mathLogE (double zahl)  { return Math.log(zahl); }               // ln() Logarithmus zur Basis e
   /** Logarithmus von zahl zur Basis e -- nat�rlicher Logarithmus wie logE(?) */
   public static double mathLn   (double zahl)  { return Math.log(zahl); }               // ln() Logarithmus zur Basis e
   /** Logarithmus von zahl zur Basis 10 -- wie log10(?) */
   public static double mathLg   (double zahl)  { return Math.log(zahl)/Math.log(10); }  // lg() Logarithmus zur Basis 10

   /** power - Potenz-Funktion:  base hoch exp (Basis hoch Exponent) */
   public static double mahtPow  (double base, double exp)  { return Math.pow(base, exp); }  // basis hoch exponent
   /** power - Potenz-Funktion:  e hoch exp (entspricht Math.exp(?) */
   public static double mathPowE (double exp)   { return Math.pow(Math.E, exp); } // oder Math.exp(exp)  // basis hoch e
   /** power - Potenz-Funktion:  e hoch exp (entspricht Math.powE(exp) */
   public static double mathExp  (double exp)   { return Math.exp(exp); } // oder Math.exp(exp)  // basis hoch e
   /** power - Potenz-Funktion:  2 hoch exp */
   public static double mahtPow2 (double exp)   { return Math.pow(     2, exp); }            // basis hoch e
   /** power - Potenz-Funktion:  10 hoch exp */
   public static double mahtPow10(double exp)   { return Math.pow(    10, exp); }            // basis hoch 10
   //public static double exp  (double radikant)          { return Math.exp(radikant); }

   /** Wurzel-Funktion: wExp-te Wurzel aus radiant (wExp=WurzelExponent */
   public static double mahtRoot  (double radikant, double wExp) { return Math.pow(radikant, 1.0/wExp+0.0); }  // n-te Wurzel von radikant
   /** Wurzel-Funktion: e-te Wurzel aus radiant */
   public static double mahtRootE (double radikant)  { return Math.pow (radikant, 1.0/Math.E); } // e-te Wurzel aus radikant
   /** Wurzel-Funktion: Quadrat-Wurzel aus radiant = square-root  wie root2(?)) */
   public static double mahtSqrt  (double radikant)  { return Math.sqrt(radikant); }             // Quadrat-Wurzel  / square-root
   /** Wurzel-Funktion: Quadrat-Wurzel aus radiant  wie sqrt(?) */
   public static double mahtRoot2 (double radikant)  { return Math.sqrt(radikant); }             // Quadrat-Wurzel  / square-root
   /** Wurzel-Funktion: Kubik-Wurzel aus radiant */
   public static double mahtRoot3 (double radikant)  { return Math.pow (radikant, 1.0/3.0); }    // Kubik-Wurzel    / cube-root
   /** Wurzel-Funktion: 10. Wurzel aus radiant */
   public static double mahtRoot10(double radikant)  { return Math.pow (radikant, 1.0/10.0); }   // 10-te Wurzel


   // asin, acos, atan,
   ///** sin-Hypbelfunktion sinh x= (e^x-e^-x)/2 */
   //public static double sinh(double x)  { return (powE(x)-powE(x))/2.0; }
   ///** cos-Hypbelfunktion cosh x= (e^x+e^-x)/2 */
   //public static double cosh(double x)  { return (powE(x)+powE(x))/2.0; }
   ///** tan-Hypbelfunktion tanh x= (e^x-e^-x)/(e^x+e^-x) */
   //public static double tanh(double x)  { return (powE(x)-powE(x))/(powE(x)+powE(x)); }
   ///** cot-Hypbelfunktion coth x= (e^x+e^-x)/(e^x-e^-x) */
   //public static double coth(double x)  { return (powE(x)+powE(x))/(powE(x)-powE(x)); }

   /** coTangens =  1/tan  cos/sin */
   public static double mahtCot(double a)  { return 1.0/Math.tan(a); }
   
   /** Pythagoras: c�= a�+b� - b wird berechnet. */
   public static double pythagorasB (double c, double a)  { return pythagorasA(c, a); }
   /** Pythagoras: c�= a�+b� - a wird berechnet. */
   public static double pythagorasA (double c, double b)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     if ((c<=0)||(b<=0)||(c==b)) { println("PythagorasAB-ERROR!");  return -1; }
     else                        { return Math.sqrt(Math.abs(c*c - b*b)); }
   }

   /** Pythagoras: c�= a�+b� - c wird berechnet. */
   public static double pythagorasC (double a, double b)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((a<=0)||(b<=0))  { println("PythagorasC-ERROR!");  return -1; }
      else                 { return Math.sqrt(a*a + b*b); }
   }

   /** H�hensatz:  h�= p*q  -  h wird berechnet. */
   public static double pythagorasH (double p, double q)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((p<=0)||(q<=0))  { println("PythagorasH-Fehler!");  return -1; }
      else                 { return Math.sqrt(p*q); }
   }
   /** H�hensatz:  h�= p*q  -  q wird berechnet. */
   public static double pythagorasQ (double h, double p)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((h<=0)||(p<=0))  { println("PythagorasQ-Fehler!");  return -1; }
      else                 { return (h*h/p); }
   }
   /** H�hensatz:  h�= p*q  -  p wird berechnet. */
   public static double pythagorasP (double h, double q)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((h<=0)||(q<=0))  { println("PythagorasP-Fehler!");  return -1; }
      else                 { return (h*h/q); }
   }

   /** Kathetensatz (Euklid):  a�= c*p */
   public static double pythagorasKA (double c, double p)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((c<=0)||(p<=0))  { println("PythagorasKA-Fehler!");  return -1; }
      else                 { return (Math.sqrt(c*p)); }
   }
   /** Kathetensatz (Euklid):  b�= c*q */
   public static double pythagorasKB (double c, double q)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((c<=0)||(q<=0))  { println("PythagorasKB-Fehler!");  return -1; }
      else                 { return (Math.sqrt(c*q)); }
   }
   /** Kathetensatz (Euklid):  c= b�/q  oder  c= a�/p */
   public static double pythagorasKC (double ab, double pq)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((ab<=0)||(pq<=0))  { println("PythagorasKC-Fehler!");  return -1; }
      else                   { return ((ab*ab)/pq); }
   }

   /** Pythagoras mit vorgeschriebener Parameter-Reihenfolge a, b, c
     * Der zu berechnende Wert wird mit 0 in der ParameterListe angegeben.
     * Bei Fehler wird -1 zur�ckgegeben.*/
   public static double pythagoras(double a, double b, double c)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if      ((a<=0)&&(b>0)&&(c>0)&&(b<c))  { return pythagorasA(b, c); }  //&&(b<c)
      else if ((b<=0)&&(a>0)&&(c>0)&&(a<c))  { return pythagorasB(a, c); }
      else if ((c<=0)&&(a>0)&&(b>0)       )  { return pythagorasC(a, b); }
      else { println("Pythagoras-Fehler!");  return -1; }
   }
   /* Pythagor�ische Zahlen:   3    4    5    (a, b, c)
                               6    8   10
                              12   16   20    usw.
   */



//////////////////////////////////// ANSI.SYS muss geladen sein! ///////////////
/// CONFIG.SYS-Datei:  DEVICE=C:\???\ANSI.SYS //////////////////////////////////
/// Bei Windows NT funktioniert's angeblich nicht!? .... unter Linux schon!? ///

   private static int  tColor= 0, bColor= 0,  tAttribute= 0;
   /** Ganzzahlige Farb-Konstanten f�r Ansi.SYS-Anzeige */
   final private static int
      _NORMAL  = 0,  _HELL     = 1,  _DUNKEL = 2,  _KURSIV    = 3,  _UNTERSTRICHEN= 4,
      _RESET   = 0,  _BRIGHT   = 1,  _DARK   = 2,  _ITALIC    = 3,  _UNDERLINE    = 4,
      _BLINKEND= 5,  _BLINKEND2= 6,  _REVERSE= 7,  _UNSICHTBAR= 8,
      _BLINK   = 5,  _BLINK2   = 6,                _HIDDEN    = 8,  // 1=Bold
      //////////////////////////////////////////////////////////////////////////
      _SCHWARZ  = 0,    _ROT         = 1,    _GRUEN      = 2,    _BRAUN    = 3,
      _BLACK    = 0,    _RED         = 1,    _GREEN     = 2,    _BROWN    = 3,
      _BLAU     = 4,    _MAGENTA     = 5,    _CYAN      = 6,    _HELLGRAU = 7,
      _BLUE     = 4,                                            _LIGHTGRAY= 7,
      _GRAU     = 100,  _HELLROT     = 101,  _HELLGRUEN  = 102,  _GELB     = 103,
      _GREY     = 100,  _LIGHTRED    = 101,  _LIGHTGREEN= 102,  _YELLOW   = 103,
      _HELLBLAU = 104,  _HELLMAGENTA = 105,  _HELLCYAN  = 106,  _WEISS    = 107,
      _LIGHTBLUE= 104,  _LIGHTMAGENTA= 105,  _LIGHTCYAN = 106,  _WHITE    = 107,
      _UNDEF    = 0;

   /** Ansi.sys Escape-Sequenzen (String) (BLUE0, BLUE01 sind z.B. Hintergrund-Farben) <BR>
     *                                 (Ansi.sys) muss geladen sein! <BR>
     * CONFIG.SYS-Datei:  DEVICE=C:\Pfad???\ANSI.SYS <BR>
     * Bei Windows NT funktioniert's angeblich nicht!? .... unter Linux schon!? */
   final public static String
      //CLS1     = "\u001b[2J",      HOME1     = "\u001b[H",
      NORMAL     = "\u001b[0m",
      BOLD       = "\u001b[1m",      DARK     = "\u001b[2m",
      ITALIC     = "\u001b[3m",      UNDERLINE= "\u001b[4m",
      BLINK      = "\u001b[5m",      BLINK2   = "\u001b[6m",
      REVERSE    = "\u001b[7m",      HIDDEN   = "\u001b[8m",
      BRIGHT     = "\u001b[2m",      AT55     = "\u001b[10;10H",
      /////////////////////////////////////////////////////////////////////////
      // 0= Background
      BLACK0     = "\u001b[40m",     RED0     = "\u001b[41m", //0=Background
      GREEN0     = "\u001b[42m",     BROWN0   = "\u001b[43m",
      BLUE0      = "\u001b[44m",     MAGENTA0 = "\u001b[45m",
      CYAN0      = "\u001b[46m",     GRAY0    = "\u001b[47m",
      // 01= Background-Color mit Aufhellung  5=blink
      BLACK01    = "\u001b[5;40m",   RED01     = "\u001b[5;41m", ORANGE0  = RED01,
      GREEN01    = "\u001b[5;42m",   BROWN01   = "\u001b[5;43m", YELLOW0  = BROWN01,
      BLUE01     = "\u001b[5;44m",   MAGENTA01 = "\u001b[5;45m", DARKGREY0= BLACK01,
      CYAN01     = "\u001b[5;46m",   GRAY01    = "\u001b[5;47m", WHITE0   = GRAY01,  LIGHTBLUE0= BLUE01,
      // Text-Color normal
      BLACK      = "\u001b[30m",     RED      = "\u001b[31m",
      GREEN      = "\u001b[32m",     BROWN    = "\u001b[33m",
      BLUE       = "\u001b[34m",     MAGENTA  = "\u001b[35m",    LILAC= MAGENTA, VIOLET= MAGENTA,
      CYAN       = "\u001b[36m",     GRAY     = "\u001b[37m",
      // Text-Color mit Aufhellung - 1=fett/bold
      BLACK1     = "\u001b[1;30m",   RED1     = "\u001b[1;31m",  DARKGRAY= BLACK1,  ORANGE= RED1,
      GREEN1     = "\u001b[1;32m",   BROWN1   = "\u001b[1;33m",  YELLOW= BROWN1,
      BLUE1      = "\u001b[1;34m",   MAGENTA1 = "\u001b[1;35m",
      CYAN1      = "\u001b[1;36m",   GRAY1    = "\u001b[1;37m",  WHITE= GRAY1;
      /* Text-Color mit Aufhellung - 2=hell/bright
      BLACK2     = "\u001b[2;30m",   RED2     = "\u001b[2;31m",
      GREEN2     = "\u001b[2;32m",   BROWN2   = "\u001b[2;33m",
      BLUE2      = "\u001b[2;34m",   MAGENTA2 = "\u001b[2;35m",
      CYAN2      = "\u001b[2;36m",   GRAY2    = "\u001b[2;37m";
      */

   //public static String setTextColor(int tCol)
   //{//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
   //   setTextColor(tCol, tAttribute);  return "";
   //}

   /** Text-Farbe anfordern.  (Ansi.sys) */
   public static int    ansiGetTextColor()  { return tColor; }
   /** Text-Farbe setzen.  (Ansi.sys) */
   public static String ansiSetTextColor(int tCol)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     String tHell= "";
     if (tCol>100)  { tHell= "2;";  tCol= tCol-100; }
     if ((tCol>=0) && (tCol<=7)) { // && (tAtt>=0) && (tAtt<=9)) {
        tColor= tCol+30;
        System.out.print("\u001B[" + tHell + tColor + "m");
        //out.print("\u001B[" + tAtt + ";" + tHell + tCol + ";" + bColor + "m");
     }
     return "";
   }

   /** Hintergrund-Farbe anfordern.  (Ansi.sys) */
   public static int    ansiGetBackColor()  { return bColor; }
   /** Hintergrund-Farbe setzen.  (Ansi.sys) */
   public static String ansiSetBackColor(int bCol)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     if (bCol>100)  { bCol= bCol-100; }
     if ((bCol>=0) && (bCol<=7)) {
        bColor= bCol + 40;  // Background-Farben haben 10 mehr.
        System.out.print("\u001B[" + bColor + "m");
        //out.print("\u001B[" + tColor + ";" + bColor + "m");
     }
     return "";
   }

   /** TextAttribute anfordern  (Ansi.sys) */
   public static int    ansiGetTextAttribute()  { return tAttribute; }
   /** TextAttribute setzen.  (Ansi.sys) */
   public static String ansiSetTextAttribute(int tAtt)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     if ((tAtt>=0) && (tAtt<=9)) {
        tAttribute= tAtt;
        System.out.print("\u001B[" + tAttribute + "m");
        //out.print("\u001B[" + tAtt + ";" + tColor + ";" + bColor + "m");
     }
     return "";
   }

   /** Auf Schwarz/Wei� bzw. Normal-Darstellung umschalten.  (Ansi.sys) */
   public static String ansiSetNormal()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      tColor= 0; bColor= 0; tAttribute= 0;
      System.out.print("\u001B[0m");  return "";
   }

   /** text- u. background-Farbe setzen.  (Ansi.sys) */
   public static String ansiSetColors(int tCol, int bCol)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     if ((tCol>=0) && (tCol<=7) && (bCol>=0) && (bCol<=7)) {
        tColor= tCol;  bColor= bCol+10;
        ansiSetBackColor(bColor);   ansiSetTextColor(tColor);
     }
     return "";
   }
   
   /** Cursor absolut positionieren. (Ansi.sys) */
   public static String ansiGoToXY(int x, int y) {
      //if (x<0) { x= 0; }   if (y<0) { y= 0; };
      /* au�erhalb des g�ltigen Bereichs wird ignoriert */
      System.out.print("\u001B[" + y + ";" + x + "H");  // funktionierte nicht
      System.out.print("\u001B[" + y + ";" + x + "f");
      return "";
   }

   /** Cursor relativ zur momentanen Position verschieben (Ansi.sys) */
   public static String ansiMoveXY(int x, int y)  { ansiMoveX(x);  ansiMoveY(y);  return ""; }
   /** Cursor relativ zur momentanen Position horizontal verschieben  (Ansi.sys) */
   public static String ansiMoveX(int x)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     if      (x > 0)  { System.out.print("\u001B[" +  x + "C"); }
     else if (x < 0)  { System.out.print("\u001B[" + -x + "D"); }
     return "";
   }

   /** Cursor nach links  (Ansi.sys) */
   final static String LEFT = "\u001B[1D";
   /** Cursor nach rechts  (Ansi.sys) */
   final static String RIGHT= "\u001B[1C";
   /** Cursor nach oben  (Ansi.sys) */
   final static String UP   = "\u001B[1A";
   /** Cursor nach unten  (Ansi.sys) */
   final static String DOWN = "\u001B[1B";

   /** Cursor relativ zur momentanen Position vertikal verschieben  (Ansi.sys) */
   public static String ansiMoveY(int y)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     if      (y > 0)  { System.out.print("\u001B[" +  y + "B"); }
     else if (y < 0)  { System.out.print("\u001B[" + -y + "A"); }
     return "";
   }
   
   static { // Block f�r statische Initialisierungen = Konstruktor f�r statische Klassen?
      //final static String HOME  = "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
      //+ "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
      //+ "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b";
   }
   
   /** HOME mit "\b\b\b..."  (ohne Ansi.sys) */
   final static String HOME  = "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
   + "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
   + "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b";
   /** Zum Zeilen-Anfang  (Ansi.sys) */
   final static String HOME2 = "\u001b[H";
   /** Zum Zeilen-Anfang - siehe move  (Ansi.sys) */
   final static String HOME3 = "\u001B[2222D";
   /** Zum Zeilen-Ende  (Ansi.sys) */
   final static String END   = "\u001B[2222C";
   /** Zum Seiten-Anfang  (Ansi.sys) */
   final static String TOP   = "\u001B[2222A";
   /** Zum Seiten-Ende  (Ansi.sys) */
   final static String BOTTOM= "\u001B[2222B";
   /** clrscr + auf Position 0/0 setzen  (Ansi.sys) */
   final static String CLRSCR= "\u001B[2J\u001B[0;0f";
   /** clrscr  (Ansi.sys) */
   final static String CLS2  = "\u001b[2J";
   /** clrscr mit "\n\n\n..."  (ohne Ansi.sys) */
   final static String CLS   = "\n\n\n\n\n\n \n\n\n\n\n\n"
   + "\n\n\n\n\n\n \n\n\n\n\n\n \n\n\n\n\n\n \n\n\n\n\n\n";

   /** Cursor nach links  (Ansi.sys) */
   public static String ansiLeft()            { ansiMoveX(-1);  return ""; }
   /** Cursor nach rechts  (Ansi.sys) */
   public static String ansiRight()           { ansiMoveX( 1);  return ""; }
   /** Cursor nach oben  (Ansi.sys) */
   public static String ansiUp()              { ansiMoveY(-1);  return ""; }
   /** Cursor nach unten  (Ansi.sys) */
   public static String ansiDown()            { ansiMoveY( 1);  return ""; }
   /** Cursor zum Zeilen-Anfang  (Ansi.sys) */
   public static String ansiHome()            { ansiMoveX(-2222);  return ""; } //out.print("\u001b[H");
   /** Cursor zum Zeilen-Ende  (Ansi.sys) */
   public static String ansiEnd()             { ansiMoveX( 2222);  return ""; }
   /** Cursor zum Seiten-Anfang  (Ansi.sys) */
   public static String ansiTop()             { ansiMoveY(-2222);  return ""; }
   /** Cursor zum Seiten-Ende  (Ansi.sys) */
   public static String ansiBottom()          { ansiMoveY( 2222);  return ""; }
   /** ClearScreen  (Ansi.sys) */
   public static String ansiClrScr()          { System.out.print("\u001B[2J"); ansiGoToXY(0,0); return "";}
   /** ClearScreen mit HintergrundFarbe  (Ansi.sys) */
   public static String ansiClrScr(int bColor)  { ansiSetBackColor(bColor); ansiClrScr();  return "";}
   /** ClearScreen mit HintergrundFarbe  (Ansi.sys) */
   public static String ansiClrScr(String bColor)  { print(bColor); ansiClrScr();  return "";}
   /** L�schen bis zum Zeilen-Ende  (Ansi.sys) */
   public static String ansiClrEol()          { System.out.print("\u001B[K" ); return "";}
   /** Zeile l�schen  (Ansi.sys) */
   public static String ansiClrLine()         { ansiMoveXY(-2222,0);  ansiClrEol();    return "";}
   /** CursorPosition anfordern  (Ansi.sys) (funktionierte nicht!!!) */
   public static String ansiGetPos()          { System.out.print("\u001B[6n"); return "";}
   /** Aktuelle CursorPosition speichern  (Ansi.sys) - siehe auch: restoreCursor() */
   public static String ansiSaveCursor()      { System.out.print("\u001B[s");  return "";}
   /** Cursor an die mit saveCursor() gespeicherte Position setzen.  (Ansi.sys) */
   public static String ansiRestoreCursor()   { System.out.print("\u001B[u");  return "";}

   /** +/-number Zeilen unter-/oberhalb der Cursorposition l�schen. (Ansi.sys) */
   public static String ansiClrLines(int number)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      ansiSaveCursor();
      for (int i=0; i < number; i++) { ansiClrEol();  ansiMoveXY(-2222, 1);  ansiClrEol(); }
      for (int i=0; i > number; i--) { ansiClrEol();  ansiMoveXY(-2222,-1);  ansiClrEol(); }
      ansiRestoreCursor();  return "";
   }
   /** Ab Cursorposition bis zum Seiten-Ende l�schen. (Ansi.sys) */
   public static String ansiClrDown()  { return ansiClrLines(33); }
   /** Ab Cursorposition bis zum Seiten-Anfang l�schen. (Ansi.sys) */
   public static String ansiClrUp()  { return ansiClrLines(33); }


   /////////////////////////////////////////////////////////////////////////////

   /** Ausgabe-Umleitung auf Konsole zur�cksetzen. - siehe: setSystemOut() */
   public static boolean resetSystemOut()  { return setSystemOut("CON"); }
   /** Ausgabe-Umleitung auf Konsole zur�cksetzen. */
   public static boolean   setSystemOut()  { return setSystemOut("CON"); }
   /** System.out.print/ln -Ausgabe nach dateiName oder "CON" umleiten. */
   public static boolean setSystemOut(String dName)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      System.out.flush();  System.out.close();
      try { System.setOut(new PrintStream(new FileOutputStream(dName))); }
      catch (FileNotFoundException e) {
         resetSystemOut();  System.err.println(e.toString());  return false;
      }
      return true;
   }

   /** Fehler-Umleitung auf Konsole zur�cksetzen. - siehe: setSystemErr() */
   public static boolean resetSystemErr()  { return setSystemErr("CON"); }
   /** Fehler-Umleitung auf Konsole zur�cksetzen. */
   public static boolean setSystemErr()  { return setSystemErr("CON"); }
   /** System.err.print/ln -Ausgabe nach dateiName oder "CON" umleiten. */
   public static boolean setSystemErr(String dName)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      System.err.flush();  System.err.close();
      try { System.setErr(new PrintStream(new FileOutputStream(dName))); }
      catch (FileNotFoundException e) {
         resetSystemErr();  System.err.println(e.toString());  return false;
      }
      return true;
   }

   /** Eingabe-Umleitung auf Konsole zur�cksetzen. - siehe: setSystemIn() */
   public static boolean resetSystemIn()  { return setSystemIn("CON"); }
   /** Eingabe-Umleitung auf Konsole zur�cksetzen. */
   public static boolean setSystemIn()  { return setSystemIn("CON"); }
   /** System.in -Eingabe von dateiName od. "CON" (Console=Keyboard) */
   public static boolean setSystemIn(String dName)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      //System.in.close();
      try { System.setIn(new FileInputStream(dName)); }
      catch (FileNotFoundException e) {
         resetSystemIn();  System.err.println(e.toString());  return false;
      }
      return true;
   }

   /** GanzZahl in Unicode-Zeichen umwandeln. (Dezimaler UNI-Code) */
   public static String    toChar(int ch)  { return ((char)ch + ""); }
   /** GanzZahl in Unicode-Zeichen umwandeln und ausgeben. (Dezimaler UNI-Code) */
   public static String printChar(int ch)  { print((char)ch);  return ""; }

   private static boolean  toFile= false;
   
   /////////////////////////////////////////////////////////////////////////////
   /////////////////////////////////////////////////////////////////////////////
   
   /** Alle oldStr in text0 l�schen bzw. durch "" ersetzen. */
   public static String stringDelete(String text0, String oldStr)
   { return  stringReplace(text0, oldStr, ""); }
   /** n-mal oldStr in text0 l�schen bzw. durch "" ersetzen. */
   public static String stringDelete(String text0, String oldStr, int n)
   { return  stringReplace(text0, oldStr, "", n); }
   /** Alle oldStr in text0 durch newStr ersetzen. */
   public static String stringReplace(String text0, String oldStr, String newStr)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String text= text0;
      if ((text.length()  ==0) || (oldStr.length()==0))  { return  text; }
      if ((oldStr.length()==1) || (newStr.length()==1))  {  // einzelnes Zeichen
         text= text.replace(oldStr.charAt(0), newStr.charAt(0));
      }else {                                               // String
         StringBuffer strBuf= new StringBuffer(text);
         int pos= text.indexOf(oldStr);
         while (pos >= 0) {
            strBuf.replace(pos, pos+oldStr.length(), newStr);
            text= strBuf.toString();
            pos = text.indexOf(oldStr);
         }
      }
      return text;
   }

   /** n-mal oldStr in text0 durch newStr ersetzen. */
   public static String stringReplace(String text0, String oldStr, String newStr, int n)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String text= text0;
      if ((text==null)||(text.length()==0)||(oldStr.length()==0))  { return text; }
      StringBuffer strBuf= new StringBuffer(text);
      int pos= text.indexOf(oldStr);
      for (int i=0; ((pos >= 0)&&(i < n)); i++)  {
         strBuf.replace(pos, pos+oldStr.length(), newStr);
         text= strBuf.toString();
         pos = text.indexOf(oldStr);
      }
      return text;
   }

   /** Anzahl der Vorkommen von search in text0 z�hlen. */
   public static int stringCount(String text0, String search)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String text= text0;
      if ((text==null) || (text.length()==0) || (search==null))  { return 0; }
      int count= 0;
      StringBuffer strBuf= new StringBuffer(text);
      int pos= text.indexOf(search);
      while (pos >= 0) {
         count++;
         strBuf.replace(pos, pos+search.length(), "");
         text= strBuf.toString();
         pos = text.indexOf(search);
      }
      return  count;
   }

   /** Anzahl der Vorkommen von search in text0 z�hlen. Gro�/kein ignorieren */
   public static int stringCountIgnoreCase(String text0, String search)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String text= text0;  text.toLowerCase();  search.toLowerCase();
      if ((text==null) || (text.length()==0) || (search==null))  { return 0; }
      int count= 0;
      StringBuffer strBuf= new StringBuffer(text);
      int pos= text.indexOf(search);
      while (pos >= 0) {
         count++;
         strBuf.replace(pos, pos+search.length(), "");
         text= strBuf.toString();
         pos = text.indexOf(search);
      }
      return  count;
   }

   /** Pr�ft, ob text mit ende endet. - case-sensetive */
   public static boolean stringEndsWith(String text, String ende)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((text==null)||(ende==null)||(text.length() < ende.length())) {
         return  false;
      }else  { return  text.startsWith(ende, text.length()-ende.length()); }
   }

   /** Pr�ft, ob text mit ende endet. - case-insensetive */
   public static boolean stringEndsWithIgnoreCase(String text, String ende)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((text==null)||(ende==null)||(text.length() < ende.length())) {
         return  false;
      }else {
         String ende2= text.substring(text.length()-ende.length());
         return (ende.equalsIgnoreCase(ende2)) ? true : false;
      }
   }

   /** Pr�ft, ob text mit start beginnt. case-sensetive */
   public static boolean stringStartsWith(String text, String start)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((text==null)||(start==null)||(text.length() < start.length())) {
         return  false;
      }else  { return  text.startsWith(start); }
   }

   /** Pr�ft, ob text mit ende endet. - case-insensetive */
   public static boolean stringStartsWithIgnoreCase(String text, String start)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((text==null)||(start==null)||(text.length() < start.length())) {
         return  false;
      }else {
         String start2= text.substring(0, start.length());
         return (start.equalsIgnoreCase(start2)) ? true : false;
      }
   }

   
   /////////////////////////////////////////////////////////

   /** Gibt text mit linkem Rand xPos aus. */
   public static void printTextPos  (String text, int xPos)
   { print  (formatTextPos(text, xPos)); }
   /** Gibt text mit linkem Rand xPos aus. */
   public static void printlnTextPos(String text, int xPos)
   { println(formatTextPos(text, xPos)); }
   /** Gibt text mit linkem Rand xPos aus. */

   /** Gibt text mit max. width Zeichen pro Zeile linksb�ndig aus. */
   public static void printTextLeft  (String text, int width)
   { print  (formatTextLeft(text, width, 0)); }
   /** Gibt text mit max. width Zeichen pro Zeile linksb�ndig aus. */
   public static void printlnLeft(String text, int width)
   { println(formatTextLeft(text, width, 0)); }

   /** Gibt text mit max. width Zeichen pro Zeile linksb�ndig aus.  xPos = linker Rand */
   public static void printTextLeft  (String text, int xPos, int width)
   { print  (formatTextLeft(text, xPos, width)); }
   /** Gibt text mit max. width Zeichen pro Zeile linksb�ndig aus.  xPos= linker Rand */
   public static void printlnLeft(String text, int xPos, int width)
   { println(formatTextLeft(text, xPos, width)); }

   /** Gibt text mit max. width Zeichen pro Zeile zentriert aus.*/
   public static void printTextCenter  (String text, int width)
   { print  (formatTextCenter(text, width, 0)); }
   /** Gibt text mit max. width Zeichen pro Zeile zentriert aus. */
   public static void printlnCenter(String text, int width)
   { println(formatTextCenter(text, width, 0)); }

   /** Gibt text mit max. width Zeichen pro Zeile zentriert aus.  xPos = linker Rand */
   public static void printTextCenter  (String text, int xPos, int width)
   { print  (formatTextCenter(text, xPos, width)); }
   /** Gibt text mit max. width Zeichen pro Zeile zentriert aus.  xPos= linker Rand */
   public static void printlnCenter(String text, int xPos, int width)
   { println(formatTextCenter(text, xPos, width)); }

   /** Gibt text mit max. width Zeichen pro Zeile rechtsb�ndig aus. */
   public static void printTextRight  (String text, int width)
   { print  (formatTextRight(text, width, 0)); }
   /** Gibt text mit max. width Zeichen pro Zeile rechtsb�ndig aus. */
   public static void printlnRight(String text, int width)
   { println(formatTextRight(text, width, 0)); }

   /** Gibt text mit max. width Zeichen pro Zeile rechtsb�ndig aus.  xPos = linker Rand */
   public static void printTextRight  (String text, int xPos, int width)
   { print  (formatTextRight(text, xPos, width)); }
   /** Gibt text mit max. width Zeichen pro Zeile rechtsb�ndig aus.  xPos= linker Rand */
   public static void printlnRight(String text, int xPos, int width)
   { println(formatTextRight(text, xPos, width)); }

   /** Formatiert text mit maximal width Zeichen pro Zeile linksb�ndig.  xPos = linker Rand */
   public static String formatTextLeft  (String text, int xPos, int width)
   { return  formatTextLCR(stringReplace(text,"\n\n","\n \n"), xPos, width, -1); }  // align: [-1, 0, 1]

   /** Formatiert text mit maximal width Zeichen pro Zeile rechtsb�ndig.  xPos = linker Rand */
   public static String formatTextCenter(String text, int xPos, int width)
   { return  formatTextLCR(stringReplace(text,"\n\n","\n \n"), xPos, width,  0); }  // align: [-1, 0, 1]

   /** Formatiert text mit maximal width Zeichen pro Zeile zentriert.  xPos = linker Rand */
   public static String formatTextRight (String text, int xPos, int width)
   { return  formatTextLCR(stringReplace(text,"\n","\n \n"), xPos, width, 1); }  // align: [-1, 0, 1]

   // Tabulatoren durch 9 Leerzeichen ersetzen?  LCR - Left/Center/Right
   // Formatiert text mit maximal width Zeichen pro Zeile.  xPos = linker Rand  align [-1,0,1]
   private static String formatTextLCR(String text, int xPos, int width, int align)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if ((text==null)||(text.length()==0)||(width<=0)||(xPos<0))  { return ""; }
      //width= Math.abs(width);   if (xPos<0) { xPos= 0; }
      String text2        = ((text.charAt(0) == '\n') ? " \n" : "");
      //if (text.charAt(0)=='\n')  { text2= " \n"; }
      String word         = "";
      String strxPos      = "";
      int currentLineWidth= 0;
      int wordWidth       = 0;
      boolean firstIsSpace= false;
      for (int i=0; i < xPos; i++)  { strxPos= strxPos + " "; }
      if (xPos > 0)  { text2= strxPos; }
      StringTokenizer  st= new StringTokenizer(text, " -", true);
      while (st.hasMoreTokens()) {
         word= st.nextToken();
         wordWidth= word.length();
         if ((currentLineWidth + wordWidth <= width) || (width < wordWidth)) {
            if (word.indexOf("\n") >= 0) {
               currentLineWidth= word.length()-word.lastIndexOf("\n")-1;  wordWidth= currentLineWidth;
            //}else if (word.indexOf("\r") >= 0) {
               //currentLineWidth= word.length()-word.indexOf("\r")-1;  wordWidth= currentLineWidth;
            //}else if (word.indexOf("\t") >= 0) {
               //currentLineWidth= currentLineWidth + 8;  wordWidth= 8;
            }else {
               currentLineWidth= currentLineWidth + wordWidth;
            }
            text2= text2 + word;
            // siehe ***
         }else {
            firstIsSpace= (word.charAt(0) == ' ');
            if (word.charAt(0) == '-' )  { firstIsSpace= true;  text2= text2 + "-" ; }
            //if (word.charAt(0) == '\n')  { firstIsSpace= true;  text2= text2 + "\n"; }
            text2= text2 + "\n" + strxPos + (firstIsSpace ? "" : word);
            currentLineWidth= (firstIsSpace ? 0 : wordWidth);
         }
      }
      //if (true) {  //align >= -1) {  //center oder right
      String[] strArr= textToStringArray(text2, "\r\n");
      for (int i=0; i < strArr.length; i++) {
         strArr[i]= strArr[i].trim();
         for (int j=strArr[i].length(); j < width; j++) {
            if       (align==0) {  //center
               if (j%2 == 0)  { strArr[i]= " " + strArr[i]; }
               else           { strArr[i]= strArr[i] + " "; }
            }else if (align==1) {  //right
               strArr[i]= " " + strArr[i];
            }
         }
         strArr[i]= strxPos + strArr[i];
      }
      text2= "";
      for (int i=0; i < strArr.length; i++)  { text2= text2 + strArr[i] + "\n"; }
      return text2;
   }
   
   /////////////////////////////////////////////////////////////////////////////
   // RandomAccessFile /////////////////////////////////////////////////////////
   
   final private static byte  BOOLEAN0_SIZE= 10,  CHAR0_SIZE= 20;
   /** Speicherplatz-Bedarf in Byte */
   final public static byte
      BOOLEAN_SIZE= 1, CHAR_SIZE= 2,
      BYTE_SIZE= 1, SHORT_SIZE= 2, INT_SIZE= 4, LONG_SIZE= 8,
      FLOAT_SIZE= 4, DOUBLE_SIZE= 8;
   private static String lastWFile= "", lastRFile= "";
   private static RandomAccessFile rRaf= null, wRaf= null;

   /** boolean-Array an den Datei-Anfang schreiben. */
   public static boolean fileWriteBoolean(String datei, boolean[] werte)
   { return  fileWriteBoolean(datei, 0, werte); }
   /** boolean-Array in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteBoolean(String datei, long pos, boolean[] werte)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      boolean ret= false;
      for (int i=0; i<werte.length; i++) {
         ret= fileWGZ(datei, pos+i, werte[i]==true ? 1 : 0, BOOLEAN0_SIZE);
      }
      return ret;
   }

   /** char-Array an den Datei-Anfang schreiben. */
   public static boolean fileWriteChar(String datei, char[] werte)
   { return  fileWriteChar(datei, 0, werte); }
   /** char-Array in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteChar (String datei, long pos, char[] werte)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      boolean ret= false;
      for (int i=0; i<werte.length; i++) { ret= fileWGZ(datei, pos+i, werte[i], CHAR0_SIZE); }
      return ret;
   }

   /** byte-Array an den Datei-Anfang schreiben. */
   public static boolean fileWriteByte(String datei, byte[] werte)
   { return  fileWriteByte(datei, 0, werte); }
   /** byte-Array in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteByte (String datei, long pos, byte[] werte)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      boolean ret= false;
      for (int i=0; i<werte.length; i++) { ret= fileWGZ(datei, pos+i, werte[i], BYTE_SIZE); }
      return ret;
   }

   /** short-Array an den Datei-Anfang schreiben. */
   public static boolean fileWriteShort(String datei, short[] werte)
   { return  fileWriteShort(datei, 0, werte); }
   /** short-Array in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteShort(String datei, long pos, short[] werte)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      boolean ret= false;
      for (int i=0; i<werte.length; i++) { ret= fileWGZ(datei, pos+i, werte[i], SHORT_SIZE); }
      return ret;
   }
   /** int-Array an den Datei-Anfang schreiben. */
   public static boolean fileWriteInt(String datei, int[] werte)
   { return  fileWriteInt(datei, 0, werte); }
   /** int-Array in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteInt  (String datei, long pos, int[] werte)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      boolean ret= false;
      for (int i=0; i<werte.length; i++) { ret= fileWGZ(datei, pos+i, werte[i], INT_SIZE); }
      return ret;
   }
   /** long-Array an den Datei-Anfang schreiben. */
   public static boolean fileWriteLong(String datei, long[] werte)
   { return  fileWriteLong(datei, 0, werte); }
   /** long-Array in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteLong (String datei, long pos, long[] werte)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      boolean ret= false;
      for (int i=0; i<werte.length; i++) { ret= fileWGZ(datei, pos+i, werte[i], LONG_SIZE); }
      return ret;
   }
   /** float-Array an den Datei-Anfang schreiben. */
   public static boolean fileWriteFloat(String datei, float[] werte)
   { return  fileWriteFloat(datei, 0, werte); }
   /** float-Array in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteFloat (String datei, long pos, float[] werte)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      boolean ret= false;
      for (int i=0; i<werte.length; i++) { ret= fileWGZ2(datei, pos+i, werte[i], FLOAT_SIZE); }
      return ret;
   }
   /** double-Array an den Datei-Anfang schreiben. */
   public static boolean fileWriteDouble(String datei, double[] werte)
   { return  fileWriteDouble(datei, 0, werte); }
   /** double-Array in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteDouble(String datei, long pos, double[] werte)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      boolean ret= false;
      for (int i=0; i<werte.length; i++) { ret= fileWGZ2(datei, pos+i, werte[i], DOUBLE_SIZE); }
      return ret;
   }

   /** boolean-Wert an den Datei-Anfang schreiben. */
   public static boolean fileWriteBoolean(String datei, boolean  wert)
   { return  fileWGZ(datei, 0, wert==true ? 1 : 0, BOOLEAN0_SIZE); }
   /** char-Wert an den Datei-Anfang schreiben. */
   public static boolean fileWriteChar(String datei, char  wert)
   { return fileWGZ(datei, 0, wert, CHAR0_SIZE); }
   /** byte-Wert an den Datei-Anfang schreiben. */
   public static boolean fileWriteByte (String datei, int  wert)
   { return fileWGZ(datei, 0, (byte)wert, BYTE_SIZE); }
   /** short-Wert an den Datei-Anfang schreiben. */
   public static boolean fileWriteShort(String datei, int wert)
   { return fileWGZ(datei, 0, (short)wert, SHORT_SIZE); }
   /** int-Wert an den Datei-Anfang schreiben. */
   public static boolean fileWriteInt  (String datei, int   wert)
   { return fileWGZ(datei, 0, wert, INT_SIZE); }
   /** long-Wert an den Datei-Anfang schreiben. */
   public static boolean fileWriteLong (String datei, long  wert)
   { return fileWGZ(datei, 0, wert, LONG_SIZE); }
   /** float-Wert an den Datei-Anfang schreiben. */
   public static boolean fileWriteFloat(String datei, double wert)
   { return fileWGZ2(datei, 0, wert, FLOAT_SIZE); }
   /** double-Wert an den Datei-Anfang schreiben. */
   public static boolean fileWriteDouble(String datei, double wert)
   { return fileWGZ2(datei, 0, (float)wert, DOUBLE_SIZE); }

   /** boolean-Wert in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteBoolean(String datei, long pos, boolean  wert)
   { return  fileWGZ(datei, pos, wert==true ? 1 : 0, BOOLEAN0_SIZE); }
   /** char-Wert in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteChar (String datei, long pos, char  wert)
   { return fileWGZ(datei, pos, wert, CHAR0_SIZE); }
   /** byte-Wert in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteByte (String datei, long pos, byte  wert)
   { return fileWGZ(datei, pos, wert, BYTE_SIZE); }
   /** short-Wert in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteShort(String datei, long pos, short wert)
   { return fileWGZ(datei, pos, wert, SHORT_SIZE); }
   /** int-Wert in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteInt  (String datei, long pos, int   wert)
   { return fileWGZ(datei, pos, wert, INT_SIZE); }
   /** long-Wert in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteLong (String datei, long pos, long  wert)
   { return fileWGZ(datei, pos, wert, LONG_SIZE); }
   /** float-Wert in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteFloat(String datei, long pos, float wert)
   { return fileWGZ2(datei, pos, wert, FLOAT_SIZE); }
   /** double-Wert in eine Datei schreiben.  pos = Dateiposition */
   public static boolean fileWriteDouble(String datei, long pos, double wert)
   { return fileWGZ2(datei, pos, wert, DOUBLE_SIZE); }

   private static boolean fileWGZ(String datei, long pos, long wert, byte typ)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      pos= pos*typ;  // INT-Position
      boolean ret= false;
      try {
         if (!lastWFile.equalsIgnoreCase(datei)) {
            try  { lastWFile= "";  wRaf.close();  wRaf= null; }
            catch (Exception e)  {;}  //System.out.println("Fehler:10" + e.toString()); }
            wRaf= new RandomAccessFile(datei, "rw");  lastWFile= datei;
         }
         if ( pos >= wRaf.length())  { wRaf.setLength(pos+typ); }
         if ((pos <= wRaf.length()) && (pos >= 0)) {
            wRaf.seek(pos);
            switch (typ) {
               case BOOLEAN0_SIZE:{ wRaf.writeBoolean (wert==1 ? true : false);  ret= true;  break; }
               case CHAR0_SIZE:   { wRaf.writeChar ((char) wert);  ret= true;  break; }
               case BYTE_SIZE:    { wRaf.writeByte ((byte) wert);  ret= true;  break; }
               case SHORT_SIZE:   { wRaf.writeShort((short)wert);  ret= true;  break; }
               case INT_SIZE:     { wRaf.writeInt  ((int)  wert);  ret= true;  break; }
               case LONG_SIZE:    { wRaf.writeLong ((long) wert);  ret= true;  break; }
               default:           { ret= false; }
            }
         }else {
            System.out.println("Write-FEHLER! " + datei);  ret= false;
         }
         return ret;
      }catch (Exception e) {
         System.out.println("Fehler:11" + datei + ": " + e.toString());  return false;
      }
   }

   private static boolean fileWGZ2(String datei, long pos, double wert, byte typ)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      pos= pos*typ;  // INT-Position
      boolean ret= false;
      try {
         if (!lastWFile.equalsIgnoreCase(datei)) {
            try  { lastWFile= "";  wRaf.close();  wRaf= null; }
            catch (Exception e)  { System.out.println("Fehler:20" + e.toString()); }
            wRaf= new RandomAccessFile(datei, "rw");  lastWFile= datei;
         }
         if ( pos >= wRaf.length())  { wRaf.setLength(pos+typ); }
         if ((pos <= wRaf.length()) && (pos >= 0)) {
            wRaf.seek(pos);
            switch (typ) {
               case FLOAT_SIZE:  { wRaf.writeFloat ((float) wert);  ret= true;  break; }
               case DOUBLE_SIZE: { wRaf.writeDouble((double)wert);  ret= true;  break; }
               default:     { ret= false; }
            }
         }else {
            System.out.println("Write-FEHLER! " + datei);  ret= false;
         }
         return ret;
      }catch (Exception e) {
         System.out.println(datei + ": " + e.toString());  return false;
      }
   }

   //               case FLOAT:  { wRaf.writeFLoat((long) wert);  ret= true;  break; }
   //               case DOUBLE: { wRaf.writeDouble((long) wert);  ret= true;  break; }

   /** boolean-Array vom Datei-Anfang lesen. */
   public static boolean[] fileReadBoolean(String datei, int n)
   { return  fileReadBoolean(datei, 0, n); }
   /** n boolean-Werte aus einer Datei lesen.  pos = Dateiposition */
   public static boolean[] fileReadBoolean(String datei, long pos, int n) {
      boolean[] intArr= new boolean[n];
      for (int i=0; i < n; i++)  { intArr[i]= fileReadBoolean(datei, pos+i); }
      return  intArr;
   }

   /** char-Array vom Datei-Anfang lesen. */
   public static char[] fileReadChar(String datei, int n)
   { return  fileReadChar(datei, 0, n); }
   /** n char-Werte aus einer Datei lesen.  pos = Dateiposition */
   public static char[] fileReadChar(String datei, long pos, int n) {
      char[] intArr= new char[n];
      for (int i=0; i < n; i++)  { intArr[i]= fileReadChar(datei, pos+i); }
      return  intArr;
   }

   /** byte-Array vom Datei-Anfang lesen. */
   public static byte[] fileReadByte(String datei, int n)
   { return  fileReadByte(datei, 0, n); }
   /** n byte-Werte aus einer Datei lesen.  pos = Dateiposition */
   public static byte[] fileReadByte(String datei, long pos, int n) {
      byte[] intArr= new byte[n];
      for (int i=0; i < n; i++)  { intArr[i]= fileReadByte(datei, pos+i); }
      return  intArr;
   }

   /** short-Array vom Datei-Anfang lesen. */
   public static short[] fileReadShort(String datei, int n)
   { return  fileReadShort(datei, 0, n); }
   /** n short-Werte aus einer Datei lesen.  pos = Dateiposition */
   public static short[] fileReadShort(String datei, long pos, int n) {
      short[] intArr= new short[n];
      for (int i=0; i < n; i++)  { intArr[i]= fileReadShort(datei, pos+i); }
      return  intArr;
   }

   /** int-Array vom Datei-Anfang lesen. */
   public static int[] fileReadInt(String datei, int n)
   { return  fileReadInt(datei, 0, n); }
   /** n int-Werte aus einer Datei lesen.  pos = Dateiposition */
   public static int[] fileReadInt(String datei, long pos, int n) {
      int[] intArr= new int[n];
      for (int i=0; i < n; i++)  { intArr[i]= fileReadInt(datei, pos+i); }
      return  intArr;
   }

   /** long-Array vom Datei-Anfang lesen. */
   public static long[] fileReadLong(String datei, int n)
   { return  fileReadLong(datei, 0, n); }
   /** n long-Werte aus einer Datei lesen.  pos = Dateiposition */
   public static long[] fileReadLong(String datei, long pos, int n) {
      long[] intArr= new long[n];
      for (int i=0; i < n; i++)  { intArr[i]= fileReadLong(datei, pos+i); }
      return  intArr;
   }

   /** float-Array vom Datei-Anfang lesen. */
   public static float[] fileReadFloat(String datei, int n)
   { return  fileReadFloat(datei, 0, n); }
   /** n float-Werte aus einer Datei lesen.  pos = Dateiposition */
   public static float[] fileReadFloat(String datei, long pos, int n) {
      float[] intArr= new float[n];
      for (int i=0; i < n; i++)  { intArr[i]= fileReadFloat(datei, pos+i); }
      return  intArr;
   }

   /** double-Array vom Datei-Anfang lesen. */
   public static double[] fileReadDouble(String datei, int n)
   { return  fileReadDouble(datei, 0, n); }
   /** n double-Werte aus einer Datei lesen.  pos = Dateiposition */
   public static double[] fileReadDouble(String datei, long pos, int n) {
      double[] intArr= new double[n];
      for (int i=0; i < n; i++)  { intArr[i]= fileReadDouble(datei, pos+i); }
      return  intArr;
   }

   /** boolean-Wert vom Datei-Anfang lesen. */
   public static boolean fileReadBoolean(String datei)
   { return  (fileReadValue(datei, 0, BOOLEAN0_SIZE)==1 ? true : false); }
   /** char-Wert vom Datei-Anfang lesen. */
   public static char  fileReadChar(String datei)
   { return  (char)fileReadValue(datei, 0, CHAR0_SIZE); }
   /** byte-Wert vom Datei-Anfang lesen. */
   public static byte  fileReadByte(String datei)
   { return  (byte) fileReadValue(datei, 0, BYTE_SIZE); }
   /** short-Wert aus einer Datei lesen.  pos = Dateiposition */
   public static short fileReadShort(String datei, long pos)
   { return  (short)fileReadValue(datei, pos, SHORT_SIZE); }
   /** int-Wert aus einer Datei lesen.  pos = Dateiposition */
   public static int   fileReadInt(String datei, long pos)
   { return  (int)  fileReadValue(datei, pos, INT_SIZE); }
   /** long-Wert aus einer Datei lesen.  pos = Dateiposition */
   public static long  fileReadLong(String datei, long pos)
   { return  (long) fileReadValue(datei, pos, LONG_SIZE); }
   /** float-Wert aus einer Datei lesen.  pos = Dateiposition */
   public static float fileReadFloat(String datei, long pos)
   { return  (float)fileReadValue2(datei, pos, FLOAT_SIZE); }
   /** double-Wert aus einer Datei lesen.  pos = Dateiposition */
   public static double fileReadDouble(String datei, long pos)
   { return  (double)fileReadValue2(datei, pos, DOUBLE_SIZE); }

   /** boolean-Wert aus einer Datei lesen.  pos = Dateiposition */
   public static boolean  fileReadBoolean(String datei, long pos)
   { return  (fileReadValue(datei, pos, BOOLEAN0_SIZE)==1 ? true : false); }
   /** char-Wert aus einer Datei lesen.  pos = Dateiposition */
   public static char  fileReadChar(String datei, long pos)
   { return  (char)fileReadValue(datei, pos, CHAR0_SIZE); }
   /** byte-Wert aus einer Datei lesen.  pos = Dateiposition */
   public static byte  fileReadByte(String datei, long pos)
   { return  (byte) fileReadValue(datei, pos, BYTE_SIZE); }
   /** short-Wert vom Datei-Anfang lesen. */
   public static short fileReadShort(String datei)
   { return  (short)fileReadValue(datei, 0, SHORT_SIZE); }
   /** int-Wert vom Datei-Anfang lesen. */
   public static int   fileReadInt(String datei)
   { return  (int)  fileReadValue(datei, 0, INT_SIZE); }
   /** long-Wert vom Datei-Anfang lesen. */
   public static long  fileReadLong(String datei)
   { return  (long) fileReadValue(datei, 0, LONG_SIZE); }
   /** float-Wert vom Datei-Anfang lesen. */
   public static float fileReadFloat(String datei)
   { return  (float)fileReadValue2(datei, 0, FLOAT_SIZE); }
   /** double-Wert vom Datei-Anfang lesen. */
   public static double fileReadDouble(String datei)
   { return  (double)fileReadValue2(datei, 0, DOUBLE_SIZE); }

   // Wert aus einer Datei lesen.  pos = Dateiposition
   private static long fileReadValue(String datei, long pos, byte typ)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      pos= pos*INT_SIZE;
      long min= Long.MIN_VALUE,  ret= min;
      if ((new File(datei)).exists()) { //fileexist
         try {
            if (!lastRFile.equalsIgnoreCase(datei)) {
               try  { lastRFile= "";  rRaf.close();  rRaf= null; }
               catch (Exception e)  {;} //System.out.println("Fehler:100" + e.toString()); }
               rRaf= new RandomAccessFile(datei, "r");  // Datei �ffnen, falls geschlossen.
               lastRFile= datei;
            }
            if ((pos >= 0) && (pos < rRaf.length())) {
               rRaf.seek(pos);
               switch (typ) {
                  case BOOLEAN0_SIZE:{ ret= (rRaf.readBoolean()==true ? 1 : 0);  break; }
                  case CHAR0_SIZE:   { ret= rRaf.readChar ();  break; }
                  case BYTE_SIZE:    { ret= rRaf.readByte ();  break; }
                  case SHORT_SIZE:   { ret= rRaf.readShort();  break; }
                  case INT_SIZE:     { ret= rRaf.readInt  ();  break; }
                  case LONG_SIZE:    { ret= rRaf.readLong ();  break; }
                  default:      { ret= min; }
                  // System.out.println("Posi: " + rRaf.getFilePointer());
                  // bei seek 6000 wird letzter Satz gelesen bei negativem Seek Fehler
               }
            }else {
               System.out.println("Read-FEHLER: " + datei);  ret= min;
            }
            return ret;  // rRaf.close();
         }catch (Exception e) {     //EOFExceptionnull
            System.out.println("Fehler:101" + datei + ": " + e.toString());  return -1;
         }  // bei falschem seek - Bad file-descriptor IOException
      }else  {
         System.out.println(datei + " not found!");  ret= min;
      }
      return min;
   }

   // Komma-Zahl aus einer Datei lesen.  pos = Dateiposition
   private static double fileReadValue2(String datei, long pos, byte typ)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      pos= pos*INT_SIZE;
      double min= Double.MIN_VALUE,  ret= min;
      if ((new File(datei)).exists()) { //fileexist
         try {
            if (!lastRFile.equalsIgnoreCase(datei)) {
               try  { lastRFile= "";  rRaf.close();  rRaf= null; }
               catch (Exception e)  { System.out.println("Fehler:10" + e.toString());}
               rRaf= new RandomAccessFile(datei, "r");  // read
               lastRFile= datei;
            }
            if ((pos >= 0) && (pos < rRaf.length())) {
               rRaf.seek(pos);
               switch (typ) {
                  case FLOAT_SIZE:   { ret= rRaf.readFloat ();  break; }
                  case DOUBLE_SIZE:  { ret= rRaf.readDouble();  break; }
                  default:      { ret= min; }
                  //System.out.println("Posi: " + rRaf.getFilePointer());
                  // bei seek 6000 wird letzter Satz gelesen bei negativem Seek Fehler
               }
            }else {
               System.out.println("Read-FEHLER: " + datei);  ret= min;
            }
            return ret;  //rRaf.close();
         }catch (Exception e) {     //EOFExceptionnull
            System.out.println(datei + ": " + e.toString());  return -1;
         }  //bei falschem seek - Bad file-descriptor IOException
      }else  {
         System.out.println(datei + " not found!");  ret= min;
      }
      return min;
   }

   /** String-Array an den Datei-Anfang schreiben.  Fehler-R�ckgabe: -1 ra */
   public static long fileWriteString(String datei, String[] zeilen)
   { return  fileWriteString(datei, 0, zeilen); }
   /** String-Array in eine Datei schreiben.  pos = Dateiposition; Fehler-R�ckgabe: -1 */
   public static long fileWriteString(String datei, long pos, String[] zeilen)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      long ret= -1L;
      for (int i=0; i < zeilen.length; i++) {
         ret= fileWriteString(datei, pos+i, zeilen[i]);
      }
      return ret;
   }

   /** String an den Datei-Anfang schreiben.  Fehler-R�ckgabe: -1  ra */
   public static long fileWriteString(String datei, String wert)
   { return  fileWriteString(datei, 0, wert); }
   /** String in eine Datei schreiben.  pos = Dateiposition; Fehler-R�ckgabe: -1 ra */
   public static long fileWriteString(String datei, long pos, String wert)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      int zeilen= 0;
      long ret= -1L;
      if (true) { //fileexist
         try {
            if (!lastWFile.equalsIgnoreCase(datei)) {
               try  { lastWFile= "";  wRaf.close();  wRaf= null; }
               catch (Exception e)  {System.out.println("Fehler:30" + e.toString());}
               wRaf= new RandomAccessFile(datei, "rw");  lastWFile= datei;
            }
            while (wRaf.readLine()!=null) { ++zeilen; }
            if (pos >= zeilen) { wRaf.setLength(wRaf.length()+wert.length()+1); } // read
            if (pos >= 0) {
               wRaf.seek(pos);  wRaf.writeBytes(wert);  ret= pos;  // ersetzen
            }else {                //writeChars (String)
               System.out.println("Write-FEHLER: " + datei);  ret= -1;
            }
            return ret;  //wRaf.close();
         }catch (Exception e) {     //EOFExceptionnull
            System.out.println(datei + ": " + e.toString());  return -1;
         }
      }else  { return -1; }
   }

   /** n Zeilen vom Datei-Anfang lesen.  Fehler-R�ckgabe: -1 ra */
   public static String[] fileReadString(String datei, int n)
   { return  fileReadString(datei, 0, n); }
   /** n Zeilen aus einer Datei lesen.  pos = Dateiposition; Fehler-R�ckgabe: -1 ra */
   public static String[] fileReadString(String datei, long pos, int n)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      String[] strArr= new String[n];
      for (int i=0; i < n; i++) { strArr[i]= fileReadString(datei, pos+i); }
      return  strArr;
   }

   /** String vom Datei-Anfang lesen.  Fehler-R�ckgabe: -1 ra */
   public static String fileReadString(String datei)
   { return  fileReadString(datei); }
   /** String aus einer Datei lesen.  pos = Dateiposition;  Fehler-R�ckgabe: -1 ra */
   public static String fileReadString(String datei, long pos)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      int zeilen= 0;
      String ret= "";
      try {
         if (!lastRFile.equalsIgnoreCase(datei)) {
            try  { lastRFile= "";  rRaf.close();  rRaf= null; }
            catch (Exception e)  { System.out.println("Fehler:40" + e.toString());}
            rRaf= new RandomAccessFile(datei, "r");  lastRFile= datei;
         }
         rRaf.seek(0);
         if (pos >= 0) {
            for (int i=0; i<=pos; i++)  {
               ret= rRaf.readLine();
               if (ret==null)  { ret= "";  break; };
            }
         }else { //pos <0
            String strBak= null;                //writeChars(String)
            StringBuffer strB= new StringBuffer(""); //rRaf.length());
            for (int i=0; i>=pos; i--) { // pos Zeilen einlesen
               strBak= rRaf.readLine();
               if (strBak==null)  { break; }
               else  { strB.append(strBak); strB.append("\n"); }
            }
            ret= strB.toString();
         }
         return ret;
      }catch (Exception e) {
         System.out.println(datei + ": " + e.toString());  return "";
      }
   }

   /** L�scht Datei und gibt bei Erfolg true zur�ck. */
   public static boolean raFileDelete(String datei)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      fileCloseAll();
      return (new File(datei)).delete();
   }

   /** L�scht Datei und gibt bei Erfolg true zur�ck. */
   public static void fileCloseAll()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      try  {
         lastRFile= "";  rRaf.close();  rRaf= null;
      }catch (Exception e)  { System.out.println("Fehler:55" + e.toString()); }
      try  {
         lastWFile= "";  wRaf.close();  wRaf= null;
      }catch (Exception e)  { System.out.println("Fehler:56" + e.toString()); }
   }

   /** Erzeugt neue Datei mit size-Gr��e. */
   public static long fileNewFile(String datei, long size)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      return fileSetSize(datei, size);
   }

   /** int->float, long-> flaot */
   public static float toFloat(long zahl)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      float zahl2= (float)zahl;
      if (zahl!=(long)zahl2)  {
         System.out.println("Cast-Fehler: long->float (" + (zahl-zahl2) + ")");
         return Float.MIN_VALUE;
      }else  { return zahl2; }
   }

   /** long->double Tip: Am Besten Integer nach Double verwenden. */
   public static double toDouble(long zahl)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      double zahl2= (double)zahl;
      if (zahl!=(long)zahl2)  {
         System.out.println("Cast-Fehler: long->double (" + (zahl-zahl2) + ")");
         return Double.MIN_VALUE;
      }else  { return zahl2; }
   }

   /** Gr��e einer Datei �ndern. */
   public static long fileSetSize(String datei, long size)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      long ret= -1L;
      if (size >= 0) {  // ((new File(datei)).exists()) &&
         try {
            RandomAccessFile raf= new RandomAccessFile(datei, "rw");
            raf.setLength(size);  raf.close();  return raf.length();
         }catch (Exception e) {
            System.out.println(datei + ": " + e.toString());  return -1;
         }
      }else { return -1; }
   }

   /** Soll String ersetzen - (nicht von mir) */
   static String replace( String str, String pattern, String replace)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      int  s= 0,  e= 0;
      StringBuffer result= new StringBuffer();
      while ((e= str.indexOf(pattern, s)) >= 0) {
         result.append(str.substring(s, e));
         result.append(replace);
         s= e+pattern.length();
      }
      result.append(str.substring(s));
      return result.toString();
   }

   // imbort java.awt.*; Achtung!!!! Auskommentierung gen�gt nicht!
   // public static void beep()  { Toolkit.getDefaultToolkit().beep(); }

   /** GanzZahl mit der Breite width ausgeben.  -width = linksb�ndig */
   public static void printLong  (long zahl, int width)
   { printString  (Long.toString(zahl), width); }    //(new Long(zahl)).toString()
   public static void printlnLong(long zahl, int width)
   { printlnString(Long.toString(zahl), width); }

   /** KommaZahl mit der Breite width ausgeben.  -width = linksb�ndig */
   public static void printDouble  (double zahl, int width)
   { printString  (Double.toString(zahl), width); }    //(new Long(zahl)).toString()
   public static void printlnDouble(double zahl, int width)
   { printlnString(Double.toString(zahl), width); }

   /** KommaZahl formatiert ausgeben.  0#,. */
   public static void printlnDouble(double zahl, String format)
   { printDouble(zahl, format);  System.out.println(); }
   public static void printDouble  (double zahl, String format)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      DecimalFormat df= new DecimalFormat(format);
      System.out.print(df.format(zahl));
   }

   /** KommZahl formatiert mit der Breite width ausgeben.  -width = linksb�ndig */
   public static void printDouble  (double zahl, int width, String format)
   {
      DecimalFormat df= new DecimalFormat(format);
      printString(df.format(zahl), width);
   }
   public static void printlnDouble(double zahl, int width, String format)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      DecimalFormat df= new DecimalFormat(format);
      printlnString(df.format(zahl), width);
   }

   /** String mit der Breite width ausgeben.  -width = linksb�ndig */
   public static void printlnString(String str, int width)
   { printString(str, width);  System.out.println(); }
   public static void printString  (String str, int width)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
       if       (width > 0) {  //rechtsb�ndig
          for (int i=str.length(); i < width; i++) { str= " " + str; }
       }else if (width < 0) {  //linksb�ndig
          width= Math.abs(width);
          for (int i=str.length(); i < width; i++) { str= str + "_"; }
       }
       //while (zOut.length() < width)  { zOut= " " + zOut; }
       System.out.print(str);
   }

   /** Programm beenden (J/N):  - (mit J/N Abfrage). */
   public static void exit()
   {//MMMMMMMMMMMMMMMMMMMMMM
      if (Kon.readBoolean("Programm beenden (J/N): ", "J", "N")) {
         System.exit(0);
      }
   }

   /** Programm beenden (J/N):  - (mit J/N Abfrage und exitCode). */
   public static void exit(int exitCode)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      if (Kon.readBoolean("Programm beenden (J/N): ", "J", "N")) {
         System.exit(exitCode);
      }
   }
   /////////////////////////////////////////////////////////////////////////////

   /** Kon-Test */
   public static void main (String[] args)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      //Kon.println(Kon.javaInfoJRE ());
      //Kon.println(Kon.javaInfoJREs());
      //Kon.println(Kon.javaInfoVM ());
      //Kon.println(Kon.javaInfoVMs());
      Kon.println(Kon.systemInfo());
      Kon.println(Kon.javaInfo());
      System.exit(0);
      for (int i=0; i < 14; i=i+2) {
        Kon.printTextCenter("(-: Java f�r Einsteiger :-) www.grabler.de\n\n", i, 30);
      }
   }
}

/*
   class ThreadSound extends Thread // implements Runnable
   {//CCCCCCCCCCCCCCCCCCCCCCCCCCCCC
      private String datei= "";
      public ThreadSound(String datei)  { this.datei= datei; }
      public void run()  { Kon.sound(datei, 1); }
   }



class Kbd extends Kon
{//CCCCCCCCCCCCCCCCCC
   // Statt Kon.methode(?);  Kbd.methode(?);    // Kon=Konsole   Kbd=Keyboard
}


13.16.3 Den Drucker am Parallelport ansprechen
Es ist nat�rlich immer aufw�ndig f�r einen einfachen 10cpi Text ein Printer
Objekt zu erzeugen und dann all den Text als Grafik zu erzeugen. Das braucht
nicht nur lange, sondern ist auch sehr umst�ndlich. Um einen Drucker am Par-allelport
oder im Netzwerk direkt anzusprechen konstruieren wir einfach ein
FileOutputStream wie folgt.
FileOutputStream fos = new FileOutputStream( "PRN:" );
PrintWriter pw = new PrintWriter( fos );
pw.println( "Hier bin ich" );
pw.close();
H�ngt dann am Printer Port ein Drucker, so schreiben wir den Text in den
Datenstrom. Anstelle von PRN: funktioniert auch LTP1: beziehungsweise auch
ein Druckername im Netzwerk. Unter UNIX kann entsprechend /dev/lp ver-wendet
werden.
Nat�rlich sehen wir auf den ersten Blick, dass dies eine Windows/DOS Ver-sion
ist. Um das Ganze auch systemunabh�ngig zu steuern, entwickelte Sun die
Communications API. Obwohl sie in erster Linie f�r die serielle Schnittstelle
gedacht ist, unterst�tzt sie auch die parallele Schnittstelle. Hier bietet sich auch
die Chance, den Zugriff zu synchronisieren. F�nf willige Druckerbenutzer
sehen dann nicht auf einem Blatt allen Text.

-------------------------

466
� � � � � Ausgabe in ein Fenster
Bei genauerer Betrachtung der Standard-Aus- und Eingabe-Methoden ist fest-zustellen,
dass das Konzept nicht besonders plattformunabh�ng ist. Wenn wir
einen Macintosh als Plattform betrachten, dann l�sst sich dort keine Konsole
ausmachen. Bei GUI-Anwendungen spricht demnach einiges daf�r, auf die
Konsolenausgabe zu verzichten und die Ausgabe in ein Fenster zu setzen. Ich
m�chte daher an dieser Stelle etwas vorgreifen und ein Programmst�ck vorstel-len,
mit dem sich die Standard-Ausgabestr�me in einem Fenster darstellen las-sen.
Dann gen�gt folgendes, unter der Annahme, dass die Variable ta ein
TextArea Objekt referenziert.
PrintStream p = new PrintStream() {
new OutputStream() {
public void write( int b ) {
ta.append ( ""+(char)b );
}
}
}
System.setErr( p );
System.setOut( p );

-----------------------------

w boolean mkdir()
Legt das Unterverzeichnis an.
w boolean mkdirs()
Legt das Unterverzeichnis inklusive weitere Verzeichnisse an.
w boolean renameTo( File d )
Benennt die Datei in den Namen um, der durch das File Objekt d gegeben ist.
Ging alles gut, wird true zur�ckgegeben.
w boolean delete()
true, wenn Datei gel�scht werden konnte. Ein zu l�schendes Verzeichnis muss
leer sein. Diese Methode l�scht wirklich. Sie ist nicht so zu verstehen, dass sie
true liefert, falls die Datei potenziell gel�scht werden kann.
class java.io.File
implements Serializable, Comparable

Sicherheitspr�fung
Wir m�ssen uns bewusst sein, dass verschiedene Methoden, unter der Bedin-gung,
dass ein Security-Manager die Dateioperationen �berwacht, eine Secu-rityException
ausl�sen k�nnen. Security-Manager kommen beispielsweise
bei Applets zum Zuge. Folgende Methoden sind Kandidaten f�r eine Securi-tyException:
exists(), canWrite(), canRead(), canWrite(), isDirec-tory(),
lastModified(), length(), mkdir(), mkdir(), list(), delete()
und renameFile().


   //public static void print  (String   s)  { prinT (s + ""  ); }
   //public static void println(String   s)  { prinT (s + "\n"); }
   //public static void print  (long[]   l)  { prinT (l + ""  ); }
   //public static void println(long[]   l)  { prinT (l + "\n"); }
   //public static void print  (double[] d)  { prinT (d + ""  ); }
   //public static void println(double[] d)  { prinT (d + "\n"); }


      Toolkit tp= new Toolkit.getProperty();
      tp.beep();
      System.out.prSystem.out.println( );intln("Aufl�sung: " + tp.getScreenResolution);
      System.out.println("ScreenSize: " + tp.getScreenSize);
      System.out.println("ScreenSize: " + tp.toString());
      Toolkit tpk= new Toolkit.getDefaultToolkit();
      System.out.println("ScreenSize: " + tpk.toString());

*/
/*
   final private static PrintStream OUT   = System.out;
   public  static PrintStream       out   = System.out;
   private static PrintStream       sBak  = null;
   private static boolean           done  = true;

   public static boolean done()  { return (done && ! out.checkError()); }

   // All output goes to this file until it is closed.
   public static void printTo(String dName)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
     closePrintTo();
     if (dName.length()>0) {
        try {
           sBak= new PrintStream(new FileOutputStream(dName));
           toFile= true;  out= sBak;
        }catch (Exception e)  { toFile= false;  done= false; }
     }
   }

   // Close the current output file. The previous output file is restored.
   public static void closePrintTo()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      out.flush();  out.close();  out= OUT;  sBak= null;  toFile= false;
   }
   */

   /*
   public static void changeIO()
   {//MMMMMMMMMMMMMMMMMMMMMMMMMM
      if       (toFile==true )                 { sBak= out;  out= OUT; }
      else if ((toFile==false)&&(sBak!=null))  { out= sBak; }
   }
   */

   /*
   private static String changeCrLf(String s)
   {//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
      int sLen= s.length(); //String s= ("*Hall*o*ii**o");
      StringBuffer sBuf= new StringBuffer(s);   sBuf.append("@@@");
      for (int i=0; i<sLen+3; i++) {
         if (sBuf.charAt(i)=='\n')  { sBuf.replace(i, i+1, "\r\n"); }
      }
      sBuf.delete(sBuf.length()-3, sBuf.length());
      //System.out.println(sBuf.toString());
      return sBuf.toString();
   }
   */

