@echo off
echo ********************* slice2java **************************
set  javaFile_des=..\..\java

echo rd /s /Q %javaFile_des%
md %javaFile_des%


for /R ./ %%s in (*.ice) do (
	slice2java --output-dir %javaFile_des% -I. %%s
	echo %%s 
) 
pause