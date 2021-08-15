package projectmanager;

//Note: All package classes edited according to Google Java Style Guide (Github.io, 2021) and Java naming conventions (Oracle.com, 2021).
//Javadoc comments added according to Oracle.com (2021*).

//For Scanner and regex.
import java.util.*;
//For FileWriter, FileinputStream, FileoutputStream, ObjectinputStream, ObjectoutputStream, and IOException & its subclasses.
import java.io.*;


/**
 * This application class is a project-management program that allows users to create, view, update, and finalize projects.
 * <p>
 * It was developed for a fictional small structural engineering firm called Poised.
 * <p>
 * This class uses <code>{@link Project}</code> and <code>{@link Person}</code> classes to create and update objects, 
 * and stores these objects in two <code>LinkedHashMaps</code>.
 * It creates/uses three text file artifacts - two to store the <code>LinkedHashMap</code> objects, and one to record the details of all finalized projects.
 * The main method follows a boolean control structure consisting of one main menu loop, three submenu loops, and one select-project loop.
 * <p>
 * Changes from V1: code refactored, attributes set to private, working maps changed from <code>HashMap</code> to <code>LinkedHashMap</code>, 
 * error/exception handling added, new methods included to add functionality to the view and update submenus, 
 * and to enable writing/reading <code>LinkedHashMap</code> objects to/from file.
 * <p>
 * Changes from V2.0: Main method shortened and refactored into static methods. 
 * Methods <code>{@link Project#selectProject(Map<String, Project>, String) selectProject}</code>,
 * <code>{@link Project#generateInvoice(Map<String, Person>) generateInvoice}</code>,
 * <code>{@link Project#viewAll(Map<String, Project>) viewAll}</code>,
 * <code>{@link Project#viewIncomplete(Map<String, Project>) viewIncomplete}</code>, and
 * <code>{@link Project#viewPastDue(Map<String, Project>) viewPastDue}</code> moved to <code>Project</code>.
 * Methods <code>{@link Person#selectPerson(String, Map<String, Person>) selectPerson}</code> and 
 * <code>{@link Person#updatePerson(String[]) updatePerson}</code> moved to <code>Person</code>.
 * 
 * @author J.E. Foster © 
 * @version V2.1 August 2021
 *
 */
public class ProjectManager {

//ATTRIBUTES (for static variables - see Ram, 2018 and JAVA, 2013).
//Maps to store Project and Person objects.
	private static Map <String, Person> persons;       
	private static Map <String, Project> projects;
	
//Controls loops to submenus.
	private static boolean submenu = false;	
//Controls loops to choose another project.
	private static boolean selectProject = false;
	
