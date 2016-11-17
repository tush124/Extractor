package extractormain;

import GUI.ExtractorGUI;

public class ExtractorMain {
	
	public static void main(String[] args){
		ExtractorGUI extractor = new ExtractorGUI();
		Thread myThread = new Thread(extractor);
		myThread.start();
	}
	

}
