package cn.bidlink.framework.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import cn.bidlink.framework.core.exceptions.GeneralException;
import cn.bidlink.framework.util.ValidateCodeUtils.ValidateCodeConstraints.ImageConstraints;
  
/**
 * 实现验证码产生及产生图片
 * 
 * @author wangjinsi
 *
 */
public abstract class ValidateCodeUtils {   
	
	public static void main(String[] args) {
		final JFrame frame = new JFrame("验证码");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		ValidateCodeConstraints constraints = new ValidateCodeConstraints();
		constraints.candidateCharset = CharsetUtils.getSimplifiedChineseCharacter();
//		//
//		constraints.imageConstraints.backgroudIsTransparent = false;
//		constraints.imageConstraints.backgroudColor = Color.GREEN;
//		//
//		constraints.imageConstraints.borderIsTransparent = false;
//		constraints.imageConstraints.borderColor = Color.RED;
//		//
//		constraints.imageConstraints.fontSzie = 30;
//		constraints.imageConstraints.fontColorIsPure = true;
//		constraints.imageConstraints.fontColor = Color.ORANGE;
//		//
		constraints.imageConstraints.disturbedEnabled = true;
		constraints.imageConstraints.disturbedCount = 200;
		constraints.imageConstraints.disturbedColorIsPure = false;
//		constraints.imageConstraints.disturbedColor = Color.GRAY;
//		constraints.imageConstraints.disturbedType = ValidateCodeImageConstraints.DISTURBED_TYPE_POINT;
//		constraints.imageConstraints.disturbedPointRadius = 1;
		constraints.imageConstraints.disturbedType = ImageConstraints.DISTURBED_TYPE_LINE;
		constraints.imageConstraints.disturbedLineLength = 30;
		final ValidateCodeEntry validateCodeEntry = ValidateCodeUtils.generateValidateCode(constraints);
		final ImageIcon imageIcon = new ImageIcon(validateCodeEntry.getImage(constraints));
		frame.add(new JLabel(imageIcon), BorderLayout.NORTH);
		frame.setSize(500, 300);
		frame.setVisible(true);
	}
	
	
	private static Color getRandomColor() {
		Random random = new Random();
		int red = random.nextInt(255);
		int green = random.nextInt(255);
		int blue = random.nextInt(255);
		return new Color(red, green, blue);
	}
  
	public static ValidateCodeEntry generateValidateCode(ValidateCodeConstraints constraints) {
		if(constraints.generatedCharacterCount < 1) {
			constraints.generatedCharacterCount = 6;
		}
		if(!StringUtils.hasLength(constraints.candidateCharset)) {
			constraints.candidateCharset = ValidateCodeConstraints.USUAL_SIMPLIFIED_CHINESE;
		}
		
		char[] validatCodes = new char[constraints.generatedCharacterCount];
		Random random = new Random();
		if(StringUtils.hasLength(constraints.candidateCharset)) {
			// 随机产生generatedSymbolCount个验证码。
			for (int i = 0; i < validatCodes.length; i++) {
				// 得到随机产生的验证码数字。
				validatCodes[i] = constraints.candidateCharset.charAt(random.nextInt(constraints.candidateCharset.length()));
			}
		}
		return new ValidateCodeEntry(validatCodes);
	}
  

	public static class ValidateCodeEntry implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private char[] codes;
		
		private ValidateCodeEntry(char[] codes) {
			this.codes = codes;
		}
		public String getCode() {
			return String.valueOf(codes, 0, codes.length);
		}
		
		/**
		 * 为验证码建立图像
		 * 
		 * @param constraints 包含验证码的所有配置信息
		 * @return BufferedImage
		 */
		public BufferedImage getImage(ValidateCodeConstraints constraints) {
			BufferedImage image = null;
			if(this.codes == null || this.codes.length < 0) {
				throw new GeneralException(" the validate code is null ");
			}
			// 先获取有关字体的信息，以便获取最终图形的
			image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = image.createGraphics();
			// 创建字体，并获取字体的属性对象
			String fontStyle = Font.MONOSPACED;
			if(StringUtils.hasLength(constraints.imageConstraints.fontName)) {
				fontStyle = constraints.imageConstraints.fontName;
			} else {
				if(!constraints.imageConstraints.fontIsMonospaced) {
					fontStyle = Font.SERIF;
				}
			}
			Font font = new Font(fontStyle, Font.BOLD, constraints.imageConstraints.fontSzie);
			FontMetrics fontMetrics = graphics.getFontMetrics(font);
			 
			//根据字体属性，确定图像高度。
			int finalWidth = 4;
			for (int i = 0; i < codes.length; i++) {
				finalWidth += fontMetrics.charWidth(codes[i]);
			}
			int finalHeight = fontMetrics.getHeight();
			if(constraints.imageConstraints.backgroudIsTransparent) {
				image = graphics.getDeviceConfiguration().createCompatibleImage(finalWidth, finalHeight, Transparency.TRANSLUCENT);
			} else {
				image = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_RGB);
			}
			//防止锯齿
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics = image.createGraphics();
			fontMetrics = graphics.getFontMetrics(font);
			
