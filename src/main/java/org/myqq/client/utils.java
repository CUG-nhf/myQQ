package org.myqq.client;

import java.io.Console;
import java.util.Scanner;

public class utils {
    /**
     * 读取密码，如果 IDEA 启动，则使用 Scanner 读取
     * 如果 console 启动，使用Console.readPassword()方法，隐藏输入的密码
     * @return a String
     */
    public static String readPassword() {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        if (console == null) {
            return scanner.next();
        } else {
            return new String(console.readPassword());
        }
    }

    /**
     * 判断字符串是否包含数字、字母、特殊字符中的至少两种
     * @return true or false
     */
    public static boolean isCombination(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        int hasDigit = 0;
        int hasLetter = 0;
        int hasSpecial = 0;
        for (Character c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = 1;
            } else if (Character.isUpperCase(c) || Character.isLowerCase(c)) {
                hasLetter = 1;
            } else {
                int charCode = (int) c;
                if (charCode >= 33 && charCode <= 47 || charCode >= 58 && charCode <= 64 || charCode >= 91 && charCode <= 96 || charCode >= 123 && charCode <= 126) {
                    hasSpecial = 1;
                }
            }
        }

        return (hasDigit + hasLetter + hasSpecial) >= 2;
    }

    /**
     * 判断字符串是否由单一字符组成
     * @return true or false
     */
    public static boolean isSingleChar(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char c = str.charAt(0);
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) != c) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字母或数字字符串是否是连续递增
     * @return true or false
     */
    public static boolean isContinuous(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char c = str.charAt(0);
        if (Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c)) {
            for (int i = 1; i < str.length(); i++) {
                if (str.charAt(i) != (char)(c + i)) {
                    return false;
                }
            }
        }
        return true;
    }
}
