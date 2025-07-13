import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Essa classe representa um escritor.
 *
 * @author Vinicius Vasconi
 */
public class Escritor implements Runnable
{
    private BancoDeDados bd;
    private Integer valor;
    private String chave;
    private final CountDownLatch startLatch;

    /**
     * Constructor for objects of class Leitor
     */
    public Escritor(BancoDeDados bd, String chave, Integer valor, CountDownLatch latch)
    {
        this.bd = bd;
        this.chave = chave;
        this.valor = valor;
        this.startLatch = latch;
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
            bd.bd_write(this.chave, this.valor);
        }
        catch(InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }
        
    }
    
    /**
     * Gerar escritores aleatorios
     */
    public static List<Runnable> gerarEscritores(BancoDeDados bd, CountDownLatch latch){
        List<Runnable> escritores = new ArrayList<>();
        for(int i = 0; i < bd.TamanhoMaximo; i++)
        {
            Random rand = new Random();
            Integer valor = rand.nextInt();
            escritores.add(new Escritor(bd, "K" + i, valor, latch));
        }
        return escritores;
    }
}
