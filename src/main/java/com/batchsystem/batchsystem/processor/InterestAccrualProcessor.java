package com.batchsystem.batchsystem.processor;

import com.batchsystem.batchsystem.model.Loan;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InterestAccrualProcessor implements ItemProcessor<Loan, Loan> {

    @Value("${endpoint.kredit_loan}")
    private String kredit_loan;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Loan process(Loan item) throws Exception {
        System.out.println("Ini processor");
        //        calculate daily interest
        if (item.getStatus_id() != 2) {
            Double daily_interest = item.getInterest_rate() * item.getBalance() / 36000;
            Double newInterest = item.getInterest_amount() + daily_interest;
            item.setInterest_amount(newInterest);
            item.setChanged_amount(daily_interest);
            item.setTransactionType((long)1);

            return item;
        } else {
            return null;
        }

//        call API loan system to add transaction (debit) to loan
//        String url = kredit_loan;
//
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("nominal", daily_interest)
//                .queryParam("loan_id", item.getAccount_id());
//
//        String finalUrl = builder.toUriString();
//
//        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(finalUrl, null, Void.class);

    }
}
