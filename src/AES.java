import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.IOException;  

public class AES {
	private static String inputFile, keyFile;
	static FileWriter out;

	public static void subBytes(int[] state){

		for(int i = 0; i < 16; i++){				
			state[i] = Tables.sbox[state[i]];
		}
	}

	public static void invSubBytes(int [] state){

		for(int i = 0; i < 16; i++){						
			state[i] = Tables.invSbox[state[i]];
		}
	}

	// Shift left
	public static void shiftRows(int[] state){
		int[] temp = new int[16];

		temp[0] = state[0];
		temp[1] = state[5];
		temp[2] = state[10];
		temp[3] = state[15];

		temp[4] = state[4];
		temp[5] = state[9];
		temp[6] = state[14];
		temp[7] = state[3];

		temp[8] = state[8];
		temp[9] = state[13];
		temp[10] = state[2];
		temp[11] = state[7];

		temp[12] = state[12];
		temp[13] = state[1];
		temp[14] = state[6];
		temp[15] = state[11];

		for(int i = 0; i < 16; i++){
			state[i] = temp[i];
		}
	}

	// Inverse of shiftRows, shifts right instead of left
	public static void invShiftRows(int[] state){ 
		int[] temp = new int[16];

		temp[0] = state[0];
		temp[1] = state[13];
		temp[2] = state[10];
		temp[3] = state[7];

		temp[4] = state[4];
		temp[5] = state[1];
		temp[6] = state[14];
		temp[7] = state[11];

		temp[8] = state[8];
		temp[9] = state[5];
		temp[10] = state[2];
		temp[11] = state[15];

		temp[12] = state[12];
		temp[13] = state[9];
		temp[14] = state[6];
		temp[15] = state[3];

		for(int i = 0; i < 16; i++){
			state[i] = temp[i];
		}
	}

	public static void mixColumns(int[] state){
		int[] temp = new int[16];

		temp[0] = (Tables.mul2[state[0]] ^ Tables.mul3[state[1]] ^ state[2] ^ state[3]);
		temp[1] = (state[0] ^ Tables.mul2[state[1]] ^ Tables.mul3[state[2]] ^ state[3]);
		temp[2] = (state[0] ^ state[1] ^ Tables.mul2[state[2]] ^ Tables.mul3[state[3]]);
		temp[3] = (Tables.mul3[state[0]] ^ state[1] ^ state[2] ^ Tables.mul2[state[3]]);

		temp[4] = (Tables.mul2[state[4]] ^ Tables.mul3[state[5]] ^ state[6] ^ state[7]);
		temp[5] = (state[4] ^ Tables.mul2[state[5]] ^ Tables.mul3[state[6]] ^ state[7]);
		temp[6] = (state[4] ^ state[5] ^ Tables.mul2[state[6]] ^ Tables.mul3[state[7]]);
		temp[7] = (Tables.mul3[state[4]] ^ state[5] ^ state[6] ^ Tables.mul2[state[7]]);

		temp[8] = (Tables.mul2[state[8]] ^ Tables.mul3[state[9]] ^ state[10] ^ state[11]);
		temp[9] = (state[8] ^ Tables.mul2[state[9]] ^ Tables.mul3[state[10]] ^ state[11]);
		temp[10] = (state[8] ^ state[9] ^ Tables.mul2[state[10]] ^ Tables.mul3[state[11]]);
		temp[11] = (Tables.mul3[state[8]] ^ state[9] ^ state[10] ^ Tables.mul2[state[11]]);

		temp[12] = (Tables.mul2[state[12]] ^ Tables.mul3[state[13]] ^ state[14] ^ state[15]);
		temp[13] = (state[12] ^ Tables.mul2[state[13]] ^ Tables.mul3[state[14]] ^ state[15]);
		temp[14] = (state[12] ^ state[13] ^ Tables.mul2[state[14]] ^ Tables.mul3[state[15]]);
		temp[15] = (Tables.mul3[state[12]] ^ state[13] ^ state[14] ^ Tables.mul2[state[15]]);

		for(int i = 0; i < 16; i++) {
			state[i] = temp[i];
		}

		//System.out.println("State array = " + Arrays.toString(state));
	}

