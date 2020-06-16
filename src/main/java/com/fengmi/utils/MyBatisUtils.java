package com.fengmi.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtils {

    //���SqlSession����
    private static SqlSessionFactory sqlSessionFactory;
    //����ThreadLocal�󶨵�ǰ�߳��е�SqlSession����
    private static final ThreadLocal<SqlSession> SQL_SESSION_THREAD_LOCAL = new ThreadLocal<>();

    static {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡ SqlSession ���Ӷ����߳�Ψһ��
     * @return SqlSession
     */
    public static SqlSession getSqlSession() {
        SqlSession sqlSession = SQL_SESSION_THREAD_LOCAL.get();
        if (null == sqlSession) {
            sqlSession = sqlSessionFactory.openSession();
            SQL_SESSION_THREAD_LOCAL.set(sqlSession);
        }
        return sqlSession;
    }

    /**
     * �ͷ� SqlSession �����Ƴ��̰߳�
     */
    public static void close() {
        getSqlSession().close();
        SQL_SESSION_THREAD_LOCAL.remove();
    }

    /**
     * �������ύ
     */
    public static void commit() {
        SqlSession session = getSqlSession();
        session.commit();
        close();
    }

    /**
     * �������ع�
     */
    public static void rollback() {
        getSqlSession().rollback();
        close();
    }

    /**
     * ��ýӿ�ʵ�������
     * @param t �ӿ������
     * @param <T> �ӿ��෺��
     * @return ���� t ��ʵ����ʵ��
     */
    public static <T> T getMapper(Class<T> t) {
        SqlSession session = getSqlSession();
        return session.getMapper(t);
    }
}