	private static final Scanner INPUT = new Scanner(System.in); 
		

//MAIN METHOD
//Suppresses unchecked cast warning (Myers, 2009).
	@SuppressWarnings("unchecked")
	public static void main (String [] args) {
		
//Reads LinkedHashMap objects from text files.		
//LinkedHashMaps (Geeksforgeeks.com, 2021) used instead of HashMaps (W3schools.com, 2021**) to maintain insertion order.	
		projects = (LinkedHashMap<String, Project>) startSequenceReadHashmaps("projects.txt");            
		persons = (LinkedHashMap<String, Person>) startSequenceReadHashmaps("persons.txt"); 
	
		System.out.println("Welcome to the Poised Project Manager.");
		
//Main menu loop.	
		while (true) {
			String choice = startMenu();
//Outcomes for main menu choices:	
//1.) Starts submenu loop and allows user to create project. 
			if (choice.equalsIgnoreCase("cn")) {
				submenu = true;		
				while (submenu) {
					createNew();
				}				
//2.) Starts submenu loop and displays view-submenu. .
			} else if (choice.equalsIgnoreCase("vp")) {
				submenu = true;	
				while (submenu) {
					String choiceVp = viewSubmenu();
//Outcomes for view-submenu choices:
					viewSubmenuOutcomes(choiceVp);
				}
//3.) Starts select-project loop, allows user to select a project, 				
			} else if (choice.equalsIgnoreCase("up")) {
				selectProject = true;
				while (selectProject) {
					Project selectedProject = select();	
//then starts submenu loop and displays update-submenu.
					while (submenu) {
						String choiceUp = updateSubmenu();
//Outcomes for "update"-submenu choices:
						updateSubmenuOutcomes(choiceUp, selectedProject);
					}
				}									
//4.)Writes LinkedHashMaps to text files and exits program.				
			} else if (choice.equalsIgnoreCase("e")) {			
				closeApp();
			}
		}
	};
	
//METHODS
//1.) mainMenu()
/**
* Displays main menu and returns user choice <code>String</code>.
* 		
* @return a <code>String</code> containing the selected menu option
* @since V2.1 
*/	
	public static String startMenu() {
		System.out.println("\nPlease select an option from the menu below:\n"
				+ "cn -\tcreate new project\n"
				+ "vp -\tview projects & people\n"
				+ "up -\tupdate or finalize project\n"
				+ "e  -\texit");		
			String choice = INPUT.nextLine();
			return choice;
	};

	
//2.) createNew() 
/**
* Gets new project info via <code>{@link #getProjectAndPersonInfo() getProjectAndPersonInfo}</code>, and populates <code>Maps</code> 
* with new <code>{@link Project}</code> and <code>{@link Person}</code> objects via
* <code>{@link Project#populateProjectsMap(String[], Map <String, Project>) Project.populateProjectsMap}</code>, and
* <code>{@link Person#populatePersonsMap(String[][], Map <String, Person>) Person.populatePersonsMap}</code>.
* <p>
* This method allows back-navigation via <code>{@link #backToMenus1(String, boolean) backToMenus1}</code>.
* 
* @since V2.1
*/
	public static void createNew() {
		String[][] projectAndPersonInfo = getProjectAndPersonInfo();
		Project.populateProjectsMap (projectAndPersonInfo[0], projects);
		Person.populatePersonsMap (projectAndPersonInfo, persons);
		submenu = backToMenus1("\nCreate another project ('y'), go back to the main menu ('b'), or exit ('e')?", submenu); 
	};

	
//3.) viewSubmenu()
/**
* Displays view-submenu and returns user choice <code>String</code>.
* 		
* @return a <code>String</code> containing the selected menu option
* @since V2.1
*/
	public static String viewSubmenu() {
		System.out.println("\nPlease select a view option below:\n" 
				+ "va  -\tview all projects\n" 
				+ "vi  -\tview incomplete projects\n"
				+ "vpd -\tview projects past due date\n"
				+ "b   -\tgo back to the main menu\n"
				+ "e   -\texit");			
			String choiceVp = INPUT.nextLine();
			return choiceVp;
	}

	
//4.) viewSubmenuOutcomes() 
/**
* Controls view-submenu outcomes that display <code>{@link Project Projects}</code> via 
* <code>{@link Project#viewAll(Map<String, Project>) Project.viewAll}</code>,
* <code>{@link Project#viewIncomplete(Map<String, Project>) Project.viewIncomplete}</code>, and
* <code>{@link Project#viewPastDue(Map<String, Project>) Project.viewPastDue}</code> methods.
* <p>
* This method allows back-navigation via <code>{@link #backToMenus1(String, boolean) backToMenus1}</code>.
* 
* @param choiceVp a <code>String</code> containing a view-submenu choice 
* @since V2.1
*/	
	public static void viewSubmenuOutcomes(String choiceVp) {
		if (choiceVp.equalsIgnoreCase("va")) {
			Project.viewAll(projects);
			submenu = backToMenus1("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?", submenu);
//2.2) Displays incomplete projects.						
		} else if (choiceVp.equalsIgnoreCase("vi")) {
			Project.viewIncomplete(projects);
			submenu = backToMenus1("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?", submenu);
//2.3) Displays projects past their due date.					
		} else if (choiceVp.equalsIgnoreCase("vpd")) {
			Project.viewPastDue(projects);
			submenu = backToMenus1("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?", submenu);
//2.4) Goes back to main menu.								
		} else if (choiceVp.equalsIgnoreCase("b")) {
			submenu = false;	
//2.5) Writes LinkedHashMaps to file and exits program.						
		} else if (choiceVp.equalsIgnoreCase("e")) {
			System.exit(0);
		}
	};

	
//5.) select() 
/**
* Selects and returns a project via <code>{@link Project#selectProject(Map<String, Project>, String) Project.selectProject}</code>
* <p>
* If selected project doesn't exist, this method allows back-navigation via <code>{@link #backToMenus1(String, boolean) backToMenus1}</code>,
* else activates submenu loop via static <code>boolean</code> <code>submenu</code>.
* 
* @return the selected <code>{@link Project}</code>
* @since V2.1
*/
	public static Project select() {
		System.out.println("\nPlease enter the number or name of the project you wish to update or finalize:");
		Project selectedProject = Project.selectProject(projects, INPUT.nextLine()); //input heree???? or have to declare variable??/
	
		if (selectedProject == null) {
			selectProject = backToMenus1("\nThe project you are looking for has already been finalized or does not exist."
				+ "\nChoose another project ('y'), go back to the main menu ('b') or exit ('e')?", selectProject);
		} else {
			submenu = true;	
		}
		return selectedProject;
		
	}

	
//6.) updateSubmenu()
/**
* Displays update-submenu and returns user choice <code>String</code>.
* 
* @return a <code>String</code> containing the selected menu option
* @since V2.1
*/
	public static String updateSubmenu() {
		System.out.println("\nPlease select an update option below:\n"  
			+ "ud  -\tupdate deadline\n"
			+ "utp -\tupdate total amount paid to date\n"
			+ "ua - \tupdate architect details\n"
			+ "uc  -\tupdate contractor details\n"  
			+ "ucu -\tupdate custormer deteails\n"
			+ "fp - \tfinalize project\n"
			+ "o   -\tselect another project\n"
			+ "b   -\tgo back to the main menu\n"
			+ "e   -\texit");
		String choiceUp = INPUT.nextLine();
		return choiceUp;
	};
	
	
//7.) updateSubmenuOutcomes() 
/**
* Controls update-submenu outcomes that update <code>{@link Project}</code> and <code>{@link Person}</code> details via 
* <code>{@link Project#setDeadLine(String) Project.setDeadLine}</code>,
* <code>{@link Project#setTotalPaid(String) Project.setTotalPaid}</code>,
* <code>{@link Person#updatePerson(String[]) Person.updatePerson}</code>, and
* <code>{@link Project#generateInvoice(Map<String, Person>) Project.generateInvoice}</code>.
* <p>
* This method allows back-navigation via <code>{@link #backToMenus1(String, boolean) backToMenus1}</code>, 
* <code>{@link #backToMenus2(String) backToMenus2}</code>, and directly changing static <code>booleans</code>.
	 
* @param choiceUp a <code>String</code> containing an update-submenu choice
* @param selectedProject a <code>{@link Project}</code> selected by the user
* @since V2.1
*/
	public static void updateSubmenuOutcomes(String choiceUp, Project selectedProject) {
//3.1) Allows user to update project deadline.		
		if (choiceUp.equalsIgnoreCase("ud")) { 
			String newDeadLine = checkUserInput("\nPlease enter a new deadline ('dd MMM yyyy'):", 
				"(0[1-9]|[1-2][0-9]|3[0-1]) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) 20\\d\\d", 
				"Error - invalid date or date format. Please check date and enter the deadline in format 'dd MMM yyyy'"
				+ "(month is capitalized):");
			selectedProject.setDeadLine(newDeadLine);
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'),"
				+ "go back to the main menu ('b'), or exit ('e')?");
//3.2) Allows user to update total amount paid.								
		} else if (choiceUp.equalsIgnoreCase("utp")) {
			String newTotalPaid = checkUserInput("\nPlease enter a new total amount paid: R", "([1-9]\\d+(\\.\\d\\d)?|[0](\\.[0]{2})?)", 
				"Error. Please enter digits only - if more than zero, amount cannot start with zero; only two decimals allowed:");
			selectedProject.setTotalPaid(newTotalPaid);
			backToMenus2("\nUpdate another project detail or finalize project('y'), select another project ('o'),"
				+ "go back to the main menu ('b'), or exit ('e')?");
//3.3) Allows user to update architect's details.								
		} else if (choiceUp.equalsIgnoreCase("ua")) {	
			Person selectedPerson = Person.selectPerson(selectedProject.getArchitect(), persons);
			selectedPerson.updatePerson(getNewPersonInfo());             
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'),"
				+ "go back to the main menu ('b'), or exit ('e')?");	
//3.4) Allows user to update architect's details.																
		} else if (choiceUp.equalsIgnoreCase("uc")) {
			Person selectedPerson = Person.selectPerson(selectedProject.getContractor(), persons);
			selectedPerson.updatePerson(getNewPersonInfo());             
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'),"
				+ "go back to the main menu ('b'), or exit ('e')?");	
