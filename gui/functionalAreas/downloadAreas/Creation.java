package gui.functionalAreas.downloadAreas;

/**
 * This simple class is used to represent a users creation. It consists of a link to their
 * creation and the description that accompanies it.
 * @author fsta657
 *
 */
public class Creation {
	private String desc;
	private String link;


	public Creation(String desc, String link) {
		this.desc = desc;
		this.link = link;
	}
	public String getDesc() {
		return desc;
	}

	public String getLink() {
		return link;
	}
}
