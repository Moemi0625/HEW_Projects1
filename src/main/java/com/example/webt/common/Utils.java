package com.example.webt.common;

import java.text.ParseException;

public class Utils {
    public static Integer str2date(String s) {
        try {
            // 入力値が yyyy フォーマットに合致するか確認
            if (s.matches("\\d{4}")) {
                return Integer.parseInt(s);
            } else {
                // フォーマットが合致しない場合の処理
                throw new ParseException("Invalid date format", 0);
            }
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            // パースエラーが発生した場合、適切に処理するか例外を再スローする
            return null;
        }
    }
}
