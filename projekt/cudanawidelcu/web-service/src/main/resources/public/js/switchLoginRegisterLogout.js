function getCookie(name) {
    let cookieArr = document.cookie.split(";");
    for (let i = 0; i < cookieArr.length; i++) {
        let cookiePair = cookieArr[i].split("=");
        if (name === cookiePair[0].trim()) {
            return decodeURIComponent(cookiePair[1]);
        }
    }
    return null;
}
document.addEventListener('DOMContentLoaded', function () {


    let jwtToken = getCookie("jwtToken");
    if (jwtToken) {
        document.getElementById('login-item').style.display = 'none';
        document.getElementById('register-item').style.display = 'none';
        document.getElementById('logout-item').style.display = 'block';
    } else {
        document.getElementById('login-item').style.display = 'block';
        document.getElementById('register-item').style.display = 'block';
        document.getElementById('logout-item').style.display = 'none';
    }
});