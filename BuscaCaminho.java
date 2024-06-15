import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class BuscaCaminho {

    private double caminhoVal;
    private ArrayList<Cidade> cidades;

    public BuscaCaminho(){
        cidades = new ArrayList<>();
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
        while(true){

        }
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




    }

    private void listarCidades(){
        for(Cidade c: cidades){
            System.out.println(c.getNome());
        }
    }

    public static void main(String args[]){
        BuscaCaminho b = new BuscaCaminho();
        b.leituraDeArquivo("data.txt");
        //b.listarCidades();
        System.out.println(b.cidades.size());
    }




}
