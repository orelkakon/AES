
public class Main {
	public static void main(String [] args) {
		boolean flagEnc = false;
		boolean flagBreak = false;
		String keysPath = "";
		String inputPath = "";
		String MinputPath = "";
		String CinputPath = "";
		String outputPath = "";
		for(int i=0; i<args.length;i++) {
			if(args[i].equals("-e"))
				flagEnc = true;
			if(args[i].equals("-k")) {
				keysPath = args[i+1];
				i++;
			}
			if(args[i].equals("-i")) {
				inputPath = args[i+1];
				i++;
			}
			if(args[i].equals("-o")) {
				outputPath = args[i+1];
				i++;
			}	
			if(args[i].equals("-b")) {
				flagBreak = true;
			}
			if(args[i].equals("-m")) {
				MinputPath = args[i+1];
				i++;
			}
			if(args[i].equals("-c")) {
				CinputPath = args[i+1];
				i++;
			}
		}	
		if(flagBreak) {
			Break BB = new Break();
			BB.readPlainText(MinputPath);
			BB.readCipherText(CinputPath);
			BB.getOutputPath(outputPath);
			BB.breakKeys();
		}
		else {
			EncDec ED = new EncDec();
			ED.getKeys(keysPath);
			ED.getInputFile(inputPath);
			ED.getOutputPath(outputPath);
			if(flagEnc) {
				ED.encryption();
			}
			else
				ED.decryption();
		}
	}
}
