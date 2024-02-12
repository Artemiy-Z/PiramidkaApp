call gradlew.bat build
pause
cd .\app\build\outputs\apk\debug
adb install -r -d app-debug.apk
adb shell monkey -p com.example.study4child 1
pause