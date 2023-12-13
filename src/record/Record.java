package record;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Record {
	
	private HashMap<String, Object> recordAttributes;
	private Set<String> bkvs;
	private Set<Record> neighbouhrs;
	private String realWId; 
	private String datasetId;
	private String id;
	
	public Record(String id) {
		bkvs = new HashSet<String>();
		this.id = id;
		recordAttributes = new HashMap<String, Object>();
		neighbouhrs = new HashSet<Record>();
	}
	
	public void addAttValue(String att, Object value) {
		recordAttributes.put(att,value);
	}
	
	public void updateAttValue(String att, Object value) {
		recordAttributes.put(att,value);
	}

	public String getId() {
		return id;
	}

	public String getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public void addBKV(String bkv) {
		bkvs.add(bkv);
	}

	public Set<String> getBkvs() {
		return bkvs;
	}

	public void setBkvs(Set<String> bkvs) {
		this.bkvs = bkvs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	public HashMap<String, Object> getRecordAttributes() {
		return recordAttributes;
	}

	public void setRecordAttributes(HashMap<String, Object> recordAttributes) {
		this.recordAttributes = recordAttributes;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		return id == other.id;
	}

	public String getRealWId() {
		return realWId;
	}

	public void setRealWId(String realWId) {
		this.realWId = realWId;
	}
	
	public void addNeighbor(Record r) {
		neighbouhrs.add(r);
	}
	
	public Set<Record> getNeighbours(){
		return neighbouhrs;
	}
	
}
