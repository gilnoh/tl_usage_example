package eu.excitementproject.tl.example;

import eu.excitementproject.eop.core.MaxEntClassificationEDA;
import eu.excitementproject.eop.lap.dkpro.TreeTaggerEN;
import eu.excitementproject.tl.demo.DemoUseCase1NICEEnglish;


public class HelloTLUseCaseOneNICE {

	public static void main(String[] args) {

		
		String configFileName = "src/test/resources/EOP_configurations/MaxEntClassificationEDA_Base_EN.xml";
		String dataDir = "src/test/resources/data/nice_email_1/";
		String outputFolder = "src/test/outputs/";
		int fileNumberLimit = 4;
		
		DemoUseCase1NICEEnglish demoEN = new DemoUseCase1NICEEnglish(configFileName, dataDir, fileNumberLimit, outputFolder, TreeTaggerEN.class, MaxEntClassificationEDA.class);		
		demoEN.inspectResults();		
	}
	
}
