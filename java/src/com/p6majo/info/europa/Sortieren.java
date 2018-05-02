package com.p6majo.info.europa;

public class Sortieren {


    public static void main(String[] args) {
        Land[] europa = new Land[45];
        europa = getLaender();

        for (int l = 0;l<europa.length;l++)
            System.out.println(europa[l].toString());


        //Sortieren der Laender nach der Einwohnerzahl

        int vergleiche=0;
        int tausche=0;

        for (int i=0;i<europa.length;i++){
            for (int j=i+1;j<europa.length;j++){
                vergleiche ++;
                if (europa[i].getEinwohner()<europa[j].getEinwohner()) {
                    tausche(europa,i,j);
                    tausche ++;
                }
            }
        }

        //dieser Algorithmus laesst sich optimieren, indem man die Anzahl der Tauschungen reduziert,
        //man tauscht erst, wenn man das kleinste gefunden hat


        System.out.println("");
        System.out.println("Die Daten wurden sortiert. Dafuer wurden "+vergleiche+" Vergleiche und "+tausche+" Vertauschungen benoetigt.");
        System.out.println("Laender sortiert nach der Einwohnerzahl: ");
        System.out.println("");

        for (int l = 0;l<europa.length;l++)
            System.out.println(europa[l].toString());







    }

    private static void tausche(Land[] laender,int i,int j){
        Land tmp = laender[i];
        laender[i]=laender[j];
        laender[j]=tmp;
    }


    private static Land[] getLaender(){
        Land[] laender = new Land[45];

        laender[0]=new Land("Weißrussland","Minsk",207595,9489000,"Dsjarschynskaja Hara",345);
        laender[1]=new Land("Albanien","Tirana",28748,3170000,"Maja e Korabit",2764);
        laender[2]=new Land("Andorra","Andorra la Vella",468,83900,"Pic de Coma Pedrosa",2944);
        laender[3]=new Land("Belgien","Brüssel",32528,10667000,"Botrange",694);
        laender[4]=new Land("Bosnien und Herzegowina","Sarajevo",51197,3791000,"Maglić",2386);
        laender[5]=new Land("Bulgarien","Sofia",110994,7365000,"Musala",2925);
        laender[6]=new Land("Deutschland","Berlin",357093,81882000,"Zugspitze",2962);
        laender[7]=new Land("Dänemark","Kopenhagen",43098,5476000,"Gunnbjørns Fjeld (Groenland)",3694);
        laender[8]=new Land("Estland","Tallinn",45227,1342000,"Suur Munamägi",318);
        laender[9]=new Land("Finnland","Helsinki",338144,5326000,"Haltitunturi",1324);
        laender[10]=new Land("Frankreich","Paris",543965,62793000,"Mont Blanc",4810);
        laender[11]=new Land("Griechenland","Athen",131957,11142000,"Olymp – Mytikas",2918);
        laender[12]=new Land("Irland","Dublin",70273,4581000,"Carrauntoohil",1041);
        laender[13]=new Land("Island","Reykjavík",103000,318000,"Hvannadalshnúkur",2110);
        laender[14]=new Land("Italien","Rom",301336,60246000,"Monte Bianco di Courmayeur",4748);
        laender[15]=new Land("Kasachstan","Astana",146700,480000,"Khan Tengri",7010);
        laender[16]=new Land("Kosovo","Priština",10908,1800000,"Daravica",2656);
        laender[17]=new Land("Kroatien","Zagreb",56542,4480000,"Dinara",1831);
        laender[18]=new Land("Lettland","Riga",64589,2075000,"Gaizinkalns",311);
        laender[19]=new Land("Liechtenstein","Vaduz",160,36000,"Vorder-Grauspitz",2599);
        laender[20]=new Land("Litauen","Vilnius",65301,2981000,"Aukštojas",294);
        laender[21]=new Land("Luxemburg","Luxemburg",2586,537000,"Kneiff",560);
        laender[22]=new Land("Malta","Valletta",316,417000,"Ta' Dmejrek",253);
        laender[23]=new Land("Mazedonien","Skopje",25713,2057000,"Golem Korab",2764);
        laender[24]=new Land("Monaco","Monaco",2,36000,"Punkt im Stadtteil Jardin Exotique",164);
        laender[25]=new Land("Montenegro","Podgorica",13812,625000,"Zla Kolata",2534);
        laender[26]=new Land("Niederlande","Amsterdam",41526,16680000,"Mount Scenery",877);
        laender[27]=new Land("Norwegen","Oslo",385200,5063000,"Galdhøpiggen",2469);
        laender[28]=new Land("Österreich","Wien",83871,8488000,"Großglockner",3798);
        laender[29]=new Land("Polen","Warschau",312685,38501000,"Meeraugspitze",2500);
        laender[30]=new Land("Portugal","Lissabon",92212,10602000,"Montanha do Pico",2351);
        laender[31]=new Land("Rumänien","Bukarest",238391,19043000,"Moldoveanu",2544);
        laender[32]=new Land("San Marino","San Marino",61,32000,"Monte Titano",739);
        laender[33]=new Land("Schweden","Stockholm",450000,9514000,"Kebnekaise",2104);
        laender[34]=new Land("Schweiz","Bern",41285,8014000,"Dufourspitze",4634);
        laender[35]=new Land("Serbien","Belgrad",77474,7120000,"Midzor",2168);
        laender[36]=new Land("Slowakei","Bratislava",49034,5404000,"Gerlachspitz",2655);
        laender[37]=new Land("Slowenien","Ljubljana",20273,2058000,"Triglav",2864);
        laender[38]=new Land("Spanien","Madrid",504645,47213000,"Pico del Teide",3718);
        laender[39]=new Land("Tschechien","Prag",78866,10526000,"Schneekoppe",1603);
        laender[40]=new Land("Türkei","Ankara",23384,9799000,"Ararat",5137);
        laender[41]=new Land("Ukraine","Kiew",603700,45665000,"Howerla",2061);
        laender[42]=new Land("Ungarn","Budapest",93030,9967000,"Kékestető",1014);
        laender[43]=new Land("Vatikanstadt","Vatikanstadt",0,836,"Vatikanischer Hügel",75);
        laender[44]=new Land("Vereinigtes Königreich","London",242910,63200000,"Mount Paget",2935);

        return laender;
    }
}