	// Inverse of mix columns
	public static void invMixColumns(int[] state){
		int[] temp = new int[16];

		temp[0] = (Tables.mul14[state[0]] ^ Tables.mul11[state[1]] ^ Tables.mul13[state[2]] ^ Tables.mul9[state[3]]);
		temp[1] = (Tables.mul9[state[0]] ^ Tables.mul14[state[1]] ^ Tables.mul11[state[2]] ^ Tables.mul13[state[3]]);
		temp[2] = (Tables.mul13[state[0]] ^ Tables.mul9[state[1]] ^ Tables.mul14[state[2]] ^ Tables.mul11[state[3]]);
		temp[3] = (Tables.mul11[state[0]] ^ Tables.mul13[state[1]] ^ Tables.mul9[state[2]] ^ Tables.mul14[state[3]]);

		temp[4] = (Tables.mul14[state[4]] ^ Tables.mul11[state[5]] ^ Tables.mul13[state[6]] ^ Tables.mul9[state[7]]);
		temp[5] = (Tables.mul9[state[4]] ^ Tables.mul14[state[5]] ^ Tables.mul11[state[6]] ^ Tables.mul13[state[7]]);
		temp[6] = (Tables.mul13[state[4]] ^ Tables.mul9[state[5]] ^ Tables.mul14[state[6]] ^ Tables.mul11[state[7]]);
		temp[7] = (Tables.mul11[state[4]] ^ Tables.mul13[state[5]] ^ Tables.mul9[state[6]] ^ Tables.mul14[state[7]]);

		temp[8] = (Tables.mul14[state[8]] ^ Tables.mul11[state[9]] ^ Tables.mul13[state[10]] ^ Tables.mul9[state[11]]);
		temp[9] = (Tables.mul9[state[8]] ^ Tables.mul14[state[9]] ^ Tables.mul11[state[10]] ^ Tables.mul13[state[11]]);
		temp[10] = (Tables.mul13[state[8]] ^ Tables.mul9[state[9]] ^ Tables.mul14[state[10]] ^ Tables.mul11[state[11]]);
		temp[11] = (Tables.mul11[state[8]] ^ Tables.mul13[state[9]] ^ Tables.mul9[state[10]] ^ Tables.mul14[state[11]]);

		temp[12] = (Tables.mul14[state[12]] ^ Tables.mul11[state[13]] ^ Tables.mul13[state[14]] ^ Tables.mul9[state[15]]);
		temp[13] = (Tables.mul9[state[12]] ^ Tables.mul14[state[13]] ^ Tables.mul11[state[14]] ^ Tables.mul13[state[15]]);
		temp[14] = (Tables.mul13[state[12]] ^ Tables.mul9[state[13]] ^ Tables.mul14[state[14]] ^ Tables.mul11[state[15]]);
		temp[15] = (Tables.mul11[state[12]] ^ Tables.mul13[state[13]] ^ Tables.mul9[state[14]] ^ Tables.mul14[state[15]]);

		for(int i = 0; i < 16; i++) {
			state[i] = temp[i];
		}

		//System.out.println("State array = " + Arrays.toString(state));
	}

	public static void addRoundkey(int[] state, int[] roundKey){
		for(int i = 0; i < 16; i++){
			state[i] ^= roundKey[i];
		}
	}

	//---------------KEY EXPANSION STUFF--------------

	// Rotate left
	public static void rotateLeft(int[] in){
		int temp = in[0];
		in[0] = in[1];
		in[1] = in[2];
		in[2] = in[3];
		in[3] = temp;

	}

	// Substitute 4 bytes
	public static void subWord(int[] in){
		in[0] = Tables.sbox[in[0]];
		in[1] = Tables.sbox[in[1]];
		in[2] = Tables.sbox[in[2]];		
		in[3] = Tables.sbox[in[3]];
	}

	public static void rconStep(int[] in, int i){
		in[0] ^= Tables.rcon[i];
	}

	public static void keyExpansionCore(int[] in, int i){
		rotateLeft(in);
		subWord(in);
		rconStep(in, i);
	}

	public static void keyExpansion(int[] inputKey, int[] expandedKeys){
		int bytesGenerated = 0; // Bytes generated so far
		int rconIteration = 1; // Rcon iterations
		int[] temp = new int[4]; // Temp storage for core

		// First 32 bytes are copied over 
		for(int i = 0; i < 32; i++){
			expandedKeys[i] = inputKey[i];
		}

		bytesGenerated = 32; // Generated 32 bytes so far
		while(bytesGenerated < 240){
			// Read 4 bytes
			for(int i = 0; i < 4; i++){
				temp[i] = expandedKeys[i + bytesGenerated - 4];
			}

			// Perform the core steps for each 32 byte key
			if((bytesGenerated % 32) == 0){
				 keyExpansionCore(temp, rconIteration);
				 rconIteration++;
			}
			// For 256-bit keys, we add an extra sbox to the calculation
			if((bytesGenerated % 32) == 16) {
                for(int i = 0; i < 4; i++) {
                	temp[i] = Tables.sbox[temp[i]];
                }
            }

			// XOR temp with [bytesGenerated - 32]
			for(int i = 0; i < 4; i++){
				expandedKeys[bytesGenerated] = expandedKeys[bytesGenerated - 32] ^ temp[i];
				bytesGenerated++;
			}
		}
	}

