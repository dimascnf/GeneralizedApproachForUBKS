package heuristics;

import java.util.HashSet;
import java.util.Set;

import blockinggoal.BlockingGoal;
import blockinggoal.BlockingGoalInput;
import blockingkey.BlockingKey;
import blockingrestriction.BlockingRestrictionInput;
import blockingrestriction.GenericBlockingRestriction;
import blockingrestriction.RestrictableType;
import blockingscheme.BlockingKeyCombinationCriteria;
import blockingscheme.BlockingScheme;
import record.Dataset;
import record.DatasetPair;
import record.RecordPair;
import traininset.TrainingSet;

public class GeneralizedMetaheuristic {
	
	private int ITERATIONS = 100;
	
	public GeneralizedMetaheuristic() {
		
	}
	
	/**
	 * 
	 * @param datasets datasets
	 * @param K set of input blocking keys
	 * @param trainingSet set of training examples (record pairs + labels)
	 * @param C blocking key combination criteria
	 * @param goal blocking goal
	 * @param Psi set of blocking restrictions
	 * @return 
	 */
	public BlockingScheme UBKS(Dataset[] datasets, HashSet<BlockingKey> K,
	TrainingSet trainingSet, BlockingKeyCombinationCriteria C, 
	BlockingGoal<BlockingGoalInput, Double> goal, 
	Set<GenericBlockingRestriction> Psi
	) {
		
		HashSet<BlockingKey> Kopt = greedyInitialKeySet(K,C,trainingSet, datasets, goal, Psi);
		
		HashSet<BlockingKey> forbiddenKeys = new HashSet<BlockingKey>();
		
		BlockingScheme S_opt = createInitialBlockingScheme(Kopt,C);
		
		int it = 0;
		
		while (it < ITERATIONS && K.size() > 0) {
			
			BlockingKey k1 = localSearch(K,goal,Psi,trainingSet,C,datasets);
			
			boolean changed = replace(k1, Kopt,S_opt,K,goal,Psi,trainingSet,datasets);
			
			if (!changed && S_opt.getSchemeKeys().size() > 1)
				changed = shrink(Kopt,S_opt,K,goal,Psi,trainingSet,datasets);
			
			if (!changed)
				changed = expand(k1, Kopt,K, S_opt, goal,Psi,trainingSet,datasets);
			
			if (!changed) {
				forbiddenKeys.add(k1);
				K.remove(k1);
			} else {
				K.addAll(forbiddenKeys);
				forbiddenKeys.clear();
			}
			
			it++;
		}
		
		return S_opt;
	}

	private BlockingScheme createInitialBlockingScheme(HashSet<BlockingKey> kopt, BlockingKeyCombinationCriteria c) {
		
		BlockingScheme bs = new BlockingScheme();
		bs.addBlockingKeys(kopt);
		bs.setCriteria(c);
		
		return bs;
	}

