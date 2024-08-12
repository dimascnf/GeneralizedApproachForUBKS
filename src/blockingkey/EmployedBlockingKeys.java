package blockingkey;

import org.apache.commons.codec.language.Soundex;
import record.Record;
/**
 * Blocking keys employed in the experimental evaluation.
 */
public class EmployedBlockingKeys {
	
	private static void generateBKsForCompanyNames() {
		Soundex sdxFunc = new Soundex();
		BKFunction<Record,String> f1 = r -> ((String) r.getRecordAttributes().get("name")).substring(0, 4);
		BKFunction<Record,String> f2 = r -> ((String) r.getRecordAttributes().get("name")).substring(0, 2);
		BKFunction<Record,String> f3 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("name")).split(" ")[0]);
		BKFunction<Record,String> f4 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("name")).split(" ")[1]);
		BKFunction<Record,String> f5 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("name")).substring(0,3));
		BKFunction<Record,String> f6 = r -> ((String) r.getRecordAttributes().get("name")).substring(((String) r.getRecordAttributes().get("name")).indexOf(" ")+1, ((String) r.getRecordAttributes().get("name")).indexOf(" ")+3);
		BKFunction<Record,String> f7 = r -> ((String) r.getRecordAttributes().get("name")).substring(((String) r.getRecordAttributes().get("name")).indexOf(" ")+1, ((String) r.getRecordAttributes().get("name")).indexOf(" ")+4);
		BKFunction<Record,String> f8 = r -> ((String) r.getRecordAttributes().get("name")).substring(((String) r.getRecordAttributes().get("name")).indexOf(" ")+1, ((String) r.getRecordAttributes().get("name")).indexOf(" ")+5);
		BKFunction<Record,String> f9 = r -> ((String) r.getRecordAttributes().get("name")).substring(1, 3);
		BKFunction<Record,String> f11 = r -> ((String) r.getRecordAttributes().get("name")).split(" ")[0];
		BKFunction<Record,String> f12 = r -> ((String) r.getRecordAttributes().get("name")).split(" ")[1];
	}

	private static void generateBKsForCora() {
			Soundex sdxFunc = new Soundex();
			BKFunction<Record,String> f1 = r -> ((String) r.getRecordAttributes().get("title")).substring(0, 4);
			BKFunction<Record,String> f2 = r -> ((String) r.getRecordAttributes().get("title")).substring(0, 5);
			BKFunction<Record,String> f3 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("title")).split(" ")[0]);
			BKFunction<Record,String> f4 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("title")).split(" ")[1]);
			BKFunction<Record,String> f5 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("title")).split(" ")[2]);
			BKFunction<Record,String> f6 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("authors")).split(" ")[0]);
			BKFunction<Record,String> f7 = r -> ((String) r.getRecordAttributes().get("authors")).split(" ")[0];
			BKFunction<Record,String> f8 = r -> ((String) r.getRecordAttributes().get("title")).split(" ")[0];
			BKFunction<Record,String> f9 = r -> ((String) r.getRecordAttributes().get("title")).split(" ")[1];
			BKFunction<Record,String> f10 = r -> ((String) r.getRecordAttributes().get("title")).split(" ")[2];
			BKFunction<Record,String> f11 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("title")).substring(0,4));
	}

	private static void generateBKsForDBLPM4() {
	Soundex sdxFunc = new Soundex();
	BKFunction<Record,String> f1 = r -> ((String) r.getRecordAttributes().get("title")).substring(0, 2);
	BKFunction<Record,String> f2 = r -> ((String) r.getRecordAttributes().get("title")).substring(0, 4);
	BKFunction<Record,String> f3 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("title")).split(" ")[0]);
	BKFunction<Record,String> f4 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("title")).split(" ")[1]);
	BKFunction<Record,String> f5 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("title")).substring(0,3));
	BKFunction<Record,String> f6 = r -> ((String) r.getRecordAttributes().get("title")).substring(((String) r.getRecordAttributes().get("title")).indexOf(" ")+1, ((String) r.getRecordAttributes().get("title")).indexOf(" ")+3);
	BKFunction<Record,String> f7 = r -> ((String) r.getRecordAttributes().get("title")).substring(((String) r.getRecordAttributes().get("title")).indexOf(" ")+1, ((String) r.getRecordAttributes().get("title")).indexOf(" ")+4);
	BKFunction<Record,String> f8 = r -> ((String) r.getRecordAttributes().get("title")).substring(((String) r.getRecordAttributes().get("title")).indexOf(" ")+1, ((String) r.getRecordAttributes().get("title")).indexOf(" ")+5);
	BKFunction<Record,String> f9 = r -> ((String) r.getRecordAttributes().get("title")).substring(0, 3);
	BKFunction<Record,String> f10 = r -> sdxFunc.encode(((String) r.getRecordAttributes().get("title")).substring(0,5));
	BKFunction<Record,String> f11 = r -> ((String) r.getRecordAttributes().get("title")).split(" ")[0];
	BKFunction<Record,String> f12 = r -> ((String) r.getRecordAttributes().get("title")).split(" ")[1];
	}

	private static void generateBKsForDS3() {
				
		BKFunction<Record,String> f1 = r -> ((String) r.getRecordAttributes().get("title")).substring(0, 4);
		BKFunction<Record,String> f2 = r -> ((String) r.getRecordAttributes().get("title")).substring(0, 5);
		BKFunction<Record,String> f3 = r -> ((String) r.getRecordAttributes().get("title")).substring(0, 6);
		BKFunction<Record,String> f4 = r -> ((String) r.getRecordAttributes().get("title")).split(" ")[2];
		BKFunction<Record,String> f5 = r -> ((String) r.getRecordAttributes().get("title")).split(" ")[0];
		BKFunction<Record,String> f6 = r -> ((String) r.getRecordAttributes().get("title")).split(" ")[1];
		BKFunction<Record,String> f7 = r -> ((String) r.getRecordAttributes().get("authors")).substring(0, 4);
		BKFunction<Record,String> f8 = r -> ((String) r.getRecordAttributes().get("authors")).split(" ")[0];
		BKFunction<Record,String> f9 = r -> ((String) r.getRecordAttributes().get("authors")).split(" ")[1];
		BKFunction<Record,String> f10 = r -> ((String) r.getRecordAttributes().get("title")).substring(0, 7);
		BKFunction<Record,String> f11 = r -> ((String) r.getRecordAttributes().get("venue")).split(" ")[1];
		BKFunction<Record,String> f12 = r -> ((String) r.getRecordAttributes().get("venue")).split(" ")[0];
				
	}

	private static void generateBKsForFebrl() {
	BKFunction<Record,String> f1 = r -> ((String) r.getRecordAttributes().get("name")).substring(0, 2);
	BKFunction<Record,String> f2 = r -> ((String) r.getRecordAttributes().get("name")).substring(0, 3);
	BKFunction<Record,String> f3 = r -> ((String) r.getRecordAttributes().get("name")).split(" ")[0];
	BKFunction<Record,String> f4 = r -> ((String) r.getRecordAttributes().get("name")).substring(0, 5);
	BKFunction<Record,String> f5 = r -> ((String) r.getRecordAttributes().get("name")).substring(1, 4);
	BKFunction<Record,String> f6 = r -> ((String) r.getRecordAttributes().get("city")).substring(0, 3);
	BKFunction<Record,String> f7 = r -> ((String) r.getRecordAttributes().get("surname")).substring(0, 2);
	BKFunction<Record,String> f8 = r -> ((String) r.getRecordAttributes().get("state")).substring(0, 2);
	BKFunction<Record,String> f9 = r -> ((String) r.getRecordAttributes().get("surname")).substring(0, 3);
	BKFunction<Record,String> f10 = r -> ((String) r.getRecordAttributes().get("city")).substring(0, 2);
	BKFunction<Record,String> f11 = r -> ((String) r.getRecordAttributes().get("street")).split(" ")[0];
	BKFunction<Record,String> f12 = r -> ((String) r.getRecordAttributes().get("name")).substring(0, 4);
	}

