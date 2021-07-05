package net.imyeyu.blog.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MySQL JSON 数据类型处理器
 *
 * <p>夜雨 创建于 2021-07-04 09:36
 */
public class JsonHandler extends BaseTypeHandler<JsonObject> {

	@Autowired
	private Gson gson;

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, JsonObject parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i,String.valueOf(parameter.toString()));
	}

	@Override
	public JsonObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String sqlJson = rs.getString(columnName);
		if (sqlJson != null) {
			return JsonParser.parseString(sqlJson).getAsJsonObject();
		}
		return null;
	}

	@Override
	public JsonObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String sqlJson = rs.getString(columnIndex);
		if (sqlJson != null) {
			return JsonParser.parseString(sqlJson).getAsJsonObject();
		}
		return null;
	}

	@Override
	public JsonObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String sqlJson = cs.getNString(columnIndex);
		if (sqlJson != null) {
			return JsonParser.parseString(sqlJson).getAsJsonObject();
		}
		return null;
	}
}