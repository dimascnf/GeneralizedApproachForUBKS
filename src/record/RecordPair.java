package record;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordPair {
	
	private Record r1;
	private Record r2;
	private Set<String> commonBKVs, commonBKs;
	private double CBSWeight, positiveWeight, negativeWeight;
	
	public RecordPair(Record r1, Record r2) {
		if (r1.getId().compareTo(r2.getId()) < 0) {
			this.r1 = r1;
			this.r2 = r2;
		} else {
			this.r1 = r2;
			this.r2 = r1;
		}
	}
	
	public Set<String> getCommonBKVs(){
		return commonBKVs;
	}
	
	public Set<String> getCommonBKs(){
		return commonBKs;
	}
	
	public void updateCommonBKs() {
		Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(commonBKVs.toString());
		commonBKs = new HashSet<String>();
		while (m.find()) {
			commonBKs.add(m.group(1));
		}
	}
	
	public void updateCommonBKVs() {
		commonBKVs = new HashSet<String>(r1.getBkvs());
		commonBKVs.retainAll(r2.getBkvs());
		updateCommonBKs();
	}

	public Record getR1() {
		return r1;
	}

	public void setR1(Record r1) {
		this.r1 = r1;
	}

	public Record getR2() {
		return r2;
	}

	public void setR2(Record r2) {
		this.r2 = r2;
	}
	
	public double getCBSWeight() {
		return CBSWeight;
	}

	public void setCBSWeight(double cBSWeight) {
		CBSWeight = cBSWeight;
	}
	
	public double getPositiveWeight() {
		return positiveWeight;
	}

	public void setPositiveWeight(double positiveWeight) {
		this.positiveWeight = positiveWeight;
	}

	public double getNegativeWeight() {
		return negativeWeight;
	}

	public void setNegativeWeight(double negativeWeight) {
		this.negativeWeight = negativeWeight;
	}

	@Override
	public int hashCode() {
		return Objects.hash(r1, r2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordPair other = (RecordPair) obj;
		return Objects.equals(r1, other.r1) && Objects.equals(r2, other.r2);
	}

	public String getIDs() {
		return r1.getId() +"="+ r2.getId();
	}
	
}