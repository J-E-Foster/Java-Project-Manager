package projectmanager;

import java.io.FileWriter;
import java.io.IOException;
//For Serializable interface (makes objects of this class writable to file 
//as intact byte stream objects - Datsabk, 2016).
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; 
import java.util.*;


/**
 * This class defines a <code>Project</code> object.
 * <p>
 * It is used by <code>{@link ProjectManager}</code>, and implements <code>Serializable</code>.
 * <p>
 * Changes from V1: code refactored, attributes encapsulated, instances now writable to file, 
 * and <code>updateDeadLine</code>, <code>updateTotalPaid</code>, and <code>finalizeProject</code>
 * methods moved to <code>ProjectManager</code>.
 * <p>
 * * Changes from V2.0: Methods <code>{@link #selectProject(Map<String, Project>, String) selectProject}</code>,
 * <code>{@link #generateInvoice(Map<String, Person>) generateInvoice}</code>,
 * <code>{@link #writeFinalized() writeFinalized}</code>,
 * <code>{@link #viewAll(Map<String, Project>) viewAll}</code>,
 * <code>{@link #viewIncomplete(Map<String, Project>) viewIncomplete}</code>, and
 * <code>{@link #viewPastDue(Map<String, Project>) viewPastDue}</code> moved here from <code>{@link ProjectManager}</code>.
 * Method <code>{@link #populateProjectsMap(String[], Map <String, Project>) populateProjectsMap}</code> added.
 * 
 * @author J.E. Foster © 
 * @version V2.1 August 2021
 */
public class Project implements Serializable {          

//ATTRIBUTES
//1.) Universal version identifier for serializable class (Jha, 2013 & Skeet, 2008).
//Used in deserialization to match class to serialized object.
	private static final long serialVersionUID = 1L;           
	
//2.1 - 2.11) Project details.
	private String projectNum;
	private String projectName;
	private String buildType;
	private String physAddress;
	private String erfNum;
	private String totalFee;
	private String totalPaid;
	private String deadLine;
	private String architect;
	private String contractor;
	private String customer;
		
//CONSTRUCTOR 
/**
 * Creates a <code>Project</code>.
 * 
 * @param projectNum a <code>String</code> containing the project number
 * @param projectName a <code>String</code> containing the project name
 * @param buildType a <code>String</code> containing the type of building
 * @param physAddress a <code>String</code> containing the building's physical address
 * @param erfNum a <code>String</code> containing the ERF number
 * @param totalFee a <code>String</code> containing the total amount owed
 * @param totalPaid a <code>String</code> containing the total amount paid to date
 * @param deadLine a <code>String</code> containing the project deadline
 * @param architect a <code>String</code> containing the architect's name and surname
 * @param contractor a <code>String</code> containing the contractor's name and surname
 * @param customer a <code>String</code> containing the customer's name and surname
 */
	public Project(String projectNum, String projectName, String buildType, String physAddress, String erfNum,
			String totalFee, String totalPaid, String deadLine, String architect, String contractor, String customer) {		
		this.setProjectNum(projectNum);
		this.setProjectName(projectName);
		this.setBuildType(buildType);
		this.setPhysAddress(physAddress);
		this.setErfNum(erfNum);
		this.setTotalFee(totalFee);
		this.setTotalPaid(totalPaid);
		this.setDeadLine(deadLine);
		this.setArchitect(architect);
		this.setContractor(contractor);
		this.setCustomer(customer);
	};

//METHODS																										
//1.1 - 1.22) Setters and getters.
	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	};
	public String getProjectNum() {
		return projectNum;
	};
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	};
	public String getProjectName() {
		return projectName;
	};
	public void setBuildType(String buildType) {
		this.buildType = buildType;
	};
	public String getBuildType() {
		return buildType;
	};
	public void setPhysAddress(String physAddress) {
		this.physAddress = physAddress;
	};
	public String getPhysAddress() {
		return physAddress;
	};
	public void setErfNum(String erfNum) {
		this.erfNum = erfNum;
	};
	public String getErfNum() {
		return erfNum;
	};
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	};
	public String getTotalFee() {
		return totalFee;
	};	
	public void setTotalPaid(String totalPaid) {
		this.totalPaid = totalPaid;
	};
	public String getTotalPaid() {
		return totalPaid;
	};
	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	};
	public String getDeadLine() {
		return deadLine;
	};
	public void setArchitect(String architect) {
		this.architect = architect;
	};
	public String getArchitect() {
		return architect;
	};
	public void setContractor(String contractor) {
		this.contractor = contractor;
	};
	public String getContractor() {
		return contractor;
	};
	public void setCustomer(String customer) {
		this.customer = customer;
	};
	public String getCustomer() {
		return customer;
	};	

	
