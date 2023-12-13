package record;

import java.util.HashSet;
import java.util.Set;

import blockingrestriction.BlockingRestrictable;

public class Dataset implements BlockingRestrictable {
	
	Set<Record> records;
	
	public Dataset() {
		records = new HashSet<Record>();
	}
	
	public int size() {
		return records.size();
	}
	
	public void addRecord(Record r) {
		records.add(r);
	}
	
	public Set<Record> getRecords(){
		return records;
	}
	
	public void setRecords(Set<Record> records) {
		this.records = records;
	}

}
