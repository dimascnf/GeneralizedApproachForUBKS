package blockinggoal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import blockingkey.BlockingKey;
import blockingscheme.BlockingKeyCombinationCriteria;
import blockingscheme.BlockingScheme;
import record.Dataset;
import record.Record;
import record.RecordPair;
import traininset.TrainingSet;

public class BlockingGoalImplementations {
	
public static Double objFunctionTPbyFP(TrainingSet trainingSet, BlockingScheme s) {
		
		int TP = 0, FP = 0;
		
		for (RecordPair pair: trainingSet.getPositiveSet()) {
			if (s.coveredBySchema(pair)) {
					TP++;
			}
		}
		
		for (RecordPair pair: trainingSet.getNegativeSet()) {
			if (s.coveredBySchema(pair)) {
					FP++;
			}
		}
		
		int numberOfKeys = s.getNumberOfBlockingKeys();
		double targetKeys = 3.0;
		return ((numberOfKeys>targetKeys?targetKeys:numberOfKeys)/targetKeys)*TP/((FP>0?FP:1));
		
		//other blocking goals
		
		//return (double) (TP/((FP>0?FP:1)));
		//return ((numberOfKeys>targetKeys?targetKeys:numberOfKeys)/targetKeys)*(TP-2*FP);
		//return (double) (TP-FP);
		//return (double) (TP*TP/(FP>0?FP:1));
		//return (double) ((numberOfKeys>targetKeys?targetKeys:numberOfKeys)/targetKeys)*(TP*TP/(FP>0?FP:1));
	}

	public static Double objFunctionForRealTimeER(Dataset dataset, 
			TrainingSet trainingSet, 
			BlockingScheme s, double referenceAvgBlockSize,
			double referenceBlockSizeVar) {
	
		HashMap<String, List<Record>> blockingResult = new HashMap<String, List<Record>>();
		
		int TP = 0, FP = 0;
		
		for (RecordPair pair: trainingSet.getPositiveSet()) {
			if (s.coveredBySchema(pair)) {
					TP++;
			}
		}
		
		for (RecordPair pair: trainingSet.getNegativeSet()) {
			if (s.coveredBySchema(pair)) {
					FP++;
			}
		}
		
		double normalizedAvg = 0;
		for (Record r: dataset.getRecords()) {
			if (s.getCriteria() == BlockingKeyCombinationCriteria.CONJUNCTION) {
				StringBuilder bkv = new StringBuilder();
				for (BlockingKey k : s.getSchemeKeys().iterator().next()) {
					bkv.append(k.getBKV(r));
				}
				if (blockingResult.containsKey(bkv.toString()))
					blockingResult.get(bkv.toString()).add(r);
				else {
					List<Record> block = new ArrayList<Record>();
					block.add(r);
					blockingResult.put(bkv.toString(), block);
				}
			} else if(s.getCriteria() == BlockingKeyCombinationCriteria.DISJUNCTION) {
				for (BlockingKey k : s.getSchemeKeys().iterator().next()) {
					String bkv = null;
					try{
						bkv = k.getBKV(r);
					} catch (Exception e) {
						continue;
					}
					if (blockingResult.containsKey(bkv.toString()))
						blockingResult.get(bkv.toString()).add(r);
					else {
						List<Record> block = new ArrayList<Record>();
						block.add(r);
						blockingResult.put(bkv.toString(), block);
					}
				}
			}
		}
		
		double avgBlockSizes = 0;
		for (List<Record> block : blockingResult.values()) {
			avgBlockSizes += block.size();
		}
		
		avgBlockSizes = avgBlockSizes / blockingResult.size();
		
		normalizedAvg = Double.min(referenceAvgBlockSize / avgBlockSizes,1);
		
		double var = 0;
		for (List<Record> block : blockingResult.values()) {
			var += Math.pow(block.size() - avgBlockSizes,2);
		}
		
		var = var/blockingResult.values().size();
		
		double normalizedVar = Double.min(referenceBlockSizeVar/var,1);
		
		double normalizedCP = TP / trainingSet.getPositiveSet().size();
		double normalizedCN = 1-(FP / trainingSet.getNegativeSet().size());
		
		int numberOfKeys = s.getNumberOfBlockingKeys();
		double targetKeys = 3.0;
		double w = ((numberOfKeys>targetKeys?targetKeys:numberOfKeys)/targetKeys);
		//double w=1;
		
		return w* ( normalizedCP + normalizedCN + normalizedAvg + normalizedVar );
		
	}

}