//2.) toSTring() override (Geeksforgeeks.com, 2018).
/**
 * Returns a labeled list of all <code>Project</code> details.
 * 
 * @return a <code>String</code> containing all <code>Project</code> attributes
 * @since V1
 */
	public String toString() {
		String output = "Project Number:\t\t\t" + getProjectNum();
		output += "\nProject Name:\t\t\t" + getProjectName();
		output += "\nBuilding Type:\t\t\t" + getBuildType();
		output += "\nPhysical Address:\t\t" + getPhysAddress();
		output += "\nERF Number:\t\t\t" + getErfNum();
		output += "\nTotal Fee:\t\t\tR " + getTotalFee();
		output += "\nTotal Amount Paid to Date:\tR " + getTotalPaid();
		output += "\nDeadline:\t\t\t" + getDeadLine();
		output += "\nArchitect:\t\t\t" + getArchitect();
		output += "\nContractor:\t\t\t" + getContractor();
		output += "\nCustomer:\t\t\t" + getCustomer();
		
		return output;
	};

	
//3.) populateProjectsMap() 		 		
/**
* Creates new <code>Project</code> and adds it to a <code>Map</code>..
* 	
* @param projectinfo an <code>Array</code> containing project info <code>Strings</code>
* @param map a <code>Map</code> with <code>String</code> keys and <code>Project</code> values
* @since V2.1
*/  		 		
	public static void populateProjectsMap (String[] projectinfo, Map <String, Project> map) {	
//Adds new Project info (from array) to Map; key = project number and project name (Jesper, 2012).		
		map.put(projectinfo[0] + " " + projectinfo[1], new Project(projectinfo[0], projectinfo[1], 
				projectinfo[2], projectinfo[3], projectinfo[4], projectinfo[5], projectinfo[6], 
				projectinfo[7], projectinfo[8], projectinfo[9], projectinfo[10]));		
		System.out.println("\n" + map.get(projectinfo[0] + " " + projectinfo[1]));
	}
	
	
//4.) selectProject()
/**
* Iterates through a <code>hMap</code> containing <code>Projects</code>, selects 
* a <code>Project</code> according to user input, and then displays and returns the chosen <code>Project</code>. 
* 
* @param map a <code>Map</code> with <code>String</code> keys and <code>Project</code> values 	
* @param userInput a <code>String</code> containing <code>Project</code> name or number.
* @return the <code>Project</code> object selected by the user (null if no project found)
* @since V1
*/
	public static Project selectProject(Map <String, Project> map, String userInput)  {
		Project selectedProject = null; 				
								
//Iterates through HashMap keys (W3Schools.com, 2021**).  .			
		for (String i : map.keySet()) {			
//Selects Project if key "i" contains project name or number.			
			if (i.contains(userInput)) {               
				selectedProject = map.get(i);           
				System.out.println("\n" + selectedProject);   	
			}
		}
		return selectedProject;	
	};
	
		

