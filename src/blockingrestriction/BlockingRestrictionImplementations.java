package blockingrestriction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import blockingkey.BlockingKey;
import blockingscheme.BlockingKeyCombinationCriteria;
import blockingscheme.BlockingScheme;
import record.Dataset;
import record.Record;
import record.RecordPair;
import staticconter.StaticCounter;
import traininset.TrainingSet;

public class BlockingRestrictionImplementations {
	
	public static boolean exceedsMaxNumberOfKeys(BlockingScheme s, int lk) {
		//should only be used using conjunction or disjunction
		return s.getSchemeKeys().iterator().next().size() > lk;
	}
	
	public static boolean exceedsMaxNumberOfFalsePositives(
			TrainingSet trainingSet, BlockingScheme s, int nm) {

		int FP = 0;
		
		//calculating false positive pairs
		for (RecordPair pair: trainingSet.getNegativeSet()) {
			if (s.coveredBySchema(pair)) {
					FP++;
			}
		}
		
		return FP < nm;
	}
	
	public static boolean exceedsMaxBlockSize(Dataset[] dataset, BlockingScheme s, int sm) {
		HashMap<String, List<Record>> blockingResult = new HashMap<String, List<Record>>();
		
		if (dataset.length == 1) {
			for (Record r : dataset[0].getRecords()) {
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
						String bkv = k.getBKV(r);
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
		}
		
		for (List<Record> block: blockingResult.values()) {
			if (block.size() > sm)
				return false;
		}
		return true;
	}
	
public static boolean optimizedExceedsMaximumAggCardinality(Dataset[] dataset, BlockingScheme s, BigDecimal maxAggCard) {
		
		HashMap<String, List<Record>> blockingResult = new HashMap<String, List<Record>>();
		
		if (dataset.length == 1) {
			for (Record r : dataset[0].getRecords()) {
				if (s.getCriteria() == BlockingKeyCombinationCriteria.CONJUNCTION) {
					StringBuilder bkv = new StringBuilder();
					for (BlockingKey k : s.getSchemeKeys().iterator().next()) {
						try {
							bkv.append(k.getBKV(r));
						} catch (Exception e) {
							continue;
						}
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
						try {
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
				} else if(s.getCriteria() == BlockingKeyCombinationCriteria.DNF) {
					for (Set<BlockingKey> conjunction: s.getSchemeKeys()) {
						StringBuilder bkv = new StringBuilder();
						for (BlockingKey k : conjunction) {
							bkv.append(k.getBKV(r));
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
		}
		
		BigDecimal sumAggCard = BigDecimal.valueOf(0);
		
		for (String bkv: blockingResult.keySet()) {
			List<Record> blockRecords = blockingResult.get(bkv);
			if (blockRecords.size()>1) {
				BigDecimal blockAgg = BigDecimal.valueOf(blockRecords.size()).multiply(BigDecimal.valueOf(blockRecords.size()-1).divide(BigDecimal.valueOf(2)));
				sumAggCard = sumAggCard.add(blockAgg);
			}
		}
		
		if (sumAggCard.compareTo(maxAggCard) == 1) {
			StaticCounter.cont++;
		}
		
		return sumAggCard.compareTo(maxAggCard) == -1;
	}
	
public static boolean exceedsMaximumAggCardinality(Dataset[] dataset, BlockingScheme s, long maxAggCard) {
		
		HashMap<String, List<Record>> blockingResult = new HashMap<String, List<Record>>();
		
		if (dataset.length == 1) {
			for (Record r : dataset[0].getRecords()) {
				if (s.getCriteria() == BlockingKeyCombinationCriteria.CONJUNCTION) {
					StringBuilder bkv = new StringBuilder();
					for (BlockingKey k : s.getSchemeKeys().iterator().next()) {
						try {
							bkv.append(k.getBKV(r));
						} catch (Exception e) {
							continue;
						}
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
						try {
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
				} else if(s.getCriteria() == BlockingKeyCombinationCriteria.DNF) {
					for (Set<BlockingKey> conjunction: s.getSchemeKeys()) {
						StringBuilder bkv = new StringBuilder();
						for (BlockingKey k : conjunction) {
							bkv.append(k.getBKV(r));
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
		}
		
		BigDecimal sumAggCard = BigDecimal.valueOf(0);
		
		for (String bkv: blockingResult.keySet()) {
			List<Record> blockRecords = blockingResult.get(bkv);
			if (blockRecords.size()>1) {
				BigDecimal blockAgg = BigDecimal.valueOf(blockRecords.size()).multiply(BigDecimal.valueOf(blockRecords.size()-1).divide(BigDecimal.valueOf(2)));
				sumAggCard = sumAggCard.add(blockAgg);
			}
		}
		
		if (sumAggCard.longValue() > maxAggCard) {
			StaticCounter.cont++;
		}
		
		return sumAggCard.longValue() <= maxAggCard;
	}
	
	public static boolean exceedsMaxNumberOfComparisonsBySingleRecord(Dataset[] dataset, 
			BlockingScheme s, int maxComparisonsBySingleRecord) {
		
		HashMap<String, List<Record>> blockingResult = new HashMap<String, List<Record>>();
		
		if (dataset.length == 1) {
			for (Record r : dataset[0].getRecords()) {
				if (s.getCriteria() == BlockingKeyCombinationCriteria.CONJUNCTION) {
					StringBuilder bkv = new StringBuilder();
					for (BlockingKey k : s.getSchemeKeys().iterator().next()) {
						try {
							bkv.append(k.getBKV(r));
						} catch (Exception e) {
							continue;
						}
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
						try {
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
				} else if(s.getCriteria() == BlockingKeyCombinationCriteria.DNF) {
					for (Set<BlockingKey> conjunction: s.getSchemeKeys()) {
						StringBuilder bkv = new StringBuilder();
						for (BlockingKey k : conjunction) {
							try {
								bkv.append(k.getBKV(r));
							} catch (Exception e) {
								continue;
							}
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
		}
		
		HashMap<Record, Integer> comparisonsByRecord = new HashMap<Record, Integer>();
		Set<RecordPair> pairsToCompare = new HashSet<RecordPair>();
		
		for (List<Record> block: blockingResult.values()) {
			for (Record r1: block) {
				for (Record r2: block) {
					RecordPair pair = new RecordPair(r1, r2);
					if (!r1.equals(r2) && !pairsToCompare.contains(pair)) {
						if (comparisonsByRecord.containsKey(r1)) {
							comparisonsByRecord.put(r1, comparisonsByRecord.get(r1)+1);
						} else {
							comparisonsByRecord.put(r1, 1);
						}
						if (comparisonsByRecord.containsKey(r2)) {
							comparisonsByRecord.put(r2, comparisonsByRecord.get(r2)+1);
						} else {
							comparisonsByRecord.put(r2, 1);
						}
					}
				}
			}
		}
		
		for (Integer c : comparisonsByRecord.values()) {
			if (c > maxComparisonsBySingleRecord) {
				StaticCounter.cont++;
				return false;
			}
		}
		
		return true;
	}
	
}
