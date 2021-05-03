//java.util package imported to use scanner and regex.
//java.text package imported to use DecimalFormat:
//java.math package imported to use RoundingMode;
import java.util.*;
import java.text.*;
import java.math.*;


///////////MAIN CLASS///////////

public class ProjectManager
{
//"Global" variables are declared to be used across methods. These are static variables declared in the main class (outside the main method).
////(Ram, 2018, Tutorialspoint.com, t.ly/ZOqf), (JAVA, 2013, Stackoverflow.com, t.ly/gUp0).
//"persons" and "projects" are HashMaps with String keys that temporarily store new Person and Project objects as values (W3Schools.com, 2021, Java HashMap, t.ly/m88l). 
//This allows for the testing of methods, otherwise there is nowhere to store them, as the program does not write to file yet. 
//Note: serialization via "java.io" would ideally be used to store the created Project and Person objects (Geeksforgeeks.com, 2019, Serialization and Deserialization in Java with Example, t.ly/ylrP).
//"menu" controls loops to the main menu.
//"submenu" controls loops to submenus.
//"selectProject" controls loops to choose another project.
//"subAndSelect" controls loops to submenus and to choose another project.
//"INPUT" is a scanner object that will read user input across all methods:
	public static Map <String, Person> persons = new HashMap<>();
	public static Map <String, Project> projects = new HashMap<>();  
	public static boolean menu = false;    
	public static boolean submenu = false;  
	public static boolean selectProject = false;
	public static boolean subAndSelect = false;
	public static final Scanner INPUT = new Scanner(System.in);

		
///////////MAIN METHOD///////////
	