//3.5) Allows user to update architect's details.							
		} else if (choiceUp.equalsIgnoreCase("ucu")) {
			Person selectedPerson = Person.selectPerson(selectedProject.getCustomer(), persons);
			selectedPerson.updatePerson(getNewPersonInfo());             
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'),"
				+ "go back to the main menu ('b'), or exit ('e')?");	
//3.6) Allows user to finalize project, generates invoice, and writes project details to file.							
		} else if (choiceUp.equalsIgnoreCase("fp")) {
			String completionDate = checkUserInput("\nPlease enter the completion date ('dd MMM yyyy'):", 
				"(0[1-9]|[1-2][0-9]|3[0-1]) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) 20\\d\\d", 
				"Error. Please check date and enter the deadLine in format 'dd MMM yyyy':");
			selectedProject.setDeadLine(selectedProject.getDeadLine() + "\nFinalized:\t\t\t" + completionDate);			
			selectedProject.generateInvoice(persons);
			selectedProject.writeFinalized();
			submenu = false;
			selectProject = backToMenus1("\nSelect another project ('y'), go back to the main menu ('b'), or exit ('e')?", selectProject);
//3.7) Goes back to select another project.								
		} else if (choiceUp.equalsIgnoreCase("o")) {
			submenu = false;
//3.8) Goes back to the main menu.								
		} else if (choiceUp.equalsIgnoreCase("b")) {
			submenu = false;
			selectProject = false;	
//3.9) Writes LinkedHashMaps to text files, and exits program.						
		} else if (choiceUp.equalsIgnoreCase("e")) {                        
			closeApp();										
		}
	};
		
			
