import java.util.Scanner;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

/**
 * Essa classe e a responsavel por gerar os usuarios que vao interagir com o banco.
 *
 * @author Vinicius Vasconi
 */



public class Main {
    
    /**
     * Define o Tamanho maximo da tabela armazenada.
     */
    static final int TAMANHO_BANCO = 30;
    
    /**
     * Define o numero de leitores permitidos simultaneamente.
     */
    static final int NUMERO_LEITORES = 30;
    
    /**
     * Define o numero de deletores totais.
     */
    static final int NUMERO_DELETORES = 10;
    
    /**
     * Define o numero de editores totais.
     */
    static final int NUMERO_EDITORES = 10;
    
    public static void main(String[] args)
    {
        
        Scanner scan = new Scanner(System.in);
        System.out.print("Insira o numero de leitores maximos: ");
        Integer maximoLeitores = scan.nextInt();
        BancoDeDados bd = new BancoDeDados(maximoLeitores, TAMANHO_BANCO);
        
        CountDownLatch startLatch = new CountDownLatch(1);
        ExecutorService executor = Executors.newCachedThreadPool();
        
        // Geramos todas as threads
        List<Callable<Integer>> leitores = Leitor.gerarLeitores(NUMERO_LEITORES, bd, startLatch);
        List<Runnable> escritores = Escritor.gerarEscritores(bd, startLatch);
        List<Runnable> deletores  = Deletor.gerarDeletores(NUMERO_DELETORES, bd, startLatch);
        List<Runnable> editores = Editor.gerarEditores(NUMERO_EDITORES, bd, startLatch);
        
        // Lista para armazenar futuros resultados dos leitores
        Map<Leitor, Future<Integer>> mapaResultados = new HashMap<>();


        // Submeter escritores
        for (Runnable escritor : escritores) 
        {
            executor.submit(escritor);
        }
        
        // Submeter leitores
        for (Callable<Integer> leitor : leitores) 
        {
            Future<Integer> resultado = executor.submit(leitor);
            mapaResultados.put((Leitor)leitor, resultado);
        }
        
        // Submeter deletores
        for (Runnable deletor : deletores) 
        {
            executor.submit(deletor);
        }
        
        // Submeter editores
        for (Runnable editor : editores) 
        {
            executor.submit(editor);
        }
                

        System.out.println("Liberando execucao simultanea");
        startLatch.countDown();
        
        executor.shutdown();
        while (!executor.isTerminated()) 
        {
            continue;
        }
        
        System.out.println("Todos os trabalhos foram concluidos");
        for (Map.Entry<Leitor, Future<Integer>> entrada : mapaResultados.entrySet()) {
            Leitor leitor = entrada.getKey();
            Future<Integer> resultado = entrada.getValue();
            try {
                Integer valor = resultado.get();
                System.out.println("Leitor leu da chave '" + leitor.getChave() + "': " + valor);
            } catch (Exception e) {
                System.out.println("Erro ao ler da chave '" + leitor.getChave() + "': " + e.getMessage());
            }
        }
        bd.bd_print();
    }
}
