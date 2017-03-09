public class Functions {
	// Converts a string to an array of bytes
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	    }	    
    	return data;
	}

	// Converts a byte array to an array of ints (unsigned)
	public static int[] byteToUnsignedIntArray(byte[] byteArray) {
		int[] temp = new int[byteArray.length];

		for(int i = 0; i < byteArray.length; i++){
			temp[i] = Byte.toUnsignedInt(byteArray[i]);
		}

		return temp;
	}

	// Convert an int array to a hex string
	public static String getHex(int[] arr) {
		String output = "";
		for(int i = 0; i < arr.length; i++) {
			output += String.format("%02X", arr[i]);
		}
		return output;
	}
}