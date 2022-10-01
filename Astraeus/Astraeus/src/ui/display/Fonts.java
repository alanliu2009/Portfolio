package ui.display;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public class Fonts 
{
	public static TrueTypeFont ocr48;

	public static TrueTypeFont ocr32;
	public static TrueTypeFont ocr28;
	public static TrueTypeFont ocr26;
	public static TrueTypeFont ocr22;
	public static TrueTypeFont ocr20;
	public static TrueTypeFont ocr18;
	public static TrueTypeFont ocr16;
	public static TrueTypeFont ocr14;
	public static TrueTypeFont ocr13;

	public static TrueTypeFont ocr12;

	public static TrueTypeFont ocr26i;
	public static TrueTypeFont ocr22i;
	public static TrueTypeFont ocr20i;
	public static TrueTypeFont ocr18i;
	
	public static void loadFonts() 
	{
		ocr12 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 12), false);		
		ocr13 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 13), false);		
		ocr14 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 14), false);		
		ocr16 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 16), false);		
		ocr18 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 18), false);		

		//		ocr18 = new TrueTypeFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 20), false);		

		ocr20 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 20), false);		
		ocr22 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 22), false);	
		ocr26 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 26), false);	
		ocr28 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 28), false);	


		ocr32 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 32), false);	
		ocr48 = new TrueTypeFont(new Font("OCR A Extended", Font.BOLD, 48), false);	

		ocr26i = new TrueTypeFont(new Font("OCR A Extended", Font.ITALIC, 26), false);	
		ocr22i = new TrueTypeFont(new Font("OCR A Extended", Font.ITALIC, 22), false);	
		ocr20i = new TrueTypeFont(new Font("OCR A Extended", Font.ITALIC, 20), false);	
		ocr18i = new TrueTypeFont(new Font("OCR A Extended", Font.ITALIC, 18), false);	



		
	}
}
