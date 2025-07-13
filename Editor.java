import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Essa classe representa um editor.
 *
 * @author Vinicius Vasconi
 */
public class Editor implements Runnable
{
    private BancoDeDados bd;
    private Integer valor;
    private String chave;
    private final CountDownLatch startLatch;

    /**
     * Constructor for objects of class Leitor
     */
    public Editor(BancoDeDados bd, String chave, Integer valor, CountDownLatch latch)
    {
        this.bd = bd;
        this.chave = chave;
        this.valor = valor;
        this.startLatch = latch;
    }
    
    /**
     * Gerar editores aleatorios
     */
    public static List<Runnable> gerarEditores(int NumEditores, BancoDeDados bd, CountDownLatch latch){
        List<Runnable> editores   = new ArrayList<>();
        for(int i = 0; i < NumEditores; i++)
        {
            Random rand = new Random();
            Integer chave = rand.nextInt(bd.TamanhoMaximo);
            Integer valor = rand.nextInt();
            editores.add(new Editor(bd, "K" + chave,valor, latch));
        }
        return editores;
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
            bd.bd_update(this.chave, this.valor);
        }
        catch(InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }
    }
}
