package Database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DatabaseState {
	private int id;
	
	private String state;
	
	public DatabaseState() {
	}
	
	public DatabaseState(String state) {
		this.state = state;
	}
	
	@Id
    @Column(name = "STATEID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String newState) {
		this.state = newState;
	}
	
//	public String convertStateToString() {
//		if (this.state == USState.NV)
//		{
//			return "Nevada";
//		}
//		else if (this.state == USState.SC) {
//			return "South Carolina";
//		}
//		else
//		{
//			return "Washington";
//		}
//	}

	@Override
    public String toString() {
        return "DatabaseState [state=" + state + "]";
    }
}