			// 设置背景颜色
			if(!constraints.imageConstraints.backgroudIsTransparent) {
				if(constraints.imageConstraints.backgroudColor != null) {
					graphics.setColor(constraints.imageConstraints.backgroudColor);
				} else {
					graphics.setColor(getRandomColor());
				}
				graphics.fillRect(0, 0, finalWidth, finalHeight);
			}
			// 画边框
			if(!constraints.imageConstraints.borderIsTransparent) {
				if(constraints.imageConstraints.borderColor != null) {
					graphics.setColor(constraints.imageConstraints.borderColor);
				} else {
					graphics.setColor(getRandomColor());
				}
				graphics.drawRect(0, 0, finalWidth - 1, finalHeight - 1);
			}
			
			// 创建一个随机数生成器类，用来产生随机的干扰线位置和、验证码颜色
			
			// 产生干扰线，使图象中的认证码不易被其它程序探测
			if(constraints.imageConstraints.disturbedEnabled) {
				if(constraints.imageConstraints.disturbedColorIsPure) {
					if(constraints.imageConstraints.disturbedColor != null) {
						graphics.setColor(constraints.imageConstraints.disturbedColor);
					} else {
						graphics.setColor(getRandomColor());
					}
				}
				Random random = new Random();
				if(constraints.imageConstraints.disturbedType == ImageConstraints.DISTURBED_TYPE_LINE) {
					int x,y,x1,y1;
					for (int i = 0; i < constraints.imageConstraints.disturbedCount; i++) {
						if(!constraints.imageConstraints.disturbedColorIsPure) {
							graphics.setColor(getRandomColor());
						}
						x = random.nextInt(finalWidth);
						y = random.nextInt(finalHeight);
						x1 = random.nextInt(constraints.imageConstraints.disturbedLineLength);
						y1 = random.nextInt(constraints.imageConstraints.disturbedLineLength);
						graphics.drawLine(x, y, x + x1, y + y1);
					}
				} else {
					int x,y;
					for (int i = 0; i < constraints.imageConstraints.disturbedCount; i++) {
						if(!constraints.imageConstraints.disturbedColorIsPure) {
							graphics.setColor(getRandomColor());
						}
						x = random.nextInt(finalWidth);
						y = random.nextInt(finalHeight);
						graphics.fillOval(x, y, constraints.imageConstraints.disturbedPointRadius * 2, constraints.imageConstraints.disturbedPointRadius * 2);
					}
				}
			}
			
			// 将验证码写入图形
			int xIndex = 2;
			int yIndex = fontMetrics.getAscent();
			graphics.setFont(font);
			if(constraints.imageConstraints.fontColorIsPure) {
				if(constraints.imageConstraints.fontColor != null) {
					graphics.setColor(constraints.imageConstraints.fontColor);
				} else {
					graphics.setColor(getRandomColor());
				}
			}
			for (int i = 0; i < codes.length; i++) {
				if(!constraints.imageConstraints.fontColorIsPure) {
					// 用随机产生的颜色将验证码绘制到图像中。
					graphics.setColor(getRandomColor());
				}
				graphics.drawChars(codes, i, 1,  xIndex, yIndex);
				//计算下个字符的横向坐标
				xIndex += fontMetrics.charWidth(codes[i]);
			}
			return image;
		}
	}
	
	
	public static class ValidateCodeConstraints {

		public static String LETTER_AND_DIGIT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		//使用频率最高的汉字为140个
		public static String USUAL_SIMPLIFIED_CHINESE = "的一是了我不人在他有这个上们来到时大地为子中你说生国年着" +
															 "就那和要她出也得里后自以会家可下而过天去能对小多然于心学" +
															 "么之都好看起发当没成只如事把还用第样道想作种开美总从无情" +
															 "己面最女但现前些所同日手又行意动方期它头经长儿回位分爱老" +
															 "因很给名法间斯知世什两次使身者被高已亲其进此话常与活正感";
		
		public String candidateCharset;

		public int generatedCharacterCount = 6;

		public ImageConstraints imageConstraints = new ImageConstraints();

		public static class ImageConstraints {

			public boolean backgroudIsTransparent = false;

			public Color backgroudColor = Color.WHITE;

			public boolean borderIsTransparent = false;

			public Color borderColor = Color.BLACK;

			public String fontName;

			public boolean fontColorIsPure = false;

			public int fontSzie = 25;

			public Color fontColor;

			public boolean fontIsMonospaced = true;

			//
			public boolean disturbedEnabled = false;

			public int disturbedType = DISTURBED_TYPE_POINT;

			public int disturbedCount = 120;

			public boolean disturbedColorIsPure = false;

			public Color disturbedColor;

			//
			public int disturbedLineLength = 10;

			public int disturbedPointRadius = 1;

			//
			public final static int DISTURBED_TYPE_LINE = 0;

			public final static int DISTURBED_TYPE_POINT = 1;
		}
	}
} 



