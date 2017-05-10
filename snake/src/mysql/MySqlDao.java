package mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;



/**
 * 数据库操作
 * @author Administrator
 *
 */
public class MySqlDao {
	public static final String T_USER = "T_USER";
	public static final String T_EMPINFO = "T_EMPINFO";
	public static final String T_DEPARTMENT = "T_DEPARTMENT";
	
	Connection conn;
	String table;

	public MySqlDao(String table){
		this.table = table;
	}
	public MySqlDao(){
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	/**
	 * 链接数据库
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void connDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/db_test",
					"root","123456");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入数据
	 * @param columns
	 * @param values
	 * @throws SQLException
	 */
	public void insertData(String table, String[] columns, Object... values){
		if(columns.length != values.length){
			return;
		}
		String sql = fromatInsertSQL(table, columns);
		System.out.println(sql);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < values.length; i++)
					ps.setObject(i+1, values[i]);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	private String fromatInsertSQL(String table, String[] columns){
		String sql = "insert into "+table+" (";
		for(int i = 0; i < columns.length; i++){
			if(i !=  columns.length-1){
				sql += columns[i]+", ";
			}else{
				sql += columns[i]+") ";
			}
		}
		sql += "values(";
		for(int i = 0; i < columns.length; i++){
			if(i !=  columns.length-1){
				sql += "?, ";
			}else{
				sql += "?) ";
			}
		}
		return sql;
	}
	
	/**
	 * 查询表数据
	 * @param columns
	 * @return
	 * @throws SQLException
	 */
	public Object[][] selectData(String table,String...columns){
		String sql;
		if(columns.length == 0)
			sql = "select * from "+table;
		else{
			sql = "select ";
			for(int i = 0; i < columns.length - 1; i++){
				sql += columns[i]+", ";
			}
			sql += columns[columns.length - 1]+" from "+table;
		}
		return executeQuery(sql);
	}
	
	public Object[][] getDataOf(ResultSet rs) throws SQLException{
		Object[][] data = null;
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = 0;
		while(rs.next())
			count++;
		data = new Object[count][rsmd.getColumnCount()];
		rs.beforeFirst();
		for(int i=0; rs.next(); i++)
			for(int j=0; j<data[i].length; j++)
				data[i][j] = rs.getObject(j+1);
		return data;
	}
	
	/**
	 * 根据条件查询表
	 * @param condition 条件
	 * @param columns
	 * @return ResultSet 结果集
	 * @throws SQLException
	 */
	public Object[][] selectDataOf(String table, Condition condition, String...columns){
		String sql;
		if(columns.length == 0)
			sql = "select * from "+table+" where "+condition.getCon();
		else{
			sql = "select ";
			for(int i = 0; i < columns.length - 1; i++){
				sql += columns[i]+", ";
			}
			sql += columns[columns.length - 1]
					+" from "+table+" where "+condition.getCon();
		}
		return executeQuery(sql);
	}
	
	/**
	 * 更改数据库数据
	 * @param condition 条件
	 * @param columns 
	 * @param values
	 * @throws SQLException
	 */
	public void updateDataOf(String table, Condition condition, String[] columns, 
			Object... values){
		if(columns.length != values.length){
			return;
		}
		String sql = formatUpdateSQL(table, condition, columns);
		System.out.println(sql);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < values.length; i++){
				ps.setObject(i+1, values[i]);
			}
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(ps!=null)
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	private String formatUpdateSQL(String table, Condition condition, 
			String[] columns){
		String sql = "update "+table+" set ";
		for(int i = 0; i < columns.length; i++){
			sql += columns[i]+"=?";
			if(i != columns.length-1)
				sql += ", ";
		}
		sql += " where "+condition.getCon();
		return sql;
	}
	
	/**
	 * 删除数据
	 * @param condition 条件
	 * @throws SQLException
	 */
	public void deleteDataOf(Condition condition){
		String sql = "delete from "+table+" where "+condition.getCon();
		execute(sql);
	}
	
	/**
	 * 获取符合条件的数量
	 * @param condition 条件
	 * @return 符合条件的数量
	 */
	public int count(String table, Condition condition){
		int count = -1;
		String sql;
		if(condition==null)
			sql = "select count(*) from "+table;
		else
			sql = "select count(*) from "+table+" where "
					+condition.getCon();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(ps!= null)
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return count;
	}
	
	/**
	 * 创建表
	 * @param tableName 表名
	 * @param columns 
	 * @param cla 
	 * @param constraints 约束
	 */
	public void createTable(String tableName, String[] columns,
			String[] cla, String[] constraints){
		if(!(columns.length==cla.length&&cla.length==constraints.length))
			return;
		String sql = "create table "+tableName+"(";
		for(int i = 0; i < columns.length; i++){
			sql += columns[i]+" "+cla[i];
			if(constraints[i]!=null)
				sql += " "+constraints[i];
			if(i==columns.length-1)
				continue;
			sql += ", ";
		}
		sql += ")";
		execute(sql);
	}
	
	/**
	 * 执行sql语句
	 * @param sql
	 */
	public void execute(String sql){
		System.out.println(sql);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(ps!= null)
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 执行sql语句
	 * @param sql
	 * @throws SQLException 
	 */
	public Object[][] executeQuery(String sql){
		System.out.println(sql);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			return getDataOf(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(ps!=null)
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public Object[][] callShow(int id){
		String sql = "{call p_show(?)}";
		try {
			CallableStatement cs = conn.prepareCall(sql);
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			return getDataOf(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int callCount(){
		String sql = "{call p_getcount(?)}";
		try {
			CallableStatement cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.INTEGER);;
			cs.execute();
			return cs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 关闭数据库链接
	 */
	public void closeConn(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
			throws ClassNotFoundException, SQLException {
		System.out.println("执行中...");
		MySqlDao db = new MySqlDao();
		db.connDB();

		Object[][] data = db.callShow(105);
		for(int i=0; i<data.length; i++){
			for(int j=0; j<data[i].length; j++)
				System.out.print(data[i][j]+" ");
			System.out.println();
		}
		System.out.println("----------------");
		System.out.println(db.callCount());
		db.closeConn();
		System.out.println("执行结束...");
	}
	
	
}