private static void generateBKsForNCV() {
	BKFunction<Record,String> f1 = r -> ((String) r.getRecordAttributes().get("givenname")).substring(0, 3);
	BKFunction<Record,String> f2 = r -> ((String) r.getRecordAttributes().get("givenname")).substring(0, 4);
	BKFunction<Record,String> f3 = r -> ((String) r.getRecordAttributes().get("givenname")).substring(1,5);
	BKFunction<Record,String> f4 = r -> ((String) r.getRecordAttributes().get("givenname")).substring(2, 6);
	BKFunction<Record,String> f5 = r -> ((String) r.getRecordAttributes().get("surname")).substring(0, 3);
	BKFunction<Record,String> f6 = r -> ((String) r.getRecordAttributes().get("surname")).substring(0, 4);
	BKFunction<Record,String> f7 = r -> ((String) r.getRecordAttributes().get("surname")).substring(1,5);
	BKFunction<Record,String> f8 = r -> ((String) r.getRecordAttributes().get("suburb")).substring(0,4);
	BKFunction<Record,String> f9 = r -> ((String) r.getRecordAttributes().get("suburb")).substring(1,5);
	BKFunction<Record,String> f10 = r -> ((String) r.getRecordAttributes().get("postcode")).substring(0,5);
	BKFunction<Record,String> f11 = r -> ((String) r.getRecordAttributes().get("postcode")).substring(0,3);
	BKFunction<Record,String> f12 = r -> ((String) r.getRecordAttributes().get("postcode")).substring(0,4);
}

private static void generateBKsForDS100k() {
	BKFunction<Record,String> f1 = r -> ((String) r.getRecordAttributes().get("given_name")).substring(0, 3);
	BKFunction<Record,String> f2 = r -> ((String) r.getRecordAttributes().get("given_name")).split(" ")[0];
	BKFunction<Record,String> f3 = r -> ((String) r.getRecordAttributes().get("given_name")).substring(0, 2);
	BKFunction<Record,String> f4 = r -> ((String) r.getRecordAttributes().get("surname")).substring(0, 3);
	BKFunction<Record,String> f5 = r -> ((String) r.getRecordAttributes().get("surname")).substring(0, 2);
	BKFunction<Record,String> f6 = r -> ((String) r.getRecordAttributes().get("address_1")).substring(0, 3);
	BKFunction<Record,String> f7 = r -> ((String) r.getRecordAttributes().get("address_2")).substring(0, 2);
	BKFunction<Record,String> f8 = r -> ((String) r.getRecordAttributes().get("soc_sec_id")).substring(0,3);
	BKFunction<Record,String> f9 = r -> ((String) r.getRecordAttributes().get("age")).substring(0,2);
	BKFunction<Record,String> f10 = r -> ((String) r.getRecordAttributes().get("phone_number")).substring(0,3);
	BKFunction<Record,String> f11 = r -> ((String) r.getRecordAttributes().get("soc_sec_id")).substring(0, 4);
	BKFunction<Record,String> f12 = r -> ((String) r.getRecordAttributes().get("state")).substring(0,3);
}


}
