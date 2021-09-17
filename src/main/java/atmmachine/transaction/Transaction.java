package atmmachine.transaction;

import atmmachine.enums.TransactionStatus;

import java.util.Date;

public class Transaction {

    private String id;
    private final Date creationDate = new Date();
    private Date startDate;
    private Date completionDate;

    private TransactionStatus transactionStatus;


}