//5.) generateInvoice()
/**
 * Generates and displays a customer invoice if amount is outstanding.
 * <p>
 * This method calculates the outstanding amount via the <code>totalFee</code> and <code>totalPaid</code> attributes 
 * of a selected <code>Project</code>, and displays an invoice containing all amounts and the customer's personal info 
 * (accessed via a <code>hMap</code> containing <code>{@link Person}</code> objects.
 * 
 * @param map a <code>Map</code> with <code>String</code> keys and <code>Project</code> values
 * @since V2.0 (originally part of V1 <code>finalizeProject</code>)
 */
	public void generateInvoice(Map <String, Person> map)	{	
		
//Gets total fee and total amount paid from selected Project, casts to double (WhiteFang34, 2011), and calculates amount due.
		double totalFee = Double.parseDouble(getTotalFee()); 
		double totalPaid = Double.parseDouble(getTotalPaid());
		double totalOutstanding = totalFee - totalPaid;
			
//Decimal format set to two decimals (Ligios, 2019).						
		DecimalFormat df = new DecimalFormat("0.00"); 
//Rounds up if the discarded fraction is > 0.5 (Bright, 2012).
		df.setRoundingMode(RoundingMode.HALF_UP);
//Formats doubles to two decimals.		
		String totalFeeStr = df.format(totalFee);
		String totalPaidStr = df.format(totalPaid);
		String totalOutstandingStr = df.format(totalOutstanding);
			
//Formats doubles to 20 character strings (Javatpoint.com, 2021) to achieve right-alignment (Leon, 2016 & Howard, 2011).
		String totalFeeStr20 = String.format("%20s", totalFeeStr);
		String totalPaidStr20 = String.format("%20s", totalPaidStr);
		String totalOutstandingStr20 = String.format("%20s", ("R " + totalOutstandingStr));
	
//Checks if project customer is present in LinkedHashMap and if amount is due.		
		boolean outstanding = false;
		for (String i : map.keySet()) {	
			if (i.contains(getCustomer()) && totalOutstanding > 0.0) {	    
				outstanding = true;
				Person selectedPerson = map.get(i);	
//Displays invoice if amount is due.					
				System.out.println("\n\nPOISED\t\t\t\t\t\t\tINVOICE# " + getProjectNum() + "\n"     
						+ "\n\nBill to"                                                            
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
		if (outstanding == false) {
			System.out.println("\n\nNo amount due.");
		}
		System.out.println("\n" + this);
	};	

	
//6.) writeFinalized()	
/**
 * Writes (appends) finalized <code>Project</code> details to a txt file.
 * <p>
 * This method catches <code>IOException</code>.
 * 	
 * @since V2.0
 */
	public void writeFinalized() {	
//Creates and writes to project details to file if file doesn't exist; appends if file exists (Geeksforgeeks.com, 2019).		
		try {
			FileWriter fw = new FileWriter("Completed project.txt", true); 
			fw.write("\r\n" + this + "\r\n\r\n");
			fw.close();
//Thrown by FileWriter constructor and write() (Docs.oracle.com, 2020**).
		} catch (IOException io) {
			System.out.println("Error. Completed project.txt cannot be found or opened, or cannot be created.");
			io.printStackTrace();
		}
	};			
	
	
//7.) viewAll()	
/**
* Iterates through a <code>Map</code> containing <code>Project</code> objects,
* and displays all <code>Projects</code>.
* 
* @param map a <code>Map</code> with <code>String</code> keys and <code>Project</code> values
* @since V2.0
*/
	public static void viewAll(Map <String, Project> map) {
		System.out.println("\nAll projects:");
//Iterates through HashMap values (harto, 2009).	
		for (Project project : map.values()) {  
			System.out.print("\n" + project + "\n");
		}
	};

	
//8) viewIncomplete()	
/**
* Iterates through a <code>Map</code> containing <code>Project</code> objects,
* and displays incomplete <code>Projects</code>.
* 
* @param map a <code>Map</code> with <code>String</code> keys and <code>Project</code> values
* @since V2.0
*/
	public static void viewIncomplete(Map <String, Project> map) {
		int counter = 0;	
		System.out.println("\nIncomplete projects:");
		for (Project project : map.values()) { 
//Displays Project if not finalized.
			if (project.getDeadLine().contains("Finalized") == false) {
				System.out.print("\n" + project + "\n");
				counter += 1;
			}		
		}
		if (counter == 0) {
			System.out.println("\nAll projects have been completed.");
		}
	};

	
//9.) viewPastDue()	
/**
* Iterates through a <code>Map</code> containing <code>Project</code> objects,
* and displays the <code>Projects</code> that are past their due date.
* 
* @param map a <code>Map</code> with <code>String</code> keys and <code>Project</code> values
* @since V2.0
*/
	public static void viewPastDue(Map <String, Project> map) {
//Gets current date (Docs.oracle.com, 2021).
		LocalDate currentDate = LocalDate.now();
//Formats a LocalDate object to/from the string pattern (Docs.oracle.com, 2021)*.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy"); 
		int counter = 0;
			
		System.out.println("\nProjects past due date:");
		for (Project project : map.values()) {  
			String deadLine = project.getDeadLine(); 
//Parses deadline String to LocalDate object if project not finalized.
			if (deadLine.contains("Finalized") ==  false) {				
				LocalDate parsedDate = LocalDate.parse(deadLine, formatter);	
//Compares current date to deadline	(Docs.oracle.com, 2021).			
				if (currentDate.compareTo(parsedDate) > 0) {  
					System.out.print("\n" + project + "\n");
					counter += 1;
				}
			}
		}
		if (counter == 0) {
			System.out.println("\nNo projects are past their due date.");
		}			
			
	};
				
}

/////////////////////////////////////////////////////////////////