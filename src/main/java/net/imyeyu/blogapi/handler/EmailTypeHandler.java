package net.imyeyu.blogapi.handler;

import net.imyeyu.blogapi.bean.EmailType;
import net.imyeyu.blogapi.entity.ArticleType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 文章类型枚举映射
 *
 * <p>夜雨 创建于 2021-07-04 10:19
 */
public class EmailTypeHandler extends BaseTypeHandler<EmailType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, EmailType parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.toString());
	}

	@Override
	public EmailType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String type = rs.getString(columnName);
		if (rs.wasNull() || type == null) {
			return null;
		} else {
			return EmailType.valueOf(type);
		}
	}

	@Override
	public EmailType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String type = rs.getString(columnIndex);
		if (rs.wasNull() || type == null) {
			return null;
		} else {
			return EmailType.valueOf(type);
		}
	}

	@Override
	public EmailType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String type = cs.getString(columnIndex);
		if (cs.wasNull() || type == null) {
			return null;
		} else {
			return EmailType.valueOf(type);
		}
	}
}