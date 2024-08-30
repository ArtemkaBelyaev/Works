package ConcurrentBank;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount {
    private final UUID accountNumber;
    private BigDecimal balance;
    private final ReentrantLock lock = new ReentrantLock();

    BankAccount(UUID accountNumber1) {
        this.accountNumber = accountNumber1;
        this.balance = BigDecimal.ZERO;
    }

    public void deposit(BigDecimal amount) {
        lock.lock();
        try {
            balance = balance.add(amount);
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(BigDecimal amount) {
        lock.lock();
        try {
            if (balance.compareTo(amount) < 0) {
                return false;
            }
            balance = balance.subtract(amount);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public UUID getAccountNumber() {
        return accountNumber;
    }
}

class ConcurrentBank {
    private final Map<UUID, BankAccount> accounts = new HashMap<>();
    private final ReentrantLock bankLock = new ReentrantLock();

    public BankAccount createAccount() {
        bankLock.lock();
        try {
            UUID accountNumber = UUID.randomUUID();
            BankAccount account = new BankAccount(accountNumber);
            accounts.put(accountNumber, account);
            return account;
        } finally {
            bankLock.unlock();
        }
    }

    public boolean transfer(UUID fromAccountNumber, UUID toAccountNumber, BigDecimal amount) {
        bankLock.lock();
        BankAccount fromAccount = accounts.get(fromAccountNumber);
        BankAccount toAccount = accounts.get(toAccountNumber);
        try {
            if (fromAccount != null && toAccount != null
                    && fromAccount.withdraw(amount)) {
                toAccount.deposit(amount);
                return true;
            } else {
                return false;
            }
        } finally {
            bankLock.unlock();
        }
    }

    public BigDecimal getTotalBalance() {
        bankLock.lock();
        try {
            BigDecimal total = BigDecimal.ZERO;
            for (BankAccount account : accounts.values()) {
                total = total.add(account.getBalance());
            }
            return total;
        } finally {
            bankLock.unlock();
        }
    }
}

public class ConcurrentBankExample {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        BankAccount account = bank.createAccount();
        BankAccount account1 = bank.createAccount();

        account.deposit(BigDecimal.valueOf(1000));
        account1.deposit(BigDecimal.valueOf(2000));

        Thread transferThread = new Thread(() -> {
            boolean success = bank.transfer(account.getAccountNumber(),
                    account1.getAccountNumber(),
                    BigDecimal.valueOf(100));
            System.out.println("Первый перевод " +
                    (success ? "Удачно" : "Неудачо"));
        });

        Thread transferThread1 = new Thread(() -> {
            boolean success = bank.transfer(account1.getAccountNumber(),
                    account.getAccountNumber(),
                    BigDecimal.valueOf(200));

            System.out.println("Второй перевод " +
                    (success ? "Удачно" : "Неудачо"));

            System.out.println("Баланс: \n Кошелёк №0 " +
                    account.getBalance() +
                    "\n Кошелёк №1 " + account1.getBalance() +
                    "\n Номера кошельков:" +
                    "\n Кошелёк №0: " + account.getAccountNumber() +
                    "\n Кошелёк №1: " + account1.getAccountNumber() +
                    "\n Общий баланс: " + bank.getTotalBalance());
        });

        transferThread.start();
        transferThread1.start();

        try {
            transferThread.join();
            transferThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
