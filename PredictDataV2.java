
public class PredictDataV2 {
	
	public static float[] data;
	public static float[] dataDelta;
	public static int dataDeltaLength;
	
	public static float[][] pattern;

	public static void main(String[] args) {
		
		int predictAmount = 10; //project the data by five entries for example
		data = new float[] {185.86f, 189.12f, 185.97f, 187.77f, 189.69f, 188.11f, 187.66f, 189.91f}; //most recent data last in order
		//data = new float[] {4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f};
		//data = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f};
		//data = new float[] {1f, 1f, 1f, 4f, 4f, 4f, 7f, 7f, 7f, 10f, 10f, 10f, 13f, 13f, 13f, 16f, 16f, 16f, 19f, 19f, 19f, 22f, 22f, 22f};
		//System.out.println(data.length);
	
		
		pattern = new float[data.length - 2][];		//the largest possible skip index will be: data.length - 2
		for (int i = 0; i + i < pattern.length; i++) {		//skip index = i+1; all shift indices work while: skip index + 2 + (skip index - 1) <= data.length :OR: (i + 1 + 2 + i) = (i + i + 3) <= data.length :OR: (i + i + 2) < data.length :OR: i + i < (data.length - 2) = pattern.length
			pattern[i] = new float[i + 1];
		}
		//need to shorten array length for large scale pattern intervals which will fit fewer shift indices
		for (int i = (pattern.length / 2) + (pattern.length % 2); i < pattern.length; i++) {		//java rounds down
			pattern[i] = new float[pattern.length - i];			//don't use this many shift indices: skip index + 2 + (skip index - 1) - data.length = i + 1 + 2 + i - data.length = i + i + 3 - data.length
		}								//i + 1 - (i + i + 3 - data.length) = data.length - 2 - i) = pattern.length - i
		
		//The pattern is relative to the starting data point
		//some of the cells will be empty for when the pattern interval is too large and therefore lacking data to guess pattern.
		//Unless the pattern is guessed solely on the single data value
		
		float[] predictedData = new float[predictAmount];
		
		//*******************************find the pattern for each data interval
		dataDelta = new float[data.length - 1];		//create it only once to allocate space only once for the largest possible array
		float newPattern;
		
		
		//PATTERN *************************************************************************************************************************
		for (int skipIndex = 1; skipIndex <= pattern.length; skipIndex++) {		//skip by skipIndex
			for (int shiftIndex = 0; shiftIndex < pattern[skipIndex - 1].length; shiftIndex++) {
				dataDeltaLength = 1;		//reset array
				dataDelta[0] = data[shiftIndex + 1] - data[shiftIndex];
				
				
				/*if (skipIndex < 5) {		//***********************
					//System.out.println("data length = " + data.length + "\nskip index = " + skipIndex + "\niterations = " + ((data.length - 1 - shiftIndex - 1)/skipIndex + 1) + "\n" + (data.length - ((skipIndex * (((data.length - 1 - shiftIndex - 1) / skipIndex) % 2)) ^ 1)) + "\n");
					System.out.println(skipIndex * ((((data.length - 1 - shiftIndex) / skipIndex) % 2) ^ 1));
				}*/
				
				
				for (int i = shiftIndex + skipIndex + 1; i < data.length - (skipIndex * ((((data.length - 1 - shiftIndex - 1) / skipIndex) % 2) ^ 1)); i += skipIndex, dataDeltaLength++) {
					addDelta(data[i] - data[i - 1]);
				}
				newPattern = getAverage();
				
				/*if (skipIndex < 5) {		//***********************
					System.out.println(newPattern);
				}*/
				
				//subtract from current average the value of (the averages from lower order skipValues that overlap with this one times the amount of their intervals that fit into the current interval)
				//this was used to set max priority to first-order pattern
				/*for (int i = 1; i < skipIndex; i++) {
					if (skipIndex % i == 0) {		//if skipIndex is completely divisible by i
						if (shiftIndex < i) {
							newPattern -= pattern[i - 1][shiftIndex];
						}
						else {
							newPattern -= pattern[i - 1][shiftIndex % i];
						}
						
					}
					else {						//else calculate the average change as a result of i over current skipInterval and subtract from tmpStorage
						float tmp = 0;								//temporary value storage
						for (int j = 0; j < pattern[i - 1].length; j++) {
							tmp += pattern[i - 1][j] / i;
						}
						newPattern -= tmp;
					}
				}*/
				
				//place final value into pattern[] array
				pattern[skipIndex - 1][shiftIndex] = newPattern;
			}
		}
		
									//*****************
		/*for (int i = 0, j = 0; i < 50; i++) {		
			//System.out.println("i = " + i);
			if (j*(j+1)/2 == i) {
				System.out.println();
				j++;
			}
			System.out.println(pattern[i]);
		}*/
		
		for (int i = 0; i < dataDelta.length; i++) {		//do this once because dataDelta is compared in getAccuracy() method
			dataDelta[i] = data[i + 1] - data[i];
		}
		
		
		//Bleed out patterns that don't exist
		/*float min = 0;
		float max = 1;
		float errorMin;		//lower bound bleed
		float errorMax;		//upper bound bleed
		float originalValue;
		for (int skipIndex = 1; skipIndex <= pattern.length; skipIndex++) {
			//System.out.println(skipIndex);		//***********************
			for (int shiftIndex = 0; shiftIndex < pattern[skipIndex - 1].length; shiftIndex++) {
				originalValue = pattern[skipIndex - 1][shiftIndex];
				errorMax = getError();						//getAccuracy first subtracts lower order, then calculates accuracy, and finally restores original array
				pattern[skipIndex - 1][shiftIndex] = 0;			//min
				errorMin = getError();
				for (int i = 0; i < 10; i++) {				//do this about 10 times for now
					//System.out.println("Error Min: " + errorMin + "\nError Max: " + errorMax);					//***********************
					pattern[skipIndex - 1][shiftIndex] = originalValue;
					if (errorMin < errorMax) {
						max = (max + min) / 2;
						pattern[skipIndex - 1][shiftIndex] *= max;
						errorMax = getError();
					}
					else {
						min = (max + min) / 2;
						pattern[skipIndex - 1][shiftIndex] *= min;
						errorMin = getError();
					}
				}
				if (errorMin < errorMax) {
					pattern[skipIndex - 1][shiftIndex] = originalValue * min;
				}
				else {
					pattern[skipIndex - 1][shiftIndex] = originalValue * max;
				}
			}
		}*/
		
		
		
		//************
		/*for (int i = 0; i < pattern.length; i++) {
			if (i == 2) {
				continue;
			}
			for (int j = 0; j < pattern[i].length; j++) {
				pattern[i][j] = 0;
			}
		}
		
		subtractLowOrder();
		
		for (int i = 0; i < pattern.length; i++) {
			if (i == 2) {
				continue;
			}
			for (int j = 0; j < pattern[i].length; j++) {
				pattern[i][j] = 0;
			}
		}*/
		
		/*for (int i = 0; i < pattern.length; i++) {	
			for (int j = 0; j < pattern[i].length; j++) {
				System.out.println(pattern[i][j]);
			}
			System.out.println();
		}*/
		
		System.out.println("Error: " + (int)getError() + "%");
		
		subtractLowOrder();
		
		//restore original patterns by reversing of subtractLowOrder()
		/*for (int skipIndex = pattern.length; skipIndex > 1; skipIndex--) {		//in this loop the code is the similar to the code for subtracting lower order patterns, except the patterns is added this time
			for (int shiftIndex = 0; shiftIndex < pattern[skipIndex - 1].length; shiftIndex++) {
				for (int i = 1; i < skipIndex; i++) {
					if (skipIndex % i == 0) {		//if skipIndex is completely divisible by i
						if (shiftIndex < i) {
							pattern[skipIndex - 1][shiftIndex] += pattern[i - 1][shiftIndex];
						}
						else {
							pattern[skipIndex - 1][shiftIndex] += pattern[i - 1][shiftIndex % i];
						}
					}
					else {
						for (int j = 0; j < pattern[i - 1].length; j++) {
							pattern[skipIndex - 1][shiftIndex] += pattern[i - 1][j] / i;
						}
					}
				}
			}
		}*/
		
		
		//predict the data using the pattern
		System.out.println("Predicted Data: ");
		predictedData[0] = data[data.length - 1];
		for (int i = 0; i < predictAmount; i++) {		//i is both iteration quantity and the reverse shift index
			if (i != 0) {
				predictedData[i] = predictedData[i - 1];
			}
			for (int skipIndex = 1; skipIndex <= pattern.length; skipIndex++) {
				if ((data.length + i - 1) % skipIndex < pattern[skipIndex - 1].length) {					//prevents index out of bounds
					predictedData[i] += pattern[skipIndex - 1][((data.length + i - 1) % skipIndex)];
				}
			}
			System.out.print(String.format("%.2f", predictedData[i]) + " ");
		}	

	}
	
	
	public static float getError() {
		//backup pattern for subtractLowOrder() method
		float[][] patternBackup = new float[pattern.length][];
		for (int i = 0; i < pattern.length; i++) {
			patternBackup[i] = new float[pattern[i].length];
			for (int j = 0; j < pattern[i].length; j++) {
				patternBackup[i][j] = pattern[i][j];
			}
		}
		subtractLowOrder();
		
		//the actual calculation
		float error = 0;
		float[] computedDelta = new float[dataDelta.length];
		for (int i = 0; i < computedDelta.length; i++) {
			for (int skipIndex = 1; skipIndex <= pattern.length; skipIndex++) {
				if (i % skipIndex < pattern[skipIndex - 1].length) {
					computedDelta[i] += pattern[skipIndex - 1][i % skipIndex];
				}
			}
			error += (Math.abs((computedDelta[i] - dataDelta[i]) / dataDelta[i])) * 100 / dataDelta.length;		//calculate error
		}
		
		pattern = patternBackup;		//restore original pattern
		return error;
	}
	
