package competitors;

import java.util.HashSet;
import java.util.Set;

import blockinggoal.BlockingGoalInput;
import blockinggoal.GenericBlockingGoal;
import blockingkey.BlockingKey;
import blockingrestriction.BlockingRestrictionInput;
import blockingrestriction.GenericBlockingRestriction;
import blockingrestriction.RestrictableType;
import blockingscheme.BlockingKeyCombinationCriteria;
import blockingscheme.BlockingScheme;
import record.Dataset;
import traininset.TrainingSet;

public class GreedyUBKS {
	
	public BlockingScheme topBestKeysForCustomScenario(Dataset[] datasets, HashSet<BlockingKey> K,
			TrainingSet trainingSet, int numberOfKeys, 
			GenericBlockingGoal goal,
			Set<GenericBlockingRestriction> Psi,
			BlockingKeyCombinationCriteria c) {
		
		BlockingScheme optimizedScheme = new BlockingScheme();
		optimizedScheme.setCriteria(c);
		
		HashSet<BlockingKey> forbiddenKeys = new HashSet<BlockingKey>();
		
		BlockingRestrictionInput blockingRestrictionInput = new BlockingRestrictionInput();
		BlockingScheme s = new BlockingScheme();
		s.setCriteria(c);
		blockingRestrictionInput.setScheme(optimizedScheme);
		
		BlockingKey bestKey = null;
		
		BlockingGoalInput blockingGoalInput = new BlockingGoalInput();
		blockingGoalInput.setTrainingSet(trainingSet);
		blockingGoalInput.setDataset(datasets[0]);
		
		int i=0;
		while (i < numberOfKeys) {
			
			Double bestQuality = Double.NEGATIVE_INFINITY;
			bestKey = null;
			
			if (K.isEmpty())
				break;
		
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
			
			boolean honorsRestrictions = true;
			for (GenericBlockingRestriction restriction: Psi) {
				if (restriction.getType() == RestrictableType.TRAINING_SET) {
					blockingRestrictionInput.setRestrictable(trainingSet);
					if (!restriction.honorsRestriction(blockingRestrictionInput)) {
						honorsRestrictions = false;
						break;
					}
				} else if (restriction.getType() == RestrictableType.DATASET) {
						blockingRestrictionInput.setRestrictable(datasets[0]);
					if (!restriction.honorsRestriction(blockingRestrictionInput)) {
						honorsRestrictions = false;
						break;
					}
				} 
			}
			
			if (!honorsRestrictions) {
				optimizedScheme.removeBlockingKey(bestKey);
				forbiddenKeys.add(bestKey);
				K.remove(bestKey);
				continue;
			}

			K.addAll(forbiddenKeys);
			forbiddenKeys.clear();
			K.remove(bestKey);
			
			i++;
		}
		
		return optimizedScheme;
		
	} 
	

}
