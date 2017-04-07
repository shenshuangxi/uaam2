<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="http://localhost:8080${pageContext.request.contextPath}/" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>开发工具</title>
<link rel="icon" href="data:;base64,=">
<link rel="stylesheet" type="text/css" href="css/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="css/easyui/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="css/easyui/demo.css"/>
<script type="text/javascript" src="js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="js/easyui/jquery.easyui.min.js"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',title:'',split:true" style="height:60px;background:#B3DFDA;padding:10px"></div>
	<div data-options="region:'west',title:'操作菜单',split:true" style="width:300px;padding:10px;">
		<ul id="orgs" class="easyui-tree" data-options="
				url: 'ldap/organization/getChildLdapOrganzationsByDn?time='+new Date().getTime(),
				method: 'get',
				animate: true,
				formatter: function(node) {
				  return node.orgName;
				},
				onBeforeLoad: function(node, param){
					if(node!=null){
						$('#orgs').tree('options').url = 'ldap/organization/getChildLdapOrganzationsByDn?time='+new Date().getTime()+'&dn='+encodeURIComponent(node.dn);
					}
				},
				onContextMenu:function(e,node){
					operatorNode = node;
					var mm = $('#mm');
					mm.menu('show', {
					    left: e.pageX,
					    top: e.pageY
					});
					e.preventDefault();
				},
				onClick: function(node){
					console.log(node);
					if ($('#tabs').tabs('exists','部门管理')){
						$('#tabs').tabs('select', '部门管理');
						var tab = $('#tabs').tabs('getSelected');
						tab.panel('refresh', 'orgInfoPage?time='+new Date().getTime()+'&orgDn='+encodeURIComponent(node.dn));
					} else {
						$('#tabs').tabs('add',{
							title: '部门管理',
							href: 'orgInfoPage?time='+new Date().getTime()+'&orgDn='+encodeURIComponent(node.dn),
							closable:true,
						});
					}
				}
			"></ul>
			
			
			<div id="mm" class="easyui-menu" style="width:120px;">
				<div data-options="iconCls:'icon-add'"  onclick="addOrg()" >添加</div>
				<div data-options="iconCls:'icon-add'"  onclick="modifyOrg()" >修改</div>
			    <div data-options="iconCls:'icon-remove'" onclick="removeOrg()" >删除</div>
			</div>
			
			<div id="org"  class="easyui-dialog"  data-options="title:'组织',width:600,height:300,closed:true,modal: true,closeable:true,buttons:[{
			text:'保存',
			handler:function(){
				var orgName = $('#orgName').val();
				if(orgName == '')
				{
					alert('请输入新分组名称');
					return;
				}
				var orgdn = $('#orgdn').val();
				var reloadNode = operatorNode;
				console.log(orgdn);
				if(orgdn!=''){
					reloadNode = $('#orgs').tree('getParent',reloadNode.target);
					console.log(reloadNode)
				}
				console.log(reloadNode);
				 $.ajax({
					url: 'ldap/organization/saveLdapOrganzation?time='+new Date().getTime()+'&baseDn='+encodeURIComponent(operatorNode.dn),
					type: 'post',
					data: $('#organizationForm').serialize(),
					dataType:'json',
					success:function(data){
						if(data.isSuccess){
							$('#organizationForm').form('clear');
							$('#org').dialog('close');
							refreshTargetNode(reloadNode);
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
				$('#organizationForm').form('clear');
				$('#org').dialog('close');
			}
		}]">
			<div style="padding:10px 177px 10px 177px">
				<form id="organizationForm">
				     <input class="easyui-textbox" type="hidden" name="dn" id="orgdn" readonly="readonly" />
					<div>
				        <label for="orgName">组织名称:</label>
				        <input class="easyui-textbox" type="text" name="orgName" id="orgName" data-options="required:true" />
				    </div>
				    <div>
				        <label for="description">组织描述:</label>
				        <input class="easyui-textbox" type="text" name="description"  id="description" />
				    </div>
				</form>
			</div>
		</div>
			
			<script type="text/javascript">
				var operatorNode = null;
				function addOrg(){
					$('#org').dialog('open');
				}
				function modifyOrg(){
					$('#organizationForm').dialog('load',"orgInfo?time='+new Date().getTime()+'&dn="+encodeURIComponent(operatorNode.dn))
					$('#org').dialog('open');
				}
				function removeOrg(){
					$.ajax({
						url: 'ldap/organization/hasChild?time='+new Date().getTime()+'&dn='+encodeURIComponent(operatorNode.dn),
						type: 'get',
						dataType:'json',
						success:function(data){
							if(data.isSuccess){
								var hasChild = data.entity;
								if(hasChild){
									if(confirm('该部门不为空,您确定要删除所选部门吗？')){
										$.ajax({
											url: 'ldap/organization/removeLdapOrganzation?time='+new Date().getTime()+'&dn='+encodeURIComponent(operatorNode.dn),
											type: 'get',
											dataType:'json',
											success:function(data){
												if(data.isSuccess){
													parentNode = $('#orgs').tree('getParent',operatorNode.target);
													console.log(parentNode);
													refreshTargetNode(parentNode);
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
								}
							}
						},
						error:function(e){
							console.log(e);
						}
					});
				}
				
				
				function refreshTargetNode(node){
					if(node==null){
						return;
					}
					$.ajax({
						url: 'ldap/organization/getChildLdapOrganzationsByDn?time='+new Date().getTime()+'&dn='+encodeURIComponent(node.dn),
						type: 'get',
						dataType:'json',
						success:function(data){
							//获取选中节点所有子节点，并全部删除
							var allChildren = $('#orgs').tree('getChildren',node.target);
							for(var i = 0; i < allChildren.length; i++){
								$('#orgs').tree('remove', allChildren[i].target);
							}
							//在当前节点下添加新子节点
							if(data.length>0){
								$('#orgs').tree('append', {
									parent: node.target,
									data: data
								});
							}
						},
						error:function(e){
							console.log(e);
						}
					});
				}
				
			</script>
	</div>
	<div data-options="region:'center',title:'center title'" style="padding: 5px; background: #eee;">
		<div id="tabs" class="easyui-tabs" style="width:100%;height:100%"></div>
	</div>
	<div data-options="region:'south',title:'',split:true" style="height:50px;background:#A9FACD;padding:10px;"></div>
</body>
</html>