// JavaScript Document
$(function(){		

// 搜索框

	$('.zhuanye  a ').click(function(event) {
		var neirong=$(this).html();
		var neirong2=$('.nr_js').html();
		$('.nr_js').html(neirong);
		$('.zhuanye  a ').html(neirong2);
	});
//	鼠标移出下拉框1秒后下拉框消失
	$('.dropdown-menu').mouseout(function(event){
		setTimeout("$('.dropdown-menu').css('display', 'none')",1000);
	});
	
	// 搜索框ajax提交
$('#sou').click(function(event){
	var major = $('.nr_js').text();
	var value = $('#scbar_txt').val();
	search_jump(major,value);
});

// 按下回车调用跳转函数至信息页
document.onkeydown = function(e)
{  
     var ev = document.all ? window.event : e;
     if(ev.keyCode==13) 
     {
       var major = $('.nr_js').text();
	   var value = $('#scbar_txt').val();
	   search_jump(major,value);	
     }
}

//确定二维码位置
var win_width=$(window).width();
$('.erwei').mouseenter(function(event) {
			$('.erwei03').css('display', 'block');
		}).mouseleave(function(event) {
			$('.erwei03').css('display', 'none');
		});
//搜索跳转
function search_jump(major,value){
	console.log(major);
	console.log(value);
	if(major=="专业"){
		major=1;
	}else{
		major=2;
	}
	window.open("search.html?t=" + major + "&c=" +value);
	console.log("????");
}

});
//页面加载函数结束

//创建注册对象
var indexregs_obj={objemail:'null',objpasd:'null'};

// 注册邮箱验证开始
	function index_email()
	{ 
	$('#input_mail2').blur(function(event) 
	{
		var index_email=$('#input_mail2').val();
		var reg =/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
		if(reg.test(index_email))
		{
		$('.zc_email').html('√邮箱合法').css('color', '#6fd415');
		// 将正确的邮箱存进注册对象
		indexregs_obj.objemail=index_email;
		// 判断是否两个输入框都合法
                        index_submit_judge(indexregs_obj.objemail,indexregs_obj.objpasd);
		}else
		{
				
		$('.zc_email').html('×邮箱不合法').css('color', 'red');
		indexregs_obj.objemail='null';
		}
		if(index_email=='')
		{
		$('.zc_email').html('×邮箱不能为空').css('color', 'red');
		indexregs_obj.objemail='null';
		}
		
	});
	
	}
// 注册邮箱验证结束
index_email();

// 注册密码验证开始
function index_pasd()
{
$('#pasd2').blur(function(event)
	 {
	 	var indexpasd2=$('#pasd2').val();
		var reg =/^[A-Za-z]\w{5,15}$/;
		if(reg.test(indexpasd2))
		{
		$('.pasd4').html('√密码合法').css('color', '#6fd415');
		// 注册确认密码
			$('#pasd3').blur(function(event)
			 {
				var indexpasd3=$('#pasd3').val();
				if(indexpasd2==indexpasd3)
				{
				$('.ecqu').html('√密码一致').css('color', '#6fd415');
				// 将正确的密码存进注册对象
				indexregs_obj.objpasd=indexpasd3;
				// 判断是否两个输入框都合法
				index_submit_judge(indexregs_obj.objemail,indexregs_obj.objpasd);

				}else
				{
				$('.ecqu').html('×两次密码输入必须一致').css('color', 'red');
				indexregs_obj.objpasd='null';
				}
			});

	            }else
		{
		$('.pasd4').html('×输入6-15位以字母开头含有字母数字的密码').css('color', 'red');
		indexregs_obj.objpasd='null';
		}
		if(indexpasd2=='')
		{
		$('.pasd4').html('×密码不能为空').css('color', 'red');
		indexregs_obj.objpasd='null';
		}
	});
}
index_pasd();
// 注册密码验证结束

