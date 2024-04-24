package id.ac.ui.cs.advprog.buy.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    WAITING_PAYMENT("WAITING_PAYMENT"),
    FAILED("FAILED"),
    SUCCESS("SUCCESS");

    private final String value;

    private TransactionStatus(String value){
        this.value = value;
    }

    public static boolean contains(String param){
        for (TransactionStatus orderStatus: TransactionStatus.values()){
            if(orderStatus.name().equals(param)){
                return true;
            }
        }
        return false;
    }
}
