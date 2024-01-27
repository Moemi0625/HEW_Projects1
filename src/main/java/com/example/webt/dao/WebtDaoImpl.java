package com.example.webt.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.webt.common.Utils;
import com.example.webt.entity.Webt;
import com.example.webt.entity.Webt_;
import com.example.webt.form.WebtQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class WebtDaoImpl implements WebtDao{

	private final EntityManager entityManager;

	@Override
	public List<Webt> findByJPQL(WebtQuery webtQuery) {
	    StringBuilder sb = new StringBuilder("select t from Webt t where 1 = 1");
	    List<Object> params = new ArrayList<>();
	    int pos = 0;

	    // タイトル
	    if (webtQuery.getTitle().length() > 0) {
	        sb.append(" and t.title like ?" + (++pos));
	        params.add("%" + webtQuery.getTitle() + "%");
	    }

	    // 作者名
	    if (webtQuery.getAuthor().length() > 0) {
	        sb.append(" and t.author like ?" + (++pos));
	        params.add("%" + webtQuery.getAuthor() + "%");
	    }

	    // ジャンル
	    if (webtQuery.getGenres() != null && webtQuery.getGenres().length > 0) {
	        sb.append(" and t.genres in (:" + (++pos) + ")");
	        params.add(Arrays.asList(webtQuery.getGenres()));
	    }

	    // 開始～
	    if (!webtQuery.getStartYearFrom().equals("")) {
	        sb.append(" and t.startYear >= ?" + (++pos));
	        params.add(Utils.str2date(webtQuery.getStartYearFrom()));
	    }

	    // ～終了で検索
	    if (!webtQuery.getStartYearTo().equals("")) {
	        sb.append(" and t.startYear <= ?" + (++pos));
	        params.add(Utils.str2date(webtQuery.getStartYearTo()));
	    }

	    // 完了
	    if (webtQuery.getDone() != null && webtQuery.getDone().equals("Y")) {
	        sb.append(" and t.done = ?" + (++pos));
	        params.add(webtQuery.getDone());
	    }

	    // order
	    sb.append(" order by id");
	    Query query = entityManager.createQuery(sb.toString());
	    for (int i = 0; i < params.size(); ++i) {
	        query = query.setParameter(i + 1, params.get(i));
	    }

	    @SuppressWarnings("unchecked")
	    List<Webt> list = query.getResultList();
	    return list;
	}

	// Criteria API による検索
		@Override
		public Page<Webt> findByCriteria(WebtQuery webtQuery, Pageable pageable) {
	    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Webt> query = builder.createQuery(Webt.class);
	    Root<Webt> root = query.from(Webt.class);
	    List<Predicate> predicates = new ArrayList<>();

	    // タイトル
	    String title = "";
	    if (webtQuery.getTitle().length() > 0) {
	        title = "%" + webtQuery.getTitle() + "%";
	    } else {
	        title = "%";
	    }
	    predicates.add(builder.like(root.get(Webt_.TITLE), title));

	    // 作者名
	    String author = "";
	    if (webtQuery.getAuthor().length() > 0) {
	        author = "%" + webtQuery.getAuthor() + "%";
	    } else {
	        author = "%";
	    }
	    predicates.add(builder.like(root.get(Webt_.AUTHOR), author));

	 // ジャンル
	    if (webtQuery.getGenres() != null && webtQuery.getGenres().length > 0) {
	        Expression<String> genreExpression = root.get(Webt_.GENRES);

	        List<Predicate> genrePredicates = new ArrayList<>();
	        for (String genre : webtQuery.getGenres()) {
	            genrePredicates.add(builder.equal(genreExpression, genre));
	        }

	        Predicate genreCondition = builder.or(genrePredicates.toArray(new Predicate[0]));
	        predicates.add(genreCondition);
	    }

	    // 開始～
	    if (!webtQuery.getStartYearFrom().equals("")) {
	        predicates.add(builder.and(builder.greaterThanOrEqualTo(root.get(Webt_.STARTYEAR),
	                Utils.str2date(webtQuery.getStartYearFrom()))));
	    }

	    // ～終了で検索
	    if (!webtQuery.getStartYearTo().equals("")) {
	        predicates.add(builder.and(
	                builder.lessThanOrEqualTo(root.get(Webt_.STARTYEAR), Utils.str2date(webtQuery.getStartYearTo()))));
	    }

	    // 完了
	    if (webtQuery.getDone() != null && webtQuery.getDone().equals("Y")) {
	        predicates.add(builder.and(builder.equal(root.get(Webt_.DONE), webtQuery.getDone())));
	    }

	 // SELECT 作成
	 		Predicate[] predArray = new Predicate[predicates.size()];
	 		predicates.toArray(predArray);
	 		query = query.select(root).where(predArray).orderBy(builder.asc(root.get(Webt_.id)));
	 		// クエリ生成
	 		TypedQuery<Webt> typedQuery = entityManager.createQuery(query); 
	 		// 該当レコード数取得
	 		int totalRows = typedQuery.getResultList().size(); 
	 		// 先頭レコードの位置設定
	 		typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
	 		// 1 ページ当たりの件数
	 		typedQuery.setMaxResults(pageable.getPageSize()); 
	 		
	 		Page<Webt> page = new PageImpl<Webt>(typedQuery.getResultList(), pageable, totalRows);
	 		return page; 
	 	}


}

