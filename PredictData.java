
public class PredictData {
	
	public static float[] dataDelta;
	public static int dataDeltaLength;

	public static void main(String[] args) {
		
		int predictAmount = 10; //project the data by five entries for example
		float[] data = new float[] {185.86f, 189.12f, 185.97f, 187.77f, 189.69f, 188.11f, 187.66f, 189.91f}; //most recent data first in order
		//float[] data = new float[] {5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f, 5f, 4f};
		//System.out.println(data.length);
		
		float[] patternSkip = new float[(data.length/2*(data.length/2 + 1)/2)];	//triangle number formula: 1+2+3+...n = n(n+1)/2; n = data.length / 2 because one dataDelta = 1 skipIndex
		float[] patternIgnore = new float[(data.length*(data.length + 1)/2)];	//use n = data.length because one skipIndex = 2 dataDelta
		for (int i = 0; i < patternSkip.length; i++) {
			patternSkip[i] = 0;
		}
		for (int i = 0; i < patternIgnore.length; i++) {
			patternIgnore[i] = 0;
		}
		//The pattern is relative to the starting data point
		//some of the cells will be empty for when the pattern interval is too large and therefore lacking data to guess pattern.
		//Unless the pattern is guessed solely on the single data value
		
		float[] predictedData = new float[predictAmount];
		
		//*******************************find the pattern for each data interval
		dataDelta = new float[data.length - 1];		//create it only once to allocate space only once for the largest possible array
		float newPattern;
		
		
		//PATTERN IGNORE *************************************************************************************************************************
		for (int skipIndex = 1; skipIndex + 1 < data.length; skipIndex++) {
			for (int shiftIndex = 0; (shiftIndex < skipIndex) && (shiftIndex + (skipIndex + 1) < data.length); shiftIndex++) {
				dataDeltaLength = 1;		//reset array
				dataDelta[0] = data[shiftIndex] - data[shiftIndex + 1];
				
				
				/*if (skipIndex < 5) {		//***********************
					//System.out.println("data length = " + data.length + "\nskip index = " + skipIndex + "\niterations = " + ((data.length - 1 - shiftIndex - 1)/skipIndex + 1) + "\n" + (data.length - ((skipIndex * (((data.length - 1 - shiftIndex - 1) / skipIndex) % 2)) ^ 1)) + "\n");
					System.out.println(skipIndex * ((((data.length - 1 - shiftIndex) / skipIndex) % 2) ^ 1));
				}*/
				
				
				for (int i = shiftIndex + skipIndex + 1; i < data.length - (skipIndex * ((((data.length - 1 - shiftIndex - 1) / skipIndex) % 2) ^ 1)); i += skipIndex, dataDeltaLength++) {
					addDelta(data[i - 1] - data[i]);
				}
				newPattern = getAverage();
				
				/*if (skipIndex < 5) {		//***********************
					System.out.println(newPattern);
				}*/
				
				//subtract from current average the value of (the averages from lower order skipValues that overlap with this one times the amount of their intervals that fit into the current interval)
				for (int i = 1; i < skipIndex; i++) {
					if (skipIndex % i == 0) {		//if skipIndex is completely divisible by i
						if (shiftIndex < i) {
							newPattern -= patternIgnore[i * (i - 1) / 2 + shiftIndex];
						}
						else {
							newPattern -= patternIgnore[i * (i - 1) / 2 + (shiftIndex % i)];
						}
						
					}
					else {						//else calculate the average change as a result of i over current skipInterval and subtract from tmpStorage
						float tmp = 0;								//temporary value storage
						for (int j = 0; j < i; j++) {
							tmp += patternIgnore[i * (i - 1) / 2 + j];
						}
						newPattern -= tmp / i;
					}
				}
				
				//place final value into patternIgnore[] array
				patternIgnore[skipIndex * (skipIndex - 1) / 2 + shiftIndex] = newPattern;
			}
		}
		
									//*****************
		/*for (int i = 0, j = 0; i < 50; i++) {		
			//System.out.println("i = " + i);
			if (j*(j+1)/2 == i) {
				System.out.println();
				j++;
			}
			System.out.println(patternIgnore[i]);
		}*/
		
		
		//predict the data using the pattern
		System.out.print("Pattern Ignore Algorithm: ");
		for (int i = 1, patternIndex; i <= predictAmount; i++) {		//i is both iteration quantity and the reverse shift index
			predictedData[i - 1] = (i == 1) ? data[0] : predictedData[i - 2];
			for (int skipIndex = 1; skipIndex + 1 < data.length; skipIndex++) {
				if (i < skipIndex) {
					patternIndex = (skipIndex * (skipIndex + 1)) / 2 - i;
				}
				else {
					patternIndex = (i % skipIndex == 0) ? ((skipIndex * (skipIndex - 1)) / 2) : ((skipIndex * (skipIndex + 1)) / 2 - (i % skipIndex));	//	(n*(n+1))/2 - n == (n*(n-1))/2  
				}
				predictedData[i - 1] += patternIgnore[patternIndex];
			}
			System.out.print(String.format("%.2f", predictedData[i - 1]) + " ");
		}
		
		
		
		
		
		
		
		//PATTERN SKIP ********************************************************************************************************************
		for (int skipIndex = 1; skipIndex * 2 < data.length; skipIndex++) {		//< data.length //process pattern intervals until the pattern interval is the same size as the entire data series
			
			for (int shiftIndex = 0; (shiftIndex < skipIndex) && (shiftIndex + (skipIndex * 2) < data.length); shiftIndex++) {	//process pattern all pattern intervals for current skip index
				dataDeltaLength = 1;		//reset array
				dataDelta[0] = data[shiftIndex] - data[shiftIndex + skipIndex];		//initialize first value before starting ordering procedure. Otherwise loop below fails
				
				for (int i = shiftIndex + (skipIndex * 2); i < data.length - (skipIndex * (((data.length - shiftIndex - 1) / skipIndex) % 2)); i += skipIndex, dataDeltaLength++) {	//process individual pattern interval	//if pattern exists, don't let an odd amount of data distort it. Thus  - (skipIndex * (((data.length - shiftIndex) / skipIndex) % 2))
					//*****Order values of (data[i - skipIndex] - data[i]) in ascending order into dataDelta array
					addDelta(data[i - skipIndex] - data[i]);
				}
				
				/*for (int i = 0; i < dataDeltaLength; i++) {			//*****************
					System.out.println(dataDelta[i]);
				}
				System.out.println("skipIndex = " + skipIndex + " : shiftIndex = " + shiftIndex);		//*****************
				*/
				
				newPattern = getAverage();
				
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
							newPattern -= patternSkip[i * (i - 1) / 2 + shiftIndex] * (skipIndex / i);
						}
						else {
							newPattern -= patternSkip[i * (i - 1) / 2 + (shiftIndex % i)] * (skipIndex / i);
						}
						
						//System.out.println("skipIndex = " + skipIndex + " i = " + i);		//*****************
					}
					else {						//else calculate the average change as a result of i over current skipInterval and subtract from tmpStorage
						float tmp1 = 0;								//temporary value storage
						float tmp2 = ((float) skipIndex) / i;		//temporary value storage to speed up loop below
						for (int j = 0; j < i; j++) {
							tmp1 += patternSkip[i * (i - 1) / 2 + j] * tmp2;
						}
						newPattern -= tmp1 / i;
					}
				}
				
