package Database;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class DatabaseEnacted {
	private int id;
	
	private DatabaseState enactedState;
	
	private String enactedFilePath;
	
	public DatabaseEnacted() {
	}
	
	@Id
    @Column(name = "ENACTEDID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STATEID")
	public DatabaseState getEnactedState() {
		return enactedState;
	}
	
	public void setEnactedState(DatabaseState newEnactedState) {
		this.enactedState = newEnactedState;
	}
	
	public String getEnactedFilePath() {
		return enactedFilePath;
	}
	
	public void setEnactedFilePath(String newEnactedFilePath) {
		this.enactedFilePath = newEnactedFilePath;
	}
	
	@Override
    public String toString() {
		return "DatabaseEnacted [enactedState=" + enactedState.getState() + ", enactedFilePath=" + enactedFilePath + "]";
    }
}
