---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# InnSync Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the AirBnB Host issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `PersonDetailPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a visitor).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

**The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.innsync.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

> Please note that certain aspects, such as UML classes, may have been simplified to fit within the diagram's constraints and maintain readability.
### \[Proposed\] Undo/redo feature
The current undo feature undoes only the last recorded change, it does not maintain a history of commands or changes. Thus, users do not have the ability to undo more than one past change.
#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a host for guests to stay at one or more properties on hosting platforms _(i.e. AirBnB)_
* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Allows AirBnB hosts to manage all their property management related contacts faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                     | I want to …​                                    | So that I can…​                                                           |
|----------|---------------------------------------------|-------------------------------------------------|---------------------------------------------------------------------------|
| `* * *`  | new user                                    | see usage instructions                          | refer to instructions when I forget how to use the App                    |
| `* * *`  | AirBnB host                                 | add a new visitor                               | keep track of who is visiting my property                                 |
| `* * *`  | AirBnB host                                 | edit a visitor                                  | update details of a visitor                                               |
| `* * *`  | AirBnB host                                 | list all visitors                               | see all the visitors I have added                                         |
| `* * *`  | AirBnB host                                 | delete a visitor                                | remove entries that I no longer need                                      |
| `* * *`  | AirBnB host                                 | add a tag to visitor                            | categorize and identify them easily                                       |
| `* * *`  | AirBnB host                                 | add a booking tag to visitor                    | quickly assign the visitor to their property                              |
| `* * *`  | AirBnB host                                 | remove a tag from the visitor                   | to unassign the visitor tag                                               |
| `* * *`  | AirBnB host                                 | remove a booking tag from the visitor           | to unassign the visitor booking tag                                       |
| `* * *`  | AirBnB host                                 | find a visitor by name                          | quickly find a specific visitor                                           |
| `* *`    | AirBnB host                                 | filter visitors by property booked              | quickly find visitors who visited a specific property                     |
| `* *`    | AirBnB host                                 | filter visitors by date of stay                 | quickly find visitors using specific time periods                         |
| `* *`    | AirBnB host                                 | filter visitors by tag                          | quickly find visitors using personalised categories                       |
| `* *`    | AirBnB host                                 | filter visitors by next upcoming booking        | prepare for future bookings efficiently                                   |
| `* *`    | AirBnB host                                 | add a request to visitor                        | fulfill the visitor request                                               |
| `* *`    | AirBnB host                                 | mark the request as completed in the visitor    | to mark visitor request as fulfilled                                      |
| `* * `   | AirBnB host                                 | unmark the request as incomplete in the visitor | to unmark visitor request as unfulfilled                                  |
| `* * `   | AirBnB host                                 | delete the request from the visitor             | to remove visitor request                                                 |
| `* *`    | AirBnB host                                 | star a visitor                                  | favorite the visitor so that they appear at the top                       |
| `* *`    | AirBnB host                                 | unstar a visitor                                | remove favorite from the visitor so that they no longer appear at the top |
| `* *`    | AirBnB host                                 | list all the starred visitor                    | see all the favorite visitors I have starred                              |
| `* * `   | AirBnB host                                 | add a memo to visitor                           | give a short note to describe the visitor                                 |
| `* *`    | AirBnB host                                 | undo the last command                           | recover from mistakes                                                     |
| `* *`    | AirBnB host                                 | clear all visitors                              | start over with a clean slate                                             |
| `* *`    | AirBnB host                                 | save visitor details to a file                  | backup my address book                                                    |
| `* *`    | AirBnB host                                 | load visitor details from a file                | restore my address book                                                   |
| `*`      | user with many visitors in the address book | sort visitors by name                           | find visitors efficiently                                                 |

### Use cases

(For all use cases below, the **System** is `InnSync` and the **Actor** is `AirBnB Host`, unless specified otherwise)

**Use case: UC01 - Add a visitor**

**MSS**

1. AirBnB Host requests to add a visitor with specified details.
2. InnSync validates the input.
3. InnSync adds the visitor.
4. InnSync shows a success message.
Use case ends.

**Extensions**

* 2a. The input format is invalid.

    * 2a1. InnSync shows an error message and informs the AirBnB Host of the proper format. <br>
      Use case resumes at step 1.

* 2b. The visitor already exists in InnSync.

    * 2b1. InnSync shows an error message and informs the AirBnB Host that the visitor already exists. <br>
      Use case resumes at step 1.

* Guarantees: The contact is successfully created and stored in the system if the input data is valid. Duplicate contacts will not be created.

**Use case: UC02 - Delete a visitor**

**MSS**

1. AirBnB Host requests to delete a specific visitor.
2. InnSync deletes the visitor.
3. InnSync displays a message for successful deletion of a visitor's contact. <br>
    Use case ends.

**Extensions**