				//place final value into patternSkip[] array
				patternSkip[skipIndex * (skipIndex - 1) / 2 + shiftIndex] = newPattern;
			}
		}
		
		
		/*int j = 0;							//*****************
		for (int i = 0; i < 50; i++) {		
			//System.out.println("i = " + i);
			if (j*(j+1)/2 == i) {
				System.out.println();
				j++;
			}
			System.out.println(patternSkip[i]);
		}
		*/
		
		
		//predict the data using the pattern
		System.out.print("\nPattern Skip Algorithm:   ");
		for (int i = 1, patternIndex; i <= predictAmount; i++) {		//i is both iteration quantity and the reverse shift index
			predictedData[i - 1] = (i == 1) ? data[0] : predictedData[i - 2];
			for (int skipIndex = 1; skipIndex * 2 < data.length; skipIndex++) {
				if (i < skipIndex) {
					patternIndex = (skipIndex * (skipIndex + 1)) / 2 - i;
				}
				else {
					patternIndex = (i % skipIndex == 0) ? ((skipIndex * (skipIndex - 1)) / 2) : ((skipIndex * (skipIndex + 1)) / 2 - (i % skipIndex));	//	(n*(n+1))/2 - n == (n*(n-1))/2  
				}
				predictedData[i - 1] += patternSkip[patternIndex];
			}
			System.out.print(String.format("%.2f", predictedData[i - 1]) + " ");
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
