package heuristics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toCollection;

import blockingkey.BlockingKey;
import enums.WeightStrategy;
import record.Dataset;
import record.Record;
import record.RecordPair;

public class SamplingMethod {
	
	/**
	 * Sampling method for selecting record pairs to compose the training sets.
	 * @param dataset (set of records)
	 * @param K set of blocking keys 
	 * @param ts target sampling size
	 * @param v number of blocking key set variations on each similarity level
	 * @return set of record pairs
	 */
public Set<RecordPair> sampling(Dataset[] datasets, Set<BlockingKey> K, int ts, int v, double alpha){
	
		System.out.println(datasets[0].size() * alpha);
	
		//Visited keys
		Set<Set<String>> KVisited = new HashSet<Set<String>>();
		//Pairs of records
		Set<RecordPair> P = new HashSet<RecordPair>();
		
		int i=1;
		
		int n = ts / (1 + K.size());
		
		Set<RecordPair> blockingGraph = generateBlockingGraph(datasets,K,WeightStrategy.CBS,n, alpha);
		
		HashMap<Double, Set<RecordPair>> indexByCBSValue = generateIndexByCBSValue(blockingGraph);
		
		addInitialSample(P,indexByCBSValue,n);
		
		while (i < K.size()) {
			for (int j=0; j<v; j++) {
				addNRecordPairs(P,n/v,i,KVisited,indexByCBSValue);
			}
			i++;
		}
		
		addNRecordPairs(P,n,K.size(),KVisited,indexByCBSValue);
		
		return P;
	}

	public HashMap<Double, Set<RecordPair>> generateIndexByCBSValue(Set<RecordPair> blockingGraph){
		//CBS -> Set<RecordPair>
		HashMap<Double, Set<RecordPair>> indexByCBSValue = new HashMap<Double, Set<RecordPair>>();
		for (RecordPair pair: blockingGraph) {
			if (!indexByCBSValue.containsKey(pair.getCBSWeight())) {
				Set<RecordPair> pairs = new HashSet<RecordPair>();
				pairs.add(pair);
				indexByCBSValue.put(pair.getCBSWeight(), pairs);
			} else {
				indexByCBSValue.get(pair.getCBSWeight()).add(pair);
			}
		}
		
		return indexByCBSValue; 
	}
	
	public Set<RecordPair> randomSampling(Dataset[] datasets, Set<BlockingKey> K, int ts, int v, double alpha){
		
		//Visited keys
		Set<Set<String>> KVisited = new HashSet<Set<String>>();
		//Pairs of records
		Set<RecordPair> P = new HashSet<RecordPair>();
		
		int i=1;
		
		int n = ts / (1 + K.size());
		
		Set<RecordPair> blockingGraph = generateBlockingGraph(datasets,K,WeightStrategy.CBS,n,alpha);
		
		HashMap<Double, Set<RecordPair>> indexByCBSValue = generateIndexByCBSValue(blockingGraph);
		
		addInitialRandomSample(P,blockingGraph,n,indexByCBSValue);
		
		while (i < K.size()) {
			for (int j=0; j<v; j++) {
				addNRandomRecordPairs(P,blockingGraph,n/v,i,KVisited,indexByCBSValue);
			}
			i++;
		}
		
		addNRandomRecordPairs(P,blockingGraph,n,K.size(),KVisited,indexByCBSValue);
		
		return P;
	}
	
	private void addNRecordPairs(Set<RecordPair> P, int n, 
			int i, Set<Set<String>> kVisited, HashMap<Double, 
			Set<RecordPair>> indexByCBSValue) {

		Set<String> Ki = null;
		int count = 0;
		
		Double sharedBlocks = (Double) (i*1.0);
		
		if (indexByCBSValue.containsKey(sharedBlocks))	
			for (RecordPair pair: indexByCBSValue.get(sharedBlocks)) {
				
				if (!P.contains(pair)) {
				
					if (pair.getCommonBKVs() == null)
						pair.updateCommonBKVs();
					
					if (Ki == null && pair.getCBSWeight() == i) {
						if (!kVisited.contains(pair.getCommonBKs())) {
							Ki = pair.getCommonBKs();
							kVisited.add(Ki);
							P.add(pair);
							count++;
						}				
					} else if (!P.contains(pair) && pair.getCommonBKs().equals(Ki)) {
						P.add(pair);
						count++;
					} 
					if (count == n)
						break;
				}
			}
	}
	
