package blockingkey;
import java.util.Objects;

import record.Record;
import record.RecordPair;

public class BlockingKey {
	
	private String att, bkName;
	private GenericBKFunction bkFunction;
	
	public BlockingKey(String att, GenericBKFunction bkFunction, String bkName) {
		this.att = att;
		this.bkFunction = bkFunction;
		this.bkName = bkName;
	}
	
	@Override
	public String toString() {
		return "BlockingKey [att=" + att + ", bkName=" + bkName +"]";
	}

	public boolean isCovered(RecordPair pair) {
		boolean isCovered = false;
		try {
			isCovered = getBKV(pair.getR1()).equals(getBKV(pair.getR2()));
		} catch (Exception e) {
			
		}
		
		return isCovered;
	}

	public String GetBKName() {
		return bkName;
	}

	public String getAtt() {
		return att;
	}

	public void setAtt(String att) {
		this.att = att;
	}

	public GenericBKFunction getBkFunction() {
		return bkFunction;
	}

	public void setBkFunction(GenericBKFunction bkFunction) {
		this.bkFunction = bkFunction;
	}
	
	public String getBKV(Record r) {
		return "{"+this.bkName+"}" + getBkFunction().bkv(r);
	}

	@Override
	public int hashCode() {
		return Objects.hash(att, bkName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockingKey other = (BlockingKey) obj;
		return Objects.equals(att, other.att) && Objects.equals(bkName, other.bkName);
	}
	

}
