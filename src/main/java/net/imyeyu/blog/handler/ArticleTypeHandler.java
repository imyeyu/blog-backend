package net.imyeyu.blog.handler;

import net.imyeyu.blog.entity.ArticleType;
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
public class ArticleTypeHandler extends BaseTypeHandler<ArticleType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, ArticleType parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.toString());
	}

	@Override
	public ArticleType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String expenseType = rs.getString(columnName);
		if (rs.wasNull() || expenseType == null) {
			return null;
		} else {
			return ArticleType.valueOf(expenseType);
		}
	}

	@Override
	public ArticleType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String expenseType = rs.getString(columnIndex);
		if (rs.wasNull() || expenseType == null) {
			return null;
		} else {
			return ArticleType.valueOf(expenseType);
		}
	}

	@Override
	public ArticleType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String expenseType = cs.getString(columnIndex);
		if (cs.wasNull() || expenseType == null) {
			return null;
		} else {
			return ArticleType.valueOf(expenseType);
		}
	}
}