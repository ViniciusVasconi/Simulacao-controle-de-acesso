import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Essa classe representa um deletor.
 *
 * @author Vinicius Vasconi
 */
public class Deletor implements Runnable
{
    private BancoDeDados bd;
    private String chave;
    private final CountDownLatch startLatch;

    /**
     * Constructor for objects of class Leitor
     */
    public Deletor(BancoDeDados bd, String chave, CountDownLatch latch)
    {
        this.bd = bd;
        this.chave = chave;
        this.startLatch = latch;
    }
    
    /**
     * Gerar deletores aleatorios
     */
    public static List<Runnable> gerarDeletores(int NumDeletores, BancoDeDados bd, CountDownLatch latch){
        List<Runnable> deletores   = new ArrayList<>();
        for(int i = 0; i < NumDeletores; i++)
        {
            Random rand = new Random();
            Integer valor = rand.nextInt(bd.TamanhoMaximo);
            deletores.add(new Deletor(bd, "K" + valor, latch));
        }
        return deletores;
    }

    /**
     * Implementa o metodo run de Runnable.
     */
    @Override
    public void run()
    {
        try
        {
            startLatch.await();
            bd.bd_delete(this.chave);
        }
        catch(InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }
    }
}

