package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
/**
 * Purpose : a program that implements a simple mobile phone contacts management & perform all CURD operation on Contacts.txt file with
 * Java FX
 */
public class Contact {

	
	private final SimpleIntegerProperty ID;
	private final SimpleStringProperty name;
	private final SimpleStringProperty phoneNumber;

	/**
	 * @param name
	 * @param phoneNumber
	 */
	public Contact(int id,String name, String phoneNumber) {
		super();
		this.ID =new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.phoneNumber =new SimpleStringProperty(phoneNumber);
		
	}
	
	public String getName() {
		return name.get();
	}

	@Override
	public String toString() {
		return "Contact [ID=" + ID + ", name=" + name + ", phoneNumber=" + phoneNumber + "]";
	}

	public void setName(String name1) {
		name.set(name1);
	}

	public String getPhoneNumber() {
		return phoneNumber.get();
	}

	public void setPhoneNumber(String phoneNumber1) {
		phoneNumber.set(phoneNumber1);
	}

	public int getID() {
		return ID.get();
	}

	public void setID(int id) {
		ID.set(id);
	}

}
