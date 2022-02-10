package org.simplilearn.workshop.spring_Assignment;

import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App 
{
    public static void main( String[] args )
    {
    	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
    	Transaction dao=context.getBean("transaction",Transaction.class);
    	Scanner scan=new Scanner(System.in);
    	System.out.println("Welcome to the bank system");
    	System.out.println();
    	System.out.println();
    
    	optionSelect(dao);
    
    }
    
    static void optionSelect(Transaction dao) {
    	Scanner scan=new Scanner(System.in);
    	System.out.println("Select the option\n1. Create an account\n2.View balance of an account\n3.Sent Money\n4.View transaction history\n5.Exit");
    	int options=scan.nextInt();
    	
    	switch(options) {
    	
    	case 1:
    		System.out.println("Account Creation");
    		dao.createAnAccount();
    		optionSelect(dao);
    		break;
    		
    	case 2 :
    		System.out.println("Balance check");
    		dao.accountBalance();
    		optionSelect(dao);
    		break;
    		
    		
    	case 3:
    		System.out.println("Money Transfer");
    		dao.transfer();
    		optionSelect(dao);
    		break;
    		
    	case 4:
    		System.out.println("Transaction history");
    		dao.viewTransaction();
    		optionSelect(dao);
    		break;
    		
    	case 5:
    		System.out.println("Thankyou for using\n have a nice day!!!");
    		break;
    	default:
    		System.out.println("Wrong choice\nTry again");
    		optionSelect(dao);
    		break;
    	}
    	
    	
    }
}
