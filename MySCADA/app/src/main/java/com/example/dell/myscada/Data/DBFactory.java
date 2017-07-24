package com.example.dell.myscada.Data;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Dell on 2017/5/12.
 */

public class DBFactory {
    private final DBDefine dbDefine;
    private Connection conn = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private ArrayList<String> list;
    private String result;

    public ArrayList<String> getList(){
        return list;
    }

    public String getString(){
        return result;
    }

    public DBFactory(DBDefine dbDefine){
        super();
        this.dbDefine = dbDefine;
        list = new ArrayList<String>();
        result = null;
    }

    private Connection getConntection(){
        try{
            Class.forName(DBDefine.JTDS_NAME);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        try{
            conn = DriverManager.getConnection(dbDefine.getServer()
                    , dbDefine.getUsername(), dbDefine.getPassword());
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }


    public void selectState() throws SQLException{
        list.clear();
        getConntection();
        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String sql = "SELECT AI036 FROM T_TAG_RTDATA_AI";
        resultSet = statement.executeQuery(sql);
        Log.w("Connection", "opened");
        while (resultSet.next()){
            String str = resultSet.getString("AI036");
            //AI036_风机状态
            Log.w("db", str);
            list.add(str);
            str = null;
        }
        close();
    }

    public void selectSituation() throws SQLException{
        result = null;
        getConntection();
        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String sql = "SELECT a.*,b.* FROM (SELECT ID,AllCap,YearCap,MonthCap,DayCap,CurPower,EqualHour,WindSpeed FROM T_CurSummary where ID=1) a "
                + "FULL JOIN (SELECT 1 AS ID,(CAST(AVG(AI076) AS numeric(18,2))) AS AI076, (CAST(AVG(AI053) AS numeric(18,2))) AS AI053, (CAST(AVG(AI054)AS numeric(18,2))) AS AI054 FROM T_TAG_RTDATA_AI) b on a.ID= b.ID";
        resultSet = statement.executeQuery(sql);
        Log.w("Connection", "opened");
        while (resultSet.next()){
            result = resultSet.getString("ALLCap") + "@_@@" + resultSet.getString("YearCap") + "@_@@" + resultSet.getString("MonthCap") + "@_@@"
                    + resultSet.getString("DayCap") + "@_@@" + resultSet.getString("CurPower") + "@_@@" + resultSet.getString("EqualHour")
                    + "@_@@" + resultSet.getString("WindSpeed") + "@_@@" + resultSet.getString("AI076") + "@_@@" + resultSet.getString("AI053")
                    + "@_@@" +resultSet.getString("AI054");
            Log.e("Result", result);
            //TB_风机编号 AI036_风机状态 AI053_风机风速 AI061_风机功率
        }
        close();
    }

    public void selectGenerator(String str) throws SQLException{
        result = null;
        getConntection();
        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String sql = "SELECT a.*,b.* FROM (SELECT TB_ID AS TB,LRUNTIME,LSTOPTIME,LUSEF,MRUNTIME,MSTOPTIME,MUSEF,TRUNTIME,TSTOPTIME,TUSEF FROM T_CurSummary_USEF  where TB_ID='" + str + "') a FULL JOIN"
                + "(SELECT '" + str + "' AS TB,a.AllCap,b.YearCap,c.MonthCap,d.MonthCapL,e.DayCap,f.HourCap FROM (SELECT 1 AS ID,CAST(SUM(TAG_VALUE)/1000 AS numeric(18,3)) AS AllCap FROM T_TAG_YearDATA where TAG_ID='" + str + "') a FULL JOIN"
                + "(SELECT 1 AS ID,CAST(TAG_VALUE/1000 AS numeric(18,3)) AS YearCap FROM T_TAG_YearDATA where TAG_ID='" + str + "' AND GET_DATE=CONVERT(varchar(4),GETDATE(),120)+'-01-01') b ON a.ID=b.ID FULL JOIN "
                + "(SELECT 1 AS ID,CAST(TAG_VALUE/1000 AS numeric(18,3)) AS MonthCap FROM T_TAG_MonthDATA where TAG_ID='" + str + "' AND GET_DATE=CONVERT(varchar(7),GETDATE(),120)+'-01') c ON a.ID=c.ID FULL JOIN "
                + "(SELECT 1 AS ID,CAST(TAG_VALUE/1000 AS numeric(18,3)) AS MonthCapL FROM T_TAG_MonthDATA where TAG_ID='" + str + "' AND GET_DATE=DATEADD(MONTH,-1,CONVERT(DATETIME,CONVERT(varchar(7),GETDATE(),120)+'-01'))) d  ON a.ID=d.ID FULL JOIN "
                + "(SELECT 1 AS ID,CAST(TAG_VALUE/1000 AS numeric(18,3)) AS DayCap FROM T_TAG_DayDATA where TAG_ID='" + str + "' AND GET_DATE=CONVERT(varchar(10),GETDATE(),120)) e  ON a.ID=e.ID FULL JOIN "
                + "(SELECT 1 AS ID,CAST(TAG_VALUE/1000 AS numeric(18,3)) AS HourCap FROM T_TAG_HourDATA where TAG_ID='" + str + "' AND GET_DATE=CONVERT(varchar(13),GETDATE(),120)+':00:00') f  ON a.ID=f.ID) b ON a.TB=b.TB";
        resultSet = statement.executeQuery(sql);
        Log.w("Connection", "opened and str =" + str);
        while (resultSet.next()){
            result = resultSet.getString("LRUNTIME") + "@_@@" + resultSet.getString("LSTOPTIME") + "@_@@" +resultSet.getString("LUSEF") + "@_@@"
                    + resultSet.getString("MRUNTIME") + "@_@@" + resultSet.getString("MSTOPTIME") + "@_@@" +resultSet.getString("MUSEF") + "@_@@"
                    + resultSet.getString("TRUNTIME") + "@_@@" + resultSet.getString("TSTOPTIME") + "@_@@" +resultSet.getString("TUSEF") + "@_@@"
                    + resultSet.getString("AllCap") + "@_@@" + resultSet.getString("YearCap") + "@_@@" + resultSet.getString("MonthCap") + "@_@@"
                    + resultSet.getString("MonthCapL") + "@_@@" + resultSet.getString("DayCap") + "@_@@" + resultSet.getString("HourCap");
            Log.e("Result", result);
            //TB_风机编号 AI036_风机状态 AI053_风机风速 AI061_风机功率
        }
        close();
    }

    public void selectFGenerator() throws SQLException{
        list.clear();
        getConntection();

        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        Log.w("Connection", "open");
        String sql = "SELECT TB,AI036,AI053,AI061,AI076 FROM T_TAG_RTDATA_AI";
        resultSet = statement.executeQuery(sql);
        Log.w("Connection", "opened");
//        resultSet.last();
//        int rows = resultSet.getRow();// 获取行数
//        resultSet.beforeFirst();
        while (resultSet.next()){
            String str = resultSet.getString("TB") + "@_@@" + resultSet.getString("AI036") + "@_@@" + resultSet.getString("AI053") + "@_@@" + resultSet.getString("AI061") + "@_@@" +resultSet.getString("AI076");
            //TB_风机编号 AI036_风机状态 AI053_风机风速 AI061_风机功率
            Log.w("db", str);
            list.add(str);
            str = null;
        }
        close();
    }

    public void selectFGenerator(String str) throws SQLException{
        result = null;
        getConntection();

        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        Log.w("Connection", "open");
        String sql = "  select convert(varchar(12), GET_DATE, 108) as sj,AI061,AI076,AI053 FROM T_TAG_RTDATA_AI where TB = '" + str + "'";
        resultSet = statement.executeQuery(sql);
        Log.w("Connection", "opened");
//        resultSet.last();
//        int rows = resultSet.getRow();// 获取行数
//        resultSet.beforeFirst();
        while (resultSet.next()){
            result = resultSet.getString("sj") + "@_@@" +resultSet.getString("AI061") + "@_@@" + resultSet.getString("AI076") + "@_@@" +resultSet.getString("AI053");
            //TB_风机编号 AI036_风机状态 AI053_风机风速 AI061_风机功率
        }
        close();
    }

    public void selectEvent(String str) throws SQLException{
        list.clear();
        getConntection();

        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        Log.w("Connection", "open");
        String sql = "SELECT AlmPoint,LOG_DESP_CH,CONVERT(varchar,UpdateTime,120) as UpdateTime,Status FROM T_AlarmInfo_AC where AlmPosition='" + str + "' ORDER BY UpdateTime DESC";
        resultSet = statement.executeQuery(sql);
//        Log.w("Connection", "opened");
//        resultSet.last();
//        int rows = resultSet.getRow();
//        Log.w("db", String.valueOf(rows));
        resultSet.beforeFirst();
        while (resultSet.next()){
            String string = resultSet.getString("AlmPoint") + "@_@@" + resultSet.getString("LOG_DESP_CH") + "@_@@" + resultSet.getString("UpdateTime") + "@_@@" + resultSet.getString("Status");
            Log.w("db", string);
            list.add(string);
            string = null;
        }
        close();
    }

    public void selectRuntime() throws SQLException{
        list.clear();
        getConntection();

        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        Log.w("Connection", "open");
        String sql = "SELECT TRUNTIME FROM T_CurSummary_USEF";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String string = resultSet.getString("TRUNTIME") ;
            Log.w("db", string);
            list.add(string);
            string = null;
        }
        close();
    }

    public void selectRuntime(String str) throws SQLException{
        list.clear();
        getConntection();

        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        Log.w("Connection", "open");
        String sql = "select CONVERT(VARCHAR(7),StartTime,120) as sj,CAST(convert(numeric(10,2),sum(Duration))/60 AS numeric(10,2)) as RunTime from T_RPT_TBSTOP "
                + "where FanId='" + str + "' and StartTime>=CONVERT(VARCHAR(4),GETDATE(),120)+'-01-01' and (StopType='未停机' or AvaType='未知') GROUP BY  CONVERT(VARCHAR(7),StartTime,120) ORDER BY sj";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String string = resultSet.getString("RunTime") ;
            Log.w("db", string);
            list.add(string);
            string = null;
        }
        close();
    }