	private boolean expand(BlockingKey k1, Set<BlockingKey> Kopt, HashSet<BlockingKey> K,
			BlockingScheme s_opt,
			BlockingGoal<BlockingGoalInput, Double> goal, 
			Set<GenericBlockingRestriction> Psi,
			TrainingSet trainingSet, Dataset[] datasets) {
		
		BlockingScheme schemeToModify = new BlockingScheme();
		schemeToModify.setCriteria(s_opt.getCriteria());
		schemeToModify.setSchemeKeys(copySet(s_opt.getSchemeKeys()));
		
		boolean changed = false;
		
		if (s_opt.getCriteria() == BlockingKeyCombinationCriteria.CONJUNCTION ||
				s_opt.getCriteria() == BlockingKeyCombinationCriteria.DISJUNCTION) {
			schemeToModify.expand(k1);
			changed = improveSchemaIfAdvantageousAndAllowed(schemeToModify,Psi,goal,s_opt,trainingSet, datasets);
			if (!changed)
				schemeToModify.removeBlockingKey(k1);
		} else if (s_opt.getCriteria() == BlockingKeyCombinationCriteria.DNF) {
			//trying to expand an existing conjunction in the DNF scheme
			for (HashSet<BlockingKey> keySet: schemeToModify.getSchemeKeys()) {
				keySet.add(k1);
				changed = improveSchemaIfAdvantageousAndAllowed(schemeToModify,Psi,goal,s_opt,trainingSet, datasets);
				if (changed)
					break;
				else{
					//undo expansion
					keySet.remove(k1);
				}
			}
			if (!changed) {
				//trying to add a new conjunction (initially composed by a single blocking key) 
				//in the current DNF scheme
				HashSet<BlockingKey> newConjunction = new HashSet<BlockingKey>();
				newConjunction.add(k1);
				schemeToModify.addBlockingKeys(newConjunction);
				changed = improveSchemaIfAdvantageousAndAllowed(schemeToModify,Psi,goal,s_opt,trainingSet, datasets);
				if (!changed) {
					//undo expansion
					schemeToModify.removeBlockingKeys(newConjunction);
				}
			}
		}
		
		if (changed) {
			K.remove(k1);
			Kopt.add(k1);
			s_opt.setSchemeKeys(schemeToModify.getSchemeKeys());
		}
		
		return changed;
	}

	private boolean shrink(Set<BlockingKey> Kopt, BlockingScheme s_opt, HashSet<BlockingKey> K,
			BlockingGoal<BlockingGoalInput, Double> goal, Set<GenericBlockingRestriction> Psi,
			TrainingSet trainingSet, Dataset[] datasets) {
		
		if (s_opt.getSchemeKeys().size() == 1)
			return false;
		
		boolean changed = false;
		
		BlockingScheme schemeToModify = new BlockingScheme();
		schemeToModify.setCriteria(s_opt.getCriteria());
		schemeToModify.setSchemeKeys(copySet(s_opt.getSchemeKeys()));
		
		for (HashSet<BlockingKey> keySet: schemeToModify.getSchemeKeys()) {
			
			//avoids shrinking a conjunction with size = 1
			if (keySet.size() == 1)
				continue;
			
			//avoids ConcurrentModificationException
			HashSet<BlockingKey> copyOfkeySet = new HashSet<BlockingKey>(keySet);
			for (BlockingKey key : copyOfkeySet) {
			
				schemeToModify.removeBlockingKey(keySet,key);
				
				changed = improveSchemaIfAdvantageousAndAllowed(schemeToModify,Psi,goal,s_opt,trainingSet, datasets);
				
				if (changed) {
					Kopt.remove(key);
					K.add(key);
					s_opt.setSchemeKeys(schemeToModify.getSchemeKeys());
					return changed;
				} else {
					//undo shrink
					schemeToModify.expand(keySet, key);
				}
			}
		}
		
		return changed;
	}
	
