Made in IntelliJ IDE
Java Version 11.0.2
working directory:   csc413-secondgame-hasanberacoskun\jar\csc413-secondgame-hasanberacoskun.jar

To run the game, run csc413-secondgame-hasanberacoskun.jar

--------------------------
CONTROlS
-------------------------
up- forward
down- backward
left- left
right- right

--------------------------
RULES
--------------------------
Objective: Get all roombas safely into the exits and complete all levels.

1. Avoid saws and TNT. You lose the game and need to restart the game once a roomba dies!
2. Move on a red button (switch) to open the corresponding locked block. You need to be lined up well
   to be able to pass through the block once it has been unlocked.
3. You can push the block with 4 arrows on it around (boulder). It will destroy saws and TNT!
4. A red exit can accept three roombas.
5. A blue exit can only accept one roomba. It will close when full.
6. Roombas will all move at the same time unless they are colliding with something!

--------------------------
MAPS
--------------------------
<> To edit map, add to map0.txt in resources folder. Feel free to add more maps files. There can be as many 
   maps as you would like. Each text file will be a new level. 
<> Maps should be named consecutively (Ex: map0.txt, map1.txt, map2.txt, map3.txt etc...).
<> Maps can be scaled up by adding as many blocks as you would like. The world will automatically scale.

Block Guide for Making Maps:
'W' --> Wall
'H' --> Blade (moves horizontally back and forth)
'V' --> Blade (moves vertically back and forth)
'B' --> Boulder
'T' --> TNT
'#' --> Exit (accepts 3 roombas)
'$' --> Exit (accepts 1 roomba)
'1' --> Roomba 1
'2' --> Roomba 2
'3' --> Roomba 3
Note: ALL 3 ROOMBA'S MUST BE ADDED TO THE MAP.
<> Lowercase letters are used for locks and switches.
<> Every other lowercase letter is a lock. The consecutive letter is its corresponding switch. 
Ex: 'a'--> Lock and 'b'--> Switch (these two are a pair since they are consecutive). 
Ex: 'e'--> Lock and 'f' --> Switch (these two are also a pair since they are consecutive). 