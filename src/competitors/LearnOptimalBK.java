package competitors;

import java.util.HashSet;
import java.util.Set;

import blockinggoal.BlockingGoalInput;
import blockinggoal.GenericBlockingGoal;
import blockingkey.BlockingKey;
import blockingscheme.BlockingKeyCombinationCriteria;
import blockingscheme.BlockingScheme;
import record.Dataset;
import record.RecordPair;
import traininset.TrainingSet;

/**
 * Competitor algorithm
 */
public class LearnOptimalBK {
	
	public BlockingScheme learnOptimalBKForCustomScenario(Dataset[] datasets, HashSet<BlockingKey> K,
			TrainingSet trainingSet, int numberOfKeys, int nm, 
			BlockingKeyCombinationCriteria c,
			GenericBlockingGoal goal) {
		
		BlockingScheme optimizedScheme = new BlockingScheme();
		optimizedScheme.setCriteria(c);
		
		BlockingKey bestKey = null;
		
		BlockingGoalInput blockingGoalInput = new BlockingGoalInput();
		blockingGoalInput.setTrainingSet(trainingSet);
		blockingGoalInput.setDataset(datasets[0]);
		
		BlockingScheme s = new BlockingScheme();
		s.setCriteria(c);
		
		//Remove the blocking keys from K that cover more than nm record pairs in trainingSet.getNegativeSet()
		for (BlockingKey key: K) {
			int coveredPairs = 0;
			for (RecordPair pair: trainingSet.getNegativeSet())
				if (key.isCovered(pair))
					coveredPairs++;
			if (coveredPairs >= nm)
				K.remove(key);
		}
		
		for (int i=0; i<numberOfKeys; i++) {
			
			Double bestQuality = Double.NEGATIVE_INFINITY;
		
			for (BlockingKey key: K) {
				
				s.addInitialBlockingKey(key);
				blockingGoalInput.setScheme(s);
				
				Double currentQuality = goal.objFunction(blockingGoalInput);
				
				if (currentQuality > bestQuality) {
					bestQuality = currentQuality;
					bestKey = key;
				}
				
				s.removeBlockingKey(key);
			}
			
			if (optimizedScheme.isEmpty())
				optimizedScheme.addInitialBlockingKey(bestKey);
			else optimizedScheme.expand(bestKey);
			
			K.remove(bestKey);
			
			Set<RecordPair> pairsToRemove = new HashSet<RecordPair>();
			
			//Remove the pairs in the Positive Examples that are covered by bestKey
			for (RecordPair pair: trainingSet.getPositiveSet())
				if (bestKey.isCovered(pair))
					pairsToRemove.remove(pair);
			
			for (RecordPair pair: pairsToRemove)
				trainingSet.getPositiveSet().remove(pair);
			
		}
		
		return optimizedScheme;
		
	}
}
