package org.khasanof.uicachingstrategy.data;

import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.repository.TransactionRepository;
import org.khasanof.uicachingstrategy.service.ContextTransactionServices;
import org.khasanof.uicachingstrategy.service.GetTransactionsService;
import org.khasanof.uicachingstrategy.service.humo.HumoTransactionService;
import org.khasanof.uicachingstrategy.service.uzcard.UzCardTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/7/2023
 * <br/>
 * Time: 7:10 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.data
 */
@Component
public class CommandLiner implements CommandLineRunner {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private ContextTransactionServices contextTransactionServices;

    @Override
    public void run(String... args) throws Exception {
//        ContextTransactionServices context = new ContextTransactionServices(new UzCardTransactionService(),
//                new HumoTransactionService());

        GetTransactionsService service = new GetTransactionsService(contextTransactionServices, repository);

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 23, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 25, 0, 0);
        List<TransactionEntity> list = service.getAllTransactionByDate("8600937995190824", from1, to1);
        System.out.println("list = " + list);
    }
}
