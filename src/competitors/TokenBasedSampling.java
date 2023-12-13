package competitors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import record.Record;
import record.Dataset;
import record.RecordPair;

public class TokenBasedSampling {
	
	public Set<RecordPair> tokenBasedSampling(Dataset[] datasets, 
			String[] attArray, 
			int size,
			int minimumTokenSize){
		HashMap<String, Set<Record>> tokenBlockingResult = new HashMap<String, Set<Record>>();
		
		//deduplication
		if (datasets.length == 1) {
			for (Record r : datasets[0].getRecords()) {
				StringBuilder sb = new StringBuilder();
				for (String att: attArray)
					sb.append((String) r.getRecordAttributes().get(att)+" ");
				String[] tokens = sb.toString().split(" ");
				for (String token: tokens) {
					if (token.length() > minimumTokenSize)
					if (tokenBlockingResult.containsKey(token)) {
						if (!tokenBlockingResult.get(token).contains(r)) {
							tokenBlockingResult.get(token).add(r);
						}
					} else {
							Set<Record> block = new HashSet<Record>();
							block.add(r);
							tokenBlockingResult.put(token, block);
						}
					}
				}
		
			Set<RecordPair> pairs = new HashSet<RecordPair>();
		
			for (Set<Record> block: tokenBlockingResult.values()) {
				for (Record r1: block) {
					for (Record r2: block) {
						if (pairs.size() < size && 
								!r1.equals(r2) && !pairs.contains(new RecordPair(r1, r2))) {
							pairs.add(new RecordPair(r1,r2));
						}
					}
				}
				if (pairs.size() == size)
					break;
			}
			
			return pairs;
		} else {
			//Record Linkage
			for (int i=0; i<2; i++)
				for (Record r : datasets[i].getRecords()) {
					StringBuilder sb = new StringBuilder();
					for (String att: attArray)
						sb.append((String) r.getRecordAttributes().get(att)+" ");
					String[] tokens = sb.toString().split(" ");
					for (String token: tokens) {
						if (tokenBlockingResult.containsKey(token)) {
							if (!tokenBlockingResult.get(token).contains(r)) {
								tokenBlockingResult.get(token).add(r);
							}
						} else {
								Set<Record> block = new HashSet<Record>();
								block.add(r);
								tokenBlockingResult.put(token, block);
							}
						}
					}
		
			Set<RecordPair> pairs = new HashSet<RecordPair>();
		
			for (Set<Record> block: tokenBlockingResult.values()) {
				for (Record r1: block) {
					for (Record r2: block) {
						if (!r1.getDatasetId().equals(r2.getDatasetId()) &&
								pairs.size() < size && 
								!r1.equals(r2) && !pairs.contains(new RecordPair(r1, r2))) {
							pairs.add(new RecordPair(r1,r2));
						}
					}
				}
				if (pairs.size() == size)
					break;
			}
			
			return pairs;
		}
	}
	
		
			
}
