import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class EncDec {
	public byte[][] input;
	public byte[][]output;
	public byte[] allFileKeys;
	public byte[] k1 = new byte[16];
	public byte[] k2 = new byte[16];
	public byte[] k3 = new byte[16];
	Path outputPath;
	public EncDec() {
		
	} 
	
	public void getKeys(String path) {
		Path newPath = Paths.get(path);
		try {
			allFileKeys = Files.readAllBytes(newPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<allFileKeys.length;i++) {
			if(i<16)
				k1[i]=allFileKeys[i];
			else if(i<32)
				k2[i-16]=allFileKeys[i];
			else
				k3[i-32]= allFileKeys[i];
		}
	}
	
	public void getInputFile(String path) {
		Path newPath = Paths.get(path);
		byte[] temp;
		try {
			temp = Files.readAllBytes(newPath);
			int numBlocks = temp.length / 16;
			int indexTemp=0;
			input = new byte[numBlocks][16];
			for(int i=0;i<numBlocks;i++) {
				for(int j=0;j<16;j++) {
					input[i][j] = temp[indexTemp];
					indexTemp++;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getOutputPath(String path) {
		outputPath = Paths.get(path);
	}

	public void encryption() {
		//for on input
		output = new byte[input.length][16];
		for(int i=0;i<input.length;i++) {
			output[i]=AES1(AES1(AES1(input[i],k1),k2),k3);
		}
		//load output to output file
		loadToOutputFile();
	}

	private void loadToOutputFile() {
		byte[] newOutput = new byte[output.length * 16];
		int index =0;
		for(int i=0;i<output.length;i++) {
			for(int j = 0; j<16;j++) {
				newOutput[index]= output[i][j];
				index++;
			}
		}
		try {
			Files.write(outputPath, newOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void decryption() {
		output = new byte[input.length][16];
		for(int i=0;i<input.length;i++) {
			output[i]=AES_1(AES_1(AES_1(input[i],k3),k2),k1);
		}
		loadToOutputFile();
		//load output to output file
	}
	
	private byte[] AES_1(byte[] C, byte[] k) {
		return ShiftRows_(AddRoundKey(C,k));
	} 
	
	private byte[] ShiftRows_(byte[] Round_C) {
		// TODO Auto-generated method stub
		byte[] result=new byte[Round_C.length];
		result[0]=Round_C[0];
		result[5]=Round_C[1];
		result[10]=Round_C[2];
		result[15]=Round_C[3];
		result[4]=Round_C[4];
		result[9]=Round_C[5];
		result[14]=Round_C[6];
		result[3]=Round_C[7];
		result[8]=Round_C[8];
		result[13]=Round_C[9];
		result[2]=Round_C[10];
		result[7]=Round_C[11];
		result[12]=Round_C[12];
		result[1]=Round_C[13];
		result[6]=Round_C[14];
		result[11]=Round_C[15];
		return result;
	}

	private byte[] AES1(byte[] M, byte[] k) {
		return AddRoundKey(ShiftRows(M),k);
	}

	private byte[] AddRoundKey(byte[] shiftRowsM, byte[] k) {
		byte[] result=new byte[shiftRowsM.length];
		for(int i=0;i<shiftRowsM.length;i++) {
			result[i]=(byte) (shiftRowsM[i] ^ k[i]);//XOR
		}
		return result;
	}

	private byte[] ShiftRows(byte[] M) {
		byte[] result=new byte[M.length];
		result[0]=M[0];
		result[1]=M[5];
		result[2]=M[10];
		result[3]=M[15];
		result[4]=M[4];
		result[5]=M[9];
		result[6]=M[14];
		result[7]=M[3];
		result[8]=M[8];
		result[9]=M[13];
		result[10]=M[2];
		result[11]=M[7];
		result[12]=M[12];
		result[13]=M[1];
		result[14]=M[6];
		result[15]=M[11];
		return result;
	}
	
}