	private void addNRandomRecordPairs(Set<RecordPair> P, Set<RecordPair> blockingGraph,  
			int n, int i, Set<Set<String>> kVisited, HashMap<Double, Set<RecordPair>> indexByCBSValue) {

		Set<String> Ki = null;
		int count = 0;
		
		Double numberOfSharedBlocks = (i*1.0);
		
		if (indexByCBSValue.containsKey(numberOfSharedBlocks)) {
		
			List<RecordPair> blockingGraphAsList = new ArrayList<RecordPair>();
			blockingGraphAsList.addAll(indexByCBSValue.get(numberOfSharedBlocks));
			
			int randomIdx = getRandomNumber(0,blockingGraphAsList.size());
			
			for (int j = randomIdx; j<blockingGraphAsList.size(); j++) {
				
				RecordPair pair = blockingGraphAsList.get(j);
				
				if (!P.contains(pair)) {
				
					if (pair.getCommonBKVs() == null)
						pair.updateCommonBKVs();
					
					if (Ki == null && pair.getCBSWeight() == i) {
						if (!kVisited.contains(pair.getCommonBKs())) {
							Ki = pair.getCommonBKs();
							kVisited.add(Ki);
							P.add(pair);
							count++;
						}				
					} else if (!P.contains(pair) && pair.getCommonBKs().equals(Ki)) {
						P.add(pair);
						count++;
					} 
					if (count == n)
						break;
				}
			}
		}
	}
	
	public static int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	private void addInitialRandomSample(Set<RecordPair> P, Set<RecordPair> blockingGraph, int n, HashMap<Double, Set<RecordPair>> indexByCBSValue) {
		
		Double numberOfSharedBlocks = .0;
		
		List<RecordPair> blockingGraphAsList = new ArrayList<RecordPair>();
		if (indexByCBSValue.get(numberOfSharedBlocks) == null)
			return;
		blockingGraphAsList.addAll(indexByCBSValue.get(numberOfSharedBlocks));
		
		int randomIdx = getRandomNumber(0,blockingGraphAsList.size());
		
		for (int i = randomIdx; i<blockingGraphAsList.size(); i++) {
			RecordPair pair = blockingGraphAsList.get(i);
			if (pair.getCBSWeight() == 0)
				P.add(pair);
			if (P.size() == n)
				break;
		}
	}

	private void addInitialSample(Set<RecordPair> P, 
			HashMap<Double, Set<RecordPair>> indexByCBSValue, 
			int n) {
		
		Double zero = (Double) 0.;
		
		if (indexByCBSValue.containsKey(zero))		
			for (RecordPair pair: indexByCBSValue.get(zero)) {
				if (pair.getCBSWeight() == 0)
					P.add(pair);
				if (P.size() == n)
					break;
			}
	}

