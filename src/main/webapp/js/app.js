const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

sign_up_btn.addEventListener('click',()=>{
    container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener('click',()=>{
    container.classList.remove("sign-up-mode");
});

function checkForm(){
    var userName=document.getElementById("userName").value;
    var password=document.getElementById("password").value;
    if(userName==null || userName==""){
        document.getElementById("error").innerHTML="用户名不能为空";
        return false;
    }
    if(password==null || password==""){
        document.getElementById("error").innerHTML="密码不能为空";
        return false;
    }
    return true;
}
