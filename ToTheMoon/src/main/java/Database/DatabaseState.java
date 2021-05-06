package Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class DatabaseState {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@OneToOne
	private String state;
	
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
//		else if (this.state == USState.NY) {
//			return "New York";
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