* 1a. The given index is invalid.

    * 1a1. InnSync shows an error message and informs the AirBnB Host that the index is invalid. <br>
      Use case resumes at step 1.

*  Guarantees: The contact is successfully deleted from InnSync, and any persistent storage.

**Use case: UC03 - Edit a visitor**

**MSS**

1.  AirBnB Host requests to list visitors
2.  InnSync shows a list of visitors
3.  AirBnB Host requests to edit a specific visitor in the list
4.  InnSync validates the entered detail
5.  InnSync updates the contact with the new provided detail
6.  InnSync shows the updated details of the visitor.
7.  InnSync updates local JSON file with updated contact detail <br>
    Use case ends.

**Extensions**

* 2a. The list is empty.
   * 2a1. InnSync shows an error message that there are no saved contacts. <br>
    Use case ends.

* 3a. The given index is invalid.

    * 3a1. InnSync shows an error message. <br>
      Use case resumes at step 2.

* 3b. Input argument(s) are invalid  tag.

    * 3b1. InnSync shows an error message. <br>
      Use case resumes at step 2.

* 3c. The contact already exists in InnSync

    * 3c1. InnSync shows an error message that the contact already exists. <br>
      Use case resumes at step 2.

**Use case: UC04 - Find a visitor by name**

**MSS**

1.  AirBnB Host requests to find a visitor by name
2.  InnSync shows the details of the visitor if found. <br>
    Use case ends.

**Extensions**

* 2a. The list is empty.
  * 2a1. InnSync shows an error message that there are no contacts available <br>
  Use case ends.

* 3a. The given name is invalid.

    * 3a1. InnSync shows an error message. <br>
      Use case ends.


**Use case: UC05 - Add Tag to Contact**

**MSS**

1.  AirBnB Host requests to list visitors
2.  InnSync shows a list of visitors
3.  AirBnB Host requests to add a tag to a specific visitor in the list
4.  InnSync validates the entered tag (ie. missing input)
5.  InnSync updates the contact with the new provided tag
6.  InnSync shows the updated details of the visitor.
7.  InnSync updates local JSON file with updated contact detail <br>
    Use case ends.

**Extensions**

* 2a. The list is empty.
   * 2a1. InnSync shows an error message that there are no saved contacts. <br>
    Use case ends.

* 3a. The given index is invalid.

    * 3a1. InnSync shows an error message. <br>
      Use case resumes at step 2.

* 4a. Input argument(s) are invalid.

    * 4a1. InnSync shows an error message. <br>
      Use case resumes at step 2.

**Use case: UC06 - Add Booking Tag to Contact**

**MSS**

1.  AirBnB Host requests to list visitors
2.  InnSync shows a list of visitors
3.  AirBnB Host requests to add a booking tag to a specific visitor in the list
4.  InnSync validates the entered booking tag (ie. date format, missing input)
5.  InnSync updates the contact with the new provided booking tag
6.  InnSync shows the updated details of the visitor.
7.  InnSync updates local JSON file with updated contact detail <br>
    Use case ends.

**Extensions**

* 2a. The list is empty.
   * 2a1. InnSync shows an error message that there are no saved contacts. <br>
    Use case ends.

* 3a. The given index is invalid.

    * 3a1. InnSync shows an error message. <br>
      Use case resumes at step 2.

* 4a. Input argument(s) are invalid.

    * 4a1. InnSync shows an error message. <br>
      Use case resumes at step 2.

* 5a. The given booking tag date overlaps with an existing booking tag.

    * 5a1. InnSync shows an error message. <br>
      Use case resumes at step 2.

**Use case: UC07 - List visitors**

**MSS**

1. AirBnB Host requests to list all visitors.
2. InnSync displays a list of all visitors.

**Extensions**

* 2a. The list is empty.

    * 2a1. InnSync shows a message that the list is empty. <br>
     Use case ends.

**Use case: UC08 - Help**

**MSS**

1. AirBnB Host wants to see the user guide
2. InnSync displays a pop up with a hyperlink to the user guide <br>
   Use case ends.

**Use case: UC09 - Clear**

**MSS**

1. AirBnB Host remove all visitor contacts in the database
2. InnSync clears the database and updates local JSON file <br>
   Use case ends.

**Use case: UC10 - Exit**

**MSS**

1. AirBnB Host wants to exit the application
2. InnSync is terminated <br>
   Use case ends.

