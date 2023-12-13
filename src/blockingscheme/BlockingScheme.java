package blockingscheme;

import java.util.HashSet;
import java.util.Set;

import blockingkey.BlockingKey;
import record.Record;
import record.RecordPair;

public class BlockingScheme {
	
	private BlockingKeyCombinationCriteria criteria;
	private Set<HashSet<BlockingKey>> schemeKeys;
	
	public BlockingScheme() {
		schemeKeys = new HashSet<HashSet<BlockingKey>>();
	}
	
	public int sizeOflargerstKeyCombination() {
		if (criteria == BlockingKeyCombinationCriteria.CONJUNCTION 
			|| criteria == BlockingKeyCombinationCriteria.DISJUNCTION)
			return schemeKeys.iterator().next().size();
		else {
			int max = 0;
			for (HashSet<BlockingKey> conjunction: schemeKeys) {
				if (conjunction.size() > max)
					max = conjunction.size();
			}
			return max;
		}
	}
	
	public void addInitialBlockingKey(BlockingKey bk) {
		HashSet<BlockingKey> keySet = new HashSet<BlockingKey>();
		keySet.add(bk);
		schemeKeys.add(keySet);
	}
	
	public void addBlockingKeys(HashSet<BlockingKey> keySet) {
		schemeKeys.add(keySet);
	}
	
	//Employed for Conjuntion, Disjunction or DNF with a single conjunction
	public void removeBlockingKey(BlockingKey bk) {
		getSchemeKeys().iterator().next().remove(bk);
		if (getSchemeKeys().iterator().next().size()==0)
			schemeKeys.clear();
	}

	public BlockingKeyCombinationCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(BlockingKeyCombinationCriteria criteria) {
		this.criteria = criteria;
	}

	public void setSchemeKeys(Set<HashSet<BlockingKey>> schemeKeys) {
		this.schemeKeys = schemeKeys;
	}

	public Set<HashSet<BlockingKey>> getSchemeKeys() {
		return schemeKeys;
	}
	
	//Employed for Conjuntion of Disjunction
	public void expand(BlockingKey k1) {
		schemeKeys.iterator().next().add(k1);
	}
	
	public boolean isEmpty() {
		return schemeKeys.isEmpty();
	}
	
	public int getNumberOfBlockingKeys() {
		if (this.criteria == BlockingKeyCombinationCriteria.CONJUNCTION
				|| this.criteria == BlockingKeyCombinationCriteria.DISJUNCTION) {
			return schemeKeys.iterator().next().size();
		} else { 
			//DNF
			int numberOfKeys = 0;
			for (HashSet<BlockingKey> conjunction: schemeKeys) {
				numberOfKeys += conjunction.size();
			}
			return numberOfKeys;
			//Conjunction or Disjunction
			//return schemeKeys.size();
		}
	}

	public void replace(HashSet<BlockingKey> keySet, BlockingKey key, BlockingKey k1) {
		keySet.remove(key);
		keySet.add(k1);
	}

	public void removeBlockingKey(HashSet<BlockingKey> keySet, BlockingKey key) {
		keySet.remove(key);		
	}

	public void expand(HashSet<BlockingKey> keySet, BlockingKey key) {
		keySet.add(key);
	}

	public boolean coveredBySchema(RecordPair pair) {
		
		if (schemeKeys.isEmpty() || schemeKeys.iterator().next().isEmpty())
			return false;
		
		if (criteria == BlockingKeyCombinationCriteria.DNF) {
			for(HashSet<BlockingKey> conjunction: schemeKeys) {
				boolean satisfiesConjunction = true;
				for (BlockingKey k : conjunction) {
					try {
						if (!k.getBKV(pair.getR1()).equals(k.getBKV(pair.getR2()))) {
							satisfiesConjunction = false;
							break;
						}
					} catch (Exception e) {
						satisfiesConjunction = false;
						break;
					}
				}
				if (satisfiesConjunction)
					return true;
			}
			return false;
		}
		
		if (criteria == BlockingKeyCombinationCriteria.DISJUNCTION) {
			HashSet<BlockingKey> keys=  schemeKeys.iterator().next(); 
			for (BlockingKey k : keys) {
				try {
					if (k.getBKV(pair.getR1()).equals(k.getBKV(pair.getR2()))) {
						return true;
					}
				} catch (Exception e) {
					return false;
				}
			}
			return false;
		}
		
		if (criteria == BlockingKeyCombinationCriteria.CONJUNCTION) {
			HashSet<BlockingKey> keys=  schemeKeys.iterator().next(); 
			for (BlockingKey k : keys) {
				try {
					if (!k.getBKV(pair.getR1()).equals(k.getBKV(pair.getR2()))) {
						return false;
					}
				} catch (Exception e) {
					return false;
				}
			}
		}
		
		return true;
	}

	public void removeBlockingKeys(HashSet<BlockingKey> newConjunction) {
		schemeKeys.remove(newConjunction);		
	}
	
}
