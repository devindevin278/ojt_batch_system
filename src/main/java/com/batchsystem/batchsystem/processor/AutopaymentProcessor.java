package com.batchsystem.batchsystem.processor;

import com.batchsystem.batchsystem.model.Loan;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class AutopaymentProcessor implements ItemProcessor<Loan, Loan> {

    @Value("${endpoint.debit_deposit}")
    private String debit_deposit;

    @Value("${endpoint.find_deposit_cin}")
    private String find_deposit_cin;

    @Value("${endpoint.find_deposit_id}")
    private String find_deposit_id;

    @Value("${endpoint.close_loan}")
    private String close_loan;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Loan process(Loan item) throws Exception {

        System.out.println("Ini processor");
        LocalDate currentDate = LocalDate.now();

//      extract maturity date
        LocalDate maturity_date = item.getMaturity_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate();

//        try {
            //        checking all loan that is open and maturity date is due
        if (maturity_date.equals(currentDate)) {

//          finding all open deposits owned by a customer by cin
            List<Long> deposit_ids = findDepositId(item.getCin());
            System.out.println("ids: "+deposit_ids);

//          calculate total due amount
            Double initial_due_amount = item.getInterest_amount() + item.getBalance();
            Double due_amount = initial_due_amount;

            int index = 0;
//          deduct from all deposit available
            while(due_amount > 0 && index < deposit_ids.size()) {
                due_amount = debitDeposit(due_amount, deposit_ids.get(index));
                System.out.println(due_amount);
                index++;
            }

//          update balance and interest amount
            if(due_amount > item.getBalance()) {
                item.setInterest_amount(due_amount - item.getBalance());
            } else {
                item.setInterest_amount(0.0);
                item.setBalance(due_amount);
            }

            if (due_amount == 0.0) { // lunas
                item.setStatus_id((long)2);

//          call API loan to close loan
                String url = close_loan;

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam("account_id", item.getAccount_id());

                String finalUrl = builder.toUriString();

                ResponseEntity<Object> responseEntity = restTemplate.postForEntity(finalUrl, null, Object.class);

            } else {
//                defaulted loan
                item.setStatus_id((long)3);
            }


//            save to transaction
            item.setTransactionType((long)2);
            item.setChanged_amount(initial_due_amount - due_amount);

            return item;

        } else {
            System.out.println(item.getAccount_id());
            System.out.println("filtered status: " + item.getStatus_id() + " " + item.getMaturity_date().toString());
            return null;
        }
//        } catch(Exception e) {
//            e.printStackTrace();
//            return null;
//        }


    }

//    to return deposit ids owned by a cin
    public List<Long> findDepositId(Long cin) throws IOException {
        List<Long> account_ids = new ArrayList<>();

//        call api deposit system to retrieve ids
        String url = find_deposit_cin;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("cin", cin);

        String finalUrl = builder.toUriString();

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(finalUrl, null, Object.class);

        LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) responseEntity.getBody();
        ArrayList<Object> result_data = (ArrayList<Object>) result.get("data");

        for (Object item : result_data) {
            LinkedHashMap<String, Object> deposit = (LinkedHashMap<String, Object>) item;

            Double balance = (Double)deposit.get("balance");

//            add if status still open and balance is more than 0
            if(deposit.get("status").equals("open") && balance > 0) {
                Integer intValue = (Integer)deposit.get("accountId");
                account_ids.add(intValue.longValue());
            }
        }


        return account_ids;
    }

    public Double debitDeposit(double due_amount, Long account_id) {
        //       call API deposit to find the deposit
        String url1 = find_deposit_id ;


        UriComponentsBuilder builder1 = UriComponentsBuilder.fromHttpUrl(url1)
                .queryParam("account_id", account_id);

        String finalUrl1 = builder1.toUriString();

        ResponseEntity<Object> responseEntity1 = restTemplate.postForEntity(finalUrl1, null, Object.class);

        LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) responseEntity1.getBody();
        LinkedHashMap<String, Object> result_data = (LinkedHashMap<String, Object>) result.get("data");

        Double balance = (Double) result_data.get("balance");

//        calculate amount to debit from deposit
        Double nominal = (balance >= due_amount) ? due_amount : balance;

//        call API deposit to deduct the balance
        String url2 = debit_deposit;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url2)
                .queryParam("nominal", nominal)
                .queryParam("deposit_id", account_id);

        String finalUrl = builder.toUriString();

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(finalUrl, null, Void.class);

//        return sisa
        return due_amount - nominal;
    }


}
