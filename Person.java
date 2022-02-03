package projectmanager;

import java.util.*;
  
//??!!!! se if javadoc works for method links!
/**
 * This class defines a <code>Person</code>, and contains methods to manipulate <code>Persons</code> stored in a <code>Map</code>.
 * <p>
 * It is used by <code>{@link ProjectManager}</code>.
 * <p>
 * Changes from V2.0: Methods <code>{@link #selectPerson(String, Map<String, Person>) selectPerson}</code> and 
 * <code>{@link #updatePerson(List<String>) updatePerson}</code> moved here from <code>ProjectManager</code>.
 * Method <code>{@link #populatePersonsMap(List<String>, Map <String, Person>) populatePersonsMap}</code> added.
 * <p>
 * Changes from V2.1: Method <code>{@link #mapToList(Map<String, Person>) mapToList}</code> added.
 * Class no longer implements Serializable.
 * 
 * @author J.E. Foster © 
 * @version V3.0 August 2021
 */
public class Person {                     

//ATTRIBUTES
	
//1.1 - 1.6) Personal details.
	private String type;
	private String surname;
	private String name;     
	private String cellNum;
	private String emailAddress;
	private String physAddress;
		
//CONSTRUCTOR 
/**
 * Creates a <code>Person</code>.
 * 
 * @param type a <code>String</code> containing person type (either "Architect", "Contractor", "Customer", "Structural Engineer", or "Project Manager")
 * @param surname a <code>String</code> containing the person's surname
 * @param name a <code>String</code> containing the person's first name
 * @param cellNum a <code>String</code> containing the person's cell number
 * @param emailAddress a <code>String</code> containing the person's email address
 * @param physAddress a <code>String</code> containing the person's residential address
 */
	public Person (String surname, String name, String cellNum, String emailAddress, String physAddress, String type) {
		this.setSurname(surname);                           
		this.setName(name);
		this.setCellNum(cellNum);
		this.setEmailAddress(emailAddress);
		this.setPhysAddress(physAddress);
		this.setType(type);
	};
	
//METHODS
//1.1 - 1.12) Setters and getters.	                                                 
	public void setSurname(String surname) {
		this.surname = surname;
	};
	public String getSurname() {
		return surname;
	};
	public void setName(String name) {
		this.name = name;
	};
	public String getName() {
		return name;
	};
	public void setCellNum(String cellNum) {
		this.cellNum = cellNum;
	};
	public String getCellNum() {
		return cellNum;
	};
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	};
	public String getEmailAddress() {
		return emailAddress;
	};
	public void setPhysAddress(String physAddress) {
		this.physAddress = physAddress;
	};
	public String getPhysAddress() {
		return physAddress;
	};
	public void setType(String type) {
		this.type = type;
	};
	public String getType() {
		return type;
	};
	
	
//2.) toString() override (Geeksforgeeks.com, 2018).
/**
 * Returns a labeled list of all <code>Person</code> attributes.
 * 
 * @return a <code>String</code> containing all <code>Person</code> attributes
 * @since V1
 */
	public String toString() {                                                                        
		String output = "Type:\t\t\t\t" + getType(); 
		output += "\nSurname:\t\t\t" + getSurname();
		output += "\nName:\t\t\t\t" + getName();
		output += "\nCell Number:\t\t\t" + getCellNum();
		output += "\nEmail Address:\t\t\t" + getEmailAddress();
		output += "\nPhysical Address:\t\t" + getPhysAddress();
		
		return output;
	};

	
//3.) toString2(). 	
/**
 * Returns all <code>Person</code> contact-detail attributes in an invoice-ready format.
 * 
 * @return a <code>String</code> containing <code>Person</code> contact-detail attributes
 * @since V1
 */
	public String toString2() {			
//Splits address at comma-spaces & stores elements in array (Geeksforgeeks.com, 2018*).                
		String[] physAddressSplit = getPhysAddress().split(", ");
		
		String output = getName() + " " + getSurname();
		output += "\n" + getCellNum();
		output += "\n" + getEmailAddress();
		output += "\n";		
//Displays address line-for-line (W3schools.com, 2021).
		for (String i : physAddressSplit) {
			output += "\n" + i;                                                                         
		}	
		return output;
	};

	
//4.) populatePersonsMap()
/**
 * Creates new <code>Person</code> and adds it to a <code>Map</code>.
 * 
 * @param personinfo a <code>List</code> containing person info <code>Strings</code>.
 * @param map a <code>Map</code> with <code>String</code> keys and <code>Person</code> values
 * @since V2.1
 */
	public static void populatePersonsMap(List<String> personinfo, Map <String, Person> map) {	
		map.put(personinfo.get(0) + ", " + personinfo.get(1), new Person(personinfo.get(0), personinfo.get(1), personinfo.get(2), 
			personinfo.get(3), personinfo.get(4), personinfo.get(5)));             		
	};	
	

