import java.util.Map;
import java.util.TreeMap;

/**
 * Your implementation of a naive bayes classifier. Please implement all four
 * methods.
 */

public class NaiveBayesClassifierImpl implements NaiveBayesClassifier
{
	private Integer v;

	private Integer hamCount = new Integer(0);
	private Integer spamCount = new Integer(0);
	
	private Integer hamTokenCount = new Integer(0);
	private Integer spamTokenCount = new Integer(0);
	
	private Map<String, Integer> hamWords = new TreeMap<String, Integer>();
	private Map<String, Integer> spamWords = new TreeMap<String, Integer>();

	private Integer nItem;
	private double factor = 0.00001;

	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 */
	@Override
	public void train(Instance[] trainingData, int v)
	{

		this.v = v;
		// each item is a single line, # of item
		nItem = trainingData.length;

		for (Instance currentItem : trainingData)
		{
			// currInst.label
			String[] currentWords = currentItem.words;

			// increase labelCount one for each time
			if(currentItem.label==Label.HAM) {
				hamCount++;
				hamTokenCount += currentWords.length;
				
				for (String currentWord : currentWords)
				{
					// check if the map contains currWord as key
					if (!hamWords.containsKey(currentWord)) {
						hamWords.put(currentWord, 1);
					} else {
						int currentWordCount = hamWords.get(currentWord);
						hamWords.put(currentWord, currentWordCount + 1);
					}
				}
			} else {
				spamCount++;
				spamTokenCount += currentWords.length;
				for (String currentWord : currentWords)
				{
					if (!spamWords.containsKey(currentWord)) {
						spamWords.put(currentWord, 1);
					} else {
						int currentWordCount = spamWords.get(currentWord);
						spamWords.put(currentWord, currentWordCount + 1);
					}
				}
			}
		}
	}

	/**
	 * Returns the prior probability of the label parameter, i.e. P(SPAM) or
	 * P(HAM)
	 */
	@Override
	public double p_l(Label label)
	{
		// Implement
		if(label==Label.HAM) {
			return hamCount.doubleValue() / nItem.doubleValue();
		} else {
			return spamCount.doubleValue() / nItem.doubleValue();					
		}
	}

	/**
	 * Returns the smoothed conditional probability of the word given the label,
	 * i.e. P(word|SPAM) or P(word|HAM)
	 */
	@Override
	public double p_w_given_l(String word, Label label)
	{

		// Implement

		if(label==Label.HAM) {

			if (!hamWords.containsKey(word))
			// unseen word
				return factor / (v.doubleValue() * factor + hamTokenCount);
			else
			// seen word
				return (hamWords.get(word).doubleValue() + factor) / (v.doubleValue() * factor + hamTokenCount);
		} else {
			if (!spamWords.containsKey(word))
			// unseen word
				return factor / (v.doubleValue() * factor + spamTokenCount);
			else
			// seen word
				return (spamWords.get(word).doubleValue() + factor) / (v.doubleValue() * factor + spamTokenCount);
			
		}
	}

	/**
	 * Classifies an array of words as either SPAM or HAM.
	 */
	@Override
	public ClassifyResult classify(String[] words)
	{
		// Implement
		Label label = Label.SPAM;

		double log_prob_spam = ComputeLogLikelihood(words, Label.SPAM);
		double log_prob_ham = ComputeLogLikelihood(words, Label.HAM);

		if (log_prob_ham > log_prob_spam)
			label = Label.HAM;

		ClassifyResult results = new ClassifyResult();
		results.label = label;
		results.log_prob_spam = log_prob_spam;
		results.log_prob_ham = log_prob_ham;

		return results;
	}


	private double ComputeLogLikelihood(String[] words, Label label)
	{
		double likelihood = Math.log(p_l(label));
		for (String currentWord : words)
			likelihood += Math.log(p_w_given_l(currentWord, label));
		return likelihood;
	}
}
