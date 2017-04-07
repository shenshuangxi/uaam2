<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="http://localhost:8080${pageContext.request.contextPath}/" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="data:;base64,=" />
<link rel="stylesheet" type="text/css" href="css/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="css/easyui/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="css/easyui/demo.css"/>
<script type="text/javascript" src="js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="js/easyui/jquery.easyui.min.js"></script>

</head>
<body>
	<div id="organmanager" class="easyui-layout"style="width: 100%;height: 100%">
		<script type="text/javascript">
			function tipsATag(value,row,index){
				if(!value){
					value = '';
				}
				return '<a  style=\'text-decoration: none;color: black;\' title=\''+value+'\' class=\'easyui-tooltip\'>'+value+'</a>'
			}
			
			function getGroupDn(){
				var rows = $('#orgGroups').datagrid('getChecked');
				if(rows)
				{
					if(rows.length == 0)
					{
						return null;
					}
					return rows[0].dn;
				}
			}
			
			function search(){
				var suffixValue = $('#search').textbox('getValue');
            	if(suffixValue==null||suffixValue==''){
            		alert('请输入搜索值');
            		return;
            	}
				var headValue = $('#headSearch').combobox('getValue');
				var headText = $('#headSearch').combobox('getValue');
				var middleValue = $('#middleSearch').combobox('getValue');
				var middleText = $('#middleSearch').combobox('getValue');
				
				if(headValue=='user'){
					$('#orgUsers').datagrid('options').url = 'ldap/user/searchUser?time='+new Date().getTime()+'&'+middleValue+'='+encodeURIComponent(suffixValue);
					$('#orgUsers').datagrid('reload');
				}else{
					$('#orgGroups').datagrid('options').url = 'ldap/group/searchGroup?time='+new Date().getTime()+'&'+middleValue+'='+encodeURIComponent(suffixValue);
					$('#orgGroups').datagrid('reload');
				}
			}
			
			$('#search').textbox({
				inputEvents: $.extend({},$.fn.textbox.defaults.inputEvents,{
					keyup: function(event){
						if(event.keyCode == 13) {
							search();}
						}
					})
				});
			
		</script>
		<div data-options="region:'north',split:false,border:false" style="height:10%;padding:10px;">
			<form >
				<input id="headSearch" class="easyui-combobox" name="headSearch" data-options="
					valueField:'id',
					textField:'text',
					editable:false,
					url:'findSearchCategory',
					onLoadSuccess:function(data){
						$('#middleSearch').combobox('reload','findSearchCategory?time='+new Date().getTime()+'&category='+data[0].id);
						$('#headSearch').combobox('select',data[0].id);
					},
					onSelect:function(record){
						$('#headSearch').combobox('setValue',record);
						$('#middleSearch').combobox('reload','findSearchCategory?time='+new Date().getTime()+'&category='+record.id); 
					}
				">
				<input id="middleSearch" class="easyui-combobox" name="middleSearch" data-options="
					valueField:'id',
					textField:'text',
					editable:false,
					onLoadSuccess:function(data){
						$('#middleSearch').combobox('select',data[0].id);
					},
					onSelect:function(record){
						$('#middleSearch').combobox('select',record.id);
					}
					">
				<input id="search" name="search"  class="easyui-textbox" data-options="icons: [{
					iconCls:'icon-search',
					handler: function(e){
						search();
					}
				}]" style="width:200px" />
			</form>
		</div>
		
		<div data-options="region:'west',title:'用户管理'" style="width:35%">
			<table id="orgUsers" class="easyui-datagrid" data-options="
				fit:true,
	      		nowrap:true,
	      		fitColumns:true,
	      		checkOnSelect:true,
	      		selectOnCheck:true,
	      		pagination:true,
	      		url:'ldap/user/getUsersByOrgDn?time='+new Date().getTime()+'&orgDn='+encodeURIComponent('${orgDn}'),
				pageList:[10,15,20,30,40,50],
	      		pageSize:15,
	      		toolbar: [{
					iconCls: 'icon-add',
					text:'添加用户',
					handler: function(){
						$('#userForm').form('load',{orgDn:'${orgDn}',status:1,isInner:1});
						$('#addUser').dialog('open');
					}
				},{
					iconCls: 'icon-edit',
					text:'修改密码',
					handler: function(){
						var rows = $('#orgUsers').datagrid('getChecked');
						if(rows)
						{
							if(rows.length == 0)
							{
								alert('请选择项');
								return ;
							}
							if(rows.length > 1)
							{
								alert('只能选择一项');
								return;
							}
							else
							{
								$('#modifPasswdForm').form('load',{login:rows[0].login});
								$('#modifyPassword').dialog('open');
							}
						}
					}
				},{
					iconCls: 'icon-no',
					text:'禁用',
					handler: function(){
						var rows = $('#orgUsers').datagrid('getChecked');
						if(rows)
						{
							if(rows.length == 0)
							{
								alert('请选择项');
								return ;
							}
							if(confirm('您确定要禁用所选用户吗？'))
							{
								var logins = new Array();
								for(var i = 0; i < rows.length;i++)
								{
									logins.push(rows[i].login);
								}
								$.post('ldap/user/forbidLdapUser?time='+new Date().getTime(),{logins:logins.join(',')},function(data){
									if(data.isSuccess){
										$('#orgUsers').datagrid('reload');
									}
									$.messager.show({
										title:'info',
										msg:data.message,
										timeout:2000,
										showType:'slide'
									});
								},'json');
							}
						}
					}
				},{
					iconCls: 'icon-ok',
					text:'启用',
					handler: function(){
						var rows = $('#orgUsers').datagrid('getChecked');
						if(rows)
						{
							if(rows.length == 0)
							{
								alert('请选择项');
								return ;
							}
							if(confirm('您确定要禁用所选用户吗？'))
							{
								var logins = new Array();
								for(var i = 0; i < rows.length;i++)
								{
									logins.push(rows[i].login);
								}
								$.post('ldap/user/enableLdapUser?time='+new Date().getTime(),{logins:logins.join(',')},function(data){
									if(data.isSuccess){
										$('#orgUsers').datagrid('reload');
									}
									$.messager.show({
										title:'info',
										msg:data.message,
										timeout:2000,
										showType:'slide'
									});
									
								},'json');
							}
						}
					}
				},{
					iconCls: 'icon-edit',
					text:'修改',
					handler: function(){
						var rows = $('#orgUsers').datagrid('getChecked');
						if(rows)
						{
							if(rows.length == 0)
							{
								alert('请选择项');
								return ;
							}
							if(rows.length > 1){
								alert('只能选择一行');
								return ;
							}
							$('#userInfoForm').form('load',{login:rows[0].login,orgDn:rows[0].baseDn,username:rows[0].username,mail:rows[0].email,mobile:rows[0].mobile,status:rows[0].status,isInner:rows[0].isInner});
							$('#modifyUserInfo').dialog('open');
						}
					}
				}]
			">
				<thead>
					<tr>
						 <th field="id" checkbox="true" width="50">编号</th>
						 <th field="orgName" width="100" data-options="formatter: tipsATag">部门</th>
						 <th field="username" width="60" data-options="formatter: tipsATag">姓名</th>
						 <th field="login" width="100" data-options="formatter: tipsATag">账号</th>
						 <th field="email" width="100" data-options="formatter:tipsATag">邮件</th>
						 <th field="mobile" width="120" data-options="formatter: tipsATag">手机号</th>
						 <th field="baseDn" width="100" data-options="formatter: tipsATag">路径</th>
						 <th field="status" width="40" data-options="formatter: function(value,row,index){
							if(value == 0)
								return '是';
							else
								return '否';
						}">禁止</th>
						<th field="isInner" width="60" data-options="formatter: function(value,row,index){
							if(value == 1)
								return '是';
							else
								return '否';
						}">内部用户</th>
					 </tr>
			    </thead>
			</table>
		</div>
	 	
	 	<div data-options="region:'center',title:'分组管理'"  >
			<table id="orgGroups" class="easyui-datagrid" style="width:30%;" data-options="fit:true,fitColumns:true,modal: true,url:'ldap/group/getGroupsByOrgDn?time='+new Date().getTime()+'&orgDn='+encodeURIComponent('${orgDn}'),
			checkOnSelect:true,selectOnCheck:true,singleSelect:true,
			pagination:true,pageList:[10,15,20,30,40,50],pageSize:15,
			onClickRow:function(index,row){
				$('#groupMember').datagrid('options').url = 'ldap/user/getUsersByGroupDn?time='+new Date().getTime()+'&groupDn='+encodeURIComponent(row.dn);
				groupDn = row.dn;
				$('#groupMember').datagrid('reload');
			},
			toolbar: [{
				iconCls: 'icon-add',
				text:'新增',
				handler: function(){
					$('#groupForm').form('load',{orgDn:'${orgDn}'})
					$('#group').dialog('open');
				}
			},'-',{
				iconCls: 'icon-remove',
				text:'删除',
				handler: function(){
					var rows = $('#orgGroups').datagrid('getChecked');
					if(rows)
					{
						if(rows.length == 0)
						{
							alert('请选择项');
							return ;
						}
						if(confirm('您确定要删除所选组吗？'))
						{
							$.post('ldap/group/delLdapGroup?time='+new Date().getTime(),{groupDn:rows[0].dn},function(data){
								if(data.isSuccess){
									$('#orgGroups').datagrid('reload');
									$('#groupMember').datagrid('reload');
								}
								$.messager.show({
									title:'info',
									msg:data.message,
									timeout:2000,
									showType:'slide'
								});
								
							},'json');
						}
					}
				}
			}]">
			    <thead>
			        <tr>
			        	<th field="dn" checkbox="true" width="50">编号</th>
			        	<th field="orgName" width="80" data-options="formatter: tipsATag">部门</th>
			        	<th field="groupName" width="60" data-options="formatter: tipsATag">组名</th>
			        	<th field="description" width="120" data-options="formatter: tipsATag">描述</th>
			        	<th field="baseDn" width="120" data-options="formatter: tipsATag">路径</th>
			    </thead>
			</table>
	 	</div>
	 	
	 	
	 	<div id="addUser"  class="easyui-dialog"  data-options="title:'添加用户',width:600,height:350,modal: true,closed:true,closeable:true,buttons:[{
			text:'提交',
			handler:function(){
				 $.ajax({
					url: 'ldap/user/addUserToOrgDn?time='+new Date().getTime(),
					type: 'post',
					data: $('#userForm').serialize(),
					dataType:'json',
					success:function(data){
						if(data.isSuccess){
							$('#userForm').form('load',{username:'',mail:'',pwd:'',mobile:'',status:1,isInner:1});
							$('#addUser').dialog('close');
							$('#orgUsers').datagrid('reload');
						}
						$.messager.show({
								title:'info',
								msg:data.message,
								timeout:2000,
								showType:'slide'
							});
					},
					error:function(e){
						console.log(e);
					}
				});
			}
		},{
			text:'关闭',
			handler:function(){
				$('#userForm').form('load',{username:'',mail:'',pwd:'',mobile:'',status:1,isInner:1});
				$('#addUser').dialog('close');
			}
		}]">
			<div style="padding:50px 150px">
				<form id="userForm">
					<div>
						<label for="org">部门选择:</label>
						<input class="easyui-combotree" type="text" name="orgDn" value='${orgDn}' id="orgSelect"  data-options="
							required:true,
							url: 'ldap/organization/getChildLdapOrganzationsByDn?time='+new Date().getTime(),
							formatter: function(node){
								return node.orgName;
							},
							onBeforeLoad:function(node,param){
								if(node!=null){
									var orgSelectTree = $('#orgSelect').combotree('tree');
									orgSelectTree.tree('options').url = 'ldap/organization/getChildLdapOrganzationsByDn?time='+new Date().getTime()+'&dn='+encodeURIComponent(node.dn);
								}
							},
							onClick: function(node){
								$('#orgSelect').combotree('setValue',node.dn);
							}" />
					</div>
				    <div>
				    	<label for="isInner">用户类型:</label>
				    	<select class="easyui-combobox" name="isInner" style="width:150px;">
						    <option selected="selected" value="1">内部用户</option>
						    <option value="0">外部用户</option>
						</select>
				    </div>
				    <div>
				    	<label for="status">用户状态:</label>
				    	<select class="easyui-combobox" name="status" style="width:60px;">
						    <option selected="selected" value="1">启用</option>
						    <option value="0">禁用</option>
						</select>
				    </div>
				    <div>
				        <label for="username">用户名:</label>
				        <input class="easyui-textbox" type="text" name="username"  data-options="required:true" />
				    </div>
				    <div>
				        <label for="password">密码:</label>
				        <input class="easyui-textbox" type="password" name="pwd" />
				    </div>
				    <div>
				        <label for="mail">邮件:</label>
				        <input class="easyui-textbox" type="text" name="mail"  />
				    </div>
				     <div>
				        <label for="mobile">手机号:</label>
				        <input class="easyui-textbox" type="text" name="mobile"  />
				    </div>
				</form>
			</div>
		</div>
	 	
	 	
	 	<div data-options="region:'east',title:'组员管理',split:false" style="width:35%;">
	 		<table id="groupMember" class="easyui-datagrid" data-options="fit:true,url:'',nowrap:true,fitColumns:true,checkOnSelect:true,selectOnCheck:true,pagination:true,pageList:[10,15,20,30,40,50],pageSize:15,toolbar: [{
				iconCls: 'icon-add',
				text:'添加',
				handler: function(){
					var groupDn = getGroupDn();
					if(groupDn==null){
						alert('请选择一个分组');
						return;
					}
					$('#searchUser').dialog('open');
				}
			},'-',{
				iconCls: 'icon-remove',
				text:'删除',
				handler: function(){
					var rows = $('#groupMember').datagrid('getChecked');
					if(rows)
					{
						if(rows.length == 0)
						{
							alert('请选择项');
							return ;
						}
						if(confirm('您确定要删除所选分组成员吗？'))
						{
							var memberUids = new Array();
							for(var i = 0; i < rows.length;i++)
							{
								memberUids.push(rows[i].login);
							}
							console.log(rows);
							$.post('ldap/user/delUsersByGroupDn?time='+new Date().getTime(),{groupDn:getGroupDn(),memberUids:memberUids.join(',')},function(data){
								if(data.isSuccess){
									$('#groupMember').datagrid('reload');
								}
								$.messager.show({
									title:'info',
									msg:data.message,
									timeout:2000,
									showType:'slide'
								});
								
							},'json');
						}
					}
				}
			}]"  >
				<thead>
					<tr>
						 <th field="id" checkbox="true" width="50">编号</th>
						 <th field="orgName" width="100" data-options="formatter: tipsATag">部门</th>
						 <th field="username" width="60" data-options="formatter: tipsATag">姓名</th>
						 <th field="login" width="120" data-options="formatter: tipsATag">账号</th>
						 <th field="email" width="100" data-options="formatter:tipsATag">邮件</th>
						 <th field="mobile" width="120" data-options="formatter: tipsATag">手机号</th>
						 <th field="baseDn" width="100" data-options="formatter: tipsATag">路径</th>
						 <th field="status" width="40" data-options="formatter: function(value,row,index){
							if(value == 0)
								return '是';
							else
								return '否';
						}">禁止</th>
						<th field="isInner" width="60" data-options="formatter: function(value,row,index){
							if(value == 1)
								return '是';
							else
								return '否';
						}">内部用户</th>
					 </tr>
			    </thead>
			</table>
	 	</div>
	 	
	 	
	 	<div class="easyui-dialog" id="searchUser" data-options="width:500,height:250,modal: true,title:'添加分组用户',closed:true,buttons:[{
	 		text:'完成',
	 		handler:function(){
	 			var copmponts = $('#searchUserCombobox').combobox('getData');
				var memberUid;
				$.each(copmponts,function(i,n){
					if(n.value == $('#searchUserCombobox').combobox('getValue'))
					{
						console.log(n);
						memberUid = n.value;
					}
				});
	 			$.ajax({
					url: 'ldap/user/addUserToGroup?time='+new Date().getTime()+'&groupDn='+encodeURIComponent(getGroupDn())+'&memberUid='+memberUid,
					type: 'post',
					dataType:'json',
					success:function(data){
						if(data.isSuccess){
							$('#searchUser').dialog('close');
							$('#groupMember').datagrid('reload');
						}
						$.messager.show({
								title:'info',
								msg:data.message,
								timeout:2000,
								showType:'slide'
							});
					},
					error:function(e){
						console.log(e);
					}
				});
	 		}
	 	}]">
			<div style="padding-top:25px" align="center">
				请输入用户账号：<input class="easyui-combobox" id="searchUserCombobox" data-options="valueField:'value',textField:'text',multiple:false,delay:600,keyHandler:{
					up: function(e){},
					down: function(e){},
					left: function(e){},
					right: function(e){},
					enter: function(e){},
					query: function(q,e){
						if(q && q != '')
						{
							$('#searchUserCombobox').combobox('reload','ldap/user/findUser?time='+new Date().getTime()+'&pageRow=50&login='+q);
							$('#searchUserCombobox').combobox('setValue',q);
						}
					}
				}
				"/>
			</div>
		</div>
	 	
	 	
	 	<div id="group"  class="easyui-dialog"  data-options="title:'部门分组',width:600,height:300,modal: true,closed:true,closeable:true,buttons:[{
			text:'保存',
			handler:function(){
				var groupName = $('#groupName').val();
				console.log(groupName);
				if(groupName == '')
				{
					alert('请输入新分组名称');
					return;
				}
				 $.ajax({
					url: 'ldap/group/addLdapGroup?time='+new Date().getTime(),
					type: 'post',
					data: $('#groupForm').serialize(),
					dataType:'json',
					success:function(data){
						if(data.isSuccess){
							$('#groupForm').form('clear');
							$('#group').dialog('close');
							$('#orgGroups').datagrid('reload');
						}
						$.messager.show({
								title:'info',
								msg:data.message,
								timeout:2000,
								showType:'slide'
							});
					},
					error:function(e){
						console.log(e);
					}
				});
			}
		},{
			text:'关闭',
			handler:function(){
				$('#group').dialog('close');
			}
		}]">
			<div style="padding:10px 177px 10px 177px">
				<form id="groupForm">
					<div>
						<label for="org">部门选择:</label>
						<input class="easyui-combotree" type="text" name="orgDn" value='${orgDn}' id="groupSelect"  data-options="
							required:true,
							url: 'ldap/organization/getChildLdapOrganzationsByDn?time='+new Date().getTime(),
							formatter: function(node){
								return node.orgName;
							},
							onBeforeLoad:function(node,param){
								if(node!=null){
									var groupSelectTree = $('#groupSelect').combotree('tree');
									groupSelectTree.tree('options').url = 'ldap/organization/getChildLdapOrganzationsByDn?time='+new Date().getTime()+'&dn='+encodeURIComponent(node.dn);
								}
							},
							onClick: function(node){
								$('#groupSelect').combotree('setValue',node.dn);
							}" />
					</div>
					<div>
				        <label for="name">分组名称:</label>
				        <input class="easyui-textbox" type="text" name="groupName" id="groupName" data-options="required:true" />
				    </div>
				    <div>
				        <label for="description">分组描述:</label>
				        <input class="easyui-textbox" type="text" name="description"  />
				    </div>
				</form>
			</div>
		</div>
	 	
	 	
	 	<div id="modifyPassword"  class="easyui-dialog"  data-options="title:'修改密码',width:600,height:300,modal: true,closed:true,closeable:true,buttons:[{
			text:'保存',
			handler:function(){
				var new_password = $('#new_password').val();
				var re_new_password = $('#re_new_password').val();
				console.log(new_password);
				if(new_password == '')
				{
					alert('请输入新密码');
					return;
				}
				if(re_new_password == '')
				{
					alert('请再次输入新密码');
					return;
				}
				if(new_password != re_new_password)
				{
					alert('两次输入的密码不相符');
					$('#re_new_password').val('');
					return;
				}
				 $.ajax({
					url: $('#modifPasswdForm').attr('action'),
					type: $('#modifPasswdForm').attr('method'),
					data: $('#modifPasswdForm').serialize(),
					dataType:'json',
					success:function(data){
						if(data.isSuccess){
							$('#modifPasswdForm').form('clear');
							$('#modifyPassword').dialog('close');
						}
						$.messager.show({
								title:'info',
								msg:data.message,
								timeout:2000,
								showType:'slide'
							});
					},
					error:function(e){
						console.log(e);
					}
				});
			}
		},{
			text:'关闭',
			handler:function(){
				$('#modifPasswdForm').form('clear');
				$('#modifyPassword').dialog('close');
			}
		}]">
			<div style="padding:10px 177px 10px 177px">
				<form id="modifPasswdForm"  method="post" action='ldap/user/modifyLdapUserPasswrod?time='+new Date().getTime() >
					<table>
						<tr>
							<td>用户账号:</td>
							<td>
								<input  type="text" name="login"  readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<td align="right">新密码:</td>
							<td>
								<input  type="password" name="pwd" id="new_password" />
							</td>
						</tr>
						<tr>
							<td>再次输入:</td>
							<td>
								<input type="password" name="re_new_password" id="re_new_password" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
 	</div>
 	
 	
 	<div id="modifyUserInfo"  class="easyui-dialog"  data-options="title:'修改用户信息',width:600,height:300,modal: true,closed:true,closeable:true,buttons:[{
			text:'保存',
			handler:function(){
				 $.ajax({
					url: 'ldap/user/modifyLdapUserInfo?time='+new Date().getTime(),
					type: 'post',
					data: $('#userInfoForm').serialize(),
					dataType:'json',
					success:function(data){
						if(data.isSuccess){
							$('#modifyUserInfo').dialog('close');
							$('#orgUsers').datagrid('reload');
						}
						$.messager.show({
								title:'info',
								msg:data.message,
								timeout:2000,
								showType:'slide'
							});
					},
					error:function(e){
						console.log(e);
					}
				});
			}
		},{
			text:'关闭',
			handler:function(){
				$('#modifyUserInfo').dialog('close');
			}
		}]">
			<div style="padding:50px 150px">
				<form id="userInfoForm">
					<div>
				    	<label for="login">账号:</label>
				    	<input class="easyui-textbox" type="text" name="login"  readonly="readonly" />
				    </div>
					<div>
						<label for="org">部门选择:</label>
						<input class="easyui-combotree" type="text" name="orgDn" value='${orgDn}' id="orgSelect1"  data-options="
							required:true,
							url: 'ldap/organization/getChildLdapOrganzationsByDn',
							formatter: function(node){
								return node.orgName;
							},
							onBeforeLoad:function(node,param){
								if(node!=null){
									var orgSelectTree = $('#orgSelect1').combotree('tree');
									orgSelectTree.tree('options').url = 'ldap/organization/getChildLdapOrganzationsByDn?time='+new Date().getTime()+'&dn='+encodeURIComponent(node.dn);
								}
							},
							onClick: function(node){
								$('#orgSelect1').combotree('setValue',node.dn);
							}" />
						
					</div>
				    <div>
				    	<label for="isInner">用户类型:</label>
				    	<select class="easyui-combobox" name="isInner" style="width:150px;">
						    <option selected="selected" value="1">内部用户</option>
						    <option value="0">外部用户</option>
						</select>
				    </div>
				    <div>
				    	<label for="status">用户状态:</label>
				    	<select class="easyui-combobox" name="status" style="width:60px;">
						    <option selected="selected" value="1">启用</option>
						    <option value="0">禁用</option>
						</select>
				    </div>
				    <div>
				        <label for="username">用户名:</label>
				        <input class="easyui-textbox" type="text" name="username"  data-options="required:true" />
				    </div>
				    <div>
				        <label for="mail">邮件:</label>
				        <input class="easyui-textbox" type="text" name="mail"  />
				    </div>
				     <div>
				        <label for="mobile">手机号:</label>
				        <input class="easyui-textbox" type="text" name="mobile"  />
				    </div>
				</form>
			</div>
		</div>
 	</div>
 	
 	<script type="text/javascript">
 		var groupDn;
 	</script>

</body>
</html>