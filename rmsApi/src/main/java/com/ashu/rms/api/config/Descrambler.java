package com.ashu.rms.api.config;
/**
 * 
 * @author ashu
 *
 */
public class Descrambler {
	private static char[] map1 = new char[64];
	private static byte[] map2 = new byte[128];
	static{
		int i = 0;
		for (char c = 'A'; c <= 'Z'; c++){
			map1[i++] = c;
		}
		for (char c = 'a'; c <= 'z'; c++){
			map1[i++] = c;
		}
		for (char c = '0'; c <= '9'; c++){
			map1[i++] = c;
		}
		map1[i++] = '+';
		map1[i++] = '/';
		
		for (i = 0; i < map2.length; i++){
			map2[i] = -1;
		}
		for (i = 0; i < 64; i++){
			map2[map1[i]] = (byte)i ;
		}
	}
	
	private static byte[] decode(byte[] in) {
		int iLen = in.length;
		if (iLen % 4 != 0){
			throw new IllegalArgumentException("given string is encoded with  Base64");
		}
		while (iLen > 0 && in[iLen - 1] == '='){
			iLen--;
		}
		int oLen = (iLen * 3) / 4;
		byte[] out = new byte[oLen];
		int ip = 0, op = 0;
		while (ip < iLen){
			int i0 = in[ip++]; 
			int i1 = in[ip++];
			int i2 = ip < iLen ? in[ip++] : 'A';
			int i3 = ip < iLen ? in[ip++] : 'A';
			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127){
				throw new IllegalArgumentException("illegal character");
			}
			int b0 = map2[i0];
			int b1 = map2[i1];
			int b2 = map2[i2];
			int b3 = map2[i3];
			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0){
				throw new IllegalArgumentException("illegal character");
			}
			int o0 = (b0 << 2) | (b1 >>> 4);
			int o1 = ( (b1 & 0xf) << 4) | (b2 >>> 2);
			int o2 = ( (b2 & 3) << 6) | b3;
			out[op++] = (byte)o0;
			if (op < oLen){
				out[op++] = (byte)o1;
			}
			if (op < oLen){
				out[op++] = (byte)o2;
			}
		}
		return out;
	}

	private static char[] encode(byte[] in, int iLen){
		int oDataLen = (iLen * 4 + 2) / 3;
		int oLen = ( (iLen + 2) / 3 ) * 4;
		char[] out = new char[oLen];
		int ip = 0, op = 0;
		while (ip < iLen){
			int i0 = in[ip++] & 0xff;
			int i1 = ip < iLen ? in[ip++] & 0xff : 0 ;
			int i2 = ip < iLen ? in[ip++] & 0xff : 0 ;
			int o0 = i0 >>> 2;
			int o1 = ( (i0 & 3) << 4 ) | (i1 >>> 4) ;
			int o2 = ( (i1 & 0xf) << 2 ) | (i2 >>> 6) ;
			int o3 = i2 & 0x3f;
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '=';
			op++;
			out[op] = op < oDataLen ? map1[o3] : '=';
			op++;
		}
		return out;
	}

	public static String encodeString(String str){
		return new String(encode(str.getBytes()));
	}
	
	public static char[] encode(byte[] in){
		return encode(in, in.length);
	}
	
	public static String decode(String encoded) {
		return new String(decode(encoded.getBytes()));
	}
	
}
