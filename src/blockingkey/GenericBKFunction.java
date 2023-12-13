package blockingkey;

import record.Record;

public abstract class GenericBKFunction {
	
	protected String attName;
	protected BKFunction<Record, String> function;
	
	//return attribute value, based on configured attName
	public abstract String bkv(Record record);

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public BKFunction<Record, String> getFunction() {
		return function;
	}

	public void setFunction(BKFunction<Record, String> function) {
		this.function = function;
	}
	
}