	public static void main (String [] args)
	{		
//The user is welcomed to the program:		
		System.out.println("Welcome to the Poised Project Manager.");

//While "menu" is true, the main menu is displayed, and user input stored as "choice":	
		menu = true;
		while (menu)
		{
			System.out.println("\nPlease select an option from the menu below:\n"
					+ "cn -\tcreate new project\n"
					+ "vp -\tview projects & people\n"
					+ "up -\tupdate project\n"
					+ "fp -\tfinalize project\n"
					+ "e  -\texit");
			
			String choice = INPUT.nextLine();

			
//The outcomes for different main menu choices are determined:
			
//1.) If the user chooses to create a new project ("choice" == "cn"), "menu" becomes false (menu loop is exited), and "submenu" becomes true.
//While "submenu" is true (this will loop back to create another project), methods "createNew()" and "backToMenus()" are called.
//"createNew()" creates a new project, while "backToMenus()" allows the user to go back to either create another project, go back to the main menu, or quit:
			if (choice.equalsIgnoreCase("cn")) 
			{
				menu = false;
				submenu = true;
				
				while (submenu) 
				{
					createNew();
					backToMenus("\nCreate another project ('y'), go back to the main menu ('b'), or exit ('e')?"); 
				}		
			}


//2.) If the user chooses to view projects or people ("choice" == "vp"), Booleans "menu" and "submenu" switch values again.
//While "submenu" is true, the "view" submenu is displayed, and user choice stored as "choiceVp":		
			if (choice.equalsIgnoreCase("vp")) 
			{
				menu = false;
				submenu = true;
				
				while (submenu)
				{
					System.out.println("\nPlease select a view option below:\n" 
							+ "va  -\tview all projects\n" ////Is this option needed?
							+ "vi  -\tview incomplete projects\n"
							+ "vpd -\tview projects past due date\n"
							+ "sp  -\tsearch for projects\n"
					        + "vp  -\tview people\n"   ////Is this option needed?, probably needs separate submenu if yes.
							+ "b   -\tgo back to the main menu\n"
							+ "e   -\texit");
					
					String choiceVp = INPUT.nextLine();

//The outcomes for different "view" submenu choices are determined:
//Note: the outcomes (and possible methods) still need to be finalized (not part of first deliverable). 
					if (choiceVp.equalsIgnoreCase("va"))
					{
						//do something
						backToMenus("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?");
					}
					
					if (choiceVp.equalsIgnoreCase("vi"))
					{
						//do something
						backToMenus("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?");
					}
					
					if (choiceVp.equalsIgnoreCase("vpd"))
					{
						//do something
						backToMenus("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?");
					}
					
					if (choiceVp.equalsIgnoreCase("sp"))
					{
						//do something
						//Project selected = selectProject();
						backToMenus("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?");
					}
					if (choiceVp.equalsIgnoreCase("vp"))
					{
						//do something
						backToMenus("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?");
					}
					
					if (choiceVp.equalsIgnoreCase("b"))
					{
						submenu = false;
						menu = true;
					}
					
					if (choiceVp.equalsIgnoreCase("e"))
					{
						INPUT.close();
						System.exit(0);
					}
				}
			}
			
//End of "view" submenu choices.
	
			
//3.) If the user chooses to update a project ("choice" == "up"), "menu" and "selectProject" switch values.
//While "selectProject", "selectProject()" method is called, and the return value (the user-selected Project object) is stored as "selected".
//If "selected" is null (i.e. the project could not be found), an error message is displayed, and "backToMenus()" called.
//Else, "selectProject" becomes false, and "submenu" and "subAndSelect" become true:
			if (choice.equalsIgnoreCase("up"))  
			{
				menu = false;
				selectProject = true;
				
				while (selectProject)
				{				
					Project selected = selectProject(); 
					if (selected == null)
					{
						backToMenus("\nThe project you are looking for has already been finalized or does not exist."
								+ "\nChoose another project ('y'), go back to the main menu ('b') or exit ('e')?");
					}
					else
					{
						selectProject = false;
						submenu = true;
						subAndSelect = true;

//While "submenu", the "update" submenu is displayed, and user choice stored as "choiceUp":						
						while (submenu)
						{
							System.out.println("\nPlease select an update option below:\n"  
									+ "ud  -\tupdate deadline\n"
									+ "utp -\tupdate total amount paid to date\n"
									+ "uc  -\tupdate contractor details\n"  ////update architect & customer details?????
									+ "o   -\tupdate another project\n"
									+ "b   -\tgo back to the main menu\n"
									+ "e   -\texit");
							
							String choiceUp = INPUT.nextLine();
							
//The outcomes for different submenu choices are determined:

//3.1) If the user chooses to update the project deadline ("choiceUp" == "ud"), "submenu" becomes false, and the user is requested to enter a new deadline, "newDeadLine".
//Project method "changeDeadLine" is called on project "selected" with the new deadline as parameter, thus changing the "deadline" attribute of "selected".
//3.2) The same process is repeated if the user chooses to update the total amount paid to date ("choiceUp" == "utp"):
							if (choiceUp.equalsIgnoreCase("ud"))
							{ 
								System.out.println("\nPlease enter a new deadline:");
								String newDeadLine = INPUT.nextLine();
								selected.changeDeadLine(newDeadLine);
								System.out.println("\n" + selected); ////This is a test.
								backToMenus("\nUpdate another project detail ('y'), choose another project ('o'), go back to the main menu ('b'), or exit ('e')?");
					        }
								
							if (choiceUp.equalsIgnoreCase("utp"))
							{
								System.out.println("\nPlease enter a new total amount paid: R ");
								String newtotalPaid = INPUT.nextLine();
								selected.changeTotalPaid(newtotalPaid);
								System.out.println("\n" + selected);  ////This is a test.
								backToMenus("\nUpdate another project detail ('y'), choose another project ('o'), go back to the main menu ('b'), or exit ('e')?");
							}

//3.3) If the user chooses to update the contractor's details ("choiceUp" == "uc"), "submenu" becomes false, and the "contractor" attribute of "selected" is stored as string "contractor".
//"updatePerson()" method is called and "contractor" passed as an argument (this will allow the user to change the contractor's cell no, email, and address):
							if (choiceUp.equalsIgnoreCase("uc"))
							{
								String contractor = selected.contractor;
								selectPerson(contractor);
								backToMenus("\nUpdate another project detail ('y'), choose another project ('o'), go back to the main menu ('b'), or exit ('e')?");
							}

//3.4 & 3.5) If the user chooses to update another project ("choiceUp" == "o") or go back to the main menu ("choiceUp" == "b"), 
//"submenu" and "subAndSelect" become false, and either "selectProject" or "menu" becomes true, respectively looping back to select another project, or go to the main menu.
//3.6) If the user chooses to exit ("choiceUp" == "e"), the scanner is closed, and the JVM terminated:
							if (choiceUp.equalsIgnoreCase("o"))
							{
								submenu = false;
								subAndSelect = false;
								selectProject = true;
							}
							
							if (choiceUp.equalsIgnoreCase("b"))
							{
								submenu = false;
								subAndSelect = false;
								menu = true;
							}
							
							if (choiceUp.equalsIgnoreCase("e"))
							{
								INPUT.close();
								System.exit(0);
							}
						}
					}					
				}
			 }

//End of "update" submenu choices.
		
			
//4.) Back in the main menu, if the user chooses to finalize a project ("choice" == "fp"), "menu" and "selectProject" switch values. 
//While "selectProject", "selectProject()" method is called, and the return value (the user-selected Project object) is stored as "selected".
//If "selected" is null (i.e. the project could not be found), an error message is displayed, and "backToMenus()" called.
//Else, "finalizeProject()" and "backToMenus()" methods are called:
			if (choice.equalsIgnoreCase("fp"))  
			{
				menu = false;
				selectProject = true;
				
				while (selectProject)
				{
					Project selected = selectProject();
					
					if (selected == null)
					{
						backToMenus("\nThe project you are looking for has already been finalized or does not exist."
								+ "\nChoose another project ('y'), go back to the main menu ('b') or exit ('e')?");
					}
					else
					{					
						finalizeProject(selected);
						backToMenus("\nFinalize another project ('y'), go back to the main menu ('b'), or exit ('e')?");
					}						
				}					
			}
			
			
//5.) If the user chooses to quit ("choice" == "e"), the scanner is closed, and JVM is terminated (Geeksforgeeks.com, 2016, System.exit() in Java, t.ly/1i6x):			
			if (choice.equalsIgnoreCase("e"))
			{
				INPUT.close();
				System.exit(0);
			}
		}
	
	}

//End of main menu choices and of main method.
	

	
///////////METHODS///////////
	
//1.) "createNew()" creates a new project. A notification is displayed, and the user requested to enter all project information, stored as strings.
//This includes: project number, project name, building type, physical address, ERF number, total fee charged, total amount paid to date, deadline, 
//architect (name, cellphone number, email address and physical address), contractor (name, cellphone number, email address and physical address), 
//and customer (name, cellphone number, email address and physical address):	
	public static void createNew()
	{
		System.out.println("\nPlease enter new project information:\n");
		System.out.println("Project number:");		
		String projectNum = INPUT.nextLine();
		System.out.println("Project name:");
		String projectName = INPUT.nextLine();
		System.out.println("Building type:");
		String buildType = INPUT.nextLine(); 
		System.out.println("Physical address:");
		String physAddress = INPUT.nextLine();
		System.out.println("ERF number:");
		String erfNum = INPUT.nextLine(); 
		System.out.println("Total fee R:");
		String totalFee = INPUT.nextLine();
		System.out.println("Total amount paid to date R:");
		String totalPaid = INPUT.nextLine();
		System.out.println("Project deadline:");
		String deadLine = INPUT.nextLine();
		
		System.out.println("Architect surname:");		
		String architectSur = INPUT.nextLine();
		System.out.println("Architect first name(s):");		
		String architectNam = INPUT.nextLine();
		System.out.println("Architect cellphone number:");   
		String architectCell = INPUT.nextLine();
		System.out.println("Architect email address:");   
		String architectMail = INPUT.nextLine();
		System.out.println("Architect physical address:");   
		String architectAddress = INPUT.nextLine();
		
		System.out.println("Contractor surname:");		
		String contractorSur = INPUT.nextLine();
		System.out.println("Contractor first name(s):");		
		String contractorNam = INPUT.nextLine();
		System.out.println("Contractor cellphone number:");
		String contractorCell = INPUT.nextLine();
		System.out.println("Contractor email address:");
		String contractorMail = INPUT.nextLine();
		System.out.println("Contractor physical address:");
		String contractorAddress = INPUT.nextLine();
		
		System.out.println("Customer surname:");		
		String customerSur = INPUT.nextLine();
		System.out.println("Customer first name(s):");		
		String customerNam = INPUT.nextLine();
		System.out.println("Customer cellphone number:");
		String customerCell = INPUT.nextLine();
		System.out.println("Customer email address:");
		String customerMail = INPUT.nextLine();
		System.out.println("Customer physical address:");
		String customerAddress = INPUT.nextLine();
				  
//An ArrayList "projectNameBlank" is created (W3schools.com, 2021, Java ArrayList, t.ly/yPnP) to help to add a project name if this was left blank above.
//A range of different new-line and blank-space characters are added to the ArrayList (these could vary depending on OS):
		ArrayList <String> projectNameBlank = new ArrayList <String>();
		projectNameBlank.add("\n");
		projectNameBlank.add("\r");
		projectNameBlank.add("\r\n");
		projectNameBlank.add(" ");
		projectNameBlank.add("");

//If the new ArrayList contains the entered project name (i.e. if the user left the project name blank),
//the project name is set equal to the building type plus the customer's surname:
		if (projectNameBlank.contains(projectName))
		{ 
			projectName = buildType + " " + customerSur; 
		}
		
//The entered project details are used to create an new Project object, which is then added to HashMap "projects" as a value, 
//using the project number and project name as key (Jesper, 2012, Stackoverflow.com, t.ly/ksAl):		 		
		projects.put(projectNum + " " + projectName, new Project(projectNum, projectName, buildType, physAddress, erfNum, totalFee, totalPaid, deadLine, 
				architectNam + " " + architectSur, contractorNam + " " + contractorSur, customerNam + " " + customerSur));		

//The person details are used to create three types of Person objects, which are added to HashMap "persons" as values, using the persons' names as keys:
		persons.put(architectNam + " " + architectSur, new Person("Architect", architectSur, architectNam, architectCell, architectMail, architectAddress));
		persons.put(contractorNam + " " + contractorSur, new Person("Contractor", contractorSur, contractorNam, contractorCell, contractorMail, contractorAddress));
		persons.put(customerNam + " " + customerSur, new Person("Customer", customerSur, customerNam, customerCell, customerMail, customerAddress));
		
		System.out.println("\n" + projects.get(projectNum + " " + projectName));           ////These are tests.
		System.out.println("\n" + persons.get(architectNam + " " + architectSur));
		System.out.println("\n" + persons.get(contractorNam + " " + contractorSur));
		System.out.println("\n" + persons.get(customerNam + " " + customerSur));              		
	}
	
	
//2.) "selectProject()" selects a project. Project "selected" is declared as null (to have a return value in case the project is not found). 
//The user is requested to enter the name or number of the project they want to update (stored as "projectNameNum").
//For string key "i" in HashMap "projects.keySet()" (this iterates through HashMap "projects"'s keys - W3Schools.com, 2021, Java HashMap, t.ly/sLs3),
//if "i" contains the entered project name or number, the value of key "i" (i.e. the Project object), is found and stored as "selected". 
//The value of "selected" is then returned:
	public static Project selectProject()  
	{
		
		Project selected = null;
		
		System.out.println("\nPlease enter the number or name of the project you wish to update:");		
		String projectNameNum = INPUT.nextLine();
		
		for (String i : projects.keySet()) 	   	
		{			
			if (i.contains(projectNameNum))  
			{
				selected = projects.get(i);
				System.out.println("\n" + selected);   ////This is a test.
				
			}
		}	
		return selected;
	}		


//3.) "finalizeProject()" finalizes a project and generates an invoice (if the customer still owes an amount). It passes a user-selected Project "selected" as a parameter.
//The completion date is requested (stored as "completionDate"), and Project method "markFinalized()" is called on "selected". 
//This adds "finalized" and the completion date to the Project object's "deadLine" attribute.
//Next, Project "selected" is displayed (Note: this should be written to file in the final deliverable):
	public static void finalizeProject(Project selected)
	{
		System.out.println("\nPlease enter the completion date:");
		String completionDate = INPUT.nextLine();
	
		selected.markFinalized(completionDate);
	
		System.out.println("\n" + selected); ////This should be written to file eventually.
	
//Then, an invoice is generated. The total fee and total amount paid to date are called from Project object "selected" and cast to double (WhiteFang34, 2011, Stackoverflow.com, t.ly/QOvF).
//The total outstanding amount is calculated and all amounts rounded off to a strings that always contain two decimals, using DecimalFormat object "df" (Ligios, 2019, A Practical Guide to DecimalFormat, Baeldung.com, t.ly/GF8B).			
//Note: the rounding mode used in "df" is changed so numbers are rounded up if the discarded fraction is > 0.5 (Bright, 2012, & Julien, 2012 (ed.), Stackoverflow.com, t.ly/uyOC):		
		double totalFee = Double.parseDouble(selected.totalFee); 
		double totalPaid = Double.parseDouble(selected.totalPaid);
		double totalOutstanding = totalFee - totalPaid;
		
		DecimalFormat df = new DecimalFormat("0.00");   
		df.setRoundingMode(RoundingMode.HALF_UP);
		
		String totalFeeStr = df.format(totalFee);
		String totalPaidStr = df.format(totalPaid);
		String totalOutstandingStr = df.format(totalOutstanding);
			
//The strings are formatted to achieve right-alignment. "String.format("%xs", y)" formats string "y" into a string containing "x" characters.
//The constructed string is filled up with string "y" from the right. The rest is left blank (Leon, 2016, Stackoverflow.com, t.ly/a2Xy) (Howard, 2011, Stackoverflow.com, t.ly/uL8L)
//(javatpoint.com, 2021, Java String format(), t.ly/f9D8):
		String totalFeeStr20 = String.format("%20s", totalFeeStr);
		String totalPaidStr20 = String.format("%20s", totalPaidStr);
		String totalOutstandingStr20 = String.format("%20s", ("R " + totalOutstandingStr));
		
//The customer name is called from Project "selected" and stored as string "customer".		
//For string "i" in HashMap "persons.keyset()", if string "i" contains the customer name, 
//and the total outstanding amount is not zero (invoices are not generated if full amount is already paid),
//then the HashMap value of key string "i" (i.e. a Person object) is found, and stored as "selectedPerson":
		String customer = selected.customer;
		
		for (String i : persons.keySet())		
		{
			if (i.contains(customer) && totalOutstanding != 0.0)
			{	 
				Person selectedPerson = persons.get(i);

//The invoice is displayed, with project and person information called from the selected Project and Person objects, along with all amounts.
//Note: Person method "toString2()" is used to conveniently display all customer details at once:
				System.out.println("\n\nPOISED\t\t\t\t\t\t\tINVOICE# " + selected.projectNum + "\n"     
						+ "\n\nBill to"                                                             ////add person title?????
						+ "\n-------------------------------"
						+ "\n" + selectedPerson.toString2()                                    
						+ "\n\n\n\nDescription\t\t\t\t\t\t\t Amount"
						+ "\n------------------------------------------------------------------------------"
						+ "\n\nTotal fee:\t\t\t\t\t\t" + totalFeeStr20  
						+ "\nTotal amount paid to date:\t\t\t\t" + totalPaidStr20
						+ "\n\n\n\n\nTotal outstanding:\t\t\t\t\t" + totalOutstandingStr20
						+ "\n------------------------------------------------------------------------------");								
			}		
		}		
	}
	
	
//4.)"selectPerson()" updates the details of any person. It takes the person's name as parameter.
//For string "i" in HashMap "persons.keyset()", if string "i" (i.e. the HashMap key) contains the chosen person's name (Landup, 2021, Stackabuse.com, t.ly/xvIT), 
//then the HashMap value of key string "i" (i.e. the Person object) is found and stored as "selectedPerson" (W3Schools.com, 2021, Java HashMap, t.ly/sLs3):
	public static void selectPerson(String name)
	{		
		for (String i : persons.keySet()) 		
		{
			if (i.contains(name))  
			{ 	
				Person selectedPerson = persons.get(i);
				
				System.out.println("\n" + selectedPerson); ////this is a test.
				
//The user is then requested to enter the new personal details for the chosen person (or skip if the detail has not changed),
//after which Person method "updatePerson()" is called on "selectedPerson", which replaces its existing cellphone number, email address, and physical address 
//attribute values with the newly-entered ones:
				System.out.println("\nPlease enter a new cell number or press enter to skip:");
				String newCellNum = INPUT.nextLine();
				System.out.println("Please enter a new email address or press enter to skip:");
				String newEmailAddress = INPUT.nextLine();
				System.out.println("Please enter a new physical address or press enter to skip:");  ////Might need to separate this function into two: "selectPerson()" and "updatePerson()".
				String newPhysAddress = INPUT.nextLine();
				
				selectedPerson.updatePerson(newCellNum, newEmailAddress, newPhysAddress);
				
				System.out.println("\n" + selectedPerson); ////this is a test.
			}
		}		
	}	
			
	
//5.) "backToMenus()" allows users to go back to the main menu ("b") or submenu ("y"), to choose another project ("o"), or to quit ("q").
//It includes the parameter "optionString" which allows it to display specified string options, to suit the menu and submenu options.
//The "optionString" is displayed, and the user is requested to make their choice, stored as "goBack".
//If "menu", "selectProject", and "subAndSelect" are false, but "submenu" is true, the user can go: back to the main menu ("b"), back to the submenu ("y"), or exit ("e").
//If "menu", "submenu", and "subAndSelect" are false, but "selectProject" is true, the user can go: back to the main menu ("b"), back to the select another project ("y"), or exit ("e").
//If "menu" and "selectProject" are false, but "submenu" and 'subAndSelect" are true, the user can go: back to the main menu ("b"), back to the submenu ("y"), back to the select a project ("o"), or exit ("e").
//If the user chooses to exit ("e") in all three scenarios, the scanner is closed, and the JVM terminated:
	public static void backToMenus(String optionString)
	{				
		boolean backToMenus = true;
		while (backToMenus)
		{
			System.out.println(optionString);
			String goBack = INPUT.nextLine();
			
			if (menu == false && submenu == true && selectProject == false && subAndSelect == false ) 
			
			{				
				if (goBack.equalsIgnoreCase("b"))
				{
					backToMenus = false;
					submenu = false;
					menu = true;
				}
				if (goBack.equalsIgnoreCase("y"))
				{				
					backToMenus = false;
				}			
				if (goBack.equalsIgnoreCase("e"))
				{
					INPUT.close();
					System.exit(0); 
				}		
			}
				
			if (menu == false && submenu == false && selectProject == true && subAndSelect == false)  
			{				
				if (goBack.equalsIgnoreCase("b"))
				{
					backToMenus = false;
					selectProject = false;
					menu = true;
				}
				if (goBack.equalsIgnoreCase("y"))
				{
					backToMenus = false;
				}			
				if (goBack.equalsIgnoreCase("e"))
				{
					INPUT.close();
					System.exit(0); 
				}		
			}	
				
			if (menu == false && submenu == true && selectProject == false && subAndSelect == true)
			{
		
				if (goBack.equalsIgnoreCase("b"))
				{										
					backToMenus = false;
					submenu = false;
					subAndSelect = false;
					menu = true;
				}
				if (goBack.equalsIgnoreCase("y"))
				{
					backToMenus = false;
				}			
				if (goBack.equalsIgnoreCase("o"))
				{
					backToMenus = false;
					submenu = false;
					subAndSelect = false;
					selectProject = true;
				}
				if (goBack.equalsIgnoreCase("e"))
				{
					INPUT.close();
					System.exit(0); 
				}					
			}		
		}						
	}
	
}
		

/////////////////// THE END ////////////////////