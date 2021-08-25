.clip-text {
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
}

.font12 {
	font-size: 12px;
}

.gray {
	color: #999;
}

.center {
	text-align: center;
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
	box-shadow: 0 0 8px rgba(0, 0, 0, .5);
	background: #F4F4F4;
}

.wrapper {
	width: 100%;
	display: block;
	border-bottom: 1px solid #CDDEF0;
}

.title {
	margin: 4px 12px;
}

.tips {
	color: #555;
	padding: 12px 0;
	margin-top: 32px;
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
	height: 40px;
	position: absolute;
	background: #FFF;
}

.line2::before {
	top: 33px;
	left: 12px;
	transform: rotate(16deg);
}

.line3::before {
	top: 32px;
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