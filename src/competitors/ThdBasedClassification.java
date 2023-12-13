package competitors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import record.Record;

import record.RecordPair;
import traininset.TrainingSet;

public class ThdBasedClassification {
	
	public TrainingSet generateTrainingSets(Set<Record> records, Set<RecordPair> P, double lt, double ut, 
			SimFunction sim, String[] atts, int targetSize) {
		
		TrainingSet trainingSet = new TrainingSet();
		TFIDFCalculator simFunction = new TFIDFCalculator();
		
		HashMap<String, Integer> tfstatistics = simFunction.generateTfStatistics(records, atts);
		
		HashMap<RecordPair, Double> simPairMap = new HashMap<RecordPair, Double>();
		
		for (RecordPair pair : P) {
			
			StringBuilder sbX = new StringBuilder();
			StringBuilder sbY = new StringBuilder();
			for (String att: atts) {
				sbX.append(pair.getR1().getRecordAttributes().get(att) + " ");
				sbY.append(pair.getR2().getRecordAttributes().get(att) + " ");
			}
			
			String x = sbX.toString();
			String y = sbY.toString();
			
			int datasetSize = records.size();
			
			double simValue = simFunction.TFIDFSimilarity(x, y, tfstatistics, datasetSize);

			simPairMap.put(pair, simValue);
			
		}
		
		while (trainingSet.getPositiveSet().size() < targetSize/2 
				|| trainingSet.getNegativeSet().size() < targetSize/2) {
			for (RecordPair pair : P) {
				
				double simValue = simPairMap.get(pair);
				
				if (trainingSet.getPositiveSet().size() < targetSize/2 &&
						simValue >= ut && !trainingSet.getPositiveSet().contains(pair)) {
					trainingSet.addPositivePair(pair);
				} else if (trainingSet.getNegativeSet().size() < targetSize/2 &&
						simValue <= lt && !trainingSet.getNegativeSet().contains(pair)) {
					trainingSet.addNegativePair(pair);
				}
				if (trainingSet.size() == targetSize)
					break;
			}
			ut -=0.05;
			lt +=0.05;
		}
		
		return trainingSet;
		
	}

}
