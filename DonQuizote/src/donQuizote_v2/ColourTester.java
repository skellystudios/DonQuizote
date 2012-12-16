package donQuizote_v2;

import java.awt.Color;
import java.awt.color.ColorSpace;

public class ColourTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int baseint = -16711936;
		//int[] comparisons = {-16711936, -16724125, -10248704, -12544, -65437, -16776961, -1, -16764928};
		int[] comparisons = {-10248704};
		for (int i : comparisons){
			
			System.out.println(compare(baseint, i));
			
		}
		
		
	}
	
	public static double compare(int i, int j){
		
		int[] c1 = makeYUV(i);
		int[] c2 = makeYUV(j);
		//System.out.println("" + c1[0] + "," + c1[1] + "," + c1[2]);
		//System.out.println("" + c2[0] + "," + c2[1] + "," + c2[2]);
		
		int d1 = (c1[0]-c2[0]); d1 = d1 * d1;
		int d2 = (c1[1]-c2[1]); d2 = d2 * d2;
		int d3 = (c1[2]-c2[2]); d3 = d3 * d3;
		
		//System.out.println("Part 1 " + d1 + ", " + d2 + ", " + d3);
		
		double diff = Math.sqrt(d1 + d2 + d3);
		
		return diff;
		
		
	}
	
	
	private static int[] makeYUV(int i){
		
		Color base = new Color(i);
		
		int rgb = base.getRGB(); //always returns TYPE_INT_ARGB
		int alpha = (rgb >> 24) & 0xFF;
		int red =   (rgb >> 16) & 0xFF;
		int green = (rgb >>  8) & 0xFF;
		int blue =  (rgb      ) & 0xFF;
		
		int[] yuv = rgb2yuv(red,green,blue);
		
		return yuv;
	}
	
	
	private static int[] rgb2yuv(int r,int g, int b) {
		int y = (int)(0.299 * r + 0.587 * g + 0.114 * b);
		int u = (int)((b - y) * 0.492f); 
		int v = (int)((r - y) * 0.877f);
		
		int[] yuv = new int[3];
		
		yuv[0]= y;
		yuv[1]= u;
		yuv[2]= v;
		
		return yuv;
	}

}
