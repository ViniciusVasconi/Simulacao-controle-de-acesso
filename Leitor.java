import java.util.concurrent.Callable;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Essa classe representa um leitor.
 *
 * @author Vinicius Vasconi
 */
public class Leitor implements Callable<Integer>
{
    private BancoDeDados bd;
    private Integer dadoLido;
    private String chave;
    private final CountDownLatch startLatch;

    /**
     * Constructor for objects of class Leitor
     */
    public Leitor(BancoDeDados bd, String chave, CountDownLatch latch)
    {
        this.bd = bd;
        this.chave = chave;
        this.startLatch = latch;
    }

    /**
     * Implementa o metodo Call de Callable.
     * 
     * @return valor Retorna o valor lido.
     */
    @Override
    public Integer call()
    {
        try 
        {
            startLatch.await();
            return bd.bd_read(this.chave);
        } 
        catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    /**
     * Gerar leitores aleatorios
     */
    public static List<Callable<Integer>> gerarLeitores(int NumLeitores, BancoDeDados bd, CountDownLatch latch){
        List<Callable<Integer>> leitores = new ArrayList<>();
        for(int i = 0; i < NumLeitores; i++)
        {
            Random rand = new Random();
            Integer chave = rand.nextInt(bd.TamanhoMaximo);
            leitores.add(new Leitor(bd, "K" + chave, latch));
        }
        return leitores;
    }
    
    /**
     * Retorna a chave.
     */
    public String getChave(){
        return this.chave;
    }
}

