//java.util is imported to use ArrayList:
import java.util.*;


////////PERSON CLASS////////
public class Person
{

	
/////////ATTRIBUTES/////////
	
//Person attributes include the following string information: person type (architect, contractor, or customer), name, cellphone number, email address,
//and physical address:
	String type;
	String surname;
	String name;     ////Add person title?
	String cellNum;
	String emailAddress;
	String physAddress;
	
	
///////////METHODS///////////
	
//1.) Constructor. This method inserts parameters as object attributes when creating a new Person object:
	public Person (String type, String surname, String name, String cellNum, String emailAddress, String physAddress)
	{
		this.type = type;
		this.surname = surname;
		this.name = name;
		this.cellNum = cellNum;
		this.emailAddress = emailAddress;
		this.physAddress = physAddress;
	}
	
//2.) "toString()" replacement: Overrides built-in "toString()" method. The output string will thus be displayed if an object is printed
//(Geeksforgeeks.com, 2018, Overriding toString() in Java, t.ly/PzEE):
	public String toString()
	{
		String output = "Type:\t\t\t\t" + type; 
		output += "\nName:\t\t\t\t" + name + " " + surname;
		output += "\nCell Number:\t\t\t" + cellNum;
		output += "\nEmail Address:\t\t\t" + emailAddress;
		output += "\nPhysical Address:\t\t" + physAddress;
		
		return output;
	}

//3.) "toString2()" displays a variation of the "toSTring()" string (needed for invoice generation).
//Attribute string "physAddress" is first split into an array (commas and spaces removed - Geeksforgeeks.com, 2018, Split() String method in Java with examples, t.ly/dwnK).
//An enhanced for loop ("for each") is used to loop through the array to print each element on a separate line (W3schools.com, 2021, Java For Loop, t.ly/Jjwv): 	
	public String toString2()
	{		
		String[] physAddressSplit = physAddress.split(", ");
		                                                         
		String output = name + " " + surname;
		output += "\n" + cellNum;
		output += "\n" + emailAddress;
		output += "\n";
		for (String i : physAddressSplit)
		{
			output += "\n" + i;
		}
		
		
		return output;
	}
	
//.4) "updatePerson()" updates a person's contact details (cellphone number, email, and physical address). The user-entered new details are passed as parameters.
//ArrayList "updateChoiceBlank" is created so that new details left blank by the user (i.e. the details didn't change) are not changed by the method.
//A range of different new-line and blank-space characters are added (these could vary depending on OS):
	public void updatePerson(String newCellNum, String newEmailAddress, String newPhysAddress)
	{		
		ArrayList <String> updateChoiceBlank = new ArrayList <String>();
		updateChoiceBlank.add("\n");
		updateChoiceBlank.add("\r");
		updateChoiceBlank.add("\r\n");
		updateChoiceBlank.add(" ");
		updateChoiceBlank.add("");

//If the "blank" ArrayList does not contain the user-entered detail (i.e. the user entered a new detail), that Person attribute is swapped for the new detail
//(Striver, 2018, Geeksforgeeks.com, Arraylist.contains() in Java, t.ly/IEWi):
		if (updateChoiceBlank.contains(newCellNum) == false) 
		{
			cellNum = newCellNum;
		}
		
		if (updateChoiceBlank.contains(newEmailAddress) == false)
		{
			emailAddress = newEmailAddress;
		}
		
		if (updateChoiceBlank.contains(newPhysAddress) == false)
		{
			physAddress = newPhysAddress;
		}
	}
	
}



///////////////// THE END /////////////////