<html>
<head>
    <title>${(project.name)!}</title>
    <meta charset="utf-8">
    <style>
    span{
       height: 0px;
       overflow: hidden;
       display: none;
    }
    input:checked~span{
        overflow: visible;
       	display: inline;
    }
    tr{
    	vertical-align: top;
    }
    </style>
</head>
<body>

<#if project.description??>
<div>
${project.description}
</div>
</#if>

<table>
    <tr>
        <td>Module</td><td>Name</td><td>Status</td><td>Detail</td>
    </tr>
    <#list reports as report>
    <tr>
        <td>${report.moduleName}</td><td>${(report.methodName)!}</td><td>${(report.status)!}</td>
        <td><input type="checkbox">
            <span>
                <#list report.actions as action>
                <div>${action.type}-${action.description}-${(action.value)!}</div>
                </#list>
                <pre>${(report.detail)!}</pre>
            </span>
        </td>
    </tr>
    </#list>
</table>

</body>
</html>
