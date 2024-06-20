import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

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

    

    public void tracaCaminho7() {
        int troca = 0;
        int mutation_boost = 0;
        int population_survivors_count = 50;
        int population_count = 150;
        int count_add_analyse = 0;
        int count = 0;
        int count_add = 0;
        ArrayList<Cidade> cit_2 = new ArrayList<>(cidades);
        ArrayList<Cidade> cit_3 = new ArrayList<>(cidades);
        Collections.shuffle(cit_2);
        Collections.shuffle(cit_3);
        Caminho c_1 = new Caminho(cidades);
        Caminho c_2 = new Caminho(cit_2);
        Caminho c_3 = new Caminho(cit_3);
        System.out.println("Caminho 1:" + c_1.getCaminhoVal());
        System.out.println("Caminho 2:" + c_2.getCaminhoVal());
        System.out.println("Caminho 3:" + c_3.getCaminhoVal());
        ArrayList<Caminho> selec = new ArrayList<>();
        selec.add(c_1);
        selec.add(c_2);
        selec.add(c_3);
        Caminho goat = new Caminho(cit_2);
        ArrayList<Cidade> cit_x_prev  = cidades;
        while (selec.size()< 300) {
            ArrayList<Cidade> cit_x = new ArrayList<>(cit_x_prev);
            Collections.shuffle(cit_x); 
            Caminho c_x = new Caminho(cidades);
            selec.add(c_x);
            cit_x_prev = cit_x;
        }
    
        while (true) {
    
            Collections.sort(selec);
            while (selec.size() >=  population_survivors_count + 1){
                selec.remove(selec.size()-1);
            }
    
            if(melhor_caminho > selec.get(0).getCaminhoVal()){
                melhor_caminho = selec.get(0).getCaminhoVal();
                goat.caminho = new ArrayList<>(selec.get(0).caminho);
                System.out.println("Iteracoes:" + count + " | Melhor caminho:" + melhor_caminho + " | Tamanho da populacao:" + selec.size());
                count_add = 0;
                cidades = new ArrayList<Cidade>(selec.get(0).caminho);
                troca = 0;
            }
    
            if(count_add == 25){
                if(troca > 1){
                    for(int i = 0; i<10; i++){
                        mutar3(selec.get(i));
                    }
                }

                
                for(int i = 0; i<10; i++){
                    mutar2(selec.get(i));
                }
                for(int i = 11; i<30; i++){
                    mutar(selec.get(i));
                }
                for(int i = 31; i<selec.size(); i++){
                    Collections.shuffle(selec.get(i).caminho);
                    selec.get(i).CalculaCaminho();
                }
                System.out.println("Mutacao feita");
                count_add = 0;
                mutation_boost = (int) (population_count * 0.7);
                count_add_analyse = count_add_analyse + mutation_boost;
                population_count = population_count + 180;
                population_survivors_count = population_survivors_count + 30;
                selec.add(goat);
                
                //continue;
                troca++;
            }

            for(int i = 0; i <count_add_analyse; i++){
                Random rand = new Random();
                int size = selec.size();
                ArrayList<Cidade> random = new ArrayList<>(selec.get(rand.nextInt(size)).caminho);
                Collections.shuffle(random);
                Caminho c = new Caminho(random);
                selec.add(c);
            }
    
            
            ArrayList<Caminho> nova_geracao = new ArrayList<>();
        
            while(nova_geracao.size() <population_count){
                Random rand = new Random();
                int size = selec.size();
                Caminho parent1 = selec.get(rand.nextInt(size));
                Caminho parent2 = selec.get(rand.nextInt(size));
                Caminho child = crossover(parent1, parent2);
                nova_geracao.add(child);
            }
            nova_geracao.add(goat);
             
           
            
            
            
            selec = nova_geracao;

    
            count_add_analyse = count_add_analyse - mutation_boost;
            count_add++;
            count++;
            mutation_boost = 0;
        }
    }

    private Caminho crossover(Caminho parent1, Caminho parent2) {
        Random rand = new Random();
        int size = parent1.getCaminho().size();
        int start = rand.nextInt(size);
        int end = rand.nextInt(size - start) + start;
    
        ArrayList<Cidade> childPath = new ArrayList<>(Collections.nCopies(size, null));
    
        for (int i = start; i <= end; i++) {
            childPath.set(i, parent1.getCaminho().get(i));
        }
    

        int currentIndex = 0;
        for (int i = 0; i < size; i++) {
            Cidade cidade = parent2.getCaminho().get(i);
            if (!childPath.contains(cidade)) {
                while (childPath.get(currentIndex) != null) {
                    currentIndex++;
                }
                childPath.set(currentIndex, cidade);
            }
        }
    
        return new Caminho(childPath);
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

    private void mutar3(Caminho caminho) {
        Random rand = new Random();
        int size = caminho.caminho.size();
        int i = rand.nextInt(size);
        int j = rand.nextInt(size);
    
        swap(caminho.caminho.get(i), caminho.caminho.get(j));
    
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

        public double getCaminhoVal() {
            return caminhoVal;
        }
    
        public void setCaminhoVal(double caminhoVal) {
            this.caminhoVal = caminhoVal;
        }
    
        public ArrayList<Cidade> getCaminho() {
            return caminho;
        }
    
        public void setCaminho(ArrayList<Cidade> caminho) {
            this.caminho = caminho;
            CalculaCaminho(); // Recalculate the path value when the path is set
        }
    
        public int getIndexMaiorDistancia() {
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
        b.leituraDeArquivo(args[0]);
        b.tracaCaminho7();
    }




}
