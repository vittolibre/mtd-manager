package mtd.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MTDStrategyService implements ApplicationRunner {

    @Autowired
    ServiceAccountStrategyService ServiceAccountStrategyService;
    @Autowired
    IPShufflingStrategyService IPShufflingStrategyService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(ServiceAccountStrategyService, "sa-shuffling").start();
        new Thread(IPShufflingStrategyService, "ip-shuffling").start();
    }
}
