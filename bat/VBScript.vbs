' VBScript source code

currentPath = createobject("Scripting.FileSystemObject").GetFile(Wscript.ScriptFullName).ParentFolder.Path
Set fso=CreateObject("Scripting.FileSystemObject")
Set objPhotoshopApp=CreateObject("Photoshop.Application")

sourceFileCount = 0
cutFileCount = 0

'//jpg
sourcePath = currentPath & "\source"
cutPath = currentPath & "\cut"
'Wscript.echo preJpgPath  输出对话框，调试用
 


Call ExecuteCMD()
'//执行步骤
Sub ExecuteCMD()
    Call InitEnv(sourcePath)
    Call InitPhotoshop(objPhotoshopApp)     '//初始化PS
	call ExecuteCut()
    Call LoopExecuteFinish(cutPath)
End Sub

 
'//初始化环境
Sub InitEnv(prePath)
	sourceFileCount = GetFileCount(prePath)
End Sub
 
'//初始化Photoshop，关闭所有已打开的文档
Sub InitPhotoshop(oPhotoshop)
    Do While oPhotoshop.Documents.Count          
        oPhotoshop.ActiveDocument.Close
    Loop
End Sub 

'//获取文件夹内文件个数
Function  GetFileCount(path)
	set fs=fso.getfolder(path).files
	for each f in fs
		GetFileCount = GetFileCount + 1
    Next
End Function 

Sub ExecuteCut()
	'//启动PS的批处理命令
	Set objShell = CreateObject("Wscript.Shell")
    WScript.Sleep 3000
	objShell.SendKeys "%F"
	objShell.SendKeys "{U}"
	objShell.SendKeys "{B}"
	
	
	'//选择分组
	objShell.SendKeys "{TAB}"
	'休息1S再发送方向键，否则不生效
	WScript.Sleep 1000
	objShell.SendKeys "{DOWN}"
	objShell.SendKeys "{DOWN}"
	objShell.SendKeys "{DOWN}"
	objShell.SendKeys "{DOWN}"
	
	'//执行动作
	WScript.Sleep 1000
	objShell.SendKeys "{ENTER}"
End Sub

Sub LoopExecuteFinish(path)
	do 
    	if 11 * sourceFileCount = cutFileCount then
		    call ExitPhotoshop(objPhotoshopApp)
			WScript.Sleep 1000
			call InvokeBat()
			Exit do
		else 
			cutFileCount = getFileCount(path)
			WScript.Sleep 1000
		end if
	loop
End Sub


'//退出Photoshop
Sub ExitPhotoshop(oPhotoshop)
    oPhotoshop.Quit
End Sub


Sub InvokeBat() 
    Set shell=Wscript.createobject("wscript.shell")
    a=shell.run("压缩.bat",0)
End Sub