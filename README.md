## BANK CLI - Completed by David Manangan

# Objective

Develop command line interface (CLI) to simulate interaction with a retail bank.


# System Pre-requisite
1. Java (at least version 1.8)

# Installation

1. Unzip BankCLI_DAVID_MANANGAN.zip
2. Open a Command Prompt or Terminal
3. Go inside the project directory, `BankCLI`
4. Run `java -jar target/BankCLI-0.0.1-SNAPSHOT.jar` 

Note: Wait for one minute for the App to run until you see the prompt `Bank CLI >`

<table>
<tr><th>Command</th><th>Description</th></tr>
<tr><td>login `<client>`</td><td>Login as `client`. Creates a new client if not yet exists.</td></tr>
<tr><td>topup `<amount>`</td><td>Increase logged-in client balance by `amount`.</td></tr>
<tr><td>pay `<another_client>` `<amount>`</td><td>Pay `amount` from logged-in client to `another_client`, maybe in parts, as soon as possible.</td></tr>
</table>

# Solution

The Bank CLI simulates a Retail Bank by utilizing a banking convention known as 'Transaction Codes' or simply 'TRAN CODE'.

Each transaction from a client is associated with a TRAN CODE. Below are the trancodes use in the Bank CLI:

<table>
<tr><th>TRANCODE</th><th>Description</th></tr>
<tr><td>1000</td><td>TOPUP</td></tr>
<tr><td>1010</td><td>TOPUP Credit</td></tr>
<tr><td>1011</td><td>TOPUP Debit</td></tr>
<tr><td>2100</td><td>PAY</td></tr>
<tr><td>2110</td><td>PAY Credit</td></tr>
<tr><td>2111</td><td>PAY Debit</td></tr>
</table>

Blow is the anatomy representation of each trancodes:

Example Trancode: <b>2110</b>, is broken down to 3 parts. Which are the TXCODE ID,Transfer Mode and Account Entry Type. 

Below are the descriptons of each of the parts of the Trancode. 

<table style="text-align:left">
<tr><td><b>TXCODE ID</b></td><td>2</td><td>Identifier to distinguish a specific transaction</td></tr>
<tr><td><b>Transfer Mode</b></td><td>1</td>
<td>A TransferMode or TF of 1 means that the transfer is going to other accounts (s)<br/>
A TransferMode or TF of 0 means that the transfer is going to the user's own account</td></tr>
<tr><td><b>Accounting Entry Type</b></td><td>10</td>
<td>An Accounting Entry of 10 means that the TX is a credit transaction<br/>
An Accounting Entry of 11 means that the TX is a debit transaction</td></tr>
</tr>
</table>

#### Bank Accounting Ledgers
The Bank CLI uses this Transactions as Entries in the Account Ledger. The Bank CLI maintains two ledgers, which are the Account Ledger and the Loan Ledger.

<b>Account Ledger</b> - stores all monetary transactions
<br/>
<b>Loan Ledger</b> - stores all loans associated with the accounts 

Note: <b>Account Balance</b> is computed base on the transactions in the Account Ledger and the Loan Ledger.