	private boolean improveSchemaIfAdvantageousAndAllowed(BlockingScheme modifiedScheme,
			Set<GenericBlockingRestriction> Psi, BlockingGoal<BlockingGoalInput, Double> goal, 
			BlockingScheme s_opt, TrainingSet trainingSet, Dataset[] datasets) {
		
		BlockingRestrictionInput blockingRestrictionInputModified = new BlockingRestrictionInput();
		blockingRestrictionInputModified.setScheme(modifiedScheme);
		
		DatasetPair datasetPair = null;
		if (datasets.length > 1)
			datasetPair = new DatasetPair(datasets[0], datasets[1]);
		
		for (GenericBlockingRestriction restriction: Psi) {
			if (restriction.getType() == RestrictableType.TRAINING_SET) {
				blockingRestrictionInputModified.setRestrictable(trainingSet);
				if (!restriction.honorsRestriction(blockingRestrictionInputModified)) {
					return false;
				}
			} else if (restriction.getType() == RestrictableType.DATASET) {
				if (datasets.length == 1)
					blockingRestrictionInputModified.setRestrictable(datasets[0]);
				else blockingRestrictionInputModified.setRestrictable(datasetPair);
				if (!restriction.honorsRestriction(blockingRestrictionInputModified)) {
					return false;
				}
			} 
		}
		
		BlockingGoalInput blockingGoalInputOriginalScheme = new BlockingGoalInput();
		blockingGoalInputOriginalScheme.setTrainingSet(trainingSet);
		blockingGoalInputOriginalScheme.setScheme(s_opt);
		blockingGoalInputOriginalScheme.setDataset(datasets[0]);
		
		BlockingGoalInput blockingGoalInputModifiedScheme = new BlockingGoalInput();
		blockingGoalInputModifiedScheme.setTrainingSet(trainingSet);
		blockingGoalInputModifiedScheme.setScheme(modifiedScheme);
		blockingGoalInputModifiedScheme.setDataset(datasets[0]);
		
		double objOriginalScheme = goal.objectiveFunction(blockingGoalInputOriginalScheme);
		double objModifiedScheme = goal.objectiveFunction(blockingGoalInputModifiedScheme);
		
		if (s_opt.getCriteria() == BlockingKeyCombinationCriteria.DISJUNCTION 
				|| s_opt.getCriteria() == BlockingKeyCombinationCriteria.CONJUNCTION)
			if (objModifiedScheme > objOriginalScheme) {
				
				s_opt.setSchemeKeys(modifiedScheme.getSchemeKeys());
	
				return true;
			}
		
		if (s_opt.getCriteria() == BlockingKeyCombinationCriteria.DNF)
			if (objModifiedScheme > objOriginalScheme
					|| (objModifiedScheme==objOriginalScheme
					&& modifiedScheme.sizeOflargerstKeyCombination() <=3
					&& modifiedScheme.getSchemeKeys().size() <=3
					&& modifiedScheme.getNumberOfBlockingKeys()>s_opt.getNumberOfBlockingKeys())) {
				
				s_opt.setSchemeKeys(modifiedScheme.getSchemeKeys());
	
				return true;
			}
		
		
		return false;
	}

	private boolean replace(BlockingKey k1, Set<BlockingKey> kopt, BlockingScheme s_opt, HashSet<BlockingKey> K,
			BlockingGoal<BlockingGoalInput, Double> goal, Set<GenericBlockingRestriction> Psi,
			TrainingSet trainingSet, Dataset[] datasets) {
		
		BlockingScheme schemeToModify = new BlockingScheme();
		schemeToModify.setCriteria(s_opt.getCriteria());
		schemeToModify.setSchemeKeys(copySet(s_opt.getSchemeKeys()));
		
		boolean changed = false;
		
		for (HashSet<BlockingKey> keySet: schemeToModify.getSchemeKeys()) {
			//avoids ConcurrentModificationException
			HashSet<BlockingKey> copyOfkeySet = new HashSet<BlockingKey>(keySet);
			for (BlockingKey key : copyOfkeySet) {
				schemeToModify.replace(keySet,key,k1);
				changed = improveSchemaIfAdvantageousAndAllowed(schemeToModify,Psi,goal,s_opt,trainingSet, datasets);
				if (changed) {
					K.add(key);
					K.remove(k1);
					kopt.add(k1);
					kopt.remove(key);
					s_opt.setSchemeKeys(schemeToModify.getSchemeKeys());
					break;
				} else {
					//undo replace
					schemeToModify.replace(keySet,k1,key);
				}
			}
		}
		
		return changed;
	}

