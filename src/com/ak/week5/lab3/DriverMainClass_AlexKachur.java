package com.ak.week5.lab3;

import java.util.Locale;
import java.util.Scanner;

/*
 * File: DriverMainClass_AlexKachur.java
 * @author Alex Kachur
 * @since 2025-10-05
 * @purpose Console menu to create Bank/Savings accounts, then deposit/withdraw repeatedly.
 */

/**
 * Menu-driven console driver: - Starts with: Create account | Exit - After
 * creating at least one account: Deposit | Withdraw | Create | List | Exit Uses
 * an array to store accounts (polymorphic: BankAccount_AK or
 * SavingsAccount_AK).
 */
public class DriverMainClass_AlexKachur {

	// Simple fixed-size storage (Week-5 friendly). Grow if you like.
	private static final int MAX_ACCOUNTS = 100;
	private static final BankAccount_AK[] ACCOUNTS = new BankAccount_AK[MAX_ACCOUNTS];
	private static int count = 0; // how many we actually have

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		Scanner in = new Scanner(System.in);

		System.out.println("=== Lab3 Week5 — Accounts Menu (AK) ===");

		boolean running = true;
		while (running) {
			// Show a small menu if we have no accounts yet; otherwise show full menu.
			if (count == 0) {
				System.out.println("\nMenu:");
				System.out.println("1) Create account");
				System.out.println("0) Exit");
				int choice = readInt(in, "Choose: ", 0, 1);
				switch (choice) {
				case 1 -> createAccountFlow(in);
				case 0 -> running = false;
				}
			} else {
				System.out.println("\nMenu:");
				System.out.println("1) Create account");
				System.out.println("2) Deposit");
				System.out.println("3) Withdraw");
				System.out.println("4) List accounts");
				System.out.println("0) Exit");
				int choice = readInt(in, "Choose: ", 0, 4);
				switch (choice) {
				case 1 -> createAccountFlow(in);
				case 2 -> depositFlow(in);
				case 3 -> withdrawFlow(in);
				case 4 -> listAccounts();
				case 0 -> running = false;
				}
			}
		}

		in.close();
		System.out.println("\nGoodbye!");
	}

	// --------------------- Flows ---------------------

	/** Create either a BankAccount or a SavingsAccount, then store it. */
	private static void createAccountFlow(Scanner in) {
		if (count >= MAX_ACCOUNTS) {
			System.out.println("Sorry, storage is full.");
			return;
		}

		System.out.println("\nCreate which type?");
		System.out.println("1) Bank account");
		System.out.println("2) Savings account (has $50 minimum after withdraw)");
		int type = readInt(in, "Choose: ", 1, 2);

		String accNo = readLine(in, "Enter account number: ");
		double init = readDouble(in, "Enter initial balance: ");

		try {
			BankAccount_AK acc = (type == 1) ? new BankAccount_AK(accNo, init) : new SavingsAccount_AK(accNo, init);

			ACCOUNTS[count++] = acc;
			System.out.printf("Created %s [%s] with balance $%.2f%n",
					(acc instanceof SavingsAccount_AK ? "SavingsAccount" : "BankAccount"), acc.getAccountNumber(),
					acc.getBalance());
		} catch (RuntimeException ex) {
			System.out.println("Creation failed: " + ex.getMessage());
		}
	}

	/** Deposit into a chosen account. */
	private static void depositFlow(Scanner in) {
		if (count == 0) {
			System.out.println("No accounts yet.");
			return;
		}
		BankAccount_AK acc = chooseAccount(in);
		if (acc == null)
			return;

		System.out.printf("Selected [%s] current balance: $%.2f%n", acc.getAccountNumber(), acc.getBalance());
		double amount = readDouble(in, "Enter deposit amount: ");
		try {
			acc.deposit(amount);
			System.out.printf("New balance: $%.2f%n", acc.getBalance());
		} catch (RuntimeException ex) {
			System.out.println("Deposit failed: " + ex.getMessage());
		}
	}

	/** Withdraw from a chosen account (Savings will enforce $50 min). */
	private static void withdrawFlow(Scanner in) {
		if (count == 0) {
			System.out.println("No accounts yet.");
			return;
		}
		BankAccount_AK acc = chooseAccount(in);
		if (acc == null)
			return;

		System.out.printf("Selected [%s] current balance: $%.2f%n", acc.getAccountNumber(), acc.getBalance());
		double amount = readDouble(in, "Enter withdrawal amount: ");
		try {
			acc.withdraw(amount); // dynamic dispatch: SavingsAccount overrides
			System.out.printf("New balance: $%.2f%n", acc.getBalance());
		} catch (RuntimeException ex) {
			System.out.println("Withdrawal failed: " + ex.getMessage());
		}
	}

	/** Print a simple table of accounts. */
	private static void listAccounts() {
		if (count == 0) {
			System.out.println("No accounts to list.");
			return;
		}
		System.out.println("\n#   Type     Account#    Balance");
		System.out.println("-----------------------------------");
		for (int i = 0; i < count; i++) {
			BankAccount_AK acc = ACCOUNTS[i];
			String type = (acc instanceof SavingsAccount_AK) ? "SAV" : "BNK";
			System.out.printf("%-3d %-7s  %-10s $%.2f%n", i + 1, type, acc.getAccountNumber(), acc.getBalance());
		}
	}

	// --------------------- Helpers ---------------------

	/**
	 * Let user pick an account by number (we’ll show a list first).
	 * 
	 * @return chosen account or null if user cancels
	 */
	private static BankAccount_AK chooseAccount(Scanner in) {
		listAccounts();
		int idx = readInt(in, "Pick # (0 cancels): ", 0, count);
		if (idx == 0)
			return null;
		return ACCOUNTS[idx - 1];
	}

	/** Read a non-blank line. */
	private static String readLine(Scanner in, String prompt) {
		System.out.print(prompt);
		String s = in.nextLine().trim();
		while (s.isBlank()) {
			System.out.print("Please enter something: ");
			s = in.nextLine().trim();
		}
		return s;
	}

	/**
	 * Read an int within [min..max], re-prompting on bad input.
	 */
	private static int readInt(Scanner in, String prompt, int min, int max) {
		System.out.print(prompt);
		while (true) {
			String s = in.nextLine().trim();
			try {
				int v = Integer.parseInt(s);
				if (v < min || v > max)
					throw new NumberFormatException();
				return v;
			} catch (NumberFormatException e) {
				System.out.printf("Please enter a whole number in [%d..%d]: ", min, max);
			}
		}
	}

	/** Read a double, re-prompting on bad input. */
	private static double readDouble(Scanner in, String prompt) {
		System.out.print(prompt);
		while (true) {
			String s = in.nextLine().trim();
			try {
				return Double.parseDouble(s);
			} catch (NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
		}
	}
}