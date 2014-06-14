package backgroud;

/**
 * �������Ҫ�����������û���Ա���йص�һЩ����
 * @author mzs
 *created on 2014-5-11
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import javax.naming.*;
import javax.sql.*;

//import background.JdbcPool;

//import org.json.JSONException;

public class UserTableManager {
	/**
	 * �޲�Ĭ�Ϲ��캯��
	 */
    public UserTableManager() throws NamingException{
		/**************************************
		 * 
		 */
	}
    /**
     * �����û��������û�����
     * @param name
     * @return
     * 	���ز�ѯ�õ��Ľ����
     * @throws SQLException
     * @throws NamingException
     * @throws ClassNotFoundException
     */
    public static ResultSet getUserDetails(String name) throws SQLException, NamingException, ClassNotFoundException  {
		 Connection conn = null;
		// conn = ((DataSource)jdbcPool.getDataSource()).getConnection();
		conn = JdbcPool.getDataSource();
		PreparedStatement pstmt = conn.prepareStatement("select * from user where name = ?");
		pstmt.setString(1, name);
		ResultSet memberDetailsResultSet = pstmt.executeQuery();
		return memberDetailsResultSet;
		
	}
   /**
    * �û�ע��
    * @param name
    * @param passWd
    * @param sex
    * @param birthDate
    * @param imagePath
    */
    public void userRegister(String name,String passWd,int sex,String birthDate,String imagePath){
	    PreparedStatement userRegsPst;
	    Connection conn = null;
	    try {
		//conn = jdbcPool.getDataSource().getConnection();
		conn = JdbcPool.getDataSource();
		userRegsPst = conn.prepareStatement("insert into user (name,passWd,registerDate,sex,birthDate,age,imagePath) values (?,?,?,?,?,?,?)");
		userRegsPst.setString(1, name);
		userRegsPst.setString(2, passWd);
		String format = "yyyy-MM-dd HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date now = new Date();
		String  registerTime = sdf.format(now);// ���ع涨��ʽ���ַ������ַ�����ʾʱ��
		userRegsPst.setString(3, registerTime);
		userRegsPst.setInt(4, sex);
		userRegsPst.setString(5, birthDate);
		sdf = new SimpleDateFormat("yyyy-MM-dd"); //����ĳ�������ҲӦ�������ָ�ʽ
		Date birthTime = sdf.parse(birthDate);
		Date nowTime = new Date();
		int age = nowTime.getYear() - birthTime.getYear();
		userRegsPst.setInt(6, age);
		userRegsPst.setString(7, imagePath);
		userRegsPst.executeUpdate();
		userRegsPst.close();
	  //	conn.commit();
	    
	    }
	    //ͬ�����벻�ɹ�Ҫ���лع�
	    catch (SQLException e) {
		// TODO Auto-generated catch block
	    	e.printStackTrace();
	    	try {
	    		conn.rollback();
	    	} catch (SQLException e1) {
			// TODO Auto-generated catch block
	    		e1.printStackTrace();
	    	}
	    } catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (ParseException e) {
		// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	    finally {
	    	try {
	    		conn.close();
	    	} catch (SQLException e) {
			// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}
	    }
		
		
    }
    /**
     * ��½ʱ��֤�û��������룬����boolean��
     * @param name
     * @param passWd
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean  login(String name,String passWd) throws SQLException, ClassNotFoundException{
		Connection conn = null;
		//conn = (jdbcPool.getDataSource()).getConnection();
		conn = JdbcPool.getDataSource();
		Statement loginPst = conn.createStatement();
		ResultSet loginResultSet = loginPst.executeQuery("select * from user where name = '" + name +"' and passWd = '"+passWd+"'");
		return loginResultSet.next();
	}
    
   /**
    * �޸��û����ϣ�ԭ�Ӳ�������
    * @param name
    * @param passWd
    * @param sex
    * @param birthDate
    * @param imagePath
    * 		ͷ�񣬴洢�����ڷ�������·��
    * @return
    * @throws SQLException
    * @throws NamingException
    * @throws ClassNotFoundException
    * @throws ParseException
    */
    public synchronized boolean modifyUserData(String name, String passWd, int sex, String birthDate, String imagePath) throws SQLException, NamingException, ClassNotFoundException, ParseException {
		Connection conn = null;
		if(!getUserDetails(name).next())
			return false;  //���޴��ˣ�����
		conn = JdbcPool.getDataSource();
		Statement modifyMemberStmt = conn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet modifyMemberResultSet = modifyMemberStmt
				.executeQuery("select * from user where name = '" + name +"'");
		modifyMemberResultSet.next();
		modifyMemberResultSet.updateString("passWd", passWd);
		modifyMemberResultSet.updateInt("sex", sex);
		modifyMemberResultSet.updateString("birthDate", birthDate);
		modifyMemberResultSet.updateString("imagePath", imagePath);
			/* ����Ҫ������,�����ڵ���ݵõ� */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date birthTime = sdf.parse(birthDate);
		Date nowTime = new Date();
		int age = nowTime.getYear() - birthTime.getYear();
		modifyMemberResultSet.updateInt("age", age);
		modifyMemberResultSet.updateRow();
		modifyMemberResultSet.close();
		modifyMemberStmt.close();
		conn.close();
		return true;
	}
   
	
    
	
	
}
