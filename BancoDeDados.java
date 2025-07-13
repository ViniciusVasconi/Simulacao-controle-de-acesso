import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.*;
import java.util.HashMap;
import java.util.Map;


/**
 * O banco de dados principal, que conta com controle de concorrencia.
 *
 * @author (Vinicius Vasconi)
 */

public class BancoDeDados
{
    private Map<String, Integer> df;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock leitura = lock.readLock();
    private Lock escrita = lock.writeLock();
    private Semaphore controleMaxLeitores;
    public final int TamanhoMaximo;

    /**
     * Constructor for objects of class BancoDeDados
     * @param maxLeitores Define o maximo de leitores permitidos no banco
     */
    public BancoDeDados(int maxLeitores, int tamanhoMaximo)
    {
        this.controleMaxLeitores = new Semaphore(maxLeitores);
        this.TamanhoMaximo = tamanhoMaximo;
        this.df = new HashMap<String,Integer>(tamanhoMaximo);
    }
    
    /**
     * Metodo de leitura do Banco de dados.
     *
     * @param  key informa o valor que deseja ler da tabela
     * @return  Retorna o valor inteiro associado
     */
    public Integer bd_read(String key)
    {
        leitura.lock();
        Integer valor = df.get(key);
        if(valor == null)
        {
            System.out.println("Leu uma chave que nao existe");
        }
        else
        {
            System.out.println("Leu na chave" + key);
        }
        leitura.unlock();
        return valor;
    }
    
    /**
     * Metodo de escrita do Banco de dados.
     *
     * @param  key informa a chave que deseja escrever na tabela
     * @param  value informa o valor que deseja escrever na tabela
     */
    public void bd_write(String key, Integer value)
    {
        escrita.lock();
        if(df.putIfAbsent(key, value)!=null)
        {
            System.out.println("Escreveu em chave ja existente.");
        }
        else
        {
            System.out.println("Escreveu em " + key + " " + value);
        }
        escrita.unlock();
    }
    
    /**
     * Metodo de atualizacao do Banco de dados.
     *
     * @param  key informa o valor que deseja ler da tabela
     * @param  value informa o valor que deseja ler da tabela
     */
    public void bd_update(String key, Integer value)
    {
        escrita.lock();
        if(df.containsKey(key))
        {
            df.put(key, value);
            System.out.println("Atualizou " + key + " para " + value);
        }
        else
        {
            System.out.println("Tentou atualizar chave que nao existe.");
        }
        escrita.unlock();
    }
    
    /**
     * Metodo de escrita do Banco de dados.
     *
     * @param  key informa a chave que deseja remover da tabela
     */
    public void bd_delete(String key)
    {
        escrita.lock();
        if(df.containsKey(key))
        {
            df.remove(key);
            System.out.println("Deletou " + key);
        }
        else
        {
            System.out.println("Deletou chave que nao existe");
        }
        escrita.unlock();
    }
    
    /**
     * Metodo para exibir a tabela do banco de dados.
     */
    public synchronized void bd_print(){
        for(Map.Entry<String, Integer> entry : df.entrySet())
        {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("Chave:\t" + key + "\tValor:\t" + value);
        }
    }
    
    /**
     * Metodo para retornar o tamanho da tabela do banco de dados.
     */
    public synchronized int getDfSize(){
        return df.size();
    }
    
}
