package com.example.webt.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.webt.common.Utils;
import com.example.webt.entity.Webt;
import com.example.webt.form.WebtData;
import com.example.webt.form.WebtQuery;
import com.example.webt.repository.WebtRepository;

@Service
public class WebtService {

	//Formのエラー表示
	public boolean isValid(WebtData webtData, BindingResult result) {
	    // 既存のバリデーション
	    boolean isValid = validateTitleOrName("title", webtData.getTitle(), result);
	    isValid &= validateTitleOrName("author", webtData.getAuthor(), result);

	    // startYearの形式をyyyyに固定
	    if (webtData.getStartYear() != null) {
	        try {
	            int year = webtData.getStartYear();
	            
	            // 4桁か確認
	            if (String.valueOf(year).length() != 4) {
	                result.rejectValue("startYear", "InvalidFormat", "年は4桁で入力してください");
	                isValid = false;
	            } else {
	                webtData.setStartYear(year);
	            }
	        } catch (NumberFormatException e) {
	            result.rejectValue("startYear", "InvalidFormat", "無効な形式です");
	            isValid = false;
	        }
	    }

	    return isValid && !result.hasErrors();
	}

	//Formのエラー表示
    private boolean validateTitleOrName(String fieldName, String value, BindingResult result) {
        // Check if the input consists only of full-width spaces
        boolean isAllDoubleSpace = value.matches("^[　]+$");

        if (isAllDoubleSpace) {
            result.rejectValue(fieldName, "FullWidthSpaces", fieldName + "が全角スペースです");
            return false;
        }

        return true;
    }
    
    
    //検索機能のエラー表示（連載開始年）：不具合あり
    public boolean isValid(WebtQuery webtQuery, BindingResult result) {
        checkDateFormat(webtQuery.getStartYearFrom(), "startYearFrom", result);
        checkDateFormat(webtQuery.getStartYearTo(), "startYearTo", result);
        
        return !result.hasErrors();
    }

    private void checkDateFormat(String date, String fieldName, BindingResult result) {
        if (!date.equals("")) {
            try {
                LocalDate.parse(date);
            } catch (DateTimeException e) {
                // parse できない場合
                FieldError fieldError = new FieldError(result.getObjectName(), fieldName, "yyyy 形式で入力してください");
                result.addError(fieldError);
            }
        }
    }


    @Autowired
    private WebtRepository webtRepository;
    
    public List<Webt> doQuery(WebtQuery webtQuery) {
        List<Webt> webtList = null;
        if (webtQuery.getTitle().length() > 0) {
            // タイトルで検索
            webtList = webtRepository.findByTitleLike("%" + webtQuery.getTitle() + "%");

        } else if (webtQuery.getAuthor().length() > 0) {
            // 作者名で検索
            webtList = webtRepository.findByAuthorLike("%" + webtQuery.getAuthor() + "%");

        } else if (!webtQuery.getStartYearFrom().equals("") && webtQuery.getStartYearTo().equals("")) {
            //  連載開始年～
            webtList = webtRepository
                    .findByStartYearGreaterThanEqualOrderByStartYearAsc(Utils.str2date(webtQuery.getStartYearFrom()));
        } else if (webtQuery.getStartYearFrom().equals("") && !webtQuery.getStartYearTo().equals("")) {
            //  ～連載開始年
            webtList = webtRepository
                    .findByStartYearLessThanEqualOrderByStartYearAsc(Utils.str2date(webtQuery.getStartYearTo()));
        } else if (!webtQuery.getStartYearFrom().equals("") && !webtQuery.getStartYearTo().equals("")) {
            // 連載開始年 ～ 連載開始年
            webtList = webtRepository.findByStartYearBetweenOrderByStartYearAsc(
                    Utils.str2date(webtQuery.getStartYearFrom()), Utils.str2date(webtQuery.getStartYearTo()));
        } else if (webtQuery.getDone() != null && webtQuery.getDone().equals("Y")) {
            // 完結で検索
            webtList = webtRepository.findByDone("Y");
        } else {
            // 入力条件が無ければ全件検索
            webtList = webtRepository.findAll();
        }
        return webtList;
    }
}
