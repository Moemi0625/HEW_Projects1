package com.example.webt.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAModelEntityProcessor")
@StaticMetamodel(Webt.class)
public class Webt_ {

    public static volatile SingularAttribute<Webt, Integer> id;
    public static volatile SingularAttribute<Webt, String> title;
    public static volatile SingularAttribute<Webt, String> author;
    public static volatile ListAttribute<Webt, String> genres;
    public static volatile SingularAttribute<Webt, String> done;
    public static volatile SingularAttribute<Webt, Integer> startYear;

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String GENRES = "genres";
    public static final String DONE = "done";
    public static final String STARTYEAR = "startYear";
}
