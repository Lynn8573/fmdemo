<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>会员登录</title>
		<link rel="stylesheet" type="text/css" href="./css/login.css">
		<script type="text/javascript" src="../js/jquery-3.1.1.js"></script>
	</head>
	<body>
		<!-- login -->
		<div class="top center">
			<div class="logo center">
				<a href="./index.html" target="_blank"><img src="./image/logo.png" width="240px" height="70px" style="padding-top:20px"
					 alt=""></a>
			</div>
		</div>
		<form method="post" class="form center">
			<div class="login">
				<div class="login_center">
					<div class="login_top">
						<div class="left fl">会员登录</div>
						<div class="right fr">您还不是我们的会员？<a href="register.jsp" target="_self">立即注册</a></div>
						<div class="clear"></div>
						<div class="xian center"></div>
					</div>
					<div class="login_main center">
						<!-- 隐藏标签，指定的 Servlet 方法 -->
						<input type="hidden" name="methodName" value="login" />
						<div class="username">用户名:&nbsp;<input class="shurukuang" type="text" name="username" placeholder="请输入你的用户名"
							 onblur="checkUsername()" /></div>
						<div class="username">密&nbsp;&nbsp;&nbsp;&nbsp;码:&nbsp;<input class="shurukuang" type="password" name="password"
							 placeholder="请输入你的密码" /></div>
						<div class="username">
							<div class="left fl">验证码:&nbsp;<input class="yanzhengma" type="text" name="validatecode" placeholder="请输入验证码" /></div>
							<div class="right fl"><img id="codeImg" src="validateCode.jsp"></div><!-- img的src是生成验证码的servlet资源 -->
							<div class="clear"></div>
						</div>
						<span id="msg" style="color:red;"></span>

					</div>
					<div class="login_submit">
						<input class="submit" type="submit" onclick="return submitForm()" name="submit" value="立即登录">
					</div>

				</div>
			</div>
		</form>
		<footer>
			<div class="copyright">简体 | 繁体 | English | 常见问题</div>
			<div class="copyright">小米公司版权所有-京ICP备10046444-<img src="./image/ghs.png" alt="">京公网安备11010802020134号-京ICP证110507号</div>

		</footer>

		<script type="text/javascript">
			// 点击验证码图像，获取新的验证码
			$("#codeImg").click(function() {
				// 因为浏览器缓存的原因，如果src的路径相同，不会再次发送请求，所以在路径后增加随机值
				$(this).attr("src", "validateCode.jsp?r=" + Math.random());
			})
		</script>


		<!-- 如果有一个表单，现在想提交
		   1、form   action=“”   submit
		   2、点击提交  触发一个事件，在事件中获取表单数据，进行ajax提交
		   3、通过js获取form表单，$("#form").submit(function(){
		      });
		 -->
		<script type="text/javascript">
			// 使用 location.href 解析 contextPath 使用
			//var servletPath = "/" + location.href.split("/")[3] + "/user";
			//console.log(servletPath);

			function checkUsername() {
				var usr = document.getElementsByName("username")[0].value;
				var reg = /^[\w]{4,}$/i;
				var msg = document.getElementById("msg");

				console.log("usr:" + usr + ", type:" + (typeof usr));

				if (null === usr || "" === usr) {
					msg.innerHTML = "账户不能为空";
					return false;
				}

				if (reg.test(usr)) {
					$.ajax({
						type: "post",
						url: "../user",
						data: {
							"methodName": "validateUsr",
							"username": usr
						},
						dataType: "json",
						success: function(data) {
							console.log("checkUsername success:" + data);
							if (data == true) {
								msg.innerHTML = "";
							} else {
								msg.innerHTML = "账户不存在";
							}
						},
						error: function(data) {
							console.log("checkUsername error:" + data);
							msg.innerHTML = "服务器错误";
						}
					});
				} else {
					msg.innerHTML = "账户不合法";
				}
			}

			function submitForm() {
				var msg = document.getElementById("msg");

				$.ajax({
					type: "post",
					url: "../user",
					data: $("form").serialize(),
					dataType: "json",
					success: function(data) {
						console.log("submitForm success:" + data);
						if (data == "1") {
							//$("#msg").text("");
							msg.innerHTML = "";
							console.log("成功");
							window.location.href = "index.jsp";
						} else {
							if (data == '3') {
								//$("#msg").text("验证码错误");
								msg.innerHTML = "验证码错误";
							}
							if (data == '2') {
								// $("#msg").text("账户或者密码错误");
								msg.innerHTML = "账户或者密码错误";
							}
						}
					},
					error: function(data) {
						console.log("submitForm error:" + data);
					}
				});
				return false;
			}
		</script>

	</body>
</html>
