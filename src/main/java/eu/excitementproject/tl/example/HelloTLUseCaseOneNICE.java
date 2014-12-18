package eu.excitementproject.tl.example;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.transform.TransformerException;

import eu.excitementproject.eop.common.EDAException;
import eu.excitementproject.eop.common.exception.ComponentException;
import eu.excitementproject.eop.common.exception.ConfigurationException;
import eu.excitementproject.tl.composition.exceptions.EntailmentGraphRawException;
import eu.excitementproject.tl.composition.exceptions.GraphMergerException;
import eu.excitementproject.tl.composition.exceptions.GraphOptimizerException;
import eu.excitementproject.tl.decomposition.exceptions.FragmentAnnotatorException;
import eu.excitementproject.tl.decomposition.exceptions.FragmentGraphGeneratorException;
import eu.excitementproject.tl.decomposition.exceptions.ModifierAnnotatorException;
import eu.excitementproject.tl.demo.UseCaseOneFromXMIs;


/**
 *
 * A simple example that shows how to use the TL for use case 1. 
 * 
 * This a simple hello-world kind of code that shows your 
 * Maven setting is Okay. 
 * 
 * Please note that actual setting is not on this 
 * "hello" file, but the Maven Project (POM file) and 
 * Maven settings (settings.xml) 
 * 
 * Note that, without proper settings.xml; your Maven will cry for "missing dependency". 
 * (e.g. cannot find TL artifact, etc) 
 * 
 * See EXCITEMENT project TL Wiki for more detail. (e.g. how to get that settings.xml file) 
 * https://github.com/hltfbk/Excitement-Transduction-Layer/wiki/How-to-use-tl-as-a-library 
 * 
 * * HOW to see TL JavaDoc, or Source? 
 * 
 * TL artifact also has Javadoc and sources with it. If you are using Eclipse --- 
 * right click on the Maven project name ("tl_example" here), and go to "Maven" 
 * menu, and select "Download JavaDoc" or "Download Source" 
 * (This will take *SOME* time, since it downloads all JavaDoc and/or source for 
 * all EOP and its related artifacts, not just TL ones.) 
 * Once the download is complete, you can see all JavaDoc and/or sources of TL layer codes. 
 * 
 * 
 * @author Vivi
 *
 */
public class HelloTLUseCaseOneNICE {

	public static void main(String[] args) {
		
		// to run for a different cluster change the cluster name
		String configFileName = "src/test/resources/EOP_configurations/MaxEntClassificationEDA_Base_EN.xml";
		String dataDir = "src/test/resources/data/nice_email_1/";
		String outputFolder = "src/test/outputs/";
		int fileNumberLimit = 4;
		
		UseCaseOneFromXMIs useOne;
		try {
			
			useOne = new UseCaseOneFromXMIs(configFileName, dataDir, fileNumberLimit, outputFolder);
			useOne.inspectResults();

		} catch (ConfigurationException | SecurityException | IllegalArgumentException
				| ComponentException
				| FragmentAnnotatorException | ModifierAnnotatorException
				| GraphMergerException | GraphOptimizerException
				| FragmentGraphGeneratorException | IOException
				| EntailmentGraphRawException | TransformerException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | EDAException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
