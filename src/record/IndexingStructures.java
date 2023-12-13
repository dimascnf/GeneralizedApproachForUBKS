package record;
import java.util.HashMap;
import java.util.Set;

public class IndexingStructures {
	
	private HashMap<RecordPair, Integer> blockingGraph;
	private HashMap<String,Set<Record>> blocks;
	private HashMap<Record,Set<String>> recordBlocks;
	
	public IndexingStructures() {
		blockingGraph = new HashMap<RecordPair, Integer>();
		blocks = new HashMap<String,Set<Record>>();
		recordBlocks = new HashMap<Record,Set<String>>();
	}

	public HashMap<RecordPair, Integer> getBlockingGraph() {
		return blockingGraph;
	}

	public void setBlockingGraph(HashMap<RecordPair, Integer> blockingGraph) {
		this.blockingGraph = blockingGraph;
	}

	public HashMap<String, Set<Record>> getBlocks() {
		return blocks;
	}

	public void setBlocks(HashMap<String, Set<Record>> blocks) {
		this.blocks = blocks;
	}

	public HashMap<Record, Set<String>> getRecordBlocks() {
		return recordBlocks;
	}

	public void setRecordBlocks(HashMap<Record, Set<String>> recordBlocks) {
		this.recordBlocks = recordBlocks;
	}
	
}
