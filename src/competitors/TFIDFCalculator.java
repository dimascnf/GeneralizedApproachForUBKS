package competitors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import record.Record;

public class TFIDFCalculator {
	
	public HashMap<String, Integer> generateTfStatistics(List<String> records){
		
		HashMap<String, Integer> tfstatistics = new HashMap<String, Integer>(); 
		
		for (String r : records) {
			String[] tokens = r.split(" ");
			for (String t: tokens) {
				if (tfstatistics.containsKey(t)) {
					tfstatistics.put(t, tfstatistics.get(t)+1);
				} else {
					tfstatistics.put(t, 1);
				}
			}
		}
		
		return tfstatistics;
	}
	
	public HashMap<String, Integer> generateTfStatistics(Set<Record> records, String att){
		
		HashMap<String, Integer> tfstatistics = new HashMap<String, Integer>(); 
		
		for (Record r : records) {
			String[] tokens = ((String) r.getRecordAttributes().get(att)).split(" ");
			for (String t: tokens) {
				if (tfstatistics.containsKey(t)) {
					tfstatistics.put(t, tfstatistics.get(t)+1);
				} else {
					tfstatistics.put(t, 1);
				}
			}
		}
		
		return tfstatistics;
	}
	
public HashMap<String, Integer> generateTfStatistics(Set<Record> records, String[] atts){
		
		HashMap<String, Integer> tfstatistics = new HashMap<String, Integer>(); 
		
		for (Record r : records) {
			for (String att: atts) {
				String[] tokens = ((String) r.getRecordAttributes().get(att)).split(" ");
				for (String t: tokens) {
					if (tfstatistics.containsKey(t)) {
						tfstatistics.put(t, tfstatistics.get(t)+1);
					} else {
						tfstatistics.put(t, 1);
					}
				}
			}
		}
		
		return tfstatistics;
	}
	
	public double TFIDFSimilarity(String x, String y, HashMap<String, Integer> tfStatistics, int datasetSize) {
		
		Set<String> s1 = new HashSet<String>(Arrays.asList(x.split(" ")));
		Set<String> s2 = new HashSet<String>(Arrays.asList(y.split(" ")));
		s1.retainAll(s2);
		String[] intersection = s1.toArray(new String[s1.size()]);
		
		double sim = 0;
		for (String tkn : intersection){
			sim += wt(tkn,x,tfStatistics, datasetSize) * wt(tkn,x,tfStatistics,datasetSize); 
		}
		
		return sim;
	}

	private double wt(String tkn, String x, HashMap<String, Integer> tfStatistics, int datasetSize) {
		double denominator = 0;
		String[] tokens = x.split(" ");
		for (String token: tokens) {
			denominator += Math.pow(wtAux(token,x,tfStatistics.get(token),datasetSize),2);
		}
		denominator = Math.sqrt(denominator);
		return wtAux(tkn,x,tfStatistics.get(tkn),datasetSize) / denominator; 
	}

	private double wtAux(String token, String x, int df_tkn, int datasetSize) {
		String[] tokens = x.split(" ");
		int tf_token = 0;
		for (String t: tokens)
			if (t.equals(token))
				tf_token++;
		return Math.log10(tf_token+1) * Math.log10((datasetSize/df_tkn) + 1);
	}

}
