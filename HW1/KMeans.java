import java.util.ArrayList;
import java.util.List;

/**
 * A k-means clustering algorithm implementation.
 * 
 */

public class KMeans {
	
	private int k, n, dim;
	List<Double> distortions = new ArrayList<Double>();
	
	public KMeansResult cluster(double[][] centroids, double[][] instances, double threshold) {
		/* ... YOUR CODE GOES HERE ... */
		k = centroids.length;
		n = instances.length;
		dim = centroids[0].length;
		KMeansResult results = new KMeansResult();
		results.clusterAssignment = new int[n];
		results.centroids = new double[k][dim];
		
		//centroids
		while(true) {
			distortionIterations(centroids, instances, results.clusterAssignment);
			//
			//System.out.println(centroids);
		//	for(int i = 0; i < 100; ++i)
		//	System.out.print(results.clusterAssignment[i] + ",");
		//	System.out.println("");
			//for(int i = 0; i<results.clusterAssignment.length; i++){
			//System.out.println("THIS IS WHAT IT IS for "+ i+": "+ results.clusterAssignment[i]);
			//}
			//System.out.println();
			boolean orphaned = orphanedCenriodHandling(instances, results.clusterAssignment, centroids);
			//System.out.println(distor + " orphaned:" + orphaned);

			if(orphaned == true) {
				continue;
			}
			
			double distor = average(instances, results.clusterAssignment, k, centroids);
			distortions.add(distor);
			int sz = distortions.size();
			if(sz > 1) {
				double last = distortions.get(sz - 2);
				double value = Math.abs((distor - last)/last);
				if(value < threshold) {
					break;
				}
			}
			//break;
		}
		for(int i = 0; i < k; ++i) {
			for(int j = 0; j < dim; ++j) {
				results.centroids[i][j] = centroids[i][j];
			}
		}
		int sz = distortions.size();
		
		results.distortionIterations = new double[sz];
		for(int i = 0; i < sz; ++i) {
			results.distortionIterations[i] = distortions.get(i);
		}
		return results;
	}
	
	
	//Math.pow(a, 2.0);
	//Math.sqrt();
	
	
	public void distortionIterations(double[][] centroids, double[][] instances, int [] table) {
		
		//int[] table = new int[n];
		for(int i = 0; i < n; i++) {
			//
			double[] inst = instances[i];
			//find the index of the centroid with smallest distance
			double [] distances = new double[k];
			for(int j = 0;j < k;j++) {
				double [] c = centroids[j];
				double dist = distance(inst, c);
				//double MaxDist = dist(inst[0],c[0]);
				distances[j] = dist;
				//if(distances[j]>MaxDist)
			}
				
			int index = getMinIndex(distances);
			//System.out.println("The centroid of "+i+" is "+index);
			table[i] = index;
		}
	}
	
	public boolean orphanedCenriodHandling(double[][] instances, int [] table, double[][] centroids) {
		
		int [] cc = new int[k];
		for(int i = 0; i < k; i++)
			cc[i] = 0;
		for(int i = 0; i < n; i++){
			int c = table[i];
			cc[c]++;
		}
		boolean orphaned = false;
		for(int i = 0; i < k; i++) {
			//System.out.println("Now at centroid: "+i);
			if(cc[i]==0) {
				orphaned = true;
				//System.out.println("The orphan is "+i);
				double [] fathest = new double[dim];
				/*for(int j = 0; j < dim; j++){
					System.out.println("NEW the centroid's "+i+"th index is "+centroids[i][j]);
				}*/
				fathest = fasthestInstance(instances, table, centroids);
				//System.out.println("The farthest is: "+ fathest[0]);
				for(int j = 0; j < dim; j++){
					centroids[i][j] = fathest[j];
				}
				//
				break;
			}
		}
		return orphaned;
		//orphaned centroid happened
	}
	
	/**
	 * 
	 */
	public double [] fasthestInstance(double[][] instances, int[] table, double[][] centroids) {
		double cMaxDist = -1.0;
		double dist = -2.0;
		int index = -1;
		/*for(int i = 0; i < centroid.length; i++){
			System.out.println("the centroid's "+i+"th index is "+centroid[i]);
		}*/
		for(int i = 0; i < n; i++){
			double [] iInstance = instances[i];
			dist = distance(iInstance, centroids[table[i]]);
			//System.out.println("distance to "+i+" is "+dist);
			if(dist > cMaxDist) {
				cMaxDist = dist;
				index = i;
			}
		}
		//System.out.println("max dist = "+cMaxDist);
		//System.out.println("index = "+index);
		return instances[index];
	}
	
	
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static int getMinIndex(double [] a) {
		int index = -1;
		double data = Double.MAX_VALUE;
		for(int i = 0;i < a.length; i++) {
			if(a[i]< data){
				data = a[i];
				index = i;
			}	
		}
		return index;
	}
	
	/**
	 * return the distance of two array
	 * @param a
	 * @param b
	 * @return
	 */
	public static double distance(double[] a, double[] b) {
		int len = a.length;
		double dist = 0.0;
		//calculate distance here:
		//assert len >= 2;
		for(int i = 0; i < len; i++) {
			dist += Math.pow(a[i]-b[i],2);
		}
		return dist;
	}
	
	public double average(double[][] instances, int[] table, int k, double[][] avgs) {
		int n = instances.length;
		int dim = instances[0].length;
		
		//double [][] avgs = new double[k][dim];
		int[] counter = new int[k];
		///init
		//for(int i = 0; i < avgs.length; i++){
		//	for(int j = 0; j < dim; j++){
		//		System.out.println("AVG1 the "+i+"th centroid's "+j+"th index is "+avgs[i][j]);
		//	}
		//	}
		for(int i = 0; i < k; i++) {
			counter[i] = 0;
			for(int j = 0; j < dim; j++) {
				avgs[i][j] = 0.0;
			}
		}
		//for(int i = 0; i < avgs.length; i++){
		//	for(int j = 0; j < dim; j++){
		//		System.out.println("AVG2 the "+i+"th centroid's "+j+"th index is "+avgs[i][j]);
		//	}
		//	}
		////////////////////////////////////
		for(int i = 0; i < n; i++) {
			int c = table[i];
			counter[c]++;
			for(int j = 0; j < dim; j++) {
				avgs[c][j] += instances[i][j];
			}
		}
		//for(int i = 0; i < avgs.length; i++){
		//	for(int j = 0; j < dim; j++){
		//		System.out.println("AVG3 the "+i+"th centroid's "+j+"th index is "+avgs[i][j]);
		//	}
		//	}
		for(int i = 0; i < k; i++) {
			if(counter[i]==0)
				continue;
			for(int j = 0; j < dim; j++) {
				avgs[i][j] /= (double)counter[i];
			}
		}
		//for(int i = 0; i < avgs.length; i++){
		//	for(int j = 0; j < dim; j++){
		//		System.out.println("AVG4 the "+i+"th centroid's "+j+"th index is "+avgs[i][j]);
		//	}
		//	}
		/*centroids = */
		// the distance between ith instance and to its centroid	
		double sumdistor = 0.0;
		for(int i = 0; i < n; i++){
			double [] inst = instances[i];
			int c = table[i];
			double [] centd = avgs[c];
			double newDist = distance(inst, centd);
			sumdistor += newDist;
		}
		//////////////////
		return sumdistor;	
	//	return avgs;
	}
	
}

