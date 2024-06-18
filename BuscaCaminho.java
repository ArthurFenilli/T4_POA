import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Random;

public class BuscaCaminho {

    private LinkedList<Cidade> cidades;
    private double melhor_caminho;

    public BuscaCaminho(){
        cidades = new LinkedList<>();
        melhor_caminho = Double.MAX_VALUE;
    }

    private void  leituraDeArquivo(String caminho){
        Path path = Paths.get(caminho);

        try(BufferedReader reader = Files.newBufferedReader(path)){
            String line;

            while((line = reader.readLine()) != null){
                try {
                    String aux[] = line.split(" ");
                    double latitude = Double.parseDouble(aux[0]);
                    double longitude = Double.parseDouble(aux[1]);
                    String nome = aux[2];
                    Cidade cidade = new Cidade(nome, latitude, longitude);
                    cidades.add(cidade);

                } catch (Exception e) {
                    System.out.println("A");
                }
            }
        }
        catch(Exception e){
            System.out.println("Erro ao ler o arquivo.");
        }
    }

    public void tracaCaminho(){
        int count = 0;
        LinkedList<Cidade> cit_2 = new LinkedList<>(cidades);
        LinkedList<Cidade> cit_3 = new LinkedList<>(cidades);
        Collections.shuffle(cit_2);
        Collections.shuffle(cit_3);
        Caminho c_1 = new Caminho(cidades);
        Caminho c_2 = new Caminho(cit_2);
        Caminho c_3 = new Caminho(cit_3);
        System.out.println("Caminho 1:" + c_1.caminhoVal);
        System.out.println("Caminho 2:" + c_2.caminhoVal);
        System.out.println("Caminho 3:" + c_3.caminhoVal);
        ArrayList<Caminho> selec = new ArrayList<>();
        selec.add(c_1);
        selec.add(c_2);
        selec.add(c_3);

        while (true) {

            Collections.sort(selec);

            if(melhor_caminho > selec.get(0).caminhoVal){
                melhor_caminho = selec.get(0).caminhoVal;
            }
            if(count %500 == 0){
                System.out.println("Iterações:" + count + " | Melhor caminho:" + melhor_caminho);
            }

            mutar(selec.get(0));


            count++;
        }

    }

    private void mutar(Caminho caminho) {
        Random rand = new Random();
        int i = rand.nextInt(caminho.caminho.size());
        int j = rand.nextInt(caminho.caminho.size());
        Collections.swap(caminho.caminho, i, j);
        caminho.CalculaCaminho();
    }

    private void reproduzir(){
        
    }



    class Cidade{
        private String nome;
        private double latitude;
        private double longitude;
    
    
        public Cidade(String nome, double latitude, double longitude){
            this.nome = nome;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getNome(){
            return nome;
        }

        public double getLatitude(){
            return latitude;
        }

        public double getLongitude(){
            return longitude;
        }


    }

    class Caminho implements Comparable<Caminho>{
        private double caminhoVal;
        private LinkedList<Cidade> caminho; 

        public Caminho(LinkedList<Cidade> caminho){
            this.caminho = caminho;
            CalculaCaminho();
            
        }

        private void CalculaCaminho(){
            double lat_prev = -1;
            double long_prev = -1;
            double lat_cur = 0;
            double long_cur = 0;
            double dist = 0;

            for(Cidade c : caminho){
                lat_cur = c.getLatitude();
                long_cur = c.getLongitude();
                if(lat_prev != -1){
                    dist = dist + Math.sqrt(Math.pow(lat_cur - lat_prev, 2) + Math.pow(long_cur - long_prev, 2));
                }
                lat_prev = lat_cur;
                long_prev = long_cur;
            }

            lat_cur = caminho.get(0).getLatitude();
            long_cur = caminho.get(0).getLongitude();

            dist = dist + Math.sqrt(Math.pow(lat_cur - lat_prev, 2) + Math.pow(long_cur - long_prev, 2));
            caminhoVal = dist;
        }

    
        @Override
        public int compareTo(Caminho c) {
            if(this.caminhoVal < c.caminhoVal){
                return -1;
            }
            else if(this.caminhoVal > c.caminhoVal){
                return 1;
            }
            return 0;
        }
    }

    private void listarCidades(){
        for(Cidade c: cidades){
            System.out.println(c.getNome());
        }
    }

    public static void main(String args[]){
        BuscaCaminho b = new BuscaCaminho();
        b.leituraDeArquivo("data.txt");
        b.tracaCaminho();
    }




}