    public void selectGeneratingCap() throws SQLException{
        list.clear();
        getConntection();

        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        Log.w("Connection", "open");
        String sql = "SELECT CAST(SUM(TAG_VALUE)/1000 AS numeric(18,3)) AS AllCap FROM T_TAG_YearDATA group by TAG_ID";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String string = resultSet.getString("AllCap") ;
            Log.w("db", string);
            list.add(string);
            string = null;
        }
        close();
    }
    public void selectGeneratingCap(String str) throws SQLException{
        list.clear();
        getConntection();

        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        Log.w("Connection", "open");
        String sql = "select GET_DATE,TAG_VALUE/1000 as AllCap FROM T_TAG_MonthDATA where TAG_ID = '" + str + "'and GET_DATE >=CONVERT(VARCHAR(4),GETDATE(),120)+'-01-01' ORDER BY GET_DATE";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String string = resultSet.getString("AllCap") ;
            Log.w("db", string);
            list.add(string);
            string = null;
        }
        close();
    }

    public boolean selectAlarm()throws SQLException{
        list.clear();
        getConntection();

        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql = "select AlmPosition, AlmPoint, AlmTime, LOG_DESP_CH from T_AlarmInfo_AC where AlmPoint like 'T%'";
        resultSet = statement.executeQuery(sql);
        if (!resultSet.next()){
            return false;
        }else {
            resultSet.beforeFirst();
            while (resultSet.next()){
                String string = resultSet.getString("AlmPosition") + "@_@@" + resultSet.getString("AlmPoint") + "@_@@" + resultSet.getString("AlmTime") + "@_@@" + resultSet.getString("LOG_DESP_CH") ;
                list.add(string);
                string = null;
            }
        }
        return true;
    }

    public void selectAccount() throws SQLException{
        list.clear();
        getConntection();

        statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql = "  select Account, Password from T_User";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String string = resultSet.getString("Account") + "@_@@" + resultSet.getString("Password") ;
            list.add(string);
            string = null;
        }
    }

    private void close() throws SQLException{
        if(resultSet != null){
            resultSet.close();
        }
        if (statement != null){
            statement.close();
        }
        if (conn != null){
            conn.close();
        }
    }
}
