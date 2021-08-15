package projectmanager;

//For Serializable interface (makes objects of this class writable to file 
//as intact byte stream objects - Datsabk, 2016).
import java.io.Serializable;
import java.util.*;
  

/**
 * This class defines a <code>Person</code> object.
 * <p>
 * It is used by <code>{@link ProjectManager}</code>, and implements <code>Serializable</code> interface.
 * <p>
 * Changes from V1: code refactored, attributes encapsulated, instances now writable to file, 
 * and <code>updatePerson</code> method moved to <code>ProjectManager</code>.
 * <p>
 * Changes from V2.0: Methods <code>{@link #selectPerson(String, Map<String, Person>) selectPerson}</code> and 
 * <code>{@link #updatePerson(String[]) updatePerson}</code> moved here from <code>ProjectManager</code>.
 * Method <code>{@link #populatePersonsMap(String[][], Map <String, Person>) populatePersonsMap}</code> added.
 * 
 * @author J.E. Foster © 
 * @version V2.1 August 2021
 */
public class Person implements Serializable {                     

//ATTRIBUTES
//1.) Universal version identifier for serializable class (Jha, 2013 & Skeet, 2008).
//Used in deserialization to match class to serialized object.
	private static final long serialVersionUID = 1L;
	
//2.1 - 2.6) Personal details.
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
 * @param type a <code>String</code> containing "Architect", "Contractor", or "Customer"
 * @param surname a <code>String</code> containing the person's surname
 * @param name a <code>String</code> containing the person's first name
 * @param cellNum a <code>String</code> containing the person's cell number
 * @param emailAddress a <code>String</code> containing the person's email address
 * @param physAddress a <code>String</code> containing the person's residential address
 */
	public Person (String type, String surname, String name, String cellNum, String emailAddress, String physAddress) {
		this.setType(type);
		this.setSurname(surname);                           
		this.setName(name);
		this.setCellNum(cellNum);
		this.setEmailAddress(emailAddress);
		this.setPhysAddress(physAddress);
	};
	
//METHODS
//1.1 - 1.12) Setters and getters.	                                                 
	public void setType(String type) {
		this.type = type;
	};
	public String getType() {
		return type;
	};
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
	
	
//2.) toString() override (Geeksforgeeks.com, 2018).
/**
 * Returns a labeled list of all <code>Person</code> details.
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
 * Returns all <code>Person</code> contact-details in an invoice-ready format.
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
* @param personinfo a two dimensional <code>Array</code> containing person info <code>Strings</code>.
* @param map a <code>Map</code> with <code>String</code> keys and <code>Person</code> values
* @since V2.1
*/
	public static void populatePersonsMap (String[][] personinfo, Map <String, Person> map) {
//Adds new Project info (from array) to Map; key = project number and project name (Jesper, 2012).
	map.put(personinfo[1][2] + " " + personinfo[1][1], new Person(personinfo[1][0], personinfo[1][1], personinfo[1][2], 
			personinfo[1][3], personinfo[1][4], personinfo[1][5]));
	map.put(personinfo[2][2] + " " + personinfo[2][1], new Person(personinfo[2][0], personinfo[2][1], personinfo[2][2], 
			personinfo[2][3], personinfo[2][4], personinfo[2][5]));
	map.put(personinfo[3][2] + " " + personinfo[3][1], new Person(personinfo[3][0], personinfo[3][1], personinfo[3][2], 
			personinfo[3][3], personinfo[3][4], personinfo[3][5]));
				           
	System.out.println("\n" + map.get(personinfo[1][2] + " " + personinfo[1][1]) + "\n"
		+ "\n" + map.get(personinfo[2][2] + " " + personinfo[2][1]) + "\n"
		+ "\n" + map.get(personinfo[3][2] + " " + personinfo[3][1]));              		
	};	
	
	
//4.) selectPerson() 
/**
* Iterates through a <code>Map</code> containing <code>Person</code> objects, selects 
* a <code>Person</code> according to the parameter, and then displays and returns the selected <code>Person</code>.
* 
* @param name a <code>String</code> containing the name and/or surname of a <code>Person</code>
* @param map a <code>Map</code> with <code>String</code> keys and <code>Person</code> values
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
	

//5.) updatePerson()
/**
* Updates <code>Person</code> contact-detail attributes (<code>cellNum</code>, <code>emailAddress</code>, and <code>physAddress</code>).
* <p>
* Takes an <code>Array</code> containing the details as parameter. If details were left blank, the attributes remain unchanged.
* <p>
* Changes from V1: moved from <code>Person</code> class, user input validation added.
* 
* @param personInfo a <code>String</code> <code>Array</code> containing new attribute values.
* @since V1
*/
	public void updatePerson(String[] personInfo) {  
			
//Blank list ensures unchanged details aren't changed to blanks.                 		      ?
		List <String> updateChoiceBlank = new ArrayList <String>();
		updateChoiceBlank.addAll(Arrays.asList("\n", "\r", "\r\n", " ", ""));
					
//If blank list doesn't contain new detail (Striver, 2018), updates old detail.
		if (updateChoiceBlank.contains(personInfo[0]) == false) {
			setCellNum(personInfo[0]);
		}
		if (updateChoiceBlank.contains(personInfo[1]) == false) {                       
			setEmailAddress(personInfo[1]);
		}
		if (updateChoiceBlank.contains(personInfo[2]) == false) {
			setPhysAddress(personInfo[2]);
		}
		System.out.println("\n" + this);  
	};	
		
}

/////////////////////////////////////////////////////////////////