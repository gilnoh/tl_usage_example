package eu.excitementproject.tl.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.jcas.JCas;
import org.junit.Assert;

import eu.excitement.type.tl.CategoryAnnotation;
import eu.excitement.type.tl.CategoryDecision;
import eu.excitementproject.eop.lap.LAPAccess;
import eu.excitementproject.eop.lap.LAPException;
import eu.excitementproject.tl.composition.api.CategoryAnnotator;
import eu.excitementproject.tl.composition.api.ConfidenceCalculator;
import eu.excitementproject.tl.composition.api.GraphOptimizer;
import eu.excitementproject.tl.composition.api.NodeMatcher;
import eu.excitementproject.tl.composition.categoryannotator.CategoryAnnotatorAllCats;
import eu.excitementproject.tl.composition.confidencecalculator.ConfidenceCalculatorCategoricalFrequencyDistribution;
import eu.excitementproject.tl.composition.exceptions.CategoryAnnotatorException;
import eu.excitementproject.tl.composition.exceptions.ConfidenceCalculatorException;
import eu.excitementproject.tl.composition.exceptions.GraphOptimizerException;
import eu.excitementproject.tl.composition.exceptions.NodeMatcherException;
import eu.excitementproject.tl.composition.graphoptimizer.SimpleGraphOptimizer;
import eu.excitementproject.tl.composition.nodematcher.NodeMatcherLongestOnly;
import eu.excitementproject.tl.decomposition.api.FragmentAnnotator;
import eu.excitementproject.tl.decomposition.api.FragmentGraphGenerator;
import eu.excitementproject.tl.decomposition.api.ModifierAnnotator;
import eu.excitementproject.tl.decomposition.exceptions.FragmentAnnotatorException;
import eu.excitementproject.tl.decomposition.exceptions.FragmentGraphGeneratorException;
import eu.excitementproject.tl.decomposition.exceptions.ModifierAnnotatorException;
import eu.excitementproject.tl.decomposition.fragmentannotator.SentenceAsFragmentAnnotator;
import eu.excitementproject.tl.decomposition.fragmentgraphgenerator.FragmentGraphGeneratorFromCAS;
import eu.excitementproject.tl.decomposition.modifierannotator.AdvAsModifierAnnotator;
import eu.excitementproject.tl.laputils.CASUtils;
import eu.excitementproject.tl.laputils.LemmaLevelLapEN;
import eu.excitementproject.tl.structures.collapsedgraph.EntailmentGraphCollapsed;
import eu.excitementproject.tl.structures.fragmentgraph.FragmentGraph;
import eu.excitementproject.tl.structures.rawgraph.EntailmentGraphRaw;
import eu.excitementproject.tl.structures.search.NodeMatch;
/**
 * 
 * A simple example that shows how to use the TL for use case 2. 
 * 
 * This a simple hello-world kind of code that shows your 
 * Maven setting is Okay. 
 * 
 * Please note that actual setting example is not this 
 * "hello" file, but the Maven Project (POM file) and 
 * Maven settings (settings.xml) 
 * 
 * Note that, without proper settings.xml; your Maven will cry for "missing dependency". 
 * (e.g. cannot find TreeTagger related artifacts, etc) 
 * 
 * See EXCITEMENT project TL Wiki for more detail. (e.g. how to get that settings.xml file) 
 * TODO (put that URL here once the doc is done) 
 * 
 * @author Kathrin
 */
public class HelloUseCase2 {

	/**
	 */
	public static void main(String[] args) {

		System.out.println("In the first step, we will create a sample entailment graph "
				+ "to be used for email categorization (use case 2)."); 
		EntailmentGraphRaw rawGraph = EntailmentGraphRaw.getSampleOuputWithCategories(false); 	
		System.out.println("Read sample raw entailment graph."); 
		GraphOptimizer cgg = new SimpleGraphOptimizer();
		EntailmentGraphCollapsed entailmentGraph = null;
		try {
			entailmentGraph = cgg.optimizeGraph(rawGraph);
			System.out.println("Optimized entailment graph."); 
			ConfidenceCalculator cc = new ConfidenceCalculatorCategoricalFrequencyDistribution();
			cc.computeCategoryConfidences(entailmentGraph);
			System.out.println("Computed category confidence scores and added them to graph."); 
		} catch (GraphOptimizerException e1) {
			e1.printStackTrace();
		} catch (ConfidenceCalculatorException e) {
			e.printStackTrace();
		} 
		System.out.println("Entailment graph for email categorization has been successfully created!");
		System.out.println("------------------------------------------------------------------------");
		System.out.println("In the second step, we will process an input text and add category "
				+ "annotation to it, using the graph we have just created."); 
		JCas aJCas = null; 
		LAPAccess lap = null; 
		try {
			aJCas = CASUtils.createNewInputCas(); 
			aJCas.setDocumentText("Hello Mister Grey. Disappointed with legroom"); 
			aJCas.setDocumentLanguage("EN"); 						
			lap = new LemmaLevelLapEN(); 
			lap.addAnnotationOn(aJCas); 
		}
		catch (Exception e)
		{
			System.out.println("Oh. Exception --- " + e.getMessage()); 
			System.out.println("If your Maven cries about 'missing dependency' --- please make sure that you have properly loaded the settings.xml in your maven (.m2) directory"); 
			System.exit(1); 
		}
		System.out.println(aJCas.getDocumentText()); 
		System.out.println("Okay. The maven project is properly setup, TreeTagger seems to run okay, and we can access classes in TL artifact correctly."); 
		
		FragmentAnnotator fa;
		try {
			fa = new SentenceAsFragmentAnnotator(lap);
			fa.annotateFragments(aJCas);
			ModifierAnnotator ma;
			ma = new AdvAsModifierAnnotator(lap);
			ma.annotateModifiers(aJCas);
			FragmentGraphGenerator fgg = new FragmentGraphGeneratorFromCAS();
			Set<FragmentGraph> fragmentGraphs;
			fragmentGraphs = fgg.generateFragmentGraphs(aJCas);
			System.out.println("Processed input text, adding fragments and modifiers.");
			NodeMatcher nm = new NodeMatcherLongestOnly(entailmentGraph); 
			for (FragmentGraph fragmentGraph : fragmentGraphs) {
				Set<NodeMatch> matches;
				matches = nm.findMatchingNodesInGraph(fragmentGraph);
				CategoryAnnotator ca = new CategoryAnnotatorAllCats();
				ca.addCategoryAnnotation(aJCas, matches);
			}
			System.out.println("Matched input text against graph and added the following category annotations:"); 
			Set<CategoryDecision> decisions = CASUtils.getCategoryAnnotationsInCAS(aJCas);
		
			Map<String,Integer> categoryScores = new HashMap<String,Integer>();
			for (CategoryDecision decision: decisions) {
				String category = decision.getCategoryId();
				int count = 0; //the sum of scores for a particular category 
				if (categoryScores .containsKey(category)) {
					count = categoryScores.get(category);
				}
				count++;
				categoryScores.put(category, count);
			}
			for (String category : categoryScores.keySet()) {
				System.out.println("category " + category + " occurs " + categoryScores.get(category) + " time(s)");
			}		
		} catch (FragmentAnnotatorException e) {
			e.printStackTrace();
		} catch (ModifierAnnotatorException e) {
			e.printStackTrace();
		} catch (FragmentGraphGeneratorException e) {
			e.printStackTrace();
		} catch (NodeMatcherException e) {
			e.printStackTrace();
		} catch (LAPException e) {
			e.printStackTrace();
		} catch (CategoryAnnotatorException e) {
			e.printStackTrace();
		}
	}
}
