<!DOCTYPE html>
<html lang="zh">
	<head>
		<meta charset="UTF-8" />
		<title>Title</title>
		<style>
			<#include "StyleSheet.ftl" />
		</style>
		<style>
			.btn {
				color: #FFF !important;
				width: 7rem;
				margin: 32px auto 48px auto;
				border: none;
				display: block;
				padding: 8px 24px;
				font-size: 15px;
				background: #53BD93;
				text-decoration: none !important;
			}

			.btn:hover {
				background: #5AC79B;
			}

			.tips {
				margin: 12px 0 6px 0;
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
				<h4 class="title">${user.name} 您好，您正在验证 夜雨博客 的邮箱绑定，请在 10 分钟内点击下面的按钮完成校验，不要泄露此邮件内容。</h4>
				<a class="btn" href="${url}" target="_blank">继续完成验证</a>
				<aside class="tips center gray">如果这不是你本人操作，请无视此邮件</aside>
				<#include "Footer.ftl" />
			</div>
			<div class="line1"></div>
			<div class="line2"></div>
			<div class="line3"></div>
		</div>
	</body>
</html>