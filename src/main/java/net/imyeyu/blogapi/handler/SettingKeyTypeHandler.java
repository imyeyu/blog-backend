package net.imyeyu.blogapi.handler;

import net.imyeyu.blogapi.bean.SettingKey;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 系统配置键类型枚举映射
 *
 * <p>夜雨 创建于 2021-07-20 22:29
 */
public class SettingKeyTypeHandler extends BaseTypeHandler<SettingKey> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, SettingKey parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.toString());
	}

	@Override
	public SettingKey getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String expenseType = rs.getString(columnName);
		if (rs.wasNull() || expenseType == null) {
			return null;
		} else {
			return SettingKey.valueOf(expenseType);
		}
	}

	@Override
	public SettingKey getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String expenseType = rs.getString(columnIndex);
		if (rs.wasNull() || expenseType == null) {
			return null;
		} else {
			return SettingKey.valueOf(expenseType);
		}
	}

	@Override
	public SettingKey getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String expenseType = cs.getString(columnIndex);
		if (cs.wasNull() || expenseType == null) {
			return null;
		} else {
			return SettingKey.valueOf(expenseType);
		}
	}
}