	public static void subtractLowOrder() {
		//subtract the the values of lower-order patterns from higher-order patterns
		for (int skipIndex = 2; skipIndex <= pattern.length; skipIndex++) {
			for (int shiftIndex = 0; shiftIndex < pattern[skipIndex - 1].length; shiftIndex++) {
				for (int i = 1; i < skipIndex; i++) {
					if (skipIndex % i == 0) {		//if skipIndex is completely divisible by i
						pattern[skipIndex - 1][shiftIndex] -= pattern[i - 1][shiftIndex % i];
					}
					else {
						for (int j = 0; j < pattern[i - 1].length; j++) {
							pattern[skipIndex - 1][shiftIndex] -= pattern[i - 1][j] / i;
						}
					}
				}
			}
		}
	}
	
	
	public static void addDelta(float newDelta) {
		int lowerBound = 0;
		int upperBound = dataDeltaLength - 1;	//upperBound is initially the index of the array's final value
		if (newDelta < dataDelta[0]) {	//if current value is lowest, place it immediately at the beginning because while loop will fail
			upperBound = 0;
		}
		else if (newDelta > dataDelta[upperBound]) {	//in case newDelta has highest value
			upperBound++;
		}
		else {
			while (upperBound - lowerBound > 1) {
				if (newDelta < dataDelta[(upperBound + lowerBound) / 2]) {	//java rounds down
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
		
		dataDelta[upperBound] = newDelta;
	}
	
	public static float getAverage() {
		//remove outliers from dataDelta array
		while (dataDeltaLength > 2) {
			float q1 = (dataDelta[(int) (dataDeltaLength * 0.25)] + dataDelta[(int) (dataDeltaLength * 0.25 + 0.5)]) / 2;
			float q3 = (dataDelta[(int) (dataDeltaLength * 0.75 - 0.05)] + dataDelta[(int) (dataDeltaLength * 0.75 - 0.55)]) / 2; //subtract an extra 0.05 for integer rounding correction
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
		float sum = 0;
		
		for (int i = 0; i < dataDeltaLength; i++) {
			sum += dataDelta[i];
		}
		return (sum / dataDeltaLength);
	}

}
