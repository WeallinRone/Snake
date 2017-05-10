package mysql;


/**
 * 操作数据库的条件类
 * @author Administrator
 *
 */
public class Condition{
	private String con;
	public Condition(String column, String value){
		if(value.toString().split("-").length==3)
			con = column+"=to_date('"+value+"', 'yyyy-MM-dd')";
		else
			con = "trim("+column+")='"+value+"'";
	}
	public Condition(String column, int value){
		con = column+"="+value;
	}
	public Condition(String condition){
		con = condition;
	}
	public String getCon(){
		return con;
	}
	public Condition and(Condition condition){
		if(condition.con.contains("and")||condition.con.contains("or"))
			con += " and ("+condition.con+")";
		else
			con += " and "+condition.con;
		return this;
	}
	public Condition or(Condition condition){
		if(condition.con.contains("and")||condition.con.contains("or"))
			con += " or ("+condition.con+")";
		else
			con += " or "+condition.con;
		return this;
	}
	public Condition not(){
		con = "not ("+con+")";
		return this;
	}
	
	@Override
	public String toString() {
		return con;
	}
}
