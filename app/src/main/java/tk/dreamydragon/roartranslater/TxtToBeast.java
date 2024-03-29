package tk.dreamydragon.roartranslater;

/* loaded from: classes.dex */
public class TxtToBeast {

    /* renamed from: bd */
    private static final char[] f31bd = {22007, 21596, 21834, '~'};

    public static String toBeast(String tf) {
        return HexToBeast(ToHex(tf));
    }

    public static String fromBeast(String tf) {
        return FromHex(BeastToHex(tf));
    }

    public static void setBeastChars(char cf0, char cf1, char cf2, char cf3) {
        f31bd[0] = cf0;
        f31bd[1] = cf1;
        f31bd[2] = cf2;
        f31bd[3] = cf3;
    }

    public static char[] getBeastChars() {
        return new char[]{f31bd[0], f31bd[1], f31bd[2], f31bd[3]};
    }

    private static String ToHex(String gbString) {
        char[] utfBytes = gbString.toCharArray();
        StringBuffer unicodeBytes = new StringBuffer();
        for (char c : utfBytes) {
            String hexB = Integer.toHexString(c);
            while (hexB.length() < 4) {
                hexB = "0" + hexB;
            }
            unicodeBytes.append(hexB);
        }
        return unicodeBytes.toString();
    }

    private static String FromHex(String dataStr) {
        StringBuffer buffer = new StringBuffer();
        int start = 0;
        for (int end = 4; end <= dataStr.length(); end += 4) {
            String charStr = dataStr.substring(start, end);
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(new Character(letter).toString());
            start += 4;
        }
        return buffer.toString();
    }

    private static String HexToBeast(String tf) {
        char[] tfArray = tf.toCharArray();
        StringBuffer beast = new StringBuffer();
        for (int i = 0; i < tfArray.length; i++) {
            int k = Integer.valueOf(String.valueOf(tfArray[i]), 16).intValue() + (i % 16);
            if (k >= 16) {
                k -= 16;
            }
            beast.append("" + f31bd[k / 4] + f31bd[k % 4]);
        }
        return beast.toString();
    }

    private static String BeastToHex(String bf) {
        char[] bfArray = bf.toCharArray();
        StringBuffer out = new StringBuffer();
        for (int i = 0; i <= bf.length() - 2; i += 2) {
            int pos1 = 0;
            int pos2 = 0;
            char c = bfArray[i];
            while (pos1 <= 3 && c != f31bd[pos1]) {
                pos1++;
            }
            char c2 = bf.charAt(i + 1);
            while (pos2 <= 3 && c2 != f31bd[pos2]) {
                pos2++;
            }
            int k = ((pos1 * 4) + pos2) - ((i / 2) % 16);
            if (k < 0) {
                k += 16;
            }
            out.append(Integer.toHexString(k));
        }
        return out.toString();
    }
}
