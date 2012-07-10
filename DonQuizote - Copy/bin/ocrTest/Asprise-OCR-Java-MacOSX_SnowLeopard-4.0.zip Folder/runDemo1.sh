echo 
echo --- A general sample with both characters and barcodes ---
echo 
export LD_LIBRARY_PATH=`pwd`:$LD_LIBRARY_PATH
java -classpath .:aspriseOCR.jar:demo.jar com.asprise.util.ocr.demo.Demo sample-images/ocr.gif
		
