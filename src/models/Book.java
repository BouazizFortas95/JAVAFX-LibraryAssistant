/**
 * 
 */
package models;

/**
 * @author Bouaziz Fortas
 *
 */
public class Book {

	private final String id;
	private final String title;
	private final String author;
	private final String publisher;
	private final Boolean isAvail;


	
	public Book(String id, String title, String author, String publisher , Boolean isAvail) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.isAvail = isAvail;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}



	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}



	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}



	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}



	/**
	 * @return the isAvailable
	 */
	public Boolean getIsAvail() {
		return isAvail;
	}
}