// 判断注册对象是不是null
function index_submit_judge(index_email,indexpasd3)
{
if(index_email!='null'&&indexpasd3!='null')
{
index_submit_click(index_email,indexpasd3);
console.log('true');
}else
       {
// 邮箱和密码若有一个错误择提交按钮不可用
	$('#register').attr('disabled', false);
	$('#register').css('background-color', '#3276B1');
       }
}

// 点击提交按钮函数
function index_submit_click(email,pasd)
{
$('#register').click(function(event) 
{
	// 在用户名和密码都输入正确的情况下调用ajax
	  index_register_ajax(email,pasd); 
});

}


// 首页注册ajax
function index_register_ajax(email,pasd)
{
$.ajax
({
 url: 'http://www.itui.cn/register',
//url: 'regs.php?msg'+Math.random(),
type: 'post',
dataType: 'html',
data: {email: email,password:pasd},
success:function(msg)
{
data=eval('msg='+msg);
if(data.status==0)
   {
   	if(data.normalReturn.register=='false')
   	{
	$('.bt_p02').text(data.normalReturn.msg+'请重新注册').css('color', 'red');
	$('#register').attr('disabled', false);
	$('#register').css('background-color', '#3276B1');
	console.log('false');
   	}else
   	{
   	$('.bt_p02').text('先去邮箱查看邮件激活吧!').css('color', 'red');
	$('#register').attr('disabled', true);
   	$('#register').css('background-color', '#cbcbcb');
   	console.log(pasd);
     	console.log(email);
   	}
     
   }
}
});
	
}
// 注册邮箱验证
// 
// 登录模态框验证开始

//创建登录对像
var indexload_obj={objemail:'null',objpasd:'null'};
// 登录邮箱验证开始
function index_load_email()
{
$('#input_mail').blur(function(event) 
     {
	var index_loademail=$('#input_mail').val();
	var reg =/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	 if(reg.test(index_loademail))
	{
	 $('.maildiv01').html('√邮箱合法').css('color', '#6fd415');
	// 将登录邮箱存进登录对象中
	indexload_obj.objemail=index_loademail;
	index_load_judge(indexload_obj.objemail,indexload_obj.objpasd);
	}
           else
           {
            $('.maildiv01').html('×邮箱不合法');
	}
	if(index_loademail=='')
	{
	$('.maildiv01').html('×邮箱不能为空');
	}
   });	
}
index_load_email();
// 登录邮箱验证结束
// 
// 获取登录密码开始
function index_loadpasd()
{
$('#pasd').blur(function(event) 
{
var index_loadpasd1=$('#pasd').val();
// 将登录密码存进登录对象
indexload_obj.objpasd=index_loadpasd1;
index_load_judge(indexload_obj.objemail,indexload_obj.objpasd);
});	
}
 index_loadpasd();

// 获取登录密码结束
// 
// 判断登录对像是否为空开始
function index_load_judge(email,pasd)
{
 if(email!='null'&&pasd!='null')
 {
 // 点击登录按钮
  index_loadsubmit(email,pasd);
 }
 else
 {
  $('#load').attr('disabled', false);
 }
}
//判断登录对像是否为空结束 
//
//登录点击函数
function index_loadsubmit(email,pasd)
{
	$('#load').click(function(event) 
	{
	
	index_load_ajax(email,pasd);
	});
}
// 首页登录ajax
function index_load_ajax(email,pasd)
{
$.ajax({
	 url: 'http://www.itui.cn/login',
//	url: 'load.php?msg'+Math.random(),
	type: 'post',
	dataType: 'html',
	data: {email: email,password:pasd},
	success:function(msg)
	{
	data=eval('msg='+msg);
	if(data.status==0)
	{
	if(data.normalReturn.login=='failure')
	{
	  $('#load').attr('disabled', false);
  	$('.bt_p01').text(data.normalReturn.msg+'请重新登录！').css('color', 'red');
  	console.log(email);
	console.log(pasd);
	}else
	{
	$('#load').attr('disabled', true);
  	$('#load').css('background-color', '#cbcbcb');
	$('.bt_p01').text('恭喜你登录成功了！').css('color', 'red');
	}
	
	}
	}
});

}

// 登录模态框验证结束


	



	
