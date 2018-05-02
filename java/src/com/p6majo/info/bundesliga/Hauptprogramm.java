package com.p6majo.info.bundesliga;

public class Hauptprogramm {
    public static void main(String[] args) {
        Liga bundesliga = new Liga("Bundesliga", 18);

        Verein werder = new Verein("Werder Bremen", 12);
        Verein koeln = new Verein("1. FC Koeln", 18);
        Verein gladbach = new Verein("Borussia Moenchengladbach", 8);
        Verein bvb = new Verein("BVB Dortmund", 3);
        Verein augsburg = new Verein("FC Augsburg", 11);
        Verein bayern = new Verein("FC Bayern", 1);
        Verein berlin =new Verein("Herta Berlin", 10);
        Verein hoffenheim = new Verein("Hoffenheim", 7);
        Verein hsv = new Verein("HSV", 17);
        Verein leverkusen = new Verein("Leverkusen", 6);
        Verein hannover = new Verein("Hannover", 13);
        Verein mainz = new Verein("Mainz 05", 16);
        Verein leipzig= new Verein("RB Leipzig", 5);
        Verein schalke = new Verein("Schalke", 2);
        Verein freiburg = new Verein("SC Freiburg", 14);
        Verein frankfurt =new Verein("SGE Frankfurt", 4);
        Verein stuttgart = new Verein("VfB Stuttgart", 9);
        Verein vfl =new Verein("VfL Wolfsburg", 15);


        bundesliga.setVerein(werder, 0);
        bundesliga.setVerein(koeln, 1);
        bundesliga.setVerein(gladbach, 2);
        bundesliga.setVerein(bvb, 3);
        bundesliga.setVerein(augsburg, 4);
        bundesliga.setVerein(bayern, 5);
        bundesliga.setVerein(berlin, 6);
        bundesliga.setVerein(hoffenheim, 7);
        bundesliga.setVerein(hsv, 8);
        bundesliga.setVerein(leverkusen, 9);
        bundesliga.setVerein(hannover, 10);
        bundesliga.setVerein(mainz, 11);
        bundesliga.setVerein(leipzig, 12);
        bundesliga.setVerein(schalke, 13);
        bundesliga.setVerein(freiburg, 14);
        bundesliga.setVerein(frankfurt, 15);
        bundesliga.setVerein( stuttgart,16);
        bundesliga.setVerein(vfl, 17);

        bundesliga.listVereine();

    }
}
