package projetoestacionebem;

/**
 *
 * @author milif
 */

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Estacionamento extends Thread {
    BlockingQueue<Carro> listaEsperaCarros;
    private final int tempoLimitePorVagaMilliSecondsSinceEpoch = 360000000;
    public List<Vaga> vagas = Arrays.asList(
    new Vaga(100),
    new Vaga(101),
    new Vaga(102),
    new Vaga(103),
    new Vaga(104),
    new Vaga(105),
    new Vaga(106),
    new Vaga(107),
    new Vaga(108),
    new Vaga(109),
    new Vaga(110),
    new Vaga(111)
    );

    public Estacionamento(BlockingQueue<Carro> listaEsperaCarros) {
        this.listaEsperaCarros = listaEsperaCarros;
    }

    @Override
    public void run() {

        try {
            Carro car = this.listaEsperaCarros.take();
            System.out.println("Está na garagem " + car.getPlacaString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public synchronized int consultaDispVagas() {
        for (Vaga vaga : vagas) {
            System.out.println("vagas " + vagas.get(MIN_PRIORITY));
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

    public synchronized boolean desocupaVagaPorTempo(Carro novoCarro) {

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