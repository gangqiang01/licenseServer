package com.db.server;

import com.db.SessionFactoryUtil;
import com.db.entity.LicenseInfo;
import com.db.impl.ILicenseInfoServer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @date ：Created in 8/13/20 1:26 PM
 * @description：license server
 */
public class LicenseInfoServer implements ILicenseInfoServer {

    public boolean insertLicenseInfo(LicenseInfo licenseInfo) {
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance().getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        if(null == licenseInfo) return false;
        try{
            Date dt = new Date();
            licenseInfo.setTs(dt.getTime());
            session.save(licenseInfo);
            tx.commit();
            session.close();
        }
        catch (Exception e){
            if(tx != null) tx.rollback();
            session.close();
            return false;
        }
        return true;
    }

    public boolean deleteLicenseInfoByMachineId(String machineId) {
        boolean nRet = false;
        if(null == machineId || machineId.equals("")) return nRet;
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance().getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try{
            String hql="delete LicenseInfo as l where l.machineid=:machineid";
            Query query=session.createQuery(hql);
            query.setParameter("machineid", machineId);
            int tmpValue = query.executeUpdate();
            System.out.println("[deleteLicenseInfoByMachineId]Result of query " + String.valueOf(tmpValue));
            tx.commit();
            nRet = true;
        }catch(Exception ex){
            System.out.println("[deleteLicenseInfoByMachineId] test delete exception" + ex.toString());
            nRet = false;
            if(tx != null) tx.rollback();
            session.close();
            return nRet;
        }
        session.close();
        return nRet;
    }

    public boolean deleteLicenseInfoAll() {
        boolean nRet = false;
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance().getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try{
            String hql="delete from LicenseInfo";
            Query query=session.createQuery(hql);
            int tmpValue = query.executeUpdate();
            System.out.println("[deleteLicenseInfoAll]Result of query " + String.valueOf(tmpValue));
            tx.commit();
            nRet = true;
        }catch(Exception ex){
            System.out.println("[deleteLicenseInfoAll] test delete exception" + ex.toString());
            nRet = false;
            if(tx != null) tx.rollback();
            session.close();
            return nRet;
        }
        session.close();
        return nRet;
    }

    public LicenseInfo[] getLicenseInfoAll() {
        List<LicenseInfo> list = new ArrayList<LicenseInfo>();
        LicenseInfo[] licenseArray = null;
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance().getSessionFactory();
        Session session = sessionFactory.openSession();
        try{
            list = (List<LicenseInfo>)session.createQuery("select l from LicenseInfo l").list();
            licenseArray = (LicenseInfo[])list.toArray(new LicenseInfo[0]);
        }catch(Exception ex){
            licenseArray = (LicenseInfo[])list.toArray(new LicenseInfo[0]);
            System.out.println("[getLicenseInfoAll] exception" + ex.toString());
        }
        session.close();
        return licenseArray;
    }

    public LicenseInfo getLicenseInfoByMachineId(String machineId) {
        List<LicenseInfo> list = new ArrayList<LicenseInfo>();
        LicenseInfo license = null;
        if(null == machineId || "" == machineId) return license;
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance().getSessionFactory();
        Session session = sessionFactory.openSession();
        try{
            list = (List<LicenseInfo>)session.createQuery("select l from LicenseInfo l where l.machineid=:machineid")
                    .setParameter("machineid", machineId).list();
            if(list.size()>0) license = list.get(0);
        }catch(Exception ex){
            System.out.println("[getLicenseInfoByMachineId] exception" + ex.toString());
        }
        session.close();
        return license;
    }

    public boolean updateLicenseInfo(LicenseInfo license) {
        boolean nRet = false;
        if(license == null)return nRet;
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance().getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try{
            String hql="select count(*) from LicenseInfo as l where l.id=:id";
            Query query=session.createQuery(hql).setParameter("id", license.getId());
            long ret = (Long) query.uniqueResult();
            if(ret > 0){
                session.update(license);
                tx.commit();
                nRet = true;
            }else nRet = false;
        }catch(Exception ex){
            System.out.println("[updateLicenseInfo] test update exception" + ex.toString());
            nRet = false;
            if(tx != null) tx.rollback();
            session.close();
            return nRet;
        }
        session.close();
        return nRet;
    }

    public List<LicenseInfo> getLicenseInfoByProductname(String productname) {
        List<LicenseInfo> list = new ArrayList<LicenseInfo>();
        if(null == productname || "" == productname) return list;
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance().getSessionFactory();
        Session session = sessionFactory.openSession();
        try{
            list = (List<LicenseInfo>)session.createQuery("select l from LicenseInfo l where l.name=:name")
                    .setParameter("name", productname).list();

        }catch(Exception ex){
            System.out.println("[getLicenseInfoByProductname] exception" + ex.toString());
        }
        session.close();
        return list;
    }
}
