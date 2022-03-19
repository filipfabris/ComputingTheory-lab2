import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {

    TreeMap<String, TreeMap<String, String>> tranzicije = new TreeMap<String, TreeMap<String, String>>();

    LinkedList<String> stanja = new LinkedList<>(); //Stanja
    LinkedList<String> abeceda = new LinkedList<>(); //Abeceda
    LinkedList<String> prihvatljivaStanja = new LinkedList<>(); //PrihvatljivaStanja
    String poctnoStanje = null; //Pocetno stanje

    LinkedList<String> neprihvatljivaStanja = new LinkedList<>();

    public int DEFAULT = 0;
    public int ISTOVJETNI = 1;
    public int RAZLICITI = 2;

    public Program() {}

    HashSet<String> visited = new HashSet<>();
    public void nedohvatljivaStanja(){
        nedohvatljivaStanjaRec(poctnoStanje);

        LinkedList<String> temp = new LinkedList<>();
        for(String stanje: stanja){
            if(visited.add(stanje) == true){   //Nadeno nedohvatljivo stanje
                tranzicije.remove(stanje);
                temp.add((stanje));
            }
        }
        for(String x: temp){
            stanja.remove(x);
        }
    }

    public void nedohvatljivaStanjaRec(String trenutnoStanje){

        if(visited.contains(trenutnoStanje)){
            return;
        }

        visited.add(trenutnoStanje);

        TreeMap<String, String> temp = tranzicije.get(trenutnoStanje);
        for(String unos: temp.keySet()){
            nedohvatljivaStanjaRec(temp.get(unos));
        }
        return;
    }

    int[][] output;
    int size;
   public void ekvivalentnaStanja(){
       size = stanja.size()-1;
       Collections.sort(stanja);
        int[][] matrica = new int[size][size];
        output = matrica;

        for(int i = 0; i < size; i++){
            for(int j =0; j<=i; j++){

                int x = ekvivalentnaStanjaRec(stanja.get(j), stanja.get(i+1));
                matrica[i][j] = x;
            }
        }

    }
    int brojac = 10;
    public int ekvivalentnaStanjaRec(String s1, String s2){

        if(prihvatljivaStanja.contains(s1) && neprihvatljivaStanja.contains(s2)){
            return RAZLICITI;
        }else if(prihvatljivaStanja.contains(s2) && neprihvatljivaStanja.contains(s1)){
            return RAZLICITI;
        }

        String prvi;
        String drugi;
        var mapaPrvi = tranzicije.get(s1);
        var mapaDrugi = tranzicije.get(s2);

        Iterator iteratorPrvi = mapaPrvi.keySet().iterator();
        Iterator iteratorDrugi = mapaDrugi.keySet().iterator();

        int zastavica_isti = 0;

        while(iteratorPrvi.hasNext() && iteratorDrugi.hasNext()){
            String input1 = iteratorPrvi.next().toString();
            String input2 = iteratorDrugi.next().toString();

            prvi = mapaPrvi.get(input1);
            drugi = mapaDrugi.get(input2);

            if(prvi.equals(drugi)){
                zastavica_isti++;
            }

            else if(prihvatljivaStanja.contains(prvi) && neprihvatljivaStanja.contains(drugi)){
                return RAZLICITI;
            }

           else if(prihvatljivaStanja.contains(drugi) && neprihvatljivaStanja.contains(prvi)){
                return RAZLICITI;
            }

        }

        if(zastavica_isti == abeceda.size()){
            return ISTOVJETNI;
        }

        iteratorPrvi = mapaPrvi.keySet().iterator();
        iteratorDrugi = mapaDrugi.keySet().iterator();

        while(iteratorPrvi.hasNext() && iteratorDrugi.hasNext()) {
            String input1 = iteratorPrvi.next().toString();
            String input2 = iteratorDrugi.next().toString();

            prvi = mapaPrvi.get(input1);
            drugi = mapaDrugi.get(input2);

            if(prvi.equals(drugi)){
                continue;
            }

            int output = Rekurzija(prvi, drugi, brojac++);
            return output;
        }

        return DEFAULT;
    }

    public int Rekurzija(String s2, String s1, int zastavica){
        int index1;
        int index2;

        if(stanja.indexOf(s1) < stanja.indexOf(s2)){
            index1 = stanja.indexOf(s2)-1;
            index2 = stanja.indexOf(s1);
        }else{
            index1 = stanja.indexOf(s1)-1;
            index2 = stanja.indexOf(s2);
        }

       if(output[index1][index2] == RAZLICITI){
           return RAZLICITI;
       }

       if(output[index1][index2] == ISTOVJETNI){
           return ISTOVJETNI;
       }

        if(output[index1][index2] == zastavica){
            output[index1][index2] = ISTOVJETNI;
            return  ISTOVJETNI;
        }

        int vrati = 0;
        if(output[index1][index2] == DEFAULT){
            output[index1][index2] = zastavica;

            var mapaPrvi = tranzicije.get(s2);
            var mapaDrugi = tranzicije.get(s1);

            Iterator iteratorPrvi = mapaPrvi.keySet().iterator();
            Iterator iteratorDrugi = mapaDrugi.keySet().iterator();

            while(iteratorPrvi.hasNext() && iteratorDrugi.hasNext()) {

                String input1 = iteratorPrvi.next().toString();
                String input2 = iteratorDrugi.next().toString();

                String prvi = mapaPrvi.get(input1);
                String drugi = mapaDrugi.get(input2);

                if (prvi.equals(drugi)) {
                    continue;
                }

                vrati = Rekurzija(prvi, drugi, zastavica);
                output[index1][index2] = DEFAULT;
                //output[index1][index2] = vrati;  -  ubrzanje
                return vrati;
            }
        }

       return vrati;
    }

    public void removeEkvivalentnaStanja(){
        for(int i = 0; i < size; i++){
            for(int j =0; j<=i; j++){
                if(output[i][j]==1){
                    tranzicije.remove(stanja.get(i+1));
                }
            }
        }
    }

    public void printMatrica(){
        for(int i = 0; i < size; i++){
            for(int j =0; j<=i; j++){
                System.out.print(output[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printTranzicije(){
        for(String key : tranzicije.keySet()){
            System.out.println(key);
            TreeMap<String, String> temp = tranzicije.get(key);
            for(String innerkey: temp.keySet()){
                System.out.println("   " + innerkey + "--->" + temp.get(innerkey));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Program automat = new Program();


        String s = reader.readLine();
        String[] str = s.split(",");
        for(String i: str){
            automat.stanja.push(i);
        }

        s = reader.readLine();
        str = s.split(",");
        for(String i: str){
            automat.abeceda.push(i);
        }

        s = reader.readLine();
        str = s.split(",");
        for(String i: str){
            automat.prihvatljivaStanja.push(i);
        }

        s = reader.readLine();
        automat.poctnoStanje = s;

        s = reader.readLine(); //Pocetak tranzicija
        while (s != null) {

            String pocetno;
            Pattern pattern3 = Pattern.compile("^(.*?),");
            Matcher matcher3 = pattern3.matcher(s);
            matcher3.find();
            pocetno = matcher3.group(1);

            String unos;
            Pattern pattern2 = Pattern.compile(",(.*?)->");
            Matcher matcher2 = pattern2.matcher(s);
            matcher2.find();
            unos = matcher2.group(1);

            String zavrsna;
            Pattern pattern = Pattern.compile("->(.*?)$");
            Matcher matcher = pattern.matcher(s);
            matcher.find();
            zavrsna = matcher.group(1);

            if(automat.tranzicije.containsKey(pocetno) == false){
                TreeMap<String, String> temp = new TreeMap<>();
                temp.put(unos, zavrsna);
                automat.tranzicije.put(pocetno, temp);
            }

            automat.tranzicije.get(pocetno).put(unos, zavrsna);

            s = reader.readLine();
        }

        for(String i: automat.stanja){
            if(!automat.prihvatljivaStanja.contains(i)){
                automat.neprihvatljivaStanja.push(i);
            }
        }

        automat.printTranzicije();

        System.out.println("Poslije provedne nedohvatljivih stanja");
        automat.nedohvatljivaStanja();
        automat.printTranzicije();

        automat.ekvivalentnaStanja();
        System.out.println();

        automat.printMatrica();

        System.out.println();
        automat.removeEkvivalentnaStanja();

        automat.printTranzicije();

    }

}
