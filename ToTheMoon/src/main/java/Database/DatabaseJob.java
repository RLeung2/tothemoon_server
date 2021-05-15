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
public class DatabaseJob {
	private int id;
	
	private DatabaseState jobState;
	
	private String jobFilePath;
	
//	private int numDistrictings;
//	
//	private float populationEquality;
//	
//	private int rounds;
//	
//	private int coolingPeriod;
//	
//	private int recom;
	
	public DatabaseJob() {
	}
	
	@Id
    @Column(name = "JOB_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STATE_ID")
	public DatabaseState getJobState() {
		return jobState;
	}
	
	public void setJobState(DatabaseState newJobState) {
		this.jobState = newJobState;
	}
	
	public String getJobFilePath() {
		return jobFilePath;
	}
	
	public void setJobFilePath(String newJobFilePath) {
		this.jobFilePath = newJobFilePath;
	}
	
//	public int getNumDistrictings() {
//		return numDistrictings;
//	}
//	
//	public void setNumDistrictings(int newNumDistrictings) {
//		this.numDistrictings = newNumDistrictings;
//	}
//	
//	public float getPopulationEquality() {
//		return populationEquality;
//	}
//	
//	public void setPopulationEquality(float newPopulationEquality) {
//		this.populationEquality = newPopulationEquality;
//	}
//	
//	public int getRounds() {
//		return rounds;
//	}
//	
//	public void setRounds(int newRounds) {
//		this.rounds = newRounds;
//	}
//	
//	public int getCoolingPeriod() {
//		return coolingPeriod;
//	}
//	
//	public void setCoolingPeriod(int newCoolingPeriod) {
//		this.coolingPeriod = newCoolingPeriod;
//	}
//	
//	public int getRecom() {
//		return recom;
//	}
//	
//	public void setRecom(int newRecom) {
//		this.recom = newRecom;
//	}
	
//	@Override
//    public String toString() {
//        return "DatabaseJob [stateId=" + String.valueOf(stateId) + String.valueOf(numDistrictings) + String.valueOf(populationEquality) 
//        + String.valueOf(rounds) + String.valueOf(coolingPeriod) + String.valueOf(recom) + "]";
//    }
	
	@Override
    public String toString() {
		return "DatabaseJob [jobState=" + jobState.getState() + ", jobFilePath=" + jobFilePath + "]";
    }
}
