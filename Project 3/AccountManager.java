package transaction.transaction_lock.server.account;

import java.util.ArrayList;


import transaction.transaction_lock.server.TransactionServer;

public class AccountManager {

	private int numberOfAccounts;
	private int initialBalance;
	private ArrayList<Account> accounts = new ArrayList<>();


	public AccountManager(int numberOfAccounts, int initialBalance) {
		this.initialBalance = initialBalance;
		this.numberOfAccounts = numberOfAccounts;
		this.InitializeAccounts();
	}
        //Intialize account with Default Values
	void InitializeAccounts() {
		for (int i = 0; i < numberOfAccounts; i++) {
			Account account = new Account(i + 1, initialBalance);
			accounts.add(account);
		}

	}
        // Reading Account's Balance
	public int readAccount(int accountNumber) {
            
		int balance = 0;
		for (Account account : accounts) {
			if (account.getAccountNumber() == accountNumber) {
				balance = account.getBalance();
			}
		}
		System.out.println("Read Account balance -> " + balance);

		return balance;
	}
  // Wrinting an amount into Account
	public void writeAccount(int accountNumber, int balance) {
		for (Account account : accounts) {
			if (account.getAccountNumber() == accountNumber) {
				account.setBalance(balance);
			}
		}
		System.out.println("WriteAccount balance -> " + balance);
		TransactionServer.accountManager.getAccounts();


	}

	public ArrayList<Account> getAccounts() {
		return this.accounts;
	}

	public Account getAccount(int accountNumber) {

		return this.accounts.stream().filter(each -> each.getAccountNumber() == accountNumber).findFirst().get();

	}
         //Returning Total ammount in the Bank
	public int getTotalAmountInBank() {
		int sum = this.accounts.stream().mapToInt(ob -> ob.getBalance()).reduce(0,
				(element1, element2) -> element1 + element2);

		return sum;
	}

}
