package com.location.reminder.aproiri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.StringTokenizer;

import com.location.reminder.model.FrequencyItemSet;

public class AproiriAlgorithm extends Observable {

	private List<int[]> itemsets;
	private int numItems;
	private int numTransactions;
	private double minSup = 0.02;
	List<String> inputData;

	Map<Integer, List<FrequencyItemSet>> frequencyItemSetsMap = new HashMap<>();

	public AproiriAlgorithm(List<String> inputData, int numItems, int numTransactions) {

		this.inputData = inputData;
		this.numItems = numItems;
		this.numTransactions = numTransactions;
		printConfig();
		// try {
		// start();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public Map<Integer, List<FrequencyItemSet>> start() {
		setDefaultValues();
		int itemsetNumber = 1;
		int nbFrequentSets = 0;

		while (itemsets.size() > 0) {

			try {
				calculateFrequentItemsets();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (itemsets.size() != 0) {
				nbFrequentSets += itemsets.size();
				log("Found " + itemsets.size() + " frequent itemsets of size " + itemsetNumber + " (with support "
						+ (minSup * 100) + "%)");
				createNewItemsetsFromPreviousOnes();
			}

			itemsetNumber++;
		}

		log("Found " + nbFrequentSets + " frequents sets for support " + (minSup * 100) + "% (absolute "
				+ Math.round(numTransactions * minSup) + ")");
		return frequencyItemSetsMap;

	}

	private void foundFrequentItemSet(int[] itemset, int support) {

		int itemsetLength = itemset.length;
		List<FrequencyItemSet> frequencyItemSets;
		if (frequencyItemSetsMap.containsKey(itemsetLength)) {

			frequencyItemSets = frequencyItemSetsMap.get(itemsetLength);

		} else {
			frequencyItemSets = new ArrayList<>();

		}
		FrequencyItemSet frequencyItemSet = new FrequencyItemSet();
		frequencyItemSet.setItemset(itemset);
		frequencyItemSet.setSupport(support);
		frequencyItemSet.setMinpercent((support / (double) numTransactions));
		frequencyItemSets.add(frequencyItemSet);
		frequencyItemSetsMap.put(itemsetLength, frequencyItemSets);

		System.out.println(
				Arrays.toString(itemset) + "  (" + ((support / (double) numTransactions)) + " " + support + ")");

	}

	private void log(String message) {
		System.err.println(message);

	}

	private void printConfig() {
		log("Input configuration: " + numItems + " items, " + numTransactions + " transactions, ");
		log("minsup = " + minSup + "%");
	}

	private void setDefaultValues() {
		itemsets = new ArrayList<int[]>();
		for (int i = 0; i < numItems; i++) {
			int[] cand = { i };
			itemsets.add(cand);
		}
	}

	private void createNewItemsetsFromPreviousOnes() {
		int currentSizeOfItemsets = itemsets.get(0).length;
		log("Creating itemsets of size " + (currentSizeOfItemsets + 1) + " based on " + itemsets.size()
				+ " itemsets of size " + currentSizeOfItemsets);

		HashMap<String, int[]> tempCandidates = new HashMap<String, int[]>();

		for (int i = 0; i < itemsets.size(); i++) {
			for (int j = i + 1; j < itemsets.size(); j++) {
				int[] X = itemsets.get(i);
				int[] Y = itemsets.get(j);

				assert (X.length == Y.length);

				int[] newCand = new int[currentSizeOfItemsets + 1];
				for (int s = 0; s < newCand.length - 1; s++) {
					newCand[s] = X[s];
				}

				int ndifferent = 0;
				for (int s1 = 0; s1 < Y.length; s1++) {
					boolean found = false;
					for (int s2 = 0; s2 < X.length; s2++) {
						if (X[s2] == Y[s1]) {
							found = true;
							break;
						}
					}
					if (!found) {
						ndifferent++;
						newCand[newCand.length - 1] = Y[s1];
					}

				}

				assert (ndifferent > 0);

				if (ndifferent == 1) {

					Arrays.sort(newCand);
					tempCandidates.put(Arrays.toString(newCand), newCand);
				}
			}
		}

		itemsets = new ArrayList<int[]>(tempCandidates.values());
		log("Created " + itemsets.size() + " unique itemsets of size " + (currentSizeOfItemsets + 1));

	}

	private void line2booleanArray(String line, boolean[] trans) {
		Arrays.fill(trans, false);
		StringTokenizer stFile = new StringTokenizer(line, " ");
		while (stFile.hasMoreTokens()) {

			int parsedVal = Integer.parseInt(stFile.nextToken());
			trans[parsedVal] = true;
		}
	}

	private void calculateFrequentItemsets() throws Exception {

		log("Passing through the data to compute the frequency of " + itemsets.size() + " itemsets of size "
				+ itemsets.get(0).length);

		List<int[]> frequentCandidates = new ArrayList<int[]>();

		boolean match;
		int count[] = new int[itemsets.size()];

		boolean[] trans = new boolean[numItems];

		for (int i = 0; i < numTransactions; i++) {

			String line = inputData.get(i);
			line2booleanArray(line, trans);

			for (int c = 0; c < itemsets.size(); c++) {
				match = true;

				int[] cand = itemsets.get(c);

				for (int xx : cand) {
					if (trans[xx] == false) {
						match = false;
						break;
					}
				}
				if (match) {
					count[c]++;
				}
			}

		}

		for (int i = 0; i < itemsets.size(); i++) {

			if ((count[i] / (double) (numTransactions)) >= minSup) {
				foundFrequentItemSet(itemsets.get(i), count[i]);
				frequentCandidates.add(itemsets.get(i));
			}
		}

		itemsets = frequentCandidates;
	}
}