//5) mapToList()
/**
 * Iterates through a <code>Map</code> containing <code>Persons</code>, adds each <code>Person</code> - as <code>List</code> of <code>String</code> attributes - 
 * to a corresponding <code>List</code> of <code>Lists</code> (representing type), then adds all these <code>Lists</code> to a
 * <code>List</code> of <code>Lists</code> of <code>Lists</code> (representing all types), which is returned.
 *
 * @param map a <code>Map</code> with <code>String</code> keys and <code>Person</code> values
 * @return a <code>List</code> of <code>Lists</code> of <code>Lists</code> containing Person attribute <code>Strings</code>.
 * @since V3.0
 */
	public static List<List<List<String>>> mapToList(Map<String, Person> map) {		
		List<List<List<String>>> allPersonsList = new ArrayList <List<List<String>>>();
//Creates List of Lists for each Person type.
		List<List<String>> architectList = new ArrayList <List<String>>();
		List<List<String>> contractorList = new ArrayList <List<String>>();
		List<List<String>> customerList = new ArrayList <List<String>>();
		List<List<String>> structuralEngList = new ArrayList <List<String>>();
		List<List<String>> projectManList = new ArrayList <List<String>>();

//For each Person in Map, adds attributes (as String List) to corresponding List of Lists.
		for (Person i : map.values()) {
			if (i.getType().equals("Architect")) {
				architectList.add(Arrays.asList(i.getSurname(), i.getName(), i.getCellNum(),  i.getEmailAddress(), i.getPhysAddress(), i.getType()));
			} else if (i.getType().equals("Contractor")) {
				contractorList.add(Arrays.asList(i.getSurname(), i.getName(), i.getCellNum(),  i.getEmailAddress(), i.getPhysAddress(), i.getType()));
			} else if (i.getType().equals("Customer")) {
				customerList.add(Arrays.asList(i.getSurname(), i.getName(), i.getCellNum(),  i.getEmailAddress(), i.getPhysAddress(), i.getType()));
			} else if (i.getType().equals("Structural Engineer")) {
				structuralEngList.add(Arrays.asList(i.getSurname(), i.getName(), i.getCellNum(),  i.getEmailAddress(), i.getPhysAddress(), i.getType()));
			} else {
				projectManList.add(Arrays.asList(i.getSurname(), i.getName(), i.getCellNum(),  i.getEmailAddress(), i.getPhysAddress(), i.getType()));
			}
		}
//Adds each Person List of Lists to List of Lists of Lists.
		allPersonsList.add(architectList);
		allPersonsList.add(contractorList); 
		allPersonsList.add(customerList);
		allPersonsList.add(structuralEngList);
		allPersonsList.add(projectManList);
		
		return allPersonsList;
	};
	
	
//6.) selectPerson() 
/**
* Iterates through a <code>Map</code> containing <code>Persons</code>, selects 
* a parameter-specified <code>Person</code>, then displays and returns the selected <code>Person</code>.
* 
* @param name a <code>String</code> containing the name and/or surname of a <code>Person</code>
* @param map a <code>Map</code> containing <code>Persons</code>.
* @return the selected <code>Person</code> object (null if no project found)
* @since V1
*/
	public static Person selectPerson(String name, Map <String, Person> map) {		
		Person selectedPerson = null;
		
		for (String i : map.keySet()) {		
			if (i.contains(name)) { 	
				selectedPerson = map.get(i);		       
				System.out.println("\n" + selectedPerson);       
			}																											
		}		
		return selectedPerson;
	};
	
	
//7.) updatePerson()
/**
* Updates the personal-detail attributes (<code>cellNum</code>, <code>emailAddress</code>, and <code>physAddress</code>) 
* of any <code>Person</code>.
* <p>
* This method validates user input via <code>{@link ProjectManager#checkUserInput(String, String, String) ProjectManager.checkUserInput}</code>.
* It allows the user to update cell number, email address, and physical address.
* If left blank, the attributes remain unchanged.
* The altered <code>Person</code> replaces the corresponding <code>Person</code> in the <code>Map</code>.
* The <code>Person</code> is then read from the <code>Map</code> and displayed.
* 
* @param personInfo a <code>List</code> containing new <code>Person</code> attribute <code>Strings</code>.
* @since V1
*/
	public void updatePerson(List<String> personInfo) {		
//Blank list ensures unchanged details aren't changed to blanks.                 		      ?
		List <String> updateChoiceBlank = new ArrayList <String>();
		updateChoiceBlank.addAll(Arrays.asList("\n", "\r", "\r\n", " ", ""));
					
//If blank list doesn't contain new detail (Striver, 2018), updates old detail.
		if (updateChoiceBlank.contains(personInfo.get(0)) == false) {
			setCellNum(personInfo.get(0));
		}
		if (updateChoiceBlank.contains(personInfo.get(1)) == false) {                       
			setEmailAddress(personInfo.get(1));
		}
		if (updateChoiceBlank.contains(personInfo.get(2)) == false) {
			setPhysAddress(personInfo.get(2));
		}
		System.out.println("\n" + this);
	};	
	
}

/////////////////////////////////////////////////////////////////