export LD_LIBRARY_PATH=`pwd`:$LD_LIBRARY_PATH
java -Xms30M -Xmx500M -classpath .:aspriseOCR.jar:demo.jar:UI-Eval/jid.jar:UI-Eval/JTwain.jar com.asprise.util.ocr.demo.DemoUI
		
