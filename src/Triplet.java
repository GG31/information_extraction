
public class Triplet {
	private String subject;
	private String property;
	private String value;
	
	public Triplet(String subject, String property, String value){
		this.subject = subject;
		this.property = property;
		this.value = value;
	}
	
	public String toString() {
		return "<" + subject + "," + property + "," + value + ">";
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public String getPropriete() {
		return this.property;
	}
}
