
public class ValuePredictor {

	public static void main(String[] args) {
		
		int predictAmount = 10; //project the data by five entries for example
		float[] data = new float[] {185.86f, 189.12f, 185.97f, 187.77f, 189.69f, 188.11f, 187.66f, 189.91f}; //most recent data first in order
		//float[] data = new float[] {4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f};
		//System.out.println(data.length);
		
		float[] pattern = new float[(data.length/2*(data.length/2 + 1)/2)];	//triangle number formula: 1+2+3+...n = n(n+1)/2; n = data.length / 2
		for (int i = 0; i < pattern.length; i++) {
			pattern[i] = 0;
		}
		//The pattern is relative to the starting data point
		//some of the cells will be empty for when the pattern interval is too large and therefore lacking data to guess pattern.
		//Unless the pattern is guessed solely on the single data value
		
		float[] predictedData = new float[predictAmount];
		
		//*******************************find the pattern for each data interval
		float[] dataDelta = new float[data.length - 1];		//create it only once to allocate space only once for the largest possible array
		float tmpStorage;
		float q1;	//IQR
		float q3;	//IQR
		int dataDeltaLength;
		int lowerBound;
		int upperBound;
		for (int skipIndex = 1; skipIndex * 2 < data.length; skipIndex++) {		//< data.length //process pattern intervals until the pattern interval is the same size as the entire data series
			
			for (int shiftIndex = 0; (shiftIndex < skipIndex) && (shiftIndex + (skipIndex * 2) < data.length); shiftIndex++) {	//process pattern all pattern intervals for current skip index
				dataDeltaLength = 1;		//reset array
				dataDelta[0] = data[shiftIndex] - data[shiftIndex + skipIndex];
				
				for (int i = shiftIndex + (skipIndex * 2); i < data.length - (skipIndex * (((data.length - shiftIndex - 1) / skipIndex) % 2)); i += skipIndex, dataDeltaLength++) {	//process individual pattern interval	//if pattern exists, don't let an odd amount of data distort it. Thus  - (skipIndex * (((data.length - shiftIndex) / skipIndex) % 2))
					//*****Order values of (data[i - skipIndex] - data[i]) in ascending order into dataDelta array
					tmpStorage = data[i - skipIndex] - data[i];
					lowerBound = 0;
					upperBound = dataDeltaLength - 1;	//upperBound is initially the index of the array's final value
					if (tmpStorage < dataDelta[0]) {	//if current value is lowest, place it immediately at the beginning because while loop will fail
						upperBound = 0;
					}
					else if (tmpStorage > dataDelta[upperBound]) {	//in case tmpStorage has highest value
						upperBound++;
					}
					else {
						while (upperBound - lowerBound > 1) {
							if (tmpStorage < dataDelta[(upperBound + lowerBound) / 2]) {	//java rounds down
								upperBound = (upperBound + lowerBound) / 2;
							}
							else {
								lowerBound = (upperBound + lowerBound) / 2;
							}
						}
					}
					for (int j = dataDeltaLength; j > upperBound; j--) {
						dataDelta[j] = dataDelta[j - 1];
					}
					
					/*if (shiftIndex == 2 && skipIndex == 3) {		//*****************
						System.out.print(" " + tmpStorage);
					}
					*/
					
					dataDelta[upperBound] = tmpStorage;
				}
				
				/*for (int i = 0; i < dataDeltaLength; i++) {			//*****************
					System.out.println(dataDelta[i]);
				}
				System.out.println("skipIndex = " + skipIndex + " : shiftIndex = " + shiftIndex);		//*****************
				*/
				
				
				
				//remove outliers from dataDelta array
				while (dataDeltaLength > 2) {
					q1 = (dataDelta[(int) (dataDeltaLength * 0.25)] + dataDelta[(int) (dataDeltaLength * 0.25 + 0.5)]) / 2;
					q3 = (dataDelta[(int) (dataDeltaLength * 0.75 - 0.05)] + dataDelta[(int) (dataDeltaLength * 0.75 - 0.55)]) / 2; //subtract an extra 0.05 for integer rounding correction
					//System.out.println("Q1 = " + q1 + " Q3 = " + q3 + " IQR = " + (q3 - q1));		//*****************
					
					if (dataDelta[dataDeltaLength - 1] - (q3 + ((q3 - q1) * 1.5)) > (q1 - ((q3 - q1) * 1.5)) - dataDelta[0]) {	//if upper value deviates further from IQR than the lower value
						if (dataDelta[dataDeltaLength - 1] > q3 + ((q3 - q1) * 1.5)) {	//if upper outlier exists
							dataDeltaLength--;
							//System.out.println("Upper Outlier = " + dataDelta[dataDeltaLength]);		//*****************
						}
						else {
							break;
						}
					}
					else if (dataDelta[0] < q1 - ((q3 - q1) * 1.5)) {	//if lower outlier exists
						//System.out.println("Lower Outlier = " + dataDelta[0]);		//*****************
						for (int i = 1; i < dataDeltaLength; i++) {
							dataDelta[i - 1] = dataDelta[i];
						}
						dataDeltaLength--;
					}
					else {
						break;
					}
				}
				//System.out.println();		//*****************
					
				//calculate average
				tmpStorage = 0;									//tmpStorage contains average
				
				for (int i = 0; i < dataDeltaLength; i++) {
					tmpStorage += dataDelta[i];
				}
				tmpStorage /= dataDeltaLength;
				
				/*if (skipIndex < 4) {					//*****************
					System.out.println(skipIndex + " " + dataDeltaLength + " " + tmpStorage);
					for (int i = 0; i < dataDeltaLength; i++) {
						System.out.print(" " + dataDelta[i]);
					}
					System.out.println();
				}*/
				
				
				//subtract from current average the value of (the averages from lower order skipValues that overlap with this one times the amount of their intervals that fit into the current interval)
				for (int i = 1; i < skipIndex; i++) {
					if (skipIndex % i == 0) {		//if skipIndex is completely divisible by i
						if (shiftIndex < i) {
							tmpStorage -= pattern[i * (i - 1) / 2 + shiftIndex] * (skipIndex / i);
						}
						else {
							tmpStorage -= pattern[i * (i - 1) / 2 + (shiftIndex % i)] * (skipIndex / i);
						}
						
						//System.out.println("skipIndex = " + skipIndex + " i = " + i);		//*****************
					}
					else {						//else calculate the average change as a result of i over current skipInterval and subtract from tmpStorage
						q1 = 0;								//temporary value storage
						q3 = ((float) skipIndex) / i;		//temporary value storage to speed up loop below
						for (int j = 0; j < i; j++) {
							q1 += pattern[i * (i - 1) / 2 + j] * q3;
						}
						tmpStorage -= q1 / i;
					}
				}
				
				//place final value into pattern[] array
				pattern[skipIndex * (skipIndex - 1) / 2 + shiftIndex] = tmpStorage;
			}
		}
		
		
		/*int j = 0;							//*****************
		for (int i = 0; i < 50; i++) {		
			//System.out.println("i = " + i);
			if (j*(j+1)/2 == i) {
				System.out.println();
				j++;
			}
			System.out.println(pattern[i]);
		}
		*/
		
		
		//predict the data using the pattern
		int patternIndex;
		for (int i = 1; i <= predictAmount; i++) {		//i is both iteration quantity and the reverse shift index
			predictedData[i - 1] = (i == 1) ? data[0] : predictedData[i - 2];
			for (int skipIndex = 1; skipIndex * 2 < data.length; skipIndex++) {
				if (i < skipIndex) {
					patternIndex = (skipIndex * (skipIndex + 1)) / 2 - i;
				}
				else {
					patternIndex = (i % skipIndex == 0) ? ((skipIndex * (skipIndex - 1)) / 2) : ((skipIndex * (skipIndex + 1)) / 2 - (i % skipIndex));	//	(n*(n+1))/2 - n == (n*(n-1))/2  
				}
				predictedData[i - 1] += pattern[patternIndex];
			}
			System.out.print(predictedData[i - 1] + " ");
		}

	}
	
	

}
