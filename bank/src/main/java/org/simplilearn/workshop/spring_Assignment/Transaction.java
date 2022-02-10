package org.simplilearn.workshop.spring_Assignment;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;



@Component
public class Transaction {
	
	DataSource dataSource;
	NamedParameterJdbcTemplate template;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template=new NamedParameterJdbcTemplate(dataSource);
	}
	
	Scanner scan = new Scanner(System.in);
	String qry;
	void accountBalance() {
	System.out.println("Enter your account id:");
	int acc_id=scan.nextInt();
	qry="select balance from account where account_id=:acc_id";
	System.out.println(template.queryForObject(qry, new MapSqlParameterSource("acc_id",acc_id), Integer.class));
	}
	
	
	
	void createAnAccount() {
		System.out.println("Enter your name:");
		String name=scan.nextLine();
		
		System.out.println("How much money Should you deposit initialy:");
		int money=scan.nextInt();
		scan.nextLine();
		
		qry="insert into account (name,balance) values (:iname, :money)";
		template.update(qry,new MapSqlParameterSource("iname",name).addValue("money", money));
		System.out.println("Account created successfully");
		qry="select * from account where name=:name";
		System.out.println("The Account Details Are:");
		System.out.println(template.queryForObject(qry, new MapSqlParameterSource("name",name), new AccountMapper()));
		
		
		
	}
	
	int getBal(int id) {
		qry="select balance from account where account_id=:id";
		return template.queryForObject(qry, new MapSqlParameterSource("id",id),Integer.class);
	}
	
//	void balanceCheck() {
//		System.out.println("Enter your personal id:");
//		int id=scan.nextInt();
//		scan.nextLine();
//		qry = "select balance from account where account_id =:id";
//		System.out.println(template.queryForObject(qry, new MapSqlParameterSource("id",id),Integer.class));
//	}
	
	void transfer() {
		System.out.println("Enter your id:");
		int sent_id =scan.nextInt();
		scan.nextLine();
		System.out.println("Enter the reciever id:");
		int rec_id=scan.nextInt();
		scan.nextLine();
		int bal=getBal(sent_id);
		System.out.println("Enter the amount you want to sent:");
		int amt=scan.nextInt();
		scan.nextLine();
		if(amt<=bal) {
			qry="insert into transaction values(:sent,:rec,:amt)";
			template.update(qry,new MapSqlParameterSource("amt",amt).addValue("sent", sent_id).addValue("rec",rec_id));
			bal=bal-amt;
			qry="update account set balance=:bal where account_id=:id";
			template.update(qry,new MapSqlParameterSource("bal",bal).addValue("id", sent_id));
			bal=getBal(rec_id);
			bal=bal+amt;
			qry="update account set balance=:bal where account_id=:id";
			template.update(qry,new MapSqlParameterSource("bal",bal).addValue("id", rec_id));
			System.out.println("The Transaction is successfull");
			
		}
		else
			System.out.println("Sorry your account doesnot have enough balance");
		
	}
	
	void viewTransaction() {
		System.out.println("Enter your personal id:");
		int per_id=scan.nextInt();
		scan.nextLine();
		qry="select  s.name,r.name,t.amount from account s, account r,transaction t where ((t.sender_id=s.Account_id) AND (t.reciever_id=r.account_id))";
		System.out.println("the transaction history is ");
		
		System.out.println(template.queryForObject(qry, new MapSqlParameterSource("per_id",per_id), new TransferMapper()));
	}
	
	
	class TransferMapper implements RowMapper<Transfer>{

		@Override
		public Transfer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Transfer theTransfer=new Transfer(rs.getString("s.name"),
					rs.getString("r.name"), rs.getInt("t.amount"));
			return theTransfer;
			
		}
		
	}
	
	
	
	
	
	class AccountMapper implements RowMapper<Account>{

		@Override
		public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
			Account theAccount=new Account(rs.getInt("account_id"),
					rs.getString("name"), rs.getInt("balance"));
			return theAccount;
			
		}
		
	}
}
