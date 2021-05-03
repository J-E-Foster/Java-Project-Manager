
///////PROJECT CLASS////////
public class Project
{

	
///////ATTRIBUTES////////
	
//Project attributes include the following string information: project number, project name, building type, physical address, ERF number, total fee charged,  
//total amount paid to date, deadline, architect name, contractor name, and customer name.
	String projectNum;
	String projectName;
	String buildType;
	String physAddress;
	String erfNum;
	String totalFee;
	String totalPaid;
	String deadLine;
	String architect;
	String contractor;
	String customer;
	
	
///////METHODS////////
	
//1.) Constructor. This method inserts parameters as object attributes when creating a new Project object:
	public Project(String projectNum, String projectName, String buildType, String physAddress, String erfNum,
			String totalFee, String totalPaid, String deadLine, String architect, String contractor, String customer) 	
	{		
		this.projectNum = projectNum;
		this.projectName = projectName;
		this.buildType = buildType;
		this.physAddress = physAddress;
		this.erfNum = erfNum;
		this.totalFee = totalFee;
		this.totalPaid = totalPaid;
		this.deadLine = deadLine;
		this.architect = architect;
		this.contractor = contractor;
		this.customer = customer;
	}
	
//2.) "toSTring()" replacement. Overrides built-in "toString()" method. The output string will thus be displayed if an object is printed
//(Geeksforgeeks.com, 2018, Overriding toString() in Java, t.ly/PzEE):	
	public String toString()
	{
		String output = "Project Number:\t\t\t" + projectNum;
		output += "\nProject Name:\t\t\t" + projectName;
		output += "\nBuilding Type:\t\t\t" + buildType;
		output += "\nPhysical Address:\t\t" + physAddress;
		output += "\nERF Number:\t\t\t" + erfNum;
		output += "\nTotal Fee:\t\t\tR " + totalFee;
		output += "\nTotal Amount Paid to Date:\tR " + totalPaid;
		output += "\nDeadline:\t\t\t" + deadLine;
		output += "\nArchitect:\t\t\t" + architect;
		output += "\nContractor:\t\t\t" + contractor;
		output += "\nCustomer:\t\t\t" + customer;
		
		return output;
	}
	
//3.) "changeDeadLine()" replaces the existing deadline attribute "deadLine" with a user-entered new deadline "newDeadLine".
//The new deadline is added as parameter:
	public void changeDeadLine(String newDeadLine)
	{
		deadLine = newDeadLine;		
	}	
	
//4.) "changeTotalPaid()" replaces the existing total amount paid to date attribute "totalPaid" with a user-entered new amount "newtotalPaid".
//The new total paid amount is added as parameter:
	public void changeTotalPaid(String newtotalPaid)
	{
		totalPaid = newtotalPaid;		
	}

//5.) "markFinalized()" adds "Finalized" and a user-entered completion date to a project deadline attribute.
//The completion date is added as parameter:
	public void markFinalized(String completionDate)
	{
		deadLine += "\nFinalized:\t\t\t" + completionDate;
	}
	
		
}


//////////////// THE END /////////////////