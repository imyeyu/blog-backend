package net.imyeyu.blogapi.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.betterjava.Network;
import net.imyeyu.blogapi.bean.GitHubCommit;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * GitHub 相关
 *
 * <p>夜雨 创建于 2021-07-15 16:27
 */
@Slf4j
@Component
public class GitHub {

	public List<GitHubCommit> getCommits(String user, String repos) throws Exception {
		String reposURL = "https://api.github.com/repos/" + user + "/" + repos + "/commits";
		String response = Network.doGet(reposURL).trim();
		JsonElement jsonElement;
		if (Encode.isJson(response) && (jsonElement = JsonParser.parseString(response)).isJsonArray()) {
			// 请求正确
			JsonArray commits = jsonElement.getAsJsonArray();

			JsonObject commit, committer;
			String msg, url, name, date;

			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			List<GitHubCommit> result = new ArrayList<>();
			for (int i = 0, l = commits.size(); i < l && i < 24; i++) {
				// HTML URL
				commit = commits.get(i).getAsJsonObject();
				url = commit.get("html_url").getAsString();
				// 提交说明
				commit = commit.get("commit").getAsJsonObject();
				msg = commit.get("message").getAsString().replaceAll("\n", "");
				// 提交者
				committer = commit.get("committer").getAsJsonObject();
				name = committer.get("name").getAsString();
				date = committer.get("date").getAsString();

				result.add(new GitHubCommit(name, msg, url, dateFormat.parse(date).getTime()));
			}
			return result;
		} else {
			log.error("无法获取该仓库提交记录：" + reposURL);
			throw new ServiceException(ReturnCode.ERROR, "无法获取该仓库提交记录：" + user + "：" + repos);
		}
	}
}