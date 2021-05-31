import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
 
public class sim {
  public static void main(String[] args) throws IOException {

	String counterName = args[0];
	int counterBit = Integer.parseInt(args[1]);
    	File file = new File(args[args.length - 1]);
    	Scanner input = new Scanner(file);
	ArrayList<String> actual = new ArrayList<>();
	ArrayList<String> PC = new ArrayList<>();
    	//int count = 0;
    	while (input.hasNext()) {
      		String word  = input.next();
      		if(word.length() == 2) actual.add(word);
		else PC.add(word);
    	}
	int correct = 0;
	char prediction = 'b';
	if(counterName.equals("smith")){
	//int correct = 0;
	int counter = 0;
	if(counterBit == 1) counter = 1;
	else if(counterBit == 2) counter = 2;
	else if(counterBit == 3) counter = 4;
	else counter = 8;
	//char prediction = 'a';
	for(int i = 0; i < actual.size(); i++){
		if(counterBit == 1) {
			if (counter > 0) prediction = 't';
			else prediction = 'n';
		}
		else if(counterBit == 2) {
			if (counter > 1) prediction = 't';
			else prediction = 'n';
		}
		else if(counterBit == 3) {
			if (counter > 3) prediction = 't';
			else prediction = 'n';
		}
		else {
			if (counter > 7) prediction = 't';
			else prediction = 'n';
		}
		if(actual.get(i).charAt(0) == prediction) correct++;
		if(actual.get(i).charAt(0) == 't'){
			if (counterBit == 1 && counter != 1) counter++;
			else if (counterBit == 2 && counter != 3) counter++;
			else if (counterBit == 3 && counter != 7) counter++;
			else if (counterBit == 4 && counter != 15) counter++;
		}
		else if (counter != 0) counter--;
  	}
	int miss = actual.size() - correct;
	double rate = miss * 100.00 / actual.size();
	System.out.println("COMMAND");
	System.out.println("./sim smith " + counterBit + " " + file);
	System.out.println("OUTPUT");
	System.out.println("number of predictions:          " + actual.size());
	System.out.println("number of mispredictions:       " + miss);
	System.out.println("misprediction rate:             " + String.format("%.2f", rate) + "%");
	System.out.println("FINAL COUNTER CONTENT:          " + counter);
	}
	if(counterName.equals("bimodal")){
		//int a = Integer.parseInt(args[2]);
		
		HashMap<Integer, Integer> counter1 = new HashMap<>();
		for(int i = 0; i < Math.pow(2, counterBit); i++) counter1.put(i, 4);
		//int correcPred = 0;
		for(int i = 0; i < actual.size(); i++){
			int k = counterBit;
			int decimalAddress = Integer.parseInt(PC.get(i), 16);
			decimalAddress = decimalAddress >> 2;
			StringBuilder sb = new StringBuilder();
        		while(k > 0) {
            			sb.append((decimalAddress & 1));
            			decimalAddress = decimalAddress >> 1;
            			k--;
        		}
        		String ab = sb.reverse().toString();
        		int index = Integer.parseInt(ab, 2);
			if(counter1.get(index) > 3) prediction = 't';
			else prediction = 'n';
			if(actual.get(i).charAt(0) == prediction) correct++;
			if(actual.get(i).charAt(0) == 't' && counter1.get(index) != 7) counter1.put(index, counter1.get(index) + 1);
			else if(actual.get(i).charAt(0) == 'n' && counter1.get(index) != 0) counter1.put(index, counter1.get(index) - 1);
		}
		int miss = actual.size() - correct;
		double rate = miss * 100.00 / actual.size();
		System.out.println("COMMAND");
		System.out.println("./sim bimodal " + counterBit + " " + file);
		System.out.println("OUTPUT");
		System.out.println("number of predictions:          " + actual.size());
		System.out.println("number of mispredictions:       " + miss);
		System.out.println("misprediction rate:             " + String.format("%.2f", rate) + "%");
		System.out.println("FINAL BIMODAL CONTENTS");
		for(int i = 0; i < counter1.size(); i++){
			System.out.println(i + "      " + counter1.get(i));
		}
			
	}
	
	if(counterName.equals("gshare")){
		int GHRbits = Integer.parseInt(args[2]);
		int GHR = 0;
		StringBuilder sb2 = new StringBuilder();
		sb2.append('1');
		for(int i = 1; i < GHRbits; i++) sb2.append('0');
		String temp = sb2.toString();
		int mask1 = Integer.parseInt(temp, 2);
		StringBuilder sb3 = new StringBuilder();
		sb2.append('0');
		for(int i = 1; i < GHRbits; i++) sb2.append('1');
		String temp2 = sb2.toString();
		int mask2 = Integer.parseInt(temp2, 2);
		HashMap<Integer, Integer> counter1 = new HashMap<>();
		for(int i = 0; i < Math.pow(2, counterBit); i++) counter1.put(i, 4);
		for(int i = 0; i < actual.size(); i++){
			int k = counterBit;
			int decimalAddress = Integer.parseInt(PC.get(i), 16);
			decimalAddress = decimalAddress >> 2;
			StringBuilder sb = new StringBuilder();
        		while(k > 0) {
            			sb.append((decimalAddress & 1));
            			decimalAddress = decimalAddress >> 1;
            			k--;
        		}
        		String ab = sb.reverse().toString();
        		int index = Integer.parseInt(ab, 2);
			index = index ^ GHR;
			GHR = GHR >> 1;
			if(counter1.get(index) > 3) prediction = 't';
			else prediction = 'n';
			if(actual.get(i).charAt(0) == prediction) correct++;
			if(actual.get(i).charAt(0) == 't') {
				GHR = GHR | mask1;
				if (counter1.get(index) != 7) counter1.put(index, counter1.get(index) + 1);
			}
			else {
				GHR = GHR & mask2;
				if(counter1.get(index) != 0) counter1.put(index, counter1.get(index) - 1);
			}
		}
		int miss = actual.size() - correct;
		double rate = miss * 100.00 / actual.size();
		System.out.println("COMMAND");
		System.out.println("./sim gshare " + counterBit + " " + GHRbits + " " + file);
		System.out.println("OUTPUT");
		System.out.println("number of predictions:          " + actual.size());
		System.out.println("number of mispredictions:       " + miss);
		System.out.println("misprediction rate:             " + String.format("%.2f", rate) + "%");
		System.out.println("FINAL GSHARE CONTENTS");
		for(int i = 0; i < counter1.size(); i++){
			System.out.println(i + "      " + counter1.get(i));
		}
			
	}

	if(counterName.equals("hybrid")){
		int k = counterBit;
		HashMap<Integer, Integer> chooser = new HashMap<>();
		for(int i = 0; i < Math.pow(2, k); i++) chooser.put(i, 1);


		int M2 = Integer.parseInt(args[4]);
		HashMap<Integer, Integer> counter1 = new HashMap<>();
		for(int i = 0; i < Math.pow(2, M2); i++) counter1.put(i, 4);

		int GHRbits = Integer.parseInt(args[3]);
		int GHR = 0;
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append('1');
		for(int i = 1; i < GHRbits; i++) sb2.append('0');
		String temp = sb2.toString();
		int mask1 = Integer.parseInt(temp, 2);
		//System.out.println(mask1);

		StringBuilder sb3 = new StringBuilder();
		sb2.append('0');
		for(int i = 1; i < GHRbits; i++) sb3.append('1');
		String temp2 = sb3.toString();
		int mask2 = Integer.parseInt(temp2, 2);
		//System.out.println(mask2);

		int M1 = Integer.parseInt(args[2]);
		HashMap<Integer, Integer> counter2 = new HashMap<>();
		for(int i = 0; i < Math.pow(2, M1); i++) counter2.put(i, 4);

		int hybridcount = 0;
		
		
		for(int i = 0; i < actual.size(); i++){
			char bimodalprediction = 'a', gshareprediction = 'b';
			int gshare = 0, bimodal = 0;
			int indexChooser = getIndex(PC.get(i), k);
			int indexGshare = getIndex(PC.get(i), M1);
			int indexBimodal = getIndex(PC.get(i), M2);
			if(counter1.get(indexBimodal) > 3) bimodalprediction = 't';
			else bimodalprediction = 'n';
			if(actual.get(i).charAt(0) == bimodalprediction) bimodal = 1;
			else bimodal = 0;
			indexGshare = indexGshare ^ GHR;
			GHR = GHR >> 1;
			if(counter2.get(indexGshare) > 3) gshareprediction = 't';
			else gshareprediction = 'n';
			if(actual.get(i).charAt(0) == gshareprediction) gshare = 1;
			else gshare = 0;
			
			if(chooser.get(indexChooser) > 1){
				if(actual.get(i).charAt(0) == 't') {
					GHR = GHR | mask1;
					if (counter2.get(indexGshare) != 7) counter2.put(indexGshare, counter2.get(indexGshare) + 1);
				}
				else {
					GHR = GHR & mask2;
					if(counter2.get(indexGshare) != 0) counter2.put(indexGshare, counter2.get(indexGshare) - 1);
				}
				if(gshare == 1) hybridcount++;
			}
			else {
				if(actual.get(i).charAt(0) == 't') {
  					if (counter1.get(indexBimodal) != 7) counter1.put(indexBimodal, counter1.get(indexBimodal) + 1);
					GHR = GHR | mask1;
				}
				else { 
					if (counter1.get(indexBimodal) != 0) counter1.put(indexBimodal, counter1.get(indexBimodal) - 1);
					GHR = GHR & mask2;
				}
				if(bimodal == 1) hybridcount++;
			}
			if(gshare == 1 && bimodal == 0 && chooser.get(indexChooser) != 3) chooser.put(i, chooser.get(indexChooser) + 1); 
			if(gshare == 0 && bimodal == 1 && chooser.get(indexChooser) != 0) chooser.put(i, chooser.get(indexChooser) - 1);
		}


		int miss = actual.size() - hybridcount;
		double rate = miss * 100.00 / actual.size();
		System.out.println("COMMAND");
		System.out.println("./sim hybrid " + counterBit +" " + M1 + " " + GHRbits + " " + M2 + " " + file);
		System.out.println("OUTPUT");
		System.out.println("number of predictions:          " + actual.size());
		System.out.println("number of mispredictions:       " + miss);
		System.out.println("misprediction rate:             " + String.format("%.2f", rate) + "%");
		System.out.println("FINAL CHOOSER CONTENTS");
		for(int i = 0; i < counter1.size(); i++){
			System.out.println(i + "      " + counter1.get(i));
		}
	}
}
		public static int getIndex(String address, int p){

			int decimalAddress = Integer.parseInt(address, 16);
			decimalAddress = decimalAddress >> 2;
			StringBuilder sb = new StringBuilder();
        		while(p > 0) {
            			sb.append((decimalAddress & 1));
            			decimalAddress = decimalAddress >> 1;
            			p--;
        		}
        		String ab = sb.reverse().toString();
        		int index = Integer.parseInt(ab, 2);
			return index;
		}
		
		
}
 