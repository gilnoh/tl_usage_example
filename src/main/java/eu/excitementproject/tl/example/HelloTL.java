package eu.excitementproject.tl.example;

import org.apache.uima.jcas.JCas;

import eu.excitementproject.eop.lap.LAPAccess;
import eu.excitementproject.tl.laputils.CASUtils;
import eu.excitementproject.tl.laputils.LemmaLevelLapEN;
/**
 * 
 * A very simple example that shows how to "depend" on 
 * TL layer. 
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
 * @author Gil
 */
public class HelloTL {

	/**
	 */
	public static void main(String[] args) {
		
		// Hmm. Let's check we can use any methods in TL. 
		// Say, something like LAPUtils? 		
		JCas aJCas = null; 
		LAPAccess lap = null; 
		try {
			aJCas = CASUtils.createNewInputCas(); 
			aJCas.setDocumentText("Hello TL?"); 
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
		
		// Oh. it works well. Please note that 
		// You can also check JavaDoc, and even source, 
		// if you set your Maven to load the Javadoc and Source. 
		
		// if you are using Eclipse --- right click on the 
		// Maven project name, and go to "Maven" menu, and 
		// select "Download JavaDoc" or "Download Source" 
		// (This will take *SOME* time, since it downloads 
		// all JavaDoc and/or source, not just TL ones). 
		// Once the download is complete, you can see all 
		// JavaDoc and/or sources of TL layer codes. 
		
		// TODO --- add some more example that shows usecase 1 
		// or use case 2 usage. 
		
	}

}
