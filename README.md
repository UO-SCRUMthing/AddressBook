# AddressBook Documentation

AddressBookRunnable.jar is the current build of the address book in cross-platform double click runnable form (assuming you already have Java installed).  

## Developer Docs
### Getting Started

This Java project follows the MVC control pattern. The root directory for source files is src/edu/uoregon/scrumthing. The project is broken up into 3 main directories that hold the source files for the GUI, tests, and the controller and backend. 

__/swingext__ holds all the classes that extend swing classes to make up the GUI. 

__/test__ (as you can imagine) is where all of the test files are. 

__/__ is where all the backend source files are. The 3 main files are Application.java, AddressBook.java, and Contact.java. These are, as the names suggest, the main application file and the 2 model files. 

Each file has a corresponding abstract class file that it extends. All data structures are implemented in a modular generic way so that the AddressBook, Contact, and Application classes can be changed independently or substituted out entirely.  

### Backend Files

__Application.java__ extends Controller.java and is the main controller that coordinates requests for data from the model. It also handles requests from the GUI such as saving, loading, and importing. 

__AddressBook.java__ extends EntryContrainer.java and defines various methods that work on a list of contacts, primarily sorting, deleting, and adding contacts. 

__Contact.java__ extends Entry.java and defines methods that work on an individual contacts. A contact is a wrapper on an ArrayList of SimpleEntries which are basically tuples. The SimpleEntries are in a very specific order as defined by <defaultFields> to facilitate quick retrieval of data fields but each SimpleEntry also has its associated field name for ease of display.  

__ControllerPool.java__ implements an iterable collection of applications so that the user can have multiple address books open at the same time. 

### GUI Files

__AddressBookGUI.java__ is the main GUI class that manages creating menus, populating lists, displaying contacts, and interpreting user input. 

__AddressDetailPanel.java__ is the class the manages displaying the actual contact information and being able to edit each contact on the fly. 

__NamePlatePanel.java__ manages the name plate above the detail panel when displaying a contact and being able to be edited when needed. 

__RegexDocumentFilter.java__ is a custom document filter that restrict user input using regex which is currently being used for ZIP code and Phone Number fields.

### Building & Running

This repo contains Eclipse project settings which can be imported by Eclipse Oxygen.2 and above.

JUnit 5 is used for unit testing which can be run by Eclipse Oxygen and above.

If imported to other IDE (i.e. IntelliJ IDEA), it is nessassary to import JUnit 5 library as well or to delete the edu.uoregon.scrumthing.test package entirely to avoid building errors.
