package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * Purpose : a program that implements a simple mobile phone contacts management & perform all CURD operation on Contacts.txt file with
 * Java FX
 */
public class MainController implements Initializable {

	@FXML
	private Label message;

	@FXML
	private TextField name;

	@FXML
	private TextField phoneNumber;

	@FXML
	private TableView<Contact> table;

	@FXML
	TableRow<Contact> tableRow;

	@FXML
	private TableColumn<Contact, Integer> idCol;
	@FXML
	private TableColumn<Contact, String> nameCol;
	@FXML
	private TableColumn<Contact, String> phoneNumberCol;

	@FXML
	private TextField searchText;

	public ObservableList<Contact> myContacts = FXCollections.observableArrayList(readTextFileContacts());

	// Sorted Filtered List, A Predicate for pulling sorted data
	// Wrap the ObservableList in a FilteredList (initially display all data).
	FilteredList<Contact> filteredData = new FilteredList<>(myContacts, p -> true);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idCol.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("ID"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));
		phoneNumberCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("phoneNumber"));
	//table.setItems(myContacts);
		 table.setEditable(true);
		table.setOnMouseClicked(e -> {
			try {
				Contact contact = table.getSelectionModel().getSelectedItem();
				name.setText(contact.getName());
				phoneNumber.setText(contact.getPhoneNumber());
			} catch (Exception eq) {
			}
		});
	}

	/**
	 * Add new contact
	 */
	public void addNewContact(ActionEvent event) {
		int id = 0;
		if (myContacts != null && myContacts.size() == 0) {
			id++;
		} else {
			id = myContacts.size() + 1;
		}

		boolean flag = true;
		flag = isContactExists(flag);
		if (flag) {
			if ((name.getText() == null || name.getText().isEmpty())
					&& (phoneNumber.getText() == null || phoneNumber.getText().isEmpty())) {
				message.setText("Name or Phone Number should not be empty!. Please provide..  ");
				return;
			}

			if ((name.getText() == null || name.getText().isEmpty())) {
				message.setText("Name should not be empty!. Please provide..  ");
				return;
			}
			if ((phoneNumber.getText() == null || phoneNumber.getText().isEmpty())) {
				message.setText("Phone Number should not be empty!. Please provide..  ");
				return;
			}
			if ((name.getText() != null && !name.getText().isEmpty())
					&& (phoneNumber.getText() != null && !phoneNumber.getText().isEmpty())) {
				if (myContacts.add(new Contact(id, name.getText(), phoneNumber.getText()))) {
					message.setText("New Contact added successfully! ");
					clearFields();
					performOperationsOnContacts();
				}
			}
			return;
		}
	}

	/**
	 * @param flag
	 * @return
	 */
	private boolean isContactExists(boolean flag) {
		for (Contact contact : myContacts) {
			if (contact.getName().equals(name.getText())) {
				message.setText(" Contact is already existed  in the list.");
				flag = false;
				return flag;
			}
		}
		return flag;
	}

	/**
	 * Remove contact
	 */
	public void removeContact(ActionEvent event) {
		ObservableList<Contact> myContacts1 = table.getSelectionModel().getSelectedItems();
		if (myContacts1.size() == 0) {
			message.setText("At least one row should be selected to delete record!");
			return;
		}
		String existingContactName = myContacts1.get(0).getName();
		name.setText(existingContactName);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure to delete?");
		Optional<ButtonType> action = alert.showAndWait();
		boolean flag = true;
		if (action.get() == ButtonType.OK) {
			try {
				Iterator<Contact> itr = myContacts.iterator();
				while (itr.hasNext()) {
					Contact contact = itr.next();
					if (contact.getName().equals(existingContactName)) {
						itr.remove();
						message.setText("Contact \"" + existingContactName + "\" was deleted successfully! ");
						performOperationsOnContacts();
						clearFields();
						flag = false;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (flag) {
			message.setText("Contact is not found in the list.");
			clearFields();
			return;
		}
	}

	/**
	 * Modify contact
	 */
	public void updateContact() {

		ObservableList<Contact> myContacts1 = table.getSelectionModel().getSelectedItems();
		if (myContacts1.size() == 0) {
			message.setText("At least one row should be selected to update record!");
			return;
		}
		String existingContactName = myContacts1.get(0).getName();
		String newContactName = name.getText();
		String newContactPhoneNumber = phoneNumber.getText();
		if ((newContactName == null || newContactName.isEmpty())
				&& (newContactPhoneNumber == null || newContactPhoneNumber.isEmpty())) {
			message.setText("Name or Phone Number should not be empty!. Please provide..  ");
			return;
		}

		if ((newContactName == null || newContactName.isEmpty())) {
			message.setText("Name should not be empty!. Please provide..  ");
			return;
		}

		if ((newContactPhoneNumber == null || newContactPhoneNumber.isEmpty())) {
			message.setText("Phone Number should not be empty!. Please provide..  ");
			return;
		}
		boolean flag = true;
		
		if ((newContactName != null && !newContactName.isEmpty())
				&& (newContactPhoneNumber != null && !newContactPhoneNumber.isEmpty())) {

			int id = 0;
			try {
				Iterator<Contact> itr = myContacts.iterator();
				while (itr.hasNext()) {
					Contact contact = itr.next();
					if (contact.getName().equals(existingContactName)) {
						id = contact.getID();
						itr.remove();

						if (myContacts.add(new Contact(id, name.getText(), phoneNumber.getText()))) {
							message.setText("Contact \"" + existingContactName + "\" was Updated successfully! ");
							clearFields();
							performOperationsOnContacts();
							flag = false;
						}

					}
				}
			} catch (Exception e2) {
			}

			for (Contact contact : myContacts) {
				if (contact.getName().equals(existingContactName)) {
					contact.setName(newContactName);
					contact.setPhoneNumber(newContactPhoneNumber);
					message.setText("Successfully updated record.");
					clearFields();
					performOperationsOnContacts();
					flag = false;
					return;
				}
			}
		}
		if (flag) {
			message.setText("Contact is not found in the list.");
			clearFields();
			return;
		}
	}

	/**
	 * Empty the Name and phone number field 
	 */
	private void clearFields() {
		name.clear();
		phoneNumber.clear();
	}

	/**
	 * Perform CURD operations on contacts.txt
	 */
	private void performOperationsOnContacts() {
		table.setItems(myContacts);
		PrintWriter outputStream = null;
		PrintWriter printWriter = null;
		String filename = "Contacts.txt";
		try {
			outputStream = new PrintWriter(new FileOutputStream(filename, true));
			outputStream.print("");
			printWriter = new PrintWriter(filename);
			for (int i = 0; i < myContacts.size(); i++) {
				String output = myContacts.get(i).getID() + "." + myContacts.get(i).getName() + " -> "
						+ myContacts.get(i).getPhoneNumber();
				printWriter.println(output);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't Open.");
			System.exit(0);
		}
		outputStream.close();
		printWriter.close();
	}

	/**
	 * @return
	 * @throws NumberFormatException
	 * Scrape out the data from Text File 
	 */
	private static List<Contact> readTextFileContacts() throws NumberFormatException {
		List<Contact> list = new ArrayList<Contact>();
		try (BufferedReader br = new BufferedReader(new FileReader("Contacts.txt"))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				String arr[] = sCurrentLine.split("->");
				String split = arr[0];
				list.add(new Contact(Integer.parseInt(split.substring(0, 1)), split.substring(2, split.length() - 1),
						arr[1].trim()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Clear the form
	 */
	public void clearData() {
		clearFields();
		table.setItems(FXCollections.observableArrayList());
		message.setText("");
		searchText.clear();
	}

	/**
	 * Display Data
	 */
	public void displayData() {
		if (myContacts != null && myContacts.size() == 0)
			message.setText("No Contacts found.Please add new contact.");

		table.setItems(myContacts);
	}

	@FXML
	public void search() {
		// Set the filter Predicate whenever the filter changes.
		searchText.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(contact -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				if (contact.getName().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				} else if (contact.getPhoneNumber().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Contact> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(table.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
		table.setItems(sortedData);

	}

}