	private BlockingKey localSearch(HashSet<BlockingKey> K, BlockingGoal<BlockingGoalInput, Double> goal,
			Set<GenericBlockingRestriction> Psi, TrainingSet trainingSet, BlockingKeyCombinationCriteria c,
			Dataset[] datasets) {
		
		BlockingKey bestKey = null;
		Double bestQuality = Double.NEGATIVE_INFINITY;
		
		BlockingGoalInput blockingGoalInput = new BlockingGoalInput();
		blockingGoalInput.setTrainingSet(trainingSet);
		blockingGoalInput.setDataset(datasets[0]);
		
		BlockingScheme s = new BlockingScheme();
		s.setCriteria(c);
		
		for (BlockingKey key: K) {
			
			s.addInitialBlockingKey(key);
			blockingGoalInput.setScheme(s);
			
			Double currentQuality = goal.objectiveFunction(blockingGoalInput);
			
			if (currentQuality > bestQuality) {
				bestQuality = currentQuality;
				bestKey = key;
			}
			
			s.removeBlockingKey(key);
		}
		
		return bestKey;
	}

	private HashSet<BlockingKey> greedyInitialKeySet(HashSet<BlockingKey> K, BlockingKeyCombinationCriteria c,
			TrainingSet trainingSet, Dataset[] datasets, BlockingGoal<BlockingGoalInput, Double> goal, 
			Set<GenericBlockingRestriction> Psi) {
		
		BlockingKey bestKey = null;
		Double bestQuality = .0;
		
		BlockingRestrictionInput blockingRestrictionInput = new BlockingRestrictionInput();
		BlockingScheme s = new BlockingScheme();
		s.setCriteria(c);
		blockingRestrictionInput.setScheme(s);
		
		BlockingGoalInput blockingGoalInput = new BlockingGoalInput();
		blockingGoalInput.setTrainingSet(trainingSet);
		blockingGoalInput.setDataset(datasets[0]);
		
		DatasetPair datasetPair = null;
		if (datasets.length > 1) {
			datasetPair = new DatasetPair(datasets[0], datasets[1]);
		}
		
		for (BlockingKey key: K) {
			
			s.addInitialBlockingKey(key);
			
			boolean honorsRestrictions = true;
			for (GenericBlockingRestriction restriction: Psi) {
				if (restriction.getType() == RestrictableType.TRAINING_SET) {
					blockingRestrictionInput.setRestrictable(trainingSet);
					if (!restriction.honorsRestriction(blockingRestrictionInput)) {
						honorsRestrictions = false;
						break;
					}
				} else if (restriction.getType() == RestrictableType.DATASET) {
					if (datasets.length == 1)
						blockingRestrictionInput.setRestrictable(datasets[0]);
					else blockingRestrictionInput.setRestrictable(datasetPair);
					if (!restriction.honorsRestriction(blockingRestrictionInput)) {
						honorsRestrictions = false;
						break;
					}
				} 
			}
			
			if (!honorsRestrictions) {
				s.removeBlockingKey(key);
				continue;
			}
			
			blockingGoalInput.setScheme(s);
			
			Double currentQuality = goal.objectiveFunction(blockingGoalInput);
			
			if (currentQuality > bestQuality) {
				bestQuality = currentQuality;
				bestKey = key;
			}
			
			s.removeBlockingKey(key);
		}
		
		HashSet<BlockingKey> optimizedKeySet = new HashSet<BlockingKey>();
		optimizedKeySet.add(bestKey);
		
		K.remove(bestKey);
		
		return optimizedKeySet;
	}
	
	private static Set<HashSet<BlockingKey>> copySet(Set<HashSet<BlockingKey>> originalSchemaKeys) {
		Set<HashSet<BlockingKey>> newSchemaBKSet = new HashSet<HashSet<BlockingKey>>();
		for (HashSet<BlockingKey> keySet: originalSchemaKeys) {
			HashSet<BlockingKey> newkeySet = new HashSet<BlockingKey>();
			for (BlockingKey bk: keySet) {
				newkeySet.add(new BlockingKey(bk.getAtt(), bk.getBkFunction(), bk.GetBKName()));
			}
			newSchemaBKSet.add(newkeySet);
		}
		return newSchemaBKSet;
	}

}
