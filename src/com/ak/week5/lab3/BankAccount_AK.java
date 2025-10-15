package com.ak.week5.lab3;

import java.util.Locale;

/*
 * File: BankAccount_AK.java
 * @author Alex Kachur
 * @since 2025-10-05
 * @purpose Base bank account with immutable account number and validated balance operations.
 */

/**
 * A simple bank account with an immutable account number and a current balance.
 * Provides validated {@link #deposit(double)} and {@link #withdraw(double)}
 * operations.
 */
public class BankAccount_AK {

	/** Account number never changes after construction. */
	private final String accountNumber;

	/** Current account balance (non-negative in this base class). */
	private double balance;

	/**
	 * Constructs a new bank account.
	 *
	 * @param accountNumber  non-empty identifier
	 * @param initialBalance starting balance, must be {@code >= 0}
	 * @throws IllegalArgumentException if inputs are invalid
	 */
	public BankAccount_AK(String accountNumber, double initialBalance) {
		if (accountNumber == null || accountNumber.isBlank()) {
			throw new IllegalArgumentException("accountNumber must be non-empty");
		}
		if (initialBalance < 0) {
			throw new IllegalArgumentException("initial balance must be >= 0");
		}
		this.accountNumber = accountNumber.trim();
		this.balance = initialBalance;
	}

	/** @return immutable account number */
	public String getAccountNumber() {
		return accountNumber;
	}

	/** @return current balance */
	public double getBalance() {
		return balance;
	}

	/**
	 * Deposits money.
	 *
	 * @param amount amount to deposit; must be {@code > 0}
	 * @throws IllegalArgumentException if {@code amount <= 0}
	 */
	public void deposit(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("deposit amount must be > 0");
		}
		balance += amount;
	}

	/**
	 * Withdraws money (no overdraft allowed in base account).
	 *
	 * @param amount amount to withdraw; must be {@code > 0} and {@code <= balance}
	 * @throws IllegalArgumentException if {@code amount <= 0}
	 * @throws IllegalStateException    if {@code amount > balance}
	 */
	public void withdraw(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("withdraw amount must be > 0");
		}
		if (amount > balance) {
			throw new IllegalStateException("insufficient funds");
		}
		balance -= amount;
	}

	@Override
	public String toString() {
		return String.format(Locale.US, "BankAccount[%s] balance=$%.2f", accountNumber, balance);
	}
}