	/**
	 * 
	 * @param datasets datasets
	 * @param K set of blocking keys
	 * @param w weightening strategy of the extended blocking graph 
	 * @param z number of blocking pairs that do not share blocks to be added in the extended blocking graph
	 * @return
	 */
	private Set<RecordPair> generateBlockingGraph(Dataset[] datasets, Set<BlockingKey> K,
			WeightStrategy w, int z, double alpha) {
		
		//deduplication
		if (datasets.length == 1) {
			//capped dataset (for speeding up)
			Set<Record> dataset = datasets[0].getRecords().stream()
				    .limit((long) (datasets[0].size()*alpha))
				    .collect(toCollection(LinkedHashSet::new));
			
			//full dataset
			//Set<Record> dataset = datasets[0].getRecords();
		
			//(r1.id,r2.id) -> (r1,r2)
			HashMap<String, RecordPair> blockingGraph = new HashMap<String, RecordPair>();
			//keyname+bkv -> {r1,r2,...}
			HashMap<String,Set<Record>> blocks = new HashMap<String,Set<Record>>();
	
			if (w == WeightStrategy.CBS) {
				
				for (BlockingKey k: K) {
					for (Record r: dataset) {
						String bkv = null;
						try {
							bkv = (String) k.getBKV(r);
						} catch (Exception e) {
							continue;
						}
						r.addBKV(bkv);
						
						if (!blocks.containsKey(bkv)) {
							HashSet<Record> block = new HashSet<Record>();
							block.add(r);
							blocks.put(bkv, block);
						} else {
							for (Record r2 : blocks.get(bkv)) {
								if (r.equals(r2))
									continue;
								RecordPair pair = new RecordPair(r,r2);
								if (blockingGraph.containsKey(pair.getIDs())) {
									blockingGraph.get(pair.getIDs()).setCBSWeight(blockingGraph.get(pair.getIDs()).getCBSWeight()+1);
								} else {
									blockingGraph.put(pair.getIDs(), pair);
									pair.setCBSWeight(1);
								}
							}		
							blocks.get(bkv).add(r);
						}
					}
					
				}
				
			}
			
			//adding record pairs that do not share blocks
			
			int cont = 0;
			for (String bkv1 : blocks.keySet()) {
				for (String bkv2 : blocks.keySet()) {
					if (!bkv1.equals(bkv2)) {
						Set<Record> rSet1 = null;
						Set<Record> rSet2 = null;
						try {
							rSet1 = blocks.get(bkv1);
							rSet2 = blocks.get(bkv2);
						} catch (Exception e) {
							continue;
						}
						
						for (Record r1: rSet1) {
							for (Record r2: rSet2) {
								if (r1.equals(r2))
									continue;
								RecordPair pair = new RecordPair(r1, r2);
								if (cont < z && !blockingGraph.containsKey(pair.getIDs())) {
									pair.setCBSWeight(0);
									pair.updateCommonBKVs();
									blockingGraph.put(pair.getIDs(), pair);
									cont++;
								}
									
							}
							if (cont >= z)
								break;
						}
					}
					if (cont >= z)
						break;
				}
				if (cont >= z)
					break;
			}
			
			for (RecordPair pair: blockingGraph.values()) {
				pair.setPositiveWeight(pair.getCBSWeight());
				pair.setNegativeWeight(-1*(K.size() - pair.getPositiveWeight()));
			}
			
			return new HashSet<RecordPair>(blockingGraph.values());
		
		} else { //record linkage
			
			//(r1.id,r2.id) -> (r1,r2)
			HashMap<String, RecordPair> blockingGraph = new HashMap<String, RecordPair>();
			//keyname+bkv -> {r1,r2,...}
			HashMap<String,Set<Record>> blocks = new HashMap<String,Set<Record>>();
	
			if (w == WeightStrategy.CBS) {
				for (int i=0; i<2;i++) {
					Set<Record> dataset = datasets[i].getRecords();
					for (BlockingKey k: K) {
						for (Record r: dataset) {
							
							String bkv = (String) k.getBKV(r);
							r.addBKV(bkv);
							
							if (!blocks.containsKey(bkv)) {
								HashSet<Record> block = new HashSet<Record>();
								block.add(r);
								blocks.put(bkv, block);
							} else {
								blocks.get(bkv).add(r);
								for (Record r2 : blocks.get(bkv)) {
									//only pairs from distinct datasets are added to the extended blocking graph
									if (r.equals(r2) || r.getDatasetId().equals(r2.getDatasetId()))
										continue;
									RecordPair pair = new RecordPair(r,r2);
									if (blockingGraph.containsKey(pair.getIDs())) {
										blockingGraph.get(pair.getIDs()).setCBSWeight(blockingGraph.get(pair.getIDs()).getCBSWeight()+1);
									} else {
										blockingGraph.put(pair.getIDs(), pair);
										pair.setCBSWeight(1);
									}
								}		
							}
						}
						
					}
					
				}
			}
			
			//adding record pairs that do not share blocks
			
			int cont = 0;
			for (String bkv1 : blocks.keySet()) {
				for (String bkv2 : blocks.keySet()) {
					if (!bkv1.equals(bkv2)) {
						Set<Record> rSet1 = blocks.get(bkv1);
						Set<Record> rSet2 = blocks.get(bkv2);
						
						for (Record r1: rSet1) {
							for (Record r2: rSet2) {
								if (r1.equals(r2) || r1.getDatasetId().equals(r2.getDatasetId()))
									continue;
								RecordPair pair = new RecordPair(r1, r2);
								if (cont < z && !blockingGraph.containsKey(pair.getIDs())) {
									pair.setCBSWeight(0);
									pair.updateCommonBKVs();
									blockingGraph.put(pair.getIDs(), pair);
									cont++;
								}
									
							}
							if (cont >= z)
								break;
						}
					}
					if (cont >= z)
						break;
				}
				if (cont >= z)
					break;
			}
			
			//calculating edge weights
			for (RecordPair pair: blockingGraph.values()) {
				pair.setPositiveWeight(pair.getCBSWeight());
				pair.setNegativeWeight(-1*(K.size() - pair.getPositiveWeight()));
			}
			
			return new HashSet<RecordPair>(blockingGraph.values());
		}
	}

}
