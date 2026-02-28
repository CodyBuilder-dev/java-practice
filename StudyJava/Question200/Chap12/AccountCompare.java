class AccountCompare {
    static int compBalance(Account a, Account b){
        // a,b에 Account가 들어올지 TimeAccount가 들어올지 확신 불가
        // 해당부분에 대한 처리가 필요함
        long totalBalanceA = a instanceof TimeAccount ?
            a.getBalance() + ((TimeAccount)a).getTimeBalance()
            : a.getBalance();
        long totalBalanceB = b instanceof TimeAccount ?
            b.getBalance() + ((TimeAccount)b).getTimeBalance()
            : b.getBalance();

        if (totalBalanceA > totalBalanceB)
            return 1;
        else if (totalBalanceA < totalBalanceB)
            return -1;
        else 
            return 0;
    }

    public static void main(String[] args){
        Account gildong = new Account("길동","123456",500);
        TimeAccount chulsua = new TimeAccount("철수","654321",300,400);

        System.out.println(compBalance(gildong,chulsua));
    }
}