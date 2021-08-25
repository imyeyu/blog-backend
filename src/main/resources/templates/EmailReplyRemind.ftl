<!DOCTYPE html>
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<title>Title</title>
		<style>
			.gray {
				color: #999;
			}

			p {
				margin: 0;
			}

			.root {
				width: 42rem;
				margin: 24px auto;
				position: relative;
			}

			.mail {
				margin: 0 3rem;
				box-shadow: 0 0 8px rgba(0, 0, 0, .6);
				background: #F4F4F4;
			}

			.wrapper {
				width: 100%;
				display: block;
				border-bottom: 2px solid #CDDEF0;
			}

			.title {
				margin: 4px 12px;
			}

			.comment {
				width: 80%;
				display: flex;
				margin: 16px auto 4px auto;
				font-size: 14px;
				justify-content: space-between;
			}

			.content {
				display: flex;
				align-items: center;
			}

			.content .avatar {
				width: 22px;
				height: 22px;
				border: 1px solid #777;
				margin-right: 4px;
			}

			.content h4 {
				margin: 0;
			}

			.other {
				color: #555;
				padding: 12px 0;
				font-size: 14px;
				margin-top: 48px;
				text-align: center;
			}

			.line1 {
				height: 3px;
				z-index: -1;
				position: relative;
				margin-top: -50px;
				background: #FF7A9B;
			}

			.line1::before {
				content: "";
				left: 0;
				width: 50%;
				height: 3px;
				bottom: 0;
				position: absolute;
				background: #FF7A9B;
				transform: rotate(-30deg);
				transform-origin: 0 0;
			}

			.line1::after {
				content: "";
				right: 0;
				width: 50%;
				height: 3px;
				bottom: 0;
				position: absolute;
				background: #FF7A9B;
				transform: rotate(30deg);
				transform-origin: 100% 0;
			}

			.line2,
			.line3 {
				width: 3px;
				height: 200px;
				position: absolute;
				background: linear-gradient(to bottom, rgba(255,122,155,1) 0%, rgba(255,255,255,0) 100%);;
			}

			.line2::before,
			.line3::before {
				content: "";
				width: 200px;
				height: 36px;
				position: absolute;
				background: #FFF;
			}

			.line2::before {
				top: 33px;
				left: 12px;
				transform: rotate(16deg);
			}

			.line3::before {
				top: 30px;
				right: 12px;
				transform: rotate(-16deg);
			}

			.line2::after,
			.line3::after {
				content: "";
				top: -1px;
				width: 21.8rem;
				height: 3px;
				position: absolute;
				background: #FF7A9B;
			}

			.line2::after {
				left: 2px;
				transform: rotate(16deg);
				transform-origin: 0 0;
			}

			.line3::after {
				right: 2px;
				transform: rotate(-16deg);
				transform-origin: 100% 0;
			}

			.line3 {
				right: 0;
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
								<p>${remind.reply.data}</p>
							</div>
							<span class="gray">${remind.reply.createdAt?number_to_datetime?string("yyyy-MM-dd HH:mm:ss")}</span>
						</section>
					</#list>
				<div class="other">
					<p>这是由系统自动发送的邮件，请不要回复</p>
					<p>
						<span>如果这封邮件打扰到您，可以登录</span>
						<a href="https://www.imyeyu.net">夜雨博客</a>
						<span>个人空间中的设置随时关闭</span>
					</p>
					<p>&nbsp;</p>
					<p>朝朝频顾惜，夜夜不相忘</p>
					<p>Copyright &copy; 2017 - ${yyyy} 夜雨 All Rights Reserved 版权所有</p>
				</div>
			</div>
			<div class="line1"></div>
			<div class="line2"></div>
			<div class="line3"></div>
		</div>
	</body>
</html>