Run below command to execute the program

mvn clean install exec:java -Dexec.args="./src/test/players.txt ./src/test/matchScores.txt"

Sample program cli inputs as below

Report Common Information
-R

Search by Name
-R Michale

Reload Player and Match Files
-F ./src/test/players.txt ./src/test/matchScores.txt

List Players Detail Report
-L

List Random Suggestions
-S

Exit
-E