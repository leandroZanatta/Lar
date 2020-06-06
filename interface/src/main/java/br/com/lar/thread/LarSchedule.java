package br.com.lar.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.lar.service.processamento.ProcessamentoDiarioService;

public class LarSchedule {

    private static LarSchedule instance;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private ProcessamentoDiarioService processamentoDiarioService = new ProcessamentoDiarioService();

    private LarSchedule() {

    }

    public void startSchedule() {

        executorService.scheduleAtFixedRate(() -> processamentoDiarioService.iniciarProcessamento(), 0, 5, TimeUnit.MINUTES);
    }

    public static LarSchedule getInstance() {

        if (instance == null) {
            instance = new LarSchedule();
        }

        return instance;
    }

}
