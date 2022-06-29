/**
 * 
 */
package models;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @author Bouaziz Fortas
 *
 */
public class Setting {
	
	public static final String CONFIG_FILE = "config.txt";
	private int nDaysCanKeepBook;
	private float finePerDay;
	private String username;
	private String password;
	
	public Setting() {
		nDaysCanKeepBook = 14;
		finePerDay = 2;
		username = "admin";
		password = "admin";
	}

	/**
	 * @return the nDaysCanKeepBook
	 */
	public int getnDaysCanKeepBook() {
		return nDaysCanKeepBook;
	}

	/**
	 * @param nDaysCanKeepBook the nDaysCanKeepBook to set
	 */
	public void setnDaysCanKeepBook(int nDaysCanKeepBook) {
		this.nDaysCanKeepBook = nDaysCanKeepBook;
	}

	/**
	 * @return the finePerDay
	 */
	public float getFinePerDay() {
		return finePerDay;
	}

	/**
	 * @param finePerDay the finePerDay to set
	 */
	public void setFinePerDay(float finePerDay) {
		this.finePerDay = finePerDay;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	public static void initConfig() {
		Writer writer = null;
		try {
			Setting setting = new Setting();
			Gson gson = new Gson();
			writer = new FileWriter(CONFIG_FILE);
			gson.toJson(setting, writer);
		} catch (IOException e) {
			System.err.println("#Error_Message_initConfig_readConfigFile : " + e.getLocalizedMessage());
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				System.err.println("#Error_Message_initConfig_closeWirter : " + e.getLocalizedMessage());
			}
		}
	}
	
	public static Setting getSettings() {
		Gson gson = new Gson();
		Setting setting = new Setting();
		try {
			setting = gson.fromJson(new FileReader(CONFIG_FILE), Setting.class);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			initConfig();
			System.err.println("#Error_Message_initConfig_FileReader : " + e.getLocalizedMessage());
		}
		return setting;
	}
	
	public static void updateSettings(Setting setting) {
		Writer writer = null;
		try {
			Gson gson = new Gson();
			writer = new FileWriter(CONFIG_FILE);
			gson.toJson(setting, writer);
		} catch (IOException e) {
			System.err.println("#Error_Message_initConfig_readConfigFile : " + e.getLocalizedMessage());
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				System.err.println("#Error_Message_initConfig_closeWirter : " + e.getLocalizedMessage());
			}
		}

	}
	
}
