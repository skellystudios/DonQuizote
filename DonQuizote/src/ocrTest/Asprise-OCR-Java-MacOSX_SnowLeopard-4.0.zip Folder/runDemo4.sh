echo 
echo --- Scanned document at 100 dpi \(very low resolution\)---
echo 
echo --- The fonts used in the document is very hard to recognize. 
echo --- Normal fonts like 'New Times', 'Arial' should result 
echo --- much better performance. 
echo 
export LD_LIBRARY_PATH=`pwd`:$LD_LIBRARY_PATH
java -classpath .:aspriseOCR.jar:demo.jar com.asprise.util.ocr.demo.Demo sample-images/scanned-text-100dpi.jpg
		
