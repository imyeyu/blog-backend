<!DOCTYPE html>
<html lang="zh">
	<head>
		<meta charset="UTF-8" />
		<title>Title</title>
		<style>
			<#include "StyleSheet.ftl" />
		</style>
		<style>
			.comment {
				width: 80%;
				display: flex;
				margin: 16px auto 4px auto;
				font-size: 14px;
				align-items: center;
				justify-content: space-between;
			}

			.comment .content {
				display: flex;
				align-items: center;
			}

			.comment .content .avatar {
				width: 22px;
				height: 22px;
				border: 1px solid #777;
				margin-right: 4px;
			}

			.comment .content h4 {
				margin: 0;
			}

			.comment .content .data {
				width: 15rem;
			}

			.sign-in-tips {
				margin-top: 20px;
			}
		</style>
	</head>
	<body>
		<div class="root">
			<div class="mail">
				<#if user.data.hasWrapper>
					<img class="wrapper" src="https://res.imyeyu.net/user/wrapper/${user.id}.png" />
				<#else>
					<img class="wrapper" src="https://res.imyeyu.net/user/wrapper/default.png" />
				</#if>
				<h4 class="title">
					<span>${user.name} 您好，您在 夜雨博客 的评论收到以下回复</span>
					<span class="gray">(24 小时内)</span>
				</h4>
				<#list reminds as remind>
					<section class="comment">
						<div class="content">
							<#if remind.reply.sender??>
								<#if remind.reply.sender.data.hasAvatar>
									<img class="avatar" src="https://res.imyeyu.net/user/avatar/${remind.reply.sender.id}.png" />
								<#else>
									<img class="avatar" src="https://res.imyeyu.net/user/avatar/default.png" />
								</#if>
								<h4>${remind.reply.sender.name} 说：</h4>
							<#else>
								<img class="avatar" src="https://res.imyeyu.net/user/avatar/default.png" />
								<h4>${remind.reply.senderNick} 说：</h4>
							</#if>
							<p class="data clip-text">${remind.reply.data}</p>
						</div>
						<span class="gray time font12">${remind.reply.createdAt?number_to_datetime?string("yyyy-MM-dd HH:mm:ss")}</span>
					</section>
				</#list>
				<p class="sign-in-tips gray font12 center">本邮件仅作摘要提醒，登录博客查看详细信息</p>
				<#include "Footer.ftl" />
			</div>
			<div class="line1"></div>
			<div class="line2"></div>
			<div class="line3"></div>
		</div>
	</body>
</html>