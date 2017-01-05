' VBScript source code

currentPath = createobject("Scripting.FileSystemObject").GetFile(Wscript.ScriptFullName).ParentFolder.Path
Set fso=CreateObject("Scripting.FileSystemObject")
Set objPhotoshopApp=CreateObject("Photoshop.Application")

sourceFileCount = 0
cutFileCount = 0

'//jpg
sourcePath = currentPath & "\source"
cutPath = currentPath & "\cut"
'Wscript.echo preJpgPath  ����Ի��򣬵�����
 


Call ExecuteCMD()
'//ִ�в���
Sub ExecuteCMD()
    Call InitEnv(sourcePath)
    Call InitPhotoshop(objPhotoshopApp)     '//��ʼ��PS
	call ExecuteCut()
    Call LoopExecuteFinish(cutPath)
End Sub

 
'//��ʼ������
Sub InitEnv(prePath)
	sourceFileCount = GetFileCount(prePath)
End Sub
 
'//��ʼ��Photoshop���ر������Ѵ򿪵��ĵ�
Sub InitPhotoshop(oPhotoshop)
    Do While oPhotoshop.Documents.Count          
        oPhotoshop.ActiveDocument.Close
    Loop
End Sub 

'//��ȡ�ļ������ļ�����
Function  GetFileCount(path)
	set fs=fso.getfolder(path).files
	for each f in fs
		GetFileCount = GetFileCount + 1
    Next
End Function 

Sub ExecuteCut()
	'//����PS������������
	Set objShell = CreateObject("Wscript.Shell")
    WScript.Sleep 3000
	objShell.SendKeys "%F"
	objShell.SendKeys "{U}"
	objShell.SendKeys "{B}"
	
	
	'//ѡ�����
	objShell.SendKeys "{TAB}"
	'��Ϣ1S�ٷ��ͷ������������Ч
	WScript.Sleep 1000
	objShell.SendKeys "{DOWN}"
	objShell.SendKeys "{DOWN}"
	objShell.SendKeys "{DOWN}"
	objShell.SendKeys "{DOWN}"
	
	'//ִ�ж���
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


'//�˳�Photoshop
Sub ExitPhotoshop(oPhotoshop)
    oPhotoshop.Quit
End Sub


Sub InvokeBat() 
    Set shell=Wscript.createobject("wscript.shell")
    a=shell.run("ѹ��.bat",0)
End Sub