# GeneralizedApproachForUBKS
Java implementation of a Generalized Approach for Unsupervised Bblocking Key Selection.

- **Proposed Optimzed Metaheuristic**: src/heuristic/GeneralizedMetaheuristic
  
- **Proposed Sampling Method**: src/heuristic/SamplingMethod

- **Proposed Classification Heuristic**: src/heuristic/ClassificationHeuristic
 
- **Competitor**: src/competitors/LearnOptimalBK
 
- **Greedy UBKS Approach**: src/competitors/GreedyUBKS

- **Datasets**: datasets/

- **Optimized Metaheuristic Parameters**: 

| Datasets | K | trainingSet | C | goal | Psi |
| :-----: | :---: | :---: | :-----: | :---: | :---: | 
| dataset(s) to be processed (set of records) | input set of blocking keys | training examples  | blocking key combination criteria | blocking goal | set of blocking restrictions |

- **Optimized Metaheuristic Output**: optimized blocking schema 

- **Sampling Method Parameters**: 

| Datasets | K | ts | v |
| :-----: | :---: | :---: | :-----: | :-----: | 
| dataset(s) to be processed (set of records) | input set of blocking keys  | target sampling size  | number of blocking key set variations on each similarity level |

- **Sampling Method Output**: set of record pairs (containing ts record pairs)

- **Classification Heuristic Parameters**: 

| P | K | ts | beta | blockingGraphUpdateStrategy | 
| :-----: | :---: | :---: | :-----: | :-----: | 
| set of record pairs | input set of blocking keys | target training set size | parameter used to influence the thresholds for classifying the input pairs as match or non match | strategy to update the extended blocking graph (default: ENTIRE_TRAINING_SET) | 

- **Classification Heuristic Output**: training set (containing ts training examples)
