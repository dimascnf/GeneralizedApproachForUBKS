package traininset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import blockingrestriction.BlockingRestrictable;
import record.RecordPair;

public class TrainingSet implements BlockingRestrictable{
	
	private Set<RecordPair> positiveSet;
	private Set<RecordPair> negativeSet;
	
	public TrainingSet() {
		positiveSet = new HashSet<RecordPair>();
		negativeSet = new HashSet<RecordPair>();
	}

	public Set<RecordPair> getPositiveSet() {
		return positiveSet;
	}

	public void setPositiveSet(Set<RecordPair> positiveSet) {
		this.positiveSet = positiveSet;
	}

	public Set<RecordPair> getNegativeSet() {
		return negativeSet;
	}

	public void setNegativeSet(Set<RecordPair> negativeSet) {
		this.negativeSet = negativeSet;
	}
	
	public void addPositivePair(RecordPair pair) {
		positiveSet.add(pair);
	}
	
	public void addNegativePair(RecordPair pair) {
		negativeSet.add(pair);
	}

	public int size() {
		return getNegativeSet().size() + getPositiveSet().size();
	}
	
}
