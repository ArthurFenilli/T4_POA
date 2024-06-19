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

    private ArrayList<Cidade> cidades;
    private double melhor_caminho;

    public BuscaCaminho(){
        cidades = new ArrayList<>();
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
        int count_add = 0;
        ArrayList<Cidade> cit_2 = new ArrayList<>(cidades);
        ArrayList<Cidade> cit_3 = new ArrayList<>(cidades);
        Collections.shuffle(cit_2);
        Collections.shuffle(cit_3);
        Caminho c_1 = new Caminho(cidades);
        Caminho c_2 = new Caminho(cit_2);
        Caminho c_3 = new Caminho(cit_3);
        System.out.println("Caminho 1:" + c_1.caminhoVal);
        System.out.println("Caminho 2:" + c_2.caminhoVal);
        System.out.println("Caminho 3:" + c_3.caminhoVal);
        ArrayList<Caminho> selec = new ArrayList<>();
        //selec.add(c_1);
        //selec.add(c_2);
        selec.add(c_3);

        while (true) {

            Collections.sort(selec);
            if (selec.size() == 31) {
                selec.remove(selec.size()-1);
            }

            if(melhor_caminho > selec.get(0).caminhoVal){
                melhor_caminho = selec.get(0).caminhoVal;
                System.out.println("Iterações:" + count + " | Melhor caminho:" + melhor_caminho + " | Tamanho da população:" + selec.size());
                System.out.println(c_1.caminhoVal);
                count_add = 0;
            }
            if(count_add == 10000){
                //ArrayList<Cidade> c = new ArrayList<>(selec.get(0).caminho);
                //Caminho c_x = new Caminho(c);
                //selec.add(c_x);
                //count_add = 0;
                //continue;
            }
            for (Caminho c  : selec) {
                mutar2(c);   
            }

            count_add++;
            count++;
        }

    }

    private void mutar(Caminho caminho) {
        Random rand = new Random();
        int size = caminho.caminho.size();
        int i = rand.nextInt(size);
        int j = rand.nextInt(size);
    
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
    
        while (i < j) {
            swap(caminho.caminho.get(i), caminho.caminho.get(j));
            i++;
            j--;
        }
    
        caminho.CalculaCaminho();
    }

    private void mutar2(Caminho caminho){
        Random rand = new Random();
        int size = caminho.caminho.size();
        int i = rand.nextInt(size);
        int index = caminho.getMaiorIndex();
        swap(caminho.caminho.get(index), caminho.caminho.get(i));
        caminho.CalculaCaminho();
    }
    
    

    private void swap(Cidade c1, Cidade c2){
        String aux_nome;
        double aux_latitude;
        double aux_longitude;

        aux_nome = c1.getNome();
        aux_latitude = c1.getLatitude();
        aux_longitude = c1.getLongitude();

        c1.setNome(c2.getNome());
        c1.setLatitude(c2.getLatitude());
        c1.setLongitude(c2.getLongitude());

        c2.setNome(aux_nome);
        c2.setLatitude(aux_latitude);
        c2.setLongitude(aux_longitude);

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

        public void setNome(String nome){
            this.nome = nome;
        }

        public void setLatitude(double latitude){
            this.latitude = latitude;
        }

        public void setLongitude(double longitude){
            this.longitude = longitude;
        }


    }

    class Caminho implements Comparable<Caminho>{
        private double caminhoVal;
        private ArrayList<Cidade> caminho;
        private int index_maior_distancia;

        public Caminho (ArrayList<Cidade> caminho){
            this.caminho = caminho;
            CalculaCaminho();
            
        }

        private void CalculaCaminho(){
            double lat_prev = -1;
            double long_prev = -1;
            double lat_cur = 0;
            double long_cur = 0;
            double dist = 0;
            double maior_dist = 0;
            int count = 0;


            for(Cidade c : caminho){
                lat_cur = c.getLatitude();
                long_cur = c.getLongitude();
                if(lat_prev != -1){
                    double dist_sum = Math.sqrt(Math.pow(lat_cur - lat_prev, 2) + Math.pow(long_cur - long_prev, 2));
                    dist = dist + dist_sum;
                    if(maior_dist <dist_sum){
                        maior_dist = dist_sum;
                        this.index_maior_distancia = count - 1;
                    }
                }
                lat_prev = lat_cur;
                long_prev = long_cur;
                count++;
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

        public int getMaiorIndex(){
            return index_maior_distancia;
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
