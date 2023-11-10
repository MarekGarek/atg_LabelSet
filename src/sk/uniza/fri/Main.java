package sk.uniza.fri;

public class Main {

    public static void main(String[] args) {
        Graf g = Graf.nacitajGraf("C:\\Users\\garek\\Documents\\Skola\\ATG\\ATG_DAT\\ShortestPath\\Florida.hrn");
        g.printInfo();
        g.labelSet(1,518);
        g.vypisNajkratsiuCestu(518);
    }
}