	//------------------------------------------------

	public static void aesEncrypt(List<String> message, String strKey){
		int numRounds = 13; // Subtract 1 round to account for final round

		byte[] tempByte = Functions.hexStringToByteArray(strKey);
		int[] key = Functions.byteToUnsignedIntArray(tempByte);

		System.out.println("Encrypting..");
		// For each line in the message
		for(int k = 0; k < message.size(); k++){
			tempByte = Functions.hexStringToByteArray(message.get(k));
			int[] state = Functions.byteToUnsignedIntArray(tempByte);

			int[] expandedKey = new int[240];
			keyExpansion(key, expandedKey);
			addRoundkey(state, key); // Whitening, addRoundKey

			for(int i = 0; i < numRounds; i++){
				int startIndex = (16 * (i+1)); // Start index for key
				subBytes(state);
				shiftRows(state);
				mixColumns(state);
				addRoundkey(state, Arrays.copyOfRange(expandedKey, startIndex, expandedKey.length));
			}

			// Final round
			subBytes(state);
			shiftRows(state);
			addRoundkey(state, Arrays.copyOfRange(expandedKey, 224, expandedKey.length));

			try{ 
		       	out.write(Functions.getHex(state)+"\n");
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	    }
	    System.out.println("Done");
	}

	public static void aesDecrypt(List<String> message, String strKey){
		int numRounds = 13;

		byte[] tempByte = Functions.hexStringToByteArray(strKey);
		int[] key = Functions.byteToUnsignedIntArray(tempByte);

		System.out.println("Decrypting..");
		// For each line in the encrypted message
		for(int k = 0; k < message.size(); k++){
			tempByte = Functions.hexStringToByteArray(message.get(k));
			int[] state = Functions.byteToUnsignedIntArray(tempByte);

			int[] expandedKey = new int[240];
			keyExpansion(key, expandedKey);
			addRoundkey(state, Arrays.copyOfRange(expandedKey, 224, expandedKey.length)); // Whitening, addRoundKey
			for(int i = numRounds; i > 0; i--){
				int startIndex = (16 * (i)); // Start index for key
				invShiftRows(state);
				invSubBytes(state);
				addRoundkey(state, Arrays.copyOfRange(expandedKey, startIndex, startIndex+16));
				invMixColumns(state);
			}
			// Final round
			invShiftRows(state);
			invSubBytes(state);
			addRoundkey(state, Arrays.copyOfRange(expandedKey, 0, expandedKey.length));
			
			try{ 
		       	out.write(Functions.getHex(state)+"\n");
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	    }
	    System.out.println("Done");
	}

	// Checks if input is valid
	public static void checkInput(List<String> input){
		for(int i = 0; i < input.size(); i++){
			// Check length
			if(input.get(i).length() < 32 ){
				throw new IllegalArgumentException("Input file contains a line shorter than 32 characters: " + input.get(i));
			}else if(input.get(i).length() > 32){
				throw new IllegalArgumentException("Input file contains a line longer than 32 characters: " + input.get(i));
			}

			// Check if input is in hex
			for(int k = 0; k < input.get(i).length(); k++){
				if(!input.get(i).matches("[0-9a-fA-F]+")){
					throw new IllegalArgumentException("Non-hex character detected in input file: " + input.get(i));
				}
			}
		}
	}

	public static void main(String[] args){
		List<String> message;
		String key;

		// Read input file and key
		try{
			keyFile = args[1]; // Get input file name
			inputFile = args[2]; // get key file name 

			key = new String(Files.readAllBytes(Paths.get(keyFile))); // Read in the key
			key = key.replaceAll("(\\r|\\n)", ""); // Remove new lines etc.


			//Determine if we are encrypting or decrypting
			if(args[0].equalsIgnoreCase("e")){
				out = new FileWriter(inputFile + ".enc");
				message = Files.readAllLines(Paths.get(inputFile));
				checkInput(message); // Check if input is valid
				aesEncrypt(message, key); // Encrypt
			}else if(args[0].equalsIgnoreCase("d")){
				out = new FileWriter(inputFile + ".dec");
				message = Files.readAllLines(Paths.get(inputFile));
				checkInput(message); // Check if input is valid
				aesDecrypt(message, key); // Decrypt
			}else{
				throw new IllegalArgumentException("Invalid argument given: " + args[0]);
			}
			out.close(); // Close output file

		} catch (Exception e){
			System.out.println(e);
		}
	}
}