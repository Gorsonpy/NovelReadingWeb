function logIn() {
    var verifyUrl = "/user/login";
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var params = {}
    params.username = username;
    params.password = password;
    console.log(params);
    $.ajax({
        type: 'post',
        url: verifyUrl,
        data: params,
        success: function (data) {
            if (data.result == "SUCCESS") {
                window.location.href = "a.html";
                alert("登录成功");
            } else {
                alert("账号或密码错误，登陆失败");
            }
        },

    })
};