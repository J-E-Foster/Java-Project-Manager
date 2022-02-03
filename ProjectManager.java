package projectmanager;

//Note: All package classes edited according to Google Java Style Guide (Github.io, 2021) and Java naming conventions (Oracle.com, 2021).
//Javadoc comments added according to Oracle.com (2021*).

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * This application class is a project-management program that allows users to create, view, update, and finalize projects
 * stored in an SQL database.
 * <p>
 * It was developed for a fictional small structural engineering firm called Poised.
 * <p>
 * Upon startup, this class reads project and person info from the DB. It passes the info to <code>{@link Project}</code> and <code>{@link Person}</code> 
 * constructors, and stores the resulting objects in four <code>LinkedHashMaps</code> - two reference <code>Maps</code>, and two operational <code>Maps</code>. 
 * Upon exiting, this class compares the reference and operational <code>Maps</code>, and writes any changes to DB.
 * The main method follows a boolean control structure consisting of one main menu loop, three submenu loops, and one select-project loop.
 * <p>
 * Changes from V2.0: Main method shortened and refactored into static methods. 
 * Methods <code>{@link Project#selectProject(Map<String, Project>, String) selectProject}</code>,
 * <code>{@link Project#generateInvoice(Map<String, Person>) generateInvoice}</code>,
 * <code>{@link Project#viewAll(Map<String, Project>) viewAll}</code>,
 * <code>{@link Project#viewIncomplete(Map<String, Project>) viewIncomplete}</code>, and
 * <code>{@link Project#viewPastDue(Map<String, Project>) viewPastDue}</code> moved to <code>Project</code>.
 * Methods <code>{@link Person#selectPerson(String, Map<String, Person>) selectPerson}</code> and 
 * <code>{@link Person#updatePerson(List<String>) updatePerson}</code> moved to <code>Person</code>.
 * <p>
 * Changes from V2.1: Class no longer creates/uses text files; SQL DB functionality added via methods 
 * <code>{@link #readTable(Connection, List<String>, String) readTable}</code>,
 * <code>{@link #writeToTable(Connection, List<List<String>>, List<List<String>>, List<String>, String) writeToTable}</code>,
 * <code>{@link #startSequenceCreateMaps(Connection) startSequenceCreateMaps}</code>, and
 * <code>{@link #endSequenceMapsToTables(Connection) endSequenceMapsToTables}</code>
 * (they replace removed <code>startSequenceReadHashmap</code> and <code>endSequenceWriteToFile</code>).
 * .
 * @author J.E. Foster © 
 * @version V3.0 August 2021
 *
 */
public class ProjectManager {

//ATTRIBUTES (for static variables - see Ram, 2018 and JAVA, 2013).
//Maps store Project and Person objects. 
//LinkedHashMaps (Geeksforgeeks.com, 2021) used instead of HashMaps (W3schools.com, 2021**) to maintain insertion order. 
	private static Map <String, Person> personsExisting = new LinkedHashMap<String, Person>();      
	private static Map <String, Project> projectsExisting = new LinkedHashMap<String, Project>();  
	private static Map <String, Person> personsNewAndEdited = new LinkedHashMap<String, Person>();
	private static Map <String, Project> projectsNewAndEdited = new LinkedHashMap<String, Project>();
	
//Controls submenu loops.
	private static boolean submenu = false;	
//Controls select-project loops.
	private static boolean selectProject = false;	
	private static final Scanner INPUT = new Scanner(System.in); 
	
	
//MAIN METHOD
	public static void main (String [] args) {	
		try {
//Connects to "poisepms" database via jdbc:mysql: channel on localhost (this PC)
//for username "otheruser", password "swordfish" (Hyperiondev, 2021*, p.23).
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/poisepms?useSSL=false", "otheruser", "swordfish");
			
//Populates Maps with Project/Person objects via DB info.			
			startSequenceCreateMaps(connection);
			
			System.out.println("Welcome to the Poised Project Manager.");
		
//Main loop.	
			while (true) {
//0.) Displays main menu.
				String choice = mainMenu();
				
//Outcomes for main menu choices:			
//1.) Create-project submenu loop. 
				if (choice.equalsIgnoreCase("cn")) {
					submenu = true;		
					while (submenu) {
						createNew(connection);
					}				
//2) View-submenu loop: displays submenu; controls outcomes.
				} else if (choice.equalsIgnoreCase("vp")) {
					submenu = true;	
					while (submenu) {
						String choiceVp = viewSubmenu();
						viewSubmenuOutcomes(choiceVp, connection);
					}
//3.) Update-project loops.				
				} else if (choice.equalsIgnoreCase("up")) {
//3.1) Select-project loop.					
					selectProject = true; 
					while (selectProject) {
						Project selectedProject = select(connection);	
//3.2) Update-submenu loop: displays submenu; controls outcomes for selected project.  
						while (submenu) {
							String choiceUp = updateSubmenu();
							updateSubmenuOutcomes(choiceUp, selectedProject, connection);
						}
					}									
//4.)Writes Map changes to DB and exits program.				
				} else if (choice.equalsIgnoreCase("e")) {			
					closeApp(connection);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();		
		}
	};
	
//METHODS
//1.) startSequenceCreateMaps() 
/**
 * Reads all information from all DB tables via <code>{@link #readTable(Connection, List<String>, String) readTable}</code>, then
 * uses the info to populate reference and operational <code>Maps</code> with <code>{@link Project}</code> or <code>{@link Person}</code> objects via
 * <code>{@link Project#populateProjectsMap(List<String>, Map<String, Project>) Project.populateProjectsMap}</code> and 
 * <code>{@link Person#populatePersonsMap(List<String>, Map<String, Person>) Person.populatePersonsMap}</code>.
 * <p>
 * This method runs at program startup.
 * 
 * @param connection an SQL database <code>Connection</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V3.0
 */
	public static void startSequenceCreateMaps(Connection connection) throws SQLException {	
//Reads Project table & stores each row (as List) in a List of Lists.	
		List<List<String>> projectList = readTable(connection, Arrays.asList("ProjectNum", "ProjectName", "BuildType", "PhysAddress", "ErfNum", 
				"TotalFee", "TotalPaid", "DeadLine", "Architect", "Contractor", "Customer", "StructuralEng", "ProjectMan"), "Project");
		
//For each List (i.e. project) in List of Lists, populates reference and operational Maps with Project.
		for (List<String> i : projectList) {
			Project.populateProjectsMap(i, projectsExisting);
			Project.populateProjectsMap(i, projectsNewAndEdited);
		}	
		List<List<String>> allPersonsList = new ArrayList<>();
		String[] personTypeArr = {"Architect", "Contractor", "Customer", "StructuralEng", "ProjectMan"};
		
//For each person type, reads corresponding table & stores each row (as List) in a List of Lists		
		for (String i : personTypeArr) {
			List<List<String>> personList = readTable(connection, Arrays.asList("Surname", "Name", "CellNum", "EmailAddress", "PhysAddress", "Type"), i);
			allPersonsList.addAll(personList);
		}	
//For each List (i.e. person) in List of Lists, populates reference and operational Maps with Person.
		for (List<String> i : allPersonsList) {
			Person.populatePersonsMap(i, personsExisting);
			Person.populatePersonsMap(i, personsNewAndEdited);
		}
	};
	

//2.) mainMenu()
/**
 * Displays main menu and returns user choice <code>String</code>.
 * 		
 * @return a <code>String</code> containing the selected menu option
 * @since V2.1 
 */
	public static String mainMenu() {
		System.out.println("\nPlease select an option from the menu below:\n"
				+ "cn -\tcreate new project\n"
				+ "vp -\tview projects & people\n"
				+ "up -\tupdate or finalize project\n"
				+ "e  -\texit");		
			String choice = INPUT.nextLine();
			return choice;
	};
	

//3.) createNew() 
/**
 * Gets new project info via <code>{@link #getProjectAndPersonInfo() getProjectAndPersonInfo}</code>, and populates operational <code>Maps</code> 
 * with new <code>{@link Project}</code> and <code>{@link Person}</code> objects via
 * <code>{@link Project#populateProjectsMap(List<String>, Map<String, Project>) Project.populateProjectsMap}</code>, and
 * <code>{@link Person#populatePersonsMap(List<String>, Map<String, Person>) Person.populatePersonsMap}</code>.
 * <p>
 * This method allows back-navigation via <code>{@link #backToMenus1(String, boolean, Connection) backToMenus1}</code>.
 * 
 * @param connection an SQL database <code>Connection</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V2.1
 */
	public static void createNew(Connection connection) throws SQLException {
		List<List<String>> projectAndPersonInfo = getProjectAndPersonInfo();
//Populates Project operational Map	
		Project.populateProjectsMap (projectAndPersonInfo.get(0), projectsNewAndEdited);
//Populates Person operational Map (five Persons) 		
		for (int i = 1; i < 6; i++) {
			Person.populatePersonsMap (projectAndPersonInfo.get(i), personsNewAndEdited);
		}	
		submenu = backToMenus1("\nCreate another project ('y'), go back to the main menu ('b'), or exit ('e')?", submenu, connection); 
	};
	
	
//4.) viewSubmenu()
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
	

//5.) viewSubmenuOutcomes() 
/**
 * Controls view-submenu outcomes that display <code>{@link Project Projects}</code> via 
 * <code>{@link Project#viewAll(Map<String, Project>) Project.viewAll}</code>,
 * <code>{@link Project#viewIncomplete(Map<String, Project>) Project.viewIncomplete}</code>, and
 * <code>{@link Project#viewPastDue(Map<String, Project>) Project.viewPastDue}</code> methods.
 * <p>
 * This method allows back-navigation via <code>{@link #backToMenus1(String, boolean, Connection) backToMenus1}</code>.
 * 
 * @param choiceVp a <code>String</code> containing a view-submenu choice 
 * @param connection an SQL database <code>Connection</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V2.1
 */
	public static void viewSubmenuOutcomes(String choiceVp, Connection connection) throws SQLException {
		if (choiceVp.equalsIgnoreCase("va")) {
			Project.viewAll(projectsNewAndEdited);
			submenu = backToMenus1("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?", submenu, connection);
//2.2) Displays incomplete projects.						
		} else if (choiceVp.equalsIgnoreCase("vi")) {
			Project.viewIncomplete(projectsNewAndEdited);
			submenu = backToMenus1("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?", submenu, connection);
//2.3) Displays projects past their due date.					
		} else if (choiceVp.equalsIgnoreCase("vpd")) {
			Project.viewPastDue(projectsNewAndEdited);
			submenu = backToMenus1("\nView other projects('y'), go back to the main menu ('b'), or exit ('e')?", submenu, connection);
//2.4) Goes back to main menu.								
		} else if (choiceVp.equalsIgnoreCase("b")) {
			submenu = false;	
//2.5) Writes LinkedHashMaps to file and exits program.						
		} else if (choiceVp.equalsIgnoreCase("e")) {
			closeApp(connection);
		}
	};
	

//6.) select() 
/**
 * Selects and returns a project via <code>{@link Project#selectProject(Map<String, Project>, String) Project.selectProject}</code>
 * <p>
 * If selected project doesn't exist, this method allows back-navigation via <code>{@link #backToMenus1(String, boolean, Connection) backToMenus1}</code>,
 * else activates submenu loop via static <code>boolean</code> <code>submenu</code>.
 * 
 * @param connection an SQL database <code>Connection</code>
 * @return the selected <code>{@link Project}</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V2.1
 */
	public static Project select(Connection connection) throws SQLException {
		System.out.println("\nPlease enter the number or name of the project you wish to update or finalize:");
		Project selectedProject = Project.selectProject(projectsNewAndEdited, INPUT.nextLine()); 
	
		if (selectedProject == null) {
			selectProject = backToMenus1("\nThe project you are looking for has already been finalized or does not exist."
				+ "\nChoose another project ('y'), go back to the main menu ('b') or exit ('e')?", selectProject, connection);
		} else {
			submenu = true;	
		}
		return selectedProject;
		
	}

	
//7.) updateSubmenu()
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
			+ "ucu -\tupdate custormer details\n"
			+ "use -\tupdate structural engineer details\n"
			+ "upm -\tupdate project manager details\n"
			+ "fp - \tfinalize project\n"
			+ "o   -\tselect another project\n"
			+ "b   -\tgo back to the main menu\n"
			+ "e   -\texit");
		String choiceUp = INPUT.nextLine();
		return choiceUp;
	};
	

//8.) updateSubmenuOutcomes() 
/**
 * Controls update-submenu outcomes that update <code>{@link Project}</code> and <code>{@link Person}</code> details via 
 * <code>{@link Project#setDeadLine(String) Project.setDeadLine}</code>,
 * <code>{@link Project#setTotalPaid(String) Project.setTotalPaid}</code>,
 * <code>{@link Person#updatePerson(List<String>) Person.updatePerson}</code>, and
 * <code>{@link Project#generateInvoice(Map<String, Person>) Project.generateInvoice}</code>.
 * <p>
 * This method allows back-navigation via <code>{@link #backToMenus1(String, boolean, Connection) backToMenus1}</code>, 
 * <code>{@link #backToMenus2(String, Connection) backToMenus2}</code>, and directly changing static <code>booleans</code>.
 
 * @param choiceUp a <code>String</code> containing an update-submenu choice
 * @param selectedProject a <code>{@link Project}</code> selected by the user
 * @param connection an SQL database <code>Connection</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V2.1
 */
	public static void updateSubmenuOutcomes(String choiceUp, Project selectedProject, Connection connection) throws SQLException {
//8.1) Allows user to update project deadline.		
		if (choiceUp.equalsIgnoreCase("ud")) { 
			String newDeadLine = checkUserInput("\nPlease enter a new deadline ('dd MMM yyyy'):", 
				"(0[1-9]|[1-2][0-9]|3[0-1]) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) 20\\d\\d", 
				"Error - invalid date or date format. Please check date and enter the deadline in format 'dd MMM yyyy'"
				+ "(month is capitalized):");
			selectedProject.setDeadLine(newDeadLine);
			System.out.println("\n" + selectedProject);
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'), "
				+ "go back to the main menu ('b'), or exit ('e')?", connection);
//8.2) Allows user to update total amount paid.								
		} else if (choiceUp.equalsIgnoreCase("utp")) {
			String newTotalPaid = checkUserInput("\nPlease enter a new total amount paid: R", "([1-9]\\d+(\\.\\d\\d)?|[0](\\.[0]{2})?)", 
				"Error. Please enter digits only - if more than zero, amount cannot start with zero; only two decimals allowed:");
			selectedProject.setTotalPaid(newTotalPaid);
			System.out.println("\n" + selectedProject);
			backToMenus2("\nUpdate another project detail or finalize project('y'), select another project ('o'), "
				+ "go back to the main menu ('b'), or exit ('e')?", connection);
//8.3) Allows user to update architect's details.								
		} else if (choiceUp.equalsIgnoreCase("ua")) {	
			Person selectedPerson = Person.selectPerson(selectedProject.getArchitect(), personsNewAndEdited);	
			selectedPerson.updatePerson(getNewPersonInfo());             
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'), "
				+ "go back to the main menu ('b'), or exit ('e')?", connection);	
//8.4) Allows user to update architect's details.																
		} else if (choiceUp.equalsIgnoreCase("uc")) {
			Person selectedPerson = Person.selectPerson(selectedProject.getContractor(), personsNewAndEdited);
			selectedPerson.updatePerson(getNewPersonInfo());             
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'), "
				+ "go back to the main menu ('b'), or exit ('e')?", connection);	
//8.5) Allows user to update architect's details.							
		} else if (choiceUp.equalsIgnoreCase("ucu")) {
			Person selectedPerson = Person.selectPerson(selectedProject.getCustomer(), personsNewAndEdited);
			selectedPerson.updatePerson(getNewPersonInfo());             
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'), "
				+ "go back to the main menu ('b'), or exit ('e')?", connection);
		} else if (choiceUp.equalsIgnoreCase("use")) {
			Person selectedPerson = Person.selectPerson(selectedProject.getStructuralEng(), personsNewAndEdited);
			selectedPerson.updatePerson(getNewPersonInfo());             
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'), "
				+ "go back to the main menu ('b'), or exit ('e')?", connection);
		} else if (choiceUp.equalsIgnoreCase("upm")) {
			Person selectedPerson = Person.selectPerson(selectedProject.getProjectMan(), personsNewAndEdited);
			selectedPerson.updatePerson(getNewPersonInfo());             
			backToMenus2("\nUpdate another project detail or finalize project ('y'), select another project ('o'), "
				+ "go back to the main menu ('b'), or exit ('e')?", connection);
//8.6) Allows user to finalize project, generates invoice, and writes project details to file.							
		} else if (choiceUp.equalsIgnoreCase("fp")) {
			String completionDate = checkUserInput("\nPlease enter the completion date ('dd MMM yyyy'):", 
				"(0[1-9]|[1-2][0-9]|3[0-1]) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) 20\\d\\d", 
				"Error. Please check date and enter the deadLine in format 'dd MMM yyyy':");
			selectedProject.setDeadLine(selectedProject.getDeadLine() + "\nFinalized:\t\t\t" + completionDate);			
			selectedProject.generateInvoice(personsNewAndEdited);
			submenu = false;
			selectProject = backToMenus1("\nSelect another project ('y'), go back to the main menu ('b'), or exit ('e')?", selectProject, connection);
//8.7) Goes back to select another project.								
		} else if (choiceUp.equalsIgnoreCase("o")) {
			submenu = false;
//8.8) Goes back to the main menu.								
		} else if (choiceUp.equalsIgnoreCase("b")) {
			submenu = false;
			selectProject = false;	
//8.9) Writes LinkedHashMaps to text files, and exits program.						
		} else if (choiceUp.equalsIgnoreCase("e")) {                        
			closeApp(connection);										
		}
	};

	
//9.) closeApp() 
/**
 * Writes new or updated info to DB via <code>{@link #endSequenceMapsToTables(Connection) endSequenceMapsToTables}</code>,
 * closes connection, and exits program.
 * 
 * @param connection an SQL database <code>Connection</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V2.1
 */
	public static void closeApp(Connection connection) throws SQLException {
		endSequenceMapsToTables (connection);
		INPUT.close();
		connection.close();
//Terminates JVM (Geeksforgeeks.com, 2016).		
		System.exit(0);
	};
	
	
//10.) endSequenceMapsToTables() 
/**
 * Writes updated and new <code>{@link Project}</code> and <code>{@link Person}</code> attributes in operational <code>Map</code>
 * to corresponding DB table via 
 * <code>{@link #writeToTable(Connection, List<List<String>>, List<List<String>>, List<String>, String) writeToTable}</code>.
 * <p>
 * To decompose <code>Projects</code> and <code>Persons</code> stored in the <code>Maps</code>, 
 * this method uses <code>{@link Project#mapToList(Map<String, Project>) Project.mapToList}</code> and 
 * <code>{@link Person#mapToList(Map<String, Person>) Person.mapToList}</code>.
 * This method runs upon exiting program.
 * 
 * @param connection an SQL database <code>Connection</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V3.0
 */
	public static void endSequenceMapsToTables (Connection connection) throws SQLException {
//Decomposes reference and operational Project-Maps into Lists of String Lists.	
		List<List<String>> projectsExistingList = Project.mapToList(projectsExisting);
		List<List<String>> projectsNewAndEditedList = Project.mapToList (projectsNewAndEdited);

//Writes updated and new Project info to Project table.
		System.out.print("\n");
		writeToTable(connection, projectsExistingList, projectsNewAndEditedList, Arrays.asList("ProjectNum", "ProjectName", "BuildType", 
			"PhysAddress", "ErfNum", "TotalFee", "TotalPaid", "DeadLine", "Architect", "Contractor", "Customer", "StructuralEng", "ProjectMan"), "Project");
	
//Decomposes reference and operational Person-Maps into Lists of String Lists
		List<List<List<String>>> personsExistingList = Person.mapToList(personsExisting);
		List<List<List<String>>> personsNewAndEditedList = Person.mapToList(personsNewAndEdited);
		
		String[] personTypeArr = {"Architect", "Contractor", "Customer", "StructuralEng", "ProjectMan"};

//For each person type, writes updated and new Person info to respective table.
		for (int i = 0; i < 5; i++) {
			writeToTable(connection, personsExistingList.get(i), personsNewAndEditedList.get(i), Arrays.asList("Surname", "Name", 
					"CellNum", "EmailAddress", "PhysAddress", "Type"), personTypeArr[i]); 
			
		}
		System.out.println("\nBye!");
	};
	
	
//11.) getProjectAndPersonInfo() 
/**
 * Gets and validates new <code>{@link Project}</code> and <code>{@link Person}</code> attribute values via 
 * <code>{@link #checkUserInput(String, String, String) checkUserInput}</code>,
 * and returns a <code>List</code> of <code>Lists</code> containing the info as <code>Strings</code>. 
 * <p>
 * User input is validated against regular expressions.
 * 
 * @return a <code>List</code> of <code>Lists</code> containing new project and person info <code>Strings</code> 
 * @since V1
 */
	public static List<List<String>> getProjectAndPersonInfo() {
		System.out.println("\nPlease enter new project information:\n");
		
//Gets project info (for regular expressions, see Vogel, 2007).
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
//Gets architect info.		
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
//Gets contractor info.		
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
//Gets customer info.
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
//Gets structural engineer info.		
		String structuralEngSur = checkUserInput("Structural Engineer surname:", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure surname is capitalized and contains no non-letter characters except hyphens:");	
		String structuralEngNam = checkUserInput("Structural Engineer first name(s):", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure name is capitalized and contains no non-letter characters except hyphens:");
		String structuralEngCell = checkUserInput("Structural Engineer cellphone number:", "0\\d{9}", 
				"Error. Ivalid number. Please make sure number contains 10 digits and starts with 0:");
		String structuralEngMail = checkUserInput("Structural Engineer email address:", 
				"(?!.*([_!#$%&'*+/=?`{|}~^-])\\1)([a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+\\.)*[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+@"
				+ "((([a-zA-Z0-9]+[-][a-zA-Z0-9]+)+|[a-zA-Z0-9]+)\\.)*(?=.*[a-zA-Z])[a-zA-Z0-9]{2,}",   
				"Error. Invalid email address format."); 
		String structuralEngAddress = checkUserInput("Structural Engineer physical address ('Address line 1, Address line 2, Address line 3, ..., postal code'):", 
				"((\\d+ )?[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*, ){2,}(\\d{4})", 
				"Error. Please enter address in format 'Address line 1, Address line 2, Address line 3, ..., postal code' "
				+ "\n(min two address lines, address lines capitalized, 4 digit postal code):");
//Gets project manager info.		
		String projectManSur = checkUserInput("Project Manager surname:", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure surname is capitalized and contains no non-letter characters except hyphens:");	
		String projectManNam = checkUserInput("Project Manager first name(s):", "[A-Z][a-z]+((-| )[A-Z]?[a-z]+)*", 
				"Error. Please make sure name is capitalized and contains no non-letter characters except hyphens:");
		String projectManCell = checkUserInput("Project Manager cellphone number:", "0\\d{9}", 
				"Error. Ivalid number. Please make sure number contains 10 digits and starts with 0:");
		String projectManMail = checkUserInput("Project Manager email address:", 
				"(?!.*([_!#$%&'*+/=?`{|}~^-])\\1)([a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+\\.)*[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+@"
				+ "((([a-zA-Z0-9]+[-][a-zA-Z0-9]+)+|[a-zA-Z0-9]+)\\.)*(?=.*[a-zA-Z])[a-zA-Z0-9]{2,}",   
				"Error. Invalid email address format."); 
		String projectManAddress = checkUserInput("Project Manager physical address ('Address line 1, Address line 2, Address line 3, ..., postal code'):", 
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
		String[][] projectInfoArr = {{projectNum, projectName, buildType, physAddress, erfNum, totalFee, totalPaid, deadLine, 
			architectSur + ", " + architectNam, contractorSur + ", " + contractorNam, customerSur + ", " + customerNam, 
			structuralEngSur + ", " + structuralEngNam, projectManSur + ", " + projectManNam},
			{architectSur, architectNam, architectCell, architectMail, architectAddress, "Architect"},
			{contractorSur, contractorNam, contractorCell, contractorMail, contractorAddress, "Contractor"},
			{customerSur, customerNam, customerCell, customerMail, customerAddress, "Customer"},
			{structuralEngSur, structuralEngNam, structuralEngCell, structuralEngMail, structuralEngAddress, "Structural Engineer"},
			{projectManSur, projectManNam, projectManCell, projectManMail, projectManAddress, "Project Manager"}};
		
		List<List<String>> projectAndPersonInfo = new ArrayList<>();
		for (String[] i : projectInfoArr) {
			projectAndPersonInfo.add(Arrays.asList(i));
		}
	    return projectAndPersonInfo;
	};
	
	
//12.) getNewPersonInfo()
/**
 * Gets and validates new <code>{@link Person}</code> attribute values (for updates) via 
 * <code>{@link #checkUserInput(String, String, String) checkUserInput}</code>,
 * and returns a <code>List</code> containing the new info <code>Strings</code>. 
 *  
 * @return a <code>List</code> containing new <code>Person</code> attribute <code>Strings</code>
 * @since V2.1
 */
	public static List<String> getNewPersonInfo() {
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
		
//Arrays.asList() inserts multiple values into List upon declaration (Maroun, 2011).	
		List<String> personInfo = new ArrayList<>(Arrays.asList(newCellNum, newEmailAddress, newPhysAddress));     
		return personInfo;                   
	};
	
	
//13.) checkUserInput()
/**
 * Validates input against a regular expression, displays an error message while input doesn't match, and returns the 
 * input <code>String</code> once it matches.
 * 	
 * @param prompt a <code>String</code> prompting user input
 * @param regex the regular expression that user input is matched against
 * @param errorPrompt the error message <code>String</code> displayed if user input does not match regex
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
	
	
//14.) backToMenus1()	
/**
 * Controls back-navigation after completion of an operation.
 * <p>
 * This method allows for three back-navigation options: go back to a submenu or select another project,
 * go back to the main menu, or exit, via changing and returning a static control <code>boolean</code> (passed as parameter).
 * <p>
 * Refactored from V1 backtoMenus method.
 * 	
 * @param optionString a <code>String</code> displaying the navigation options
 * @param staticBoolean the relevant static control <code>boolean</code>
 * @param connection an SQL database <code>Connection</code>
 * @return the relevant static control <code>boolean</code> - changed according to user navigation choice
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V2.0
 */
	public static boolean backToMenus1(String optionString, boolean staticBoolean, Connection connection) throws SQLException {					                                                         
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
				closeApp(connection);  
			}
		}
	};
	
	
//15.) backToMenus2()	
/**
 * Controls back-navigation after completion of an operation.
 * <p>
 * This method allows for 4 navigation options: go back to a submenu, select another project,
 * go back to the main menu, or exit, via directly changing static control <code>booleans</code>.
 * <p>
 * Refactored from V1 backtoMenus method.
 * 	
 * @param optionString a <code>String</code> displaying the navigation options
 * @param connection an SQL database <code>Connection</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V2.0
 */
	public static void backToMenus2(String optionString, Connection connection) throws SQLException {			
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
				closeApp(connection); 
			}					
		}		
	};
	
	
//16.) readTable()	
/**
 * Reads all data from a DB table and returns a <code>List</code> of <code>Lists</code>
 * containing specified column value <code>Strings</code> for each row. 
 * 
 * @param connection an SQL database <code>Connection</code>
 * @param columnList a <code>List</code> containing table column name <code>Strings</code>
 * @param tableName a <code>String</code> containing the table name
 * @return a <code>List</code> of <code>Lists</code> containing each table row as a separate <code>String</code> <code>List</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V3.0
 */
	public static List<List<String>> readTable(Connection connection, List<String> columnList, String tableName) throws SQLException {         
		List<List<String>> allProjectsorPersonsList = new ArrayList<>();
//Creates direct line to database for running queries (Hyperiondev, 2021*, p.23).	
		Statement statement = connection.createStatement();
//Selects all data from table (Hyperiondev, 2021, p.9).		
		ResultSet results = statement.executeQuery("SELECT * FROM " + tableName);

//Loops over results (Hyperiondev, 2021*, p.23). 
		while (results.next()) {
			List<String> projectOrPersonList = new ArrayList<>();
//For column in columList, adds value to row List (Hyperiondev, 2021*, p.25).
			for (String i : columnList) {
				projectOrPersonList.add(results.getString(i));
			}
//Adds row List to List of Lists.
			allProjectsorPersonsList.add(projectOrPersonList);
		}	
		statement.close();
		return allProjectsorPersonsList;		
	};

		
//17.) writeToTable()	
/**
 * Writes new and updated <code>{@link Project}</code> or <code>{@link Person}</code> information to a DB table, and displays
 * amount of updated and inserted cells/rows in the table.
 * <p>
 * This method compares attribute info for each <code>Project</code> or <code>Person</code> in the reference and operational <code>Maps</code>.
 * If attribute values differ, new values are written to their corresponding DB rows/columns.
 * If new <code>Projects</code> or <code>Persons</code> were added to operational <code>Map</code>, these are inserted as new DB rows.
 * 
 * @param connection an SQL database <code>Connection</code>
 * @param listExist a <code>List</code> of <code>Lists</code> containing <code>Project</code> and 
 * <code>Person</code> info <code>Strings</code>from reference <code>Map</code> 
 * @param listNew a <code>List</code> of <code>Lists</code> containing <code>Project</code> and 
 * <code>Person</code> info from operational <code>Map</code>  
 * @param columnList a <code>List</code> containing column name <code>Strings</code>
 * @param tableName a <code>List</code> containing the table name <code>Strings</code>
 * @throws SQLException provides information on a database access errors or other SQL related errors
 * @since V3.0
 */
	public static void writeToTable(Connection connection, List<List<String>> listExist, List<List<String>> listNew, 
			List<String> columnList, String tableName) throws SQLException {   	
		Statement statement = connection.createStatement();	
		int rows = 0;
		int cellsUpdated = 0;
		
//Checks if Lists (i.e. Projects or Persons) in reference-Map List differ from corresponding Lists in operational-Map List. 		
		for (int i = 0; i < listExist.size(); i++) {
			if (listExist.get(i).equals(listNew.get(i)) == false) {
				rows += 1;
				
//If Lists differ, finds differing attributes and writes new values to their corresponding columns/rows (Hyperiondev, 2021*, p.24).  				
				for (int ii = 2; ii < listExist.get(i).size(); ii++) {
					if (listExist.get(i).get(ii).equals(listNew.get(i).get(ii)) == false) {
						cellsUpdated += statement.executeUpdate("UPDATE " + tableName + " SET " + columnList.get(ii) + " = " + "'" + listNew.get(i).get(ii) + "'"
								+ " WHERE " + columnList.get(0) + " = " + "'" + listNew.get(i).get(0) + "'");  
					}
				}
			}	 
		}
//Displays amount of cells/rows updated.	
		System.out.println("Query complete, " + cellsUpdated + " cell/s updated across " + rows + " row/s in " + tableName  + ".");
		
//Inserts row for each new Project or Person in the operational-Map List (Hyperiondev, 2021, p.8).
		int rowsInserted = 0;		
		for (int i = listExist.size(); i < listNew.size(); i++)	{
			String sqlValues = "'" + listNew.get(i).get(0) + "'";
			
			for (int ii = 1; ii < listNew.get(i).size(); ii++) {
				sqlValues += ", '" + listNew.get(i).get(ii) + "'";
			}	
			rowsInserted += statement.executeUpdate("INSERT INTO " + tableName + " VALUES (" + sqlValues + ")");
		}
//Displays amount of rows inserted.
		System.out.println("Query complete, " + rowsInserted + " row/s added in " + tableName + ".");	
	};
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
//Hyperiondev (2021). Introduction to SQL. p.6-11.
//Hyperiondev (2021)*. Java Database Programming: The JDBC. p.23-25.
//iamvineettiwari012 (2012). HashMap replace(key, value) method in Java with Examples. [online] Geeksforgeeks.com. Available at: https://www.geeksforgeeks.org/hashmap-replacekey-value-method-in-java-with-examples/ [Accessed 16 July 2021].
//JAVA (2013). Global variables in Java. [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/4646577/global-variables-in-java [Accessed 22 July 2021].
//Javatpoint.com (2021). Java String format(). [online] Available at: https://www.javatpoint.com/java-string-format [Accessed 22 July 2021].
//Jesper (2012). Java object name from user input as method call. [online]  Stackoverflow.com. Available at: https://stackoverflow.com/questions/13743637/java-object-name-from-user-input-as-method-call [Accessed 22 July 2021].
//Landup, D. (2021). Java: Check if String Contains a Substring. [online] Stackabuse.com. Available at: https://stackabuse.com/java-check-if-string-contains-a-substring [Accessed 22 July 2021].
//Leon (2016). What does System.out.printf( “%-15s%03d\n”, s1, x) do? [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/37780027/what-does-system-out-printf-15s03d-n-s1-x-do [Accessed 22 July 2021].
//Ligios (2019). A Practical Guide to DecimalFormat. [online] Baeldung.com. Available at: https://www.baeldung.com/java-decimalformat [Accessed 22 July 2021].
//Maroun (2011). How to declare an ArrayList with values? [duplicate]. [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/21696784/how-to-declare-an-arraylist-with-values [Accessed 31 July 2021].
//Oracle.com (2021). 9 - Naming Conventions. [online] Available at: https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html [Accessed 20 May 2021].
//Oracle.com (2021)*. How to Write Doc Comments for the Javadoc Tool. [online] Available at: https://www.oracle.com/za/technical-resources/articles/java/javadoc-tool.html#exampleresult [Accessed 22 July 2021].
//Paul, J. (2021). A Simple Example to Check if File is Empty in Java. [online] Java67.com. Available at: https://www.java67.com/2018/03/a-simple-example-to-check-if-file-is-empty-in-java.html [Accessed 06 June 2021].
//Ram, V. (2018). Final static variables in Java. [online] Tutorialspoint.com. Available at: https://www.tutorialspoint.com/Final-static-variables-in-Java [Accessed 22 July 2021].
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