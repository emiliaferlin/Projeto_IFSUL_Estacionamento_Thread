package projetoestacionebem;

/**
 *
 * @author milif
 */

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Estacionamento extends Thread {
    BlockingQueue<Carro> listaEsperaCarros;
    private final int tempoLimitePorVagaMilliSecondsSinceEpoch = 3600;
    public ArrayList<Vaga> vagas = new ArrayList(12);
    int pegaCarro = -1;

    public Estacionamento(BlockingQueue<Carro> listaEsperaCarros) {
        this.listaEsperaCarros = listaEsperaCarros;
    }

    @Override
    public void run() {
        
        while(pegaCarro <= 12){
            try {
            Carro car = this.listaEsperaCarros.take();
            System.out.println("Está na garagem " + car.getPlacaString());
            
                if(vagas.size() == 12){
                    desocupaVagaPorTempo();
                    System.out.println("aqui veio");
                }
                Thread.sleep(800);
            } catch (InterruptedException e) {
            e.printStackTrace();
            } 
        }      
    }

    public synchronized int consultaDispVagas() {
        for (Vaga vaga : vagas) {
            Carro car = vaga.getCarro();
            if (car == null) {
                return vagas.indexOf(vaga);
                
            }
        }
        return -1;
    }

    public synchronized boolean chegaCarro(Carro novoCarro) {
        int codigoVaga = consultaDispVagas();
        if (codigoVaga != -1) {
            Vaga tmp = vagas.get(codigoVaga);
            tmp.setCarro(novoCarro);
            vagas.set(codigoVaga, tmp);
            return true;
        }
        return false;
    }

    public synchronized boolean desocupaVagaPorTempo() {
        for (Vaga vaga : vagas) {
            int time = vaga.getDt();
            if (time > 0) {
                if (LocalDateTime.now().getNano() + tempoLimitePorVagaMilliSecondsSinceEpoch > time) {
                    Vaga tmp = vaga;
                    tmp.setCarro(null);
                    vagas.set(vagas.indexOf(vaga), tmp);
                }

            }
        }
        return false;
    }
}