//8.) getProjectAndPersonInfo()
/**
 * Gets new project and person information via user input,
 * and returns a two dimensional a <code>Array</code> containing the info.
 * <p>
 * This method validates user input via <code>{@link #checkUserInput checkUserInput}</code>. If the project name is left blank, 
 * the method constructs a project name containing the building type and customer surname.
 * <p>
 * Changes from V1: errors in user input handled via regex.
 * 
 * @return projectAndPersonInfo a two dimensional <code>String</code> <code>Array</code> containing new project and person info.
 * @since V1	
 */
	public static String[][] getProjectAndPersonInfo() {
		System.out.println("\nPlease enter new project information:\n");
		
//Collects project and person information from user, and validates input using regular expressions (Vogel, 2007).
		String projectNum = checkUserInput("Project number:", "\\d+", "Error. Please enter digits only:");
		String projectName = checkUserInput("Project name:", "([A-Z][a-z]+((-| )[A-Z]?[a-z]+)*)?",
				"Error. Please make sure project name is capitalized and contains no non-letter characters except hyphens:");	               
		String buildType = checkUserInput("Building type:", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure building type is capitalized and contains no non-letter characters except hyphens::");																							            
		String physAddress = checkUserInput("Physical address ('Address line 1, Address line 2, Address line 3, ..., postal code'):",
				"((\\d+ )?[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*, ){2,}(\\d{4})", 
				"Error. Please enter address in format 'Address line 1, Address line 2, Address line 3, ..., postal code' "
				+ "\n(min two address lines, address lines capitalized, 4 digit postal code):");                 
		String erfNum = checkUserInput("ERF number:", "\\d+", "Error. Please enter digits only:");	
		String totalFee = checkUserInput("Total fee R:", "[1-9]\\d+(\\.\\d\\d)?", 
				"Error. Please enter digits only - amount cannot start with zero and only two decimals allowed:");
		String totalPaid = checkUserInput("Total amount paid to date R:", "([1-9]\\d+(\\.\\d\\d)?|[0](\\.[0]{2})?)", 
				"Error. Please enter digits only - if more than zero, amount cannot start with zero; only two decimals allowed:");                   
		String deadLine = checkUserInput("Project deadline ('dd MMM yyyy'):", 
				"(0[1-9]|[1-2][0-9]|3[0-1]) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) 20\\d\\d", 
				"Error - invalid date or date format. Please check date and enter the deadline in format 'dd MMM yyyy' (month is capitalized):");
		
		String architectSur = checkUserInput("Architect surname:", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure surname is capitalized and contains no non-letter characters except hyphens:");		
		String architectNam = checkUserInput("Architect first name(s):", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure name is capitalized and contains no non-letter characters except hyphens:");
		String architectCell = checkUserInput("Architect cellphone number:", "0\\d{9}", 
				"Error. Ivalid number. Please make sure number contains 10 digits and starts with 0:");	          
//Email regex based on regex suggested by Gupta (2020).		
		String architectMail = checkUserInput("Architect email address:", 
				"(?!.*([_!#$%&'*+/=?`{|}~^-])\\1)([a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+\\.)*[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+@"
				+ "((([a-zA-Z0-9]+[-][a-zA-Z0-9]+)+|[a-zA-Z0-9]+)\\.)*(?=.*[a-zA-Z])[a-zA-Z0-9]{2,}",   
				"Error. Invalid email address format."); 
		String architectAddress = checkUserInput("Architect physical address ('Address line 1, Address line 2, Address line 3, ..., postal code'):", 
				"((\\d+ )?[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*, ){2,}(\\d{4})", 
				"Error. Please enter address in format 'Address line 1, Address line 2, Address line 3, ..., postal code' "
				+ "\n(min two address lines, address lines capitalized, 4 digit postal code):");
		
		String contractorSur = checkUserInput("Contractor surname:", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure surname is capitalized and contains no non-letter characters except hyphens:");	
		String contractorNam = checkUserInput("Contractor first name(s):", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure name is capitalized and contains no non-letter characters except hyphens:");
		String contractorCell = checkUserInput("Contractor cellphone number:", "0\\d{9}", 
				"Error. Ivalid number. Please make sure number contains 10 digits and starts with 0:");		
		String contractorMail = checkUserInput("Contractor email address:", 
				"(?!.*([_!#$%&'*+/=?`{|}~^-])\\1)([a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+\\.)*[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+@"
				+ "((([a-zA-Z0-9]+[-][a-zA-Z0-9]+)+|[a-zA-Z0-9]+)\\.)*(?=.*[a-zA-Z])[a-zA-Z0-9]{2,}",   
				"Error. Invalid email address format."); 	
		String contractorAddress = checkUserInput("Contractor physical address ('Address line 1, Address line 2, Address line 3, ..., postal code'):", 
				"((\\d+ )?[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*, ){2,}(\\d{4})", 
				"Error. Please enter address in format 'Address line 1, Address line 2, Address line 3, ..., postal code' "
				+ "\n(min two address lines, address lines capitalized, 4 digit postal code):");

		String customerSur = checkUserInput("Customer surname:", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure surname is capitalized and contains no non-letter characters except hyphens:");	
		String customerNam = checkUserInput("Customer first name(s):", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure name is capitalized and contains no non-letter characters except hyphens:");
		String customerCell = checkUserInput("Customer cellphone number:", "0\\d{9}", 
				"Error. Ivalid number. Please make sure number contains 10 digits and starts with 0:");
		String customerMail = checkUserInput("Customer email address:", 
				"(?!.*([_!#$%&'*+/=?`{|}~^-])\\1)([a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+\\.)*[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+@"
				+ "((([a-zA-Z0-9]+[-][a-zA-Z0-9]+)+|[a-zA-Z0-9]+)\\.)*(?=.*[a-zA-Z])[a-zA-Z0-9]{2,}",   
				"Error. Invalid email address format."); 
		String customerAddress = checkUserInput("Customer physical address ('Address line 1, Address line 2, Address line 3, ..., postal code'):", 
				"((\\d+ )?[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*, ){2,}(\\d{4})", 
				"Error. Please enter address in format 'Address line 1, Address line 2, Address line 3, ..., postal code' "
				+ "\n(min two address lines, address lines capitalized, 4 digit postal code):");
				  
//Blank ArrayList (W3schools.com, 2021***).                                            
		List <String> projectNameBlank = new ArrayList <String>();
		projectNameBlank.addAll(Arrays.asList("\n", "\r", "\r\n", " ", ""));
	
//If user leaves project name blank (blank list contains user input - Landup, 2021), 
//sets project name to building type plus customer surname.
		if (projectNameBlank.contains(projectName)) { 
			projectName = buildType + " " + customerSur; 
		}
		String[][] projectAndPersonInfo = {{projectNum, projectName, buildType, physAddress, erfNum, totalFee, totalPaid, deadLine, 
			architectNam + " " + architectSur, contractorNam + " " + contractorSur, customerNam + " " + customerSur},
			{"Architect", architectSur, architectNam, architectCell, architectMail, architectAddress},
			{"Contractor", contractorSur, contractorNam, contractorCell, contractorMail, contractorAddress},
			{"Customer", customerSur, customerNam, customerCell, customerMail, customerAddress}};
		
	    return projectAndPersonInfo;
	};

	
//9.) getNewPersonInfo()
/**
* Gets and validates new <code>{@link Person}</code> attribute values (for updates) via 
* <code>{@link #checkUserInput(String, String, String) checkUserInput}</code>,
* and returns a <code>List</code> containing the new info <code>Strings</code>. 
*  
* @return a <code>List</code> containing new <code>Person</code> attribute <code>Strings</code>
* @since V2.1
*/
	public static String[] getNewPersonInfo() {
		String newCellNum = checkUserInput("\nPlease enter a new cell number or press enter to skip:", "(0\\d{9})?", 
				"Error. Ivalid number. Please make sure number contains 10 digits and starts with 0:");				
		String newEmailAddress = checkUserInput("Please enter a new email address or press enter to skip:", 
				"((?!.*([_!#$%&'*+/=?`{|}~^-])\\1)([a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+\\.)*[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+@"
				+ "((([a-zA-Z0-9]+[-][a-zA-Z0-9]+)+|[a-zA-Z0-9]+)\\.)*(?=.*[a-zA-Z])[a-zA-Z0-9]{2,})?",   
				"Error. Invalid email address format."); 			
		String newPhysAddress = checkUserInput("Please enter a new physical address or press enter to skip:"
				+ "('Address line 1, Address line 2, Address line 3, ..., postal code'):", 
				"(((\\d+ )?[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*, ){2,}(\\d{4}))?", 
				"Error. Please enter address in format 'Address line 1, Address line 2, Address line 3, ..., postal code' "
					+ "\n(min two address lines, address lines capitalized, 4 digit postal code):");
	
		String[] personInfo = {newCellNum, newEmailAddress, newPhysAddress};
		return personInfo;
	};

	
//10.) checkUserInput()
/**
 * Checks user input against a regular expression (passed as parameter),
 * displays an error message while the input does not match, or returns the 
 * user input <code>String</code> once it matches.
 * 	
 * @param prompt a <code>String</code> prompting user input
 * @param regex the regular expression that user input is matched against
 * @param errorPrompt the error message displayed if user input does not match regex
 * @return a <code>String</code> containing the user input 
 * @since V2.0
 */
	public static String checkUserInput(String prompt, String regex, String errorPrompt) {
		System.out.println(prompt);
		String userInput = INPUT.nextLine();
//Checks if input matches regex (Tutorialspoint, 2021).	
		while (userInput.matches(regex) == false) {
			System.out.println(errorPrompt);
			userInput = INPUT.nextLine();
		}
		return userInput;
	};		

	
//11.) backToMenus1()	
/**
 * Controls back-navigation via changing and returning a static control <code>boolean</code>.
 * <p>
 * This method allows for three navigation options: go back to a submenu or select another project,
 * go back to the main menu, or exit.
 * <p>
 * Refactored from V1 <code>backtoMenus</code> method.
 * 	
 * @param optionString a <code>String</code> displaying the navigation options
 * @param staticBoolean the relevant static control <code>boolean</code>
 * @return the relevant static control <code>boolean</code> - changed according to user navigation choice
 * @since V2.0
 */
	public static boolean backToMenus1(String optionString, boolean staticBoolean) {					                                                         
		while (true) {											
			System.out.println(optionString); 					
			String goBack = INPUT.nextLine();
//Goes back to current submenu loop.
			if (goBack.equalsIgnoreCase("y")) {				
				return staticBoolean;
//Goes back to main menu.
			} else if (goBack.equalsIgnoreCase("b")) {
				staticBoolean = false;
				return staticBoolean;
//Writes LinkedHashMaps to text files and exits program.
			} else if (goBack.equalsIgnoreCase("e")) {				
				closeApp();  
			}
		}
	};
	
	
//12.) backToMenus2()	
/**
 * Controls back-navigation via directly changing static control <code>booleans</code>.
 * <p>
 * This method allows for 4 navigation options: go back to a submenu, select another project,
 * go back to the main menu, or exit.
 * <p>
 * Refactored from V1 <code>backtoMenus</code> method.
 * 	
 * @param optionString a <code>String</code> displaying the navigation options
 * @since V2.0
 */
	public static void backToMenus2(String optionString) {			
		while (true)  {
			System.out.println(optionString); 					
			String goBack = INPUT.nextLine();
//Goes back to current submenu loop.				
			if (goBack.equalsIgnoreCase("y")) {			
				break;
//Goes back to select another project.
			} else if (goBack.equalsIgnoreCase("o")) {
				submenu = false;
				break;
//Goes back to main menu
			} else if (goBack.equalsIgnoreCase("b")) {											
				submenu = false;
				selectProject = false;
				break;
//Writes LinkedHashMaps to text files and exits program.
			} else if (goBack.equalsIgnoreCase("e")) {
				closeApp(); 
			}					
		}		
	};						


//13.) startSequenceReadHashMaps()	
/**
* Reads/deserializes a <code>Map</code> from a txt file and returns it
* (if no file exists or the file is empty, it returns an empty <code>Map</code>).
* <p>
* This method is called twice at the start of <code>ProjectManager</code> app to create two <code>LinkedHashMaps</code>,
* respectively storing the <code>{@link Project}</code> and <code>{@link Person}</code> objects created and used by the application.
* It catches <code>FileNotFoundException</code>, <code>IOException</code>, and <code>ClassNotFoundException</code>.
* 
* @param fileName the <code>String</code> name of the text file to be read
* @return the <code>LinkedHashMap</code> object contained in the text file
* @since V2.0
*/
	public static Map <?, ?> startSequenceReadHashmaps(String fileName) {          
		Map <?, ?> temp = null;
		File HashMapTextFile = new File(fileName);
			
//Checks if file is empty (Paul, 2021) and returns empty LinkedHashMap if yes.
		if (HashMapTextFile.length() == 0) {
			temp = new LinkedHashMap<>();
			return temp;			
		} else {			
			try {
//Creates InputStream from file if exists and wraps it in an ObjectInputStream (Datsabk, 2016 & Geeksforgeeks.com, 2019*). 
				FileInputStream fis = new FileInputStream(fileName);           
				ObjectInputStream oips = new ObjectInputStream(fis);
//Reads LinkedHashMap from file (Datsabk, 2016 & Geeksforgeeks.com, 2019*).
				temp = (LinkedHashMap<?, ?>)oips.readObject();
				oips.close();
				fis.close();
//Thrown by FileinputStream constructor (Docs.oracle.com, 2020).
			} catch (FileNotFoundException e) {
				System.out.println("Error. " + fileName + " does not exist or cannot be created.");   
				e.printStackTrace();
//Thrown by ObjectinputStream constructor and readObject() (Docs.oracle.com, 2020***).
			} catch (IOException io) {
				System.out.println("Error. Problem has occured with I/O stream.");
				io.printStackTrace();
//Thrown by readObject() (Docs.oracle.com, 2020***).
			} catch (ClassNotFoundException c) {
				System.out.println("Error. Class of object not found.");
				c.printStackTrace();
			}
			return temp;
		}			
	};	
	
	
//14.) endSequenceWriteToFile()	
/**
 * Writes/serializes a <code>Map</code> to a text file
 * (overwrites any existing <code>Map</code> object in the file).
 * <p>
 * This method is run twice every time <code>ProjectManager</code> app is exited to serialize 
 * two <code>LinkedHashMaps</code>, respectively containing the <code>{@link Project}</code> and <code>{@link Person}</code> objects 
 * created and used by the application.
 * It catches <code>FileNotFoundException</code> and <code>IOException</code>.
 * 	
 * @param fileName the <code>String</code> name of the text file to be written to
 * @param hashMap the <code>Map</code> object to be written to file
 * @since V2.0
 */
	public static void endSequenceWriteToFile(String fileName, Map<?,?> hashMap) {   
		Map<?,?> temp = hashMap;
		
		try {
//Creates new file or creates FileOutputStream to existing file,
			FileOutputStream fos = new FileOutputStream(fileName);
//and wraps FileOutputStream in ObjectOutputStream (Datsabk, 2016 & Geeksforgeeks.com, 2019*).
			ObjectOutputStream oos = new ObjectOutputStream(fos);
//Writes LinkedHashMap object to file (Datsabk, 2016 & Geeksforgeeks.com, 2019*).
			oos.writeObject(temp);                                          
			oos.close();
			fos.close();
//Thrown by FileOutputStream constructor (Docs.oracle.com, 2015).
		} catch (FileNotFoundException e) {
			System.out.println("Error. " + fileName + " does not exist or cannot be created.");   
			e.printStackTrace();
//Thrown by ObjectOutputStream constructor and writeObject() (Docs.oracle.com, 2020*).
		} catch (IOException io) {
			System.out.println("Error. Problem has occured with I/O stream.");
			io.printStackTrace();
		}		
	};


//15.) closeApp() 
/**
* Writes/serializes <code>Map</code> objects to txt file via
* <code>{@link #endSequenceWriteToFile(String, Map<?,?>) endSequenceWriteToFile}</code>, and exits program.
* 
* @since V2.1
*/
	public static void closeApp() {
		endSequenceWriteToFile("projects.txt", projects);
		endSequenceWriteToFile("persons.txt", persons);
		INPUT.close();
//Terminates JVM (Geeksforgeeks.com, 2016)		
		System.exit(0);
	}
}

			
///////////////////////////////// THE END ///////////////////////////////////


//References:

//Bright (2012), & Julien (ed.)(2012). [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/10144636/rounding-with-decimalformat-in-java [Accessed 22 July 2021].
//Datsabk (2016). How to read and write Java object to a file. [online] Mykong.com. Available at: https://mkyong.com/java/how-to-read-and-write-java-object-to-a-file/ [Accessed 06 June 2021].
//Docs.oracle.com (2015). java.io Class FileOutputStream. [online] Available at: https://docs.oracle.com/javase/6/docs/api/java/io/FileOutputStream.html#FileOutputStream%28java.lang.String,%20boolean%29 [Accessed 06 June 2021].
//Docs.oracle.com (2020). Java.ioClass FileInputStream. [online] Available at: https://docs.oracle.com/javase/7/docs/api/java/io/FileInputStream.html [Accessed 06 June 2021].
//Docs.oracle.com (2020)*.java.io Class ObjectOutputStream. [online] Available at: https://docs.oracle.com/javase/7/docs/api/java/io/ObjectOutputStream.html [Accessed 06 June 2021].
//Docs.oracle.com (2020)**. java.io Class FileWriter [online] Available at: https://docs.oracle.com/javase/7/docs/api/java/io/FileWriter.html [Accessed 22 July 2021].
//Docs.oracle.com (2020)***. java.io Class ObjectInputStream. [online] Available at: https://docs.oracle.com/javase/7/docs/api/java/io/ObjectInputStream.html [Accessed 06 June 2021].
//Docs.oracle.com (2021). java.time Class LocalDate. [online] Available at: https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html [Accessed 16 July 2021].
//Docs.oracle.com (2021)*. java.time.format Class DateTimeFormatter [online] Available at: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html [Accessed 16 July 2021].
//Docs.oracle.com (2021)**. java.util Class Formatter. [online] Available at: https://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#dndec [Accessed 20 May 2021].
//Francis (2013). About File file = new File(path). [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/19702659/about-file-file-new-filepath [Accessed 06 June 2021].
//Geeksforgeeks.com (2016). System.exit() in Java. [online] Available at: https://www.geeksforgeeks.org/system-exit-in-java/  [Accessed 22 July 2021].
//Geeksforgeeks.com (2018). Overriding toString() in Java. [online] Available at: https://www.geeksforgeeks.org/overriding-tostring-method-in-java/ [Accessed 03 June 2021].
//Geeksforgeeks.com (2018)*. Split() String method in Java with examples. [online] Available at: https://www.geeksforgeeks.org/split-string-java-examples/ [Accessed 03 June 2021].
//Geeksforgeeks.com (2019). File handling in Java using FileWriter and FileReader. [online] Available at: https://www.geeksforgeeks.org/file-handling-java-using-filewriter-filereader/ [Accessed 16 July 2021].
//Geeksforgeeks.con (2019)*. Serialization and Deserialization in Java with Example. [online] Available at: https://www.geeksforgeeks.org/serialization-in-java/ [Accessed 22 July 2021].
//Geeksforgeeks.com (2021). LinkedHashMap in Java. [online] Available at: https://www.geeksforgeeks.org/linkedhashmap-class-java-examples/ [Accessed 22 July 2021].
//Github.io (2021). Google Java Style Guide. [online] Available at: https://google.github.io/styleguide/javaguide.html#s4-formatting [Accessed 20 May 2021].
//Gupta, L. (2020). Java email validation using regex. [online] Howtodoinjava.com. Available at: https://howtodoinjava.com/java/regex/java-regex-validate-email-address/ [Accessed 18 July 2021].
//harto (2009). Iterate through a HashMap [duplicate]. [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap [Accessed 16 July 2021].
//Howard (2011). Java string align to right. [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/6080390/java-string-align-to-right [Accessed 22 July 2021].
//iamvineettiwari012 (2012). HashMap replace(key, value) method in Java with Examples. [online] Geeksforgeeks.com. Available at: https://www.geeksforgeeks.org/hashmap-replacekey-value-method-in-java-with-examples/ [Accessed 16 July 2021].
//JAVA (2013). Global variables in Java. [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/4646577/global-variables-in-java [Accessed 22 July 2021].
//Javatpoint.com (2021). Java String format(). [online] Available at: https://www.javatpoint.com/java-string-format [Accessed 22 July 2021].
//Jesper (2012). Java object name from user input as method call. [online]  Stackoverflow.com. Available at: https://stackoverflow.com/questions/13743637/java-object-name-from-user-input-as-method-call [Accessed 22 July 2021].
//Jha, A. (2013) & user207421 (ed.)(2019). Static Final Long serialVersionUID = 1L [duplicate]. [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/14274480/static-final-long-serialversionuid-1l [Accessed 06 June 2021].
//Landup, D. (2021). Java: Check if String Contains a Substring. [online] Stackabuse.com. Available at: https://stackabuse.com/java-check-if-string-contains-a-substring [Accessed 22 July 2021].
//Leon (2016). What does System.out.printf( “%-15s%03d\n”, s1, x) do? [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/37780027/what-does-system-out-printf-15s03d-n-s1-x-do [Accessed 22 July 2021].
//Ligios (2019). A Practical Guide to DecimalFormat. [online] Baeldung.com. Available at: https://www.baeldung.com/java-decimalformat [Accessed 22 July 2021].
//Myers, M. (2009). How do I address unchecked cast warnings? [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/509076/how-do-i-address-unchecked-cast-warnings [Accessed 22 July 2021].
//Oracle.com (2021). 9 - Naming Conventions. [online] Available at: https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html [Accessed 20 May 2021].
//Oracle.com (2021)*. How to Write Doc Comments for the Javadoc Tool. [online] Available at: https://www.oracle.com/za/technical-resources/articles/java/javadoc-tool.html#exampleresult [Accessed 22 July 2021].
//Paul, J. (2021). A Simple Example to Check if File is Empty in Java. [online] Java67.com. Available at: https://www.java67.com/2018/03/a-simple-example-to-check-if-file-is-empty-in-java.html [Accessed 06 June 2021].
//Ram, V. (2018). Final static variables in Java. [online] Tutorialspoint.com. Available at: https://www.tutorialspoint.com/Final-static-variables-in-Java [Accessed 22 July 2021].
//Skeet. J. (2008) & jcsahnwaldt Reinstate Monica (ed.)(2014). What is a serialVersionUID and why should I use it? [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/285793/what-is-a-serialversionuid-and-why-should-i-use-it [Accessed 06 June 2021].     
//Striver (2018). Arraylist.contains() in Java. [online] Geeksforgeeks.com. Available at: https://www.geeksforgeeks.org/arraylist-contains-java/ [Accessed 03 June 2021].
//talnicolas (2012) & Damiano (ed.)(2016). Java FileOutputStream Create File if not exists.[online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/9620683/java-fileoutputstream-create-file-if-not-exists [Accessed 06 June 2021].
//Tutorialspoint.com (2021). Java - String matches() Method. [online] Available at: https://www.tutorialspoint.com/java/java_string_matches.htm [Accessed 17 May 2021]. 
//Vogel, L. (2007). Regular expressions in Java - Tutorial. [online] Vogella.com. Available at: https://www.vogella.com/tutorials/JavaRegularExpressions/article.html [Accessed 17 May 2021].
//W3schools.com (2021). Java For Loop. [online] Available at: https://www.w3schools.com/java/java_for_loop.asp [Accessed 03 June 2021].
//W3schools.com (2021)*. Java String replace() Method. [online] Available at: https://www.w3schools.com/java/ref_string_replace.asp [Accessed 16 July 2021].
//W3schools.com (2021)**. Java HashMap. [online] Available at: https://www.w3schools.com/java/java_hashmap.asp [Accessed 22 July 2021].
//W3schools.com (2021)***. Java ArrayList. [online] Available at: https://www.w3schools.com/java/java_arraylist.asp [Accessed 22 July 2021].
//WhiteFang34 (2011). Convert String to double in Java. [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/5769669/convert-string-to-double-in-java [Accessed 22 July 2021].


/////////////////////////////////////////////////////