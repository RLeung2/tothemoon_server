package Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class DatabaseJob {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@ManyToOne
	private int stateId;
	
//	@OneToOne
//	private String jobFilePath;
	
	@OneToOne
	private int numDistrictings;
	
	@OneToOne
	private float populationEquality;
	
	@OneToOne
	private int rounds;
	
	@OneToOne
	private int coolingPeriod;
	
	@OneToOne
	private int recom;
	
	public int getStateId() {
		return stateId;
	}
	
	public void setStateId(int newStateId) {
		this.stateId = newStateId;
	}
	
//	public String getJobFilePath() {
//		return jobFilePath;
//	}
//	
//	public void setJobFilePath(String newJobFilePath) {
//		this.jobFilePath = newJobFilePath;
//	}
	
	public int getNumDistrictings() {
		return numDistrictings;
	}
	
	public void setNumDistrictings(int newNumDistrictings) {
		this.numDistrictings = newNumDistrictings;
	}
	
	public float getPopulationEquality() {
		return populationEquality;
	}
	
	public void setPopulationEquality(float newPopulationEquality) {
		this.populationEquality = newPopulationEquality;
	}
	
	public int getRounds() {
		return rounds;
	}
	
	public void setRounds(int newRounds) {
		this.rounds = newRounds;
	}
	
	public int getCoolingPeriod() {
		return coolingPeriod;
	}
	
	public void setCoolingPeriod(int newCoolingPeriod) {
		this.coolingPeriod = newCoolingPeriod;
	}
	
	public int getRecom() {
		return recom;
	}
	
	public void setRecom(int newRecom) {
		this.recom = newRecom;
	}
	
	@Override
    public String toString() {
        return "DatabaseJob [stateId=" + String.valueOf(stateId) + String.valueOf(numDistrictings) + String.valueOf(populationEquality) 
        + String.valueOf(rounds) + String.valueOf(coolingPeriod) + String.valueOf(recom) + "]";
    }
}
