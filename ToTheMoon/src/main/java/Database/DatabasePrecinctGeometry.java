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
public class DatabasePrecinctGeometry {
	private int id;
	
	private DatabaseState precinctGeometryState;
	
	private String precinctGeometryFilePath;
	
	public DatabasePrecinctGeometry() {
	}
	
	@Id
    @Column(name = "PRECINCTGEOMETRYID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STATEID")
	public DatabaseState getPrecinctGeometryState() {
		return precinctGeometryState;
	}
	
	public void setPrecinctGeometryState(DatabaseState newPrecinctGeometryState) {
		this.precinctGeometryState = newPrecinctGeometryState;
	}
	
	public String getPrecinctGeometryFilePath() {
		return precinctGeometryFilePath;
	}
	
	public void setPrecinctGeometryFilePath(String newPrecinctGeometryFilePath) {
		this.precinctGeometryFilePath = newPrecinctGeometryFilePath;
	}
	
	@Override
    public String toString() {
		return "DatabasePrecinctGeometry [precinctGeometryState=" + precinctGeometryState.getState() + ", precinctGeometryFilePath=" + precinctGeometryFilePath + "]";
    }
}