### Non-Functional Requirements
1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 visitors without noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The application size should not be more than 100mb to enable user to have easy installation on various devices.
5.  The application should also be optimized such that its CPU and memory usage is low so that it can run on low-end machines.
6.  The system should be optimized for single-user operation instead of multiple-user operation.
7.  The application should not crash during network failures and be reliable enough for continuous use case during operation hours.
8.  The data used should be stored in a structured and human-readable format like JSON.
9.  Should be easily extendable so that new features can be added without having to refactor the entire project.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **AirBnB host**: An AirBnB host is an individual or business that list their property on the platform for short-term rentals. The host provide accommodations in the forms of apartments, houses or rooms for guests, typically for leisure activities.
* **Visitor**:  Any individual who accesses an AirBnB property, including guests staying at the property, property owners, service providers performing work, or other authorized individuals. Visitors may include cleaners, maintenance personnel, property inspectors, delivery services, and other vendors.
* **CLI (Command Line Interface)**: A text-based interface where users interact with the application with a keyboard typing commands instead of using a graphical user interface.
* **JAR**: A packed file format used in Java that contains compiled java codes to enable easy distribution, portability and execution that includes libraries and resources to allow the program to function.
* **JSON (JavaScript Object Notation)**: A lightweight data format widely used for storing and exchanging structured data in a human-readable, that is often used in databases and APIs.
* **GUI (Graphical User Interface)**: A visual graphical interface that allows users to interact with the application using various graphical elements like search boxes, buttons, text boxes, and eta.
* **Prerequisite**: A set of rules or instructions to be executed before prior to executing a particular task.
* **MSS (Main Success Scenario)**:It describes the most straightforward interaction for a given use case, which assumes that nothing goes wrong.
* **API (Application Programming Interface)**: A set of rules or protocols that govern the application to allow different software applications to work together by communication with each other.
--------------------------------------------------------------------------------------------------------------------
## **Appendix: Planned Enhancements**
Team Size: 5

1. **Allow all command parsers to escape prefixes in all fields**: Currently, only the edit and add command allow escaping of prefixes, and only in the name argument. We plan to extend this to all commands and fields, in order to allow the user more freedom in their choice of arguments and keywords.

--------------------------------------------------------------------------------------------------------------------
## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Open a new terminal and navigate to the directory containing the jar file

   3. Run the jar file using the command `java -jar innsync.jar`. <br>
      Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app using the command `java -jar innsync.jar`.<br>
      Expected: The most recent window size and location is retained.

### Listing all visitors

1. Listing all visitors when there are no contacts

   1. Prerequisites: No contacts in addressbook. This can be done using the `clear` command.

   2. Test case: `list`<br>
      Expected: No contacts displayed in list. Message stating that there are no contacts shown in the status message.

   3. Other incorrect list commands to try: `List`, `listx`, `...`<br>
      Expected: Error message displayed in status message.

2. Listing all visitors when there is at least one contact in address book

   1. Prerequisites: At least one contact in address book.

   2. Test case: `list`<br>
      Expected: All contacts displayed in list, with all contacts sorted first by whether they are starred, and then in lexicographical order of their name. Success Message shown in the status message.

   3. Other incorrect list commands to try: `List`, `listx`, `...`<br>
      Expected: Error message displayed in status message.

### Deleting a visitor

1. Deleting a visitor while all visitors are being shown

   1. Prerequisites: List all visitors using the `list` command. Multiple visitors in the list.

   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.

   3. Test case: `delete 0`<br>
      Expected: No visitor is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Adding a visitor

1. Adding a visitor who is not currently in the addressbook

   1. Prerequisites: No visitor with matching phone number and email address is displayed in addressbook when using `list` command.

   2. Test case: `add n/John Doe a/John Doe Street e/johndoe@example.com p/+65 8888 8888`<br>
      Expected: Contact is added to list. Contact List Card is selected in GUI. Contact Details are shown in Details Panel in GUI. Details of the added contact shown in the status message.

   3. Test case: `add n/John Doe a/John Doe Street e/jd@example.com p/+65 88888888 t/guest m/no room cleaning b/StarHotel from/2025-04-01 to/2025-04-05 r/Needs Laundry Detergent`<br>
      Expected: Contact is added to list. Contact List Card is selected in GUI. Contact Details are shown in Details Panel in GUI. Details of the added contact shown in the status message.

   4. Test case: `add n/John Doe`<br>
      Expected: No visitor is added. Error details shown in the status message.

   5. Test case: `add n/John Doe a/John Doe Street e/johndoe@example.com p/123`<br>
      Expected: No visitor is added. Error details shown in the status message.

   6. Other incorrect add commands to try: `Add`, `add abc`, `...`<br>
      Expected: Similar to previous.

### Saving data

1. Dealing with missing data files

   1. Navigate to the directory containing the .jar file

   2. Navigate into the 'data/' folder and locate the data file 'addressbook.json'

   3. Delete the file 'addressbook.json'

   4. Attempt to run the app. <br>
      Expected: App populates address book with default list of contacts

2. Dealing with corrupted data files

   1. Navigate to the directory containing the .jar file

   2. Navigate into the 'data/' folder and locate the data file 'addressbook.json'

   3. Open the file and delete a semicolon (';'), or add random characters such as '/' anywhere in the file

   4. Attempt to run the app. <br>
      Expected: App starts up using an empty address book.
