package com.aliDns.common;

/**
 * 编码计算工具
 */
public class LevelCodeUtils {

    private static final String separator = "/";

    private static final int digit = 62;

    private static final String[] codes;

    static {
        codes = new String[digit];
        int index = 0;
        // 0-9
        for (int i = 0; i < 10; i++) {
            codes[i] = i + "";
            ++index;
        }
        // a-z
        char a = 'a';
        for (char i = a, l = 'z'; i <= l; i++) {
            codes[index] = String.valueOf(i);
            ++index;
        }
        // a-z
        char A = 'A';
        for (char i = A, l = 'Z'; i <= l; i++) {
            codes[index] = String.valueOf(i);
            ++index;
        }
    }

    /**
     * 将ID转换为code
     * @param id 主键，雪花算法
     * @return 文件目录编码
     */
    public static String toCode(Long id) {
        StringBuilder code = new StringBuilder();
        while(id >= digit) {
            code.append(codes[(int) (id % digit)]);
            id = id / digit;
        }
        code.append(codes[Integer.parseInt(id.toString())]);
        return code.reverse().toString();
    }

}
