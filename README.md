# TicTacToe: play online with other players or against unfinished AI. Various board sizes.

## Table of contents
* [Keywords](#Keywords)
* [Project goals](#Projectgoals)
* [Architecture](#architecture)
* [Youtube-link](#link)
* [Disclaimer](#disclaimer)
* [Technical setup](#Setup)

## Keywords
WebSocket, HtmlCanvas, JavaScript with TypeScript, Vue3, vuex, Java, EJB, Dependency Injection, annotations,  MariaDB, Maven, TomEE plume 9

## Project goals

**Frontend:** 
Study html canvas using Vue wrapper with TypeScript. Using .vue files. 
**Backend:** 
Study new backend server and what is going on with latest Java versions.
**Database:** 
Create simple SQL-based database to backup the gaming.
**Have some fun!**

## Architecture

 ![tictactoe](./OverAll.png)
 
##link
www.

## Disclaimer
Use at your own risk. No warranties. 
Using, testing, playing, deploying any parts of the software is done with your own responsibility and risk. Software and instructions might contain bugs/errors.

## Technical setup
Read the whole setup first. This is not a step by step instructions for creating workspace. <br>
Even if you have done some similarish setups before there might( will) come problems to solve.

Big steps:
1. Create database with a script 
2. Setup backend dev env
3. Use browser to test current version with current UI
4. Setup UI dev env if required/ want to change UI. Publish changes to DIST-folder using npm-build.
5. Prod-publish: Copy DIST-folder to Eclipse portal-project. Deploy Portal.war and TicTacToe.war to server running prod TomEE and database.
   Set Server launch environment to  -DServer_Environment="Production" Compare to  2.7
---
1. Create database with script -> Script "LocalDbSetup.sql" is in this (main) folder
2. Backend dev environment with Eclipse <br>
2.1 Import backend projects portal and tictactoe into Eclipse as Maven-projects. 
2.1.1 Right click portal and tictactoe projects -> Maven-> Update project (to fetch configured deps) <br>
2.2 Windows-> preferences -> Validations -> Disable JavaScript validations for errors on problems tab <br>
2.3 Create server: Click Window -> Show view -> Other -> Servers -> Create new <br>
2.3.1 Attach TomEE Plume Server Files from directory where you downloaded them. https://tomee.apache.org/download.html <br>
2.3.2 ![tictactoe](./ServerInEclipse.png) <br>
2.4 Start empty server (check logs for errors, no errors should occur). <br>
2.4 Add both projects to server (restart server) <br>
2.5 Open resources.xml in both of the projects. Find "TODO password" and type your passwords from create database section. Restart server.<br>
2.6 Open browser http://localhost:8081/portal/index.html  <br>
2.7 Add Server "Open launch configuration" -DServer_Environment="Development" <br>
![tictactoe](./AddLauncConfigurationEnvironment.png)
2.8 Set Feedback file path and name. Feedbacks go to file appending it endlessly. Biggest file size not configured.
	Find FeedbackEJB.java -> set fileNameLocal and fileNameProd to point what files you want use. <br>
2.9 Set context root for "portal" and "tictactoe" projects <br>
![tictactoe](./TomEEContextRoots.png) <br>
3. Use browser to test current UI-version <br>
3.1 Open browser from address http://localhost:8081/portal/index.html
    Check that port matches your server, check logs from Eclipse if errors occur. <br>
    Check browser console if errors occur.
    UI should open up now.
3.2 Try registering new username. Username and password are not allowed to be the same. Check browser dev-tools
	that address is http://localhost:8081/portal/api/user/register  <br>
3.3 Try Login with the credentials. <br>
3.4 Click play link and check WebSocket connection is opened ws://localhost:8081/tictactoe/ws <br>

4. Setup UI dev env if required to change UI. Sections 1-3 are required to be done before to get backend services working.
  4.1 UI tries to get services from backend server created in section 2. <br>
  4.2 Run "npm install" to get packages from folder where package.json is located. Consider installing globally with -g. <br>
  4.3 Find .env.local file -> set VUE_APP_API_BASE_URL and VUE_APP_WS_URL corresponding where backend services are configured. <br>
       For example: http://localhost:8081  and websocket ws://localhost:8081/tictactoe/ws <br>
  4.3.1 Backend should give "CORS-headers" accordingly, browser makes http OPTIONS call since port 8080!=8081
  
  4.4 Run "npm run serve -o" to start dev server. Vue-App runs default at http://localhost:8080/ (note port) <br>
  4.5 Since Now running UI in port 8080 -> Backend given cors headers do not match. Find Constants.java file
      In Eclipse, click ctrl+shift+T -> Type Constants.java and select one from TicTacToe project.
	  Change variable to WEBSOCKET_LOCALHOST_ORIGIN = "http://localhost:8080"; note the port 8080. Restart backend server.
  4.6 Make wanted changes -> For example Home.vue texts changes.
  4.7 To build package in order to use it prod environment run "npm run build --prod". Copy created Dist folder contents
	  to Eclipse Portal-project and delete old/unnecessery UI-files.

5. Prod deployment from Eclipse: 
	5.1 Select portal-project ->  File -> Export -> war-file. Check contents inside .war file are in correct places. 
	5.2 Select tictactoe-project >  File -> Export -> war-file. Check contents inside .war file are in correct places.
	5.3 Double check inner contents of both .war files, that they don't contain unwanted files and that files are
	 in correct places. Source-files should not be in .war file and classes should be WEB-INF/lib folder.
	5.4 Move the .war files to production server -> test.
