import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Break {
	public byte[][] M;
	public byte[][] C;
	public byte[] k1 = new byte[16];
	public byte[] k2 = new byte[16];
	public byte[] k3 = new byte[16];
	public Path outputPath;

	public Break() {
		
	}
	
	public void readPlainText(String path) {
		Path newPath = Paths.get(path);
		byte[] temp;
		try {
			temp = Files.readAllBytes(newPath);
			int numBlocks = temp.length / 16;
			int indexTemp=0;
			M= new byte[numBlocks][16];
			for(int i=0;i<numBlocks;i++) {
				for(int j=0;j<16;j++) {
					M[i][j] = temp[indexTemp];
					indexTemp++;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readCipherText(String path) {
		Path newPath = Paths.get(path);
		byte[] temp;
		try {
			temp = Files.readAllBytes(newPath);
			int numBlocks = temp.length / 16;
			C = new byte[numBlocks][16]; 
			int indexTemp=0;
			for(int i=0;i<numBlocks;i++) {
				for(int j=0;j<16;j++) {
					C[i][j] = temp[indexTemp];
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
	
	public void breakKeys() {
		new Random().nextBytes(k1);
		new Random().nextBytes(k2);
		byte[] C_tag = ShiftRows(AES1(AES1(M[0],k1),k2));
		for(int i=0;i<C[0].length;i++) {
			k3[i] = (byte) (C[0][i] ^ C_tag[i]); 	
		}
		// load to file
		ByteArrayOutputStream B = new ByteArrayOutputStream();
		try {
			B.write(k1);
			B.write(k2);
			B.write(k3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] result = B.toByteArray();
		try {
			Files.write(outputPath, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private byte[] AES1(byte[] M, byte[] k) {
		return AddRoundKey(ShiftRows(M),k);
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

	private byte[] AddRoundKey(byte[] shiftRowsM, byte[] k) {
		byte[] result=new byte[shiftRowsM.length];
		for(int i=0;i<shiftRowsM.length;i++) {
			result[i]=(byte) (shiftRowsM[i] ^ k[i]);//XOR
		}
		return result;
	}

}
