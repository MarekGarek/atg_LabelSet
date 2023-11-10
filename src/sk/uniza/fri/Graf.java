package sk.uniza.fri;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Graf {

    private static final int INFINITY = Integer.MAX_VALUE/2;
    private int n;                      //pocet vrcholov
    private int pocetHran;              //pocet hran grafu
    private int H[][];                  //pole udajov o hranach
    private int x[];                    //pole poslednych bodov
    private int t[];                    //pole celkovej ceny hran
    private ArrayList<Vrchol> E;        //pole potencionálnych riadiacich vrcholov
    private int S[];                    //pole smernikov

    public Graf(int pocetVrcholov, int pocetHran) {
        this.n = pocetVrcholov;
        this.pocetHran = pocetHran;
        this.H = new int[1 + this.pocetHran][3];
        this.x = new int[pocetVrcholov+1];
        this.t = new int[pocetVrcholov+1];
        this.E = new ArrayList<>();
        this.S = new int [pocetVrcholov+2];
    }

    public static Graf nacitajGraf(String nazovSuboru) {
        int pocVrcholov = 1;
        int pocHran = 0;
        try {
            Scanner sc = new Scanner(new File(nazovSuboru));

            while (sc.hasNextLine()) {
                int u = sc.nextInt();
                int v = sc.nextInt();
                int c = sc.nextInt();

                //nacital som hranu, zvysim pocet
                pocHran++;

                //skontrolujem ci netreba zvysit pocet vrcholov
                if (pocVrcholov < u) pocVrcholov = u;
                if (pocVrcholov < v) pocVrcholov = v;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        //vytvorenie a naplnenie grafu (vrchol,vrchol,cena)
        Graf graf = new Graf(pocVrcholov,pocHran);
        try {
            Scanner sc = new Scanner(new File(nazovSuboru));
            for (int j = 1; j <= pocHran; j++) {
                int u = sc.nextInt();
                int v = sc.nextInt();
                int c = sc.nextInt();

                graf.H[j][0] = u;
                graf.H[j][1] = v;
                graf.H[j][2] = c;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        return graf;
    }



    public void printInfo() {
        System.out.println("Pocet vrcholov: " + this.n);
        System.out.println("Pocet hran: " + this.pocetHran);
    }




    public void labelSet(int zacV,int konV) {
        //nastavenie pociatocnych hodnot
        for(int i = 0; i <= n; i++) {
            x[i] = 0;                      // posledný bod - predchodca
            t[i] = INFINITY;               // celková cena hrán
            S[i] = 0;
        }

        // vytvorenie smernikov
        for (int k = 1; k <= n; k++) {
            int i = H[k][0];
            if (S[i] == 0) {
                S[i] = k;
            }
        }
        S[n+1] = INFINITY;                 // oznacenie konca smernikov

        t[zacV] = 0;                       //oznacenie ze zacV je zaciatocny vrchol
        int k = 0;
        int riadiaciVrchol = zacV;

        while (riadiaciVrchol != konV) {                   //ak riadiaci vrchol je konV skonci sa algoritmus
            k = S[riadiaciVrchol];
            while (riadiaciVrchol == H[k][0] && k != INFINITY) {            // zabezpecis ze cyklus ide len pre incidentne hrany s momentalnym riadiacim vrcholom
                int i = H[k][0];                           // i = vrchol1
                int j = H[k][1];                           // j = vrchol2
                int cij = H[k][2];                         // cij = cena hrany j i

                if (t[j] > t[i] + cij) {                    //ak cena hrany ( , x) > cena hrany (x, ) + cena hrany(x,x)
                    t[j] = t[i] + cij;                      //zmenis ( ,x) cenu hrany na novu
                    x[j] = i;                               // zmenis ( ,x) bod na ktory ukazuje najkratsia cesta na (x, )
                    E.add(new Vrchol(H[k][1],t[j]));        // do epsilomu si pridam vrchol s cenou
                }
                k++;
            }
            E.sort((v1,v2) -> v1.getCena() - v2.getCena() );   //usporiada zoznam podla cien od najmensej ceny
            riadiaciVrchol = E.get(0).getVrchol();             //vrchol z najmensou cenou sa stane novy riadiaci
            E.remove(0);
            k = 1;
        }
    }






    public void vypisNajkratsiuCestu(int v) {
        int bod = v;
        List<Integer> cesta = new ArrayList<>();
        cesta.add(v);

        while (bod != 0) {
            cesta.add(this.x[bod]);
            bod = this.x[bod];
        }
        Collections.reverse(cesta);

        System.out.print("Najkratšia cesta z " + cesta.get(1) + " do " + cesta.get(cesta.size()-1) + ": ");
        for (int i = 1; i < cesta.size(); i++) {
            System.out.print(cesta.get(i) + " ");
        }
        System.out.println();
        System.out.println("(Cena: " + this.t[v] + ")");
    }
}