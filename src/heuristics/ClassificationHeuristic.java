package heuristics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import blockingkey.BlockingKey;
import enums.BlockingGraphUpdateStrategy;
import record.RecordPair;
import traininset.Classification;
import traininset.TrainingSet;
import record.Record;

public class ClassificationHeuristic {
	
	/**
	 * 
	 * @param P set of record pairs
	 * @param K set of input blocking keys
	 * @param ts target training set size
	 * @param beta parameter used to influence the thresholds for classifying the input pairs as match or non match 
	 * @param blockingGraphUpdateStrategy strategy to update the extended blocking graph
	 * @return
	 */
	public TrainingSet generateTrainingSets(Set<RecordPair> P, Set<BlockingKey> K, 
			int ts, double beta, BlockingGraphUpdateStrategy blockingGraphUpdateStrategy) {
		
		HashMap<String,Double> BKtruePositiveRates = new HashMap<String, Double>();
		HashMap<String,Double> BKtrueNegativeRates = new HashMap<String, Double>();
		
		for (BlockingKey bk : K) {
			BKtruePositiveRates.put(bk.GetBKName(), .0);
			BKtrueNegativeRates.put(bk.GetBKName(), .0);
		}
		
		TrainingSet trainingSet = new TrainingSet();
		
		//iteration count
		int i = 1;
		
		while ((trainingSet.getPositiveSet().size() + trainingSet.getNegativeSet().size()) < ts) {
			
			Set<RecordPair> pairsToRemove = new HashSet<RecordPair>();
			
			Set<RecordPair> matchesOfCurrentIteration = new HashSet<RecordPair>();
			Set<RecordPair> nonMatchesOfCurrentIteration = new HashSet<RecordPair>();
			
			for (RecordPair pair : P) {
				if (trainingSet.getPositiveSet().size() < ts/2 && pair.getPositiveWeight() > -1*beta*pair.getNegativeWeight()
						&& pair.getNegativeWeight()<0) {
					trainingSet.addPositivePair(pair);
					pairsToRemove.add(pair);
					matchesOfCurrentIteration.add(pair);
				} else if (trainingSet.getNegativeSet().size() < ts/2 && pair.getNegativeWeight() < -1*beta*pair.getPositiveWeight()
						&& pair.getPositiveWeight()>0) {
					trainingSet.addNegativePair(pair);
					pairsToRemove.add(pair);
					nonMatchesOfCurrentIteration.add(pair);
				}
			}
			
			//avoids error produced by manipulating the P set inside the for each statement
			P.removeAll(pairsToRemove);
			if (!nonMatchesOfCurrentIteration.isEmpty() && !matchesOfCurrentIteration.isEmpty())
				updateBlockingGraph(P,K,matchesOfCurrentIteration,
						nonMatchesOfCurrentIteration, BKtruePositiveRates, 
						BKtrueNegativeRates, blockingGraphUpdateStrategy,
						trainingSet.getPositiveSet().size(), trainingSet.getNegativeSet().size(),i);
			else beta = beta - 0.5;
			i++;
			
		}
		
		return trainingSet;
	}

	private void updateBlockingGraph(Set<RecordPair> P, Set<BlockingKey> K, Set<RecordPair> matchesOfCurrentIteration,
			Set<RecordPair> nonMatchesOfCurrentIteration, HashMap<String,Double> BKtruePositiveRates, 
			HashMap<String,Double> BKtrueNegativeRates, BlockingGraphUpdateStrategy strategy,
			int positiveSetSize, int negativeSetSize, int iteration) {
		
		if (strategy == BlockingGraphUpdateStrategy.ENTIRE_TRAINING_SET) {
		
			for (BlockingKey bk : K) {
				for (RecordPair matchPair : matchesOfCurrentIteration) {
						if (bk.getBKV(matchPair.getR1()).equals(bk.getBKV(matchPair.getR2())))
							BKtruePositiveRates.put(bk.GetBKName(), BKtruePositiveRates.get(bk.GetBKName())+1);
				}
				for (RecordPair nonMatchPair : nonMatchesOfCurrentIteration) {
						if (!bk.getBKV(nonMatchPair.getR1()).equals(bk.getBKV(nonMatchPair.getR2())))
							BKtrueNegativeRates.put(bk.GetBKName(), BKtrueNegativeRates.get(bk.GetBKName())+1);
				}
				BKtrueNegativeRates.put(bk.GetBKName(), BKtrueNegativeRates.get(bk.GetBKName())/negativeSetSize);
				BKtruePositiveRates.put(bk.GetBKName(), BKtruePositiveRates.get(bk.GetBKName())/positiveSetSize);
			}
			
			for (RecordPair pair : P) {
				double truePositiveRateSum = 0;
				double trueNegativeRateSum = 0;
				
				for (String bk : pair.getCommonBKs()) {
					truePositiveRateSum += BKtruePositiveRates.get(bk);
				}
				pair.setPositiveWeight(truePositiveRateSum);
				
				Set<String> notCommonBKs = new HashSet<String>();
				notCommonBKs.addAll(BKtrueNegativeRates.keySet());
				notCommonBKs.removeAll(pair.getCommonBKs());
				
				for (String bk : notCommonBKs) {
					trueNegativeRateSum += BKtrueNegativeRates.get(bk);
				}
				pair.setNegativeWeight(-1*trueNegativeRateSum);
			}
		
		} 
		
	}

}
