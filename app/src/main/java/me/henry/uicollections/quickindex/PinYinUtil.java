package me.henry.uicollections.quickindex;

import java.util.Iterator;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.text.TextUtils;
import android.util.Log;

public class PinYinUtil {
	private static String[] CENG = { "CENG" };
	private static String[] ZENG = { "ZENG" };
	private static HanyuPinyinOutputFormat format;

	/**
	 * @param hanzi
	 * @return 不应该被频繁调用，映射表比较大
	 * 
	 */
	public static String getPinYin(String hanzi) {
		String pinyin = "";
		if (TextUtils.isEmpty(hanzi)) {
			return pinyin;
		}
		format = new HanyuPinyinOutputFormat();
		// 设置大写
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		// 音标
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		// 由于只能对耽搁汉字转换，所以需要转换为char[]
		char[] arr = hanzi.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			if (Character.isWhitespace(arr[i]))
				continue;
			// 汉字是2个字节存储的，大于127
			if (arr[i] > 127) {
				// 可以当成汉字转换，但不一定是汉字
				try {
					// 因为多音字的存在 单: dan,shan
					String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(
							arr[i], format);

					if (pinyinArr == null) {
						// 全角字符
						pinyin += arr[i];
					} else if (whetherZeng(arr[i])) {
						pinyin = ZENG[0];
						continue;
					} else {
						pinyin += pinyinArr[0];
					}

				} catch (BadHanyuPinyinOutputFormatCombination e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				// 不可能是汉字，直接拼接，例如曾haha进,haha变成了HAHA
				// pinyin+=arr[i]
				pinyin += Character.toUpperCase(arr[i]);
			}

		}
		char[] te = "曾".toCharArray();
		try {
			String[] test = PinyinHelper
					.toHanyuPinyinStringArray(te[0], format);
			Log.e("zeng", test[1]);
		} catch (BadHanyuPinyinOutputFormatCombination e) {

			e.printStackTrace();
		}

		return pinyin;

	}

	private static boolean whetherZeng(char arr) {

		if (Character.toString(arr).equals("曾")) {

			return true;
		}

		return false;
	}
}
