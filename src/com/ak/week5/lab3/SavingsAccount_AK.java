package com.ak.week5.lab3;

import java.util.Locale;

/*
 * File: SavingsAccount_AK.java
 * @author Alex Kachur
 * @since 2025-10-05
 * @purpose Savings account that enforces a $50 minimum balance after withdrawals.
 */

/**
 * A savings account that blocks any withdrawal which would reduce the balance
 * below $50.
 */
public class SavingsAccount_AK extends BankAccount_AK {

	/** Minimum balance that must remain after any withdrawal. */
	private static final double MIN_BALANCE = 50.0;

	/**
	 * Constructs a new savings account.
	 *
	 * @param accountNumber  non-empty identifier
	 * @param initialBalance starting balance (can be below $50; the rule applies to
	 *                       withdrawals)
	 * @throws IllegalArgumentException if inputs are invalid
	 */
	public SavingsAccount_AK(String accountNumber, double initialBalance) {
		super(accountNumber, initialBalance); // constructor chaining required in subclasses
	}

	/**
	 * Withdraws money but refuses operations that violate the $50 minimum.
	 *
	 * @param amount amount to withdraw; must be {@code > 0}
	 * @throws IllegalArgumentException if {@code amount <= 0}
	 * @throws IllegalStateException    if resulting balance would be {@code < $50}
	 */
	@Override
	public void withdraw(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("withdraw amount must be > 0");
		}
		double newBalance = getBalance() - amount;
		if (newBalance < MIN_BALANCE) {
			throw new IllegalStateException(
					String.format(Locale.US, "cannot withdraw: balance would fall below $%.2f", MIN_BALANCE));
		}
		// Delegate to base implementation for final validation and subtraction.
		super.withdraw(amount);
	}
}