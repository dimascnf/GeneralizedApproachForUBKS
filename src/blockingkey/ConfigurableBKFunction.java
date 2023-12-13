package blockingkey;
import record.Record;

public class ConfigurableBKFunction extends GenericBKFunction {
	
	@Override
	public String bkv(Record record) {
		
		return function.apply(record);
	}
	
}
