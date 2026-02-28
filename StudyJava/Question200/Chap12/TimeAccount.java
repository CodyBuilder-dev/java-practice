class Account {
    private String name;
    private String no;
    private long balance;

    Account(String name,String no,long balance){
        this.name = name;
        this.no = no;
        this.balance = balance;
    }

    String getName(){
        return this.name;
    }

    String getNo(){
        return this.no;
    }

    long getBalance() {
        return this.balance;
    }

    void deposit(long k){
        this.balance += k;
    }

    
    void withdraw(long k){
        this.balance -= k;
    }
}

public class TimeAccount extends Account {
    private long timeBalance;

    TimeAccount(String name, String no, long balance,long timeBalance){
        super(name,no,balance);
        this.timeBalance = timeBalance;
    }

    long getTimeBalance(){
        return this.timeBalance;
    }

    void cancel() {
        deposit(this.timeBalance);
        this.timeBalance = 0 ;
    }
}